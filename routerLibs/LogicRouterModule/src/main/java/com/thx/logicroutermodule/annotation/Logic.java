package com.thx.logicroutermodule.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
@Target(ElementType.TYPE)
public @interface Logic {
    /**
     * Logic 的类名
     */
    String value();
}
