package ai.care.arc.core.common;

import ai.care.arc.core.dictionary.DgraphPredicateTypeEnum;
import ai.care.arc.core.dictionary.GraphqlFieldTypeEnum;

import java.util.Optional;

/**
 * GraphqlField与DgraphPredicate类型互相转换
 *
 * @author yuheng.wang
 */
public class GraphqlField2DgraphPredicateConverter implements Converter<GraphqlFieldTypeEnum, DgraphPredicateTypeEnum> {

    @Override
    public Optional<DgraphPredicateTypeEnum> convert(GraphqlFieldTypeEnum graphqlFieldTypeEnum) {
        switch (graphqlFieldTypeEnum) {
            case BOOLEAN:
                return Optional.of(DgraphPredicateTypeEnum.BOOL);
            case ID:
            case TYPE:
                return Optional.of(DgraphPredicateTypeEnum.UID);
            case DATE_TIME:
                return Optional.of(DgraphPredicateTypeEnum.DATE_TIME);
            case INT:
            case POSITIVE_INT:
            case NEGATIVE_INT:
            case NON_POSITIVE_INT:
            case NON_NEGATIVE_INT:
                return Optional.of(DgraphPredicateTypeEnum.INT);
            case FLOAT:
            case POSITIVE_FLOAT:
            case NEGATIVE_FLOAT:
            case NON_POSITIVE_FLOAT:
            case NON_NEGATIVE_FLOAT:
                return Optional.of(DgraphPredicateTypeEnum.FLOAT);
            case STRING:
            case DATE:
            case TIME:
            case JSON:
            case OBJECT:
            case URL:
            case LOCALE:
            case ENUM:
                return Optional.of(DgraphPredicateTypeEnum.STRING);
            default:
                return Optional.empty();
        }
    }

    @Override
    public Optional<GraphqlFieldTypeEnum> reverse(DgraphPredicateTypeEnum dgraphPredicateTypeEnum) {
        switch (dgraphPredicateTypeEnum) {
            case UID:
                return Optional.of(GraphqlFieldTypeEnum.ID);
            case DATE_TIME:
                return Optional.of(GraphqlFieldTypeEnum.DATE_TIME);
            case FLOAT:
                return Optional.of(GraphqlFieldTypeEnum.FLOAT);
            case INT:
                return Optional.of(GraphqlFieldTypeEnum.INT);
            case BOOL:
                return Optional.of(GraphqlFieldTypeEnum.BOOLEAN);
            case STRING:
            case GEO:
            case PASSWORD:
                return Optional.of(GraphqlFieldTypeEnum.STRING);
            default:
                return Optional.empty();
        }
    }

}