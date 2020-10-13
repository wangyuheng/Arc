package ai.care.arc.generator.codegen;

import ai.care.arc.core.util.StreamUtils;
import ai.care.arc.dgraph.annotation.DgraphType;
import ai.care.arc.dgraph.annotation.UidField;
import ai.care.arc.dgraph.dictionary.IDgraphType;
import ai.care.arc.generator.codegen.spec.FieldSpecGenGetter;
import ai.care.arc.generator.codegen.spec.FieldSpecGenSetter;
import ai.care.arc.generator.codegen.spec.SpecNameManager;
import ai.care.arc.generator.convert.GraphqlType2JavapoetTypeName;
import ai.care.arc.generator.convert.IsContainsGraphqlMethodField;
import ai.care.arc.generator.convert.IsGraphqlMethodField;
import ai.care.arc.generator.convert.IsOperator;
import ai.care.arc.generator.dictionary.GeneratorGlobalConst;
import ai.care.arc.graphql.annotation.DataFetcherService;
import ai.care.arc.graphql.annotation.GraphqlMethod;
import ai.care.arc.graphql.util.GraphqlTypeUtils;
import com.squareup.javapoet.*;
import graphql.language.Description;
import graphql.language.FieldDefinition;
import graphql.language.ObjectTypeDefinition;
import graphql.language.UnionTypeDefinition;
import graphql.schema.DataFetcher;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 根据 {@link ObjectTypeDefinition} 生成 {@link ai.care.arc.generator.conf.CodeGenType#TYPE} 代码
 *
 * @author yuheng.wang
 */

public class TypeGenerator implements IGenerator {

    private static final String DEFAULT_UID_FIELD_NAME = "id";
    private final FieldSpecGenGetter fieldSpecGenGetter = new FieldSpecGenGetter();
    private final FieldSpecGenSetter fieldSpecGenSetter = new FieldSpecGenSetter();
    private final Predicate<ObjectTypeDefinition> isContainGraphqlMethodField = new IsContainsGraphqlMethodField();
    private final Predicate<FieldDefinition> isGraphqlMethodField = new IsGraphqlMethodField();
    private final GraphqlType2JavapoetTypeName toJavapoetTypeName;
    private PackageManager packageManager;

    public TypeGenerator(PackageManager packageManager) {
        this.packageManager = packageManager;
        this.toJavapoetTypeName = new GraphqlType2JavapoetTypeName(packageManager);
    }

    @Override
    public Stream<JavaFile> apply(TypeDefinitionRegistry typeDefinitionRegistry) {
        final Predicate<ObjectTypeDefinition> isOperator = new IsOperator(GraphqlTypeUtils.getOperationTypeNames(typeDefinitionRegistry));

        return StreamUtils.merge(
                typeDefinitionRegistry.getTypes(ObjectTypeDefinition.class).stream()
                        .filter(isOperator.negate())
                        .map(new TypeBuilder())
                        .map(it -> JavaFile.builder(packageManager.getTypePackage(), it.build()).build()),
                typeDefinitionRegistry.getTypes(ObjectTypeDefinition.class).stream()
                        .filter(isOperator)
                        .map(new OperatorBuilder())
                        .map(it -> JavaFile.builder(packageManager.getTypePackage(), it.build()).build()),
                typeDefinitionRegistry.getTypes(UnionTypeDefinition.class).stream()
                        .map(new UnionBuilder())
                        .map(it -> JavaFile.builder(packageManager.getTypePackage(), it.build()).build())
        );
    }

    class UnionBuilder implements Function<UnionTypeDefinition, TypeSpec.Builder> {

        @Override
        public TypeSpec.Builder apply(UnionTypeDefinition unionTypeDefinition) {
            return TypeSpec.classBuilder(unionTypeDefinition.getName())
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addJavadoc(Optional.ofNullable(unionTypeDefinition.getDescription()).map(Description::getContent).orElse(unionTypeDefinition.getName()))
                    .addJavadoc("\n")
                    .addJavadoc(GeneratorGlobalConst.GENERAL_CODE_BLOCK);
        }
    }

    class TypeBuilder implements Function<ObjectTypeDefinition, TypeSpec.Builder> {

        @Override
        public TypeSpec.Builder apply(ObjectTypeDefinition objectTypeDefinition) {
            TypeSpec.Builder typeSpecBuilder = base(objectTypeDefinition).get();
            fillMethods(objectTypeDefinition, isGraphqlMethodField)
                    .andThen(fillDgraphType(objectTypeDefinition))
                    .andThen(fillAutowired(objectTypeDefinition))
                    .accept(typeSpecBuilder);
            return typeSpecBuilder;
        }
    }

    class OperatorBuilder implements Function<ObjectTypeDefinition, TypeSpec.Builder> {

        @Override
        public TypeSpec.Builder apply(ObjectTypeDefinition objectTypeDefinition) {
            TypeSpec.Builder typeSpecBuilder = base(objectTypeDefinition).get();
            fillMethods(objectTypeDefinition, p -> true)
                    .andThen(fillAutowired(objectTypeDefinition))
                    .accept(typeSpecBuilder);
            return typeSpecBuilder;
        }
    }

    private List<FieldSpec> buildFieldSpecs(ObjectTypeDefinition objectTypeDefinition) {
        return objectTypeDefinition.getFieldDefinitions().stream()
                .filter(isGraphqlMethodField.negate())
                .map(fieldDefinition -> {
                    FieldSpec.Builder fieldSpecBuilder = FieldSpec.builder(toJavapoetTypeName.apply(fieldDefinition.getType()), fieldDefinition.getName(), Modifier.PRIVATE)
                            .addJavadoc(Optional.ofNullable(fieldDefinition.getDescription()).map(Description::getContent).orElse(fieldDefinition.getName()));
                    if (DEFAULT_UID_FIELD_NAME.equals(fieldDefinition.getName())) {
                        fieldSpecBuilder.addAnnotation(UidField.class);
                    }
                    return fieldSpecBuilder.build();
                })
                .collect(Collectors.toList());
    }


    private Consumer<TypeSpec.Builder> fillMethods(ObjectTypeDefinition objectTypeDefinition, Predicate<FieldDefinition> predicate) {
        return builder -> objectTypeDefinition.getFieldDefinitions().stream()
                .filter(predicate)
                .map(fieldDefinition ->
                        MethodSpec.methodBuilder(fieldDefinition.getName())
                                .addModifiers(Modifier.PUBLIC)
                                .beginControlFlow("return dataFetchingEnvironment -> ")
                                .addAnnotation(AnnotationSpec.builder(GraphqlMethod.class).addMember("type", "$S", objectTypeDefinition.getName()).build())
                                .addStatement(" return $L.handle$L(dataFetchingEnvironment)", SpecNameManager.getUnCapitalizeApiName(objectTypeDefinition), StringUtils.capitalize(fieldDefinition.getName()))
                                .addStatement("}")
                                .returns(ParameterizedTypeName.get(ClassName.get(DataFetcher.class), toJavapoetTypeName.apply(fieldDefinition.getType())))
                                .build())
                .forEach(builder::addMethod);
    }

    private Supplier<TypeSpec.Builder> base(ObjectTypeDefinition objectTypeDefinition) {
        return () -> TypeSpec.classBuilder(objectTypeDefinition.getName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(DataFetcherService.class)
                .addSuperinterface(IDgraphType.class)
                .addJavadoc(Optional.ofNullable(objectTypeDefinition.getDescription()).map(Description::getContent).orElse(objectTypeDefinition.getName()))
                .addJavadoc("\n")
                .addJavadoc(GeneratorGlobalConst.GENERAL_CODE_BLOCK);
    }

    private Consumer<TypeSpec.Builder> fillAutowired(ObjectTypeDefinition objectTypeDefinition) {
        return typeSpecBuilder -> {
            if (isContainGraphqlMethodField.test(objectTypeDefinition)) {
                typeSpecBuilder.addField(
                        FieldSpec.builder(ClassName.get(packageManager.getInterfacePackage(), SpecNameManager.getApiName(objectTypeDefinition)), SpecNameManager.getUnCapitalizeApiName(objectTypeDefinition))
                                .addModifiers(Modifier.PRIVATE)
                                .addAnnotation(Autowired.class)
                                .build()
                );
            }
        };
    }


    private Consumer<TypeSpec.Builder> fillDgraphType(ObjectTypeDefinition objectTypeDefinition) {
        return typeSpecBuilder -> {
            typeSpecBuilder.addAnnotation(AnnotationSpec.builder(DgraphType.class)
                    .addMember("value", "$S", objectTypeDefinition.getName().toUpperCase()).build());
            List<FieldSpec> fieldSpecs = buildFieldSpecs(objectTypeDefinition);
            if (!CollectionUtils.isEmpty(fieldSpecs)) {
                fieldSpecs.forEach(fieldSpec -> {
                    typeSpecBuilder.addMethod(fieldSpecGenSetter.apply(fieldSpec));
                    typeSpecBuilder.addMethod(fieldSpecGenGetter.apply(fieldSpec));
                });
                typeSpecBuilder.addFields(fieldSpecs);
            }
        };
    }


}
