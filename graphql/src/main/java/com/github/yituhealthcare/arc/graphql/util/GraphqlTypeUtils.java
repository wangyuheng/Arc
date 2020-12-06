package com.github.yituhealthcare.arc.graphql.util;

import graphql.language.*;
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

    private GraphqlTypeUtils() {}

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

    public static boolean isListType(Type<?> type) {
        TypeInfo typeInfo = TypeInfo.typeInfo(type);
        if (typeInfo.isNonNull()) {
            return isListType(((NonNullType) type).getType());
        } else {
            return typeInfo.isList();
        }
    }

    /**
     * 获取list or nonNull 包装内的真实type
     */
    public static Type getUnWrapperType(Type<?> type) {
        if (TypeInfo.typeInfo(type).isList()) {
            return getUnWrapperType(((ListType) type).getType());
        } else if (TypeInfo.typeInfo(type).isNonNull()) {
            return getUnWrapperType(((NonNullType) type).getType());
        } else {
            return type;
        }
    }
}
