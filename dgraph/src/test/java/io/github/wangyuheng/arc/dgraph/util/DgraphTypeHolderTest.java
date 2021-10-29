package io.github.wangyuheng.arc.dgraph.util;

import io.github.wangyuheng.arc.dgraph.annotation.DgraphType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

import static org.junit.Assert.*;

public class DgraphTypeHolderTest {

    private BeanDefinition beanDefinition;

    @Before
    public void setup() {
        DgraphTypeHolder.clear();
        beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(DgraphTypeClass.class).getBeanDefinition();
    }

    @Test
    public void should_be_not_empty_after_add_util_clear() {
        assertTrue(DgraphTypeHolder.isEmpty());
        DgraphTypeHolder.add(beanDefinition);
        assertFalse(DgraphTypeHolder.isEmpty());
        DgraphTypeHolder.clear();
        assertTrue(DgraphTypeHolder.isEmpty());
    }

    @Test
    public void should_get_unique_when_repeat_add() {
        DgraphTypeHolder.add(beanDefinition);
        DgraphTypeHolder.add(beanDefinition);
        assertEquals(DgraphTypeHolder.listDomainClassName().size(), 1);
        assertEquals(DgraphTypeHolder.listDgraphType().size(), 1);
    }

    @Test
    public void should_get_domain_class_name() {
        DgraphTypeHolder.add(beanDefinition);
        String domainClassName = DgraphTypeHolder.listDomainClassName().stream()
                .findAny()
                .orElseThrow(NullPointerException::new);
        assertEquals(DgraphTypeClass.class.getName(), domainClassName);
    }

    @Test
    public void should_get_graph_type() {
        DgraphTypeHolder.add(beanDefinition);
        String graphType = DgraphTypeHolder.listDgraphType().stream()
                .findAny()
                .orElseThrow(NullPointerException::new);
        assertEquals("gt", graphType);
    }

    @DgraphType("gt")
    static class DgraphTypeClass {

    }
}