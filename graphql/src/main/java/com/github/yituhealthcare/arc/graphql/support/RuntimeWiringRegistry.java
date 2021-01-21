package com.github.yituhealthcare.arc.graphql.support;

import com.github.yituhealthcare.arc.core.util.DomainClassUtil;
import com.github.yituhealthcare.arc.graphql.interceptor.DataFetcherDecorator;
import com.github.yituhealthcare.arc.graphql.interceptor.DataFetcherHandlerInterceptor;
import com.github.yituhealthcare.arc.graphql.interceptor.DataFetcherInterceptorRegistry;
import graphql.language.InterfaceTypeDefinition;
import graphql.language.TypeDefinition;
import graphql.language.UnionTypeDefinition;
import graphql.scalars.ExtendedScalars;
import graphql.schema.DataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.idl.TypeRuntimeWiring;
import graphql.validation.rules.OnValidationErrorStrategy;
import graphql.validation.rules.ValidationRules;
import graphql.validation.schemawiring.ValidationSchemaWiring;
import org.slf4j.Logger;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * 注册DataFetcher
 * 配合Annotation实现自动扫描+注册
 * - {@link com.github.yituhealthcare.arc.graphql.annotation.GraphqlMethod}
 * - {@link com.github.yituhealthcare.arc.graphql.annotation.GraphqlQuery}
 * - {@link com.github.yituhealthcare.arc.graphql.annotation.GraphqlMutation}
 * <p>
 * 基于 {@link ExtendedScalars} 实现类型扩展
 *
 * @author yuheng.wang
 */
public class RuntimeWiringRegistry {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(RuntimeWiringRegistry.class);

    private RuntimeWiringRegistry() {
    }

    private static final Map<String, Map<String, DataFetcher<?>>> TYPE_MAP_FIELD_AND_DF = new ConcurrentHashMap<>();
    private static final Map<String, SchemaDirectiveWiring> NAME_AND_DIRECTIVE_WIRING = new ConcurrentHashMap<>();

    public static void register(String type, String fieldName, DataFetcher<?> dataFetcher) {
        Map<String, DataFetcher<?>> map = TYPE_MAP_FIELD_AND_DF.getOrDefault(type, new ConcurrentHashMap<>());
        map.put(fieldName, dataFetcher);
        TYPE_MAP_FIELD_AND_DF.putIfAbsent(type, map);
    }

    public static void registerDirective(String name, SchemaDirectiveWiring schemaDirectiveWiring) {
        NAME_AND_DIRECTIVE_WIRING.putIfAbsent(name, schemaDirectiveWiring);
    }

    private static TypeRuntimeWiring.Builder builder(String name, Map<String, DataFetcher<?>> dataFetcherMap, DataFetcherInterceptorRegistry dataFetcherInterceptorRegistry) {
        Map<String, DataFetcher> decoratorMap = new HashMap<>();
        dataFetcherMap.forEach((key, value) -> {
            if (dataFetcherInterceptorRegistry != null) {
                List<DataFetcherHandlerInterceptor> interceptors = dataFetcherInterceptorRegistry.getMatchedInterceptor(key);
                if (!CollectionUtils.isEmpty(interceptors)) {
                    decoratorMap.put(key, environment -> new DataFetcherDecorator<>(value, interceptors).get(environment));
                    return;
                }
            }
            decoratorMap.put(key, value);
        });
        return TypeRuntimeWiring.newTypeWiring(name).dataFetchers(decoratorMap);
    }

    public static RuntimeWiring initRuntimeWiring(TypeDefinitionRegistry typeRegistry, DataFetcherInterceptorRegistry dataFetcherInterceptorRegistry) {
        log.info("graphql registered method:{}", TYPE_MAP_FIELD_AND_DF);

        RuntimeWiring.Builder builder = RuntimeWiring.newRuntimeWiring()
                .scalar(ExtendedScalars.DateTime)
                .scalar(ExtendedScalars.Json)
                .scalar(ExtendedScalars.Object)
                .scalar(ExtendedScalars.Date);
        TYPE_MAP_FIELD_AND_DF.forEach((type, map) -> builder.type(builder(type, map, dataFetcherInterceptorRegistry)));
        NAME_AND_DIRECTIVE_WIRING.forEach(builder::directive);

        Stream.of(typeRegistry.getTypes(UnionTypeDefinition.class), typeRegistry.getTypes(InterfaceTypeDefinition.class))
                .flatMap(Collection::stream)
                .forEach(RuntimeWiringRegistry.fillBuilder(builder));

        builder.directiveWiring(new ValidationSchemaWiring(ValidationRules.newValidationRules()
                // will return null for the field input if it is not considered valid
                .onValidationErrorStrategy(OnValidationErrorStrategy.RETURN_NULL)
                .build()));

        return builder.build();
    }

    /**
     * UnionTypeDefinition | InterfaceTypeDefinition 基于domainClass实现反序列化
     */
    private static Consumer<TypeDefinition<?>> fillBuilder(RuntimeWiring.Builder builder) {
        return typeDefinition -> builder.type(TypeRuntimeWiring.newTypeWiring(typeDefinition.getName())
                .typeResolver(env -> {
                    String domainClassName = Optional.ofNullable(env.getObject())
                            .map(DomainClassUtil::getDomainClass)
                            .map(Class::getSimpleName)
                            .orElseThrow(() -> new IllegalArgumentException("domainClass Illegal"));
                    return env.getSchema().getObjectType(domainClassName);
                }));
    }

}
