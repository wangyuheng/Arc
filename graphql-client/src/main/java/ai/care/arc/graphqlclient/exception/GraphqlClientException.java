package ai.care.arc.graphqlclient.exception;

/**
 * GraphqlClient请求异常
 *
 * @author yuheng.wang
 */
public class GraphqlClientException extends RuntimeException {

    public GraphqlClientException(String message) {
        super(message);
    }

    public GraphqlClientException(Throwable cause) {
        super(cause);
    }
}