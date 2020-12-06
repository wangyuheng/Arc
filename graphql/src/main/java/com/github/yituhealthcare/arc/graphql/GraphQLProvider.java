package com.github.yituhealthcare.arc.graphql;

import com.github.yituhealthcare.arc.graphql.exception.CustomDataFetcherExceptionHandler;
import com.github.yituhealthcare.arc.graphql.interceptor.DataFetcherInterceptorRegistry;
import com.github.yituhealthcare.arc.graphql.support.RuntimeWiringRegistry;
import graphql.GraphQL;
import graphql.execution.AsyncExecutionStrategy;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * 解析schema并提供graphql服务
 *
 * @author yuheng.wang
 * @see GraphQL
 */
public class GraphQLProvider {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(GraphQLProvider.class);
    @Value("${arc.graphql.define:graphql/schema.graphqls}")
    private ClassPathResource schema;
    @Autowired(required = false)
    private DataFetcherInterceptorRegistry dataFetcherInterceptorRegistry;
    private GraphQL graphQL;

    public GraphQL getGraphQL() {
        if (null == graphQL) {
            this.refresh();
        }
        return graphQL;
    }

    private synchronized void refresh() {
        if (null == graphQL) {
            this.initGraphQL();
        }
    }

    private void initGraphQL() {
        log.info("init graphql");
        TypeDefinitionRegistry typeRegistry;
        try {
            typeRegistry = new SchemaParser().parse(schema.getInputStream());
        } catch (IOException e) {
            log.error("read graphql schema fail!", e);
            throw new IllegalStateException("read graphql schema fail! path: " + schema.getPath());
        }

        GraphQLSchema graphQLSchema = new SchemaGenerator()
                .makeExecutableSchema(typeRegistry, RuntimeWiringRegistry.initRuntimeWiring(typeRegistry, dataFetcherInterceptorRegistry));
        this.graphQL = GraphQL
                .newGraphQL(graphQLSchema)
                .queryExecutionStrategy(new AsyncExecutionStrategy(new CustomDataFetcherExceptionHandler()))
                .mutationExecutionStrategy(new AsyncExecutionStrategy(new CustomDataFetcherExceptionHandler()))
                .build()
        ;
    }

}
