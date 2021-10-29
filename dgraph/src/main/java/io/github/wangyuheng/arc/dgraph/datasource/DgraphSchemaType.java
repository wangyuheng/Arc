package io.github.wangyuheng.arc.dgraph.datasource;

import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

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

    private final String name;
    private final List<DgraphSchemaPredicate> predicateList;

    public DgraphSchemaType(@NonNull String name, List<DgraphSchemaPredicate> predicateList) {
        Assert.notNull(name, "name must be not null!");
        this.name = name;
        this.predicateList = Collections.unmodifiableList(predicateList);;
    }

    /**
     * 生成dgraph schema 执行语句
     * 只需要predicate name 不校验类型等信息，不包含通用predicate
     */
    public List<String> buildDgraphSchemaLines() {
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

    public String getName() {
        return name;
    }

    public List<DgraphSchemaPredicate> getPredicateList() {
        return predicateList;
    }

}
