package io.github.wangyuheng.arc.generator.convert;

import io.github.wangyuheng.arc.core.dictionary.DgraphPredicateTypeEnum;
import io.github.wangyuheng.arc.core.util.DomainClassUtil;
import io.github.wangyuheng.arc.dgraph.datasource.DgraphSchemaPredicate;
import io.github.wangyuheng.arc.dgraph.datasource.DgraphSchemaType;

import java.util.ArrayList;
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
                .map(dgraphSchemaType -> {
                    List<DgraphSchemaPredicate> predicateList = new ArrayList<>(GENERAL_DGRAPH_PREDICATE_LIST);
                    if (null != dgraphSchemaType.getPredicateList()) {
                        predicateList.addAll(dgraphSchemaType.getPredicateList());
                    }
                    return new DgraphSchemaType(dgraphSchemaType.getName(), predicateList);
                })
                .flatMap(dgraphSchemaType -> dgraphSchemaType.buildDgraphSchemaLines().stream());

        return Stream.of(GENERAL_DGRAPH_PREDICATE_LIST.stream().map(DgraphSchemaPredicate::buildRdf), fields, types)
                .flatMap(it -> it);
    }

}