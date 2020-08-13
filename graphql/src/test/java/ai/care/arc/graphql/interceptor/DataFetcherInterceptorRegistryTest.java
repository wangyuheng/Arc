package ai.care.arc.graphql.interceptor;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link DataFetcherInterceptorRegistry}.
 *
 * @author yuheng.wang
 */
public class DataFetcherInterceptorRegistryTest {

    @Test
    public void should_get_matched_interceptor(){
        DataFetcherInterceptorRegistry registry = new DataFetcherInterceptorRegistry();

        registry.addInterceptor(null).addPatterns("(?i)project");
        registry.addInterceptor(null);

        assertEquals(2, registry.getMatchedInterceptor("Project").size());
        assertEquals(1, registry.getMatchedInterceptor("task").size());
    }
}