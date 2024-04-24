package com.thx.logicroutermodule;

import static com.thx.logicroutermodule.LogicRouter.sLog;

import androidx.annotation.NonNull;

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
public abstract class BaseSyncLogic extends AbstractLogic {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    /**
     * 是否已经调用过 {@link #markResult} 方法。
     */
    private boolean mIsMarked;



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    /**
     * 实现类在这个方法中处理具体的业务逻辑
     *
     * @return 处理的结果
     */
    @NonNull
    public abstract LogicResult run();

    @Override
    public void tryStop() {
    }



    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    public LogicResult markResult(@ILogicHandler.LogicResultCode int code, Object data) {

        if (mIsMarked && !ignoreException()) {
            throw new IllegalStateException("markResult(int, Object) 方法被调用了多次！"
                    + "通常情况这是一种异常，如果因为特殊需求，可以重写 ignoreException() 方法并返回 true 来忽略此异常。");
        }

        if (code == ILogicHandler.CODE_FAILURE) {
            sLog.w("Logic异常监测", getClass().getSimpleName() + "执行时不成功");
        }

        // 只要不是 「更新」 或 「其他」，都认为是已经处理过了。
        if (code != ILogicHandler.CODE_UPDATE && code != ILogicHandler.CODE_OTHER) {
            mIsMarked = true;
        }

        return new LogicResult(code, data, uuid());
    }
}