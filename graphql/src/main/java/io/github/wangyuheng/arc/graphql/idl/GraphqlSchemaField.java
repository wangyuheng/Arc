package io.github.wangyuheng.arc.graphql.idl;

import io.github.wangyuheng.arc.core.dictionary.GraphqlFieldTypeEnum;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * graphql schema 字段描述
 * <p>
 * 补充 {@link graphql.language.FieldDefinition}
 * 增加 type、enum类型区分 {@link GraphqlFieldTypeEnum#ENUM} {@link GraphqlFieldTypeEnum#TYPE}
 *
 * @author yuheng.wang
 */
public class GraphqlSchemaField {

    private final String name;
    private final GraphqlFieldTypeEnum type;
    private final boolean listType;

    public GraphqlSchemaField(@NonNull String name, @NonNull GraphqlFieldTypeEnum type, boolean listType) {
        Assert.notNull(name, "name must be not null!");
        Assert.notNull(type, "type must be not null!");
        this.name = name;
        this.type = type;
        this.listType = listType;
    }

    public String getName() {
        return name;
    }

    public GraphqlFieldTypeEnum getType() {
        return type;
    }

    public boolean isListType() {
        return listType;
    }
}