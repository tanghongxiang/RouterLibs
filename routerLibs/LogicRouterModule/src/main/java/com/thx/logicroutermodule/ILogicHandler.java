package com.thx.logicroutermodule;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
public interface ILogicHandler {
    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    /**
     * 其他结果
     */
    int CODE_OTHER = 0;

    /**
     * 没有找到指定的 Logic 实现类
     */
    int CODE_NO_SUCH_LOGIC = 1;

    /**
     * 不是一个有效的同步Logic
     */
    int CODE_NOT_VALID_SYNC_LOGIC = 2;

    /**
     * 参数不合法。Logic 的 shouldRun 返回 false 时出现。
     *
     * @see ILogic#shouldRun()
     */
    int CODE_PARAMS_INVALID = 3;

    /**
     * 执行成功
     */
    int CODE_SUCCESS = 4;

    /**
     * 执行失败
     */
    int CODE_FAILURE = 5;

    /**
     * 数据更新。对于有的 Logic，会不断产生新的数据，调用方也是在监听这些中间数据。
     * 这时需要使用该值。
     */
    int CODE_UPDATE = 6;



    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    /**
     * Logic 执行完成时回调。
     *
     * @param result 执行的结果
     */
    void onResponse(@NonNull LogicResult result);




    /* ======================================================= */
    /* Inner Class                                             */
    /* ======================================================= */

    @IntDef({CODE_OTHER, CODE_NO_SUCH_LOGIC, CODE_NOT_VALID_SYNC_LOGIC,
            CODE_PARAMS_INVALID, CODE_SUCCESS, CODE_FAILURE, CODE_UPDATE})
    @Retention(RetentionPolicy.SOURCE)
    @interface LogicResultCode {
    }
}
