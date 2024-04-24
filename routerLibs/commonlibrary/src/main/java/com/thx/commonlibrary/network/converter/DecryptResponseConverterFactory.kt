package com.thx.commonlibrary.network.converter

import com.thx.anynetworkmodule.AnyResponse
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
class DecryptResponseConverterFactory() : Converter.Factory() {

    companion object {
        fun create(): DecryptResponseConverterFactory {
            return DecryptResponseConverterFactory()
        }
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        if (type === AnyResponse::class.java) {
            return DecryptResponseBodyConverter()
        }
        return super.responseBodyConverter(type, annotations, retrofit)
    }


}