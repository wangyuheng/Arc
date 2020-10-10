package ai.care.arc.generator;

import ai.care.arc.generator.conf.CodeGenConfig;
import ai.care.arc.generator.conf.CodeGenOperation;
import ai.care.arc.generator.conf.CodeGenStrategy;
import ai.care.arc.generator.conf.CodeGenType;
import ai.care.arc.generator.io.CodeWriter;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;

@Ignore
public class JavaCodeGeneratorTest {

    @Test
    public void gene_java_code() throws IOException {
        ClassPathResource schema = new ClassPathResource("schema.graphqls");
        CodeWriter codeWriter = new CodeWriter(Paths.get(".", "/src/main/java"));
        CodeGenConfig config = new CodeGenConfig(Collections.singletonList(new CodeGenStrategy(CodeGenType.REPO, CodeGenOperation.SKIP_IF_EXISTED)));

        new JavaCodeGenerator(codeWriter, config).generate(schema.getInputStream(), "ai.care.generatorsample.generated");
    }

}
