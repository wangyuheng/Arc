package ai.care.arc.generator;

import ai.care.arc.generator.conf.CodeGenConfig;
import ai.care.arc.generator.conf.CodeGenOperation;
import ai.care.arc.generator.conf.CodeGenType;
import ai.care.arc.generator.io.CodeWriter;
import com.squareup.javapoet.JavaFile;

import java.io.InputStream;
import java.util.function.Predicate;

/**
 * 代码生成器处理
 *
 * @author yuheng.wang
 */
public class JavaCodeGeneratorHandler {

    private CodeWriter codeWriter;
    private CodeGenConfig config;

    public JavaCodeGeneratorHandler(CodeWriter codeWriter, CodeGenConfig config) {
        this.codeWriter = codeWriter;
        this.config = config;
    }

    private final JavaCodeGenerator javaCodeGenerator = new JavaCodeGenerator();
    private final Predicate<JavaFile> doExec = javaFile -> {
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

    public void handle(InputStream inputStream, String basePackage) {
        javaCodeGenerator.generate(inputStream, basePackage)
                .stream()
                .filter(doExec)
                .forEach(codeWriter::write);
    }

}
