package com.github.yituhealthcare.arc.dgraph.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 指定扫描目录
 *
 * @see ClassPathDgraphScanner
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(DgraphScannerRegistrar.class)
@Documented
public @interface DgraphScan {
    String[] basePackage() default {};
}
