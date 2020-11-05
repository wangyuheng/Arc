package ai.care.arc.generator;

import ai.care.arc.generator.codegen.IGenerator;

import java.util.Arrays;

public class CodeGenResourceArgument {

    /**
     * 基础包路径
     * 决定了生成的java代码包路径
     */
    private String basePackage;

    /**
     * 代码生成器，创建实例，调用以生成java代码
     *
     * @see IGenerator
     */
    private Class<? extends IGenerator> generatorClazz;

    /**
     * graphql schema在resource目录下路径
     */
    private String sourceGraphqlSchemaPath;

    /**
     * expected java文件在resource目录下路径
     */
    private String[] generatedJavaCodePaths;

    public CodeGenResourceArgument(String basePackage, Class<? extends IGenerator> generatorClazz, String sourceGraphqlSchemaPath, String[] generatedJavaCodePaths) {
        this.basePackage = basePackage;
        this.generatorClazz = generatorClazz;
        this.sourceGraphqlSchemaPath = sourceGraphqlSchemaPath;
        this.generatedJavaCodePaths = generatedJavaCodePaths;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public Class<? extends IGenerator> getGeneratorClazz() {
        return generatorClazz;
    }

    public String getSourceGraphqlSchemaPath() {
        return sourceGraphqlSchemaPath;
    }

    public String[] getGeneratedJavaCodePaths() {
        return generatedJavaCodePaths;
    }

    @Override
    public String toString() {
        return "CodeGenResourceArgument{" +
                "basePackage='" + basePackage + '\'' +
                ", generatorClazz=" + generatorClazz +
                ", sourceGraphqlSchemaPath='" + sourceGraphqlSchemaPath + '\'' +
                ", generatedJavaCodePaths=" + Arrays.toString(generatedJavaCodePaths) +
                '}';
    }
}
