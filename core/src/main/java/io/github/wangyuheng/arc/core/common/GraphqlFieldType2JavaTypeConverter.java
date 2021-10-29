package io.github.wangyuheng.arc.core.common;

import io.github.wangyuheng.arc.core.dictionary.GraphqlFieldTypeEnum;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Locale;
import java.util.Map;

/**
 * GraphqlField与Java类型互相转换
 *
 * @author yuheng.wang
 */
public class GraphqlFieldType2JavaTypeConverter implements Converter<GraphqlFieldTypeEnum, Class<?>> {

    @Override
    public Class<?> convert(GraphqlFieldTypeEnum graphqlFieldTypeEnum) {
        switch (graphqlFieldTypeEnum) {
            case ID:
            case STRING:
            case URL:
                return String.class;
            case JSON:
                return Map.class;
            case INT:
            case POSITIVE_INT:
            case NEGATIVE_INT:
            case NON_POSITIVE_INT:
            case NON_NEGATIVE_INT:
                return Integer.class;
            case FLOAT:
            case POSITIVE_FLOAT:
            case NEGATIVE_FLOAT:
            case NON_POSITIVE_FLOAT:
            case NON_NEGATIVE_FLOAT:
                return Double.class;
            case BOOLEAN:
                return Boolean.class;
            case DATE_TIME:
                return OffsetDateTime.class;
            case DATE:
                return LocalDate.class;
            case TIME:
                return OffsetTime.class;
            case LOCALE:
                return Locale.class;
            case ENUM:
            case OBJECT:
            case TYPE:
            default:
                return Object.class;
        }
    }

    /**
     * 注意: 会丢失类型. 比如 {@link GraphqlFieldTypeEnum#ID} -convert-> {@link String} -reverse-> {@link GraphqlFieldTypeEnum#STRING}
     */
    @Override
    public GraphqlFieldTypeEnum reverse(Class<?> o) {
        if (String.class.equals(o)) {
            return GraphqlFieldTypeEnum.STRING;
        }
        if (Integer.class.equals(o)) {
            return GraphqlFieldTypeEnum.INT;
        }
        if (Float.class.equals(o)) {
            return GraphqlFieldTypeEnum.FLOAT;
        }
        if (Boolean.class.equals(o)) {
            return GraphqlFieldTypeEnum.BOOLEAN;
        }
        if (OffsetDateTime.class.equals(o)) {
            return GraphqlFieldTypeEnum.DATE_TIME;
        }
        if (LocalDate.class.equals(o)) {
            return GraphqlFieldTypeEnum.DATE;
        }
        if (OffsetTime.class.equals(o)) {
            return GraphqlFieldTypeEnum.TIME;
        }
        if (Locale.class.equals(o)) {
            return GraphqlFieldTypeEnum.LOCALE;
        }
        if (Object.class.equals(o)) {
            return GraphqlFieldTypeEnum.TYPE;
        }
        return GraphqlFieldTypeEnum.OBJECT;
    }


}