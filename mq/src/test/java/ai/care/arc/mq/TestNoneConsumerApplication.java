package ai.care.arc.mq;

import ai.care.arc.mq.consumer.ConsumerBeanDefinitionRegistryPostProcessor;
import ai.care.arc.mq.store.Store;
import ai.care.arc.mq.store.VmStore;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;

//@SpringBootApplication
//@ComponentScan(basePackages = {"ai.care.arc.mq"},
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
