package ai.care.arc.dgraph.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author junhao.chen
 * @date 2020/6/4
 */
public class DgraphSQLHelper {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DgraphSQLHelper.class);

    private static final List<Class> BASIC_CLASS = Arrays.asList(String.class, Integer.class, Float.class, boolean.class, Boolean.class, Object.class, OffsetDateTime.class, Long.class, JSONObject.class);

    private static final Integer MAX_LEVEL_LIMIT = 10;

    public static String getVar(Class clazz,Integer levelLimit) {
        Integer maxLevel = flatClass(clazz, new ArrayList<>()).stream().mapToInt(it -> it.split("\\.").length).max().orElse(1) - 1;
        levelLimit = Optional.ofNullable(levelLimit).filter(it -> it > 0 ).orElse(MAX_LEVEL_LIMIT);
        if (maxLevel > levelLimit) {
            return generateQueryByMaxLevel(levelLimit);
        } else {
            return generateQueryByMaxLevel(maxLevel);
        }
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
        Field[] fields = clazz.getDeclaredFields();
        Set<String> flatFieldList = new HashSet<>();
        for (Field field : fields) {
            if (Objects.isNull(field.getAnnotation(UnionClasses.class))) {
                Class<?> fieldType;
                if (Objects.equals(List.class, field.getType())) {
                    fieldType = (Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                } else {
                    fieldType = field.getType();
                }

                if (Objects.isNull(fieldType)) {
                    log.error("flatClass error class: {} , field: {}", clazz.getSimpleName(), field.getName());
                    continue;
                }

                if (newAlreadyExistClass.contains(fieldType)) {
                    continue;
                }

                if (BASIC_CLASS.contains(fieldType) || fieldType.isEnum()) {
                    flatFieldList.add(clazz.getSimpleName() + "." + field.getName());
                } else {
                    flatFieldList.addAll(flatClass(fieldType, newAlreadyExistClass).stream().map(it -> clazz.getSimpleName() + "." + it).collect(Collectors.toList()));
                }
            } else {
                UnionClasses unionClasses = field.getAnnotation(UnionClasses.class);
                flatFieldList.addAll(Arrays.stream(unionClasses.value()).flatMap(it -> flatClass(it, newAlreadyExistClass).stream()).map(it -> clazz.getSimpleName() + "." + it).collect(Collectors.toList()));
            }
        }
        return flatFieldList;
    }
}