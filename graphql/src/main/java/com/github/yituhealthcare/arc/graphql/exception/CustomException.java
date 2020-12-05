package com.github.yituhealthcare.arc.graphql.exception;

/**
 * 生命自定义异常，会直接抛出，不通过Graphql默认异常处理
 *
 * @see CustomDataFetcherExceptionHandler
 * @see graphql.execution.SimpleDataFetcherExceptionHandler
 */
public abstract class CustomException extends RuntimeException {

    public CustomException() {
    }

    public CustomException(String message) {
        super(message);
    }
}
