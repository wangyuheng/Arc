package ai.care.arc.dgraph.trace;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import brave.handler.MutableSpan;
import brave.handler.SpanHandler;
import brave.propagation.ThreadLocalSpan;
import brave.propagation.TraceContext;
import io.dgraph.DgraphProto;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DgraphTracerFilterTest {

    private DgraphTracerFilter dgraphTracerFilter = new DgraphTracerFilter();
    private static List<MutableSpan> spans = new ArrayList<>();

    @BeforeClass
    public static void setUp() {
        Tracer tracer = Tracing.newBuilder()
                .addSpanHandler(new SpanHandler() {
                    @Override
                    public boolean end(TraceContext context, MutableSpan span, Cause cause) {
                        spans.add(span);
                        return true;
                    }

                    @Override
                    public boolean begin(TraceContext context, MutableSpan span, TraceContext parent) {
                        return true;
                    }

                    @Override
                    public boolean handlesAbandoned() {
                        return true;
                    }
                })
                .build().tracer();

        ThreadLocalSpan.create(tracer);
    }

    @Test
    public void should_span_dgraph_mutation_info_after_pre_mutation() throws Exception {
        DgraphProto.Request request = DgraphProto.Request.newBuilder().build();
        assertTrue(dgraphTracerFilter.preMutationHandle("a", null, request));
        assertTrue(dgraphTracerFilter.postMutationHandle(null, null, request));

        MutableSpan span = spans.stream()
                .filter(it -> "mutation: a".equals(it.name()))
                .findAny()
                .orElseThrow(NullPointerException::new);

        assertEquals(Span.Kind.CLIENT, span.kind());
        assertTrue(span.tags().containsKey("dgraph.mutation"));
    }

    @Test
    public void should_span_dgraph_query_info_after_pre_query() throws Exception {
        String sql = "a";
        assertTrue(dgraphTracerFilter.preQueryHandle(null, sql));
        assertTrue(dgraphTracerFilter.postQueryHandle(null, null, sql));

        MutableSpan span = spans.stream()
                .filter(it -> "query: a".equals(it.name()))
                .findAny()
                .orElseThrow(NullPointerException::new);

        assertEquals(Span.Kind.CLIENT, span.kind());
    }

    @Test
    public void should_clear_trace_after_post_handle() {
        ThreadLocalSpan.CURRENT_TRACER.next();
        Span span = ThreadLocalSpan.CURRENT_TRACER.remove();
        assertNotNull(span);
        span.finish();
        ThreadLocalSpan.CURRENT_TRACER.next();
        assertTrue(dgraphTracerFilter.postMutationHandle(null, null, null));
        assertNull(ThreadLocalSpan.CURRENT_TRACER.remove());

        ThreadLocalSpan.CURRENT_TRACER.next();
        assertTrue(dgraphTracerFilter.postQueryHandle(null, null, null));
        assertNull(ThreadLocalSpan.CURRENT_TRACER.remove());
    }

}