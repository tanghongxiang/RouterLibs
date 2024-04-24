package com.thx.commonlibrary.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
public interface RetrofitNetworkServiceApi {

    /**
     * 普通GET请求
     */
    @GET
    fun get(@Url url: String, @QueryMap map: Map<String, String>): Call<ResponseBody>

    /** 用于流式文件下载的get请求*/
    @Streaming
    @GET
    fun streamGet(@Url url: String, @QueryMap map: Map<String, String>): Call<ResponseBody>

    /**
     * 普通POST表单请求
     */
    @FormUrlEncoded
    @POST
    fun post(@Url url: String, @FieldMap map: Map<String, String>): Call<ResponseBody>

    /**
     * 普通POST表单请求-带Header
     */
    @FormUrlEncoded
    @POST
    fun post(@Url url: String, @FieldMap map: Map<String, String>,@HeaderMap headers:Map<String,String>): Call<ResponseBody>

    /** 用于流式文件下载的post请求*/
    @Streaming
    @FormUrlEncoded
    @POST
    fun streamPost(@Url url: String, @FieldMap map: Map<String, String>): Call<ResponseBody>

    /**
     * POST请求带RequestBody
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST
    fun postWithBody(
        @Url url: String,
        @Body data: RequestBody
    ): Call<ResponseBody>

    /**
     * POST请求带RequestBody-带Header
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST
    fun postWithBody(
        @Url url: String,
        @Body data: RequestBody,
        @HeaderMap headers:Map<String,String>
    ): Call<ResponseBody>

    /**
     * post-文件上传
     */
    @Multipart
    @POST
    fun multipart(@Url url: String, @Part parts: List<MultipartBody.Part>): Call<ResponseBody>

    /**
     * 普通DELETE请求
     */
    @FormUrlEncoded
    @HTTP(method = "DELETE", hasBody = true)
    fun delete(
        @Url url: String,
        @FieldMap map: Map<String, String>
    ): Call<ResponseBody>

    /**
     * 普通DELETE请求-带Header
     */
    @FormUrlEncoded
    @HTTP(method = "DELETE", hasBody = true)
    fun delete(
        @Url url: String,
        @FieldMap map: Map<String, String>,@HeaderMap headers:Map<String,String>
    ): Call<ResponseBody>

    /**
     * DELETE请求带RequestBody
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @HTTP(method = "DELETE", hasBody = true)
    fun deleteWithBody(
        @Url url: String,
        @Body data: RequestBody
    ): Call<ResponseBody>

    /**
     * DELETE请求带RequestBody-带Header
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @HTTP(method = "DELETE", hasBody = true)
    fun deleteWithBody(
        @Url url: String,
        @Body data: RequestBody,@HeaderMap headers:Map<String,String>
    ): Call<ResponseBody>

    /**
     * 普通PUT请求
     */
    @FormUrlEncoded
    @PUT
    fun put(
        @Url url: String,
        @FieldMap map: Map<String, String>
    ): Call<ResponseBody>

    /**
     * 普通PUT请求-带Header
     */
    @FormUrlEncoded
    @PUT
    fun put(
        @Url url: String,
        @FieldMap map: Map<String, String>,@HeaderMap headers:Map<String,String>
    ): Call<ResponseBody>

    /**
     * PUT请求带RequestBody
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @PUT
    fun putWithBody(
        @Url url: String,
        @Body data: RequestBody
    ): Call<ResponseBody>

    /**
     * PUT请求带RequestBody-带Header
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @PUT
    fun putWithBody(
        @Url url: String,
        @Body data: RequestBody,@HeaderMap headers:Map<String,String>
    ): Call<ResponseBody>

}