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
        TypeDefinition<?> typeDefinition = ObjectTypeDefinition.newObjectTypeDefinition()
                .name("o1")
                .description(new Description("This is A Object Type", null, false))
                .build();

        assertEquals("This is A Object Type\n" + GeneratorGlobalConst.GENERAL_CODE_BLOCK,
                JavadocUtils.getDocForType(typeDefinition).toString());
    }

    @Test
    public void should_get_type_name_if_description_content_is_null() {
        TypeDefinition<?> typeDefinition = ObjectTypeDefinition.newObjectTypeDefinition()
                .name("o1")
                .description(new Description(null, null, false))
                .build();

        assertEquals("o1\n" + GeneratorGlobalConst.GENERAL_CODE_BLOCK,
                JavadocUtils.getDocForType(typeDefinition).toString());
    }

    @Test
    public void should_get_default_value_if_description_content_is_null() {
        Description description = new Description(null, null, false);

        assertEquals("d1\n" + GeneratorGlobalConst.GENERAL_CODE_BLOCK,
                JavadocUtils.getClassDocByDescription(description, "d1").toString());
    }

    @Test
    public void should_get_description_without_general_by_field() {
        Description description = new Description("Field1", null, false);

        assertEquals("Field1", JavadocUtils.getFieldDocByDescription(description, "Field1").toString());
    }

}