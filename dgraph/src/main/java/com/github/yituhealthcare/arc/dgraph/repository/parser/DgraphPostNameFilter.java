package com.github.yituhealthcare.arc.dgraph.repository.parser;

import com.github.yituhealthcare.arc.dgraph.util.DgraphTypeHolder;
import com.alibaba.fastjson.serializer.NameFilter;

import java.util.Comparator;

/**
 * DB -> Bean 过程中，移除DB前缀
 * <p>
 * 至过滤dgraphType
 */
public class DgraphPostNameFilter implements NameFilter {

    /**
     * 排序避免 a.b.c 被 a 替换而忽略 a.b
     */
    public String process(Object source, String name, Object value) {
        if (name == null || name.length() == 0) {
            return name;
        } else {
            return DgraphTypeHolder.listDgraphType()
                    .stream()
                    .sorted(Comparator.comparing(String::length).reversed())
                    .filter(k -> name.startsWith(k + "."))
                    .map(s -> name.substring(s.length() + 1))
                    .findAny()
                    .orElse(name);
        }
    }

}
