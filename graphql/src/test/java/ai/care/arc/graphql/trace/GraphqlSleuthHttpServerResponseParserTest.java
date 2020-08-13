package ai.care.arc.graphql.trace;

import ai.care.arc.graphql.GraphqlAutoConfiguration;
import brave.Span;
import brave.Tracer;
import brave.Tracing;
import brave.handler.MutableSpan;
import brave.handler.SpanHandler;
import brave.http.HttpResponseParser;
import brave.http.HttpServerResponse;
import brave.propagation.TraceContext;
import brave.servlet.HttpServletResponseWrapper;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link GraphqlSleuthHttpServerResponseParser}.
 * 手动创建 {@link SpanHandler} 模拟zipkin处理
 *
 * @author yuheng.wang
 */
public class GraphqlSleuthHttpServerResponseParserTest {

    @Test
    public void should_set_span_name_by_response_header() {
        GraphqlSleuthHttpServerResponseParser parser = new GraphqlSleuthHttpServerResponseParser();

        final String name = "abc";
        List<MutableSpan> spans = new ArrayList<>();
        Tracer tracer = Tracing.newBuilder()
                .addSpanHandler(new SpanHandler() {
                    @Override
                    public boolean end(TraceContext context, MutableSpan span, Cause cause) {
                        spans.add(span);
                        return true;
                    }
                })
                .build().tracer();
        Span span = tracer.nextSpan();


        MockHttpServletResponse httpResponse = new MockHttpServletResponse();
        httpResponse.setHeader(HttpResponseParser.class.getName(), name);

        HttpServerResponse response = HttpServletResponseWrapper.create(new MockHttpServletRequest(), httpResponse, new NullPointerException());
        parser.parse(response, TraceContext.newBuilder().traceId(1L).spanId(1L).build(), span);
        span.flush();

        assertEquals(name, spans.get(0).name());
    }
}