package io.github.wangyuheng.arc.dgraph.scanner;

import io.github.wangyuheng.arc.dgraph.annotation.DgraphType;
import io.github.wangyuheng.arc.dgraph.util.DgraphTypeHolder;
import org.junit.Test;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

/**
 * Tests for {@link AutoConfiguredDgraphScannerRegistrar}.
 *
 * @author yuheng.wang
 */
public class AutoConfiguredDgraphScannerRegistrarTest {

    private final AutoConfiguredDgraphScannerRegistrar registrar = new AutoConfiguredDgraphScannerRegistrar();

    @DgraphType("mockDgraphType")
    static class MockDgraphTypeClass {

    }

    @Test
    public void should_ignore_when_holder_not_empty() {
        DgraphTypeHolder.add(BeanDefinitionBuilder.genericBeanDefinition(MockDgraphTypeClass.class).getBeanDefinition());
        registrar.postProcessBeanDefinitionRegistry(null);
    }

}