package ai.care.arc.generator.codegen;

import ai.care.arc.generator.codegen.util.PackageManager;
import ai.care.arc.generator.dictionary.GeneratorGlobalConst;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import graphql.language.Description;
import graphql.language.UnionTypeDefinition;

import javax.lang.model.element.Modifier;
import java.util.Optional;
import java.util.function.Function;

/**
 * 根据 {@link UnionTypeDefinition} 生成 {@link ai.care.arc.generator.conf.CodeGenType#TYPE} 代码
 *
 * @author yuheng.wang
 */
public class TypeForUnionGenerator implements Function<UnionTypeDefinition, JavaFile> {

    private PackageManager packageManager;

    public TypeForUnionGenerator(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    @Override
    public JavaFile apply(UnionTypeDefinition unionTypeDefinition) {
        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(unionTypeDefinition.getName())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addJavadoc(Optional.ofNullable(unionTypeDefinition.getDescription()).map(Description::getContent).orElse(unionTypeDefinition.getName()))
                .addJavadoc("\n")
                .addJavadoc(GeneratorGlobalConst.GENERAL_CODE_BLOCK);

        return JavaFile.builder(packageManager.getTypePackage(), typeSpecBuilder.build()).build();
    }
}
