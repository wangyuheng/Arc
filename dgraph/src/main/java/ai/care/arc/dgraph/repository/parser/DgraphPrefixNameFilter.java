package ai.care.arc.dgraph.repository.parser;

import com.alibaba.fastjson.serializer.NameFilter;
import ai.care.arc.core.util.DomainClassUtil;
import ai.care.arc.dgraph.util.DgraphTypeUtil;
import lombok.SneakyThrows;

/**
 * Bean -> DB 序列化添加前缀
 */
public class DgraphPrefixNameFilter implements NameFilter {

    private String prefix;

    public DgraphPrefixNameFilter(String prefix) {
        this.prefix = prefix;
    }

    @SneakyThrows
    public String process(Object source, String name, Object value) {
        if (name == null || name.length() == 0 || DomainClassUtil.isDomainClassKey(name)) {
            return name;
        } else {
            Class<?> clazz = DomainClassUtil.getDomainClass(source);
            if (DgraphTypeUtil.isDgraphType(clazz)) {
                if (DgraphTypeUtil.isUidField(clazz, name)) {
                    return name;
                } else if (DgraphTypeUtil.isRelationshipField(clazz, name)) {
                    return DgraphTypeUtil.getRelationshipField(clazz, name);
                } else {
                    return DgraphTypeUtil.getFieldWrapper(clazz, name);
                }
            } else {
                return prefix + name;
            }
        }
    }

}
