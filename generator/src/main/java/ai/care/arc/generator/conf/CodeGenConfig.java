package ai.care.arc.generator.conf;

import java.util.Collections;
import java.util.List;

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
    private List<CodeGenStrategy> genStrategies;
    /**
     * 是否删除上次生成的代码
     * 会清空指定包下全部代码
     */
    private boolean dropAll;
    /**
     * 全局忽略，不会覆盖相关代码文件
     */
    private List<String> ignoreJavaFileNames;

    public CodeGenConfig(List<CodeGenStrategy> genStrategies) {
        this(genStrategies, false);
    }

    public CodeGenConfig(List<CodeGenStrategy> genStrategies, boolean dropAll) {
        this(genStrategies, dropAll, Collections.emptyList());
    }

    public CodeGenConfig(List<CodeGenStrategy> genStrategies, boolean dropAll, List<String> ignoreJavaFileNames) {
        this.genStrategies = genStrategies;
        this.dropAll = dropAll;
        this.ignoreJavaFileNames = ignoreJavaFileNames;
    }

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
}
