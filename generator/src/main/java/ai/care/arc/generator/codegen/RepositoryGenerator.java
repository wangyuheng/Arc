package ai.care.arc.generator.codegen;

import ai.care.arc.dgraph.repository.SimpleDgraphRepository;
import ai.care.arc.generator.codegen.util.PackageManager;
import ai.care.arc.generator.convert.IsContainsGraphqlMethodField;
import ai.care.arc.generator.convert.IsOperator;
import ai.care.arc.generator.dictionary.GeneratorGlobalConst;
import ai.care.arc.graphql.util.GraphqlTypeUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import graphql.language.ObjectTypeDefinition;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.stereotype.Repository;

import javax.lang.model.element.Modifier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * {@link ai.care.arc.generator.conf.CodeGenType#REPO} 代码生成
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
        final Predicate<ObjectTypeDefinition> isOperator = new IsOperator(GraphqlTypeUtils.getOperationTypeNames(typeDefinitionRegistry));
        final Predicate<ObjectTypeDefinition> isContainGraphqlMethodField = new IsContainsGraphqlMethodField();

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
            return TypeSpec.classBuilder(objectTypeDefinition.getName() + GeneratorGlobalConst.REPOSITORY_SUFFIX)
                    .superclass(ParameterizedTypeName.get(ClassName.get(SimpleDgraphRepository.class), ClassName.get(packageManager.getTypePackage(), objectTypeDefinition.getName())))
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Repository.class)
                    .addJavadoc(GeneratorGlobalConst.GENERAL_CODE_BLOCK);
        }
    }

}