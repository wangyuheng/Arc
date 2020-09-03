package ai.care.arc.dgraph.datasource;

import ai.care.arc.core.dictionary.DgraphPredicateTypeEnum;
import ai.care.arc.dgraph.util.RDFUtil;

import java.util.Arrays;

/**
 * 描述 Dgraph DB predicate
 *
 * @author yuheng.wang
 */
public class DgraphSchemaPredicate {

    public DgraphSchemaPredicate() {
        this(null, null, false);
    }

    public DgraphSchemaPredicate(String name, DgraphPredicateTypeEnum predicateType) {
        this(name, predicateType, false);
    }

    public DgraphSchemaPredicate(String name, DgraphPredicateTypeEnum predicateType, boolean isList) {
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
        if (null == predicateType || null == name) {
            return null;
        } else {
            String type = isList ? "[" + predicateType.getKey() + "]" : predicateType.getKey();
            return String.join(" ", Arrays.asList(RDFUtil.wrapper(this.getName()) + ":", type, "."));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DgraphPredicateTypeEnum getPredicateType() {
        return predicateType;
    }

    public void setPredicateType(DgraphPredicateTypeEnum predicateType) {
        this.predicateType = predicateType;
    }

    public boolean isList() {
        return isList;
    }

    public void setList(boolean list) {
        isList = list;
    }
}
