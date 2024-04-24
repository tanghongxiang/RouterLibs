package com.thx.commonlibrary.network

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import com.thx.anynetworkmodule.AnyResponse
import com.thx.anynetworkmodule.IAnyAsyncCallback
import java.lang.reflect.ParameterizedType


/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
abstract class AnyAsyncCallback<T> : IAnyAsyncCallback {

    override fun onSuccess(res: String?) {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        var clazz: Class<T>? = null
        try {
            if (type is Class<*>) {
                clazz = type as Class<T>
            }
            if (clazz == String::class.java) {
                val data: Any? = res
                if (data == null) {
                    onRequestSuccess(null)
                } else {
                    onRequestSuccess(data.toString() as T)
                }
            } else {
                if (res == null) {
                    onRequestFailure(Throwable("处理数据异常！"))
                } else {
                    val mTypeReference = object : TypeReference<T>() {}.type
                    onRequestSuccess(JSON.parseObject(res, mTypeReference))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onRequestFailure(e)
        }
    }

    override fun onFailure(throwable: Throwable?) {
        onRequestFailure(throwable)
    }

    abstract fun onRequestSuccess(response: T?)

    abstract fun onRequestFailure(throwable: Throwable?)
}