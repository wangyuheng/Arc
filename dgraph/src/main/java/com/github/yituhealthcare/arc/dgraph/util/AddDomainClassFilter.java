package com.github.yituhealthcare.arc.dgraph.util;

import com.github.yituhealthcare.arc.core.util.DomainClassUtil;
import com.alibaba.fastjson.serializer.AfterFilter;

/**
 * @author junhao.chen
 * @date 2020/6/12
 */
public class AddDomainClassFilter extends AfterFilter {
    @Override
    public void writeAfter(Object object) {
        writeKeyValue(DomainClassUtil.DOMAIN_CLASS_KEY, object.getClass().getName());
    }
}
