package ai.care.arc.core.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for {@link StreamUtils}.
 *
 * @author yuheng.wang
 */
public class StreamUtilsTest {

    @Test
    public void should_merge_streams_in_one() {
        assertArrayEquals(new Object[]{"a", "b", "c", Arrays.asList("d", "e")},
                StreamUtils.merge(Stream.of("a"), Stream.of("b", "c"), Stream.of(Arrays.asList("d", "e"))).toArray());
    }

}