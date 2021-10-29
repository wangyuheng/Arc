package io.github.wangyuheng.arc.dgraph.util;

import io.github.wangyuheng.arc.dgraph.annotation.DgraphType;
import io.github.wangyuheng.arc.dgraph.annotation.UidField;
import com.alibaba.fastjson.JSONObject;
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

    @DgraphType("TASK")
    private static class Task {
        @UidField
        private String id;
        private String name;
        private List<Question> questions;

        public Task(String id, String name, List<Question> questions) {
            this.id = id;
            this.name = name;
            this.questions = questions;
        }

        public Task() {
        }

        public static TaskBuilder builder() {
            return new TaskBuilder();
        }

        public String getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public List<Question> getQuestions() {
            return this.questions;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setQuestions(List<Question> questions) {
            this.questions = questions;
        }

        public String toString() {
            return "JSONFieldDeserializerUtilTest.Task(id=" + this.getId() + ", name=" + this.getName() + ", questions=" + this.getQuestions() + ")";
        }

        public static class TaskBuilder {
            private String id;
            private String name;
            private List<Question> questions;

            TaskBuilder() {
            }

            public Task.TaskBuilder id(String id) {
                this.id = id;
                return this;
            }

            public Task.TaskBuilder name(String name) {
                this.name = name;
                return this;
            }

            public Task.TaskBuilder questions(List<Question> questions) {
                this.questions = questions;
                return this;
            }

            public Task build() {
                return new Task(id, name, questions);
            }

            public String toString() {
                return "JSONFieldDeserializerUtilTest.Task.TaskBuilder(id=" + this.id + ", name=" + this.name + ", questions=" + this.questions + ")";
            }
        }
    }

    @DgraphType("QUESTION")
    private static class Question {
        @UidField
        private String id;
        @UnionClasses(ImageDefine.class)
        private Object define;

        public Question(String id, Object define) {
            this.id = id;
            this.define = define;
        }

        public Question() {
        }

        public static QuestionBuilder builder() {
            return new QuestionBuilder();
        }

        public String getId() {
            return this.id;
        }

        public Object getDefine() {
            return this.define;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setDefine(Object define) {
            this.define = define;
        }

        public String toString() {
            return "JSONFieldDeserializerUtilTest.Question(id=" + this.getId() + ", define=" + this.getDefine() + ")";
        }

        public static class QuestionBuilder {
            private String id;
            private Object define;

            QuestionBuilder() {
            }

            public Question.QuestionBuilder id(String id) {
                this.id = id;
                return this;
            }

            public Question.QuestionBuilder define(Object define) {
                this.define = define;
                return this;
            }

            public Question build() {
                return new Question(id, define);
            }

            public String toString() {
                return "JSONFieldDeserializerUtilTest.Question.QuestionBuilder(id=" + this.id + ", define=" + this.define + ")";
            }
        }
    }

    @DgraphType("DEFINE")
    private static class ImageDefine {
        @UidField
        private String id;
        private SubjectForm subjectForm;
        private LocationForm locationForm;
        private JSONObject displayConfigs;

        public ImageDefine(String id, SubjectForm subjectForm, LocationForm locationForm, JSONObject displayConfigs) {
            this.id = id;
            this.subjectForm = subjectForm;
            this.locationForm = locationForm;
            this.displayConfigs = displayConfigs;
        }

        public ImageDefine() {
        }

        public static ImageDefineBuilder builder() {
            return new ImageDefineBuilder();
        }

        public String getId() {
            return this.id;
        }

        public SubjectForm getSubjectForm() {
            return this.subjectForm;
        }

        public LocationForm getLocationForm() {
            return this.locationForm;
        }

        public JSONObject getDisplayConfigs() {
            return this.displayConfigs;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setSubjectForm(SubjectForm subjectForm) {
            this.subjectForm = subjectForm;
        }

        public void setLocationForm(LocationForm locationForm) {
            this.locationForm = locationForm;
        }

        public void setDisplayConfigs(JSONObject displayConfigs) {
            this.displayConfigs = displayConfigs;
        }

        public String toString() {
            return "JSONFieldDeserializerUtilTest.ImageDefine(id=" + this.getId() + ", subjectForm=" + this.getSubjectForm() + ", locationForm=" + this.getLocationForm() + ", displayConfigs=" + this.getDisplayConfigs() + ")";
        }

        public static class ImageDefineBuilder {
            private String id;
            private SubjectForm subjectForm;
            private LocationForm locationForm;
            private JSONObject displayConfigs;

            ImageDefineBuilder() {
            }

            public ImageDefine.ImageDefineBuilder id(String id) {
                this.id = id;
                return this;
            }

            public ImageDefine.ImageDefineBuilder subjectForm(SubjectForm subjectForm) {
                this.subjectForm = subjectForm;
                return this;
            }

            public ImageDefine.ImageDefineBuilder locationForm(LocationForm locationForm) {
                this.locationForm = locationForm;
                return this;
            }

            public ImageDefine.ImageDefineBuilder displayConfigs(JSONObject displayConfigs) {
                this.displayConfigs = displayConfigs;
                return this;
            }

            public ImageDefine build() {
                return new ImageDefine(id, subjectForm, locationForm, displayConfigs);
            }

            public String toString() {
                return "JSONFieldDeserializerUtilTest.ImageDefine.ImageDefineBuilder(id=" + this.id + ", subjectForm=" + this.subjectForm + ", locationForm=" + this.locationForm + ", displayConfigs=" + this.displayConfigs + ")";
            }
        }
    }

    @DgraphType("SUBJECTFORM")
    private static class SubjectForm {
        private List<FormItemConfig> formItems;

        public SubjectForm(List<FormItemConfig> formItems) {
            this.formItems = formItems;
        }

        public SubjectForm() {
        }

        public static SubjectFormBuilder builder() {
            return new SubjectFormBuilder();
        }

        public List<FormItemConfig> getFormItems() {
            return this.formItems;
        }

        public void setFormItems(List<FormItemConfig> formItems) {
            this.formItems = formItems;
        }

        public String toString() {
            return "JSONFieldDeserializerUtilTest.SubjectForm(formItems=" + this.getFormItems() + ")";
        }

        public static class SubjectFormBuilder {
            private List<FormItemConfig> formItems;

            SubjectFormBuilder() {
            }

            public SubjectForm.SubjectFormBuilder formItems(List<FormItemConfig> formItems) {
                this.formItems = formItems;
                return this;
            }

            public SubjectForm build() {
                return new SubjectForm(formItems);
            }

            public String toString() {
                return "JSONFieldDeserializerUtilTest.SubjectForm.SubjectFormBuilder(formItems=" + this.formItems + ")";
            }
        }
    }

    @DgraphType("FORM_ITEM_CONFIG")
    private static class FormItemConfig {
        private JSONObject configDetail;

        public FormItemConfig(JSONObject configDetail) {
            this.configDetail = configDetail;
        }

        public FormItemConfig() {
        }

        public static FormItemConfigBuilder builder() {
            return new FormItemConfigBuilder();
        }

        public JSONObject getConfigDetail() {
            return this.configDetail;
        }

        public void setConfigDetail(JSONObject configDetail) {
            this.configDetail = configDetail;
        }

        public String toString() {
            return "JSONFieldDeserializerUtilTest.FormItemConfig(configDetail=" + this.getConfigDetail() + ")";
        }

        public static class FormItemConfigBuilder {
            private JSONObject configDetail;

            FormItemConfigBuilder() {
            }

            public FormItemConfig.FormItemConfigBuilder configDetail(JSONObject configDetail) {
                this.configDetail = configDetail;
                return this;
            }

            public FormItemConfig build() {
                return new FormItemConfig(configDetail);
            }

            public String toString() {
                return "JSONFieldDeserializerUtilTest.FormItemConfig.FormItemConfigBuilder(configDetail=" + this.configDetail + ")";
            }
        }
    }

    @DgraphType("LOCATION_FORM")
    private static class LocationForm {
        private List<FormItemConfig> formItems;

        public LocationForm(List<FormItemConfig> formItems) {
            this.formItems = formItems;
        }

        public LocationForm() {
        }

        public static LocationFormBuilder builder() {
            return new LocationFormBuilder();
        }

        public List<FormItemConfig> getFormItems() {
            return this.formItems;
        }

        public void setFormItems(List<FormItemConfig> formItems) {
            this.formItems = formItems;
        }

        public String toString() {
            return "JSONFieldDeserializerUtilTest.LocationForm(formItems=" + this.getFormItems() + ")";
        }

        public static class LocationFormBuilder {
            private List<FormItemConfig> formItems;

            LocationFormBuilder() {
            }

            public LocationForm.LocationFormBuilder formItems(List<FormItemConfig> formItems) {
                this.formItems = formItems;
                return this;
            }

            public LocationForm build() {
                return new LocationForm(formItems);
            }

            public String toString() {
                return "JSONFieldDeserializerUtilTest.LocationForm.LocationFormBuilder(formItems=" + this.formItems + ")";
            }
        }
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
        String jsonString = "{\"TASK.name\":\"Task072004\",\"uid\":\"0x3d07\",\"domainClass\":\"io.github.wangyuheng.stark.domain.task.type.Task\",\"TASK.questions\":[{\"uid\":\"0x3d09\",\"QUESTION.define\":{\"DEFINE.locationForm\":{\"uid\":\"0x3d06\",\"LOCATION_FORM.grouped\":false,\"LOCATION_FORM.allowDeletingOriginLabels\":false,\"LOCATION_FORM.formItems\":[{\"uid\":\"0x3d0a\",\"FORM_ITEM_CONFIG.key\":\"type\",\"FORM_ITEM_CONFIG.title\":\"病灶类型\",\"FORM_ITEM_CONFIG.configDetail\":\"{\\\"optional\\\":[\\\"非钙化\\\",\\\"钙化\\\"]}\",\"FORM_ITEM_CONFIG.controlType\":\"SINGLE_SELECT\",\"domainClass\":\"io.github.wangyuheng.stark.domain.task.type.FormItemConfig\"}],\"LOCATION_FORM.allowAddingLabels\":false,\"domainClass\":\"io.github.wangyuheng.stark.domain.task.type.LocationForm\",\"LOCATION_FORM.locationLabelType\":\"CIRCLE\"},\"uid\":\"0x3d0b\",\"domainClass\":\"io.github.wangyuheng.stark.domain.task.type.ImageDefine\",\"DEFINE.displayConfigs\":\"{\\\"a\\\":\\\"abc\\\"}\"},\"domainClass\":\"io.github.wangyuheng.stark.domain.task.type.Question\"},{\"uid\":\"0x3d0c\",\"QUESTION.define\":{\"DEFINE.locationForm\":{\"uid\":\"0x3d08\",\"LOCATION_FORM.grouped\":false,\"LOCATION_FORM.allowDeletingOriginLabels\":false,\"LOCATION_FORM.formItems\":[{\"uid\":\"0x3d0d\",\"FORM_ITEM_CONFIG.key\":\"type\",\"FORM_ITEM_CONFIG.title\":\"病灶类型\",\"FORM_ITEM_CONFIG.configDetail\":\"{\\\"optional\\\":[\\\"非钙化\\\",\\\"钙化\\\"]}\",\"FORM_ITEM_CONFIG.controlType\":\"SINGLE_SELECT\",\"domainClass\":\"io.github.wangyuheng.stark.domain.task.type.FormItemConfig\"}],\"LOCATION_FORM.allowAddingLabels\":false,\"domainClass\":\"io.github.wangyuheng.stark.domain.task.type.LocationForm\",\"LOCATION_FORM.locationLabelType\":\"CIRCLE\"},\"uid\":\"0x3d0e\",\"domainClass\":\"io.github.wangyuheng.stark.domain.task.type.ImageDefine\",\"DEFINE.displayConfigs\":\"{\\\"a\\\":\\\"abc\\\"}\"},\"domainClass\":\"io.github.wangyuheng.stark.domain.task.type.Question\"}]}";
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
