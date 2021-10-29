package io.github.wangyuheng.arc.generator.codegen;

import io.github.wangyuheng.arc.generator.codegen.spec.FieldDefinition2MethodSpec;
import io.github.wangyuheng.arc.generator.codegen.util.PackageManager;
import io.github.wangyuheng.arc.generator.convert.GraphqlType2JavapoetTypeName;
import io.github.wangyuheng.arc.generator.convert.IsContainsGraphqlMethodField;
import io.github.wangyuheng.arc.generator.convert.IsGraphqlMethodField;
import io.github.wangyuheng.arc.generator.convert.IsOperator;
import io.github.wangyuheng.arc.generator.dictionary.GeneratorGlobalConst;
import io.github.wangyuheng.arc.graphql.util.GraphqlTypeUtils;
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
 * {@link io.github.wangyuheng.arc.generator.conf.CodeGenType#DATA_FETCHER} 代码生成
 *
 * @author yuheng.wang
 */
public class DataFetcherGenerator implements IGenerator {

    private final PackageManager packageManager;

    public DataFetcherGenerator(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    @Override
    public Stream<JavaFile> apply(TypeDefinitionRegistry typeDefinitionRegistry) {
        final IsOperator isOperator = new IsOperator(GraphqlTypeUtils.getOperationTypeNames(typeDefinitionRegistry));
        final IsGraphqlMethodField isGraphqlMethodField = new IsGraphqlMethodField(typeDefinitionRegistry);
        final IsContainsGraphqlMethodField isContainGraphqlMethodField = new IsContainsGraphqlMethodField(isGraphqlMethodField);
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