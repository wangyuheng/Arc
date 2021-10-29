package io.github.wangyuheng.arc.dgraph.annotation;

import io.github.wangyuheng.arc.dgraph.util.DgraphTypeHolder;
import org.slf4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 扫描 {@link DgraphType} bean
 * 并通过 {@link DgraphTypeHolder} 持有
 *
 * @author yuheng.wang
 */
public class ClassPathDgraphScanner extends ClassPathBeanDefinitionScanner {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ClassPathDgraphScanner.class);

    public ClassPathDgraphScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        Set<BeanDefinition> beanDefinitions = beanDefinitionHolders.stream()
                .map(BeanDefinitionHolder::getBeanDefinition)
                .collect(Collectors.toSet());
        this.scanExistingDef(beanDefinitions, basePackages);

        if (beanDefinitions.isEmpty()) {
            logger.warn("No Dgraph Type was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        } else {
            processBeanDefinitions(beanDefinitions);
        }

        return beanDefinitionHolders;
    }

    /**
     * 避免通过其他annotation已经被spring扫描的bean不会重复添加
     *
     * @see ClassPathBeanDefinitionScanner#checkCandidate
     */
    private Set<BeanDefinition> scanExistingDef(Set<BeanDefinition> beanDefinitions, String... basePackages) {
        Assert.notEmpty(basePackages, "At least one base package must be specified");
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            for (BeanDefinition candidate : candidates) {
                try {
                    String beanName = AnnotationBeanNameGenerator.INSTANCE.generateBeanName(candidate, Objects.requireNonNull(super.getRegistry()));
                    beanDefinitions.add(super.getRegistry().getBeanDefinition(beanName));
                } catch (NoSuchBeanDefinitionException e) {
                    log.warn("no such bean for scan! basePackages:{}", beanDefinitions, e);
                } catch (NullPointerException e) {
                    log.warn("scan fail!", e);
                }
            }
        }
        return beanDefinitions;
    }

    public void registerFilters() {
        addIncludeFilter(new AnnotationTypeFilter(DgraphType.class));
    }

    private void processBeanDefinitions(Set<BeanDefinition> beanDefinitions) {
        beanDefinitions.forEach(DgraphTypeHolder::add);
    }
}
