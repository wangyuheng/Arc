package ai.care.arc.dgraph.datasource;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link DgraphSchemaType}.
 *
 * @author yuheng.wang
 */
public class DgraphSchemaTypeTest {

    @Test
    public void parse_dgraph_schema_lines() {
        DgraphSchemaType type = new DgraphSchemaType();
        assertEquals(Collections.emptyList(), type.buildDgraphSchemaLines());
        type.setName("t1");
        assertEquals("type t1 {" +
                "domainClass" +
                "dgraph.graphql.schema" +
                "}", String.join("", type.buildDgraphSchemaLines()));

        type.setPredicateList(Arrays.asList(
                DgraphSchemaPredicate.builder()
                        .name("p1")
                        .build(),
                DgraphSchemaPredicate.builder()
                        .name("p2")
                        .build()
        ));
        assertEquals("type t1 {" +
                "domainClass" +
                "dgraph.graphql.schema" +
                "p1" +
                "p2" +
                "}", String.join("", type.buildDgraphSchemaLines()));
    }
}