package io.github.wangyuheng.arc.generator.conf;

import io.github.wangyuheng.arc.generator.io.CodeWriter;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link CodeGenConfigHandler}.
 *
 * @author yuheng.wang
 */
public class CodeGenConfigHandlerTest {

    @Test
    public void can_exec_should_return_false_when_config_ignore_java_file_by_name() {
        CodeWriter codeWriter = new CodeWriter();
        CodeGenConfig config = new CodeGenConfig();
        config.setIgnoreJavaFileNames(Collections.singletonList("IgnoreJavaFile"));
        JavaFile javaFile = JavaFile.builder("a.b.c", TypeSpec.classBuilder("IgnoreJavaFile").build()).build();
        assertFalse(new CodeGenConfigHandler(codeWriter, config).canExec().test(javaFile));
    }

    @Test
    public void can_exec_should_return_true_default_config() {
        CodeWriter codeWriter = new CodeWriter();
        CodeGenConfig config = new CodeGenConfig();
        JavaFile javaFile = JavaFile.builder("type", TypeSpec.classBuilder("RandomJavaFile").build()).build();
        assertTrue(new CodeGenConfigHandler(codeWriter, config).canExec().test(javaFile));
    }

    @Test
    public void can_exec_should_return_true_when_override() {
        assertTrue(this.buildRandomJavaFileCanExec(CodeGenOperation.OVERRIDE, new CodeWriter()));
    }

    @Test
    public void can_exec_should_return_false_when_skip() {
        assertFalse(this.buildRandomJavaFileCanExec(CodeGenOperation.SKIP, new CodeWriter()));
    }

    @Test
    public void can_exec_should_return_true_when_skip_is_existed_and_code_writer_exist() {
        CodeWriter codeWriter = Mockito.mock(CodeWriter.class);
        when(codeWriter.exist(any())).thenReturn(true);
        assertFalse(this.buildRandomJavaFileCanExec(CodeGenOperation.SKIP_IF_EXISTED, codeWriter));
    }

    @Test
    public void can_exec_should_return_false_when_skip_is_existed_and_code_writer_not_exist() {
        CodeWriter codeWriter = Mockito.mock(CodeWriter.class);
        when(codeWriter.exist(any())).thenReturn(false);
        assertTrue(this.buildRandomJavaFileCanExec(CodeGenOperation.SKIP_IF_EXISTED, codeWriter));
    }

    private boolean buildRandomJavaFileCanExec(CodeGenOperation codeGenOperation, CodeWriter codeWriter) {
        CodeGenConfig config = new CodeGenConfig();
        config.setGenStrategies(Collections.singletonList(new CodeGenStrategy(CodeGenType.TYPE, codeGenOperation)));
        JavaFile javaFile = JavaFile.builder("type", TypeSpec.classBuilder("RandomJavaFile").build()).build();
        return new CodeGenConfigHandler(codeWriter, config).canExec().test(javaFile);
    }

}