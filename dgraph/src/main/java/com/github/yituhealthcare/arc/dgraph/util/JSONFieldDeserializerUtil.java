package com.github.yituhealthcare.arc.dgraph.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author junhao.chen
 * @date 2020/8/7
 * 将DGraph数据转回JavaBean之前先将JavaBean中的JSONString字段转换回JSONObject 原因之一看下一行,原因之二FastJson bug JSONObject中套复杂的JSONString出现的转义符会导致反序列化出现异常
 * @see JSONObjectSerializer
 *
 */
public class JSONFieldDeserializerUtil {

    private static List<Class> BASIC_CLASS_EXCEPT_JSON = Arrays.asList(String.class, Integer.class, Float.class, Object.class, boolean.class, Boolean.class, OffsetDateTime.class, Long.class);

    public static void changeJson(JSONObject json, Map<String, Object> map) {
        map.entrySet().stream().filter(it -> json.containsKey(it.getKey())).forEach(entry -> {
            if (json.get(entry.getKey()) instanceof JSONArray) {
                if (Objects.isNull(entry.getValue())) {
                    JSONArray jsonArray = json.getJSONArray(entry.getKey());
                    for (int i = 0; i < jsonArray.size(); i++) {
                        jsonArray.set(i, JSON.parseObject((String) jsonArray.get(i)));
                    }
                } else {
                    json.getJSONArray(entry.getKey()).forEach(it -> changeJson((JSONObject) it, (Map<String, Object>) entry.getValue()));
                }
            } else {
                if (Objects.isNull(entry.getValue())) {
                    json.put(entry.getKey(), JSON.parseObject(json.getString(entry.getKey())));
                } else {
                    changeJson(json.getJSONObject(entry.getKey()), (Map<String, Object>) entry.getValue());
                }
            }
        });
    }

    public static Map<String,Object> getJsonMap(Class clazz){
        return getJsonMap(Arrays.asList(clazz),new ArrayList<>());
    }

    private static Map<String, Object> getJsonMap(List<Class> classes,List<Class> alreadyExistedClasses) {
        Map<String, Object> map = new HashMap<>();
        for (Class clazz : classes) {
            if (alreadyExistedClasses.contains(clazz)){
                continue;
            }
            alreadyExistedClasses.add(clazz);
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (Objects.equals(field.getType(), JSONObject.class) || Objects.equals(field.getType(),Map.class)) {
                    map.put(DgraphTypeUtil.getDgraphTypeValue(clazz) + "." + field.getName(), null);
                    continue;
                }
                if (!BASIC_CLASS_EXCEPT_JSON.contains(field.getType()) && !field.isEnumConstant()) {
                    if (Objects.equals(field.getType(), List.class)) {
                        Class listInnerClazz = (Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                        if (!BASIC_CLASS_EXCEPT_JSON.contains(listInnerClazz)) {
                            Map<String, Object> innerJsonMap = getJsonMap(Collections.singletonList((Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]),new ArrayList<>(alreadyExistedClasses));
                            if (!CollectionUtils.isEmpty(innerJsonMap.keySet())) {
                                map.put(DgraphTypeUtil.getDgraphTypeValue(clazz) + "." + field.getName(), innerJsonMap);
                            }
                        }
                    } else {
                        Map<String, Object> innerJsonMap = getJsonMap(Collections.singletonList(field.getType()),new ArrayList<>(alreadyExistedClasses));
                        if (!CollectionUtils.isEmpty(innerJsonMap.keySet())) {
                            map.put(DgraphTypeUtil.getDgraphTypeValue(clazz) + "." + field.getName(), innerJsonMap);
                        }
                    }
                }
                UnionClasses unionClasses = field.getAnnotation(UnionClasses.class);
                if (Objects.nonNull(unionClasses)) {
                    Map<String, Object> innerJsonMap = getJsonMap(Arrays.stream(unionClasses.value()).collect(Collectors.toList()),new ArrayList<>(alreadyExistedClasses));
                    if (!CollectionUtils.isEmpty(innerJsonMap.keySet())) {
                        map.put(DgraphTypeUtil.getDgraphTypeValue(clazz) + "." + field.getName(), innerJsonMap);
                    }
                }
            }
        }
        return map;
    }
}
