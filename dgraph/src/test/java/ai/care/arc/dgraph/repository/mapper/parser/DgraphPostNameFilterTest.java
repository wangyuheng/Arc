package ai.care.arc.dgraph.repository.mapper.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import ai.care.arc.dgraph.annotation.DgraphType;
import ai.care.arc.dgraph.repository.parser.DgraphPostNameFilter;
import ai.care.arc.dgraph.repository.parser.DgraphPrefixNameFilter;
import ai.care.arc.dgraph.util.DgraphTypeHolder;
import lombok.AllArgsConstructor;
import lombok.Data;
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

    @Data
    @DgraphType("abc")
    public static class Parent {
        String name;
        List<Child> children;
    }

    @Data
    @DgraphType("a")
    public static class Uncle {
        String name;
        List<Child> children;
    }

    @Data
    @AllArgsConstructor
    public static class Child {
        String name;
    }

}