package com.github.yituhealthcare.arc.generator.codegen.util;

import com.github.yituhealthcare.arc.generator.dictionary.GeneratorGlobalConst;
import com.squareup.javapoet.CodeBlock;
import graphql.Assert;
import graphql.language.*;

import java.util.Optional;

/**
 * 生成器javadoc工具类
 *
 * @author yuheng.wang
 */
public interface JavadocUtils {

    /**
     * 获取graphql字段描述，填充javadoc
     * 默认为type名称
     */
    static CodeBlock getDocForType(TypeDefinition<?> typeDefinition) {
        switch (TypeKind.getTypeKind(typeDefinition)) {
            case Object:
                return getClassDocByDescription(((ObjectTypeDefinition) typeDefinition).getDescription(), typeDefinition.getName());
            case Interface:
                return getClassDocByDescription(((InterfaceTypeDefinition) typeDefinition).getDescription(), typeDefinition.getName());
            case Union:
                return getClassDocByDescription(((UnionTypeDefinition) typeDefinition).getDescription(), typeDefinition.getName());
            case Enum:
                return getClassDocByDescription(((EnumTypeDefinition) typeDefinition).getDescription(), typeDefinition.getName());
            case Scalar:
                return getClassDocByDescription(((ScalarTypeDefinition) typeDefinition).getDescription(), typeDefinition.getName());
            case InputObject:
                return getClassDocByDescription(((InputObjectTypeDefinition) typeDefinition).getDescription(), typeDefinition.getName());
            case Operation:
            default:
                return Assert.assertShouldNeverHappen();
        }
    }

    /**
     * 填充字段javadoc
     *
     * @param description    字段描述
     * @param defaultContent 默认显示内容
     */
    static CodeBlock getFieldDocByDescription(Description description, String defaultContent) {
        return CodeBlock.builder()
                .add(Optional.ofNullable(description).map(Description::getContent).map(String::trim).orElse(defaultContent))
                .build();
    }

    /**
     * 填充类javadoc
     *
     * @param description    类描述
     * @param defaultContent 默认显示内容
     */
    static CodeBlock getClassDocByDescription(Description description, String defaultContent) {
        return CodeBlock.builder()
                .add(Optional.ofNullable(description).map(Description::getContent).map(String::trim).orElse(defaultContent))
                .add("\r\n")
                .add(GeneratorGlobalConst.GENERAL_CODE_BLOCK)
                .build();
    }

}