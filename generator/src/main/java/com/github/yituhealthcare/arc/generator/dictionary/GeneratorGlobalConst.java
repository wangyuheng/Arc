package com.github.yituhealthcare.arc.generator.dictionary;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.Map;

/**
 * 代码生成器全局配置
 *  type类中可能会注入service，所以后缀名等操作没有放在具体的生成类中
 *
 * @author yuheng.wang
 */
public class GeneratorGlobalConst {

    public static final CodeBlock GENERAL_CODE_BLOCK = CodeBlock.builder()
            .add("Generate with GraphQL Schema")
            .add("\r\n")
            .add("\r\n")
            .add("@author Arc")
            .build();

    public static final ClassName DEFAULT_LIST_CLASS_NAME = ClassName.get("java.util", "List");

    public static final TypeName DEFAULT_MAP_TYPE = ParameterizedTypeName.get(Map.class, String.class, Object.class);

    public static final String INTERFACE_SUFFIX = "DataFetcher";

    public static final String REPOSITORY_SUFFIX = "Repository";

    public static final String HANDLE_METHOD_SUFFIX = "handle";

    public static final String DIRECTIVE_ACTION = "Action";

}
