package ai.care.arc.graphql.rest;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class GraphQLRequestBody implements Serializable {

    private String query;
    private String operationName;
    private Map<String, Object> variables = new HashMap<>();

}
