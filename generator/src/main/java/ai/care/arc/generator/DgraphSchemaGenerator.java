package ai.care.arc.generator;

import ai.care.arc.dgraph.datasource.DgraphSchemaType;
import ai.care.arc.generator.convert.DgraphSchemaTypes2DdlString;
import ai.care.arc.generator.convert.GraphqlSchemaType2DgraphSchemaType;
import ai.care.arc.generator.convert.TypeDefinitionRegistry2GraphqlSchemaTypes;
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

    private final Function<InputStream, TypeDefinitionRegistry> inputStream2TypeDefinitionRegistry;
    private final Function<Stream<DgraphSchemaType>, Stream<String>> dgraphSchemaTypes2DdlString;
    private final Function<TypeDefinitionRegistry, Stream<DgraphSchemaType>> typeDefinitionRegistry2DgraphSchemaTypes;

    public DgraphSchemaGenerator(Function<InputStream, TypeDefinitionRegistry> inputStream2TypeDefinitionRegistry, Function<Stream<DgraphSchemaType>, Stream<String>> dgraphSchemaTypes2DdlString, Function<TypeDefinitionRegistry, Stream<DgraphSchemaType>> typeDefinitionRegistry2DgraphSchemaTypes) {
        this.inputStream2TypeDefinitionRegistry = inputStream2TypeDefinitionRegistry;
        this.dgraphSchemaTypes2DdlString = dgraphSchemaTypes2DdlString;
        this.typeDefinitionRegistry2DgraphSchemaTypes = typeDefinitionRegistry2DgraphSchemaTypes;
    }

    public DgraphSchemaGenerator() {
        this(inputStream -> new SchemaParser().parse(inputStream),
                new DgraphSchemaTypes2DdlString(),
                typeDefinitionRegistry -> {
                    GraphqlSchemaType2DgraphSchemaType graphqlSchemaType2DgraphSchemaType = new GraphqlSchemaType2DgraphSchemaType();
                    return new TypeDefinitionRegistry2GraphqlSchemaTypes()
                            .andThen(graphqlSchemaTypeStream -> graphqlSchemaTypeStream.map(graphqlSchemaType2DgraphSchemaType))
                            .apply(typeDefinitionRegistry);
                }
        );
    }

    public List<String> generate(InputStream graphSchemaInputStream) {
        return typeDefinitionRegistry2DgraphSchemaTypes
                .compose(inputStream2TypeDefinitionRegistry)
                .andThen(dgraphSchemaTypes2DdlString)
                .apply(graphSchemaInputStream)
                .collect(Collectors.toList());
    }

}