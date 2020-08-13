package ai.care.arc.dgraph.util;

import com.alibaba.fastjson.JSONObject;
import ai.care.arc.dgraph.annotation.DgraphType;
import ai.care.arc.dgraph.annotation.UidField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author junhao.chen
 * @date 2020/8/7
 */
public class JSONFieldDeserializerUtilTest {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @DgraphType("TASK")
    private static class Task {
        @UidField
        private String id;
        private String name;
        private List<Question> questions;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @DgraphType("QUESTION")
    private static class Question {
        @UidField
        private String id;
        @UnionClasses(ImageDefine.class)
        private Object define;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @DgraphType("DEFINE")
    private static class ImageDefine {
        @UidField
        private String id;
        private SubjectForm subjectForm;
        private LocationForm locationForm;
        private JSONObject displayConfigs;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @DgraphType("SUBJECTFORM")
    private static class SubjectForm {
        private List<FormItemConfig> formItems;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @DgraphType("FORM_ITEM_CONFIG")
    private static class FormItemConfig {
        private JSONObject configDetail;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @DgraphType("LOCATION_FORM")
    private static class LocationForm {
        private List<FormItemConfig> formItems;
    }

    @Test
    public void test_get_json_map() {
        Class clazz = Task.class;
        Map<String, Object> map = JSONFieldDeserializerUtil.getJsonMap(Arrays.asList(clazz));
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
        String jsonString = "{\"TASK.name\":\"Task072004\",\"uid\":\"0x3d07\",\"domainClass\":\"ai.care.stark.domain.task.type.Task\",\"TASK.questions\":[{\"uid\":\"0x3d09\",\"QUESTION.define\":{\"DEFINE.locationForm\":{\"uid\":\"0x3d06\",\"LOCATION_FORM.grouped\":false,\"LOCATION_FORM.allowDeletingOriginLabels\":false,\"LOCATION_FORM.formItems\":[{\"uid\":\"0x3d0a\",\"FORM_ITEM_CONFIG.key\":\"type\",\"FORM_ITEM_CONFIG.title\":\"病灶类型\",\"FORM_ITEM_CONFIG.configDetail\":\"{\\\"optional\\\":[\\\"非钙化\\\",\\\"钙化\\\"]}\",\"FORM_ITEM_CONFIG.controlType\":\"SINGLE_SELECT\",\"domainClass\":\"ai.care.stark.domain.task.type.FormItemConfig\"}],\"LOCATION_FORM.allowAddingLabels\":false,\"domainClass\":\"ai.care.stark.domain.task.type.LocationForm\",\"LOCATION_FORM.locationLabelType\":\"CIRCLE\"},\"uid\":\"0x3d0b\",\"domainClass\":\"ai.care.stark.domain.task.type.ImageDefine\",\"DEFINE.displayConfigs\":\"{\\\"a\\\":\\\"abc\\\"}\"},\"domainClass\":\"ai.care.stark.domain.task.type.Question\"},{\"uid\":\"0x3d0c\",\"QUESTION.define\":{\"DEFINE.locationForm\":{\"uid\":\"0x3d08\",\"LOCATION_FORM.grouped\":false,\"LOCATION_FORM.allowDeletingOriginLabels\":false,\"LOCATION_FORM.formItems\":[{\"uid\":\"0x3d0d\",\"FORM_ITEM_CONFIG.key\":\"type\",\"FORM_ITEM_CONFIG.title\":\"病灶类型\",\"FORM_ITEM_CONFIG.configDetail\":\"{\\\"optional\\\":[\\\"非钙化\\\",\\\"钙化\\\"]}\",\"FORM_ITEM_CONFIG.controlType\":\"SINGLE_SELECT\",\"domainClass\":\"ai.care.stark.domain.task.type.FormItemConfig\"}],\"LOCATION_FORM.allowAddingLabels\":false,\"domainClass\":\"ai.care.stark.domain.task.type.LocationForm\",\"LOCATION_FORM.locationLabelType\":\"CIRCLE\"},\"uid\":\"0x3d0e\",\"domainClass\":\"ai.care.stark.domain.task.type.ImageDefine\",\"DEFINE.displayConfigs\":\"{\\\"a\\\":\\\"abc\\\"}\"},\"domainClass\":\"ai.care.stark.domain.task.type.Question\"}]}";
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

}
