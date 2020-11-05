package ai.care.arc.dgraph.scanner;

import ai.care.arc.dgraph.annotation.ClassPathDgraphScanner;
import ai.care.arc.dgraph.annotation.DgraphScan;
import ai.care.arc.dgraph.util.DgraphTypeHolder;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * 如果未指定 {@link DgraphScan} 时自动直接注入
 * 优先级低于 DgraphScan
 *
 * @see ClassPathDgraphScanner
 */
public class AutoConfiguredDgraphScannerRegistrar implements BeanFactoryAware, BeanDefinitionRegistryPostProcessor, ResourceLoaderAware {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AutoConfiguredDgraphScannerRegistrar.class);
    private BeanFactory beanFactory;

    private ResourceLoader resourceLoader;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        if (DgraphTypeHolder.isEmpty()) {
            ClassPathDgraphScanner scanner = new ClassPathDgraphScanner(registry);
            if (this.resourceLoader != null) {
                scanner.setResourceLoader(this.resourceLoader);
            }
            List<String> basePackages;
            try {
                basePackages = AutoConfigurationPackages.get(this.beanFactory);
            } catch (IllegalStateException e) {
                log.warn("scan all package!", e);
                basePackages = Collections.singletonList("*");
            }
            log.debug("Using auto-configuration base package {}", basePackages);
            scanner.registerFilters();
            scanner.doScan(StringUtils.toStringArray(basePackages));
        }

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }
}