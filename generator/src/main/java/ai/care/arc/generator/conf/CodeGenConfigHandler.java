package ai.care.arc.generator.conf;

import ai.care.arc.generator.io.CodeWriter;
import com.squareup.javapoet.JavaFile;

import java.util.function.Predicate;

/**
 * 配置解析
 *
 * @author yuheng.wang
 */
public class CodeGenConfigHandler {

    private final CodeWriter codeWriter;
    private final CodeGenConfig config;

    public CodeGenConfigHandler(CodeWriter codeWriter, CodeGenConfig config) {
        this.codeWriter = codeWriter;
        this.config = config;
    }

    public final Predicate<JavaFile> canExec(){
        return javaFile -> {
            if (config.getIgnoreJavaFileNames().contains(javaFile.typeSpec.name)) {
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
    }
}
