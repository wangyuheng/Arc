package ai.care.arc.generator.codegen;

import ai.care.arc.generator.convert.GraphqlType2JavapoetTypeName;
import ai.care.arc.generator.dictionary.GeneratorGlobalConst;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import graphql.language.ObjectTypeDefinition;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Graphql Operator 生成 {@link ai.care.arc.generator.conf.CodeGenType#API} 代码
 *
 * @author yuheng.wang
 */
public class OperatorInterfaceGenerator implements Function<ObjectTypeDefinition, JavaFile> {

    private final PackageManager packageManager;
    private final FieldDefinition2MethodSpec fieldDefinition2MethodSpec;

    public OperatorInterfaceGenerator(PackageManager packageManager) {
        this.packageManager = packageManager;
        this.fieldDefinition2MethodSpec = new FieldDefinition2MethodSpec(new GraphqlType2JavapoetTypeName(packageManager));
    }

    @Override
    public JavaFile apply(ObjectTypeDefinition typeDefinition) {
        List<MethodSpec> methodSpecs = typeDefinition.getFieldDefinitions().stream()
                .map(fieldDefinition2MethodSpec)
                .collect(Collectors.toList());

        TypeSpec typeSpec = TypeSpec.interfaceBuilder(typeDefinition.getName() + GeneratorGlobalConst.INTERFACE_SUFFIX)
                .addModifiers(Modifier.PUBLIC)
                .addMethods(methodSpecs)
                .addJavadoc(GeneratorGlobalConst.GENERAL_CODE_BLOCK)
                .build();
        return JavaFile.builder(packageManager.getInterfacePackage(), typeSpec).build();
    }
}