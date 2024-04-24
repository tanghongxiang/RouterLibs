package com.thx.logicroutermodule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.thx.logicroutermodule.inner.LogicRouterImpl;
import com.thx.logicroutermodule.inner.LogicRouterThreadPool;

import java.util.Map;

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
public final class LogicRouter {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    /**
     * 严格模式。
     * 默认非严格模式。
     * 严格模式下，任何异常情况都会抛出 Exception，方便开发者构建更稳定的应用。
     * 非严格模式下，部分异常情况会执行容错处理。
     * <p>
     * 例如：当试图执行一个不存在的 Logic 时，严格模式会立即抛出异常，非严格模式会调用 handler 返回
     * NO_SUCH_LOGIC。
     */
    public static boolean isStrictMode;

    public static ILog sLog = new ILog.Log();

    private static IConcurrentExecutor sExecutor;

    private static LogicRouterImpl sRouter = new LogicRouterImpl();



    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    public static void registerLogic(Map<String, Class<? extends AbstractLogic>> name2LogicClass) {
        sRouter.addLogicMap(name2LogicClass);
    }

    @Nullable
    public static ILogicController asynExecute(String name, Map params, ILogicHandler handler) {
        return sRouter.asynExecute(name, params, handler);
    }

    @Nullable
    public static ILogicController asynExecute(String name, String body, ILogicHandler handler) {
        return sRouter.asynExecuteWithObject(name, body, handler);
    }

    @Nullable
    public static ILogicController asynExecute(String name, Map params, Map pathParams, ILogicHandler handler) {
        return sRouter.asynExecute(name, params, pathParams, handler);
    }

    @Nullable
    public static ILogicController asynExecute(String name, String body, Map pathParams, ILogicHandler handler) {
        return sRouter.asynExecuteWithObject(name, body, pathParams, handler);
    }

    @Nullable
    public static ILogicController asynExecute(String name, Map params) {
        return sRouter.asynExecute(name, params, null);
    }


    @Nullable
    public static ILogicController asynExecute(String name, ILogicHandler handler) {
        return sRouter.asynExecute(name, null, handler);
    }

    @Nullable
    public static ILogicController asynExecute(String name) {
        return sRouter.asynExecute(name, null, null);
    }

    @NonNull
    public static LogicResult syncExecute(String name, Map params) {
        return sRouter.syncExecute(name, params);
    }

    @NonNull
    public static LogicResult syncExecute(String name) {
        return sRouter.syncExecute(name, null);
    }

    public static void setLog(ILog logger) {
        sLog = logger;
    }

    /**
     * 设置并发异步执行器。不设置时使用内部的线程池。
     */
    public static void setConcurrentExecutor(IConcurrentExecutor executor) {
        sExecutor = executor;
    }

    /**
     * 异步执行一个Runnable
     */
    public static void runOnConcurrent(Runnable runnable) {
        if (LogicRouter.sExecutor != null) {
            LogicRouter.sExecutor.runOnConcurrent(runnable);
        } else {
            LogicRouterThreadPool.run(runnable);
        }
    }



    /* ======================================================= */
    /* Inner Class                                             */
    /* ======================================================= */

    public interface IConcurrentExecutor {

        void runOnConcurrent(Runnable runnable);

    }
}