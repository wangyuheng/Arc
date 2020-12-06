package com.github.yituhealthcare.arc.dgraph.annotation;

import java.lang.annotation.*;

/**
 * 声明dgraph uid字段,决定uid返回map
 *  为了扩展如果回填uid值时，可以指定对应的key
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UidField {
}
