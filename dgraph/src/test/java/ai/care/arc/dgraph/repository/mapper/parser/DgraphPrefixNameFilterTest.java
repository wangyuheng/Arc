package ai.care.arc.dgraph.repository.mapper.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import ai.care.arc.dgraph.repository.parser.DgraphPrefixNameFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DgraphPrefixNameFilterTest {

    @Test
    public void json_key_should_append_prefix() {
        String prefix = "abc" + ".";
        DgraphPrefixNameFilter prefixNameFilter = new DgraphPrefixNameFilter(prefix);

        Parent parent = new Parent();
        parent.setName("papa");
        parent.setChildren(Arrays.asList(new Child("b0"), new Child("b1")));

        String jsonStr = JSON.toJSONString(parent, prefixNameFilter);
        JSONObject json = JSON.parseObject(jsonStr);

        assertEquals("papa", json.getString("abc.name"));
        assertEquals("b0", json.getJSONArray("abc.children").getJSONObject(0).getString("abc.name"));
        assertEquals("b1", json.getJSONArray("abc.children").getJSONObject(1).getString("abc.name"));
    }

    @Data
    static class Parent {
        String name;
        List<Child> children;
    }

    @Data
    @AllArgsConstructor
    static class Child {
        String name;
    }
}