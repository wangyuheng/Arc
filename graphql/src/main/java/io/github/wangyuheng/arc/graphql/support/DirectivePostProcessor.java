package io.github.wangyuheng.arc.graphql.support;

import io.github.wangyuheng.arc.graphql.annotation.Directive;
import graphql.schema.idl.SchemaDirectiveWiring;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 扫描注册Directive类
 *
 * @author yuheng.wang
 * @see Directive
 * @see RuntimeWiringRegistry
 */
public class DirectivePostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
        if (targetClass.isAnnotationPresent(Directive.class)) {
            Directive directive = targetClass.getAnnotation(Directive.class);
            if (bean instanceof SchemaDirectiveWiring) {
                RuntimeWiringRegistry.registerDirective(directive.value(), (SchemaDirectiveWiring) bean);
            } else {
                throw new BeanInitializationException("directive bean must implements SchemaDirectiveWiring! bean:" + beanName);
            }
        }
        return bean;
    }

}
