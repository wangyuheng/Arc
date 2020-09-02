package ai.care.arc.generator.convert;

import ai.care.arc.dgraph.datasource.DgraphSchemaPredicate;
import ai.care.arc.dgraph.datasource.DgraphSchemaType;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link DgraphSchemaType} 转换为DgraphSchema执行语句
 * <p>
 * 需要填充通用字段 {@link DgraphSchemaType#GENERAL_DGRAPH_PREDICATE_LIST}
 *
 * @author yuheng.wang
 */
public class DgraphSchemaTypes2DdlString implements Function<Stream<DgraphSchemaType>, Stream<String>> {

    @Override
    public Stream<String> apply(Stream<DgraphSchemaType> dgraphSchemaTypeStream) {
        List<DgraphSchemaType> dgraphSchemaTypeList = dgraphSchemaTypeStream.collect(Collectors.toList());

        Stream<String> generalFields = DgraphSchemaType.GENERAL_DGRAPH_PREDICATE_LIST.stream()
                .map(DgraphSchemaPredicate::buildRdf);
        Stream<String> fields = dgraphSchemaTypeList.stream()
                .flatMap(dgraphSchemaType -> dgraphSchemaType.getPredicateList().stream())
                .map(DgraphSchemaPredicate::buildRdf)
                .distinct();
        Stream<String> types = dgraphSchemaTypeList.stream()
                .flatMap(dgraphSchemaType -> dgraphSchemaType.buildDgraphSchemaLines().stream());

        return Stream.of(generalFields, fields, types)
                .flatMap(it -> it);
    }

}
