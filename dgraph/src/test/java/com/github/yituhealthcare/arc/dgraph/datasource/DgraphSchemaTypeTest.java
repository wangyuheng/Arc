package com.github.yituhealthcare.arc.dgraph.datasource;

import com.github.yituhealthcare.arc.core.dictionary.DgraphPredicateTypeEnum;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        assertEquals("type t1 {}", String.join("", new DgraphSchemaType("t1", Collections.emptyList()).buildDgraphSchemaLines()));

        List<DgraphSchemaPredicate> dgraphSchemaPredicates = Arrays.asList(
                new DgraphSchemaPredicate("p1", DgraphPredicateTypeEnum.STRING),
                new DgraphSchemaPredicate("p2", DgraphPredicateTypeEnum.INT)
        );
        assertEquals("type t2 {" +
                        "p1" +
                        "p2" +
                        "}",
                String.join("", new DgraphSchemaType("t2", dgraphSchemaPredicates).buildDgraphSchemaLines()));
    }

    @Test
    public void name_must_be_not_null_when_constructor() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("name must be not null!");
        DgraphSchemaType type = new DgraphSchemaType(null, null);
    }

}