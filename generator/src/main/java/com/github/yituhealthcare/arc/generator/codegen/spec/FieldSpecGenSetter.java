package com.github.yituhealthcare.arc.generator.codegen.spec;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import org.springframework.util.StringUtils;

import javax.lang.model.element.Modifier;
import java.util.function.Function;

/**
 * 根据 {@link FieldSpec} 生成 setter {@link MethodSpec}
 *
 * @author yuheng.wang
 */
public class FieldSpecGenSetter implements Function<FieldSpec, MethodSpec> {

    @Override
    public MethodSpec apply(FieldSpec fieldSpec) {
        return MethodSpec.methodBuilder("set" + StringUtils.capitalize(fieldSpec.name))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(fieldSpec.type, fieldSpec.name)
                .addStatement("this.$L = $L", fieldSpec.name, fieldSpec.name)
                .build();
    }

}
