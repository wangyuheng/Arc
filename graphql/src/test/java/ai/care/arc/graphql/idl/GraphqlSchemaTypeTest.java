package ai.care.arc.graphql.idl;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link GraphqlSchemaType}.
 *
 * @author yuheng.wang
 */
public class GraphqlSchemaTypeTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void name_must_be_not_null_when_constructor() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("name must be not null!");
        new GraphqlSchemaType(null, null);
    }
}