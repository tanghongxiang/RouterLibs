package com.thx.logicroutermodule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
public final class LogicResult {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    public @ILogicHandler.LogicResultCode
    int code;

    public @Nullable
    Object data;

    /**
     * Logic 的唯一 ID。
     * 当无法实例化Logic时，该值为 null 。
     */
    public @Nullable
    String uuid;



    /* ======================================================= */
    /* Constructors                                            */
    /* ======================================================= */

    public LogicResult(@ILogicHandler.LogicResultCode int code, @Nullable Object data, @Nullable String uuid) {
        this.code = code;
        this.data = data;
        this.uuid = uuid;
    }



    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    public boolean isSuccess() {
        return code == ILogicHandler.CODE_SUCCESS;
    }

    /**
     * 业务执行失败、路由处理错误都算作失败
     */
    public boolean isFailure() {
        return code == ILogicHandler.CODE_FAILURE
                || code == ILogicHandler.CODE_PARAMS_INVALID
                || code == ILogicHandler.CODE_NO_SUCH_LOGIC
                || code == ILogicHandler.CODE_NOT_VALID_SYNC_LOGIC;
    }

    public boolean isUpdate() {
        return code == ILogicHandler.CODE_UPDATE;
    }

    @Nullable
    public <T> T getData() {
        //noinspection unchecked
        return (T) data;
    }

    /**
     * 当已经知道data不为null时，可以调用这个方法避免一次判空
     */
    @NonNull
    public <T> T getDataNonnull() {
        //noinspection unchecked
        return (T) data;
    }
}

