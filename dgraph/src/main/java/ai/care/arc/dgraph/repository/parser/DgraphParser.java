package ai.care.arc.dgraph.repository.parser;

import ai.care.arc.core.util.DomainClassUtil;
import ai.care.arc.dgraph.dictionary.DgraphConstant;
import ai.care.arc.dgraph.util.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Bean --parser--> DB
 * DB --parser--> Bean
 * <p>
 * dependent FastJson
 */
public class DgraphParser {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DgraphParser.class);
    private static List<Class> BASIC_CLASS = Arrays.asList(String.class, Integer.class, Float.class, boolean.class, Boolean.class, OffsetDateTime.class, Long.class, JSONObject.class);


    private Class<?> domainClass;

    private SerializeFilter preFilter;
    private SerializeFilter postFilter;

    public DgraphParser(Class<?> domainClass) {
        this.domainClass = domainClass;
        String prefix = DgraphTypeUtil.getPrefix(domainClass);
        preFilter = new DgraphPrefixNameFilter(prefix);
        postFilter = new DgraphPostNameFilter();
    }

    public String parseDecoratorArgs(String... vars) {
        //TODO 多层dgraph type 解析
        if (null == vars || vars.length == 0) {
            vars = Arrays.stream(domainClass.getDeclaredFields())
                    .map(Field::getName)
//                    .map(name -> DgraphTypeHelper.getFieldWrapper(domainClass, name))
                    .toArray(String[]::new);
        }
        return Arrays.stream(vars)
                .map(var -> {
                    if (DgraphTypeUtil.isWrapper(domainClass, var)) {
                        return var;
                    } else {
                        return DgraphTypeUtil.getFieldWrapper(domainClass, var);
                    }
                })
                .map(var -> var + "\n")
                .collect(Collectors.joining());
    }

    public JSONObject parseDecoratorJSON(Object entity) {
        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.put(JSONObject.class, new JSONObjectSerializer());
        String jsonString = JSON.toJSONString(entity, serializeConfig, new SerializeFilter[]{preFilter, new AddDomainClassFilter(), new UidSerializer()});
        JSONObject jsonObject = JSON.parseObject(jsonString, JSONObject.class);
//        jsonObject.put(DomainClassUtil.DOMAIN_CLASS_KEY, domainClass.getName());
        this.fillDgraphElement(jsonObject);
        return jsonObject;
    }

    public <T> List<T> extractJSONArray(JSONArray array) {
        return array.stream()
                .map(it -> (JSONObject) it)
                .peek(it -> JSONFieldDeserializerUtil.changeJson(it,JSONFieldDeserializerUtil.getJsonMap(Collections.singletonList(domainClass))))
                .map(jsonObject -> JSON.toJSONString(jsonObject, postFilter))
                .map(jsonStr -> (T) JSON.parseObject(jsonStr, domainClass, new JSONObjectDeserializer()))
                .peek(this::unionClassHandle)
                .collect(Collectors.toList());
    }

    /**
     * 解析json结果，反序列化为domain对象
     *
     * @param jsonObject DB查询结果
     * @param <T>        domain对象class
     * @return domain对象
     */
    public <T> Optional<T> extractJSON(JSONObject jsonObject) {
        return Optional.ofNullable(jsonObject)
                .map(it -> {
                    JSONFieldDeserializerUtil.changeJson(it,JSONFieldDeserializerUtil.getJsonMap(Collections.singletonList(domainClass)));
                    return it;
                })
                .map(it -> JSON.toJSONString(it, postFilter))
                .map(jsonStr -> (T) JSON.parseObject(jsonStr, domainClass, new JSONObjectDeserializer()))
                .map(it -> {
                    unionClassHandle(it);
                    return it;
                });
    }


    private <T> void unionClassHandle(T domain) {
        Field[] fields = domain.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                //  避免static方法造成死循环
                if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()) || Modifier.isTransient(field.getModifiers())) {
                    return;
                }
                field.setAccessible(true);
                Object value = field.get(domain);
                if (Objects.nonNull(value)) {
                    UnionClasses unionClasses = field.getAnnotation(UnionClasses.class);
                    Class fieldClazz = value.getClass();
                    if (Objects.nonNull(unionClasses)) {
                        if (value instanceof java.util.List) {
                            JSONArray fieldValue = JSON.parseArray(JSON.toJSONString(value));
                            field.set(domain, jsonArrayHandle(fieldValue));
                        } else {
                            JSONObject fieldValue = (JSONObject) value;
                            fieldClazz = Class.forName(fieldValue.getString(DomainClassUtil.DOMAIN_CLASS_KEY));
                            field.set(domain, JSON.parseObject(fieldValue.toJSONString(), fieldClazz, new JSONObjectDeserializer()));
                        }
                    }
                    value = field.get(domain);
                    if (!BASIC_CLASS.contains(fieldClazz) && !value.getClass().isEnum()) {
                        if (value instanceof java.util.List) {
                            ((List) value).forEach(this::unionClassHandle);
                        } else {
                            unionClassHandle(value);
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                log.error("handle union class error, class not found, class : {} , field : {}", domain.getClass().getName(), field.getName(), e);
            } catch (IllegalAccessException e) {
                log.error("handle union field error, field illegal access, class : {} , field : {}", domain.getClass().getName(), field.getName(), e);
            }
        }
    }

    private List<Object> jsonArrayHandle(JSONArray jsonArray) throws ClassNotFoundException {
        List<Object> results = new ArrayList<>();
        for (Object object : jsonArray) {
            Class fieldClazz = Class.forName(JSON.parseObject(JSON.toJSONString(object)).getString(DomainClassUtil.DOMAIN_CLASS_KEY));
            results.add(JSON.parseObject(JSON.toJSONString(object), fieldClazz, new JSONObjectDeserializer()));
        }
        return results;
    }

    private void fillUid(JSONObject json) {
        String uidVal = json.getString(DgraphConstant.DB_UID_FIELD);
        if (json.containsKey(DomainClassUtil.DOMAIN_CLASS_KEY)) {
            Class<?> clazz = DomainClassUtil.getDomainClass(json);
            String uidFieldName = DgraphTypeUtil.getUidFieldSimpleName(clazz);
            json.put(Optional.ofNullable(uidFieldName).orElse("id"), uidVal);
        }
        deepTraverseJson(json, this::fillUid);
    }

    private void fillDgraphElement(JSONObject json) {
        if (json.containsKey(DomainClassUtil.DOMAIN_CLASS_KEY)) {
            Class<?> clazz = DomainClassUtil.getDomainClass(json);
            // 如果id一致，会在Dgraph中重复创建多条记录
            if (clazz == domainClass) {
                json.computeIfAbsent("uid", k -> DgraphTypeUtil.getUidFieldDbWrapper(clazz));
            } else {
                String uidValue = json.getString("uid");
                if (StringUtils.isEmpty(uidValue)) {
                    json.put("uid", "_:" + UUID.randomUUID().toString());
                } else if (uidValue.length() == 36) {
                    // 如果指定了uuid的uid 则代表创建时引用
                    json.put("uid", "_:" + uidValue);
                }
            }
            json.put("dgraph.type", DgraphTypeUtil.getDgraphTypeValue(clazz));
        } else {
            if (json.size() == 1 && json.containsKey("id")) {
                json.put("uid", json.getString("id"));
            }
        }
        deepTraverseJson(json, this::fillDgraphElement);
    }

    private void deepTraverseJson(JSONObject json, Consumer<JSONObject> consumer) {
        json.values()
                .stream()
                .filter(JSONObject.class::isInstance)
                .map(o -> (JSONObject) o)
                .forEach(consumer);

        json.values()
                .stream()
                .filter(JSONArray.class::isInstance)
                .map(o -> (JSONArray) o)
                .flatMap(Collection::stream)
                .filter(JSONObject.class::isInstance)
                .map(o -> (JSONObject) o)
                .forEach(consumer);
    }
}
