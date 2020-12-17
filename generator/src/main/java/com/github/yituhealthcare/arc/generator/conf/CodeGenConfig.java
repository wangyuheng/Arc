package com.github.yituhealthcare.arc.generator.conf;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 代码生成器配置
 *
 * @author yuheng.wang
 */
public class CodeGenConfig {
    private static final CodeGenOperation DEFAULT_OPERATION = CodeGenOperation.OVERRIDE;

    /**
     * 生成策略
     */
    private List<CodeGenStrategy> genStrategies = new ArrayList<>();
    /**
     * 基本包路径
     */
    private String basePackage = "com.github.yituhealthcare.arc.generated";
    /**
     * 是否删除上次生成的代码
     * 会清空指定包下全部代码
     */
    private boolean dropAll = false;
    /**
     * 全局忽略，不会覆盖相关代码文件
     */
    private List<String> ignoreJavaFileNames = new ArrayList<>();
    /**
     * dgraph schema 生成路径
     */
    private String dgraphPath = "dgraph/schema.dgraph";

    public CodeGenOperation getOperationByType(CodeGenType codeGenType) {
        return genStrategies.stream()
                .filter(it -> codeGenType.equals(it.getCodeGenType()))
                .map(CodeGenStrategy::getCodeGenOperation)
                .findAny()
                .orElse(DEFAULT_OPERATION);
    }

    public boolean isDropAll() {
        return dropAll;
    }

    public List<String> getIgnoreJavaFileNames() {
        return ignoreJavaFileNames;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public List<CodeGenStrategy> getGenStrategies() {
        return genStrategies;
    }

    public void setGenStrategies(List<CodeGenStrategy> genStrategies) {
        this.genStrategies = genStrategies;
    }

    public void setDropAll(boolean dropAll) {
        this.dropAll = dropAll;
    }

    public void setIgnoreJavaFileNames(List<String> ignoreJavaFileNames) {
        this.ignoreJavaFileNames = ignoreJavaFileNames;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getDgraphPath() {
        return dgraphPath;
    }

    public void setDgraphPath(String dgraphPath) {
        this.dgraphPath = dgraphPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CodeGenConfig config = (CodeGenConfig) o;
        return dropAll == config.dropAll &&
                genStrategies.equals(config.genStrategies) &&
                basePackage.equals(config.basePackage) &&
                ignoreJavaFileNames.equals(config.ignoreJavaFileNames) &&
                dgraphPath.equals(config.dgraphPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genStrategies, basePackage, dropAll, ignoreJavaFileNames, dgraphPath);
    }
}
