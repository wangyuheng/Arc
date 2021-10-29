package io.github.wangyuheng.arc.dgraph.annotation;

import io.github.wangyuheng.arc.dgraph.util.DgraphTypeHolder;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link ClassPathDgraphScanner}.
 *
 * @author yuheng.wang
 */
public class ClassPathDgraphScannerTest {

    @DgraphType("mockDgraphType")
    static class MockDgraphTypeClass {

    }

    @Test
    public void should_scan_empty_without_register_filter() {
        DgraphTypeHolder.clear();
        ClassPathDgraphScanner scanner = new ClassPathDgraphScanner(new DefaultListableBeanFactory());
        Set<BeanDefinitionHolder> holderSet = scanner.doScan(this.getClass().getPackage().getName());
        assertEquals(0, holderSet.size());
        assertEquals(0, DgraphTypeHolder.listDgraphType().size());
    }

    @Test
    public void should_scan_types_after_register_filter() {
        DgraphTypeHolder.clear();
        ClassPathDgraphScanner scanner = new ClassPathDgraphScanner(new DefaultListableBeanFactory());
        scanner.registerFilters();
        Set<BeanDefinitionHolder> holderSet = scanner.doScan(this.getClass().getPackage().getName());
        assertEquals(1, holderSet.size());
        assertEquals(1, DgraphTypeHolder.listDgraphType().size());
    }

}