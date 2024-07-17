package com.thx.logicroutermodule.logic

import com.thx.logicroutermodule.LogicRouter
import java.util.HashMap

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/4/23 6:48 PM
 */

/** 网络请求-处理Logic shouldRun校验不通过 的提示内容---同步 */
const val PROCESS_SHOULD_RUN_CHECK_UN_PASS_CONTENT =
    "commonLibrary-网络请求-处理Logic shouldRun校验不通过 的提示内容---同步"


/**
 * 网络请求-处理Logic shouldRun校验不通过 的提示内容---同步
 */
fun processUnPassShouldRunErrMsg(async: Boolean): String? {
    val map = HashMap<String, Boolean>()
    map.put("async", async)
    return LogicRouter.syncExecute(PROCESS_SHOULD_RUN_CHECK_UN_PASS_CONTENT, map).data?.toString()
}