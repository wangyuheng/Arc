package io.github.wangyuheng.arc.generator.codegen.spec;

import com.squareup.javapoet.*;
import org.springframework.util.CollectionUtils;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * 根据 {@link FieldSpec} 生成 setter {@link MethodSpec}
 *
 * @author yuheng.wang
 */
public class TypeSpecGenBuilder implements UnaryOperator<TypeSpec> {

    @Override
    public TypeSpec apply(TypeSpec source) {
        if (CollectionUtils.isEmpty(source.fieldSpecs)) {
            return source;
        } else {
            TypeSpec target = TypeSpec.classBuilder(source.name + "Builder").build();
            return source.toBuilder()
                    .addMethod(getAllArgsConstructor(source.fieldSpecs))
                    .addMethod(getNoArgsConstructor())
                    .addMethod(getBuilderInstanceMethod(target))
                    .addType(getBuilderType(source, target))
                    .build();
        }
    }

    private MethodSpec getAllArgsConstructor(List<FieldSpec> fieldSpecs) {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameters(fieldSpecs.stream().map(f -> ParameterSpec.builder(f.type, f.name).build()).collect(Collectors.toList()));
        fieldSpecs.forEach(fieldSpec -> builder.addStatement("this.$L = $L", fieldSpec.name, fieldSpec.name));
        return builder.build();
    }

    private MethodSpec getNoArgsConstructor() {
        return MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).build();
    }

    private MethodSpec getBuilderInstanceMethod(TypeSpec target) {
        return MethodSpec.methodBuilder("builder")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(getType(target))
                .addStatement("return new $T()", getType(target))
                .build();
    }

    private TypeSpec getBuilderType(TypeSpec source, TypeSpec target) {
        return target.toBuilder()
                .addFields(source.fieldSpecs.stream().map(it -> FieldSpec.builder(it.type, it.name, Modifier.PRIVATE).build()).collect(Collectors.toList()))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build())
                .addMethods(getBuilderSetMethodSpecList(source, target))
                .addMethod(getSourceInstanceMethod(source))
                .build();
    }

    private List<MethodSpec> getBuilderSetMethodSpecList(TypeSpec source, TypeSpec target) {
        return source.fieldSpecs.stream()
                .map(fieldSpec -> MethodSpec.methodBuilder(fieldSpec.name)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(getType(target))
                        .addParameter(ParameterSpec.builder(fieldSpec.type, fieldSpec.name).build())
                        .addStatement("this.$L = $L", fieldSpec.name, fieldSpec.name)
                        .addStatement("return this")
                        .build()).collect(Collectors.toList());
    }

    private MethodSpec getSourceInstanceMethod(TypeSpec source) {
        return MethodSpec.methodBuilder("build")
                .addStatement("return new $L($L)", source.name, source.fieldSpecs.stream().map(f -> f.name).collect(Collectors.joining(",")))
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get("", source.name))
                .build();
    }

    private TypeName getType(TypeSpec typeSpec) {
        return ClassName.get("", typeSpec.name);
    }

}
