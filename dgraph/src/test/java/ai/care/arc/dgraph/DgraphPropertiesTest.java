package ai.care.arc.dgraph;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link DgraphProperties}.
 *
 * @author yuheng.wang
 */
public class DgraphPropertiesTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void default_address_is_null() {
        DgraphProperties properties = new DgraphProperties();
        assertTrue(properties.getAddresses().isEmpty());
    }

    @Test
    public void should_get_address_when_set() {
        DgraphProperties properties = new DgraphProperties();
        properties.setUrls(Collections.singletonList("localhost:9080"));
        assertEquals(9080, properties.getAddresses().get(0).getPort());
        assertEquals("localhost", properties.getAddresses().get(0).getName());
    }

    @Test
    public void should_throw_illegal_when_set_illegal_url() {
        DgraphProperties properties = new DgraphProperties();
        thrown.expect(IllegalArgumentException.class);
        properties.setUrls(Collections.singletonList("a"));
    }

    @Test
    public void should_throw_illegal_when_set_null() {
        DgraphProperties properties = new DgraphProperties();
        thrown.expect(IllegalArgumentException.class);
        properties.setUrls(null);
    }

    @Test
    public void should_init_0_dgraph_stubs_when_not_set_urls() {
        assertEquals(0, new DgraphProperties().getDgraphStubs().length);
    }

    @Test
    public void should_init_x_dgraph_stubs_when_set_x_urls() {
        DgraphProperties dgraphProperties = new DgraphProperties();
        dgraphProperties.setUrls(Arrays.asList("localhost:8080", "localhost:8081", "localhost:8082"));
        assertEquals(3, dgraphProperties.getDgraphStubs().length);
    }

}