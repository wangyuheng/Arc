package io.github.wangyuheng.arc.dgraph.util;

import java.lang.annotation.*;

/**
 * @author junhao.chen
 * @date 2020/7/20
 * 用于标识此union包含哪些类，用于生成动态sql
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface UnionClasses {
    Class<?>[] value() default {};
}
