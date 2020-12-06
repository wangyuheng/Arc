package com.github.yituhealthcare.arc.graphql.interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataFetcherInterceptorRegistry {

    private List<ConditionalInterceptorWrapper> interceptorWrapperList = new ArrayList<>();

    public List<DataFetcherHandlerInterceptor> getMatchedInterceptor(String dataFetcherName) {
        return interceptorWrapperList.stream()
                .filter(it -> it.isMatch(dataFetcherName))
                .map(ConditionalInterceptorWrapper::getInterceptor)
                .collect(Collectors.toList());
    }
    
    public ConditionalInterceptorWrapper addInterceptor(DataFetcherHandlerInterceptor interceptor) {
        ConditionalInterceptorWrapper wrapper = new ConditionalInterceptorWrapper(interceptor);
        this.interceptorWrapperList.add(wrapper);
        return wrapper;
    }
}
