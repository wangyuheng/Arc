package ai.care.arc.generator;

import ai.care.arc.generator.codegen.IGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.Extension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 设置代码生成端到端测试case
 *
 * @author yuheng.wang
 * @see Extension
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Test
public @interface GenE2ECase {

    /**
     * 基础包路径
     * 决定了生成的java代码包路径
     */
    String basePackage() default "a.b.c";

    /**
     * 代码生成器，创建实例，调用以生成java代码
     *
     * @see IGenerator
     */
    Class<? extends IGenerator> clazz();

    /**
     * graphql schema在resource目录下路径
     */
    String schemaPath();

    /**
     * expected java文件在resource目录下路径
     */
    String[] javaPaths();

}
