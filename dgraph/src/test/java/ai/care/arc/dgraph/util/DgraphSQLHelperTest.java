package ai.care.arc.dgraph.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author junhao.chen
 * @date 2020/11/27
 */
public class DgraphSQLHelperTest {
    private static class ExampleParentClass{
        private String uid;
        private ExampleBClass bClass;
        private ExampleAClass aClass;
        private List<ExampleAClass> aList;
        private List<ExampleBClass> bList;
        private List<String> stringList;
    }

    private static class ExampleAClass{
        private String uid;
        private ExampleBClass bClass;
        private ExampleAClass aClass;
    }

    private static class ExampleBClass{
        private String uid;
        private List<String> stringList;
    }

    private static class ExampleParentWithUnionClass{
        private String uid;
        @UnionClasses({ExampleAClass.class,ExampleBClass.class})
        private Object unionField;
        @UnionClasses({ExampleAClass.class,ExampleBClass.class})
        private List<Object> unionListField;
    }

    @Test
    public void  test_flat_class(){
        Set<String> result = DgraphSQLHelper.flatClass(ExampleParentClass.class,new ArrayList<>());
        Assert.assertEquals(7,result.size());
        Assert.assertTrue(result.contains("ExampleParentClass.uid"));
        Assert.assertTrue(result.contains("ExampleParentClass.stringList"));
        Assert.assertTrue(result.contains("ExampleParentClass.ExampleBClass.uid"));
        Assert.assertTrue(result.contains("ExampleParentClass.ExampleBClass.stringList"));
        Assert.assertTrue(result.contains("ExampleParentClass.ExampleAClass.uid"));
        Assert.assertTrue(result.contains("ExampleParentClass.ExampleAClass.ExampleBClass.uid"));
        Assert.assertTrue(result.contains("ExampleParentClass.ExampleAClass.ExampleBClass.stringList"));
    }

    @Test
    public void test_flat_union_class(){
        Set<String> result = DgraphSQLHelper.flatClass(ExampleParentWithUnionClass.class,new ArrayList<>());
        Assert.assertEquals(6,result.size());
        Assert.assertTrue(result.contains("ExampleParentWithUnionClass.uid"));
        Assert.assertTrue(result.contains("ExampleParentWithUnionClass.ExampleAClass.uid"));
        Assert.assertTrue(result.contains("ExampleParentWithUnionClass.ExampleBClass.uid"));
        Assert.assertTrue(result.contains("ExampleParentWithUnionClass.ExampleBClass.stringList"));
        Assert.assertTrue(result.contains("ExampleParentWithUnionClass.ExampleAClass.ExampleBClass.uid"));
        Assert.assertTrue(result.contains("ExampleParentWithUnionClass.ExampleAClass.ExampleBClass.stringList"));
    }

    @Test
    public void test_generate_query(){
        String oneLevelResult = DgraphSQLHelper.generateQueryByMaxLevel(1);
        Assert.assertEquals("{ \n uid \n expand(_all_)\n}",oneLevelResult);
        String twoLevelResult = DgraphSQLHelper.generateQueryByMaxLevel(2);
        Assert.assertEquals("{ \n uid \n expand(_all_){ \n uid \n expand(_all_)\n}\n}",twoLevelResult);
        String threeLevelResult = DgraphSQLHelper.generateQueryByMaxLevel(3);
        Assert.assertEquals("{ \n uid \n expand(_all_){ \n uid \n expand(_all_){ \n uid \n expand(_all_)\n}\n}\n}",threeLevelResult);
        String fourLevelResult = DgraphSQLHelper.generateQueryByMaxLevel(4);
        Assert.assertEquals("{ \n uid \n expand(_all_){ \n uid \n expand(_all_){ \n uid \n expand(_all_){ \n uid \n expand(_all_)\n}\n}\n}\n}",fourLevelResult);
        String fiveLevelResult = DgraphSQLHelper.generateQueryByMaxLevel(5);
        Assert.assertEquals("{ \n uid \n expand(_all_){ \n uid \n expand(_all_){ \n uid \n expand(_all_){ \n uid \n expand(_all_){ \n uid \n expand(_all_)\n}\n}\n}\n}\n}",fiveLevelResult);
    }

    @Test
    public void test_get_var_compare_different_level_limit(){
        String twoLevelLimitResult = DgraphSQLHelper.getVar(ExampleParentClass.class,2);
        String noLevelLimitResult = DgraphSQLHelper.getVar(ExampleParentClass.class,null);
        Assert.assertNotEquals(twoLevelLimitResult,noLevelLimitResult);
    }
}
