package ai.care.arc.generator.codegen.util;

import ai.care.arc.generator.dictionary.GeneratorGlobalConst;
import graphql.language.Description;
import graphql.language.ObjectTypeDefinition;
import graphql.language.TypeDefinition;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link JavadocUtils}.
 *
 * @author yuheng.wang
 */
public class JavadocUtilsTest {

    @Test
    public void should_get_description() {
        Description description = new Description("This is A Object Type", null, false);
        TypeDefinition<?> typeDefinition = ObjectTypeDefinition.newObjectTypeDefinition().name("o1").build();

        assertEquals("This is A Object Type\n" +
                GeneratorGlobalConst.GENERAL_CODE_BLOCK, JavadocUtils.getDocForType(typeDefinition, description).toString());
    }

    @Test
    public void should_get_type_name_if_content_is_null() {
        Description description = new Description(null, null, false);
        TypeDefinition<?> typeDefinition = ObjectTypeDefinition.newObjectTypeDefinition().name("o1").build();

        assertEquals("o1\n" +
                GeneratorGlobalConst.GENERAL_CODE_BLOCK, JavadocUtils.getDocForType(typeDefinition, description).toString());
    }

    @Test
    public void should_get_type_name_if_description_is_null() {
        TypeDefinition<?> typeDefinition = ObjectTypeDefinition.newObjectTypeDefinition().name("o1").build();

        assertEquals("o1\n" +
                GeneratorGlobalConst.GENERAL_CODE_BLOCK, JavadocUtils.getDocForType(typeDefinition, null).toString());
    }

}