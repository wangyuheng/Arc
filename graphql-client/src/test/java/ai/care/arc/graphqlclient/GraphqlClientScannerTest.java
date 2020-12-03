package ai.care.arc.graphqlclient;

import ai.care.arc.graphqlclient.annotation.GraphqlClient;
import org.junit.Test;
import org.powermock.core.classloader.MockClassLoader;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.mock.env.MockEnvironment;

import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link GraphqlClientScanner}.
 *
 * @author yuheng.wang
 */
public class GraphqlClientScannerTest {

    @Test
    public void should_scan_class_and_interface_client() {
        GraphqlClientScanner scanner = new GraphqlClientScanner(MockClassLoader.getSystemClassLoader(), new MockEnvironment(), new DefaultResourceLoader());
        Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents(this.getClass().getPackage().getName());

        assertTrue(beanDefinitions.stream().anyMatch(it -> Objects.equals(it.getBeanClassName(), A.class.getName())));
        assertTrue(beanDefinitions.stream().anyMatch(it -> Objects.equals(it.getBeanClassName(), B.class.getName())));
        assertFalse(beanDefinitions.stream().anyMatch(it -> Objects.equals(it.getBeanClassName(), C.class.getName())));
    }

    @Test
    public void should_ignore_annotation_class() {
        GraphqlClientScanner scanner = new GraphqlClientScanner(MockClassLoader.getSystemClassLoader(), new MockEnvironment(), new DefaultResourceLoader());

        assertTrue(scanner.isCandidateComponent(new AnnotatedGenericBeanDefinition(B.class)));
        assertFalse(scanner.isCandidateComponent(new AnnotatedGenericBeanDefinition(C.class)));
        assertTrue(scanner.isCandidateComponent(new AnnotatedGenericBeanDefinition(D.class)));
    }

    @GraphqlClient(url = "a", value = "a")
    static class A {
    }

    @GraphqlClient(url = "b", value = "b")
    interface B {
    }

    @GraphqlClient(url = "c", value = "c")
    @interface C {
    }

    @GraphqlClient(url = "d", value = "d")
    interface D extends Annotation {
    }

}