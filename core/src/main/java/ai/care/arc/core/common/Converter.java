package ai.care.arc.core.common;

import java.util.Optional;

/**
 * 类型转换 I <-> O
 *
 * @author yuheng.wang
 */
public interface Converter<I, O> {

    Optional<O> convert(I i);

    Optional<I> reverse(O o);
}
