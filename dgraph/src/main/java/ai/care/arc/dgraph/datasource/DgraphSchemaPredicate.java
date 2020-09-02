package ai.care.arc.dgraph.datasource;

import ai.care.arc.core.dictionary.DgraphPredicateTypeEnum;
import ai.care.arc.dgraph.util.RDFUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

/**
 * 描述 Dgraph DB predicate
 *
 * @author yuheng.wang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DgraphSchemaPredicate {

    public DgraphSchemaPredicate(String name, DgraphPredicateTypeEnum predicateType) {
        this.name = name;
        this.predicateType = predicateType;
        this.isList = false;
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

}
