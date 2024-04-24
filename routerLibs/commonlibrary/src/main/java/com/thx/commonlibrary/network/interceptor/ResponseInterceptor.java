package com.thx.commonlibrary.network.interceptor;

import android.os.Handler;
import android.os.Looper;

import com.thx.commonlibrary.base.RouterFrameApplication;
import com.thx.commonlibrary.logic.LogicUse;
import com.thx.commonlibrary.utils.ToastUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @Description:
 * @Author: tanghongxiang
 * @Version: V1.00
 * @since 2021/10/26 9:43 上午
 */
public class ResponseInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        if (response.code() != 200) {
            return new Response.Builder()
                    .code(500)
                    .addHeader("Content-Type", "application/json")
                    .body(ResponseBody.create(MediaType.parse("application/json"), ""))
                    .message("")
                    .request(chain.request())
                    .protocol(Protocol.HTTP_2)
                    .build();
        }

        ResponseBody cloneResponseBody = response.newBuilder().build().body();
        String responseStr = "";
        if (cloneResponseBody != null) {
            responseStr = cloneResponseBody.string();
        }
        // 处理返回数据
        responseStr = LogicUse.Companion.getMInstance().processHttpBackResponseContent(responseStr);
        ResponseBody newResponseBody = ResponseBody.create(cloneResponseBody.contentType(), responseStr);
        return response.newBuilder().body(newResponseBody).build();


        // 解密操作
//        ResponseBody cloneResponseBody = response.newBuilder().build().body();
//        String responseStr = "";
//        if (cloneResponseBody != null) {
//            responseStr = cloneResponseBody.string();
//        }
//        JSONObject responseJsonObj = null;
//        try {
//            responseJsonObj = new JSONObject(responseStr);
//            String offset = responseJsonObj.optString("tyg", "");
//            String cipherText = responseJsonObj.optString("cipherText", "");
//            if (!TextUtils.isEmpty(offset) && !TextUtils.isEmpty(cipherText)) {
//                responseStr = AESEncryptionUtils.decrypt(cipherText, offset);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        ResponseBody newResponseBody = ResponseBody.create(cloneResponseBody.contentType(), responseStr);
//        return response.newBuilder().body(newResponseBody).build();
    }

}
