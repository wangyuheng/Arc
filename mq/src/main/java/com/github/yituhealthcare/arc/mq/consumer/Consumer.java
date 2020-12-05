package com.github.yituhealthcare.arc.mq.consumer;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Consumer {

    /**
     * 消息主题
     */
    String topic();

    /**
     * 消费者标识
     *  缺省值为 beanName + methodName
     */
    String id() default "";

}
