package io.github.wangyuheng.arc.core.dictionary;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for {@link GraphqlFieldTypeEnum}.
 *
 * @author yuheng.wang
 */
public class GraphqlFieldTypeEnumTest {

    @Test
    public void should_parse_enum_by_correct_key() {
        assertTrue(GraphqlFieldTypeEnum.parse("string").isPresent());
        assertEquals(GraphqlFieldTypeEnum.STRING, GraphqlFieldTypeEnum.parse("string").get());
    }

    @Test
    public void should_parse_optional_empty_by_illegal_key() {
        assertFalse(GraphqlFieldTypeEnum.parse("abc").isPresent());
    }

    @Test
    public void should_return_true_when_key_existed() {
        assertFalse(GraphqlFieldTypeEnum.exist("abc"));
        assertTrue(GraphqlFieldTypeEnum.exist(GraphqlFieldTypeEnum.STRING.getKey()));
    }
}