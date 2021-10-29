package io.github.wangyuheng.arc.graphql.support;

import io.github.wangyuheng.arc.core.util.DomainClassUtil;
import io.github.wangyuheng.arc.graphql.interceptor.DataFetcherInterceptorRegistry;
import graphql.TypeResolutionEnvironment;
import graphql.language.UnionTypeDefinition;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.schema.TypeResolver;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Tests for {@link RuntimeWiringRegistry}.
 *
 * @author yuheng.wang
 */
public class RuntimeWiringRegistryTest {

    @Test
    public void should_get_after_register_and_init() throws Exception {
        final String type = "a";
        final String method = "b";
        final String result = "c";

        RuntimeWiringRegistry.register(type, method, (DataFetcher<?>) environment -> result);
        RuntimeWiring runtimeWiring = RuntimeWiringRegistry.initRuntimeWiring(new TypeDefinitionRegistry(), new DataFetcherInterceptorRegistry());

        assertTrue(runtimeWiring.getDataFetchers().containsKey(type));
        assertTrue(runtimeWiring.getDataFetchers().get(type).containsKey(method));
        assertEquals(result, runtimeWiring.getDataFetchers().get(type).get(method).get(null));
    }

    @Test
    public void should_get_type_by_domain_class_when_union_type() {
        TypeDefinitionRegistry typeDefinitionRegistry = new TypeDefinitionRegistry();
        final String unionName = "unionName";
        final String graphQLObjectTypeName = "graphQLObjectTypeName";

        GraphQLSchema schema = PowerMockito.mock(GraphQLSchema.class);
        when(schema.getObjectType(RuntimeWiringRegistryTest.class.getSimpleName())).thenReturn(GraphQLObjectType.newObject()
                .name(graphQLObjectTypeName)
                .build());

        Map<String, Object> obj = new HashMap<>();
        obj.put("id", "1");
        obj.put(DomainClassUtil.DOMAIN_CLASS_KEY, RuntimeWiringRegistryTest.class.getName());

        typeDefinitionRegistry.add(UnionTypeDefinition.newUnionTypeDefinition()
                .name(unionName)
                .build());
        RuntimeWiring runtimeWiring = RuntimeWiringRegistry.initRuntimeWiring(typeDefinitionRegistry, new DataFetcherInterceptorRegistry());

        TypeResolver typeResolver = runtimeWiring.getTypeResolvers().get(unionName);
        GraphQLObjectType resultGraphQLObjectType = typeResolver.getType(new TypeResolutionEnvironment(obj, null, null, null, schema, null));

        assertEquals(graphQLObjectTypeName, resultGraphQLObjectType.getName());
    }

}