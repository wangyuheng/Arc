package com.github.yituhealthcare.arc.dgraph.util;

import com.alibaba.fastjson.serializer.NameFilter;

/**
 * @author junhao.chen
 * @date 2020/6/22
 */
public class UidSerializer implements NameFilter {

    @Override
    public String process(Object object, String name, Object value) {
        if(DgraphTypeUtil.isUidField(object.getClass(),name)){
            return "uid";
        }else{
            return name;
        }
    }
}
