package com.github.yituhealthcare.arc.graphqlclient;

import com.github.yituhealthcare.arc.graphqlclient.exception.GraphqlClientException;
import com.github.yituhealthcare.arc.graphqlclient.model.GraphqlRequest;
import com.github.yituhealthcare.arc.graphqlclient.model.GraphqlResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link GraphqlTemplate}.
 *
 * @author yuheng.wang
 */
public class GraphqlTemplateTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void should_serialize_response_by_http_server() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        MockResult mockResult = new MockResult();
        mockResult.setName("mock_name");
        mockResult.setName("mock_age");

        MockRestServiceServer mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
        mockRestServiceServer.expect(MockRestRequestMatchers.anything())
                .andRespond(MockRestResponseCreators.withSuccess(new ObjectMapper().writeValueAsString(mockResult), MediaType.APPLICATION_JSON));

        GraphqlResponse<MockResult> response = new GraphqlTemplate(restTemplate).execute("http://mock-url", new GraphqlRequest("{}"), MockResult.class);
        assertEquals(mockResult, response.getData());
    }

    @Test
    public void should_return_body_when_type_string() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        MockRestServiceServer mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
        mockRestServiceServer.expect(MockRestRequestMatchers.anything())
                .andRespond(MockRestResponseCreators.withSuccess("abc", MediaType.APPLICATION_JSON));

        GraphqlResponse<String> response = new GraphqlTemplate(restTemplate).execute("http://mock-url", new GraphqlRequest("{}"));
        assertEquals("abc", response.getData());
    }

    @Test
    public void should_throw_exception_if_url_illegal() {
        GraphqlRequest graphqlRequest = new GraphqlRequest("{}");
        GraphqlTemplate graphqlTemplate = new GraphqlTemplate();
        thrown.expect(IllegalArgumentException.class);
        graphqlTemplate.execute("mock-url", graphqlRequest);
    }

    @Test
    public void should_throw_rest_exception_when_5xx() {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
        mockRestServiceServer.expect(MockRestRequestMatchers.anything())
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.INTERNAL_SERVER_ERROR));
        thrown.expect(RestClientException.class);
        new GraphqlTemplate(restTemplate).execute("http://mock-url", new GraphqlRequest("{}"), MockResult.class);
    }

    /**
     * //TODO 考虑 3xx 如何处理?
     * 目前是抛出 {@link GraphqlClientException} 异常. 是否需要跟踪重定向?
     * 如果需要跟踪重定向, 是否仍暴露 {@link RestTemplate} 作为构造参数？
     */
    @Test
    public void should_throw_custom_exception_when_3xx() {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
        mockRestServiceServer.expect(MockRestRequestMatchers.anything())
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.MOVED_PERMANENTLY));
        thrown.expect(GraphqlClientException.class);
        new GraphqlTemplate(restTemplate).execute("http://mock-url", new GraphqlRequest("{}"), MockResult.class);
    }

    static class MockResult {
        private String name;
        private String age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MockResult that = (MockResult) o;
            return Objects.equals(name, that.name) &&
                    Objects.equals(age, that.age);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }
    }
}