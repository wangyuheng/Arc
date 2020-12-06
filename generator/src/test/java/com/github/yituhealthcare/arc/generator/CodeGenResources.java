package com.github.yituhealthcare.arc.generator;

import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ArgumentsSource(CodeGenResourceProvider.class)
public @interface CodeGenResources {

    CodeGenResource[] value();

}
