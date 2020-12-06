package com.github.yituhealthcare.arc.graphqlclient;

import com.github.yituhealthcare.arc.graphqlclient.annotation.GraphqlClient;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;

/**
 * 扫描指定目录下的 {@link GraphqlClient} 类
 *
 * @author yuheng.wang
 */
public class GraphqlClientScanner extends ClassPathScanningCandidateComponentProvider {

    private final ClassLoader classLoader;
    private final ResourceLoader resourceLoader;

    public GraphqlClientScanner(ClassLoader classLoader, Environment environment, ResourceLoader resourceLoader) {
        super(false, environment);
        this.classLoader = classLoader;
        this.resourceLoader = resourceLoader;
        if (resourceLoader != null) {
            this.setResourceLoader(this.resourceLoader);
        }
        this.addIncludeFilter(new AnnotationTypeFilter(GraphqlClient.class));
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        if (beanDefinition.getMetadata().isIndependent()) {
            // ignore @interface
            if (beanDefinition.getMetadata().isInterface()
                    && beanDefinition.getMetadata().getInterfaceNames().length == 1
                    && Annotation.class.getName().equals(beanDefinition.getMetadata().getInterfaceNames()[0])) {
                try {
                    Class<?> target = ClassUtils.forName(beanDefinition.getMetadata().getClassName(), classLoader);
                    return !target.isAnnotation();
                } catch (Exception ex) {
                    this.logger.error("Could not load target class: " + beanDefinition.getMetadata().getClassName(), ex);
                }
            }
            return true;
        }
        return false;
    }

}
