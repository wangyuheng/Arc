package io.github.wangyuheng.arc.generator.convert;

import io.github.wangyuheng.arc.core.dictionary.DgraphPredicateTypeEnum;
import io.github.wangyuheng.arc.dgraph.datasource.DgraphSchemaPredicate;
import io.github.wangyuheng.arc.dgraph.datasource.DgraphSchemaType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class DgraphSchemaTypes2DdlStringTest {

    /**
     * 测试数据点
     *
     * 1. 自动声明通用字段，且只声明一次
     * 2. 每个type添加通用字段引用，可以只有通用字段
     * 3. list字段声明为[]
     * 4. 所有type中使用到的字段都会在type之前声明
     */
    @Test
    public void generate_ddl() {
        List<DgraphSchemaPredicate> d1Predicates = new ArrayList<>();
        d1Predicates.add(new DgraphSchemaPredicate("dp1", DgraphPredicateTypeEnum.STRING));
        d1Predicates.add(new DgraphSchemaPredicate("dp2", DgraphPredicateTypeEnum.INT, true));

        Stream<DgraphSchemaType> dgraphSchemaTypes = Stream.of(
                new DgraphSchemaType("d1", d1Predicates),
                new DgraphSchemaType("d2", Collections.emptyList())
        );

        String expected = "<domainClass>: string ." +
                "<dgraph.graphql.schema>: string ." +
                "<dp1>: string ." +
                "<dp2>: [int] ." +
                "type d1 {" +
                "dgraph.graphql.schema" +
                "domainClass" +
                "dp1" +
                "dp2" +
                "}" +
                "type d2 {" +
                "dgraph.graphql.schema" +
                "domainClass" +
                "}";
        assertEquals(expected, new DgraphSchemaTypes2DdlString().apply(dgraphSchemaTypes).collect(Collectors.joining("")));
    }

}