package com.github.yituhealthcare.arc.dgraph.util;


import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author junhao.chen
 * @date 2020/6/4
 */
public class DgraphSqlHelper {

    private static final Integer MAX_LEVEL_LIMIT = 10;

    public static String getVar(Class clazz, Integer levelLimit) {
        Integer maxLevel = flatClass(clazz, new ArrayList<>()).stream().mapToInt(it -> it.split("\\.").length).max().orElse(1) - 1;
        levelLimit = Optional.ofNullable(levelLimit).filter(it -> it > 0).orElse(MAX_LEVEL_LIMIT);
        if (maxLevel > levelLimit) {
            return generateQueryByMaxLevel(levelLimit);
        } else {
            return generateQueryByMaxLevel(maxLevel);
        }
    }

    public static String getVar(Class clazz){
        return getVar(clazz,MAX_LEVEL_LIMIT);
    }

    static String generateQueryByMaxLevel(Integer maxLevel) {
        if (maxLevel == 0) {
            return "";
        }
        return "{ \n uid \n expand(_all_)" + generateQueryByMaxLevel(maxLevel - 1) + "\n}";
    }

    static Set<String> flatClass(Class clazz, List<Class> alreadyExistClass) {
        List<Class> newAlreadyExistClass = new ArrayList<>(alreadyExistClass);
        newAlreadyExistClass.add(clazz);
        Set<Field> fields = ReflectionUtils.getFields(clazz,field -> Objects.nonNull(field) && !field.isSynthetic());
        Set<String> flatFieldList = new HashSet<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(UnionClasses.class)) {
                UnionClasses unionClasses = field.getAnnotation(UnionClasses.class);
                flatFieldList.addAll(Arrays.stream(unionClasses.value()).flatMap(it -> flatClass(it, newAlreadyExistClass).stream()).map(it -> clazz.getSimpleName() + "." + it).collect(Collectors.toList()));
            } else {
                Class<?> fieldType;
                if (List.class.isAssignableFrom(field.getType())) {
                    if (field.getGenericType() instanceof Class) {
                        //没有泛型的List当成Object处理
                        fieldType = Object.class;
                    } else {
                        fieldType = (Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                    }
                } else {
                    fieldType = field.getType();
                }
                if (newAlreadyExistClass.contains(fieldType)) {
                    continue;
                }
                if(DgraphTypeUtil.isDgraphType(fieldType)){
                    flatFieldList.addAll(flatClass(fieldType, newAlreadyExistClass).stream().map(it -> clazz.getSimpleName() + "." + it).collect(Collectors.toList()));
                }else{
                    flatFieldList.add(clazz.getSimpleName() + "." + field.getName());
                }
            }
        }
        return flatFieldList;
    }
}