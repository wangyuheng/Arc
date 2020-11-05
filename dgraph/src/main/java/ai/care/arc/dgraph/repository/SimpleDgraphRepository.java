package ai.care.arc.dgraph.repository;

import ai.care.arc.core.pageable.Filter;
import ai.care.arc.core.pageable.Order;
import ai.care.arc.core.pageable.Pagination;
import ai.care.arc.core.pageable.QueryBody;
import ai.care.arc.core.util.BeanUtil;
import ai.care.arc.dgraph.repository.mapper.DgraphMapperManager;
import ai.care.arc.dgraph.repository.parser.DgraphParser;
import ai.care.arc.dgraph.util.DgraphSQLHelper;
import ai.care.arc.dgraph.util.UidUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ByteString;
import io.dgraph.DgraphProto;
import io.dgraph.TxnConflictException;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class SimpleDgraphRepository<T> implements DgraphRepository<T> {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(SimpleDgraphRepository.class);
    @Autowired
    protected DgraphMapperManager dgraphMapperManager;
    @Autowired
    protected DgraphClientAdapter dgraphClientAdapter;

    protected static final String VAR_PREFIX = "$";

    protected ResolvableType domainType;
    protected DgraphTypeInformation typeInformation;
    protected DgraphParser dgraphParser;

    public SimpleDgraphRepository() {
        this.domainType = ResolvableType.forClass(this.getClass()).getSuperType().getGenerics()[0];
        this.typeInformation = new DgraphTypeInformation(this.domainType.getRawClass());
        this.dgraphParser = new DgraphParser(this.domainType.getRawClass());
    }

    @Override
    public void createRelationship(RelationshipInformation information) {
        String rdf = information.toRdf();
        DgraphProto.Response response = null;
        DgraphProto.Mutation mutation = DgraphProto.Mutation.newBuilder()
                .setSetNquads(ByteString.copyFromUtf8(rdf))
                .setCommitNow(true)
                .build();
        try {
            response = dgraphClientAdapter.mutation(mutation, information.getRelationship());
            log.info("repository createRelationship information:{}, response:{} ", information, response);
        } catch (TxnConflictException | IllegalArgumentException | StatusRuntimeException ex) {
            log.info("repository createRelationship error! information:{} response:{}", information, response, ex);
            throw ex;
        }
    }

    /**
     * 1. 如果返回多个uid 如何处理？
     * TODO 封装返回值并set field？
     *
     * @param entity
     * @param <S>
     * @return
     */
    @Override
    public <S extends T> String save(S entity) {
        JSONObject json = dgraphParser.parseDecoratorJSON(entity);
        log.info("save json:{}", json);
        DgraphProto.Response response = null;
        DgraphProto.Mutation mutation = DgraphProto.Mutation.newBuilder()
                .setSetJson(ByteString.copyFromUtf8(json.toJSONString()))
                .setCommitNow(true)
                .build();
        try {
            response = dgraphClientAdapter.mutation(mutation, entity.getClass().getSimpleName());
            if (UidUtil.isUid(json.getString("uid"))) {
                return json.getString("uid");
            } else {
                return response.getUidsOrThrow(typeInformation.getUidFieldName());
            }
        } catch (TxnConflictException | IllegalArgumentException | StatusRuntimeException ex) {
            log.info("repository save error! domain:{} entity:{} response:{}", typeInformation.getDomainClass(), entity, Optional.ofNullable(response).map(DgraphProto.Response::getJson).map(ByteString::toStringUtf8), ex);
            throw ex;
        }
    }

    @Override
    public <S extends T> S saveAndReturn(S entity) {
        JSONObject json = dgraphParser.parseDecoratorJSON(entity);
        log.info("save json:{}", json);
        DgraphProto.Response response = null;
        DgraphProto.Mutation mutation = DgraphProto.Mutation.newBuilder()
                .setSetJson(ByteString.copyFromUtf8(json.toJSONString()))
                .setCommitNow(true)
                .build();
        try {
            response = dgraphClientAdapter.mutation(mutation, entity.getClass().getSimpleName());
            String uid;
            if (UidUtil.isUid(json.getString("uid"))) {
                uid = json.getString("uid");
            } else {
                uid = response.getUidsOrThrow(typeInformation.getUidFieldName());
            }
            return (S) getOne(uid).orElseThrow(() -> new RuntimeException("repository save error!"));
        } catch (TxnConflictException | IllegalArgumentException | StatusRuntimeException ex) {
            log.info("repository save error! domain:{} entity:{} response:{}", typeInformation.getDomainClass(), entity, Optional.ofNullable(response).map(DgraphProto.Response::getJson).map(ByteString::toStringUtf8), ex);
            throw ex;
        }
    }

    @Override
    public void mutation(String path, Map<String, String> vars) {
        String sql = dgraphMapperManager.getSql(path);
        for (Map.Entry<String, String> entry : vars.entrySet()) {
            sql = sql.replaceAll("\\$" + entry.getKey(), entry.getValue());
        }
        String method = path.split("\\.")[1];
        log.info("execute mutation sql:{}, vars:{}, method:{}", sql, vars, method);

        DgraphProto.Response response = null;
        DgraphProto.Mutation mutation = DgraphProto.Mutation.newBuilder()
                .setSetNquads(ByteString.copyFromUtf8(sql))
                .setCommitNow(true)
                .build();
        try {
            response = dgraphClientAdapter.mutation(mutation, path);
            log.info("mutation path:{} response:{}", path, response.getJson());
        } catch (TxnConflictException | IllegalArgumentException | StatusRuntimeException ex) {
            log.info("repository update error! sql:{}, vars:{}, method:{} response:{}", sql, vars, method, Optional.ofNullable(response).map(DgraphProto.Response::getJson).map(ByteString::toStringUtf8), ex);
            throw ex;
        }
    }

    @Override
    public <S extends T> void upsert(S entity, String path, Map<String, String> vars) {
        String sql = dgraphMapperManager.getSql(path);
        for (Map.Entry<String, String> entry : vars.entrySet()) {
            sql = sql.replaceAll("\\$" + entry.getKey(), entry.getValue());
        }
        String tmp = sql.split("(?i) as uid")[0];
        String uidVar = tmp.substring(tmp.lastIndexOf(' ')).trim();

        JSONObject json = dgraphParser.parseDecoratorJSON(entity);
        json.put("uid", "uid(" + uidVar + ")");

        log.info("upsert sql:{} json:{}", sql, json);
        DgraphProto.Response response = null;
        DgraphProto.Mutation mutation = DgraphProto.Mutation.newBuilder()
                .setSetJson(ByteString.copyFromUtf8(json.toJSONString()))
                .setCommitNow(true)
                .build();
        try {
            response = dgraphClientAdapter.upsert(mutation, sql);
            log.info("mutation path:{} response:{}", path, response.getJson());
        } catch (TxnConflictException | IllegalArgumentException | StatusRuntimeException ex) {
            log.info("repository upsert error! sql:{}, vars:{}, response:{}", sql, vars, Optional.ofNullable(response).map(DgraphProto.Response::getJson).map(ByteString::toStringUtf8), ex);
            throw ex;
        }
    }

    @Override
    public void deleteRelationship(String uid, String linkName, String linkUid) {
        linkName = typeInformation.getTypeValue() + "." + linkName;
        JSONObject deleteJson = new JSONObject();
        deleteJson.put("uid", uid);
        JSONObject linkJson = new JSONObject();
        linkJson.put("uid", linkUid);
        deleteJson.put(linkName, linkJson);

        DgraphProto.Response response = null;
        DgraphProto.Mutation mutation = DgraphProto.Mutation.newBuilder()
                .setDeleteJson(ByteString.copyFromUtf8(deleteJson.toJSONString()))
                .setCommitNow(true)
                .build();
        try {
            response = dgraphClientAdapter.mutation(mutation, "delete");
            log.info("mutation path:{} response:{}", "delete", response.getJson());
        } catch (TxnConflictException | IllegalArgumentException | StatusRuntimeException ex) {
            log.info("repository update error! sql:{}, method:{} response:{}", deleteJson.toJSONString(), "delete", Optional.ofNullable(response).map(DgraphProto.Response::getJson).map(ByteString::toStringUtf8), ex);
            throw ex;
        }
    }


    /**
     * 嵌套 type 未解决
     * 查询结果mapping规则未解决
     */
    @Deprecated
    public <S extends T> List<S> queryAll(String... vars) {
        String type = typeInformation.getTypeValue();
        String varStr = dgraphParser.parseDecoratorArgs(vars);

        String sql = "query {\n" +
                "listAll(func: type(" + type + ")) {\n" +
                "uid\n" +
                varStr +
                "}\n" +
                "}\n";

        log.info("execute sql for list all. sql:{}", sql);

        DgraphProto.Response response = dgraphClientAdapter.query(sql, null);
        JSONObject jsonResult = JSON.parseObject(response.getJson().toStringUtf8());
        try {
            return dgraphParser.extractJSONArray(jsonResult.getJSONArray("listAll"));
        } catch (ClassCastException e) {
            log.error("execute getOne query error! jsonResult:{}", jsonResult, e);
        }
        return Collections.emptyList();
    }

    /**
     * 嵌套 type 未解决
     */
    public <S extends T> Optional<S> getOne(String uid, String... vars) {
        Class<T> tClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        String sql = "query {\n" +
                "getOne(func: uid(" + uid + ")) @filter( type(" + typeInformation.getTypeValue() + "))" + DgraphSQLHelper.getVar(Collections.singletonList(tClass), new ArrayList<>(), true) +
                "}\n";

        log.info("execute sql for get one. sql:{}", sql);

        DgraphProto.Response response = dgraphClientAdapter.query(sql, null);
        JSONObject jsonResult = JSON.parseObject(response.getJson().toStringUtf8());
        try {
            JSONArray array = jsonResult.getJSONArray("getOne");
            if (array.isEmpty()) {
                return Optional.empty();
            } else {
                return dgraphParser.extractJSON((JSONObject) array.get(0));
            }
        } catch (ClassCastException e) {
            log.error("execute getOne query error! jsonResult:{}", jsonResult, e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<T> queryForObject(String path, Map<String, String> vars) {
        List<T> list = this.queryForList(path, vars);
        if (CollectionUtils.isEmpty(list)) {
            return Optional.empty();
        } else {
            return Optional.of(list.get(0));
        }
    }

    @Override
    public List<T> queryForList(String path, Map<String, String> vars) {
        String method = path.split("\\.")[1];

        String sql = dgraphMapperManager.getSql(path);
        vars = this.fillVarPrefix(vars);
        log.info("execute query sql:{}, vars:{}, method:{}", sql, vars, method);
        DgraphProto.Response response = dgraphClientAdapter.query(sql, vars);
        JSONObject jsonResult = JSON.parseObject(response.getJson().toStringUtf8());
        try {
            return dgraphParser.extractJSONArray(jsonResult.getJSONArray(method));
        } catch (ClassCastException e) {
            log.error("execute query error! method:{}, jsonResult:{}", method, jsonResult, e);
        }
        return Collections.emptyList();
    }

    @Override
    public List<T> queryForList(String path) {
        return this.queryForList(path, null);
    }

    /**
     * 补全变量$前缀
     */
    protected Map<String, String> fillVarPrefix(Map<String, String> vars) {
        if (null == vars) {
            return null;
        } else {
            Map<String, String> newVars = new HashMap<>();
            vars.forEach((k, v) -> {
                if (k.startsWith(VAR_PREFIX)) {
                    newVars.put(k, v);
                } else {
                    newVars.put(VAR_PREFIX + k, v);
                }
            });
            return newVars;
        }
    }

    @Override
    public Integer count(QueryBody queryBody) {
        queryBody = preHandle(queryBody);
        Map<String, String> vars = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("{ countAll");
        sql.append(Stream.of("func: type(" + typeInformation.getTypeValue() + ")")
                .filter(it -> !StringUtils.isEmpty(it))
                .collect(Collectors.joining(",", "(", ")")));
        if (needFilter(queryBody)) {
            sql.append(generateDGraphFilter(queryBody));
        }
        sql.append("{\n count(uid)\n }}");
        log.info(sql.toString());
        DgraphProto.Response response = dgraphClientAdapter.query(sql.toString(), vars);
        JSONObject jsonResult = JSON.parseObject(response.getJson().toStringUtf8());
        return jsonResult.getJSONArray("countAll").getJSONObject(0).getInteger("count");
    }

    @Override
    public List<T> listAll(QueryBody queryBody) {
        // todo queryBody校验
        queryBody = preHandle(queryBody);
        String first = queryBody.getPagination().getPageSize().toString();
        String offset = Objects.toString((queryBody.getPagination().getPageNum() - 1) * queryBody.getPagination().getPageSize());
        Map<String, String> vars = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("{ listAll");
        sql.append(Stream.of("func: type(" + typeInformation.getTypeValue() + ")",
                "offset:" + offset,
                "first:" + first,
                Optional.of(queryBody)
                        .map(QueryBody::getSorter)
                        .map(it -> mapping2DGraphOrder(it.getOrder()) + ":" + it.getField())
                        .orElse(""))
                .filter(it->!StringUtils.isEmpty(it))
                .collect(Collectors.joining(",", "(", ")")));
        if (needFilter(queryBody)) {
            sql.append(generateDGraphFilter(queryBody));
        }
        sql.append("{\n uid\n expand(")
                .append(typeInformation.getTypeValue())
                .append(")\n }}");
        log.info(sql.toString());
        DgraphProto.Response response = dgraphClientAdapter.query(sql.toString(), vars);
        JSONObject jsonResult = JSON.parseObject(response.getJson().toStringUtf8());
        try {
            return dgraphParser.extractJSONArray(jsonResult.getJSONArray("listAll"));
        } catch (ClassCastException var8) {
            log.error("execute query error! method:{}, jsonResult:{}", new Object[]{"listAll", jsonResult, var8});
            return Collections.emptyList();
        }
    }

    private boolean needFilter(QueryBody queryBody) {
        return !CollectionUtils.isEmpty(queryBody.getFilters()) || !StringUtils.isEmpty(queryBody.getSearchText());
    }

    private String generateDGraphFilter(QueryBody queryBody) {
        Stream<String> filterStream = queryBody.getFilters().stream().map(this::filterHandle);
        Stream<String> searchStream = Stream.of(searchHandle(queryBody.getSearchText(), queryBody.getSearchFields()));
        return "@filter" + Stream.of(filterStream, searchStream).flatMap(stringStream -> stringStream)
                .filter(it -> !StringUtils.isEmpty(it))
                .collect(Collectors.joining(" and ", "(", ")"));
    }

    private String mapping2DGraphOrder(Order order) {
        if (Objects.equals(order, Order.ASC)) {
            return "orderasc";
        } else {
            return "orderdesc";
        }
    }

    private String filterHandle(Filter filter) {
        if (Objects.equals(typeInformation.getUidFieldName(), filter.getField())) {
            return "uid" + filter.getValue().stream().collect(Collectors.joining(",", "(", ")"));
        } else {
            switch (filter.getOperator()) {
                case EQ:
                    return "eq(" + filter.getField() + ",\"" + filter.getValue().get(0) + "\")";
                case IN:
                    return filter.getValue().stream().map(value -> "eq(" + filter.getField() + ",\"" + value + "\")").collect(Collectors.joining(" or ", "(", ")"));
                default:
                    return "";
            }
        }
    }

    private String searchHandle(String searchText, List<String> searchFields) {
        if (StringUtils.isEmpty(searchText) || CollectionUtils.isEmpty(searchFields)) {
            return "";
        } else {
            return searchFields.stream().map(field -> fieldHandle(searchText, field)).collect(Collectors.joining(" or ", "(", ")"));
        }
    }

    private String fieldHandle(String searchText, String field) {
        if (Objects.equals(typeInformation.getUidFieldName(), field) && searchText.startsWith("0x")) {
            return "uid(" + searchText + ")";
        } else {
            return "regexp(" + field + ",/" + searchText + "/)";
        }
    }

    private QueryBody preHandle(QueryBody queryBody) {
        queryBody = BeanUtil.deepClone(queryBody, QueryBody.class);
        if (Objects.isNull(queryBody)) {
            return QueryBody.builder().pagination(Pagination.builder().pageNum(1).pageSize(10).build()).build();
        }
        // 添加前缀
        if (!CollectionUtils.isEmpty(queryBody.getFilters())) {
            queryBody.getFilters().forEach(filter -> filter.setField(typeInformation.getTypeValue() + "." + filter.getField()));
        }
        if (!CollectionUtils.isEmpty(queryBody.getSearchFields())) {
            queryBody.setSearchFields(queryBody.getSearchFields().stream().map(field -> typeInformation.getTypeValue() + "." + field).collect(Collectors.toList()));
        }
        //排序处理
        if (Objects.nonNull(queryBody.getSorter()) && !StringUtils.isEmpty(queryBody.getSorter().getField())) {
            queryBody.getSorter().setField(typeInformation.getTypeValue() + "." + queryBody.getSorter().getField());
            if (Objects.equals(typeInformation.getUidFieldName(), queryBody.getSorter().getField())) {
                queryBody.getSorter().setField(typeInformation.getTypeValue() + "." + "createdTime");
            }
        }
        if (Objects.isNull(queryBody.getPagination())) {
            queryBody.setPagination(Pagination.builder().pageNum(1).pageSize(10).build());
        }
        return queryBody;
    }

}
