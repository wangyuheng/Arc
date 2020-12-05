package com.github.yituhealthcare.arc.graphql;

import com.github.yituhealthcare.arc.graphql.interceptor.DataFetcherInterceptorRegistry;
import com.github.yituhealthcare.arc.graphql.rest.GraphQLController;
import com.github.yituhealthcare.arc.graphql.support.DataFetcherServicePostProcessor;
import com.github.yituhealthcare.arc.graphql.trace.GraphqlSleuthHttpServerResponseParser;
import brave.http.HttpResponseParser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class GraphqlAutoConfiguration {

    @Bean
    @Primary
    public DataFetcherServicePostProcessor dataFetcherServicePostProcessor() {
        return new DataFetcherServicePostProcessor();
    }

    @Bean
    @ConditionalOnMissingBean
    public GraphQLProvider graphQLProvider() {
        return new GraphQLProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    public GraphQLController graphQLController(GraphQLProvider graphQLProvider) {
        return new GraphQLController(graphQLProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public DataFetcherInterceptorRegistry dataFetcherInterceptorRegistry() {
        return new DataFetcherInterceptorRegistry();
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpResponseParser sleuthHttpServerResponseParser() {
        return new GraphqlSleuthHttpServerResponseParser();
    }
}
