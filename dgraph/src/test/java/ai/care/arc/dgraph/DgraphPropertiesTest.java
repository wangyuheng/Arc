package ai.care.arc.dgraph;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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

}