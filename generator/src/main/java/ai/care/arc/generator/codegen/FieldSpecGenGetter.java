package ai.care.arc.generator.codegen;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import org.springframework.util.StringUtils;

import javax.lang.model.element.Modifier;
import java.util.Objects;
import java.util.function.Function;

/**
 * 根据 {@link FieldSpec} 生成 getter {@link MethodSpec}
 *
 * @author yuheng.wang
 */
public class FieldSpecGenGetter implements Function<FieldSpec, MethodSpec> {

    private static final ClassName BOOLEAN_CLASS_NAME = ClassName.get(Boolean.class);

    @Override
    public MethodSpec apply(FieldSpec fieldSpec) {
        return MethodSpec.methodBuilder(getPrefix(fieldSpec) + StringUtils.capitalize(fieldSpec.name))
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return $L", fieldSpec.name)
                .build();
    }

    private String getPrefix(FieldSpec fieldSpec) {
        if (Objects.equals(fieldSpec.type, BOOLEAN_CLASS_NAME)) {
            return "is";
        } else {
            return "get";
        }
    }
}
