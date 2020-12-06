package com.github.yituhealthcare.arc.graphql.rest;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GraphQLRequestBody implements Serializable {

    private String query;
    private String operationName;
    private Map<String, Object> variables = new HashMap<>();

    public GraphQLRequestBody() {
    }

    public String getQuery() {
        return this.query;
    }

    public String getOperationName() {
        return this.operationName;
    }

    public Map<String, Object> getVariables() {
        return this.variables;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public String toString() {
        return "GraphQLRequestBody(query=" + this.getQuery() + ", operationName=" + this.getOperationName() + ", variables=" + this.getVariables() + ")";
    }
}
