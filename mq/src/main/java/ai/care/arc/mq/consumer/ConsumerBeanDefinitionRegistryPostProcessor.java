package ai.care.arc.mq.consumer;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import ai.care.arc.mq.store.Store;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * 注册消费者bean
 *
 * @see Consumer
 * @see MessageHandler
 * @see Store
 */
@Slf4j
public class ConsumerBeanDefinitionRegistryPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(targetClass);
        for (Method method : methods) {
            if (AnnotatedElementUtils.hasAnnotation(method, Consumer.class)) {
                final String topic = method.getAnnotation(Consumer.class).topic();
                final String id = StringUtils.isEmpty(method.getAnnotation(Consumer.class).id()) ? beanName + method.getName() : method.getAnnotation(Consumer.class).id();
                final BeanFactory beanFactory = applicationContext.getBeanFactory();
                final Store store = beanFactory.getBean(Store.class);
                method.setAccessible(true);
                final MessageHandler messageHandler = message -> {
                    log.info("consumer {} receive topic: {}", id, topic);
                    Span span = Optional.ofNullable(Tracing.currentTracer())
                            .map(Tracer::nextSpan)
                            .orElse(null);
                    try {
                        if (null != span && !span.isNoop()) {
                            span.kind(Span.Kind.CONSUMER).name("mq.consumer: " + id);
                            span.tag("mq.topic", topic);
                            span.start();
                        }
                        log.debug("consumer {} receive topic: {} message: {}", id, topic, message);
                        ReflectionUtils.invokeMethod(method, bean, message);
                    } catch (Exception e) {
                        log.error("init consumer listener fail! ", e);
                        throw new IllegalStateException("init consumer listener fail!", e);
                    } finally {
                        if (null != span && !span.isNoop()) {
                            span.finish();
                        }
                    }
                };

                final BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(ConsumerCluster.class, () -> {
                    ConsumerCluster consumerCluster = new ConsumerCluster();
                    consumerCluster.setId(id);
                    consumerCluster.setTopic(topic);
                    consumerCluster.setMessageHandler(messageHandler);
                    consumerCluster.start(store);
                    return consumerCluster;
                });
                BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
                ((DefaultListableBeanFactory) beanFactory).registerBeanDefinition(beanName + method.getName() + "Listener", beanDefinition);
            }
        }
        return bean;
    }

}
