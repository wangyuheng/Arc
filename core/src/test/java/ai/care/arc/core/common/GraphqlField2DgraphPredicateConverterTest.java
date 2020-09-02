package ai.care.arc.core.common;

import ai.care.arc.core.dictionary.DgraphPredicateTypeEnum;
import ai.care.arc.core.dictionary.GraphqlFieldTypeEnum;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link GraphqlField2DgraphPredicateConverter}.
 *
 * @author yuheng.wang
 */
public class GraphqlField2DgraphPredicateConverterTest {

    private GraphqlField2DgraphPredicateConverter converter = new GraphqlField2DgraphPredicateConverter();

    @Test
    public void all_enum_values_should_be_convert() {
        Arrays.stream(GraphqlFieldTypeEnum.values())
                .map(graphqlFieldTypeEnum -> converter.convert(graphqlFieldTypeEnum))
                .forEach(it -> assertTrue(it.isPresent()));

        Arrays.stream(DgraphPredicateTypeEnum.values())
                .map(dgraphPredicateTypeEnum -> converter.reverse(dgraphPredicateTypeEnum))
                .forEach(it -> assertTrue(it.isPresent()));
    }

    @Test
    public void mutual_convert() {
        assertTrue(converter.convert(GraphqlFieldTypeEnum.STRING).isPresent());
        assertEquals(DgraphPredicateTypeEnum.STRING, converter.convert(GraphqlFieldTypeEnum.STRING).get());
    }

}