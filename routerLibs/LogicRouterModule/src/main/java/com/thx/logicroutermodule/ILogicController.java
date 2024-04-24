package com.thx.logicroutermodule;

import androidx.annotation.Nullable;

import java.util.Map;

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
public interface ILogicController {

    /**
     * 尝试停止当前 Logic 的执行。
     * <p>
     * <br/> 该方法不一定可以立即停止，其实现由具体实现类决定。
     * <br/> 如果子类 Logic 不能被停止，可以不重写该方法。
     */
    void tryStop();

    /**
     * 更新 Logic 运行过程中的参数。
     * <br/> 如果子类 Logic 不需要在中途改变参数，可以不重写该方法。
     * <br/> 如果子类没有实现这个方法，调用后可能会不生效
     *
     * @param updateParams 新的参数
     */
    void updateParams(@Nullable Map updateParams);

    /**
     * 不同的对象有不同的 uuid。
     *
     * @return 唯一的UUID
     */
    String uuid();

    /**
     * 是否正在运行。
     *
     * @return true: 正在运行; false: 没在运行。
     */
    boolean isRunning();
}
