package ai.care.arc.core.common;

import ai.care.arc.core.dictionary.GraphqlFieldTypeEnum;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Locale;

/**
 * GraphqlField与Java类型互相转换
 *
 * @author yuheng.wang
 */
public class GraphqlFieldType2JavaTypeConverter implements Converter<GraphqlFieldTypeEnum, Type> {

    @Override
    public Type convert(GraphqlFieldTypeEnum graphqlFieldTypeEnum) {
        switch (graphqlFieldTypeEnum) {
            case ID:
            case STRING:
            case JSON:
            case URL:
                return String.class;
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
    public GraphqlFieldTypeEnum reverse(Type o) {
        if (String.class.getSimpleName().equals(o.getTypeName())) {
            return GraphqlFieldTypeEnum.STRING;
        }
        if (Integer.class.getSimpleName().equals(o.getTypeName())) {
            return GraphqlFieldTypeEnum.INT;
        }
        if (Float.class.getSimpleName().equals(o.getTypeName())) {
            return GraphqlFieldTypeEnum.FLOAT;
        }
        if (Boolean.class.getSimpleName().equals(o.getTypeName())) {
            return GraphqlFieldTypeEnum.BOOLEAN;
        }
        if (OffsetDateTime.class.getSimpleName().equals(o.getTypeName())) {
            return GraphqlFieldTypeEnum.DATE_TIME;
        }
        if (LocalDate.class.getSimpleName().equals(o.getTypeName())) {
            return GraphqlFieldTypeEnum.DATE;
        }
        if (OffsetTime.class.getSimpleName().equals(o.getTypeName())) {
            return GraphqlFieldTypeEnum.TIME;
        }
        if (Locale.class.getSimpleName().equals(o.getTypeName())) {
            return GraphqlFieldTypeEnum.LOCALE;
        }
        if (Object.class.getSimpleName().equals(o.getTypeName())) {
            return GraphqlFieldTypeEnum.TYPE;
        }
        return GraphqlFieldTypeEnum.OBJECT;
    }


}