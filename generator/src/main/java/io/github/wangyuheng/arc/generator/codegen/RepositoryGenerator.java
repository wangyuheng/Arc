package io.github.wangyuheng.arc.generator.codegen;

import io.github.wangyuheng.arc.dgraph.repository.SimpleDgraphRepository;
import io.github.wangyuheng.arc.generator.codegen.util.PackageManager;
import io.github.wangyuheng.arc.generator.convert.IsContainsGraphqlMethodField;
import io.github.wangyuheng.arc.generator.convert.IsGraphqlMethodField;
import io.github.wangyuheng.arc.generator.convert.IsOperator;
import io.github.wangyuheng.arc.generator.dictionary.GeneratorGlobalConst;
import io.github.wangyuheng.arc.graphql.util.GraphqlTypeUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import graphql.language.ObjectTypeDefinition;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.lang.model.element.Modifier;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * {@link io.github.wangyuheng.arc.generator.conf.CodeGenType#REPO} 代码生成
 * 非Operator 且包含GraphqlMethod字段的Type需要生成对应的Repo
 *
 * @author yuheng.wang
 */
public class RepositoryGenerator implements IGenerator {

    private final PackageManager packageManager;

    public RepositoryGenerator(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    @Override
    public Stream<JavaFile> apply(TypeDefinitionRegistry typeDefinitionRegistry) {
        final IsOperator isOperator = new IsOperator(GraphqlTypeUtils.getOperationTypeNames(typeDefinitionRegistry));
        final IsGraphqlMethodField isGraphqlMethodField = new IsGraphqlMethodField(typeDefinitionRegistry);
        final IsContainsGraphqlMethodField isContainGraphqlMethodField = new IsContainsGraphqlMethodField(isGraphqlMethodField);

        return typeDefinitionRegistry.getTypes(ObjectTypeDefinition.class).stream()
                .filter(isOperator.negate().and(isContainGraphqlMethodField))
                .distinct()
                .map(new ObjectTypeDefinitionGenerator(packageManager))
                .map(it -> JavaFile.builder(packageManager.getRepoPackage(), it.build()).build());
    }

    static class ObjectTypeDefinitionGenerator implements Function<ObjectTypeDefinition, TypeSpec.Builder> {

        private final PackageManager packageManager;

        public ObjectTypeDefinitionGenerator(PackageManager packageManager) {
            this.packageManager = packageManager;
        }

        @Override
        public TypeSpec.Builder apply(ObjectTypeDefinition objectTypeDefinition) {
            return TypeSpec.classBuilder(StringUtils.capitalize(objectTypeDefinition.getName()) + GeneratorGlobalConst.REPOSITORY_SUFFIX)
                    .superclass(ParameterizedTypeName.get(ClassName.get(SimpleDgraphRepository.class), ClassName.get(packageManager.getTypePackage(), objectTypeDefinition.getName())))
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Repository.class)
                    .addJavadoc(GeneratorGlobalConst.GENERAL_CODE_BLOCK);
        }
    }

}