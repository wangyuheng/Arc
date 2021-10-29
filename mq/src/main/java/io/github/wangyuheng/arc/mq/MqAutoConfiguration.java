package io.github.wangyuheng.arc.mq;

import io.github.wangyuheng.arc.mq.consumer.ConsumerBeanDefinitionRegistryPostProcessor;
import io.github.wangyuheng.arc.mq.producer.DefaultProducer;
import io.github.wangyuheng.arc.mq.producer.Producer;
import io.github.wangyuheng.arc.mq.store.Store;
import io.github.wangyuheng.arc.mq.store.VmStore;
import io.github.wangyuheng.arc.mq.transport.Transport;
import io.github.wangyuheng.arc.mq.transport.VmTransport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 消息队列自动配置
 * 扫描类及初始化
 *
 * @see ConsumerBeanDefinitionRegistryPostProcessor
 * @see Store
 * @see Transport
 */
@Configuration
@ComponentScan("io.github.wangyuheng.arc.mq")
public class MqAutoConfiguration {

    @Bean
    public ConsumerBeanDefinitionRegistryPostProcessor consumerBeanDefinitionRegistryPostProcessor() {
        return new ConsumerBeanDefinitionRegistryPostProcessor();
    }

    @Bean
    @ConditionalOnMissingBean(Store.class)
    public Store store() {
        return new VmStore();
    }

    @Bean
    @ConditionalOnMissingBean(Transport.class)
    public Transport transport(Store store) {
        return new VmTransport(store);
    }

    @Bean
    @ConditionalOnMissingBean(Producer.class)
    public <T> Producer<T> producer(Transport transport) {
        return new DefaultProducer<>(transport);
    }

}
