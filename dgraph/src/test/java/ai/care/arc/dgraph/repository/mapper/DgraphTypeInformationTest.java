package ai.care.arc.dgraph.repository.mapper;

import ai.care.arc.dgraph.annotation.DgraphType;
import ai.care.arc.dgraph.annotation.UidField;
import ai.care.arc.dgraph.repository.DgraphTypeInformation;
import lombok.Data;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DgraphTypeInformationTest {

    private DgraphTypeInformation information;

    @Before
    public void setUp() {
        information = new DgraphTypeInformation(Mock.class);
    }

    @Test
    public void should_get_domain_class() {
        assertEquals(information.getDomainClass(), Mock.class);
    }

    @Test
    public void should_is_dgraph_type() {
        assertTrue(information.isDgraphType());
    }

    @Test
    public void should_get_type() {
        assertEquals("m", information.getTypeValue());
    }

    @Test
    public void should_check_uid_field() {
        assertTrue(information.isUidField("id"));
        assertTrue(information.isUidField("uid"));
        assertFalse(information.isUidField("xid"));
    }

    @Test
    public void should_get_uid_name() {
        assertEquals("m.id", information.getUidFieldName());
    }

    @Test
    public void should_get_uid_name_with_db() {
        assertEquals("_:m.id", information.getUidFieldDbWrapper());
    }

    @Data
    @DgraphType("m")
    static class Mock {

        @UidField
        private String id;
        private String name;

    }

}