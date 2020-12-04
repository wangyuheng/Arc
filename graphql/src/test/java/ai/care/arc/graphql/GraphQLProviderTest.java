package ai.care.arc.graphql;

import graphql.GraphQL;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.Times;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.core.io.ClassPathResource;

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

    @Test
    public void graphQL_must_not_null() throws IllegalAccessException {
        GraphQLProvider mock = PowerMockito.spy(new GraphQLProvider());
        PowerMockito.field(GraphQLProvider.class, "schema").set(mock, new ClassPathResource("graphql/schema.graphqls"));
        assertNotNull(mock.getGraphQL());
    }

    @Test
    public void should_not_init_if_graphQL_existed() throws Exception {
        GraphQLProvider mock = PowerMockito.spy(new GraphQLProvider());
        PowerMockito.field(GraphQLProvider.class, "schema").set(mock, new ClassPathResource("graphql/schema.graphqls"));
        PowerMockito.verifyPrivate(mock, new Times(0)).invoke("initGraphQL");
    }

    @Test
    public void should_init_once_if_not_graphQL_not_existed() throws Exception {
        GraphQLProvider mock = PowerMockito.spy(new GraphQLProvider());
        PowerMockito.field(GraphQLProvider.class, "schema").set(mock, new ClassPathResource("graphql/schema.graphqls"));
        GraphQL graphQL = mock.getGraphQL();
        assertNotNull(graphQL);
        PowerMockito.verifyPrivate(mock, new Times(1)).invoke("initGraphQL");
    }

    @Test
    public void should_throw_illegal_state_if_schema_not_existed() throws Exception {
        GraphQLProvider mock = new GraphQLProvider();
        PowerMockito.field(GraphQLProvider.class, "schema").set(mock, new ClassPathResource("graphql/not_existed_schema.graphqls"));
        thrown.expect(IllegalStateException.class);
        mock.getGraphQL();
    }

}