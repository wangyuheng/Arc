package com.github.yituhealthcare.arc.graphqlclient;

import com.github.yituhealthcare.arc.graphqlclient.annotation.GraphqlMapping;
import com.github.yituhealthcare.arc.graphqlclient.annotation.GraphqlParam;
import com.github.yituhealthcare.arc.graphqlclient.model.GraphqlResponse;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import java.lang.reflect.Method;

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
    public void should_put_vars_by_param() throws Throwable {
        Method method = Subject.class.getMethod("graphqlMethodWithParam", String.class, String.class);
        GraphqlResponse<String> result = (GraphqlResponse<String>) handler.invoke(subject, method, new Object[]{"mock_id", "mock_name"});
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
        public String graphqlMethodWithoutParam() {
            return "ok";
        }

        @GraphqlMapping(path = "echo.graphql")
        public String graphqlMethodWithParam(String id, @GraphqlParam("nickname") String name) {
            return "ok";
        }

    }

}