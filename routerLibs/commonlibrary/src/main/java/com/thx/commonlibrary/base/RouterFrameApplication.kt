package com.thx.commonlibrary.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.webkit.WebStorage
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.thx.commonlibrary.AppLauncher
import java.util.Stack


/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
open class RouterFrameApplication : Application(), LifecycleOwner {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    private var mLifecycleRegistry: LifecycleRegistry? = null

    private var activityList: Stack<Activity> = Stack()

    private var mAppLauncher: AppLauncher? = null

    companion object {
        var mInstance: RouterFrameApplication? = null
        fun getInstance(): RouterFrameApplication {
            if (mInstance != null) {
                return mInstance!!
            } else {
                throw IllegalArgumentException("Application is null")
            }
        }
    }


    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        mLifecycleRegistry = LifecycleRegistry(this)
        mLifecycleRegistry?.markState(Lifecycle.State.CREATED)
        registerLifecycle()
        initLaunch()
    }

    private fun registerLifecycle() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {
                this@RouterFrameApplication.onActivityPaused(activity)
            }

            override fun onActivityStarted(activity: Activity) {
                this@RouterFrameApplication.onActivityStarted(activity)
            }

            override fun onActivityDestroyed(activity: Activity) {
                activityList.remove(activity)
                this@RouterFrameApplication.onActivityDestroyed(activity)
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                this@RouterFrameApplication.onActivitySaveInstanceState(activity, outState)
            }

            override fun onActivityStopped(activity: Activity) {
                this@RouterFrameApplication.onActivityStopped(activity)
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activityList.add(activity)
                this@RouterFrameApplication.onActivityCreated(activity, savedInstanceState)
            }

            override fun onActivityResumed(activity: Activity) {
                this@RouterFrameApplication.onActivityResumed(activity)
            }

        })
    }

    override fun getLifecycle(): Lifecycle {
        return mLifecycleRegistry!!
    }


    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    /**
     * 初始化网络框架
     */
    private fun initLaunch() {
        mAppLauncher?.let {
            mLifecycleRegistry?.removeObserver(it)
        }
        this.mAppLauncher = AppLauncher()
        mAppLauncher?.let {
            mLifecycleRegistry?.addObserver(it)
        }
    }


    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    /**
     * 获取当前activity任务栈
     */
    fun getActivityStack(): Stack<Activity> = activityList

    /**
     * 清除activity任务栈所有Activity
     */
    fun clearStackAllActivity() {
        WebStorage.getInstance().deleteAllData()
        for (item in activityList) item.finish()
    }

    /**
     * 获取activity栈最上层的Activity
     */
    fun topActivity(): Activity? {
        if (activityList.isEmpty()) return null
        return activityList.lastElement()
    }

    /**
     * 获取activity栈最底层的Activity
     */
    fun btmActivity(): Activity? {
        if (activityList.isEmpty()) return null
        return activityList.firstElement()
    }


    /* ======================================================= */
    /* As requested to Override                                */
    /* ======================================================= */

    /** Activity栈回调-onActivityPaused-根据需求重写，增加处理逻辑 */
    fun onActivityPaused(activity: Activity) {}

    /** Activity栈回调-onActivityStarted-根据需求重写，增加处理逻辑 */
    fun onActivityStarted(activity: Activity) {}

    /** Activity栈回调-onActivityDestroyed-根据需求重写，增加处理逻辑 */
    fun onActivityDestroyed(activity: Activity) {}

    /** Activity栈回调-onActivitySaveInstanceState-根据需求重写，增加处理逻辑 */
    fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    /** Activity栈回调-onActivityStopped-根据需求重写，增加处理逻辑 */
    fun onActivityStopped(activity: Activity) {}

    /** Activity栈回调-onActivityCreated-根据需求重写，增加处理逻辑 */
    fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    /** Activity栈回调-onActivityResumed-根据需求重写，增加处理逻辑 */
    fun onActivityResumed(activity: Activity) {}

}