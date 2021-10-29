package io.github.wangyuheng.arc.graphql.annotation;

import java.lang.annotation.*;

/**
 * 声明Graphql方法
 *  必须指定type类型
 *
 * @see Graphql
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GraphqlMethod {

    String type();

}
