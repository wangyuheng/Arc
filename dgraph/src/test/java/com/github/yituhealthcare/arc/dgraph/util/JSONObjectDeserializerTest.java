package com.github.yituhealthcare.arc.dgraph.util;

import com.github.yituhealthcare.arc.dgraph.annotation.UidField;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author junhao.chen
 * @date 2020/8/7
 */

public class JSONObjectDeserializerTest {

    private static class Parent{
        @UidField
        private String id;
        private String name;
        private Integer age;

        public Parent(String id, String name, Integer age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        public Parent() {
        }

        public String getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public Integer getAge() {
            return this.age;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String toString() {
            return "JSONObjectDeserializerTest.Parent(id=" + this.getId() + ", name=" + this.getName() + ", age=" + this.getAge() + ")";
        }
    }

    private static class Children extends Parent{
        private String name;
        private Integer age;
        private String hobby;

        public Children(String name, Integer age, String hobby) {
            this.name = name;
            this.age = age;
            this.hobby = hobby;
        }

        public Children() {
        }

        public String getName() {
            return this.name;
        }

        public Integer getAge() {
            return this.age;
        }

        public String getHobby() {
            return this.hobby;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public void setHobby(String hobby) {
            this.hobby = hobby;
        }

        public String toString() {
            return "JSONObjectDeserializerTest.Children(name=" + this.getName() + ", age=" + this.getAge() + ", hobby=" + this.getHobby() + ")";
        }
    }

    @Test
    public void test_uid_field_in_parent_class(){
        Children children = new Children("杰哥",11,"捉妖");
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(children));
        jsonObject.put("domainClass","com.github.yituhealthcare.arc.dgraph.util.JSONObjectDeserializerTest$Children");
        String id = "12345";
        jsonObject.put("uid",id);
        Children result = JSON.parseObject(jsonObject.toJSONString(),Children.class,new JSONObjectDeserializer());
        assertEquals(result.getId(),id);
    }
}
