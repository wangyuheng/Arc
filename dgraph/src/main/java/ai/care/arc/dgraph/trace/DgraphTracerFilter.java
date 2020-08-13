package ai.care.arc.dgraph.trace;

import ai.care.arc.dgraph.repository.DgraphInterceptor;
import brave.Span;
import brave.propagation.ThreadLocalSpan;
import io.dgraph.AsyncTransaction;
import io.dgraph.DgraphGrpc;
import io.dgraph.DgraphProto;
import io.dgraph.Transaction;
import io.grpc.Channel;
import io.grpc.stub.AbstractStub;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class DgraphTracerFilter implements DgraphInterceptor {

    @Override
    public boolean preMutationHandle(String method, Transaction txn, DgraphProto.Request request) {
        Span span = ThreadLocalSpan.CURRENT_TRACER.next();
        try {
            if (isNormalSpan(span)) {
                span.kind(Span.Kind.CLIENT).name("mutation: " + method);
                span.tag("dgraph.mutation", Objects.toString(request.getMutationsList()));
                this.getChannel(txn)
                        .ifPresent(it -> span.tag("dgraph.channel", it.toString()));
                span.start();
            }
        } catch (Exception e) {
            exportFailMessage(span, method, e);
        }
        return true;
    }

    @Override
    public boolean postMutationHandle(DgraphProto.Response response, Transaction txn, DgraphProto.Request request) {
        this.graceCloseSpan();
        return true;
    }

    @Override
    public boolean preQueryHandle(Transaction txn, String sql) {
        Span span = ThreadLocalSpan.CURRENT_TRACER.next();
        try {
            if (null != span && !span.isNoop()) {
                span.kind(Span.Kind.CLIENT).name("query: " + this.getSqlName(sql));
                span.tag("dgraph.query", sql);
                this.getChannel(txn)
                        .ifPresent(it -> span.tag("dgraph.channel", it.toString()));
                span.start();
            }
        } catch (Exception e) {
            exportFailMessage(span, this.getSqlName(sql), e);
        }
        return true;
    }

    @Override
    public boolean postQueryHandle(DgraphProto.Response response, Transaction txn, String sql) {
        this.graceCloseSpan();
        return true;
    }

    private void graceCloseSpan() {
        Span span = ThreadLocalSpan.CURRENT_TRACER.remove();
        if (null != span) {
            span.finish();
        }
    }

    private String getSqlName(String sql) {
        int startIndex = sql.indexOf('(');
        if (startIndex > -1) {
            String fragment = sql.substring(0, startIndex);
            int spaceIndex = fragment.lastIndexOf(' ');
            if (spaceIndex > -1) {
                return fragment.substring(spaceIndex).trim();
            }
        }
        return sql;
    }

    /**
     * 通过反射获取channel，可以追踪每个请求访问的stub client
     * Dgraph4j 未提供相关方法
     */
    private Optional<Channel> getChannel(Transaction txn) {
        try {
            Field asyncTransactionField = txn.getClass().getDeclaredField("asyncTransaction");
            asyncTransactionField.setAccessible(true);
            AsyncTransaction asyncTransaction = (AsyncTransaction) ReflectionUtils.getField(asyncTransactionField, txn);
            Field stubField = Objects.requireNonNull(asyncTransaction).getClass().getDeclaredField("stub");
            stubField.setAccessible(true);
            DgraphGrpc.DgraphStub stub = (DgraphGrpc.DgraphStub) ReflectionUtils.getField(stubField, asyncTransaction);
            return Optional.ofNullable(stub)
                    .map(AbstractStub::getChannel)
                    ;
        } catch (Exception e) {
            log.warn("reflection channel fail! txn:{}", txn, e);
            return Optional.empty();
        }
    }

    private boolean isNormalSpan(Span span) {
        return null != span && !span.isNoop();
    }

    /**
     * 上报span异常信息
     * 不影响业务
     */
    private void exportFailMessage(Span span, String method, Exception e) {
        if (isNormalSpan(span)) {
            try {
                span.tag("error", e.getMessage());
            } catch (Exception e2) {
                log.warn("request error span message fail! method:{}", method, e2);
            }
        }
    }
}
