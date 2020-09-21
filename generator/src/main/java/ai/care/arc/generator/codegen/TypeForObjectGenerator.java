package ai.care.arc.generator.codegen;

import ai.care.arc.dgraph.annotation.DgraphType;
import ai.care.arc.dgraph.annotation.UidField;
import ai.care.arc.dgraph.dictionary.IDgraphType;
import ai.care.arc.generator.convert.GraphqlType2JavapoetTypeName;
import ai.care.arc.generator.convert.IsContainsGraphqlMethodField;
import ai.care.arc.generator.convert.IsGraphqlMethodField;
import ai.care.arc.generator.dictionary.GeneratorGlobalConst;
import ai.care.arc.graphql.annotation.DataFetcherService;
import ai.care.arc.graphql.annotation.GraphqlMethod;
import com.squareup.javapoet.*;
import graphql.language.Description;
import graphql.language.FieldDefinition;
import graphql.language.ObjectTypeDefinition;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 根据 {@link ObjectTypeDefinition} 生成 {@link ai.care.arc.generator.conf.CodeGenType#TYPE} 代码
 *
 * @author yuheng.wang
 */
public class TypeForObjectGenerator implements Function<ObjectTypeDefinition, JavaFile> {

    private final Predicate<FieldDefinition> isGraphqlMethodField = new IsGraphqlMethodField();
    private final Predicate<ObjectTypeDefinition> isContainGraphqlMethodField = new IsContainsGraphqlMethodField();
    private final FieldSpecGenGetter fieldSpecGenGetter = new FieldSpecGenGetter();
    private final FieldSpecGenSetter fieldSpecGenSetter = new FieldSpecGenSetter();
    private final Predicate<ObjectTypeDefinition> isOperator;

    private static final String DEFAULT_UID_FIELD_NAME = "id";

    private PackageManager packageManager;
    private GraphqlType2JavapoetTypeName toJavapoetTypeName;

    public TypeForObjectGenerator(Predicate<ObjectTypeDefinition> isOperator, PackageManager packageManager) {
        this.isOperator = isOperator;
        this.packageManager = packageManager;
        this.toJavapoetTypeName = new GraphqlType2JavapoetTypeName(packageManager);
    }

    @Override
    public JavaFile apply(ObjectTypeDefinition objectTypeDefinition) {
        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(objectTypeDefinition.getName())
                .addModifiers(Modifier.PUBLIC)
                .addMethods(buildMethodSpecs(objectTypeDefinition))
                .addAnnotation(DataFetcherService.class)
                .addSuperinterface(IDgraphType.class)
                .addJavadoc(Optional.ofNullable(objectTypeDefinition.getDescription()).map(Description::getContent).orElse(objectTypeDefinition.getName()))
                .addJavadoc("\n")
                .addJavadoc(GeneratorGlobalConst.GENERAL_CODE_BLOCK);

        if (isOperator.negate().test(objectTypeDefinition)) {
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
        }

        if (isContainGraphqlMethodField.test(objectTypeDefinition)) {
            typeSpecBuilder.addField(
                    FieldSpec.builder(ClassName.get(packageManager.getInterfacePackage(), getInterfaceName(objectTypeDefinition)), getUnCapitalizeInterfaceName(objectTypeDefinition))
                            .addModifiers(Modifier.PRIVATE)
                            .addAnnotation(Autowired.class)
                            .build()
            );
        }
        return JavaFile.builder(packageManager.getTypePackage(), typeSpecBuilder.build()).build();
    }

    private String getInterfaceName(ObjectTypeDefinition objectTypeDefinition) {
        return objectTypeDefinition.getName() + GeneratorGlobalConst.INTERFACE_SUFFIX;
    }

    private String getUnCapitalizeInterfaceName(ObjectTypeDefinition objectTypeDefinition) {
        return StringUtils.uncapitalize(getInterfaceName(objectTypeDefinition));
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

    private List<MethodSpec> buildMethodSpecs(ObjectTypeDefinition objectTypeDefinition) {
        return objectTypeDefinition.getFieldDefinitions().stream()
                .filter(isGraphqlMethodField.or(a -> isOperator.test(objectTypeDefinition)))
                .map(fieldDefinition ->
                        MethodSpec.methodBuilder(fieldDefinition.getName())
                                .addModifiers(Modifier.PUBLIC)
                                .beginControlFlow("return dataFetchingEnvironment -> ")
                                .addAnnotation(AnnotationSpec.builder(GraphqlMethod.class).addMember("type", "$S", objectTypeDefinition.getName()).build())
                                .addStatement(" return $L.handle$L(dataFetchingEnvironment)", getUnCapitalizeInterfaceName(objectTypeDefinition), StringUtils.capitalize(fieldDefinition.getName()))
                                .addStatement("}")
                                .returns(ParameterizedTypeName.get(ClassName.get(DataFetcher.class), toJavapoetTypeName.apply(fieldDefinition.getType())))
                                .build())
                .collect(Collectors.toList());
    }
}
