package io.github.wangyuheng.arc.generator.convert;

import io.github.wangyuheng.arc.core.dictionary.GraphqlFieldTypeEnum;
import io.github.wangyuheng.arc.generator.dictionary.GeneratorGlobalConst;
import io.github.wangyuheng.arc.graphql.util.GraphqlTypeUtils;
import graphql.language.FieldDefinition;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.idl.TypeInfo;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
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

    private final TypeDefinitionRegistry typeDefinitionRegistry;

    public IsGraphqlMethodField(TypeDefinitionRegistry typeDefinitionRegistry) {
        this.typeDefinitionRegistry = typeDefinitionRegistry;
    }

    @Override
    public boolean test(FieldDefinition fieldDefinition) {
        return hasInput()
                .or(isAction())
                .or(isNotGraphqlFieldType().and(isNotEnum()))
                .test(fieldDefinition);
    }

    private Predicate<FieldDefinition> isAction() {
        return f -> Objects.nonNull(f.getDirective(GeneratorGlobalConst.DIRECTIVE_ACTION));
    }

    private Predicate<FieldDefinition> hasInput() {
        return f -> !CollectionUtils.isEmpty(f.getInputValueDefinitions());
    }

    private Predicate<FieldDefinition> isNotGraphqlFieldType() {
        return f -> !GraphqlFieldTypeEnum.exist(TypeInfo.typeInfo(f.getType()).getName());
    }

    private Predicate<FieldDefinition> isNotEnum() {
        return f -> !GraphqlTypeUtils.isEnumType(typeDefinitionRegistry, f.getType());
    }

}
