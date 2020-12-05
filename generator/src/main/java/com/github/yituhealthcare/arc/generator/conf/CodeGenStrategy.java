package com.github.yituhealthcare.arc.generator.conf;

/**
 * 生成代码的策略
 * 包含 类型 {@link CodeGenType} 和 操作 {@link CodeGenOperation}
 *
 * @author yuheng.wang
 */
public class CodeGenStrategy {
    private final CodeGenType codeGenType;
    private final CodeGenOperation codeGenOperation;

    public CodeGenStrategy(CodeGenType codeGenType, CodeGenOperation codeGenOperation) {
        this.codeGenType = codeGenType;
        this.codeGenOperation = codeGenOperation;
    }

    public CodeGenType getCodeGenType() {
        return codeGenType;
    }

    public CodeGenOperation getCodeGenOperation() {
        return codeGenOperation;
    }
}
