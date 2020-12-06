package com.github.yituhealthcare.arc.dgraph;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link DgraphAutoConfiguration}.
 * 相关bean初始化成功
 *
 * @author yuheng.wang
 */
@SpringBootTest
public class DgraphAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(DgraphAutoConfiguration.class));

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test_context_load() {
        this.contextRunner.withPropertyValues("arc.dgraph.urls=localhost:8080,localhost:8081")
                .run(context -> {
                    assertNotNull(context.getBean("autoConfiguredDgraphScannerRegistrar"));
                    assertNotNull(context.getBean("dgraphProperties"));
                    assertNotNull(context.getBean("dgraphClient"));
                    assertNotNull(context.getBean("dgraphMapperManager"));
                    assertNotNull(context.getBean("dgraphClientAdapter"));
                    assertNotNull(context.getBean("dgraphTracerFilter"));
                });
    }

    @Test
    public void should_init_by_properties_set_init_true() {
        this.contextRunner.withPropertyValues("arc.dgraph.init=true", "arc.dgraph.urls=localhost:8080")
                .run(context -> assertNotNull(context.getBean("dataSourceInitializer")));
        this.contextRunner.withPropertyValues("arc.dgraph.init=false", "arc.dgraph.urls=localhost:8080")
                .run(context -> {
                    thrown.expect(NoSuchBeanDefinitionException.class);
                    context.getBean("dataSourceInitializer");
                });
    }
}