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
import graphql.schema.idl.errors.SchemaProblem;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

/**
 * 解析schema并提供graphql服务
 *
 * @author yuheng.wang
 * @see GraphQL
 */
public class GraphQLProvider implements InitializingBean {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(GraphQLProvider.class);
    @Value("${arc.graphql.define:graphql/schema.graphqls}")
    private String locationPattern;

    private final PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    private final TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();

    private final DataFetcherInterceptorRegistry dataFetcherInterceptorRegistry;

    public GraphQLProvider(DataFetcherInterceptorRegistry dataFetcherInterceptorRegistry) {
        this.dataFetcherInterceptorRegistry = dataFetcherInterceptorRegistry;
    }

    private GraphQL graphQL;

    public GraphQL getGraphQL() {
        if (null == graphQL) {
            this.refresh();
        }
        return graphQL;
    }

    private synchronized void refresh() {
        if (null == graphQL) {
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

    private void loadSchema() {
        log.info("init graphql");
        SchemaParser schemaParser = new SchemaParser();
        try {
            Resource[] resources = resourcePatternResolver.getResources(locationPattern);
            for (Resource resource : resources) {
                typeRegistry.merge(schemaParser.parse(resource.getInputStream()));
            }
        } catch (SchemaProblem schemaProblem) {
            log.error("schema defined error! locationPattern:{}", locationPattern, schemaProblem);
            throw schemaProblem;
        } catch (IOException e) {
            log.warn("read graphql schema fail!", e);
        }
        if (typeRegistry.types().isEmpty()) {
            throw new IllegalStateException("read graphql schema fail! locationPattern: " + locationPattern);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.loadSchema();
    }
}
