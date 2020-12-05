package com.github.yituhealthcare.arc.core.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.util.Arrays;

/**
 * BeanUtil扩展
 *
 * @author yuheng.wang
 * @see BeanUtils
 */
public final class BeanUtil {

    private BeanUtil(){}

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        return Arrays.stream(src.getPropertyDescriptors())
                .filter(pd -> null == src.getPropertyValue(pd.getName()))
                .map(FeatureDescriptor::getName)
                .toArray(String[]::new);
    }

    /**
     * 复制非空field
     */
    public static <T> T copyNotNullProperties(Object source, T target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
        return target;
    }

    public static <T> T deepClone(Object source, Class<T> classOfT){
        String sourceString = JSONObject.toJSONString(source);
        return JSONObject.parseObject(sourceString,classOfT);
    }

}
