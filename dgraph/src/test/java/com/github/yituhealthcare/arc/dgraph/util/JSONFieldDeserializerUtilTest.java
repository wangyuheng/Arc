package com.github.yituhealthcare.arc.dgraph.util;

import com.github.yituhealthcare.arc.dgraph.annotation.DgraphType;
import com.github.yituhealthcare.arc.dgraph.annotation.UidField;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

/**
 * @author junhao.chen
 * @date 2020/8/7
 */
public class JSONFieldDeserializerUtilTest {

    @DgraphType("TASK")
    private static class Task {
        @UidField
        private String id;
        private String name;
        private List<Question> questions;
    }

    @DgraphType("QUESTION")
    private static class Question {
        @UidField
        private String id;
        @UnionClasses(ImageDefine.class)
        private Object define;
    }

    @DgraphType("DEFINE")
    private static class ImageDefine {
        @UidField
        private String id;
        private SubjectForm subjectForm;
        private LocationForm locationForm;
        private JSONObject displayConfigs;
    }

    @DgraphType("SUBJECTFORM")
    private static class SubjectForm {
        private List<FormItemConfig> formItems;
    }

    @DgraphType("FORM_ITEM_CONFIG")
    private static class FormItemConfig {
        private JSONObject configDetail;
    }

    @DgraphType("LOCATION_FORM")
    private static class LocationForm {
        private List<FormItemConfig> formItems;
    }

    @Test
    public void test_get_json_map() {
        Class clazz = Task.class;
        Map<String, Object> map = JSONFieldDeserializerUtil.getJsonMap(clazz);
        assertTrue(map.containsKey("TASK.questions"));
        Map<String, Object> question = (Map<String, Object>) map.get("TASK.questions");
        assertTrue(question.containsKey("QUESTION.define"));
        Map<String, Object> define = (Map<String, Object>) question.get("QUESTION.define");
        assertTrue(define.containsKey("DEFINE.displayConfigs"));
        assertNull(define.get("DEFINE.displayConfigs"));
        assertTrue(define.containsKey("DEFINE.subjectForm"));
        Map<String, Object> subjectForm = (Map<String, Object>) define.get("DEFINE.subjectForm");
        assertTrue(subjectForm.containsKey("SUBJECTFORM.formItems"));
        Map<String, Object> subjectFormFormItems = (Map<String, Object>) subjectForm.get("SUBJECTFORM.formItems");
        assertTrue(subjectFormFormItems.containsKey("FORM_ITEM_CONFIG.configDetail"));
        assertNull(subjectFormFormItems.get("FORM_ITEM_CONFIG.configDetail"));
        assertTrue(define.containsKey("DEFINE.locationForm"));
        Map<String, Object> locationForm = (Map<String, Object>) define.get("DEFINE.locationForm");
        assertTrue(locationForm.containsKey("LOCATION_FORM.formItems"));
        Map<String, Object> locationFormFormItems = (Map<String, Object>) locationForm.get("LOCATION_FORM.formItems");
        assertTrue(locationFormFormItems.containsKey("FORM_ITEM_CONFIG.configDetail"));
        assertNull(locationFormFormItems.get("FORM_ITEM_CONFIG.configDetail"));
    }

