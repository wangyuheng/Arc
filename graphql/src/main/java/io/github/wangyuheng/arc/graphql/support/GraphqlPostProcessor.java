package io.github.wangyuheng.arc.graphql.support;

import io.github.wangyuheng.arc.graphql.annotation.Graphql;
import io.github.wangyuheng.arc.graphql.annotation.GraphqlMethod;
import io.github.wangyuheng.arc.graphql.annotation.GraphqlMutation;
import io.github.wangyuheng.arc.graphql.annotation.GraphqlQuery;
import graphql.schema.DataFetcher;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

/**
 * 扫描注册DataFetcher方法
 *
 * @author yuheng.wang
 * @see Graphql
 * @see GraphqlMutation
 * @see GraphqlQuery
 * @see RuntimeWiringRegistry
 */
public class GraphqlPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
        if (targetClass.isAnnotationPresent(Graphql.class)) {
            Method[] methods = ReflectionUtils.getAllDeclaredMethods(targetClass);
            for (Method method : methods) {
                Optional.ofNullable(method.getAnnotation(GraphqlQuery.class))
                        .map(GraphqlQuery::type)
                        .ifPresent(type -> RuntimeWiringRegistry.register(type, method.getName(), (DataFetcher) Objects.requireNonNull(ReflectionUtils.invokeMethod(method, bean))));
                Optional.ofNullable(method.getAnnotation(GraphqlMutation.class))
                        .map(GraphqlMutation::type)
                        .ifPresent(type -> RuntimeWiringRegistry.register(type, method.getName(), (DataFetcher) Objects.requireNonNull(ReflectionUtils.invokeMethod(method, bean))));
                Optional.ofNullable(method.getAnnotation(GraphqlMethod.class))
                        .map(GraphqlMethod::type)
                        .ifPresent(type -> RuntimeWiringRegistry.register(type, method.getName(), (DataFetcher) Objects.requireNonNull(ReflectionUtils.invokeMethod(method, bean))));
            }
        }
        return bean;
    }

}
