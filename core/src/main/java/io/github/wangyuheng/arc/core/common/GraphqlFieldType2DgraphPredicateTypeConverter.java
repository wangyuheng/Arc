package io.github.wangyuheng.arc.core.common;

import io.github.wangyuheng.arc.core.dictionary.DgraphPredicateTypeEnum;
import io.github.wangyuheng.arc.core.dictionary.GraphqlFieldTypeEnum;

/**
 * GraphqlField与DgraphPredicate类型互相转换
 *
 * @author yuheng.wang
 */
public class GraphqlFieldType2DgraphPredicateTypeConverter implements Converter<GraphqlFieldTypeEnum, DgraphPredicateTypeEnum> {

    @Override
    public DgraphPredicateTypeEnum convert(GraphqlFieldTypeEnum graphqlFieldTypeEnum) {
        switch (graphqlFieldTypeEnum) {
            case BOOLEAN:
                return DgraphPredicateTypeEnum.BOOL;
            case ID:
            case TYPE:
                return DgraphPredicateTypeEnum.UID;
            case DATE_TIME:
                return DgraphPredicateTypeEnum.DATE_TIME;
            case INT:
            case POSITIVE_INT:
            case NEGATIVE_INT:
            case NON_POSITIVE_INT:
            case NON_NEGATIVE_INT:
                return DgraphPredicateTypeEnum.INT;
            case FLOAT:
            case POSITIVE_FLOAT:
            case NEGATIVE_FLOAT:
            case NON_POSITIVE_FLOAT:
            case NON_NEGATIVE_FLOAT:
                return DgraphPredicateTypeEnum.FLOAT;
            case STRING:
            case DATE:
            case TIME:
            case JSON:
            case OBJECT:
            case URL:
            case LOCALE:
            case ENUM:
                return DgraphPredicateTypeEnum.STRING;
            default:
                throw new IllegalArgumentException("convert value not fount!");
        }
    }

    @Override
    public GraphqlFieldTypeEnum reverse(DgraphPredicateTypeEnum dgraphPredicateTypeEnum) {
        switch (dgraphPredicateTypeEnum) {
            case UID:
                return GraphqlFieldTypeEnum.ID;
            case DATE_TIME:
                return GraphqlFieldTypeEnum.DATE_TIME;
            case FLOAT:
                return GraphqlFieldTypeEnum.FLOAT;
            case INT:
                return GraphqlFieldTypeEnum.INT;
            case BOOL:
                return GraphqlFieldTypeEnum.BOOLEAN;
            case STRING:
            case GEO:
            case PASSWORD:
                return GraphqlFieldTypeEnum.STRING;
            default:
                throw new IllegalArgumentException("reverse value not fount!");
        }
    }

}