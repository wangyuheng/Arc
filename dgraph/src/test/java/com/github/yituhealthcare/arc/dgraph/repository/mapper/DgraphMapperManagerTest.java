package com.github.yituhealthcare.arc.dgraph.repository.mapper;

import org.junit.Assert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * DgraphMapperManager扫描进static.
 * 需要注意case顺序
 */
public class DgraphMapperManagerTest {

    @ParameterizedTest
    @ValueSource(strings = { "test.testQuery", "tEsT.tEstQuery" })
    public void should_scan_test_dgraph_xml_and_can_read_query_method(String paths) throws Exception {
        DgraphMapperManager dgraphMapperManager = new DgraphMapperManager();
        ReflectionTestUtils.setField(dgraphMapperManager, "location", "dgraph");
        dgraphMapperManager.afterPropertiesSet();
        Assert.assertNotNull(dgraphMapperManager.getSql(paths));
    }

}