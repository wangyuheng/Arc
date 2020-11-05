package ai.care.arc.generator.codegen.util;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;

import java.util.Objects;

/**
 * 代码生成Spec工具类
 *
 * @author yuheng.wang
 */
public interface GenSpecUtil {

    /**
     * getter方法前缀
     */
    static String getGetterPrefix(FieldSpec fieldSpec) {
        if (Objects.equals(fieldSpec.type, ClassName.get(Boolean.class))) {
            return "is";
        } else {
            return "get";
        }
    }
}
