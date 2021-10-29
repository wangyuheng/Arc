package io.github.wangyuheng.arc.graphql.annotation;

import java.lang.annotation.*;

/**
 * 声明Graphql修改方法
 *
 * @see Graphql
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GraphqlMutation {

    String type() default "Mutation";

}
