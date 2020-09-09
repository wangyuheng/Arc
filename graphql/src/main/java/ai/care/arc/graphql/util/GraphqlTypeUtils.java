package ai.care.arc.graphql.util;

import graphql.language.EnumTypeDefinition;
import graphql.language.OperationTypeDefinition;
import graphql.language.Type;
import graphql.language.TypeName;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.idl.TypeInfo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * GraphqlType增强工具类
 * <p>
 * 扩展 {@link TypeDefinitionRegistry} {@link TypeInfo} 的功能
 */
public class GraphqlTypeUtils {

    public static List<String> getOperationTypeNames(TypeDefinitionRegistry typeRegistry) {
        return typeRegistry.schemaDefinition()
                .map(schemaDefinition -> schemaDefinition.getOperationTypeDefinitions().stream()
                        .map(OperationTypeDefinition::getTypeName)
                        .map(TypeName::getName)
                        .collect(Collectors.toList())
                ).orElse(Collections.emptyList());
    }

    public static boolean isEnumType(TypeDefinitionRegistry typeRegistry, Type<?> type) {
        return typeRegistry.getTypes(EnumTypeDefinition.class).stream()
                .map(EnumTypeDefinition::getName)
                .anyMatch(name -> name.equalsIgnoreCase(TypeInfo.typeInfo(type).getName()));
    }

}
