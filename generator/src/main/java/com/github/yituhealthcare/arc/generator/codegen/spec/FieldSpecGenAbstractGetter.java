package com.github.yituhealthcare.arc.generator.codegen.spec;

import com.github.yituhealthcare.arc.generator.codegen.util.GenSpecUtil;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import org.springframework.util.StringUtils;

import javax.lang.model.element.Modifier;
import java.util.function.Function;

/**
 * 根据 {@link FieldSpec} 生成 abstract getter {@link MethodSpec}
 *
 * @author yuheng.wang
 */
public class FieldSpecGenAbstractGetter implements Function<FieldSpec, MethodSpec> {

    @Override
    public MethodSpec apply(FieldSpec fieldSpec) {
        return MethodSpec.methodBuilder(GenSpecUtil.getGetterPrefix(fieldSpec) + StringUtils.capitalize(fieldSpec.name))
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(fieldSpec.type)
                .build();
    }

}
