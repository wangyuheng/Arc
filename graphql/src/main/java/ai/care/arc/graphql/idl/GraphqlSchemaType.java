package ai.care.arc.graphql.idl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * graphql schema type描述
 * <p>
 * 补充 {@link graphql.language.TypeDefinition}
 *
 * @author yuheng.wang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GraphqlSchemaType {

    private String name;
    private List<GraphqlSchemaField> fieldList;

}
