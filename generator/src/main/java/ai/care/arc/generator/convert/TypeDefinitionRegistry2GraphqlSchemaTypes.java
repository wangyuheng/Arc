package ai.care.arc.generator.convert;

import ai.care.arc.core.dictionary.GraphqlFieldTypeEnum;
import ai.care.arc.graphql.idl.GraphqlSchemaField;
import ai.care.arc.graphql.idl.GraphqlSchemaType;
import ai.care.arc.graphql.util.GraphqlTypeUtils;
import graphql.language.FieldDefinition;
import graphql.language.ListType;
import graphql.language.ObjectTypeDefinition;
import graphql.language.Type;
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

    private BiFunction<TypeDefinitionRegistry, FieldDefinition, GraphqlSchemaField> fieldDefinition2GraphqlSchemaField = new FieldDefinition2GraphqlSchemaField(new GraphqlFieldType2GraphqlFieldType());

    @Override
    public Stream<GraphqlSchemaType> apply(TypeDefinitionRegistry typeDefinitionRegistry) {
        final Predicate<ObjectTypeDefinition> isOperator = new IsOperator(typeDefinitionRegistry);
        return typeDefinitionRegistry.getTypes(ObjectTypeDefinition.class).stream()
                .filter(isOperator.negate())
                .map(typeDefinition ->
                        new GraphqlSchemaType(typeDefinition.getName(),
                                typeDefinition.getFieldDefinitions().stream()
                                        .map(fieldDefinition -> fieldDefinition2GraphqlSchemaField.apply(typeDefinitionRegistry, fieldDefinition))
                                        .collect(Collectors.toList()))
                );
    }

    static class FieldDefinition2GraphqlSchemaField implements BiFunction<TypeDefinitionRegistry, FieldDefinition, GraphqlSchemaField> {

        private BiFunction<TypeDefinitionRegistry, Type<?>, GraphqlFieldTypeEnum> graphqlFieldType2GraphqlFieldType;

        public FieldDefinition2GraphqlSchemaField(GraphqlFieldType2GraphqlFieldType graphqlFieldType2GraphqlFieldType) {
            this.graphqlFieldType2GraphqlFieldType = graphqlFieldType2GraphqlFieldType;
        }

        @Override
        public GraphqlSchemaField apply(TypeDefinitionRegistry typeDefinitionRegistry, FieldDefinition fieldDefinition) {
            return new GraphqlSchemaField(fieldDefinition.getName(),
                    fieldDefinition.getType() instanceof ListType,
                    graphqlFieldType2GraphqlFieldType.apply(typeDefinitionRegistry, fieldDefinition.getType()));
        }
    }

    static class GraphqlFieldType2GraphqlFieldType implements BiFunction<TypeDefinitionRegistry, Type<?>, GraphqlFieldTypeEnum> {

        @Override
        public GraphqlFieldTypeEnum apply(TypeDefinitionRegistry typeDefinitionRegistry, Type<?> type) {
            return GraphqlFieldTypeEnum.parse(TypeInfo.typeInfo(type).getName())
                    .orElseGet(() -> {
                        if (typeDefinitionRegistry.isObjectType(type)) {
                            return GraphqlFieldTypeEnum.TYPE;
                        } else if (GraphqlTypeUtils.isEnumType(typeDefinitionRegistry, type)) {
                            return GraphqlFieldTypeEnum.ENUM;
                        } else {
                            return GraphqlFieldTypeEnum.STRING;
                        }
                    });
        }
    }
}
