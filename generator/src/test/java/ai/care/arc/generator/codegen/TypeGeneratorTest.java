package ai.care.arc.generator.codegen;

import ai.care.arc.generator.GenE2ECase;
import ai.care.arc.generator.GenE2EExtension;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Tests for {@link TypeGenerator}.
 *
 * @author yuheng.wang
 */
@ExtendWith({GenE2EExtension.class})
public class TypeGeneratorTest {

    @GenE2ECase(clazz = TypeGenerator.class, schemaPath = "type_test.graphqls", javaPaths = {"type_test_Milestone.java","type_test_Mutation.java","type_test_Project.java","type_test_Query.java","type_test_User.java"})
    public void type_gen_e2e_test() {
    }

    @GenE2ECase(clazz = TypeGenerator.class, schemaPath = "type_union_test.graphqls", javaPaths = {"type_union_test_Milestone.java","type_union_test_Entity.java","type_union_test_Project.java"})
    public void union_type_gen_e2e_test() {
    }



}