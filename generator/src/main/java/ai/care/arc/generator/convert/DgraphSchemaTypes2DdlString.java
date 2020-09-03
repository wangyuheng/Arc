package ai.care.arc.generator.convert;

import ai.care.arc.core.dictionary.DgraphPredicateTypeEnum;
import ai.care.arc.core.util.DomainClassUtil;
import ai.care.arc.dgraph.datasource.DgraphSchemaPredicate;
import ai.care.arc.dgraph.datasource.DgraphSchemaType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link DgraphSchemaType} 转换为DgraphSchema执行语句
 * 1. dgraph 结构语句必须type不能引用uid
 * 2. dgraph 结构语句必须先定义predicate，再通过type引用。 e.g.:
 *
 * <domainClass>: string .
 * <USER.name>: string .
 * type User {
 * USER.name
 * domainClass
 * }
 *
 * @author yuheng.wang
 */
public class DgraphSchemaTypes2DdlString implements Function<Stream<DgraphSchemaType>, Stream<String>> {

    public static final List<DgraphSchemaPredicate> GENERAL_DGRAPH_PREDICATE_LIST = Collections.unmodifiableList(
            Arrays.asList(
                    new DgraphSchemaPredicate(DomainClassUtil.DOMAIN_CLASS_KEY, DgraphPredicateTypeEnum.STRING),
                    new DgraphSchemaPredicate("dgraph.graphql.schema", DgraphPredicateTypeEnum.STRING)
            )
    );

    @Override
    public Stream<String> apply(Stream<DgraphSchemaType> dgraphSchemaTypeStream) {
        List<DgraphSchemaType> dgraphSchemaTypeList = dgraphSchemaTypeStream.collect(Collectors.toList());

        Stream<String> fields = dgraphSchemaTypeList.stream()
                .flatMap(dgraphSchemaType -> dgraphSchemaType.getPredicateList().stream())
                .map(DgraphSchemaPredicate::buildRdf)
                .sorted()
                .distinct();

        Stream<String> types = dgraphSchemaTypeList.stream()
                .flatMap(dgraphSchemaType -> {
                    if (null == dgraphSchemaType.getPredicateList()) {
                        dgraphSchemaType.setPredicateList(GENERAL_DGRAPH_PREDICATE_LIST);
                    } else {
                        dgraphSchemaType.getPredicateList().addAll(GENERAL_DGRAPH_PREDICATE_LIST);
                    }
                    return dgraphSchemaType.buildDgraphSchemaLines().stream();
                });

        return Stream.of(GENERAL_DGRAPH_PREDICATE_LIST.stream().map(DgraphSchemaPredicate::buildRdf), fields, types)
                .flatMap(it -> it);
    }

}