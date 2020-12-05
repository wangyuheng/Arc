package com.github.yituhealthcare.arc.core.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * 操作type中的domainClass字段
 * <p>
 * 每个Type都包含一个domainClass字段，用于存储type对应javaBean的className
 */
public class DomainClassUtil {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DomainClassUtil.class);

    private DomainClassUtil() {
    }

    public static final String DOMAIN_CLASS_KEY = "domainClass";

    public static boolean isDomainClassKey(String name) {
        return Objects.equals(DomainClassUtil.DOMAIN_CLASS_KEY, name);
    }

    public static Class<?> getDomainClass(Object source) {
        Class<?> clazz;
        if (source instanceof JSONObject) {
            try {
                clazz = Class.forName(((JSONObject) source).getString(DomainClassUtil.DOMAIN_CLASS_KEY));
            } catch (Exception e) {
                log.warn("domainClass not found! json:{}", source, e);
                clazz = source.getClass();
            }
        } else if (source instanceof Map) {
            try {
                String className = ((Map) source).getOrDefault(DomainClassUtil.DOMAIN_CLASS_KEY, source.getClass()).toString();
                clazz = Class.forName(className);
            } catch (Exception e) {
                log.warn("domainClass not found! json:{}", source, e);
                clazz = source.getClass();
            }
        } else if (source instanceof Collection<?>) {
            return getDomainClass(((Collection<?>) source).iterator().next());
        } else {
            clazz = source.getClass();
        }
        return clazz;
    }

}
