package ai.care.arc.generator.io;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.junit.Test;

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

}