    @Test
    public void test_change_json() {
        Map<String, Object> map = generateMap4Test();
        String jsonString = "{\"TASK.name\":\"Task072004\",\"uid\":\"0x3d07\",\"domainClass\":\"com.github.yituhealthcare.stark.domain.task.type.Task\",\"TASK.questions\":[{\"uid\":\"0x3d09\",\"QUESTION.define\":{\"DEFINE.locationForm\":{\"uid\":\"0x3d06\",\"LOCATION_FORM.grouped\":false,\"LOCATION_FORM.allowDeletingOriginLabels\":false,\"LOCATION_FORM.formItems\":[{\"uid\":\"0x3d0a\",\"FORM_ITEM_CONFIG.key\":\"type\",\"FORM_ITEM_CONFIG.title\":\"病灶类型\",\"FORM_ITEM_CONFIG.configDetail\":\"{\\\"optional\\\":[\\\"非钙化\\\",\\\"钙化\\\"]}\",\"FORM_ITEM_CONFIG.controlType\":\"SINGLE_SELECT\",\"domainClass\":\"com.github.yituhealthcare.stark.domain.task.type.FormItemConfig\"}],\"LOCATION_FORM.allowAddingLabels\":false,\"domainClass\":\"com.github.yituhealthcare.stark.domain.task.type.LocationForm\",\"LOCATION_FORM.locationLabelType\":\"CIRCLE\"},\"uid\":\"0x3d0b\",\"domainClass\":\"com.github.yituhealthcare.stark.domain.task.type.ImageDefine\",\"DEFINE.displayConfigs\":\"{\\\"a\\\":\\\"abc\\\"}\"},\"domainClass\":\"com.github.yituhealthcare.stark.domain.task.type.Question\"},{\"uid\":\"0x3d0c\",\"QUESTION.define\":{\"DEFINE.locationForm\":{\"uid\":\"0x3d08\",\"LOCATION_FORM.grouped\":false,\"LOCATION_FORM.allowDeletingOriginLabels\":false,\"LOCATION_FORM.formItems\":[{\"uid\":\"0x3d0d\",\"FORM_ITEM_CONFIG.key\":\"type\",\"FORM_ITEM_CONFIG.title\":\"病灶类型\",\"FORM_ITEM_CONFIG.configDetail\":\"{\\\"optional\\\":[\\\"非钙化\\\",\\\"钙化\\\"]}\",\"FORM_ITEM_CONFIG.controlType\":\"SINGLE_SELECT\",\"domainClass\":\"com.github.yituhealthcare.stark.domain.task.type.FormItemConfig\"}],\"LOCATION_FORM.allowAddingLabels\":false,\"domainClass\":\"com.github.yituhealthcare.stark.domain.task.type.LocationForm\",\"LOCATION_FORM.locationLabelType\":\"CIRCLE\"},\"uid\":\"0x3d0e\",\"domainClass\":\"com.github.yituhealthcare.stark.domain.task.type.ImageDefine\",\"DEFINE.displayConfigs\":\"{\\\"a\\\":\\\"abc\\\"}\"},\"domainClass\":\"com.github.yituhealthcare.stark.domain.task.type.Question\"}]}";
        JSONObject json = JSONObject.parseObject(jsonString);
        JSONFieldDeserializerUtil.changeJson(json, map);
        json.getJSONArray("TASK.questions").stream().map(it -> (JSONObject)it ).forEach(question -> {
            assertEquals(question.getJSONObject("QUESTION.define").getJSONObject("DEFINE.displayConfigs").getClass(),JSONObject.class);
            question.getJSONObject("QUESTION.define")
                    .getJSONObject("DEFINE.locationForm")
                    .getJSONArray("LOCATION_FORM.formItems")
                    .forEach(formItem -> assertEquals(formItem.getClass(),JSONObject.class));

        });
    }

    private Map<String, Object> generateMap4Test() {
        Map<String, Object> locationFormFormItems = new HashMap<>();
        locationFormFormItems.put("FORM_ITEM_CONFIG.configDetail", null);
        Map<String, Object> locationForm = new HashMap<>();
        locationForm.put("LOCATION_FORM.formItems", locationFormFormItems);
        Map<String, Object> subjectFormFormItems = new HashMap<>();
        subjectFormFormItems.put("FORM_ITEM_CONFIG.configDetail", null);
        Map<String, Object> subjectForm = new HashMap<>();
        subjectForm.put("SUBJECTFORM.formItems", subjectFormFormItems);
        Map<String, Object> define = new HashMap<>();
        define.put("DEFINE.subjectForm", subjectForm);
        define.put("DEFINE.locationForm", locationForm);
        define.put("DEFINE.displayConfigs", null);
        Map<String, Object> question = new HashMap<>();
        question.put("QUESTION.define", define);
        Map<String, Object> task = new HashMap<>();
        task.put("TASK.questions", question);
        return task;
    }

    @DgraphType("PARENT")
    private static class ParentClass{
        private A a;
        private D d;
    }

    @DgraphType("A")
    private static class A{
        private B b;
    }

    @DgraphType("B")
    private static class B{
        private C c;
    }

    @DgraphType("C")
    private static class C{
        private JSONObject json;
        private A a;
    }

    @DgraphType("D")
    private static class D{
        private E e;
    }

    @DgraphType("E")
    private static class E{
        private F f;
    }

    @DgraphType("F")
    private static class F{
        private Map map;
        private F f;
    }

    @Test
    public void test_get_nested_class_json_map(){
        Class clazz = ParentClass.class;
        Map<String, Object> map = JSONFieldDeserializerUtil.getJsonMap(clazz);
        JSONObject json = new JSONObject(map);
        assertNull(json.getJSONObject("PARENT.d").getJSONObject("D.e").getJSONObject("E.f").getString("F.map"));
        assertNull(json.getJSONObject("PARENT.a").getJSONObject("A.b").getJSONObject("B.c").getString("C.json"));
    }

}
