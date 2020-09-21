package ai.care.arc.generator.codegen;

import ai.care.arc.generator.convert.GraphqlType2JavapoetTypeName;
import ai.care.arc.generator.convert.IsGraphqlMethodField;
import ai.care.arc.generator.convert.IsOperator;
import ai.care.arc.generator.dictionary.GeneratorGlobalConst;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import graphql.language.FieldDefinition;
import graphql.language.ObjectTypeDefinition;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.util.StringUtils;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * {@link ai.care.arc.generator.conf.CodeGenType#API} 代码生成
 *
 * @author yuheng.wang
 */
public class InterfaceGenerator implements Function<ObjectTypeDefinition, JavaFile> {

    private final Predicate<FieldDefinition> isGraphqlMethodField = new IsGraphqlMethodField();
    private final Predicate<ObjectTypeDefinition> isOperator;
    private PackageManager packageManager;
    private GraphqlType2JavapoetTypeName graphqlType2JavapoetTypeName;

    public InterfaceGenerator(Predicate<ObjectTypeDefinition> isOperator, PackageManager packageManager) {
        this.packageManager = packageManager;
        this.graphqlType2JavapoetTypeName = new GraphqlType2JavapoetTypeName(packageManager);
        this.isOperator = isOperator;
    }

    @Override
    public JavaFile apply(ObjectTypeDefinition typeDefinition) {
        List<MethodSpec> methodSpecs = typeDefinition.getFieldDefinitions().stream()
                .filter(isGraphqlMethodField.or(a -> isOperator.test(typeDefinition)))
                .map(fieldDefinition -> MethodSpec.methodBuilder(GeneratorGlobalConst.HANDLE_METHOD_SUFFIX + StringUtils.capitalize(fieldDefinition.getName()))
                        .addParameter(DataFetchingEnvironment.class, StringUtils.uncapitalize(DataFetchingEnvironment.class.getSimpleName()))
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .returns(graphqlType2JavapoetTypeName.apply(fieldDefinition.getType()))
                        .build())
                .collect(Collectors.toList());

        TypeSpec typeSpec = TypeSpec.interfaceBuilder(typeDefinition.getName() + GeneratorGlobalConst.INTERFACE_SUFFIX)
                .addModifiers(Modifier.PUBLIC)
                .addMethods(methodSpecs)
                .addJavadoc(GeneratorGlobalConst.GENERAL_CODE_BLOCK)
                .build();
        return JavaFile.builder(packageManager.getInterfacePackage(), typeSpec).build();
    }
}