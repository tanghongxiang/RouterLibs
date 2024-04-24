package com.thx.commonlibrary.network.converter

import com.alibaba.fastjson.JSON
import com.thx.anynetworkmodule.AnyResponse
import okhttp3.ResponseBody
import retrofit2.Converter

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
class DecryptResponseBodyConverter : Converter<ResponseBody, AnyResponse> {

    override fun convert(value: ResponseBody): AnyResponse? {
        var responseInfo = AnyResponse()
        try {
            responseInfo = JSON.parseObject(value.string(), AnyResponse::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            responseInfo.isSuccess = false
            responseInfo.message = "JSON格式转换异常！"
            responseInfo.code = "-2"
        }
        return responseInfo
    }
}