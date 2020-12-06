package com.github.yituhealthcare.arc.graphql.util;

import com.alibaba.fastjson.JSON;

import java.util.Map;

public class GraphqlPayloadUtil {
    private GraphqlPayloadUtil(){}

    private static final String PAYLOAD_KEY = "payload";

    public static <T> T resolveArguments(Map<String, Object> args, Class<T> clazz) {
        if (null == args) {
            return null;
        }
        return JSON.parseObject(JSON.toJSONString(args.get(PAYLOAD_KEY)), clazz);
    }
}
