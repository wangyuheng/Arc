package ai.care.arc.graphql.exception;

import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import graphql.execution.SimpleDataFetcherExceptionHandler;

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
        } else {
            return super.onException(DataFetcherExceptionHandlerParameters
                    .newExceptionParameters()
                    .exception(new RuntimeException("general exception",exception))
                    .dataFetchingEnvironment(handlerParameters.getDataFetchingEnvironment())
                    .build());
        }
    }
}
