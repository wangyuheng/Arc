package ai.care.arc.dgraph.datasource;

import ai.care.arc.core.dictionary.DgraphPredicateTypeEnum;
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
        DgraphSchemaPredicate predicate = new DgraphSchemaPredicate("n1", DgraphPredicateTypeEnum.STRING);
        assertEquals("<n1>: string .", predicate.buildRdf());
        predicate.setList(true);
        assertEquals("<n1>: [string] .", predicate.buildRdf());
    }

    @Test
    public void name_must_be_not_null_when_build_rdf() {
        DgraphSchemaPredicate predicate = new DgraphSchemaPredicate();
        predicate.setPredicateType(DgraphPredicateTypeEnum.STRING);
        thrown.expect(IllegalArgumentException.class);
        predicate.buildRdf();
    }

    @Test
    public void predicate_type_must_be_not_null_when_build_rdf() {
        DgraphSchemaPredicate predicate = new DgraphSchemaPredicate();
        predicate.setName("n1");
        thrown.expect(IllegalArgumentException.class);
        predicate.buildRdf();
    }
}