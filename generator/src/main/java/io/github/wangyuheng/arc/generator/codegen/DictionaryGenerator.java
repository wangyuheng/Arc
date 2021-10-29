package io.github.wangyuheng.arc.generator.codegen;

import io.github.wangyuheng.arc.generator.codegen.util.JavadocUtils;
import io.github.wangyuheng.arc.generator.codegen.util.PackageManager;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import graphql.language.EnumTypeDefinition;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.util.StringUtils;

import javax.lang.model.element.Modifier;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * {@link io.github.wangyuheng.arc.generator.conf.CodeGenType#DICTIONARY} 代码生成
 *
 * @author yuheng.wang
 */
public class DictionaryGenerator implements IGenerator {

    private final PackageManager packageManager;

    public DictionaryGenerator(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    @Override
    public Stream<JavaFile> apply(TypeDefinitionRegistry typeDefinitionRegistry) {
        return typeDefinitionRegistry.getTypes(EnumTypeDefinition.class).stream()
                .map(new EnumTypeDefinitionGenerator())
                .map(it -> JavaFile.builder(packageManager.getEnumPackage(), it.build()).build());
    }

    static class EnumTypeDefinitionGenerator implements Function<EnumTypeDefinition, TypeSpec.Builder> {

        @Override
        public TypeSpec.Builder apply(EnumTypeDefinition enumTypeDefinition) {
            TypeSpec.Builder typeSpecBuilder = TypeSpec.enumBuilder(StringUtils.capitalize(enumTypeDefinition.getName()))
                    .addModifiers(Modifier.PUBLIC)
                    .addJavadoc(JavadocUtils.getDocForType(enumTypeDefinition));
            enumTypeDefinition.getEnumValueDefinitions()
                    .forEach(enumValueDefinition ->
                            typeSpecBuilder.addEnumConstant(enumValueDefinition.getName(), TypeSpec.anonymousClassBuilder("")
                                    .addJavadoc(JavadocUtils.getFieldDocByDescription(enumValueDefinition.getDescription(), enumValueDefinition.getName()))
                                    .build()));
            return typeSpecBuilder;
        }
    }

}
