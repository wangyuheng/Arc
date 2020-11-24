package ai.care.arc.graphqlclient.model;

import java.util.HashMap;
import java.util.Map;

/**
 * graphql 请求参数
 *
 * @author yuheng.wang
 */
public class GraphqlRequest {

    /**
     * graphql 执行语句
     */
    private final String query;
    /**
     * graphql 变量，配合语句中的变量定义使用
     */
    private Map<String, Object> variables = new HashMap<>();

    public GraphqlRequest(String query) {
        this.query = query;
    }

    public GraphqlRequest(String query, Map<String, Object> variables) {
        this.query = query;
        this.variables = variables;
    }

    public String getQuery() {
        return query;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }
}
