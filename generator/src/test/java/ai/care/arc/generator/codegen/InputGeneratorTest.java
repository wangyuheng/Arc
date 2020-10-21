package ai.care.arc.generator.codegen;

import ai.care.arc.generator.GenE2ECase;
import ai.care.arc.generator.GenE2EExtension;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Tests for {@link InputGenerator}.
 *
 * @author yuheng.wang
 */
@ExtendWith({GenE2EExtension.class})
public class InputGeneratorTest {

    @GenE2ECase(clazz = InputGenerator.class, schemaPath = "input_test.graphqls", javaPaths = {"input_test_I1.java"})
    public void input_gen_e2e_test() {
    }

}