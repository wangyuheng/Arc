package ai.care.arc.graphql.idl;

import ai.care.arc.core.dictionary.GraphqlFieldTypeEnum;

/**
 * graphql schema 字段描述
 * <p>
 * 补充 {@link graphql.language.FieldDefinition}
 * 增加 type、enum类型区分 {@link GraphqlFieldTypeEnum#ENUM} {@link GraphqlFieldTypeEnum#TYPE}
 *
 * @author yuheng.wang
 */
public class GraphqlSchemaField {

    public GraphqlSchemaField(String name, boolean listType, GraphqlFieldTypeEnum type) {
        this.name = name;
        this.listType = listType;
        this.type = type;
    }

    private String name;
    private boolean listType;
    private GraphqlFieldTypeEnum type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isListType() {
        return listType;
    }

    public void setListType(boolean listType) {
        this.listType = listType;
    }

    public GraphqlFieldTypeEnum getType() {
        return type;
    }

    public void setType(GraphqlFieldTypeEnum type) {
        this.type = type;
    }
}