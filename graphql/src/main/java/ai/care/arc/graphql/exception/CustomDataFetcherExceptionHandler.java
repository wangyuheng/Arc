package ai.care.arc.graphql.exception;

import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import graphql.execution.SimpleDataFetcherExceptionHandler;

import java.util.concurrent.CompletionException;

public class CustomDataFetcherExceptionHandler extends SimpleDataFetcherExceptionHandler {

    @Override
    public DataFetcherExceptionHandlerResult onException(DataFetcherExceptionHandlerParameters handlerParameters) {
        Throwable exception = handlerParameters.getException();
        if (exception instanceof CustomException) {
            return super.onException(DataFetcherExceptionHandlerParameters
                    .newExceptionParameters()
                    .exception(exception)
                    .dataFetchingEnvironment(handlerParameters.getDataFetchingEnvironment())
                    .build());
        } else if (exception instanceof RuntimeException && exception.getCause() instanceof CompletionException) {
            return super.onException(DataFetcherExceptionHandlerParameters
                    .newExceptionParameters()
                    .exception(new RuntimeException("操作过于频繁，请稍后再试",exception.getCause()))
                    .dataFetchingEnvironment(handlerParameters.getDataFetchingEnvironment())
                    .build());
        } else {
            return super.onException(handlerParameters);
        }
    }
}
