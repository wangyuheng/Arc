package com.github.yituhealthcare.arc.generator.codegen;

import com.squareup.javapoet.JavaFile;
import graphql.schema.idl.TypeDefinitionRegistry;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 代码生成
 * 提取 {@link TypeDefinitionRegistry} 中的相关类定义，转换为 {@link JavaFile} 。关系为 N -> N
 */
public interface IGenerator extends Function<TypeDefinitionRegistry, Stream<JavaFile>> {
}
