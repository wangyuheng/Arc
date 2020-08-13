package ai.care.arc.dgraph.repository.mapper.parser;

import com.alibaba.fastjson.JSONObject;
import ai.care.arc.dgraph.annotation.DgraphType;
import ai.care.arc.dgraph.annotation.RelationshipField;
import ai.care.arc.dgraph.repository.parser.DgraphParser;
import ai.care.arc.dgraph.util.UnionClasses;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DgraphParserTest {

    @Test
    public void should_get_custom_ref() {
        DgraphParser dgraphParser = new DgraphParser(Parent.class);
        Parent parent = new Parent();
        parent.setName("papa");
        parent.setChildren(Arrays.asList(new Child("b0"), new Child("b1")));
        JSONObject json = dgraphParser.parseDecoratorJSON(parent);
        assertEquals(2, json.getJSONArray("association").size());
    }

    @Data
    @DgraphType
    static class Parent {
        private String name;
        @RelationshipField(value = "association", reverse = true)
        private List<Child> children;
    }

    @Data
    @AllArgsConstructor
    static class Child {
        private String name;
    }

    @Test
    public void union_class_should_get_domain_class() {
        DgraphParser dgraphParser = new DgraphParser(A.class);
        String jsonString = "{\n" +
                "    \"id\":\"12dca\",\n" +
                "    \"unionField\":{\n" +
                "        \"id\":\"asdgg1\",\n" +
                "        \"domainClass\":\"ai.care.arc.dgraph.repository.mapper.parser.DgraphParserTest$B\"\n" +
                "    },\n" +
                "    \"UnUnionField\":{\n" +
                "         \"id\":\"asdav14\",   \n" +
                "         \"unionField\":{\n" +
                "             \"id\":\"bvlhj1\",\n" +
                "             \"domainClass\":\"ai.care.arc.dgraph.repository.mapper.parser.DgraphParserTest$C\"\n" +
                "         }\n" +
                "    }\n" +
                "}";
        A a = (A) dgraphParser.extractJSON(JSONObject.parseObject(jsonString)).get();
        assertEquals("12dca",a.getId());
        assertEquals(B.class,a.getUnionField().getClass());
        assertEquals("asdgg1",((B) a.getUnionField()).getId());
        assertEquals("asdav14",a.getUnUnionField().getId());
        assertEquals(C.class,a.getUnUnionField().getUnionField().getClass());
        assertEquals("bvlhj1",((C)a.getUnUnionField().getUnionField()).getId());
    }


    @Data
    private static class A {
        private String id;
        @UnionClasses({B.class, C.class})
        private Object unionField;
        private D UnUnionField;
    }

    @Data
    private static class B {
        private String id;
    }

    @Data
    private static class C {
        private String id;
    }

    @Data
    private static class D {
        private String id;
        @UnionClasses({B.class, C.class})
        private Object unionField;
    }

}