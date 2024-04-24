package com.thx.anynetworkmodule;

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
public interface IAnyServices {
    void setConfig(AnyConfig config);

    AnyResponse syncRequest(AnyRequest request);

    AnyRequestId asyncRequest(AnyRequest request, IAnyAsyncCallback callback);

    AnyRequestId asyncRequest(AnyRequest request, String filePath, IAnyAsyncProgressCallback progress, IAnyAsyncCallback callback);

    boolean cancelRequest(AnyRequestId requestId);
}
