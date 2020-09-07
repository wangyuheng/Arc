package ai.care.arc.dgraph.datasource;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link DgraphSchemaType}.
 *
 * @author yuheng.wang
 */
public class DgraphSchemaTypeTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void parse_dgraph_schema_lines() {
        DgraphSchemaType type = new DgraphSchemaType();
        type.setName("t1");
        assertEquals("type t1 {}", String.join("", type.buildDgraphSchemaLines()));

        type.setPredicateList(
                Arrays.asList(
                        new DgraphSchemaPredicate("p1", null),
                        new DgraphSchemaPredicate("p2", null)
                ));
        assertEquals("type t1 {" +
                "p1" +
                "p2" +
                "}", String.join("", type.buildDgraphSchemaLines()));
    }

    @Test
    public void name_must_be_not_null_when_parse_dgraph_schema_lines() {
        DgraphSchemaType type = new DgraphSchemaType();
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("name must be not null!");
        type.buildDgraphSchemaLines();
    }

}