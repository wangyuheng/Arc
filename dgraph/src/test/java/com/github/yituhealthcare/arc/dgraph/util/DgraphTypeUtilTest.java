package com.github.yituhealthcare.arc.dgraph.util;

import com.github.yituhealthcare.arc.dgraph.annotation.DgraphType;
import com.github.yituhealthcare.arc.dgraph.annotation.RelationshipField;
import com.github.yituhealthcare.arc.dgraph.annotation.UidField;
import org.junit.Test;

import static org.junit.Assert.*;

public class DgraphTypeUtilTest {

    @Test
    public void check_dgraph_type() {
        assertTrue(DgraphTypeUtil.isDgraphType(SingleDgraphType.class));
        assertFalse(DgraphTypeUtil.isDgraphType(NotDgraphTypeClass.class));
        assertFalse(DgraphTypeUtil.isDgraphType(null));
    }

    @Test
    public void should_get_name_if_wrapper() {
        String s1 = DgraphTypeUtil.getFieldWrapper(SingleDgraphType.class, "SingleDgraphTypePrefix.ab");
        String s2 = DgraphTypeUtil.getFieldWrapper(SingleDgraphType.class, "SingleDgraphType.ab");
        assertEquals("SingleDgraphType.SingleDgraphTypePrefix.ab", s1);
        assertEquals("SingleDgraphType.ab", s2);
    }

    @Test
    public void should_get_prefix() {
        assertEquals("SingleDgraphType.", DgraphTypeUtil.getPrefix(SingleDgraphType.class));
    }

    @Test
    public void should_get_null_when_get_not_dgraph_type_prefix() {
        assertNull(DgraphTypeUtil.getPrefix(NotDgraphTypeClass.class));
    }

    @Test
    public void should_match_Superclass_uidField() {
        assertFalse(DgraphTypeUtil.isUidField(Grandson.class, "b"));
        assertTrue(DgraphTypeUtil.isUidField(Grandson.class, "parentId"));
    }

    @Test
    public void should_match_default_uid_field_by_name() {
        assertTrue(DgraphTypeUtil.isUidField(Grandson.class, "uid"));
    }

    @Test
    public void should_get_uid_field_name() {
        assertEquals("parentId", DgraphTypeUtil.getUidFieldSimpleName(Grandson.class));
    }

    @Test
    public void should_get_current_class_name_concat_uid_field_name() {
        assertEquals("Grandson.parentId", DgraphTypeUtil.getUidFieldName(Grandson.class));
    }

    @Test
    public void should_get_dgraph_type_concat_uid_field_name() {
        assertEquals("gdt.parentId", DgraphTypeUtil.getUidFieldName(GrandsonDgraphType.class));
    }

    @Test
    public void should_get_class_simple_name_when_not_set_dgraph_type_value() {
        assertEquals(SingleDgraphType.class.getSimpleName(), DgraphTypeUtil.getDgraphTypeValue(SingleDgraphType.class));
    }

    @Test
    public void should_get_null_when_not_set_dgraph_type() {
        assertNull(DgraphTypeUtil.getDgraphTypeValue(NotDgraphTypeClass.class));
    }

    @Test
    public void should_get_custom_relation_value() {
        assertEquals("customRef", DgraphTypeUtil.getRelationshipField(ParentWithUidField.class, "refWithCustom"));
    }

    @Test
    public void should_get_fieldName_if_field_not_existed() {
        assertEquals("abc", DgraphTypeUtil.getRelationshipField(ParentWithUidField.class, "abc"));
    }

    @Test
    public void should_get_field_as_relationship() {
        assertEquals("ref", DgraphTypeUtil.getRelationshipField(ParentWithUidField.class, "ref"));
    }

    @Test
    public void should_get_wrapper_uid_value() {
        assertEquals("_:a", DgraphTypeUtil.getUidFieldDbWrapper("a"));
    }

    @DgraphType
    static class ParentWithUidField {
        @UidField
        private String parentId;
        @RelationshipField(value = "customRef")
        private String refWithCustom;
        @RelationshipField
        private String ref;
    }

    static class ParentWithUid {
        private String uid;
    }

    static class Child extends ParentWithUidField {
        private String b;

    }

    static class Grandson extends Child {

    }

    @DgraphType("gdt")
    static class GrandsonDgraphType extends Child {

    }

    @DgraphType
    static class SingleDgraphType {
    }

    static class NotDgraphTypeClass {
    }

}