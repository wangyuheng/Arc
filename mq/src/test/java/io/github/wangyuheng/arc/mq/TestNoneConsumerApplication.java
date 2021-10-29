package io.github.wangyuheng.arc.mq;

import io.github.wangyuheng.arc.mq.consumer.ConsumerBeanDefinitionRegistryPostProcessor;
import io.github.wangyuheng.arc.mq.store.Store;
import io.github.wangyuheng.arc.mq.store.VmStore;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;

//@SpringBootApplication
//@ComponentScan(basePackages = {"io.github.wangyuheng.arc.mq"},
//        excludeFilters = {@ComponentScan.Filter(
//                type = FilterType.ASSIGNABLE_TYPE,
//                value = {MqAutoConfiguration.class})
//        })
public class TestNoneConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestNoneConsumerApplication.class, args);
    }


    @Bean
    public ConsumerBeanDefinitionRegistryPostProcessor consumerBeanDefinitionRegistryPostProcessor() {
        return new ConsumerBeanDefinitionRegistryPostProcessor();
    }

    @Bean
    public Store store() {
        return new VmStore();
    }


}
