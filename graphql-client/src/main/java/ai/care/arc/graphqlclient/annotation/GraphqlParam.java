package ai.care.arc.graphqlclient.annotation;

import java.lang.annotation.*;

/**
 * Annotation for mapping Graphql Variables.
 * User Java parameter name as the variables key if unassigned this annotation
 *
 * @author yuheng.wang
 * @see GraphqlClient
 * @see GraphqlMapping
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GraphqlParam {

    /**
     * The key of Graphql Query Variables.
     */
    String value();

}