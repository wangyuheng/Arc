package ai.care.arc.graphqlclient;

import ai.care.arc.graphqlclient.annotation.EnableGraphqlClients;
import ai.care.arc.graphqlclient.annotation.GraphqlClient;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 将 {@link GraphqlClient} 修饰的interface类生成代理，并注册为Spring Bean
 *
 * @author yuheng.wang
 */
public class GraphqlClientsRegistrar implements ImportBeanDefinitionRegistrar,
        ResourceLoaderAware, BeanClassLoaderAware, EnvironmentAware {

    private ClassLoader classLoader;
    private Environment environment;
    private ResourceLoader resourceLoader;

    private static final String ENABLE_GRAPHQL_CLIENTS_BASE_PACKAGES = "basePackages";
    private static final String ENABLE_GRAPHQL_CLIENTS_VALUE = "value";
    private static final String URL_HTTP_SEG = "://";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        registerGraphqlClients(importingClassMetadata, registry);
    }

    private void registerGraphqlClients(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        final GraphqlClientScanner scanner = new GraphqlClientScanner(classLoader, environment, resourceLoader);
        final GraphqlTemplate graphqlTemplate = new GraphqlTemplate();

        getBasePackages(metadata).stream()
                .flatMap(basePackage -> scanner.findCandidateComponents(basePackage).stream())
                .filter(AnnotatedBeanDefinition.class::isInstance)
                .map(AnnotatedBeanDefinition.class::cast)
                .map(AnnotatedBeanDefinition::getMetadata)
                .forEach(annotationMetadata -> {
                    Assert.isTrue(annotationMetadata.isInterface(), "@GraphqlClient can only be specified on an interface");
                    Map<String, Object> attributes = annotationMetadata.getAnnotationAttributes(GraphqlClient.class.getCanonicalName());
                    registerGraphqlClient(registry, annotationMetadata, graphqlTemplate, attributes);
                });
    }

    private void registerGraphqlClient(BeanDefinitionRegistry registry,
                                       AnnotationMetadata annotationMetadata, GraphqlTemplate graphqlTemplate, Map<String, Object> attributes) {
        String className = annotationMetadata.getClassName();
        BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(GraphqlClientFactoryBean.class);
        definition.addPropertyValue("url", getUrl(attributes));
        definition.addPropertyValue("type", className);
        definition.addPropertyValue("graphqlTemplate", graphqlTemplate);
        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className);
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    private String getUrl(Map<String, Object> attributes) {
        String url = resolve((String) attributes.get("url"));
        Assert.hasText(url, "url must be not empty!");
        if (!url.contains(URL_HTTP_SEG)) {
            url = "http" + URL_HTTP_SEG + url;
        }
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(url + " is malformed", e);
        }
        return url;
    }

    private String resolve(String value) {
        if (StringUtils.hasText(value)) {
            return this.environment.resolvePlaceholders(value);
        }
        return value;
    }

    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Set<String> basePackages = new HashSet<>();
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableGraphqlClients.class.getCanonicalName()));

        if (null != annotationAttributes) {
            if (annotationAttributes.containsKey(ENABLE_GRAPHQL_CLIENTS_BASE_PACKAGES)) {
                for (String pkg : annotationAttributes.getStringArray(ENABLE_GRAPHQL_CLIENTS_BASE_PACKAGES)) {
                    if (StringUtils.hasText(pkg)) {
                        basePackages.add(pkg);
                    }
                }
            }
            if (annotationAttributes.containsKey(ENABLE_GRAPHQL_CLIENTS_VALUE)) {
                for (String pkg : annotationAttributes.getStringArray(ENABLE_GRAPHQL_CLIENTS_VALUE)) {
                    if (StringUtils.hasText(pkg)) {
                        basePackages.add(pkg);
                    }
                }
            }
        }
        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }
        return basePackages;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

}
