package ai.care.arc.generator;

import ai.care.arc.generator.codegen.*;
import ai.care.arc.generator.codegen.util.PackageManager;
import ai.care.arc.generator.conf.CodeGenConfig;
import ai.care.arc.generator.conf.CodeGenOperation;
import ai.care.arc.generator.conf.CodeGenType;
import ai.care.arc.generator.io.CodeWriter;
import com.squareup.javapoet.JavaFile;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

import java.io.InputStream;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * 根据GraphqlSchema生成JavaCode，包括
 * <p>
 * 1. enum {@link DictionaryGenerator}
 * 2. input type {@link InputGenerator}
 * 3. type {@link TypeGenerator}
 * 4. repository {@link RepositoryGenerator}
 * 5. interface {@link ApiGenerator} 此interface为java接口类, graphql interface归类为type
 * <p>
 * 开发者只需编写interface的实现类
 *
 * @author yuheng.wang
 */
public class JavaCodeGenerator {

    private final CodeWriter codeWriter;
    private final CodeGenConfig config;

    public JavaCodeGenerator(CodeWriter codeWriter, CodeGenConfig config) {
        this.codeWriter = codeWriter;
        this.config = config;
    }

    public JavaCodeGenerator(CodeWriter codeWriter) {
        this.codeWriter = codeWriter;
        this.config = new CodeGenConfig();
    }

    public void generate(InputStream inputStream, String basePackage) {
        final Predicate<JavaFile> canExec = javaFile -> {
            if (config.getIgnoreJavaFileNames().contains(javaFile.packageName)) {
                return false;
            } else {
                CodeGenOperation operation = config.getOperationByType(CodeGenType.parse(javaFile.packageName));
                switch (operation) {
                    case OVERRIDE:
                        return true;
                    case SKIP:
                        return false;
                    case SKIP_IF_EXISTED:
                        return !codeWriter.exist(javaFile);
                    default:
                        throw new IllegalArgumentException("operation illegal");
                }
            }
        };
        this.parseJavaFileStream(inputStream, basePackage)
                .filter(canExec)
                .forEach(codeWriter::write);
    }

    private Stream<JavaFile> parseJavaFileStream(InputStream schema, String basePackage) {
        final TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(schema);
        final PackageManager packageManager = new PackageManager(basePackage, typeDefinitionRegistry);
        return Stream.of(
                new DictionaryGenerator(packageManager),
                new InputGenerator(packageManager),
                new TypeGenerator(packageManager),
                new RepositoryGenerator(packageManager),
                new ApiGenerator(packageManager))
                .flatMap(it -> it.apply(typeDefinitionRegistry));
    }
}
