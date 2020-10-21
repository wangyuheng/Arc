package ai.care.arc.generator;

import ai.care.arc.generator.codegen.*;
import ai.care.arc.generator.codegen.util.PackageManager;
import ai.care.arc.generator.conf.CodeGenConfig;
import ai.care.arc.generator.conf.CodeGenConfigHandler;
import ai.care.arc.generator.io.CodeWriter;
import com.squareup.javapoet.JavaFile;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

import java.io.InputStream;
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
    private final CodeGenConfigHandler codeGenConfigHandler;

    public JavaCodeGenerator(CodeWriter codeWriter, CodeGenConfig config) {
        this.codeWriter = codeWriter;
        this.codeGenConfigHandler = new CodeGenConfigHandler(codeWriter, config);
    }

    public JavaCodeGenerator(CodeWriter codeWriter) {
        this(codeWriter, new CodeGenConfig());
    }

    public void generate(InputStream inputStream, String basePackage) {
        this.parseJavaFileStream(inputStream, basePackage)
                .filter(codeGenConfigHandler.canExec())
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
