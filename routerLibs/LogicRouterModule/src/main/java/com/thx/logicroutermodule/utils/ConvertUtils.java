package com.thx.logicroutermodule.utils;

import androidx.annotation.Nullable;

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
public class ConvertUtils {
    /**
     * 将一个Object转为double数字
     *
     * @throws NumberFormatException 当 obj 既不是数字也不是字符串时抛出
     */
    public static double doubleOf(@Nullable Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }

        if (obj instanceof String) {
            return Double.valueOf((String) obj);
        }

        throw new NumberFormatException();
    }

    /**
     * 将一个 Object 转为 int 数字
     *
     * @throws NumberFormatException 当 obj 既不是数字也不是字符串时抛出
     */
    public static int intOf(@Nullable Object obj) {
        if (obj == null) {
            return 0;
        }

        if (obj instanceof Integer) {
            return (int) obj;
        }

        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }

        if (obj instanceof String) {
            return Integer.valueOf((String) obj);
        }

        throw new NumberFormatException();
    }

}
