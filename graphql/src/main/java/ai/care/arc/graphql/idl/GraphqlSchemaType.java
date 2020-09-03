package ai.care.arc.graphql.idl;

import java.util.List;

/**
 * graphql schema type描述
 * <p>
 * 补充 {@link graphql.language.TypeDefinition}
 *
 * @author yuheng.wang
 */
public class GraphqlSchemaType {

    public GraphqlSchemaType(String name, List<GraphqlSchemaField> fieldList) {
        this.name = name;
        this.fieldList = fieldList;
    }

    private String name;
    private List<GraphqlSchemaField> fieldList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GraphqlSchemaField> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<GraphqlSchemaField> fieldList) {
        this.fieldList = fieldList;
    }
}
