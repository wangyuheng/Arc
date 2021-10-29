package io.github.wangyuheng.arc.graphql;

import brave.http.HttpResponseParser;
import io.github.wangyuheng.arc.graphql.interceptor.DataFetcherInterceptorRegistry;
import io.github.wangyuheng.arc.graphql.rest.GraphQLController;
import io.github.wangyuheng.arc.graphql.support.DirectivePostProcessor;
import io.github.wangyuheng.arc.graphql.support.GraphqlPostProcessor;
import io.github.wangyuheng.arc.graphql.trace.GraphqlSleuthHttpServerResponseParser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author wangyuheng
 */
@Configuration
public class GraphqlAutoConfiguration {

    @Bean
    @Primary
    public GraphqlPostProcessor graphqlPostProcessor() {
        return new GraphqlPostProcessor();
    }

    @Bean
    @Primary
    public DirectivePostProcessor directivePostProcessor() {
        return new DirectivePostProcessor();
    }

    @Bean
    @ConditionalOnMissingBean
    public GraphQLProvider graphQLProvider() {
        return new GraphQLProvider(dataFetcherInterceptorRegistry());
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
