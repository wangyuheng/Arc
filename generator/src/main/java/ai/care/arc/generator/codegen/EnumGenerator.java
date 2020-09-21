package ai.care.arc.generator.codegen;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import graphql.language.Description;
import graphql.language.EnumTypeDefinition;

import javax.lang.model.element.Modifier;
import java.util.Optional;
import java.util.function.Function;

/**
 * {@link ai.care.arc.generator.conf.CodeGenType#DICTIONARY} 代码生成
 *
 * @author yuheng.wang
 */
public class EnumGenerator implements Function<EnumTypeDefinition, JavaFile> {
    
    private PackageManager packageManager;

    public EnumGenerator(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    @Override
    public JavaFile apply(EnumTypeDefinition enumTypeDefinition) {
        TypeSpec.Builder typeSpecBuilder = TypeSpec.enumBuilder(enumTypeDefinition.getName())
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc(JavadocUtils.getDocForType(enumTypeDefinition, enumTypeDefinition.getDescription()));
        enumTypeDefinition.getEnumValueDefinitions()
                .forEach(enumValueDefinition ->
                        typeSpecBuilder.addEnumConstant(enumValueDefinition.getName(), TypeSpec.anonymousClassBuilder("")
                                .addJavadoc(Optional.ofNullable(enumValueDefinition.getDescription()).map(Description::getContent).orElse(enumValueDefinition.getName()))
                                .build()));
        return JavaFile.builder(packageManager.getEnumPackage(), typeSpecBuilder.build())
                .build();
    }
}
