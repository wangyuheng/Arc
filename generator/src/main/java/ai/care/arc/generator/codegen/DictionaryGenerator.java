package ai.care.arc.generator.codegen;

import ai.care.arc.generator.codegen.util.JavadocUtils;
import ai.care.arc.generator.codegen.util.PackageManager;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import graphql.language.Description;
import graphql.language.EnumTypeDefinition;
import graphql.schema.idl.TypeDefinitionRegistry;

import javax.lang.model.element.Modifier;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * {@link ai.care.arc.generator.conf.CodeGenType#DICTIONARY} 代码生成
 *
 * @author yuheng.wang
 */
public class DictionaryGenerator implements IGenerator {

    private PackageManager packageManager;

    public DictionaryGenerator(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    @Override
    public Stream<JavaFile> apply(TypeDefinitionRegistry typeDefinitionRegistry) {
        return typeDefinitionRegistry.getTypes(EnumTypeDefinition.class).stream()
                .map(new EnumTypeDefinitionGenerator());
    }

    class EnumTypeDefinitionGenerator implements Function<EnumTypeDefinition, JavaFile> {

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

}
