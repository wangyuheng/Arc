package ai.care.arc.generator.io;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link CodeWriter}.
 *
 * @author yuheng.wang
 */
public class CodeWriterTest {

    @Test
    public void should_return_true_if_java_exist() {
        assertTrue(new CodeWriter().exist(JavaFile.builder(CodeWriter.class.getPackage().getName(), TypeSpec.classBuilder(CodeWriter.class.getSimpleName()).build()).build()));
    }

    @Test
    public void should_return_false_if_java_not_exist_when_path_test() {
        assertFalse(new CodeWriter(Paths.get(".", "/src/test/java")).exist(JavaFile.builder(CodeWriter.class.getPackage().getName(), TypeSpec.classBuilder(CodeWriter.class.getSimpleName()).build()).build()));
    }

}