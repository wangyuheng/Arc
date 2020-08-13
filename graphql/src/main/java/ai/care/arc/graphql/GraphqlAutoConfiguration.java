package ai.care.arc.graphql;

import ai.care.arc.graphql.client.GraphqlClient;
import ai.care.arc.graphql.interceptor.DataFetcherInterceptorRegistry;
import ai.care.arc.graphql.rest.GraphQLController;
import ai.care.arc.graphql.support.DataFetcherServicePostProcessor;
import ai.care.arc.graphql.trace.GraphqlSleuthHttpServerResponseParser;
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
    public GraphqlClient graphqlClient(GraphQLProvider graphQLProvider) {
        return new GraphqlClient(graphQLProvider);
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
