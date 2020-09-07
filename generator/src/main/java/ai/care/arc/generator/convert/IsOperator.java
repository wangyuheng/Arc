package ai.care.arc.generator.convert;

import ai.care.arc.graphql.util.GraphqlTypeUtils;
import graphql.language.ObjectTypeDefinition;
import graphql.schema.idl.TypeDefinitionRegistry;

import java.util.function.Predicate;

/**
 * 判断 {@link ObjectTypeDefinition} 是否为 {@link graphql.language.OperationTypeDefinition}
 *
 * @author yuheng.wang
 */
public class IsOperator implements Predicate<ObjectTypeDefinition> {

    private final TypeDefinitionRegistry typeDefinitionRegistry;

    public IsOperator(TypeDefinitionRegistry typeDefinitionRegistry) {
        this.typeDefinitionRegistry = typeDefinitionRegistry;
    }

    @Override
    public boolean test(ObjectTypeDefinition typeDefinition) {
        return GraphqlTypeUtils.getOperationTypeNames(typeDefinitionRegistry).contains(typeDefinition.getName());
    }
}
