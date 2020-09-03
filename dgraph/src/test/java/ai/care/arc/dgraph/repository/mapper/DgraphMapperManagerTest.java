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
public class DgraphMapperManagerTest {

    @Test
    public void should_scan_test_dgraph_xml_and_can_read_query_method() throws Exception {
        DgraphMapperManager dgraphMapperManager = new DgraphMapperManager();
        ReflectionTestUtils.setField(dgraphMapperManager, "location", "dgraph");
        dgraphMapperManager.afterPropertiesSet();
        Assert.assertNotNull(dgraphMapperManager.getSql("test.testQuery"));
        //ignore match case
        Assert.assertNotNull(dgraphMapperManager.getSql("tEsT.tEstQuery"));
    }

}