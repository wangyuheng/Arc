package com.github.yituhealthcare.arc.graphql.rest;

import com.github.yituhealthcare.arc.graphql.GraphQLProvider;
import com.github.yituhealthcare.arc.mq.producer.Producer;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.Only;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Tests for {@link GraphQLController}.
 *
 * @author yuheng.wang
 */
public class GraphQLControllerTest {

    private GraphQLController graphQLController;
    private Map<String, Object> mockMap = new HashMap<>();

    @Before
    public void setUp() {
        ExecutionResult mockResult = PowerMockito.mock(ExecutionResult.class);
        mockMap.put("ak", "av");

        when(mockResult.toSpecification()).thenReturn(mockMap);
        when(mockResult.getData()).thenReturn(new LinkedHashMap<>(mockMap));
        GraphQLProvider graphQLProvider = PowerMockito.mock(GraphQLProvider.class);
        GraphQL graphQL = PowerMockito.mock(GraphQL.class);
        PowerMockito.when(graphQL.execute(any(ExecutionInput.class))).thenReturn(mockResult);
        PowerMockito.when(graphQL.execute(any(ExecutionInput.Builder.class))).thenReturn(mockResult);
        when(graphQLProvider.getGraphQL()).thenReturn(graphQL);

        graphQLController = new GraphQLController(graphQLProvider);
    }

    @Test
    public void should_return_by_query_schema_structure() throws Exception {
        GraphQLRequestBody requestBody = new GraphQLRequestBody();
        requestBody.setOperationName("IntrospectionQuery");
        requestBody.setQuery("{a}");

        MockHttpServletResponse response = new MockHttpServletResponse();
        Object result = graphQLController.dispatcher(requestBody, response);
        assertEquals(mockMap, result);
    }

    @Test
    public void should_get_methods_by_response() throws Exception {
        GraphQLRequestBody requestBody = new GraphQLRequestBody();
        requestBody.setQuery("{a}");
        MockHttpServletResponse response = new MockHttpServletResponse();
        graphQLController.dispatcher(requestBody, response);
        assertFalse(response.getHeaderNames().isEmpty());
        Set<String> result = response.getHeaderNames()
                .stream()
                .map(response::getHeader)
                .map(String::valueOf)
                .collect(Collectors.toSet());
        assertEquals(result, mockMap.keySet());
    }

    @Test
    public void should_send_message_once_when_enable_event() throws Exception {
        Producer<?> producer = PowerMockito.mock(Producer.class);
        PowerMockito.field(GraphQLController.class, "eventEnable").setBoolean(graphQLController, true);
        PowerMockito.field(GraphQLController.class, "producer").set(graphQLController, producer);

        GraphQLRequestBody requestBody = new GraphQLRequestBody();
        requestBody.setQuery("{a}");
        MockHttpServletResponse response = new MockHttpServletResponse();
        graphQLController.dispatcher(requestBody, response);

        Mockito.verify(producer, new Only()).send(any());
    }

}