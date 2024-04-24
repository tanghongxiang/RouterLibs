package com.thx.logicroutermodule.inner;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
public class LogicRouterThreadPool {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    private static ThreadPoolExecutor sExecutor = new ThreadPoolExecutor(1,
            5, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());



    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    public static void run(Runnable runnable) {
        sExecutor.execute(runnable);
    }

}