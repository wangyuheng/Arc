package ai.care.arc.graphql.interceptor;

import graphql.schema.DataFetchingEnvironment;

public interface DataFetcherHandlerInterceptor {

    default boolean preHandle(DataFetchingEnvironment environment) {
        return true;
    }

    default boolean postHandle(Object result, DataFetchingEnvironment environment) {
        return true;
    }

}
