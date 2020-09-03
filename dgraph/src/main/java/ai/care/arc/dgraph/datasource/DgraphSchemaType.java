package ai.care.arc.dgraph.datasource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述 Dgraph DB type
 *
 * @author yuheng.wang
 */
public class DgraphSchemaType {

    private String name;
    private List<DgraphSchemaPredicate> predicateList;

    public DgraphSchemaType() {
    }

    public DgraphSchemaType(String name, List<DgraphSchemaPredicate> predicateList) {
        this.name = name;
        this.predicateList = predicateList;
    }

    /**
     * 生成dgraph schema 执行语句
     * 只需要predicate name 不校验类型等信息，不包含通用predicate
     */
    public List<String> buildDgraphSchemaLines() {
        if (null == name) {
            return Collections.emptyList();
        } else {
            List<DgraphSchemaPredicate> allPredicateList = new ArrayList<>();
            if (null != predicateList) {
                allPredicateList.addAll(predicateList);
            }
            List<String> ddl = new ArrayList<>();
            ddl.add("type " + name + " {");
            ddl.addAll(allPredicateList.stream().map(DgraphSchemaPredicate::getName).sorted().collect(Collectors.toList()));
            ddl.add("}");
            return ddl;
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DgraphSchemaPredicate> getPredicateList() {
        return predicateList;
    }

    public void setPredicateList(List<DgraphSchemaPredicate> predicateList) {
        this.predicateList = predicateList;
    }
}
