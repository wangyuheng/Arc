package ai.care.arc.graphql.exception;

import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import graphql.execution.SimpleDataFetcherExceptionHandler;

public class CustomDataFetcherExceptionHandler extends SimpleDataFetcherExceptionHandler {

    @Override
    public DataFetcherExceptionHandlerResult onException(DataFetcherExceptionHandlerParameters handlerParameters) {
        Throwable exception = handlerParameters.getException();
        if (exception instanceof CustomException) {
            throw (RuntimeException) exception;
        } else {
            return super.onException(handlerParameters);
        }
    }
}
