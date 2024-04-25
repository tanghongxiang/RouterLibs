package com.thx.commonlibrary.network

import android.content.pm.ApplicationInfo
import com.thx.anynetworkmodule.AnyConfig
import com.thx.anynetworkmodule.AnyRequest
import com.thx.anynetworkmodule.AnyRequestId
import com.thx.anynetworkmodule.AnyResponse
import com.thx.anynetworkmodule.IAnyAsyncCallback
import com.thx.anynetworkmodule.IAnyAsyncProgressCallback
import com.thx.anynetworkmodule.IAnyServices
import com.thx.commonlibrary.base.RouterFrameApplication
import com.thx.commonlibrary.logic.LogicUse
import com.thx.commonlibrary.network.BaseNetworkLogic.DELETE
import com.thx.commonlibrary.network.BaseNetworkLogic.DELETE_BODY
import com.thx.commonlibrary.network.BaseNetworkLogic.GET
import com.thx.commonlibrary.network.BaseNetworkLogic.MULTIPART
import com.thx.commonlibrary.network.BaseNetworkLogic.POST
import com.thx.commonlibrary.network.BaseNetworkLogic.POST_BODY
import com.thx.commonlibrary.network.BaseNetworkLogic.PUT
import com.thx.commonlibrary.network.BaseNetworkLogic.PUT_BODY
import com.thx.commonlibrary.network.interceptor.HeadersInterceptor
import com.thx.commonlibrary.network.interceptor.ResponseInterceptor
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
open class AnyNetworkService(config: AnyConfig?) : IAnyServices {
    private var service: RetrofitNetworkServiceApi? = null
    private var config: AnyConfig? = null

    init {
        this.config = config
        val builder: OkHttpClient.Builder =
            OkHttpClient.Builder().connectTimeout(config?.timeOut ?: 30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS)
        val applicationInfo = RouterFrameApplication.getInstance().applicationInfo
        if (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE !== 0) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(logging)
        }
        builder.addInterceptor(HeadersInterceptor())
        builder.addInterceptor(ResponseInterceptor())
//        if (!BuildConfig.DEBUG) {
//            builder.proxy(Proxy.NO_PROXY)
//        }
//        val spec=ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
        val okHttpClient = builder.build()
        okHttpClient.dispatcher().maxRequests = 64
        okHttpClient.dispatcher().maxRequestsPerHost = 10
        val retrofit = Retrofit.Builder().baseUrl("http://funny.frame.com")
//            .addConverterFactory(DecryptResponseConverterFactory.create())
            .client(okHttpClient).build()
        this.service = retrofit.create(RetrofitNetworkServiceApi::class.java)
    }

    override fun setConfig(configs: AnyConfig?) {
        throw IllegalArgumentException("请用构造函数初始化Config！")
    }


    override fun asyncRequest(request: AnyRequest?, callback: IAnyAsyncCallback?): AnyRequestId? {
        return asyncRequest(request, null, null, callback)
    }

    override fun asyncRequest(
        requestt: AnyRequest?,
        filePath: String?,
        progress: IAnyAsyncProgressCallback?,
        callback: IAnyAsyncCallback?
    ): AnyRequestId? {
        var call: Call<*>? = null
        val request = requestt ?: return null
        val commonParams = LogicUse.mInstance.getHttpPublicParams(request.paramMap)
        if (commonParams.isNotEmpty()) {
            commonParams.forEach {
                request.paramMap[it.key] = it.value
            }
        }

        when (request.requestType) {
            GET -> {
                call = if (request.isFileDownloadType) {
                    service?.streamGet(request.url, request.paramMap)
                } else {
                    service?.get(request.url, request.paramMap)
                }
            }

            POST -> {
                call = if (request.isFileDownloadType) {
                    service?.streamPost(request.url, request.paramMap)
                } else if (request.headersMap == null || request.headersMap.isEmpty()) {
                    service?.post(request.url, request.paramMap)
                } else {
                    service?.post(request.url, request.paramMap, request.headersMap)
                }
            }

            MULTIPART -> {
                val parts = ArrayList<MultipartBody.Part>()
                if (request.paramMap.isNotEmpty()) {
                    for (key in request.paramMap.keys) {
                        parts.add(MultipartBody.Part.createFormData(key, request.paramMap[key]))
                    }
                }
                if (request.fileMap.isNotEmpty()) {
                    for (key in request.fileMap.keys) {
                        val requestFile = RequestBody.create(
                            MediaType.parse("multipart/form-data"), request.fileMap[key]
                        )
                        parts.add(
                            MultipartBody.Part.createFormData(
                                key, request.fileMap[key]?.name ?: "*", requestFile
                            )
                        )
                    }
                }
                call = service?.multipart(request.url, parts)
            }

            POST_BODY -> {
                call = if (request.headersMap == null || request.headersMap.isEmpty()) {
                    service?.postWithBody(request.url, request.requestBody)
                } else {
                    service?.postWithBody(request.url, request.requestBody, request.headersMap)
                }
            }

            PUT -> {
                call = if (request.headersMap == null || request.headersMap.isEmpty()) {
                    service?.put(request.url, request.paramMap)
                } else {
                    service?.put(request.url, request.paramMap, request.headersMap)
                }
            }

            PUT_BODY -> {
                call = if (request.headersMap == null || request.headersMap.isEmpty()) {
                    service?.putWithBody(request.url, request.requestBody)
                } else {
                    service?.putWithBody(request.url, request.requestBody, request.headersMap)
                }
            }

            DELETE -> {
                call = if (request.headersMap == null || request.headersMap.isEmpty()) {
                    service?.delete(request.url, request.paramMap)
                } else {
                    service?.delete(request.url, request.paramMap, request.headersMap)
                }
            }

            DELETE_BODY -> {
                call = if (request.headersMap == null || request.headersMap.isEmpty()) {
                    service?.deleteWithBody(request.url, request.requestBody)
                } else {
                    service?.deleteWithBody(request.url, request.requestBody, request.headersMap)
                }
            }

        }
        if (request.isFileDownloadType) {
            //TODO ...
        } else {
            if (call == null) return null
            val newCall = call as Call<ResponseBody>
            newCall.enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    callback?.onFailure(t)
                }

                override fun onResponse(
                    call: Call<ResponseBody>, response: Response<ResponseBody>
                ) {

                    val mCallback = callback ?: return
                    if (response.isSuccessful) {
                        val anyResponse = response.body() ?: return
                        mCallback.onSuccess(anyResponse.string())
                    } else {
                        mCallback.onFailure(
                            Throwable(
                                response.errorBody()?.string() ?: "网络异常~"
                            )
                        )
                    }
                }
            })
        }
        return MyRequestId(call)
    }

    override fun syncRequest(request: AnyRequest?): AnyResponse? {
        return null
    }

    override fun cancelRequest(requestId: AnyRequestId?): Boolean {
        if (requestId is MyRequestId) {
            try {
                requestId.getCall()?.cancel()
            } catch (e: Exception) {
                return false
            }
        }
        return false
    }


}