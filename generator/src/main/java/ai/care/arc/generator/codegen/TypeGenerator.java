package ai.care.arc.generator.codegen;

import ai.care.arc.dgraph.annotation.DgraphType;
import ai.care.arc.dgraph.annotation.UidField;
import ai.care.arc.dgraph.dictionary.IDgraphType;
import ai.care.arc.generator.codegen.spec.FieldSpecGenGetter;
import ai.care.arc.generator.codegen.spec.FieldSpecGenSetter;
import ai.care.arc.generator.codegen.util.JavadocUtils;
import ai.care.arc.generator.codegen.util.PackageManager;
import ai.care.arc.generator.codegen.util.SpecNameManager;
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
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 根据 {@link ObjectTypeDefinition} 生成 {@link ai.care.arc.generator.conf.CodeGenType#TYPE} 代码
 *
 * @author yuheng.wang
 */
public class TypeGenerator implements IGenerator {

    private static final String DEFAULT_UID_FIELD_NAME = "id";

    private final PackageManager packageManager;

    public TypeGenerator(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    @Override
    public Stream<JavaFile> apply(TypeDefinitionRegistry typeDefinitionRegistry) {
        final Predicate<ObjectTypeDefinition> isOperator = new IsOperator(GraphqlTypeUtils.getOperationTypeNames(typeDefinitionRegistry));
        final GraphqlType2JavapoetTypeName toJavapoetTypeName = new GraphqlType2JavapoetTypeName(packageManager);
        final MethodSpecBuilder toMethodSpec = new MethodSpecBuilder(toJavapoetTypeName);
        final DgraphTypeFiller dgraphTypeFiller = new DgraphTypeFiller(toJavapoetTypeName);
        final AutowiredFieldFiller autowiredFieldFiller = new AutowiredFieldFiller(packageManager);
        final TypeSpecConvert typeSpecConvert = new TypeSpecConvert();

        return Stream.concat(
                Stream.concat(
                        typeDefinitionRegistry.getTypes(ObjectTypeDefinition.class).stream()
                                .filter(isOperator.negate())
                                .map(new ObjectGenerator(typeSpecConvert, dgraphTypeFiller, autowiredFieldFiller, toMethodSpec)),
                        typeDefinitionRegistry.getTypes(ObjectTypeDefinition.class).stream()
                                .filter(isOperator)
                                .map(new OperatorGenerator(typeSpecConvert, autowiredFieldFiller, toMethodSpec))
                ),
                typeDefinitionRegistry.getTypes(UnionTypeDefinition.class).stream()
                        .map(new UnionGenerator())
        ).map(it -> JavaFile.builder(packageManager.getTypePackage(), it.build()).build());
    }

    static class ObjectGenerator implements Function<ObjectTypeDefinition, TypeSpec.Builder> {

        private final TypeSpecConvert typeSpecConvert;
        private final DgraphTypeFiller dgraphTypeFiller;
        private final AutowiredFieldFiller autowiredFieldFiller;
        private final MethodSpecBuilder toMethodSpec;

        public ObjectGenerator(TypeSpecConvert typeSpecConvert,
                               DgraphTypeFiller dgraphTypeFiller,
                               AutowiredFieldFiller autowiredFieldFiller,
                               MethodSpecBuilder toMethodSpec) {
            this.typeSpecConvert = typeSpecConvert;
            this.dgraphTypeFiller = dgraphTypeFiller;
            this.autowiredFieldFiller = autowiredFieldFiller;
            this.toMethodSpec = toMethodSpec;
        }

        @Override
        public TypeSpec.Builder apply(ObjectTypeDefinition objectTypeDefinition) {
            return new MethodsFiller(toMethodSpec, new IsGraphqlMethodField())
                    .andThen(dgraphTypeFiller)
                    .andThen(autowiredFieldFiller)
                    .apply(typeSpecConvert.apply(objectTypeDefinition), objectTypeDefinition);
        }

    }

    static class OperatorGenerator implements Function<ObjectTypeDefinition, TypeSpec.Builder> {

        private final TypeSpecConvert typeSpecConvert;
        private final AutowiredFieldFiller autowiredFieldFiller;
        private final MethodSpecBuilder toMethodSpec;

        public OperatorGenerator(TypeSpecConvert typeSpecConvert, AutowiredFieldFiller autowiredFieldFiller, MethodSpecBuilder toMethodSpec) {
            this.typeSpecConvert = typeSpecConvert;
            this.autowiredFieldFiller = autowiredFieldFiller;
            this.toMethodSpec = toMethodSpec;
        }

        @Override
        public TypeSpec.Builder apply(ObjectTypeDefinition objectTypeDefinition) {
            return new MethodsFiller(toMethodSpec, p -> true)
                    .andThen(autowiredFieldFiller)
                    .apply(typeSpecConvert.apply(objectTypeDefinition), objectTypeDefinition);
        }
    }

    /**
     * Union 需要融合多个类的内容提供转换能力
     * 目前只生成class未处理
     */
    static class UnionGenerator implements Function<UnionTypeDefinition, TypeSpec.Builder> {

        @Override
        public TypeSpec.Builder apply(UnionTypeDefinition unionTypeDefinition) {
            return TypeSpec.classBuilder(unionTypeDefinition.getName())
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addJavadoc(JavadocUtils.getDocForType(unionTypeDefinition));
        }
    }

    static class MethodSpecBuilder implements BiFunction<ObjectTypeDefinition, FieldDefinition, MethodSpec> {

        private final GraphqlType2JavapoetTypeName toJavapoetTypeName;

        public MethodSpecBuilder(GraphqlType2JavapoetTypeName toJavapoetTypeName) {
            this.toJavapoetTypeName = toJavapoetTypeName;
        }

        @Override
        public MethodSpec apply(ObjectTypeDefinition objectTypeDefinition, FieldDefinition fieldDefinition) {
            return MethodSpec.methodBuilder(fieldDefinition.getName())
                    .addModifiers(Modifier.PUBLIC)
                    .beginControlFlow("return dataFetchingEnvironment -> ")
                    .addAnnotation(AnnotationSpec.builder(GraphqlMethod.class).addMember("type", "$S", objectTypeDefinition.getName()).build())
                    .addStatement(" return $L.handle$L(dataFetchingEnvironment)", SpecNameManager.getUnCapitalizeApiName(objectTypeDefinition), StringUtils.capitalize(fieldDefinition.getName()))
                    .addStatement("}")
                    .returns(ParameterizedTypeName.get(ClassName.get(DataFetcher.class), toJavapoetTypeName.apply(fieldDefinition.getType())))
                    .build();
        }
    }

    static class TypeSpecConvert implements Function<ObjectTypeDefinition, TypeSpec.Builder> {

        @Override
        public TypeSpec.Builder apply(ObjectTypeDefinition objectTypeDefinition) {
            return TypeSpec.classBuilder(StringUtils.capitalize(objectTypeDefinition.getName()))
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(DataFetcherService.class)
                    .addSuperinterface(IDgraphType.class)
                    .addJavadoc(JavadocUtils.getDocForType(objectTypeDefinition));
        }
    }

    static class DgraphTypeFiller implements Filler<TypeSpec.Builder, ObjectTypeDefinition> {

        private final GraphqlType2JavapoetTypeName toJavapoetTypeName;
        private final IsGraphqlMethodField isGraphqlMethodField = new IsGraphqlMethodField();
        private final FieldSpecGenGetter fieldSpecGenGetter = new FieldSpecGenGetter();
        private final FieldSpecGenSetter fieldSpecGenSetter = new FieldSpecGenSetter();

        public DgraphTypeFiller(GraphqlType2JavapoetTypeName toJavapoetTypeName) {
            this.toJavapoetTypeName = toJavapoetTypeName;
        }

        @Override
        public TypeSpec.Builder apply(TypeSpec.Builder typeSpecBuilder, ObjectTypeDefinition objectTypeDefinition) {
            typeSpecBuilder.addAnnotation(AnnotationSpec.builder(DgraphType.class)
                    .addMember("value", "$S", objectTypeDefinition.getName().toUpperCase()).build());
            List<FieldSpec> fieldSpecs = objectTypeDefinition.getFieldDefinitions().stream()
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
            if (!CollectionUtils.isEmpty(fieldSpecs)) {
                fieldSpecs.forEach(fieldSpec -> {
                    typeSpecBuilder.addMethod(fieldSpecGenSetter.apply(fieldSpec));
                    typeSpecBuilder.addMethod(fieldSpecGenGetter.apply(fieldSpec));
                });
                typeSpecBuilder.addFields(fieldSpecs);
            }
            return typeSpecBuilder;
        }
    }

    static class AutowiredFieldFiller implements Filler<TypeSpec.Builder, ObjectTypeDefinition> {

        private final Predicate<ObjectTypeDefinition> isContainGraphqlMethodField = new IsContainsGraphqlMethodField();
        private final PackageManager packageManager;

        public AutowiredFieldFiller(PackageManager packageManager) {
            this.packageManager = packageManager;
        }

        @Override
        public TypeSpec.Builder apply(TypeSpec.Builder typeSpecBuilder, ObjectTypeDefinition objectTypeDefinition) {
            if (isContainGraphqlMethodField.test(objectTypeDefinition)) {
                typeSpecBuilder.addField(
                        FieldSpec.builder(ClassName.get(packageManager.getInterfacePackage(), SpecNameManager.getApiName(objectTypeDefinition)), SpecNameManager.getUnCapitalizeApiName(objectTypeDefinition))
                                .addModifiers(Modifier.PRIVATE)
                                .addAnnotation(Autowired.class)
                                .build()
                );
            }
            return typeSpecBuilder;
        }
    }

    static class MethodsFiller implements Filler<TypeSpec.Builder, ObjectTypeDefinition> {

        private final MethodSpecBuilder methodSpecBuilder;
        private final Predicate<FieldDefinition> filter;

        public MethodsFiller(MethodSpecBuilder methodSpecBuilder, Predicate<FieldDefinition> filter) {
            this.methodSpecBuilder = methodSpecBuilder;
            this.filter = filter;
        }

        @Override
        public TypeSpec.Builder apply(TypeSpec.Builder typeSpecBuilder, ObjectTypeDefinition objectTypeDefinition) {
            objectTypeDefinition.getFieldDefinitions().stream()
                    .filter(filter)
                    .map(fieldDefinition -> methodSpecBuilder.apply(objectTypeDefinition, fieldDefinition))
                    .forEach(typeSpecBuilder::addMethod);
            return typeSpecBuilder;
        }
    }
}