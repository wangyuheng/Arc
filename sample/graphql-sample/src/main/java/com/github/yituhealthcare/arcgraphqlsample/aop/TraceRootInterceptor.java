package com.github.yituhealthcare.arcgraphqlsample.aop;

import com.github.yituhealthcare.arc.graphql.interceptor.DataFetcherHandlerInterceptor;
import graphql.GraphQLContext;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLNamedType;
import graphql.schema.GraphQLType;

import java.util.Objects;

public class TraceRootInterceptor implements DataFetcherHandlerInterceptor {

    @Override
    public boolean preHandle(DataFetchingEnvironment environment) {
        GraphQLType parentType = environment.getParentType();
        if (parentType instanceof GraphQLNamedType && Objects.nonNull(environment.getSource())) {
            GraphQLContext context = environment.getContext();
            context.put(((GraphQLNamedType) parentType).getName(), environment.getSource());
        }
        return true;
    }

}
