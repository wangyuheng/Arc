package ai.care.arc.generator.codegen;

import ai.care.arc.generator.GenE2ECase;
import ai.care.arc.generator.GenE2EExtension;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Tests for {@link DictionaryGenerator}.
 *
 * @author yuheng.wang
 */
@ExtendWith({GenE2EExtension.class})
public class DictionaryGeneratorTest {

    @GenE2ECase(clazz = DictionaryGenerator.class, schemaPath = "dictionary_test.graphqls", javaPaths = {"dictionary_test_E1.java"})
    public void enum_gen_e2e_test() {
    }

}