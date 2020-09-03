package ai.care.arc.generator.convert;

import ai.care.arc.core.common.GraphqlFieldType2DgraphPredicateTypeConverter;
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
    private BiFunction<GraphqlSchemaType, GraphqlSchemaField, DgraphSchemaPredicate> field2predicate = new GraphqlSchemaField2DgraphSchemaPredicate();

    @Override
    public DgraphSchemaType apply(GraphqlSchemaType graphqlSchemaType) {
        return new DgraphSchemaType(
                graphqlSchemaType.getName(),
                graphqlSchemaType.getFieldList().stream()
                        .filter(graphqlSchemaField -> !IGNORE_FIELD.contains(graphqlSchemaField.getName()))
                        .map(graphqlField -> field2predicate.apply(graphqlSchemaType, graphqlField))
                        .collect(Collectors.toList())
        );
    }

    static class GraphqlSchemaField2DgraphSchemaPredicate implements BiFunction<GraphqlSchemaType, GraphqlSchemaField, DgraphSchemaPredicate> {

        private GraphqlFieldType2DgraphPredicateTypeConverter converter = new GraphqlFieldType2DgraphPredicateTypeConverter();

        @Override
        public DgraphSchemaPredicate apply(GraphqlSchemaType graphqlSchemaType, GraphqlSchemaField graphqlSchemaField) {
            return new DgraphSchemaPredicate(graphqlSchemaType.getName().toUpperCase() + "." + graphqlSchemaField.getName(),
                    converter.convert(graphqlSchemaField.getType()),
                    graphqlSchemaField.isListType()
            );
        }
    }
}
