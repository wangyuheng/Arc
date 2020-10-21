package ai.care.arc.generator.codegen;

import ai.care.arc.generator.GenE2ECase;
import ai.care.arc.generator.GenE2EExtension;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Tests for {@link ApiGenerator}.
 *
 * @author yuheng.wang
 */
@ExtendWith({GenE2EExtension.class})
public class ApiGeneratorTest {

    @GenE2ECase(clazz = ApiGenerator.class, schemaPath = "api_test.graphqls", javaPaths = {"api_test_ProjectService.java", "api_test_MutationService.java", "api_test_QueryService.java"})
    public void api_gen_e2e_test() {
    }

}