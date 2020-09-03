package ai.care.arc.core.common;

import ai.care.arc.core.dictionary.DgraphPredicateTypeEnum;
import ai.care.arc.core.dictionary.GraphqlFieldTypeEnum;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link GraphqlFieldType2DgraphPredicateTypeConverter}.
 *
 * @author yuheng.wang
 */
public class GraphqlFieldType2DgraphPredicateTypeConverterTest {

    private GraphqlFieldType2DgraphPredicateTypeConverter converter = new GraphqlFieldType2DgraphPredicateTypeConverter();

    @Test
    public void all_enum_values_should_be_convert() {
        Arrays.stream(GraphqlFieldTypeEnum.values())
                .map(graphqlFieldTypeEnum -> converter.convert(graphqlFieldTypeEnum))
                .forEach(Assert::assertNotNull);

        Arrays.stream(DgraphPredicateTypeEnum.values())
                .map(dgraphPredicateTypeEnum -> converter.reverse(dgraphPredicateTypeEnum))
                .forEach(Assert::assertNotNull);
    }

    @Test
    public void mutual_convert() {
        assertNotNull(converter.convert(GraphqlFieldTypeEnum.STRING));
        assertEquals(DgraphPredicateTypeEnum.STRING, converter.convert(GraphqlFieldTypeEnum.STRING));
    }

}