package ai.care.arc.generator;

import ai.care.arc.generator.io.CodeWriter;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertArrayEquals;

@Ignore
public class JavaCodeGeneratorTest {

    private final String BASE_PACKAGE = "ai.care.generatorsample.generated";
    private final String BASE_PACKAGE_PATH = BASE_PACKAGE.replaceAll("\\.", "/");
    private final Path TEST_DATA_PATH = Paths.get(".", "/src/test/resources/testdata", BASE_PACKAGE_PATH);

    @Test
    public void gene_java_code_e2e_test() throws IOException {
        String generatedPath = "./src/test/resources/gd3";
        Path generatedPathWithPackage = Paths.get(generatedPath, BASE_PACKAGE_PATH);

        ClassPathResource schema = new ClassPathResource("schema.graphqls");
        CodeWriter codeWriter = new CodeWriter(Paths.get(generatedPath));
        new JavaCodeGenerator(codeWriter).generate(schema.getInputStream(), BASE_PACKAGE);

        try (Stream<Path> walk = Files.walk(generatedPathWithPackage)) {
            List<Path> paths = walk.filter(Files::isRegularFile).collect(Collectors.toList());
            for (Path p : paths) {
                Path tmp = Paths.get(TEST_DATA_PATH.toString(), p.toString().replace(generatedPathWithPackage.toString(), ""));
                assertArrayEquals(Files.readAllBytes(tmp), Files.readAllBytes(p));
            }
        }

    }
}
