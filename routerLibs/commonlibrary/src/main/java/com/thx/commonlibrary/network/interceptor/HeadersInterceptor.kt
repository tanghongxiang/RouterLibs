package com.thx.commonlibrary.network.interceptor

import com.thx.commonlibrary.logic.LogicUse
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.internal.http.HttpMethod

/**
 * @Description:请求头全局统一参数
 * @Author: tanghongxiang
 * @Version: V1.00
 * @since 2021/11/8 10:28 上午
 */
class HeadersInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val headers: MutableMap<String, String> = getCommonHeader()
        if (HttpMethod.permitsRequestBody(chain.request().method())) {
            headers["Content-Type"] = "application/json;charset=UTF-8"
        }
        if (headers.isEmpty()) {
            return chain.proceed(builder.build())
        }
        try {
            for ((key, value) in headers) {
                //去除重复的header
                builder.header(key, value).build()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return chain.proceed(builder.build())
    }

    private fun getCommonHeader(): MutableMap<String, String> {
        val headers: MutableMap<String, String> = LinkedHashMap()
        val selfHeadersParams = LogicUse.mInstance.getHttpPublicHeaders()
        if (selfHeadersParams.isNotEmpty()) {
            selfHeadersParams.forEach {
                headers[it.key] = it.value
            }
        }
        return headers
    }


}