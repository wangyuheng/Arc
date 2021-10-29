package io.github.wangyuheng.arc.dgraph.util;

import io.github.wangyuheng.arc.dgraph.annotation.DgraphType;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author junhao.chen
 * @date 2020/11/27
 */
public class DgraphSqlHelperTest {

    @DgraphType
    private static class ExampleClassWithDgraphTypeFieldClass {
        private ExampleClassWithNotDgraphTypeFieldClass dgraphTypeListField;
    }

    @DgraphType
    private static class ExampleClassWithNotDgraphTypeFieldClass {
        private String uid;
    }

    private static class ExampleClassWithUnionClass {
        @UnionClasses({ExampleClassWithDgraphTypeFieldClass.class, ExampleClassWithNotDgraphTypeFieldClass.class})
        private Object unionField;
        @UnionClasses({ExampleClassWithDgraphTypeFieldClass.class, ExampleClassWithNotDgraphTypeFieldClass.class})
        private List<Object> unionListField;
    }

    private static class ExampleClassWithNoGenericListField {
        private List noGenericList;
    }

    private static class ExampleClassWithHasGenericListField {
        private List<String> stringList;
    }


    private static class ExampleClassWithSameNestedClass {
        private ExampleClassWithSameNestedClass sameNestedClassField;
    }

    private static class ExampleParentClass {
        private String uid;
        private ExampleClassWithNotDgraphTypeFieldClass bClass;
        private List<ExampleClassWithNotDgraphTypeFieldClass> bList;
    }

    private Set<String> classWithUnionClassFieldFlatResult;
    private Set<String> classWithNoGenericListFieldFlatResult;
    private Set<String> classWithHasGenericListFieldFlatResult;
    private Set<String> classWithNotDgraphTypeFieldFlatResult;
    private Set<String> classWithDgraphTypeFieldFlatResult;

    @BeforeEach
    public void init() {
        classWithUnionClassFieldFlatResult = Stream.of(
                "ExampleClassWithUnionClass.ExampleClassWithNotDgraphTypeFieldClass.uid",
                "ExampleClassWithUnionClass.ExampleClassWithDgraphTypeFieldClass.ExampleClassWithNotDgraphTypeFieldClass.uid").collect(Collectors.toSet());

        classWithNoGenericListFieldFlatResult = Stream.of(
                "ExampleClassWithNoGenericListField.noGenericList").collect(Collectors.toSet());

        classWithHasGenericListFieldFlatResult = Stream.of(
                "ExampleClassWithHasGenericListField.stringList").collect(Collectors.toSet());

        classWithNotDgraphTypeFieldFlatResult = Stream.of(
                "ExampleClassWithNotDgraphTypeFieldClass.uid").collect(Collectors.toSet());

        classWithDgraphTypeFieldFlatResult = Stream.of(
                "ExampleClassWithDgraphTypeFieldClass.ExampleClassWithNotDgraphTypeFieldClass.uid").collect(Collectors.toSet());
    }

    @Test
    public void test_flat_union_class_field() {
        Set<String> result = DgraphSqlHelper.flatClass(ExampleClassWithUnionClass.class, new ArrayList<>());
        Assert.assertEquals(classWithUnionClassFieldFlatResult, result);
    }

    @Test
    public void test_flat_no_generics_list_field() {
        Set<String> result = DgraphSqlHelper.flatClass(ExampleClassWithNoGenericListField.class, new ArrayList<>());
        Assert.assertEquals(classWithNoGenericListFieldFlatResult, result);
    }

    @Test
    public void test_flat_has_generics_list_field() {
        Set<String> result = DgraphSqlHelper.flatClass(ExampleClassWithHasGenericListField.class, new ArrayList<>());
        Assert.assertEquals(classWithHasGenericListFieldFlatResult, result);
    }

    @Test
    public void test_flat_not_dgraph_type_field() {
        Set<String> result = DgraphSqlHelper.flatClass(ExampleClassWithNotDgraphTypeFieldClass.class, new ArrayList<>());
        Assert.assertEquals(classWithNotDgraphTypeFieldFlatResult, result);
    }

    @Test
    public void test_flat_with_dgraph_type_field() {
        Set<String> result = DgraphSqlHelper.flatClass(ExampleClassWithDgraphTypeFieldClass.class, new ArrayList<>());
        Assert.assertEquals(classWithDgraphTypeFieldFlatResult, result);
    }

    @Test
    public void test_flat_with_same_nested_class_field(){
        Set<String> result = DgraphSqlHelper.flatClass(ExampleClassWithSameNestedClass.class, new ArrayList<>());
        Assert.assertEquals(Collections.EMPTY_SET,result);
    }

    @ParameterizedTest
    @MethodSource("io.github.wangyuheng.arc.dgraph.util.DgraphSqlHelperTest#test_generate_query_parameters")
    public void test_generate_query(AbstractMap.SimpleImmutableEntry<Integer,String> levelResultPair){
        Assert.assertEquals(levelResultPair.getValue(),DgraphSqlHelper.generateQueryByMaxLevel(levelResultPair.getKey()));
    }

    private static Stream<AbstractMap.SimpleImmutableEntry<Integer, String>> test_generate_query_parameters() {
        return Stream.of(new AbstractMap.SimpleImmutableEntry<>(1, "{ \n uid \n expand(_all_)\n}"),
                new AbstractMap.SimpleImmutableEntry<>(2, "{ \n uid \n expand(_all_){ \n uid \n expand(_all_)\n}\n}"),
                new AbstractMap.SimpleImmutableEntry<>(3, "{ \n uid \n expand(_all_){ \n uid \n expand(_all_){ \n uid \n expand(_all_)\n}\n}\n}"),
                new AbstractMap.SimpleImmutableEntry<>(4, "{ \n uid \n expand(_all_){ \n uid \n expand(_all_){ \n uid \n expand(_all_){ \n uid \n expand(_all_)\n}\n}\n}\n}"),
                new AbstractMap.SimpleImmutableEntry<>(5, "{ \n uid \n expand(_all_){ \n uid \n expand(_all_){ \n uid \n expand(_all_){ \n uid \n expand(_all_){ \n uid \n expand(_all_)\n}\n}\n}\n}\n}"));
    }

    @Test
    public void test_get_var_compare_different_level_limit() {
        String oneLevelLimitResult = DgraphSqlHelper.getVar(ExampleParentClass.class, 1);
        String noLevelLimitResult = DgraphSqlHelper.getVar(ExampleParentClass.class, null);
        Assert.assertNotEquals(oneLevelLimitResult, noLevelLimitResult);
    }
}
