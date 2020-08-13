package ai.care.arc.graphql.client;

import com.alibaba.fastjson.JSON;
import ai.care.arc.graphql.GraphQLProvider;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;

public class GraphqlClient {
    private final GraphQLProvider graphQLProvider;

    public GraphqlClient(GraphQLProvider graphQLProvider) {
        this.graphQLProvider = graphQLProvider;
    }

    public <T> T execute(String query, Type type) {
        String method = query.split("\\{")[1].trim();
        LinkedHashMap<String, Object> data = this.doExecute(query);
        String json = JSON.toJSONString(data.get(method));
        return JSON.parseObject(json, type);
    }

    private LinkedHashMap<String, Object> doExecute(String query) {
        return graphQLProvider.getGraphQL().execute(query).getData();
    }
}
