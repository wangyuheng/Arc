package com.github.yituhealthcare.arc.graphql.interceptor;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.powermock.api.mockito.PowerMockito;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Tests for {@link DataFetcherDecorator}.
 *
 * @author yuheng.wang
 */
public class DataFetcherDecoratorTest {

    @Test
    public void should_handler_interceptor_once() throws Exception {
        DataFetcherHandlerInterceptor interceptor = PowerMockito.mock(DataFetcherHandlerInterceptor.class);
        when(interceptor.preHandle(any())).thenReturn(true);
        when(interceptor.postHandle(any(), any())).thenReturn(true);

        new DataFetcherDecorator(environment -> "a", Collections.singletonList(interceptor)).get(null);

        Mockito.verify(interceptor, new Times(1)).preHandle(null);
        Mockito.verify(interceptor, new Times(1)).postHandle("a", null);

    }

}