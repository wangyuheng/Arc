package io.github.wangyuheng.arc.graphql.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 声明Directive类并作为component加入spring生命周期
 *
 * @author wangyuheng
 * @see io.github.wangyuheng.arc.graphql.support.DirectivePostProcessor
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Directive {

    /**
     * The name of the directive to wire.
     * Default is spring bean name.
     *
     * @return directive name
     */
    String value() default "";

}
