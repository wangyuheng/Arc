package ai.care.arc.generator.codegen;

import ai.care.arc.generator.codegen.spec.FieldDefinition2MethodSpec;
import ai.care.arc.generator.codegen.util.PackageManager;
import ai.care.arc.generator.convert.GraphqlType2JavapoetTypeName;
import ai.care.arc.generator.convert.IsContainsGraphqlMethodField;
import ai.care.arc.generator.convert.IsGraphqlMethodField;
import ai.care.arc.generator.convert.IsOperator;
import ai.care.arc.generator.dictionary.GeneratorGlobalConst;
import ai.care.arc.graphql.util.GraphqlTypeUtils;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import graphql.language.FieldDefinition;
import graphql.language.ObjectTypeDefinition;
import graphql.schema.idl.TypeDefinitionRegistry;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link ai.care.arc.generator.conf.CodeGenType#API} 代码生成
 *
 * @author yuheng.wang
 */
public class ApiGenerator implements IGenerator {

    private final PackageManager packageManager;

    public ApiGenerator(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    @Override
    public Stream<JavaFile> apply(TypeDefinitionRegistry typeDefinitionRegistry) {
        final Predicate<ObjectTypeDefinition> isOperator = new IsOperator(GraphqlTypeUtils.getOperationTypeNames(typeDefinitionRegistry));
        final Predicate<ObjectTypeDefinition> isContainGraphqlMethodField = new IsContainsGraphqlMethodField();
        final Predicate<FieldDefinition> isGraphqlMethodField = new IsGraphqlMethodField();
        final FieldDefinition2MethodSpec fieldDefinition2MethodSpec = new FieldDefinition2MethodSpec(new GraphqlType2JavapoetTypeName(packageManager));

        return Stream.concat(
                        typeDefinitionRegistry.getTypes(ObjectTypeDefinition.class).stream()
                                .filter(isOperator.negate().and(isContainGraphqlMethodField))
                                .distinct()
                                .map(new ObjectTypeDefinitionGenerator(isGraphqlMethodField, fieldDefinition2MethodSpec)),
                        typeDefinitionRegistry.getTypes(ObjectTypeDefinition.class).stream()
                                .filter(isOperator)
                                .distinct()
                                .map(new OperatorDefinitionGenerator(fieldDefinition2MethodSpec))
                ).map(it -> JavaFile.builder(packageManager.getInterfacePackage(), it.build()).build());
    }

    static class OperatorDefinitionGenerator implements Function<ObjectTypeDefinition, TypeSpec.Builder> {

        private final FieldDefinition2MethodSpec fieldDefinition2MethodSpec;

        public OperatorDefinitionGenerator(FieldDefinition2MethodSpec fieldDefinition2MethodSpec) {
            this.fieldDefinition2MethodSpec = fieldDefinition2MethodSpec;
        }

        @Override
        public TypeSpec.Builder apply(ObjectTypeDefinition objectTypeDefinition) {
            List<MethodSpec> methodSpecs = objectTypeDefinition.getFieldDefinitions().stream()
                    .map(fieldDefinition2MethodSpec)
                    .collect(Collectors.toList());

            return TypeSpec.interfaceBuilder(objectTypeDefinition.getName() + GeneratorGlobalConst.INTERFACE_SUFFIX)
                    .addModifiers(Modifier.PUBLIC)
                    .addMethods(methodSpecs)
                    .addJavadoc(GeneratorGlobalConst.GENERAL_CODE_BLOCK)
                    ;
        }
    }

    static class ObjectTypeDefinitionGenerator implements Function<ObjectTypeDefinition, TypeSpec.Builder> {

        private final Predicate<FieldDefinition> isGraphqlMethodField;
        private final FieldDefinition2MethodSpec fieldDefinition2MethodSpec;

        public ObjectTypeDefinitionGenerator(Predicate<FieldDefinition> isGraphqlMethodField, FieldDefinition2MethodSpec fieldDefinition2MethodSpec) {
            this.isGraphqlMethodField = isGraphqlMethodField;
            this.fieldDefinition2MethodSpec = fieldDefinition2MethodSpec;
        }

        @Override
        public TypeSpec.Builder apply(ObjectTypeDefinition objectTypeDefinition) {

            List<MethodSpec> methodSpecs = objectTypeDefinition.getFieldDefinitions().stream()
                    .filter(isGraphqlMethodField)
                    .map(fieldDefinition2MethodSpec)
                    .collect(Collectors.toList());

            return TypeSpec.interfaceBuilder(objectTypeDefinition.getName() + GeneratorGlobalConst.INTERFACE_SUFFIX)
                    .addModifiers(Modifier.PUBLIC)
                    .addMethods(methodSpecs)
                    .addJavadoc(GeneratorGlobalConst.GENERAL_CODE_BLOCK)
                    ;
        }
    }
}