package io.github.wangyuheng.arc.mq;

import io.github.wangyuheng.arc.mq.producer.Producer;
import io.github.wangyuheng.arc.mq.store.Store;
import io.github.wangyuheng.arc.mq.transport.Transport;
import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class MqAutoConfigurationTest {


    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(MqAutoConfiguration.class));

    @Test
    public void should_load_mock_component_context() {
        this.contextRunner.run(context -> assertNotNull(context.getBean(MockComponent.class)));
    }

    @Test
    public void should_load_required_bean() {
        this.contextRunner.run(context -> {
            assertNotNull(context.getBean(Store.class));
            assertNotNull(context.getBean(Transport.class));
            assertNotNull(context.getBean(Producer.class));
        });
    }

    @Component
    static class MockComponent {

    }

}