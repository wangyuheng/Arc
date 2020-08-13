package ai.care.arc.dgraph.util;

import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import ai.care.arc.core.util.DomainClassUtil;
import lombok.extern.slf4j.Slf4j;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * 将DGraph结果转换为JavaBean时使用
 *
 * @author junhao.chen
 * @date 2020/6/8
 */
@Slf4j
public class JSONObjectDeserializer implements ExtraProcessor {
    @Override
    public void processExtra(Object o, String s, Object value) {
        if (Objects.equals(s, DomainClassUtil.DOMAIN_CLASS_KEY)) {
            return;
        }
        try {
            if (Objects.equals("uid", s)) {
                //fill uid
                String uidFieldName = DgraphTypeUtil.getUidFieldSimpleName(o.getClass());
                Field field = ReflectionUtils.getAllFields(o.getClass(), f -> f != null && Objects.equals(uidFieldName, f.getName()))
                        .stream()
                        .findAny()
                        .orElseThrow(() -> new RuntimeException("not found filed"));
                field.setAccessible(true);
                field.set(o, value);
            }
        } catch (IllegalAccessException e) {
            log.error("Json deserialize error", e);
        }
    }
}
