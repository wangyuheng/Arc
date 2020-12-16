package com.github.yituhealthcare.arc.dgraph.dictionary;

/**
 * 通过json在序列化时放置domainClass较复杂，暂时通过interface实现
 */
public interface IDomainClass {
    default String getDomainClass() {
        return this.getClass().getName();
    }
}
