package com.github.yituhealthcare.arc.core.common;

/**
 * 类型转换 I - O
 *
 * @author yuheng.wang
 */
public interface Converter<I, O> {

    O convert(I i);

    I reverse(O o);
}
