package ai.care.arc.dgraph.datasource;

import ai.care.arc.core.dictionary.DgraphPredicateTypeEnum;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link DgraphSchemaPredicate}.
 *
 * @author yuheng.wang
 */
public class DgraphSchemaPredicateTest {

    @Test
    public void parse_rdf() {
        DgraphSchemaPredicate predicate = new DgraphSchemaPredicate();
        assertNull(predicate.buildRdf());
        predicate.setPredicateType(DgraphPredicateTypeEnum.STRING);
        assertNull(predicate.buildRdf());
        predicate.setName("n1");
        assertEquals("<n1>: string .", predicate.buildRdf());
        predicate.setList(true);
        assertEquals("<n1>: [string] .", predicate.buildRdf());
    }
}