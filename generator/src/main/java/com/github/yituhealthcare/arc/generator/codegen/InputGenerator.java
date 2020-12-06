package com.github.yituhealthcare.arc.generator.codegen;

import com.github.yituhealthcare.arc.generator.codegen.spec.FieldSpecGenGetter;
import com.github.yituhealthcare.arc.generator.codegen.spec.FieldSpecGenSetter;
import com.github.yituhealthcare.arc.generator.codegen.util.JavadocUtils;
import com.github.yituhealthcare.arc.generator.codegen.util.PackageManager;
import com.github.yituhealthcare.arc.generator.convert.GraphqlType2JavapoetTypeName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import graphql.language.InputObjectTypeDefinition;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link com.github.yituhealthcare.arc.generator.conf.CodeGenType#INPUT} 代码生成
 *
 * @author yuheng.wang
 */
public class InputGenerator implements IGenerator {

    private final PackageManager packageManager;

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
        private final GraphqlType2JavapoetTypeName toJavapoetTypeName;

        public InputObjectTypeDefinitionGenerator(GraphqlType2JavapoetTypeName toJavapoetTypeName) {
            this.toJavapoetTypeName = toJavapoetTypeName;
        }

        @Override
        public TypeSpec.Builder apply(InputObjectTypeDefinition inputObjectTypeDefinition) {
            List<FieldSpec> fieldSpecs = inputObjectTypeDefinition.getInputValueDefinitions().stream()
                    .map(inputValueDefinition -> FieldSpec.builder(toJavapoetTypeName.apply(inputValueDefinition.getType()), inputValueDefinition.getName(), Modifier.PRIVATE)
                            .addJavadoc(JavadocUtils.getFieldDocByDescription(inputValueDefinition.getDescription(), inputValueDefinition.getName()))
                            .build())
                    .collect(Collectors.toList());

            TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(StringUtils.capitalize(inputObjectTypeDefinition.getName()))
                    .addModifiers(Modifier.PUBLIC)
                    .addFields(fieldSpecs)
                    .addJavadoc(JavadocUtils.getDocForType(inputObjectTypeDefinition));

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