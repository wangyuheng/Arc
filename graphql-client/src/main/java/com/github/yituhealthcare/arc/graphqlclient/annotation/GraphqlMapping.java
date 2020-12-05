package com.github.yituhealthcare.arc.graphqlclient.annotation;

import java.lang.annotation.*;

/**
 * Annotation for mapping Graphql Query File.
 *
 * @author yuheng.wang
 * @see GraphqlClient
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GraphqlMapping {

    /**
     * The path of Graphql Query File.
     */
    String path();

}