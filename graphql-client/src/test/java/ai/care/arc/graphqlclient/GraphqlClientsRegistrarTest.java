package ai.care.arc.graphqlclient;

import ai.care.arc.graphqlclient.annotation.EnableGraphqlClients;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.mock.env.MockEnvironment;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Tests for {@link GraphqlClientsRegistrar}.
 *
 * @author yuheng.wang
 */
public class GraphqlClientsRegistrarTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void should_completion_http_schema_if_url_not_specified() throws Exception {
        GraphqlClientsRegistrar registrar = new GraphqlClientsRegistrar();
        registrar.setEnvironment(new MockEnvironment());
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("url", "mock_url");
        String result = Whitebox.invokeMethod(registrar, "getUrl", attributes);
        assertEquals("http://mock_url", result);
    }

    @Test
    public void should_resolve_vars_by_env_when_get_url() throws Exception {
        GraphqlClientsRegistrar registrar = new GraphqlClientsRegistrar();
        MockEnvironment environment = new MockEnvironment();
        environment.setProperty("var", "abc");
        registrar.setEnvironment(environment);
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("url", "mock_${var}_url");
        String result = Whitebox.invokeMethod(registrar, "getUrl", attributes);
        assertEquals("http://mock_abc_url", result);
    }

    @Test
    public void should_throw_exception_if_attribute_not_include_url() throws Exception {
        GraphqlClientsRegistrar registrar = new GraphqlClientsRegistrar();
        registrar.setEnvironment(new MockEnvironment());
        Map<String, Object> attributes = new HashMap<>();
        thrown.expect(IllegalArgumentException.class);
        Whitebox.invokeMethod(registrar, "getUrl", attributes);
    }

    @Test
    public void should_throw_exception_if_url_illegal() throws Exception {
        GraphqlClientsRegistrar registrar = new GraphqlClientsRegistrar();
        registrar.setEnvironment(new MockEnvironment());
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("url", "abc_http://_sss");
        thrown.expect(IllegalArgumentException.class);
        Whitebox.invokeMethod(registrar, "getUrl", attributes);
    }

    @Test
    public void should_return_by_class_package_if_attribute_not_specified() throws Exception {
        GraphqlClientsRegistrar registrar = new GraphqlClientsRegistrar();
        AnnotationMetadata metadata = PowerMockito.mock(AnnotationMetadata.class);
        Map<String, Object> map = new HashMap<>();
        when(metadata.getAnnotationAttributes(anyString())).thenReturn(map);
        when(metadata.getClassName()).thenReturn(this.getClass().getName());
        Set<String> basePackages = registrar.getBasePackages(metadata);
        assertEquals(basePackages, new HashSet<>(Collections.singletonList(this.getClass().getPackage().getName())));
    }

    @Test
    public void should_return_by_class_package_if_attribute_not_specified2() throws Exception {
        GraphqlClientsRegistrar registrar = new GraphqlClientsRegistrar();
        AnnotationMetadata metadata = PowerMockito.mock(AnnotationMetadata.class);
        Map<String, Object> map = new HashMap<>();
        map.put("basePackages", Arrays.asList("a", "b").toArray(new String[]{}));
        map.put("value", Arrays.asList("b", "c").toArray(new String[]{}));
        when(metadata.getAnnotationAttributes(anyString())).thenReturn(map);
        when(metadata.getClassName()).thenReturn(this.getClass().getName());
        Set<String> basePackages = registrar.getBasePackages(metadata);
        assertEquals(basePackages, new HashSet<>(Arrays.asList("a", "b", "c")));
    }

    @Test
    public void should_register_bean_by_scan() {
        AnnotationConfigApplicationContext config = new AnnotationConfigApplicationContext();
        ((DefaultListableBeanFactory) config.getBeanFactory()).setAllowBeanDefinitionOverriding(false);
        config.register(CorrectConfig.class);
        assertThatCode(config::refresh).doesNotThrowAnyException();
        assertNotNull(config.getBean(GraphqlClientFactoryBean.class));
    }

    @Test
    public void should_throw_when_annotation_on_class_by_scan() {
        AnnotationConfigApplicationContext config = new AnnotationConfigApplicationContext();
        ((DefaultListableBeanFactory) config.getBeanFactory()).setAllowBeanDefinitionOverriding(false);
        config.register(InterfaceOnlyErrorConfig.class);
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("@GraphqlClient can only be specified on an interface");
        config.refresh();
    }

    @Test
    public void should_throw_when_url_empty_by_scan() {
        AnnotationConfigApplicationContext config = new AnnotationConfigApplicationContext();
        ((DefaultListableBeanFactory) config.getBeanFactory()).setAllowBeanDefinitionOverriding(false);
        config.register(UrlEmptyErrorConfig.class);
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("url must be not empty!");
        config.refresh();
    }

    @Configuration(proxyBeanMethods = false)
    @EnableAutoConfiguration
    @EnableGraphqlClients("ai.care.arc.graphqlclient.mock.correct")
    protected static class CorrectConfig {
    }

    @Configuration(proxyBeanMethods = false)
    @EnableAutoConfiguration
    @EnableGraphqlClients("ai.care.arc.graphqlclient.mock.interfaceonly")
    protected static class InterfaceOnlyErrorConfig {
    }

    @Configuration(proxyBeanMethods = false)
    @EnableAutoConfiguration
    @EnableGraphqlClients("ai.care.arc.graphqlclient.mock.urlempty")
    protected static class UrlEmptyErrorConfig {
    }
}