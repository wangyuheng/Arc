package com.github.yituhealthcare.arc.mq;

import com.github.yituhealthcare.arc.mq.consumer.ConsumerBeanDefinitionRegistryPostProcessor;
import com.github.yituhealthcare.arc.mq.producer.DefaultProducer;
import com.github.yituhealthcare.arc.mq.producer.Producer;
import com.github.yituhealthcare.arc.mq.store.Store;
import com.github.yituhealthcare.arc.mq.store.VmStore;
import com.github.yituhealthcare.arc.mq.transport.Transport;
import com.github.yituhealthcare.arc.mq.transport.VmTransport;
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
@ComponentScan("com.github.yituhealthcare.arc.mq")
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
