package com.github.yituhealthcare.arc.dgraph.repository.mapper.parser;

import com.github.yituhealthcare.arc.dgraph.annotation.DgraphType;
import com.github.yituhealthcare.arc.dgraph.repository.parser.DgraphPostNameFilter;
import com.github.yituhealthcare.arc.dgraph.repository.parser.DgraphPrefixNameFilter;
import com.github.yituhealthcare.arc.dgraph.util.DgraphTypeHolder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DgraphPostNameFilterTest {

    @Before
    public void setUp() {
        BeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(Parent.class).getBeanDefinition();
        DgraphTypeHolder.add(beanDefinition);
    }

    @Test
    public void json_key_should_remove_prefix() {
        DgraphPostNameFilter postNameFilter = new DgraphPostNameFilter();

        JSONObject jsonWithPrefix = getPrefixedJson();
        String jsonStr = JSON.toJSONString(jsonWithPrefix);
        Parent parent = JSON.parseObject(jsonStr, Parent.class);
        assertNull(parent.getName());

        String jsonPostStr = JSON.toJSONString(jsonWithPrefix, postNameFilter);
        parent = JSON.parseObject(jsonPostStr, Parent.class);
        assertEquals("papa", parent.getName());
        assertEquals("b0", parent.getChildren().get(0).getName());
        assertEquals("b1", parent.getChildren().get(1).getName());

    }

    private JSONObject getPrefixedJson() {
        String prefix = "abc" + ".";
        DgraphPrefixNameFilter prefixNameFilter = new DgraphPrefixNameFilter(prefix);
        Parent parent = new Parent();
        parent.setName("papa");
        parent.setChildren(Arrays.asList(new Child("b0"), new Child("b1")));
        String jsonStr = JSON.toJSONString(parent, prefixNameFilter);
        return JSON.parseObject(jsonStr);
    }

    @DgraphType("abc")
    public static class Parent {
        String name;
        List<Child> children;

        public Parent() {
        }

        public String getName() {
            return this.name;
        }

        public List<Child> getChildren() {
            return this.children;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setChildren(List<Child> children) {
            this.children = children;
        }

        public String toString() {
            return "DgraphPostNameFilterTest.Parent(name=" + this.getName() + ", children=" + this.getChildren() + ")";
        }
    }

    @DgraphType("a")
    public static class Uncle {
        String name;
        List<Child> children;

        public Uncle() {
        }

        public String getName() {
            return this.name;
        }

        public List<Child> getChildren() {
            return this.children;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setChildren(List<Child> children) {
            this.children = children;
        }

        public String toString() {
            return "DgraphPostNameFilterTest.Uncle(name=" + this.getName() + ", children=" + this.getChildren() + ")";
        }
    }

    public static class Child {
        String name;

        public Child(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String toString() {
            return "DgraphPostNameFilterTest.Child(name=" + this.getName() + ")";
        }
    }

}