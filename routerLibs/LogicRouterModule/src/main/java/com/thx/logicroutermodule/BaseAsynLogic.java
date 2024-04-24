package com.thx.logicroutermodule;

import androidx.annotation.Nullable;

import java.util.Map;

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
public abstract class BaseAsynLogic extends AbstractLogic {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    protected boolean isShouldStopped;

    protected ILogicHandler mHandler;

    /**
     * 是否已经调用过 {@link #markResult(int, Object)} 了。
     */
    protected boolean mIsMarked;



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    /**
     * 启动一个 Logic。
     * <br/> 该方法由 LogicRouter 内部调用。子类不要实现和调用。
     *
     * @param params  传入的参数。
     * @param handler 回调
     */
    public final void start(@Nullable Map params,@Nullable Map pathParams, @Nullable ILogicHandler handler) {
        if (params != null) {
            setParams(params);
        }
        if(pathParams!=null){
            setPathParams(pathParams);
        }
        mHandler = handler;

        // 检查参数
        if (!shouldRun()) {
            LogicRouter.runOnConcurrent(() -> markResult(ILogicHandler.CODE_PARAMS_INVALID, "网络不稳定~"));
            return;
        }

        // 运行
        run();
    }

    /**
     * 启动一个 Logic。
     * <br/> 该方法由 LogicRouter 内部调用。子类不要实现和调用。
     *
     * @param body  传入的参数。
     * @param handler 回调
     */
    public final void startWithBody(@Nullable String body,@Nullable Map pathParams, @Nullable ILogicHandler handler) {
        if (body != null) {
            setParams(body);
        }
        if(pathParams!=null){
            setPathParams(pathParams);
        }
        mHandler = handler;

        // 检查参数
        if (!shouldRun()) {
            LogicRouter.runOnConcurrent(() -> markResult(ILogicHandler.CODE_PARAMS_INVALID, "网络不稳定~"));
            return;
        }

        // 运行
        run();
    }

    @Override
    public void tryStop() {
        isShouldStopped = true;
    }



    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    /**
     * 实现类在这个方法中处理具体的业务逻辑
     */
    public abstract void run();

    /**
     * 标记当前 Logic 的执行结果
     */
    protected final void markResult(@ILogicHandler.LogicResultCode int code, Object data) {

        if (mIsMarked && !ignoreException()) {
            throw new IllegalStateException("markResult(int, Object) 方法被调用了多次！"
                    + "通常情况这是一种异常，如果因为特殊需求，可以重写 ignoreException() 方法并返回 true 来忽略此异常。");
        }

        LogicResult result = new LogicResult(code, data, uuid());
        if (mHandler != null) {
            mHandler.onResponse(result);
        }

        // 只要不是 「更新」 或 「其他」，都认为是已经处理过了。
        if (code != ILogicHandler.CODE_UPDATE && code != ILogicHandler.CODE_OTHER) {
            mIsMarked = true;
        }
    }
}

