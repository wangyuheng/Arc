package io.github.wangyuheng.arc.graphqlclient.model;

import java.util.List;

/**
 * graphql 返回值
 *
 * @author yuheng.wang
 */
public class GraphqlResponse<T> {

    private T data;
    private List<GraphqlError> errors;

    public GraphqlResponse() {
    }

    public GraphqlResponse(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<GraphqlError> getErrors() {
        return errors;
    }

    public void setErrors(List<GraphqlError> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "GraphqlResponse{" +
                "data=" + data +
                ", errors=" + errors +
                '}';
    }
}