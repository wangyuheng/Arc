package com.github.yituhealthcare.arc.dgraph.util;

import com.github.yituhealthcare.arc.dgraph.annotation.DgraphType;
import com.github.yituhealthcare.arc.dgraph.annotation.RelationshipField;
import com.github.yituhealthcare.arc.dgraph.annotation.UidField;
import org.reflections.ReflectionUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

public class DgraphTypeUtil {

    private DgraphTypeUtil() {
    }

    private static final String DEFAULT_UID_FIELD = "uid";
    private static final String DEFAULT_PREFIX_DELIMITER = ".";

    public static boolean isDgraphType(Class<?> clazz) {
        if (clazz == null) {
            return false;
        } else {
            return clazz.isAnnotationPresent(DgraphType.class);
        }
    }

    public static String getDgraphTypeValue(Class<?> clazz) {
        if (CollectionUtils.isEmpty(ReflectionUtils.getAllSuperTypes(clazz, c -> c != null && c.isAnnotationPresent(DgraphType.class)))) {
            return null;
        }
        return Optional.ofNullable(clazz.getAnnotation(DgraphType.class))
                .map(DgraphType::value)
                .filter(it -> !StringUtils.isEmpty(it))
                .orElse(clazz.getSimpleName());
    }

    public static String getPrefix(Class<?> clazz) {
        return Optional.ofNullable(getDgraphTypeValue(clazz))
                .map(it -> it + DEFAULT_PREFIX_DELIMITER)
                .orElse(null)
                ;
    }

    public static String getFieldWrapper(Class<?> clazz, String fieldName) {
        if (isWrapper(clazz, fieldName)) {
            return fieldName;
        } else {
            return Optional.ofNullable(getPrefix(clazz))
                    .map(it -> it + fieldName)
                    .orElse(null);
        }
    }

    public static boolean isWrapper(Class<?> clazz, String fieldName) {
        return fieldName.startsWith(getPrefix(clazz));
    }

    public static boolean isUidField(Class<?> clazz, String fieldName) {
        if (Objects.equals(fieldName, DEFAULT_UID_FIELD)) {
            return true;
        }
        return Optional.ofNullable(clazz)
                .map(it -> ReflectionUtils.getAllFields(it, f -> f != null && Objects.equals(f.getName(), fieldName))
                        .stream()
                        .anyMatch(f -> f.isAnnotationPresent(UidField.class))
                )
                .orElse(false);
    }

    public static boolean isRelationshipField(Class<?> clazz, String fieldName) {
        return Optional.ofNullable(clazz)
                .map(it -> ReflectionUtils.getAllFields(it, f -> f != null && Objects.equals(f.getName(), fieldName))
                        .stream()
                        .anyMatch(f -> f.isAnnotationPresent(RelationshipField.class))
                )
                .orElse(false);
    }

    public static String getRelationshipField(Class<?> clazz, String fieldName) {
        return Optional.ofNullable(clazz)
                .map(it -> {
                    try {
                        return clazz.getDeclaredField(fieldName);
                    } catch (NoSuchFieldException e) {
                        return null;
                    }
                })
                .map(f -> f.getAnnotation(RelationshipField.class))
                .map(RelationshipField::value)
                .filter(v -> !StringUtils.isEmpty(v))
                .orElse(fieldName);
    }

    public static String getUidFieldSimpleName(Class<?> clazz) {
        return ReflectionUtils.getAllFields(clazz, f -> f != null && f.isAnnotationPresent(UidField.class))
                .stream()
                .map(Field::getName)
                .findAny()
                .orElse(DEFAULT_UID_FIELD);
    }

    public static String getUidFieldName(Class<?> clazz) {
        return getDgraphTypeValue(clazz) + DEFAULT_PREFIX_DELIMITER + getUidFieldSimpleName(clazz);
    }

    public static String getUidFieldDbWrapper(Class<?> clazz) {
        return "_:" + getUidFieldName(clazz);
    }

    public static String getUidFieldDbWrapper(String uidValue) {
        return "_:" + uidValue;
    }

}
