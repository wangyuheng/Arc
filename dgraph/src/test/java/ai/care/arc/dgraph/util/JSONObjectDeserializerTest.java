package ai.care.arc.dgraph.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import ai.care.arc.dgraph.annotation.UidField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author junhao.chen
 * @date 2020/8/7
 */

public class JSONObjectDeserializerTest {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Parent{
        @UidField
        private String id;
        private String name;
        private Integer age;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Children extends Parent{
        private String name;
        private Integer age;
        private String hobby;
    }

    @Test
    public void test_uid_field_in_parent_class(){
        Children children = new Children("杰哥",11,"捉妖");
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(children));
        jsonObject.put("domainClass","ai.care.arc.dgraph.util.JSONObjectDeserializerTest$Children");
        String id = "12345";
        jsonObject.put("uid",id);
        Children result = JSON.parseObject(jsonObject.toJSONString(),Children.class,new JSONObjectDeserializer());
        assertEquals(result.getId(),id);
    }
}
