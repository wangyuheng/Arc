package ai.care.arc.core.util;

import java.util.stream.Stream;

/**
 * 流操作工具
 */
public class StreamUtils {

    private StreamUtils() {
    }

    @SafeVarargs
    public static <T> Stream<T> merge(Stream<T>... a) {
        return Stream.of(a)
                .flatMap(it -> it);
    }
}
