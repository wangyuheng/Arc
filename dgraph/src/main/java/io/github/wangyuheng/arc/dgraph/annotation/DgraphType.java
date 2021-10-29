package io.github.wangyuheng.arc.dgraph.annotation;

import java.lang.annotation.*;

/**
 * 指定存储的graph.type字段名
 *  默认为java类名
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DgraphType {

    String value() default "";

}
