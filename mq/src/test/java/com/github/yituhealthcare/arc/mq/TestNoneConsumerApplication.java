package com.github.yituhealthcare.arc.mq;

import com.github.yituhealthcare.arc.mq.consumer.ConsumerBeanDefinitionRegistryPostProcessor;
import com.github.yituhealthcare.arc.mq.store.Store;
import com.github.yituhealthcare.arc.mq.store.VmStore;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;

//@SpringBootApplication
//@ComponentScan(basePackages = {"com.github.yituhealthcare.arc.mq"},
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
