package com.thx.commonlibrary.logic

import com.thx.logicroutermodule.LogicRouter

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/4/23 6:48 PM
 */
class LogicUse {

    /** 获取网络请求公共Header---同步 */
    val GET_OKHTTP_COMMON_HEADERS = "commonLibrary-网络请求配置-获取网络请求的公共header配置"

    /** 获取网络请求公共参数---同步 */
    val GET_OKHTTP_COMMON_PARAMS = "commonLibrary-网络请求配置-获取网络请求的公共参数"

    /** 处理网络请求返回数据---同步 */
    val PROCESS_HTTP_BACK_RESPONSE_CONTENT = "commonLibrary-网络请求配置-全局处理网络请求返回数据"

    /** 网络请求-判断当前请求的token是否不可用---同步 */
    val CHECK_REQUEST_TOKEN_USABLE_FOR_REQUEST =
        "commonLibrary-网络请求-判断当前请求的token是否不可用"

    /** 网络请求-获取---异步 */
    val GET_NEW_REQUEST_TOKEN_FOR_NET_REQUEST = "commonLibrary-网络请求-获取新的网络请求token"

    companion object {
        val mInstance: LogicUse by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            LogicUse()
        }
    }

    /**
     * 获取网络请求公共Header
     */
    fun getHttpPublicHeaders(): MutableMap<String, String> {
        val headers =
            LogicRouter.syncExecute(GET_OKHTTP_COMMON_HEADERS).data ?: return mutableMapOf()
        if (headers !is MutableMap<*, *>) return mutableMapOf()
        return try {
            headers as MutableMap<String, String>
        } catch (e: Exception) {
            e.printStackTrace()
            mutableMapOf()
        }
//        getUserLoginInfo()?.let {
//            headers["Access-User-Id"] = it.id
//            headers["Access-Token"] = it.tokens.access_token
//        }
//        headers["hupoversion"] = ManifestUtil.getVersionName(BaseApplication.getInstance())
//        headers["appName"] = "collector"
    }

    /**
     * 获取网络请求公共参数
     */
    fun getHttpPublicParams(): MutableMap<String, String> {
        val params = LogicRouter.syncExecute(GET_OKHTTP_COMMON_PARAMS).data ?: return mutableMapOf()
        if (params !is MutableMap<*, *>) return mutableMapOf()
        return try {
            params as MutableMap<String, String>
        } catch (e: Exception) {
            e.printStackTrace()
            mutableMapOf()
        }
//        if (request.paramMap != null && request.paramMap.isNotEmpty()) {
//            request.paramMap["appid"] = "hupoapp"
//            request.paramMap["signtime"] = "${Date().time}"
//            request.paramMap["platform"] = "android"
//            request.paramMap["hupoversion"] =
//                ManifestUtil.getVersionName(BaseApplication.getInstance())
//            val keys = request.paramMap.keys.sortedBy { it }
//            val totalParamsStr = StringBuilder()
//            keys.forEach {
//                totalParamsStr.append("&$it=${URLEncoder.encode(request.paramMap[it])}")
//            }
//            totalParamsStr.delete(0, 1).append("63b19e86485b12f17f48980a97f6dfa0")
//            val result = SignUtil.GetMD5Code(totalParamsStr.toString())
//            request.paramMap["sign"] = result
//        }
    }

    /**
     * 处理接口返回数据
     */
    fun processHttpBackResponseContent(originalData: String): String {
        return LogicRouter.syncExecute(PROCESS_HTTP_BACK_RESPONSE_CONTENT).data?.toString()
            ?: originalData
    }

    /**
     * 获取网络请求的token是否不可用
     * @return true:不可用 false:可用
     */
    fun getHttpRequestTokenUsable(): Boolean {
        return true == LogicRouter.syncExecute(CHECK_REQUEST_TOKEN_USABLE_FOR_REQUEST).data
    }

    /**
     * 获取新的网络请求的token
     * 参数和业务处理让外层处理
     */
    fun getNewHttpRequestToken(callBack: (Boolean) -> Unit) {
        LogicRouter.asynExecute(GET_NEW_REQUEST_TOKEN_FOR_NET_REQUEST) { result ->
            callBack.invoke(result.isSuccess)
        }
    }

}