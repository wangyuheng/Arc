package ai.care.arc.generator.io;

import com.squareup.javapoet.JavaFile;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 将生成代码输出到指定目录
 *
 * @author yuheng.wang
 * @see JavaFile
 */
@Slf4j
public class CodeWriter {

    private Path targetPath;

    public CodeWriter(Path targetPath) {
        this.targetPath = targetPath;
    }

    public CodeWriter() {
        this.targetPath = Paths.get(".", "/src/main/java");
    }

    public boolean exist(JavaFile javaFile) {
        Path outputDirectory = targetPath;
        for (String packageComponent : javaFile.packageName.split("\\.")) {
            outputDirectory = outputDirectory.resolve(packageComponent);
        }
        return outputDirectory.resolve(javaFile.typeSpec.name + ".java").toFile().exists();
    }

    public void write(JavaFile javaFile) {
        try {
            javaFile.writeToPath(targetPath);
        } catch (IOException e) {
            log.error("write java file error! javaFile:{}", javaFile, e);
        }
    }

}
