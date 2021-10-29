package io.github.wangyuheng.arc.graphqlclient.annotation;

import java.lang.annotation.*;

/**
 * Annotation for interfaces declaring that a GraphQL client with that interface should be
 * created (e.g. for autowiring into another component).
 *
 * @author yuheng.wang
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GraphqlClient {

    /**
     * The name of the service with optional protocol prefix.
     * The value must be specified for all clients.
     */
    String value();

    /**
     * An absolute URL or resolvable hostname.
     */
    String url();

}