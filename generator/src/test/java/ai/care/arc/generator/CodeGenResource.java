package ai.care.arc.generator;

import ai.care.arc.generator.codegen.IGenerator;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(CodeGenResources.class)
@ArgumentsSource(CodeGenResourceProvider.class)
public @interface CodeGenResource {

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
    Class<? extends IGenerator> generatorClazz();

    /**
     * graphql schema在resource目录下路径
     */
    String sourceGraphqlSchemaPath();

    /**
     * expected java文件在resource目录下路径
     */
    String[] generatedJavaCodePaths();

}
