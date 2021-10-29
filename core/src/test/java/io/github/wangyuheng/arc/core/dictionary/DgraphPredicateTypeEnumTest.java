package io.github.wangyuheng.arc.core.dictionary;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for {@link DgraphPredicateTypeEnum}.
 *
 * @author yuheng.wang
 */
public class DgraphPredicateTypeEnumTest {

    @Test
    public void should_parse_enum_by_correct_key() {
        assertTrue(DgraphPredicateTypeEnum.parse("string").isPresent());
        assertEquals(DgraphPredicateTypeEnum.STRING, DgraphPredicateTypeEnum.parse("string").get());
    }

    @Test
    public void should_parse_optional_empty_by_illegal_key() {
        assertFalse(DgraphPredicateTypeEnum.parse("abc").isPresent());
    }

    @Test
    public void should_return_true_when_key_existed() {
        assertFalse(DgraphPredicateTypeEnum.exist("abc"));
        assertTrue(DgraphPredicateTypeEnum.exist(DgraphPredicateTypeEnum.STRING.getKey()));
    }

}