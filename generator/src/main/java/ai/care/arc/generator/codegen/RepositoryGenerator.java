package ai.care.arc.generator.codegen;

import ai.care.arc.dgraph.repository.SimpleDgraphRepository;
import ai.care.arc.generator.dictionary.GeneratorGlobalConst;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import graphql.language.ObjectTypeDefinition;
import org.springframework.stereotype.Repository;

import javax.lang.model.element.Modifier;
import java.util.function.Function;

/**
 * {@link ai.care.arc.generator.conf.CodeGenType#REPO} 代码生成
 *
 * @author yuheng.wang
 */
public class RepositoryGenerator implements Function<ObjectTypeDefinition, JavaFile> {
    private PackageManager packageManager;

    public RepositoryGenerator(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    @Override
    public JavaFile apply(ObjectTypeDefinition objectTypeDefinition) {
        return JavaFile.builder(packageManager.getRepoPackage(),
                TypeSpec.classBuilder(objectTypeDefinition.getName() + GeneratorGlobalConst.REPOSITORY_SUFFIX)
                        .superclass(ParameterizedTypeName.get(ClassName.get(SimpleDgraphRepository.class), ClassName.get(packageManager.getTypePackage(), objectTypeDefinition.getName())))
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Repository.class)
                        .addJavadoc(GeneratorGlobalConst.GENERAL_CODE_BLOCK)
                        .build())
                .build();
    }
}
