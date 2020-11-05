package ai.care.arc.dgraph.util;

import ai.care.arc.dgraph.annotation.DgraphType;
import org.slf4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 持有 {@link DgraphType} bean
 */
public class DgraphTypeHolder {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DgraphTypeHolder.class);

    private DgraphTypeHolder(){}

    private static final Map<String, String> DOMAIN_CLASS_MAP_DGRAPH_TYPE_NAME = new ConcurrentHashMap<>();

    public static void add(BeanDefinition it) {
        try {
            log.info("dgraph type holder add: {}", it);
            String domainClassName = it.getBeanClassName();
            Class<?> clazz = Class.forName(domainClassName);
            String dgraphTypeValue = DgraphTypeUtil.getDgraphTypeValue(clazz);
            DOMAIN_CLASS_MAP_DGRAPH_TYPE_NAME.putIfAbsent(domainClassName, dgraphTypeValue);
        } catch (ClassNotFoundException e) {
            log.warn("bean not found! BeanDefinitionHolder:{}", it, e);
        }
    }


    public static Set<String> listDomainClassName() {
        return DOMAIN_CLASS_MAP_DGRAPH_TYPE_NAME.keySet();
    }

    public static Collection<String> listDgraphType() {
        return DOMAIN_CLASS_MAP_DGRAPH_TYPE_NAME.values();
    }

    public static boolean isEmpty(){
        return DOMAIN_CLASS_MAP_DGRAPH_TYPE_NAME.isEmpty();
    }

    public static void clear(){
        DOMAIN_CLASS_MAP_DGRAPH_TYPE_NAME.clear();
    }

}
