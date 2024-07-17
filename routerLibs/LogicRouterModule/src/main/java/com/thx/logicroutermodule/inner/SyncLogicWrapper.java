package com.thx.logicroutermodule.inner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.thx.logicroutermodule.BaseAsynLogic;
import com.thx.logicroutermodule.BaseSyncLogic;
import com.thx.logicroutermodule.ILogicHandler;
import com.thx.logicroutermodule.LogicResult;
import com.thx.logicroutermodule.LogicRouter;
import com.thx.logicroutermodule.logic.LogicUse;

import java.util.Map;

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
public class SyncLogicWrapper extends BaseAsynLogic {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    private BaseSyncLogic mSyncLogic;



    /* ======================================================= */
    /* Constructors                                            */
    /* ======================================================= */

    /*package*/ SyncLogicWrapper(BaseSyncLogic syncLogic) {
        mSyncLogic = syncLogic;
    }



    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    /**
     * 启动一个 Logic。
     * <br/> 该方法由 LogicRouter 内部调用。子类不要实现和调用。
     *
     * @param params  传入的参数。
     * @param handler 回调
     */
    public final void startCompat(@Nullable Map params, @Nullable ILogicHandler handler) {
        if (params != null) {
            setParams(params);
        }

        if (!shouldRun()) {
            String res = LogicUse.processUnPassShouldRunErrMsg(false);
            markResult(ILogicHandler.CODE_PARAMS_INVALID, res);
            return;
        }

        LogicRouter.runOnConcurrent(() -> {
            LogicResult result = mSyncLogic.run();
            if (handler != null) {
                handler.onResponse(result);
            }
        });
    }

    /**
     * 启动一个 Logic。
     * <br/> 该方法由 LogicRouter 内部调用。子类不要实现和调用。
     *
     * @param params  传入的参数。
     * @param handler 回调
     */
    public final void startCompat(@Nullable String params, @Nullable ILogicHandler handler) {
        if (params != null) {
            setParams(params);
        }

        if (!shouldRun()) {
            String res = LogicUse.processUnPassShouldRunErrMsg(false);
            markResult(ILogicHandler.CODE_PARAMS_INVALID, res);
            return;
        }

        LogicRouter.runOnConcurrent(() -> {
            LogicResult result = mSyncLogic.run();
            if (handler != null) {
                handler.onResponse(result);
            }
        });
    }


    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    @Override
    public void tryStop() {
        mSyncLogic.tryStop();
    }

    @Override
    public void updateParams(@Nullable Map updateParams) {
        mSyncLogic.updateParams(updateParams);
    }

    @Override
    public String uuid() {
        return mSyncLogic.uuid();
    }

    @Override
    public boolean isRunning() {
        return mSyncLogic.isRunning();
    }

    @Override
    public boolean shouldRun() {
        return mSyncLogic.shouldRun();
    }

    @Override
    public boolean ignoreException() {
        return mSyncLogic.ignoreException();
    }

    @Override
    public void setParams(@NonNull Map params) {
        mSyncLogic.setParams(params);
    }

    @Override
    public void setParams(@NonNull String requestBody) {
        mSyncLogic.setParams(requestBody);
    }

    @Override
    public void setPathParams(@NonNull Map params) {
        mSyncLogic.setPathParams(params);
    }

    @Override
    public void run() {
        throw new IllegalStateException("SyncLogicWrapper#run() 方法不应该被执行。而是应该执行 mSyncLogic#run() 。");
    }
}
