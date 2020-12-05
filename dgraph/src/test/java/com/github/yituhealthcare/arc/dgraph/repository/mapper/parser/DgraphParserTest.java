package com.github.yituhealthcare.arc.dgraph.repository.mapper.parser;

import com.github.yituhealthcare.arc.dgraph.annotation.DgraphType;
import com.github.yituhealthcare.arc.dgraph.annotation.RelationshipField;
import com.github.yituhealthcare.arc.dgraph.repository.parser.DgraphParser;
import com.github.yituhealthcare.arc.dgraph.util.UnionClasses;
import com.alibaba.fastjson.JSONObject;
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

    @DgraphType
    static class Parent {
        private String name;
        @RelationshipField(value = "association", reverse = true)
        private List<Child> children;

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
            return "DgraphParserTest.Parent(name=" + this.getName() + ", children=" + this.getChildren() + ")";
        }
    }

    static class Child {
        private String name;

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
            return "DgraphParserTest.Child(name=" + this.getName() + ")";
        }
    }

    @Test
    public void union_class_should_get_domain_class() {
        DgraphParser dgraphParser = new DgraphParser(A.class);
        String jsonString = "{\n" +
                "    \"id\":\"12dca\",\n" +
                "    \"unionField\":{\n" +
                "        \"id\":\"asdgg1\",\n" +
                "        \"domainClass\":\"com.github.yituhealthcare.arc.dgraph.repository.mapper.parser.DgraphParserTest$B\"\n" +
                "    },\n" +
                "    \"UnUnionField\":{\n" +
                "         \"id\":\"asdav14\",   \n" +
                "         \"unionField\":{\n" +
                "             \"id\":\"bvlhj1\",\n" +
                "             \"domainClass\":\"com.github.yituhealthcare.arc.dgraph.repository.mapper.parser.DgraphParserTest$C\"\n" +
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


    private static class A {
        private String id;
        @UnionClasses({B.class, C.class})
        private Object unionField;
        private D UnUnionField;

        public A() {
        }

        public String getId() {
            return this.id;
        }

        public Object getUnionField() {
            return this.unionField;
        }

        public D getUnUnionField() {
            return this.UnUnionField;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setUnionField(Object unionField) {
            this.unionField = unionField;
        }

        public void setUnUnionField(D UnUnionField) {
            this.UnUnionField = UnUnionField;
        }

        public String toString() {
            return "DgraphParserTest.A(id=" + this.getId() + ", unionField=" + this.getUnionField() + ", UnUnionField=" + this.getUnUnionField() + ")";
        }
    }

    private static class B {
        private String id;

        public B() {
        }

        public String getId() {
            return this.id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String toString() {
            return "DgraphParserTest.B(id=" + this.getId() + ")";
        }
    }

    private static class C {
        private String id;

        public C() {
        }

        public String getId() {
            return this.id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String toString() {
            return "DgraphParserTest.C(id=" + this.getId() + ")";
        }
    }

    private static class D {
        private String id;
        @UnionClasses({B.class, C.class})
        private Object unionField;

        public D() {
        }

        public String getId() {
            return this.id;
        }

        public Object getUnionField() {
            return this.unionField;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setUnionField(Object unionField) {
            this.unionField = unionField;
        }

        public String toString() {
            return "DgraphParserTest.D(id=" + this.getId() + ", unionField=" + this.getUnionField() + ")";
        }
    }

}