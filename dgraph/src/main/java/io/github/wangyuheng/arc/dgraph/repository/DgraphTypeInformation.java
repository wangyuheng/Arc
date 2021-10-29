package io.github.wangyuheng.arc.dgraph.repository;

import io.github.wangyuheng.arc.dgraph.util.DgraphTypeUtil;
import org.springframework.util.Assert;

/**
 * 维护Dgraph Entity的相关信息
 */
public class DgraphTypeInformation {

    private Class<?> domainClass;

    public DgraphTypeInformation(Class<?> domainClass) {
        Assert.notNull(domainClass, "domainClass must be not null");
        this.domainClass = domainClass;
    }

    public Class<?> getDomainClass() {
        return domainClass;
    }

    public boolean isDgraphType() {
        return DgraphTypeUtil.isDgraphType(domainClass);
    }

    public String getTypeValue() {
        return DgraphTypeUtil.getDgraphTypeValue(domainClass);
    }

    public boolean isUidField(String fieldName) {
        return DgraphTypeUtil.isUidField(domainClass,fieldName);
    }

    public String getUidFieldName() {
        return DgraphTypeUtil.getUidFieldName(domainClass);
    }

    public String getUidFieldDbWrapper() {
        return DgraphTypeUtil.getUidFieldDbWrapper(domainClass);
    }

}
