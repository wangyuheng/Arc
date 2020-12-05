package com.github.yituhealthcare.arc.dgraph.datasource;

import com.github.yituhealthcare.arc.core.dictionary.DgraphPredicateTypeEnum;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link DgraphSchemaPredicate}.
 *
 * @author yuheng.wang
 */
public class DgraphSchemaPredicateTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void parse_rdf() {
        assertEquals("<n1>: string .", new DgraphSchemaPredicate("n1", DgraphPredicateTypeEnum.STRING).buildRdf());
        assertEquals("<n2>: [string] .", new DgraphSchemaPredicate("n2", DgraphPredicateTypeEnum.STRING, true).buildRdf());
    }

    @Test
    public void name_must_be_not_null_when_build_rdf() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("name must be not null!");
        DgraphSchemaPredicate predicate = new DgraphSchemaPredicate(null, DgraphPredicateTypeEnum.STRING);
    }

    @Test
    public void predicate_type_must_be_not_null_when_build_rdf() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("predicateType must be not null!");
        DgraphSchemaPredicate predicate = new DgraphSchemaPredicate("n1", null);
    }
}