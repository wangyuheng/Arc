package ai.care.arc.dgraph.util;

import com.alibaba.fastjson.JSONObject;

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

    private static List<Class> BASIC_CLASS = Arrays.asList(String.class, Integer.class, Float.class, boolean.class,Boolean.class, Object.class, OffsetDateTime.class, Long.class, JSONObject.class);

    public static String getVar(List<Class> classes, List<Class> alreadyExistClass, boolean ifAdd2AlreadyExistClass) {
        if(ifAdd2AlreadyExistClass) {
            alreadyExistClass.addAll(classes);
        }
        StringBuilder var = new StringBuilder("{ \n uid \n expand(_all_)");
        classes.forEach(clazz -> sqlHandle(clazz, alreadyExistClass, var));
        var.append("\n}");
        return var.toString();
    }

    private static void sqlHandle(Class clazz, List<Class> alreadyExistClass, StringBuilder var) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            UnionClasses unionClasses = field.getAnnotation(UnionClasses.class);
            Class fieldType = Optional.of(field)
                    .filter(f -> Objects.equals(List.class, f.getType()))
                    .map(f -> (ParameterizedType) f.getGenericType())
                    .map(ParameterizedType::getActualTypeArguments)
                    .map(types -> (Class) types[0])
                    .orElse(field.getType());
            if (needExpand(fieldType, alreadyExistClass) || Objects.nonNull(unionClasses)) {
                var.append("\n");
                var.append(DgraphTypeUtil.getDgraphTypeValue(clazz));
                var.append(".");
                var.append(fieldName);
                if (Objects.nonNull(unionClasses)) {
                    var.append(getVar(Arrays.stream(unionClasses.value()).collect(Collectors.toList()), new ArrayList<>(alreadyExistClass),false));
                } else {
                    var.append(getVar(Collections.singletonList(fieldType), new ArrayList<>(alreadyExistClass),true));
                }
            }
        }
    }

    private static boolean needExpand(Class fieldType, List<Class> alreadyExistClass) {
        return !BASIC_CLASS.contains(fieldType) && !alreadyExistClass.contains(fieldType) && !fieldType.isEnum();
    }
}