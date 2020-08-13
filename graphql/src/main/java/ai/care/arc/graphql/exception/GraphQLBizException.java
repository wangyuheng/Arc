package ai.care.arc.graphql.exception;

import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.List;

/**
 * 自定义业务异常，修改GraphQL返回值
 *
 * @author yuheng.wang
 * @see CustomDataFetcherExceptionHandler
 */
public class GraphQLBizException implements GraphQLError {

    private String message;

    public GraphQLBizException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public ErrorClassification getErrorType() {
        return null;
    }
}
