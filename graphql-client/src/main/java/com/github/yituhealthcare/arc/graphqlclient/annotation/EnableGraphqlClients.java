package com.github.yituhealthcare.arc.graphqlclient.annotation;

import com.github.yituhealthcare.arc.graphqlclient.GraphqlClientsRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Scans for interfaces that declare they are graphql clients (via {@link GraphqlClient
 * <code>@GraphqlClient</code>}). Configures component scanning directives for use with
 * {@link org.springframework.context.annotation.Configuration
 * <code>@Configuration</code>} classes.
 *
 * @author yuheng.wang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(GraphqlClientsRegistrar.class)
public @interface EnableGraphqlClients {

    /**
     * Alias for the {@link #basePackages()} attribute. Allows for more concise annotation
     * declarations e.g.: {@code @ComponentScan("org.my.pkg")} instead of
     * {@code @ComponentScan(basePackages="org.my.pkg")}.
     *
     * @return the array of 'basePackages'.
     */
    String[] value() default {};

    /**
     * Base packages to scan for annotated components.
     * <p>
     * {@link #value()} is an alias for (and mutually exclusive with) this attribute.
     *
     * @return the array of 'basePackages'.
     */
    String[] basePackages() default {};
}
