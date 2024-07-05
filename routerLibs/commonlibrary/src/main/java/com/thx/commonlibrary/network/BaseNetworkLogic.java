package com.thx.commonlibrary.network;

import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.IntDef;

import com.alibaba.fastjson.JSON;
import com.thx.anynetworkmodule.AnyNetworkManager;
import com.thx.anynetworkmodule.AnyRequest;
import com.thx.anynetworkmodule.AnyRequestId;
import com.thx.anynetworkmodule.NetUtils;
import com.thx.commonlibrary.base.RouterFrameApplication;
import com.thx.commonlibrary.logic.LogicUse;
import com.thx.commonlibrary.network.utils.NetworkExecutor;
import com.thx.logicroutermodule.BaseAsynLogic;
import com.thx.logicroutermodule.ILogicHandler;
import com.thx.logicroutermodule.LogicRouter;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
public abstract class BaseNetworkLogic extends BaseAsynLogic {

    public static final int GET = 0;
    public static final int POST = 1;
    public static final int MULTIPART = 2;
    public static final int POST_BODY = 4;
    public static final int PUT = 5;
    public static final int PUT_BODY = 6;
    public static final int DELETE = 7;
    public static final int DELETE_BODY = 8;

    private AnyRequestId anyRequestId;

    ExecutorService service = NetworkExecutor.getInstance();

    @Override
    public boolean shouldRun() {
        return super.shouldRun() && LogicUse.Companion.getMInstance().getNetworkRequestCanRun();//&& NetUtils.isNetConnected(RouterFrameApplication.Companion.getInstance());
    }

    @Override
    public void run() {
        AnyRequest anyRequest = new AnyRequest();
        anyRequest.setRequestType(requestType());
        anyRequest.setUrl(url());
        Pair<String, Map<String, String>> bodyParams = networkBodyParams();
        Map params = networkParams();
        if (bodyParams != null) {
            anyRequest.setRequestBody(bodyParams.first);
            anyRequest.addHeaders(bodyParams.second);
        } else if (params != null) {
            anyRequest.addParams(params);
        }
        AnyAsyncCallback callback = new AnyAsyncCallback<String>() {

            @Override
            public void onRequestFailure(@Nullable Throwable throwable) {
                BaseNetworkLogic.this.onFailure(throwable);
            }

            @Override
            public void onRequestSuccess(@Nullable String response) {
                service.execute(() -> BaseNetworkLogic.this.onSuccess(response));
            }
        };

        // 检查请求的token是否可用
        if(!LogicUse.Companion.getMInstance().getTokenWhiteList().contains(url())){
            // 走token校验
            if (LogicUse.Companion.getMInstance().getHttpRequestTokenUsable()) {
                // token过期了,去获取新的token
                LogicUse.Companion.getMInstance().getNewHttpRequestToken(new Function2<Boolean, String, Unit>() {
                    @Override
                    public Unit invoke(Boolean success, String newToken) {
                        if(success){
                            // 获取新签名成功，那么就替换当前签名字段
                            anyRequest.getParamMap().put("access_token",newToken);
                            anyRequest.getHeadersMap().put("Access-Token",newToken);
                        }
                        // 成功或者失败都继续请求把，如果token获取失败即接口有问题或者网络异常
                        anyRequestId = AnyNetworkManager.getInstance().getGlobalAnyNetWork().asyncRequest(anyRequest, callback);
                        return null;
                    }
                });
            } else {
                // token未过期，继续请求
                anyRequestId = AnyNetworkManager.getInstance().getGlobalAnyNetWork().asyncRequest(anyRequest, callback);
            }
        }else{
            // 不走token校验
            Log.e("NetworkLogic","不走token校验：" + url());
            anyRequestId = AnyNetworkManager.getInstance().getGlobalAnyNetWork().asyncRequest(anyRequest, callback);
        }
    }


    @Override
    public void tryStop() {
        super.tryStop();
        if (anyRequestId != null) {
            AnyNetworkManager.getInstance().getGlobalAnyNetWork().cancelRequest(anyRequestId);
        }
    }

    private String url() {
        StringBuilder url;
        if (host().endsWith("/") && path().startsWith("/")) {
            url = new StringBuilder(host() + path().substring(1));
        } else if (!host().endsWith("/") && !path().startsWith("/")) {
            url = new StringBuilder(host() + "/" + path());
        } else {
            url = new StringBuilder(host() + path());
        }
        return url.toString();
    }

    /**
     * 请求类型
     *
     * @return GET POST
     */
    @RequestTypeDef
    public abstract int requestType();

    /**
     * 域名
     *
     * @return http://www.baidu.com
     */
    public abstract String host();

    /**
     * 域名之后的路径
     *
     * @return /a/b/c.ajax
     */
    public abstract String path();

    /**
     * 网络请求参数
     *
     * @return key-value形式参数
     */
    public Map networkParams() {
        return new HashMap<String, String>();
    }

    /**
     * 网络请求参数-requestBody
     *
     * @return first:请求体参数  second：请求头参数
     */
    public Pair<String, Map<String, String>> networkBodyParams() {
        return null;
    }

    /**
     * 成功的回调
     *
     * @param obj
     */
    public abstract void onSuccess(String obj);

    /**
     * 失败的回调
     *
     * @param throwable
     */
    public void onFailure(Throwable throwable) {
        String errorMsg = throwable.getMessage();
        if (TextUtils.isEmpty(errorMsg)) {
            markResult(ILogicHandler.CODE_FAILURE, "网络不稳定");
            return;
        }
        errorMsg = errorMsg.toLowerCase(Locale.CHINESE);
        if (errorMsg.contains("unable to resolve host")) {
            errorMsg = "域名解析失败";
        } else if (errorMsg.contains("timed out") || errorMsg.contains("timeout")) {
            errorMsg = "网络连接超时,请尝刷新页面";
        } else if (errorMsg.contains("refused")) {
            errorMsg = "服务器拒绝连接";
        } else {
            errorMsg = "网络不稳定";
        }
        markResult(ILogicHandler.CODE_FAILURE, errorMsg);
    }

    protected boolean isPostJson() {
        return false;
    }

    /**
     * JSON转换
     *
     * @param json
     * @param classOfT
     * @param <T>
     * @return
     */
    protected <T> T modeFromJson(String json, Class<T> classOfT) {
        return JSON.parseObject(json, classOfT);
    }

    /**
     * JSON转换
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    protected <T> T modeFromJson(String json, Type type) {
        return JSON.parseObject(json, type);
    }

    protected void clearBeforeRequest() {
        this.anyRequestId = null;
    }

    @IntDef({GET, POST, MULTIPART, POST_BODY, PUT, PUT_BODY, DELETE, DELETE_BODY})
    @interface RequestTypeDef {
    }


}
