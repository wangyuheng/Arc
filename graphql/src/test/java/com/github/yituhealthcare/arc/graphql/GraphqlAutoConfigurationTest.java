package com.github.yituhealthcare.arc.graphql;

import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link GraphqlAutoConfiguration}.
 * 相关bean初始化成功
 *
 * @author yuheng.wang
 */
@SpringBootTest
public class GraphqlAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(GraphqlAutoConfiguration.class));

    @Test
    public void test_context_load() {
        this.contextRunner.run(context -> {
            assertNotNull(context.getBean("graphqlPostProcessor"));
            assertNotNull(context.getBean("directivePostProcessor"));
            assertNotNull(context.getBean("graphQLController"));
            assertNotNull(context.getBean("dataFetcherInterceptorRegistry"));
        });
    }

}