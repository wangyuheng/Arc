package io.github.wangyuheng.arcgraphqlexample.directive;

import io.github.wangyuheng.arc.graphql.annotation.Directive;
import io.github.wangyuheng.arcgraphqlexample.biz.AuthService;
import graphql.GraphQLContext;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLFieldsContainer;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author wangyuheng
 */
@Directive("Auth")
public class AuthDirective implements SchemaDirectiveWiring {

    @Autowired
    private AuthService authService;

    @Override
    public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> environment) {
        String targetAuthRole = (String) environment.getDirective().getArgument("role").getValue();

        GraphQLFieldDefinition field = environment.getElement();
        GraphQLFieldsContainer parentType = environment.getFieldsContainer();
        // build a data fetcher that first checks authorisation roles before then calling the original data fetcher
        DataFetcher originalDataFetcher = environment.getCodeRegistry().getDataFetcher(parentType, field);
        DataFetcher authDataFetcher = dataFetchingEnvironment -> {
            GraphQLContext graphQLContext = dataFetchingEnvironment.getContext();
            Map<String, Object> headers = graphQLContext.get("headers");
            String authorization = String.valueOf(headers.getOrDefault("authorization", ""));
            authService.valid(targetAuthRole, authorization);
            return originalDataFetcher.get(dataFetchingEnvironment);
        };
        // now change the field definition to have the new authorising data fetcher
        environment.getCodeRegistry().dataFetcher(parentType, field, authDataFetcher);
        return field;
    }

}
