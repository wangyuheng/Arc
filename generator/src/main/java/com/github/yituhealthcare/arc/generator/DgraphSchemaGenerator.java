package com.github.yituhealthcare.arc.generator;

import com.github.yituhealthcare.arc.dgraph.datasource.DgraphSchemaType;
import com.github.yituhealthcare.arc.generator.convert.DgraphSchemaTypes2DdlString;
import com.github.yituhealthcare.arc.generator.convert.TypeDefinitionRegistry2DgraphSchemaTypes;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

import java.io.InputStream;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 根据GraphqlSchema生成Dgraph数据库结构Schema
 * <p>
 * ┌─────────────────────────────┐          ┌──────────────────────┐          ┌────────────────────────┐          ┌───────────────────────────────┐
 * │InputStream for GraphqlSchema│          │TypeDefinitionRegistry│          │Stream<DgraphSchemaType>│          │Stream<String> for DgraphSchema│
 * └──────────────┬──────────────┘          └──────────┬───────────┘          └───────────┬────────────┘          └───────────────┬───────────────┘
 *                │                                    │                                  │                                       │
 *                │───────────────────────────────────>│                                  │                                       │
 *                │                                    │                                  │                                       │
 *                │                                    │                                  │                                       │
 *                │                                    │ ─────────────────────────────────>                                       │
 *                │                                    │                                  │                                       │
 *                │                                    │                                  │                                       │
 *                │                                    │                                  │ ─────────────────────────────────────>│
 * ┌──────────────┴──────────────┐          ┌──────────┴───────────┐          ┌───────────┴────────────┐          ┌───────────────┴───────────────┐
 * │InputStream for GraphqlSchema│          │TypeDefinitionRegistry│          │Stream<DgraphSchemaType>│          │Stream<String> for DgraphSchema│
 * └─────────────────────────────┘          └──────────────────────┘          └────────────────────────┘          └───────────────────────────────┘
 *
 * @author yuheng.wang
 */
public class DgraphSchemaGenerator {

    private final Function<InputStream, TypeDefinitionRegistry> toTypeDefinitionRegistry;
    private final Function<TypeDefinitionRegistry, Stream<DgraphSchemaType>> toDgraphTypes;
    private final Function<Stream<DgraphSchemaType>, Stream<String>> toDdlStrings;

    public DgraphSchemaGenerator(Function<InputStream, TypeDefinitionRegistry> toTypeDefinitionRegistry,
                                 Function<TypeDefinitionRegistry, Stream<DgraphSchemaType>> toDgraphTypes,
                                 Function<Stream<DgraphSchemaType>, Stream<String>> toDdlStrings) {
        this.toTypeDefinitionRegistry = toTypeDefinitionRegistry;
        this.toDgraphTypes = toDgraphTypes;
        this.toDdlStrings = toDdlStrings;
    }

    public DgraphSchemaGenerator() {
        this(
                inputStream -> new SchemaParser().parse(inputStream),
                new TypeDefinitionRegistry2DgraphSchemaTypes(),
                new DgraphSchemaTypes2DdlString()
        );
    }

    public List<String> generate(InputStream graphSchemaInputStream) {
        return toDgraphTypes
                .compose(toTypeDefinitionRegistry)
                .andThen(toDdlStrings)
                .apply(graphSchemaInputStream)
                .collect(Collectors.toList());
    }

}