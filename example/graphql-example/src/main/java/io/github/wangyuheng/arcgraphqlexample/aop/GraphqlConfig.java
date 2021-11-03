package io.github.wangyuheng.arcgraphqlexample.aop;

import io.github.wangyuheng.arc.graphql.interceptor.DataFetcherInterceptorRegistry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphqlConfig implements InitializingBean {

    @Bean
    public TraceRootInterceptor traceRootInterceptor(){
        return new TraceRootInterceptor();
    }

    @Bean
    public DataFetcherInterceptorRegistry dataFetcherInterceptorRegistry() {
        return new DataFetcherInterceptorRegistry();
    }

    public void addInterceptors(DataFetcherInterceptorRegistry registry) {
        registry.addInterceptor(traceRootInterceptor());
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        this.addInterceptors(dataFetcherInterceptorRegistry());
    }

}
