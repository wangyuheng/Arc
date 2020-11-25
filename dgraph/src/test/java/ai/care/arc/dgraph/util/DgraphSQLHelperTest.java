package ai.care.arc.dgraph.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author junhao.chen
 * @date 2020/9/30
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
}
