package ai.care.arc.generator.codegen.util;

import ai.care.arc.generator.dictionary.GeneratorGlobalConst;
import com.squareup.javapoet.CodeBlock;
import graphql.language.Description;
import graphql.language.TypeDefinition;

import java.util.Optional;

/**
 * 生成器javadoc工具类
 *
 * @author yuheng.wang
 */
public interface JavadocUtils {

    /**
     * 获取graphql字段描述，填充javadoc
     *
     * 无法通过 {@link TypeDefinition} 获取 {@link Description}, 因为getDescription是每个子类<? extends TypeDefinition>自己的方法
     */
    static CodeBlock getDocForType(TypeDefinition<?> typeDefinition, Description description) {
        return CodeBlock.builder()
                .add(Optional.ofNullable(description).map(Description::getContent).orElse(typeDefinition.getName()))
                .add("\n")
                .add(GeneratorGlobalConst.GENERAL_CODE_BLOCK)
                .build();
    }

}