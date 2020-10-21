package ai.care.arc.generator.codegen;

import ai.care.arc.generator.GenE2ECase;
import ai.care.arc.generator.GenE2EExtension;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Tests for {@link RepositoryGenerator}.
 *
 * @author yuheng.wang
 */
@ExtendWith({GenE2EExtension.class})
public class RepositoryGeneratorTest {

    @GenE2ECase(clazz = RepositoryGenerator.class, schemaPath = "repository_test.graphqls", javaPaths = {"repository_test_ProjectRepository.java"})
    public void repo_gen_e2e_test() {
    }

}