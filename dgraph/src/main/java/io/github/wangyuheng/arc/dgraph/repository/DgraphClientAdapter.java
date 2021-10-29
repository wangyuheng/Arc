package io.github.wangyuheng.arc.dgraph.repository;

import io.dgraph.DgraphClient;
import io.dgraph.DgraphProto;
import io.dgraph.Transaction;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class DgraphClientAdapter {

    private final DgraphClient dgraphClient;
    private final List<DgraphInterceptor> interceptors;

    public DgraphClientAdapter(DgraphClient dgraphClient, List<DgraphInterceptor> interceptors) {
        this.dgraphClient = dgraphClient;
        this.interceptors = interceptors;
    }

    public DgraphProto.Response query(String ql, Map<String, String> vars) {
        try (Transaction txn = dgraphClient.newReadOnlyTransaction()) {
            this.getInterceptors().forEach(i -> i.preQueryHandle(txn, ql));
            DgraphProto.Response response;
            if (null == vars) {
                response = txn.query(ql);
            } else {
                response = txn.queryWithVars(ql, vars);
            }
            this.getInterceptors().forEach(i -> i.postQueryHandle(response, txn, ql));
            return response;
        }
    }

    public DgraphProto.Response mutation(DgraphProto.Mutation mutation, String method) {
        try (Transaction txn = dgraphClient.newTransaction()) {
            DgraphProto.Request request = DgraphProto.Request.newBuilder()
                    .addMutations(mutation)
                    .setCommitNow(true)
                    .build();
            this.getInterceptors().forEach(i -> i.preMutationHandle(method, txn, request));
            DgraphProto.Response response = txn.doRequest(request);
            this.getInterceptors().forEach(i -> i.postMutationHandle(response, txn, request));
            return response;
        }
    }

    public DgraphProto.Response upsert(DgraphProto.Mutation mutation, String query) {
        try (Transaction txn = dgraphClient.newTransaction()) {
            DgraphProto.Request request = DgraphProto.Request.newBuilder()
                    .setQuery(query)
                    .addMutations(mutation)
                    .setCommitNow(true)
                    .build();
            this.getInterceptors().forEach(i -> i.preMutationHandle("upsert", txn, request));
            DgraphProto.Response response = txn.doRequest(request);
            this.getInterceptors().forEach(i -> i.postMutationHandle(response, txn, request));
            return response;
        }
    }

    private Stream<DgraphInterceptor> getInterceptors() {
        if (null == interceptors) {
            return Stream.empty();
        } else {
            return interceptors.stream();
        }
    }
}
