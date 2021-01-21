package com.github.yituhealthcare.arc.graphql.rest;

import brave.http.HttpResponseParser;
import com.github.yituhealthcare.arc.graphql.GraphQLProvider;
import com.github.yituhealthcare.arc.graphql.event.DomainEvent;
import com.github.yituhealthcare.arc.mq.Message;
import com.github.yituhealthcare.arc.mq.producer.Producer;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQLContext;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class GraphQLController {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(GraphQLController.class);
    @Autowired(required = false)
    private Producer<DomainEvent> producer;

    private GraphQLProvider graphQLProvider;

    public GraphQLController(GraphQLProvider graphQLProvider) {
        this.graphQLProvider = graphQLProvider;
    }

    @Value("${arc.graphql.event.enable:true}")
    private boolean eventEnable;

    @PostMapping(value = "${arc.graphql.path:graphql}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object dispatcher(@RequestBody GraphQLRequestBody requestBody, HttpServletRequest request, HttpServletResponse response) {
        log.info("graphql receive body:{}", requestBody);
        if ("IntrospectionQuery".equalsIgnoreCase(requestBody.getOperationName())) {
            // schema 查询直接return
            return graphQLProvider.getGraphQL().execute(ExecutionInput.newExecutionInput(requestBody.getQuery()).build()).toSpecification();
        }
        ExecutionInput executionInput = ExecutionInput.newExecutionInput()
                .query(requestBody.getQuery())
                .operationName(requestBody.getOperationName())
                .variables(requestBody.getVariables())
                .context(GraphQLContext.newContext()
                        .of("query", requestBody.getQuery())
                        .of("headers", Collections.list(request.getHeaderNames())
                                .stream()
                                .collect(Collectors.toMap(h -> h, request::getHeader)))
                        .build())
                .build();

        ExecutionResult executionResult = graphQLProvider.getGraphQL().execute(executionInput);
        if (executionResult.getData() instanceof LinkedHashMap) {
            Map<String, Object> data = executionResult.getData();
            if (data.size() > 0) {
                String methods = String.join(",", data.keySet());
                response.addHeader(HttpResponseParser.class.getName(), methods);
            }
        }
        if (eventEnable) {
            publishDomainEvent(executionInput, executionResult);
        }
        return executionResult.toSpecification();
    }

    private void publishDomainEvent(ExecutionInput executionInput, ExecutionResult executionResult) {
        LinkedHashMap<String, Object> map = executionResult.getData();
        if (null != map) {
            map.keySet().forEach(k -> {
                Message<DomainEvent> record = new Message<>();
                record.setTopic(k);
                record.setData(new DomainEvent(executionInput, executionResult));
                producer.send(record);
            });
        }
    }

}
