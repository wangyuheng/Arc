package io.github.wangyuheng.arc.graphql.interceptor;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;

/**
 * 扩展 {@link DataFetcher} 功能，提供AOP增强
 * 根据 {@link ConditionalInterceptorWrapper} 中指定的规则触发 {@link DataFetcherHandlerInterceptor} 调用
 *
 * @author yuheng.wang
 */
public class DataFetcherDecorator<T> implements DataFetcher<T> {

    private DataFetcher<T> delegate;
    private List<DataFetcherHandlerInterceptor> interceptors;

    public DataFetcherDecorator(DataFetcher<T> delegate, List<DataFetcherHandlerInterceptor> interceptors) {
        this.delegate = delegate;
        this.interceptors = interceptors;
    }

    @Override
    public T get(DataFetchingEnvironment environment) throws Exception {
        this.doPreHandle(environment);
        T result = delegate.get(environment);
        this.doPostHandle(result, environment);
        return result;
    }

    private void doPostHandle(T result, DataFetchingEnvironment environment) {
        for (DataFetcherHandlerInterceptor interceptor : interceptors) {
            if (!interceptor.postHandle(result, environment)) {
                throw new IllegalStateException("handler graphql post interceptor fail! stop chain. interceptor:" + interceptor);
            }
        }
    }

    private void doPreHandle(DataFetchingEnvironment environment) {
        for (DataFetcherHandlerInterceptor interceptor : interceptors) {
            if (!interceptor.preHandle(environment)) {
                throw new IllegalStateException("handler graphql pre interceptor fail! stop chain. interceptor:" + interceptor);
            }
        }
    }
}
