package com.thx.logicroutermodule;

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
public interface IRecyclable {
    /**
     * 重用之前的清理操作
     */
    void clearBeforeReuse();
}
