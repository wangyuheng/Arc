package ai.care.arc.generator.convert;

import ai.care.arc.core.common.GraphqlFieldType2DgraphPredicateTypeConverter;
import ai.care.arc.core.dictionary.DgraphPredicateTypeEnum;
import ai.care.arc.dgraph.datasource.DgraphSchemaPredicate;
import ai.care.arc.dgraph.datasource.DgraphSchemaType;
import ai.care.arc.graphql.idl.GraphqlSchemaField;
import ai.care.arc.graphql.idl.GraphqlSchemaType;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * {@link GraphqlSchemaType} convert to {@link DgraphSchemaType}
 * {@link GraphqlSchemaField} convert to {@link DgraphSchemaPredicate}
 * 根据名称忽略GraphqlSchema中 {@link GraphqlSchemaType2DgraphSchemaType#IGNORE_FIELD} 定义的字段
 * 类型转换依赖 {@link GraphqlFieldType2DgraphPredicateTypeConverter}
 *
 * @author yuheng.wang
 */
public class GraphqlSchemaType2DgraphSchemaType implements Function<GraphqlSchemaType, DgraphSchemaType> {

    private static final List<String> IGNORE_FIELD = Arrays.asList("id", "uid");
    private GraphqlFieldType2DgraphPredicateTypeConverter converter = new GraphqlFieldType2DgraphPredicateTypeConverter();

    private BiFunction<GraphqlSchemaType, GraphqlSchemaField, DgraphSchemaPredicate> field2predicate = (graphqlSchemaType, graphqlSchemaField) -> DgraphSchemaPredicate.builder()
            .name(graphqlSchemaType.getName().toUpperCase() + "." + graphqlSchemaField.getName())
            .predicateType(converter.convert(graphqlSchemaField.getType()).orElse(DgraphPredicateTypeEnum.STRING))
            .isList(graphqlSchemaField.isListType())
            .build();

    @Override
    public DgraphSchemaType apply(GraphqlSchemaType graphqlSchemaType) {
        return DgraphSchemaType.builder()
                .name(graphqlSchemaType.getName())
                .predicateList(
                        graphqlSchemaType.getFieldList().stream()
                                .filter(graphqlSchemaField -> !IGNORE_FIELD.contains(graphqlSchemaField.getName()))
                                .map(graphqlField -> field2predicate.apply(graphqlSchemaType, graphqlField))
                                .collect(Collectors.toList())
                )
                .build();
    }
}
