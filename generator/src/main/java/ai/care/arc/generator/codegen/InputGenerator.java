package ai.care.arc.generator.codegen;

import ai.care.arc.generator.codegen.spec.FieldSpecGenGetter;
import ai.care.arc.generator.codegen.spec.FieldSpecGenSetter;
import ai.care.arc.generator.codegen.util.PackageManager;
import ai.care.arc.generator.convert.GraphqlType2JavapoetTypeName;
import ai.care.arc.generator.dictionary.GeneratorGlobalConst;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import graphql.language.Description;
import graphql.language.InputObjectTypeDefinition;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.util.CollectionUtils;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link ai.care.arc.generator.conf.CodeGenType#INPUT} 代码生成
 *
 * @author yuheng.wang
 */

public class InputGenerator implements IGenerator {

    private PackageManager packageManager;

    public InputGenerator(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    @Override
    public Stream<JavaFile> apply(TypeDefinitionRegistry typeDefinitionRegistry) {
        return typeDefinitionRegistry.getTypes(InputObjectTypeDefinition.class).stream()
                .map(new InputObjectTypeDefinitionGenerator(new GraphqlType2JavapoetTypeName(packageManager)))
                .map(it -> JavaFile.builder(packageManager.getInputPackage(), it.build()).build());
    }

    static class InputObjectTypeDefinitionGenerator implements Function<InputObjectTypeDefinition, TypeSpec.Builder> {

        private final FieldSpecGenGetter fieldSpecGenGetter = new FieldSpecGenGetter();
        private final FieldSpecGenSetter fieldSpecGenSetter = new FieldSpecGenSetter();
        private GraphqlType2JavapoetTypeName toJavapoetTypeName;

        public InputObjectTypeDefinitionGenerator(GraphqlType2JavapoetTypeName toJavapoetTypeName) {
            this.toJavapoetTypeName = toJavapoetTypeName;
        }

        @Override
        public TypeSpec.Builder apply(InputObjectTypeDefinition inputObjectTypeDefinition) {
            List<FieldSpec> fieldSpecs = inputObjectTypeDefinition.getInputValueDefinitions().stream()
                    .map(inputValueDefinition -> FieldSpec.builder(toJavapoetTypeName.apply(inputValueDefinition.getType()), inputValueDefinition.getName(), Modifier.PRIVATE)
                            .addJavadoc(Optional.ofNullable(inputValueDefinition.getDescription()).map(Description::getContent).orElse(inputValueDefinition.getName()))
                            .build())
                    .collect(Collectors.toList());

            TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(inputObjectTypeDefinition.getName())
                    .addModifiers(Modifier.PUBLIC)
                    .addFields(fieldSpecs)
                    .addJavadoc(Optional.ofNullable(inputObjectTypeDefinition.getDescription()).map(Description::getContent).orElse(inputObjectTypeDefinition.getName()))
                    .addJavadoc("\n")
                    .addJavadoc(GeneratorGlobalConst.GENERAL_CODE_BLOCK);

            if (!CollectionUtils.isEmpty(fieldSpecs)) {
                fieldSpecs.forEach(fieldSpec -> {
                    typeSpecBuilder.addMethod(fieldSpecGenSetter.apply(fieldSpec));
                    typeSpecBuilder.addMethod(fieldSpecGenGetter.apply(fieldSpec));
                });
            }

            return typeSpecBuilder;
        }
    }
}