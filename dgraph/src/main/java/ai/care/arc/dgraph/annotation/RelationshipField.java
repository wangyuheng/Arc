package ai.care.arc.dgraph.annotation;

import java.lang.annotation.*;

/**
 * 声明关系字段，不增加前缀
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RelationshipField {
    String value() default "";
    boolean reverse() default false;
}
