package com.thx.commonlibrary.network.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
open class MainThreadExecutor() : Executor {

    val handler: Handler = Handler(Looper.getMainLooper())

    override fun execute(command: Runnable?) {
        command?.let {
            handler.post(it)
        }
    }
}