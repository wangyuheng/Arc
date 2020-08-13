package ai.care.arc.graphql.annotation;

import java.lang.annotation.*;

/**
 * 声明Graphql查询方法
 *
 * @see DataFetcherService
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GraphqlQuery {

    String type() default "Query";

}
