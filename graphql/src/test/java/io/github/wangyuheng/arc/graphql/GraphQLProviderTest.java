package io.github.wangyuheng.arc.graphql;

import graphql.GraphQL;
import graphql.schema.idl.errors.SchemaProblem;
import io.github.wangyuheng.arc.graphql.interceptor.DataFetcherInterceptorRegistry;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.Times;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link GraphQLProvider}.
 * mock GraphQL 初始化
 *
 * @author yuheng.wang
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(GraphQLProvider.class)
public class GraphQLProviderTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private GraphQLProvider graphQLProvider;

    @Before
    public void setUp() {
        graphQLProvider = new GraphQLProvider(new DataFetcherInterceptorRegistry());
    }

    @Test
    public void graphQL_must_not_null() throws Exception {
        GraphQLProvider mock = PowerMockito.spy(graphQLProvider);
        PowerMockito.field(GraphQLProvider.class, "locationPattern").set(mock, "graphql/s*.graphqls");
        mock.afterPropertiesSet();
        assertNotNull(mock.getGraphQL());
    }

    @Test
    public void should_init_once_if_schema_existed() throws Exception {
        GraphQLProvider mock = PowerMockito.spy(graphQLProvider);
        PowerMockito.field(GraphQLProvider.class, "locationPattern").set(mock, "graphql/schema*.graphqls");
        mock.afterPropertiesSet();
        GraphQL graphQL = mock.getGraphQL();
        assertNotNull(graphQL);
        PowerMockito.verifyPrivate(mock, new Times(1)).invoke("loadSchema");
    }

    @Test
    public void should_throw_illegal_state_if_schema_not_existed() throws Exception {
        GraphQLProvider mock = PowerMockito.spy(graphQLProvider);
        PowerMockito.field(GraphQLProvider.class, "locationPattern").set(mock, "graphql/not_existed_schema.graphqls");
        thrown.expect(IllegalStateException.class);
        mock.afterPropertiesSet();
    }

    @Test
    public void should_throw_illegal_state_if_schema_type_repeat() throws Exception {
        GraphQLProvider mock = PowerMockito.spy(graphQLProvider);
        PowerMockito.field(GraphQLProvider.class, "locationPattern").set(mock, "graphql/*.graphqls");
        thrown.expect(SchemaProblem.class);
        mock.afterPropertiesSet();
    }

}