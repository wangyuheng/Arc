package com.github.yituhealthcare.arc.graphql.idl;

import com.github.yituhealthcare.arc.core.dictionary.GraphqlFieldTypeEnum;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link GraphqlSchemaField}.
 *
 * @author yuheng.wang
 */
public class GraphqlSchemaFieldTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void name_must_be_not_null_when_constructor() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("name must be not null!");
        new GraphqlSchemaField(null, GraphqlFieldTypeEnum.ID, false);
    }

    @Test
    public void type_must_be_not_null_when_constructor() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("type must be not null!");
        new GraphqlSchemaField("n1", null, false);
    }
}