package com.github.yituhealthcare.arc.generator.codegen;

import java.util.Objects;

/**
 * 给某个对象填充属性，并返回填充后的对象
 * @param <T> 填充对象
 * @param <U> 填充属性
 */
@FunctionalInterface
public interface Filler<T, U> {

    T apply(T t, U u);

    default Filler<T, U> andThen(Filler<? super T, ? super U> after) {
        Objects.requireNonNull(after);

        return (t, u) -> {
            apply(t, u);
            after.apply(t, u);
            return t;
        };
    }
}