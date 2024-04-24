package com.thx.logicroutermodule.inner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.thx.logicroutermodule.AbstractLogic;
import com.thx.logicroutermodule.ILogicController;
import com.thx.logicroutermodule.ILogicHandler;
import com.thx.logicroutermodule.LogicResult;

import java.util.Map;

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
public interface ILogicRouter {
    /**
     * 注册路由表。
     * <p>
     * <br/> 可以多次注册，如果有重复的，会用新的替换旧的。
     *
     * @param map Key: Logic的名称; Value: 对应的 Class 实现类
     */
    void addLogicMap(@NonNull Map<String, Class<? extends AbstractLogic>> map);

    /**
     * 在内部并发队列异步执行一个业务逻辑。
     *
     * @param name    业务逻辑的名称，需要是已经注册的Logic，如果没有不会执行。
     * @param params  提供给业务逻辑执行的参数
     * @param handler 异步任务的回调。这个回调可能在当前函数执行完成之前就调用。
     * @return 当前 Logic 的控制器
     */
    @Nullable
    ILogicController asynExecute(@NonNull String name, @Nullable Map params, @Nullable ILogicHandler handler);

    /**
     * 在内部并发队列异步执行一个业务逻辑。
     *
     * @param name    业务逻辑的名称，需要是已经注册的Logic，如果没有不会执行。
     * @param body  提供给业务逻辑执行的参数-JSON
     * @param handler 异步任务的回调。这个回调可能在当前函数执行完成之前就调用。
     * @return 当前 Logic 的控制器
     */
    @Nullable
    ILogicController asynExecuteWithObject(@NonNull String name, @Nullable String body, @Nullable ILogicHandler handler);

    /**
     * 在内部并发队列异步执行一个业务逻辑。
     *
     * @param name    业务逻辑的名称，需要是已经注册的Logic，如果没有不会执行。
     * @param params  提供给业务逻辑执行的参数
     * @param handler 异步任务的回调。这个回调可能在当前函数执行完成之前就调用。
     * @param pathParams 路径path上的动态参数
     * @return 当前 Logic 的控制器
     */
    @Nullable
    ILogicController asynExecute(@NonNull String name, @Nullable Map params,@Nullable Map pathParams, @Nullable ILogicHandler handler);

    /**
     * 在内部并发队列异步执行一个业务逻辑。
     *
     * @param name    业务逻辑的名称，需要是已经注册的Logic，如果没有不会执行。
     * @param body  提供给业务逻辑执行的参数-JSON
     * @param handler 异步任务的回调。这个回调可能在当前函数执行完成之前就调用。
     * @param pathParams 路径path上的动态参数
     * @return 当前 Logic 的控制器
     */
    @Nullable
    ILogicController asynExecuteWithObject(@NonNull String name, @Nullable String body,@Nullable Map pathParams, @Nullable ILogicHandler handler);

    /**
     * 同步执行一个业务逻辑。
     *
     * @param name   业务逻辑的名称，需要是已经注册的Logic，如果没有不会执行。
     * @param params 提供给业务逻辑执行的参数
     * @return 执行的结果
     */
    LogicResult syncExecute(String name, Map params);

}
