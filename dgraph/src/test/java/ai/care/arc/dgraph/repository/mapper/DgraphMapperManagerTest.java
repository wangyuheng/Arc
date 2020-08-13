package ai.care.arc.dgraph.repository.mapper;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * DgraphMapperManager扫描进static.
 * 需要注意case顺序
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DgraphMapperManagerTest {

    @Test
    public void s1_should_be_empty_with_empty_location() throws Exception {
        DgraphMapperManager dgraphMapperManager = new DgraphMapperManager();
        dgraphMapperManager.afterPropertiesSet();
        Assert.assertNull(dgraphMapperManager.getSql("test.testQuery"));
    }

    @Test
    public void s2_should_scan_TestDgraphXml_and_can_read_query_method() throws Exception {
        DgraphMapperManager dgraphMapperManager = new DgraphMapperManager();
        ReflectionTestUtils.setField(dgraphMapperManager, "location", "dgraph");
        dgraphMapperManager.afterPropertiesSet();
        Assert.assertNotNull(dgraphMapperManager.getSql("test.testQuery"));
        //ignore match case
        Assert.assertNotNull(dgraphMapperManager.getSql("tEsT.tEstQuery"));
    }

}