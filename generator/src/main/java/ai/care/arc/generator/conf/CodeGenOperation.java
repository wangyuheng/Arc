package ai.care.arc.generator.conf;

/**
 * 生成代码的操作行为
 *
 * @author yuheng.wang
 */
public enum CodeGenOperation {
    /**
     * 覆盖更新
     * 如果不存在，则新建
     * 如果存在，则覆盖
     */
    OVERRIDE,
    /**
     * 跳过
     */
    SKIP,
    /**
     * 存在则跳过
     */
    SKIP_IF_EXISTED,
}
