package ai.care.arc.dgraph.repository;

import io.dgraph.DgraphProto;
import io.dgraph.Transaction;

public interface DgraphInterceptor {

    default boolean preMutationHandle(String method, Transaction txn, DgraphProto.Request request) {
        return true;
    }

    default boolean postMutationHandle(DgraphProto.Response response, Transaction txn, DgraphProto.Request request) {
        return true;
    }

    default boolean preQueryHandle(Transaction txn, String sql) {
        return true;
    }

    default boolean postQueryHandle(DgraphProto.Response response, Transaction txn, String sql) {
        return true;
    }

}
