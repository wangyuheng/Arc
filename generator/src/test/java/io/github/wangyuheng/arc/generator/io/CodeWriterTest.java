package io.github.wangyuheng.arc.generator.io;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.junit.Test;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    @Test
    public void should_generator_with_indent_4() throws IOException {
        Path path = Files.createTempDirectory("code_writer");
        JavaFile javaFile = JavaFile.builder("a.b.c", TypeSpec.enumBuilder("MockEnum")
                .addEnumConstant("A")
                .addEnumConstant("B")
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("This is a mock enum!").build()).build();

        new CodeWriter(path).write(javaFile);

        assertTrue(Files.readAllLines(Paths.get(path.toString(), "a", "b", "c", "MockEnum.java"))
                .stream()
                .filter(it -> it.endsWith("A,") || it.endsWith("B,"))
                .allMatch(it -> it.startsWith("    ")));
    }

}