package io.github.wangyuheng.arc.graphqlclient;

import io.github.wangyuheng.arc.graphqlclient.annotation.GraphqlMapping;
import io.github.wangyuheng.arc.graphqlclient.annotation.GraphqlParam;
import io.github.wangyuheng.arc.graphqlclient.model.GraphqlResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.api.mockito.PowerMockito;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Tests for {@link GraphqlClientInvocationHandler}.
 *
 * @author yuheng.wang
 */
public class GraphqlClientInvocationHandlerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private GraphqlClientInvocationHandler handler;
    private Subject subject;
    private GraphqlTemplate template;

    @Before
    public void setUp() {
        String url = "mock_url";
        template = PowerMockito.mock(GraphqlTemplate.class);
        when(template.execute(anyString(), any())).thenReturn(new GraphqlResponse<>("mockResult"));
        handler = new GraphqlClientInvocationHandler(url, template);
        subject = new Subject();
    }

    @Test
    public void should_invoke_original_method_without_annotation() throws Throwable {
        Method method = Subject.class.getMethod("m1", String.class);
        String result = (String) handler.invoke(subject, method, new Object[]{"parameter1"});
        assertEquals("parameter1", result);
    }

    @Test
    public void should_get_response_by_template_execute() throws Throwable {
        Method method = Subject.class.getMethod("graphqlMethodWithoutParam");
        GraphqlResponse<String> result = (GraphqlResponse<String>) handler.invoke(subject, method, new Object[]{});
        assertEquals("mockResult", result.getData());
    }

    @Test
    public void should_get_response_and_deserialize_generic() throws Throwable {
        String url = "mock_url";
        GraphqlTemplate templateGeneric = PowerMockito.mock(GraphqlTemplate.class);
        when(templateGeneric.execute(anyString(), any(), any())).thenReturn(new GraphqlResponse<>(new MockType("m1")));
        GraphqlClientInvocationHandler handlerGeneric = new GraphqlClientInvocationHandler(url, templateGeneric);
        Method method = Subject.class.getMethod("graphqlMethodWithGeneric");
        GraphqlResponse<MockType> result = (GraphqlResponse<MockType>) handlerGeneric.invoke(new Subject(), method, new Object[]{});
        assertEquals("m1", result.getData().getId());
    }

    @Test
    public void should_exception_when_generic_nested() throws Throwable {
        String url = "mock_url";
        GraphqlTemplate templateGeneric = PowerMockito.mock(GraphqlTemplate.class);
        GraphqlClientInvocationHandler handlerGeneric = new GraphqlClientInvocationHandler(url, templateGeneric);
        Method method = Subject.class.getMethod("graphqlMethodWithListGeneric");
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("GraphqlResponse generic must be not nested!");
        handlerGeneric.invoke(new Subject(), method, new Object[]{});
    }

    @Test
    public void should_exception_when_return_not_graphql_response() throws Throwable {
        String url = "mock_url";
        GraphqlTemplate templateGeneric = PowerMockito.mock(GraphqlTemplate.class);
        GraphqlClientInvocationHandler handlerGeneric = new GraphqlClientInvocationHandler(url, templateGeneric);
        Method method = Subject.class.getMethod("graphqlMethodReturnString");
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("GraphqlClient method must return GraphqlResponse!");
        handlerGeneric.invoke(new Subject(), method, new Object[]{});
    }

    @Test
    public void should_put_vars_by_param() throws Throwable {
        Method method = Subject.class.getMethod("graphqlMethodWithParam", String.class, String.class);
        GraphqlResponse result = (GraphqlResponse) handler.invoke(subject, method, new Object[]{"mock_id", "mock_name"});
        verify(template).execute(anyString(), argThat(r ->
                2 == r.getVariables().size() &&
                        "mock_id".equals(r.getVariables().get("id")) &&
                        "mock_name".equals(r.getVariables().get("nickname"))
        ));
        assertEquals("mockResult", result.getData());
    }

    static class Subject {

        public String m1(String p1) {
            return p1;
        }

        @GraphqlMapping(path = "echo.graphql")
        public GraphqlResponse graphqlMethodWithoutParam() {
            return null;
        }

        @GraphqlMapping(path = "echo.graphql")
        public GraphqlResponse graphqlMethodWithParam(String id, @GraphqlParam("nickname") String name) {
            return null;
        }

        @GraphqlMapping(path = "echo.graphql")
        public GraphqlResponse<MockType> graphqlMethodWithGeneric() {
            return new GraphqlResponse<>(new MockType("mockId"));
        }

        @GraphqlMapping(path = "echo.graphql")
        public GraphqlResponse<List<MockType>> graphqlMethodWithListGeneric() {
            return null;
        }
        @GraphqlMapping(path = "echo.graphql")
        public String graphqlMethodReturnString() {
            return null;
        }
    }

    static class MockType {
        private String id;

        public MockType(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

}