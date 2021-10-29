package io.github.wangyuheng.arc.graphql.exception;

import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import graphql.execution.SimpleDataFetcherExceptionHandler;

public class CustomDataFetcherExceptionHandler extends SimpleDataFetcherExceptionHandler {

    @Override
    public DataFetcherExceptionHandlerResult onException(DataFetcherExceptionHandlerParameters handlerParameters) {
        Throwable exception = handlerParameters.getException();
        //todo 提供新的异常处理 隐藏location信息和exception整体信息 只保留message并额外添加errorCode
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
