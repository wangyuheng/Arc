package io.github.wangyuheng.arc.core.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DomainClassUtilTest {

    @Test
    public void should_get_class_when_simple_class() {
        Class<?> clazz = DomainClassUtil.getDomainClass(new SimpleClass());
        assertEquals(clazz, SimpleClass.class);
    }

    @Test
    public void should_match_if_field_name_equals_domain_class() {
        assertTrue(DomainClassUtil.isDomainClassKey(DomainClassUtil.DOMAIN_CLASS_KEY));
    }

    @Test
    public void should_get_json_class_when_json_object_without_domain_class_field() {
        Class<?> clazz = DomainClassUtil.getDomainClass(new JSONObject());
        assertEquals(clazz, JSONObject.class);
    }

    @Test
    public void should_get_json_class_when_json_object_with_domain_class_field() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(DomainClassUtil.DOMAIN_CLASS_KEY, SimpleClass.class.getName());
        Class<?> clazz = DomainClassUtil.getDomainClass(jsonObject);
        assertEquals(clazz, SimpleClass.class);
    }

    @Test
    public void should_get_json_class_when_json_array_item_with_domain_class_field() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(DomainClassUtil.DOMAIN_CLASS_KEY, SimpleClass.class.getName());
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject);
        Class<?> clazz = DomainClassUtil.getDomainClass(jsonArray);
        assertEquals(clazz, SimpleClass.class);
    }

    @Test
    public void should_get_json_class_when_list_item_with_domain_class_field() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(DomainClassUtil.DOMAIN_CLASS_KEY, SimpleClass.class.getName());
        List<JSONObject> list = new ArrayList<>();
        list.add(jsonObject);
        Class<?> clazz = DomainClassUtil.getDomainClass(list);
        assertEquals(clazz, SimpleClass.class);
    }


    static class SimpleClass {

    }
}