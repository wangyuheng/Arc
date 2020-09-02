package ai.care.arc.generator.convert;

import ai.care.arc.core.dictionary.GraphqlFieldTypeEnum;
import ai.care.arc.graphql.idl.GraphqlSchemaField;
import ai.care.arc.graphql.idl.GraphqlSchemaType;
import ai.care.arc.graphql.util.GraphqlTypeUtils;
import graphql.language.FieldDefinition;
import graphql.language.ListType;
import graphql.language.ObjectTypeDefinition;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.idl.TypeInfo;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link TypeDefinitionRegistry} convert to {@link GraphqlSchemaType}
 * {@link FieldDefinition} convert to {@link GraphqlSchemaField}
 * 忽略 {@link graphql.language.OperationTypeDefinition} 类型
 *
 * @author yuheng.wang
 */
public class TypeDefinitionRegistry2GraphqlSchemaTypes implements Function<TypeDefinitionRegistry, Stream<GraphqlSchemaType>> {

    private BiFunction<TypeDefinitionRegistry, FieldDefinition, GraphqlSchemaField> fieldDefinition2GraphqlSchemaField = (typeDefinitionRegistry, fieldDefinition) -> GraphqlSchemaField.builder()
            .name(fieldDefinition.getName())
            .listType(fieldDefinition.getType() instanceof ListType)
            .type(GraphqlFieldTypeEnum.parse(TypeInfo.typeInfo(fieldDefinition.getType()).getName())
                    .orElseGet(() -> {
                        if (typeDefinitionRegistry.isObjectType(fieldDefinition.getType())) {
                            return GraphqlFieldTypeEnum.TYPE;
                        } else if (GraphqlTypeUtils.isEnumType(typeDefinitionRegistry, fieldDefinition.getType())) {
                            return GraphqlFieldTypeEnum.ENUM;
                        } else {
                            return GraphqlFieldTypeEnum.STRING;
                        }
                    }))
            .build();

    @Override
    public Stream<GraphqlSchemaType> apply(TypeDefinitionRegistry typeDefinitionRegistry) {
        final Predicate<ObjectTypeDefinition> isOperator = new IsOperator(typeDefinitionRegistry);
        return typeDefinitionRegistry.getTypes(ObjectTypeDefinition.class).stream()
                .filter(isOperator.negate())
                .map(typeDefinition -> GraphqlSchemaType.builder()
                        .name(typeDefinition.getName())
                        .fieldList(typeDefinition.getFieldDefinitions().stream()
                                .map(fieldDefinition -> fieldDefinition2GraphqlSchemaField.apply(typeDefinitionRegistry, fieldDefinition))
                                .collect(Collectors.toList()))
                        .build());
    }
}
