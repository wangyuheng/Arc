package io.github.wangyuheng.arc.generator;

import io.github.wangyuheng.arc.generator.conf.CodeGenConfig;
import io.github.wangyuheng.arc.generator.io.CodeWriter;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.doNothing;

/**
 * Tests for {@link JavaCodeGenerator}.
 *
 * @author yuheng.wang
 */
@Ignore
public class JavaCodeGeneratorTest {

    @Test
    public void should_ignore_write_if_config_ignore() throws IOException {
        CodeWriter codeWriter = PowerMockito.mock(CodeWriter.class);
        doNothing().when(codeWriter).write(any());
        new JavaCodeGenerator(codeWriter).generate(new ClassPathResource("java_code_generator_schema.graphqls", this.getClass().getClassLoader()).getInputStream());
        Mockito.verify(codeWriter, Mockito.times(13)).write(any());

        CodeGenConfig config = new CodeGenConfig();
        config.setIgnoreJavaFileNames(Arrays.asList("Project", "ProjectDataFetcher"));
        new JavaCodeGenerator(codeWriter, config).generate(new ClassPathResource("java_code_generator_schema.graphqls", this.getClass().getClassLoader()).getInputStream());
        Mockito.verify(codeWriter, Mockito.times(13 * 2 - 2)).write(any());
    }

}