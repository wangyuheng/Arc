package ai.care.arc.core.common;

import ai.care.arc.core.dictionary.GraphqlFieldTypeEnum;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Tests for {@link GraphqlFieldType2JavaTypeConverter}.
 *
 * @author yuheng.wang
 */
public class GraphqlFieldType2JavaTypeConverterTest {

    private GraphqlFieldType2JavaTypeConverter converter = new GraphqlFieldType2JavaTypeConverter();

    @Test
    public void all_enum_values_should_be_convert() {
        Arrays.stream(GraphqlFieldTypeEnum.values())
                .map(graphqlFieldTypeEnum -> converter.convert(graphqlFieldTypeEnum))
                .forEach(Assert::assertNotNull);
    }

}