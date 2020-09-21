package ai.care.arc.generator.convert;

import ai.care.arc.core.dictionary.GraphqlFieldTypeEnum;
import graphql.language.FieldDefinition;
import graphql.schema.idl.TypeInfo;
import org.springframework.util.CollectionUtils;

import java.util.function.Predicate;

/**
 * 判断 {@link FieldDefinition} 是否为 graphql method
 * graphql本身不区分field & method, 比如 users:[User] users(status:Status):[User] 都会被解析为 {@link FieldDefinition}
 * 包含入参一定是method
 * 返回值是另一个type一定是method
 *
 * @author yuheng.wang
 */
public class IsGraphqlMethodField implements Predicate<FieldDefinition> {

    @Override
    public boolean test(FieldDefinition fieldDefinition) {
        return !CollectionUtils.isEmpty(fieldDefinition.getInputValueDefinitions())
                && !GraphqlFieldTypeEnum.exist(TypeInfo.typeInfo(fieldDefinition.getType()).getName());
    }

}
