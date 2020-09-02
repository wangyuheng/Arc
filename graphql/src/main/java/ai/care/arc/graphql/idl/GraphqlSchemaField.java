package ai.care.arc.graphql.idl;

import ai.care.arc.core.dictionary.GraphqlFieldTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * graphql schema 字段描述
 * <p>
 * 补充 {@link graphql.language.FieldDefinition}
 * 增加 type、enum类型区分 {@link GraphqlFieldTypeEnum#ENUM} {@link GraphqlFieldTypeEnum#TYPE}
 *
 * @author yuheng.wang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GraphqlSchemaField {

    private String name;
    private boolean listType;
    private GraphqlFieldTypeEnum type;

}