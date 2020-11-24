package ai.care.arc.graphqlclient;

import ai.care.arc.graphqlclient.exception.GraphqlClientException;
import ai.care.arc.graphqlclient.model.GraphqlRequest;
import ai.care.arc.graphqlclient.model.GraphqlResponse;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

/**
 * 发送http请求graphql接口
 *
 * @author yuheng.wang
 */
public class GraphqlTemplate {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_0_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.67 Safari/537.36";

    public GraphqlTemplate() {
        this(new RestTemplateBuilder().build());
    }

    public GraphqlTemplate(RestTemplate restTemplate) {
        this(restTemplate, new ObjectMapper());
    }

    public GraphqlTemplate(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public <T> GraphqlResponse<T> execute(String url, GraphqlRequest request, Class<T> type) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 部分api服务会对 User-Agent 进行校验，通过默认配置模拟浏览器请求
        headers.add(HttpHeaders.USER_AGENT, DEFAULT_USER_AGENT);
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, headers), String.class);
        } catch (Exception e) {
            throw new GraphqlClientException(e);
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new GraphqlClientException("execute graphql is fail! status=" + response + " body:" + response.getBody());
        }
        return new GraphqlResponse<>(JSON.parseObject(response.getBody(), type));
    }

    public GraphqlResponse<String> execute(String url, GraphqlRequest request) {
        return this.execute(url, request, String.class);
    }

}