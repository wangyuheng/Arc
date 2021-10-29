package io.github.wangyuheng.arc.dgraph.datasource;

import io.github.wangyuheng.arc.core.dictionary.DgraphPredicateTypeEnum;
import io.github.wangyuheng.arc.dgraph.util.RDFUtil;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.util.Arrays;

/**
 * 描述 Dgraph DB predicate
 *
 * @author yuheng.wang
 */
public class DgraphSchemaPredicate {

    public DgraphSchemaPredicate(@NonNull String name, @NonNull DgraphPredicateTypeEnum predicateType) {
        this(name, predicateType, false);
    }

    public DgraphSchemaPredicate(@NonNull String name, @NonNull DgraphPredicateTypeEnum predicateType, boolean isList) {
        Assert.notNull(name, "name must be not null!");
        Assert.notNull(predicateType, "predicateType must be not null!");
        this.name = name;
        this.predicateType = predicateType;
        this.isList = isList;
    }

    private String name;
    private DgraphPredicateTypeEnum predicateType;
    boolean isList;

    /**
     * 转换RDF格式，用于sql执行
     */
    public String buildRdf() {
        String type = isList ? "[" + predicateType.getKey() + "]" : predicateType.getKey();
        return String.join(" ", Arrays.asList(RDFUtil.wrapper(name) + ":", type, "."));
    }

    public String getName() {
        return name;
    }

    public DgraphPredicateTypeEnum getPredicateType() {
        return predicateType;
    }

    public boolean isList() {
        return isList;
    }

}
