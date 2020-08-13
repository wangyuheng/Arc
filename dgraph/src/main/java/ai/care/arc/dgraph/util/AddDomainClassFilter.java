package ai.care.arc.dgraph.util;

import com.alibaba.fastjson.serializer.AfterFilter;
import ai.care.arc.core.util.DomainClassUtil;

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
