package ai.care.arc.dgraph.util;

/**
 * Dgraph Uid 工具类
 */
public class UidUtil {
    private UidUtil() {
    }

    /**
     * 判断是否为uid
     * 判断是否为16进制字符串
     *
     * @param value uid字符串
     * @return boolean 是否为16进制字符串
     */
    public static boolean isUid(String value) {
        if (null == value) {
            return false;
        } else {
            final int index = (value.startsWith("-") ? 1 : 0);
            if (value.startsWith("0x", index) || value.startsWith("0X", index) || value.startsWith("#", index)) {
                try {
                    //noinspection ResultOfMethodCallIgnored
                    Long.decode(value);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

}
