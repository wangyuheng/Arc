package io.github.wangyuheng.arc.dgraph.repository.mapper;

import io.github.wangyuheng.arc.dgraph.annotation.DgraphType;
import io.github.wangyuheng.arc.dgraph.annotation.UidField;
import io.github.wangyuheng.arc.dgraph.repository.DgraphTypeInformation;
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

    @DgraphType("m")
    static class Mock {

        @UidField
        private String id;
        private String name;

        public Mock() {
        }

        public String getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String toString() {
            return "DgraphTypeInformationTest.Mock(id=" + this.getId() + ", name=" + this.getName() + ")";
        }
    }

}