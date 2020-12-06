package com.github.yituhealthcare.arc.graphql.idl;

import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.util.List;

/**
 * graphql schema type描述
 * <p>
 * 补充 {@link graphql.language.TypeDefinition}
 *
 * @author yuheng.wang
 */
public class GraphqlSchemaType {

    private final String name;
    private final List<GraphqlSchemaField> fieldList;

    public GraphqlSchemaType(@NonNull String name, List<GraphqlSchemaField> fieldList) {
        Assert.notNull(name, "name must be not null!");
        this.name = name;
        this.fieldList = fieldList;
    }

    public String getName() {
        return name;
    }

    public List<GraphqlSchemaField> getFieldList() {
        return fieldList;
    }

}
