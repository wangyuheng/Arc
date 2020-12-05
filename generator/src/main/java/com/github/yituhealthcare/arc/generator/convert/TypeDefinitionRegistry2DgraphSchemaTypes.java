package com.github.yituhealthcare.arc.generator.convert;

import com.github.yituhealthcare.arc.dgraph.datasource.DgraphSchemaType;
import com.github.yituhealthcare.arc.graphql.idl.GraphqlSchemaType;
import graphql.schema.idl.TypeDefinitionRegistry;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * {@link TypeDefinitionRegistry} -> {@link GraphqlSchemaType} -> {@link DgraphSchemaType}
 *
 * @author yuheng.wang
 */
public class TypeDefinitionRegistry2DgraphSchemaTypes implements Function<TypeDefinitionRegistry, Stream<DgraphSchemaType>> {

    private final TypeDefinitionRegistry2GraphqlSchemaTypes toGraphqlTypes = new TypeDefinitionRegistry2GraphqlSchemaTypes();
    private final GraphqlSchemaType2DgraphSchemaType toDgraphTypes = new GraphqlSchemaType2DgraphSchemaType();

    @Override
    public Stream<DgraphSchemaType> apply(TypeDefinitionRegistry typeDefinitionRegistry) {
        return toGraphqlTypes
                .andThen(graphqlSchemaTypeStream -> graphqlSchemaTypeStream.map(toDgraphTypes))
                .apply(typeDefinitionRegistry);
    }
}
