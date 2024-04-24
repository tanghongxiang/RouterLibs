package com.thx.commonlibrary

import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.thx.anynetworkmodule.AnyNetwork
import com.thx.anynetworkmodule.AnyNetworkManager
import com.thx.commonlibrary.network.AnyNetworkService
import com.thx.commonlibrary.network.NetworkConfig

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
class AppLauncher : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onAppCreate() {
        if (isMainProcess()) {
            val config = NetworkConfig()
//            config.DEBUG = BuildConfig.DEBUG
            config.https = false
            config.timeOut = 15
//            config.baseUrl = Constants.SERVER_DEBUG_URL
            val service = AnyNetworkService(config)
            val anyNetwork = AnyNetwork()
            anyNetwork.setService(service)
            AnyNetworkManager.getInstance().globalAnyNetWork = anyNetwork
        }
    }

    private fun isMainProcess(): Boolean {
        return Looper.getMainLooper().thread == Thread.currentThread()
    }
}