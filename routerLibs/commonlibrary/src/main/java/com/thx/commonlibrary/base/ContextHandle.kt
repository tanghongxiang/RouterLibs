package com.thx.commonlibrary.base

import android.content.Intent
import java.util.*

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
class ContextHandle {


    private var activityStack: Stack<CustomActivity> = Stack()

    companion object {
        val instance: ContextHandle by lazy { ContextHandle() }
    }

    /**
     * 添加Activity
     */
    fun addActivity(activity: CustomActivity) {
        activityStack.add(activity)
    }

    fun size(): Int {
        return activityStack.size
    }

    /**
     * 移出存在的Activity
     */
    fun removeActivity(activity: CustomActivity) {
        if (activityStack.contains(activity)) {
            activity.finish()
            activityStack.remove(activity)
        }
    }

    /**
     * 获取最上面的Activity
     */
    fun getTopActivity(): CustomActivity {
        return activityStack.lastElement()
    }

    /**
     * 移除指定activity
     */
    fun finishAppointActivity(activityClass: Class<*>) {
        if (activityStack == null) {
            activityStack = Stack<CustomActivity>()
        }
        val iterator = activityStack.listIterator()
        while (iterator.hasNext()) {
            val activity = iterator.next()
            if (activity::class.java === activityClass) {
                iterator.remove()
                activity.finish()
            }
        }
    }

    /**
     * 移除特殊组的Activity
     */
    fun goForwardFinishAllGroup(activityClass: Class<*>?) {
        if (ContextHandle().activityStack == null) {
            ContextHandle().activityStack =
                Stack<CustomActivity>()
        }
        if (ContextHandle().activityStack.size > 0) {
            ContextHandle().goForward(activityClass!!)
            val iterator: MutableIterator<CustomActivity> =
                ContextHandle().activityStack.listIterator()
            while (iterator.hasNext()) {
                val activity: CustomActivity = iterator.next()
                iterator.remove()
                activity.finish()
            }
        }

    }


    /**
     * 获得当前运行的Activity
     */
    fun currentActivity(): CustomActivity? {
        return if (activityStack != null && !activityStack.isEmpty()) activityStack.peek() else null
    }


    /**
     * 移除最后的Activity
     */
    fun finish() {
        val abstractActivity = currentActivity()
        if (abstractActivity == null) {
            return
        }
        if (abstractActivity != null) {
            abstractActivity!!.finish()
        }
        if (activityStack != null && !activityStack.isEmpty()) {
            activityStack.pop()
        }
    }

    /**
     * 跳转界面，可选择参数为关闭标志（boolean），传递状态（State）
     */
    fun goForward(activityClass: Class<*>, vararg objs: Any?) {

        val currentActivity: CustomActivity

        currentActivity = this!!.currentActivity()!!

        if (currentActivity == null) {
            return
        }
        val intent = Intent(currentActivity, activityClass)

        //打开activity
        currentActivity.startActivity(intent)
//        currentActivity.overridePendingTransition(R.anim.slide_right_in, R.anim.activity_nomal)
    }


    /**
     * 清除Activity栈
     */
    fun clearActivity() {
        for (activity in activityStack) {
            activity.finish()
        }
        activityStack.clear()
    }

}
