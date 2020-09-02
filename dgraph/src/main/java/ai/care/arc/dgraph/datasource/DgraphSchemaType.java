package ai.care.arc.dgraph.datasource;

import ai.care.arc.core.dictionary.DgraphPredicateTypeEnum;
import ai.care.arc.core.util.DomainClassUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述 Dgraph DB type
 *
 * @author yuheng.wang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DgraphSchemaType {

    private String name;
    private List<DgraphSchemaPredicate> predicateList;

    public static final List<DgraphSchemaPredicate> GENERAL_DGRAPH_PREDICATE_LIST = Collections.unmodifiableList(
            Arrays.asList(
                    new DgraphSchemaPredicate(DomainClassUtil.DOMAIN_CLASS_KEY, DgraphPredicateTypeEnum.STRING),
                    new DgraphSchemaPredicate("dgraph.graphql.schema", DgraphPredicateTypeEnum.STRING)
            )
    );

    /**
     * 生成dgraph schema 执行语句
     * 只需要predicate name 不校验类型等信息
     */
    public List<String> buildDgraphSchemaLines() {
        if (null == name) {
            return Collections.emptyList();
        } else {
            List<DgraphSchemaPredicate> allPredicateList = new ArrayList<>(GENERAL_DGRAPH_PREDICATE_LIST);
            if (null != predicateList) {
                allPredicateList.addAll(predicateList);
            }

            List<String> ddl = new ArrayList<>();
            ddl.add("type " + name + " {");
            ddl.addAll(allPredicateList.stream().map(DgraphSchemaPredicate::getName).collect(Collectors.toList()));
            ddl.add("}");
            return ddl;
        }

    }

}
