package com.github.yituhealthcare.arc.graphqlclient;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.yituhealthcare.arc.graphqlclient.exception.GraphqlClientException;
import com.github.yituhealthcare.arc.graphqlclient.model.GraphqlRequest;
import com.github.yituhealthcare.arc.graphqlclient.model.GraphqlResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

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
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public GraphqlTemplate(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public <T> GraphqlResponse<T> execute(String url, GraphqlRequest request, Class<?> type) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        // 部分api服务会对 User-Agent 进行校验，通过默认配置模拟浏览器请求
        headers.add(HttpHeaders.USER_AGENT, DEFAULT_USER_AGENT);
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, headers), String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new GraphqlClientException("execute graphql is fail! status=" + response + " body:" + response.getBody());
            }
            return objectMapper.readValue(response.getBody(), objectMapper.getTypeFactory().constructParametricType(GraphqlResponse.class, type));
        } catch (RestClientException | IllegalArgumentException | GraphqlClientException e) {
            throw e;
        } catch (Exception e) {
            throw new GraphqlClientException(e);
        }
    }

    public GraphqlResponse<String> execute(String url, GraphqlRequest request) {
        return this.execute(url, request, String.class);
    }

}