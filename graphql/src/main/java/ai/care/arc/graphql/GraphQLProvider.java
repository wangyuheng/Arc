package ai.care.arc.graphql;

import ai.care.arc.graphql.exception.CustomDataFetcherExceptionHandler;
import ai.care.arc.graphql.interceptor.DataFetcherInterceptorRegistry;
import ai.care.arc.graphql.support.RuntimeWiringRegistry;
import graphql.GraphQL;
import graphql.execution.AsyncExecutionStrategy;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class GraphQLProvider implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(GraphQLProvider.class);
    @Value("${arc.graphql.define:graphql/schema.graphqls}")
    private ClassPathResource schema;
    @Autowired(required = false)
    private DataFetcherInterceptorRegistry dataFetcherInterceptorRegistry;
    private GraphQL graphQL;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("receive event:{}", event);
        refresh();
    }

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
