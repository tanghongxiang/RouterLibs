package com.thx.anynetworkmodule;

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
public class AnyNetwork {
    private IAnyServices service;

    public AnyNetwork() {
    }

    public void setService(IAnyServices service) {
        this.service = service;
    }

    public AnyResponse syncRequest(AnyRequest request) {
        return this.service.syncRequest(request);
    }

    public AnyRequestId asyncRequest(AnyRequest request, IAnyAsyncCallback callback) {
        return this.service.asyncRequest(request, callback);
    }

    public AnyRequestId asyncRequest(AnyRequest request, String fileStorePath, IAnyAsyncProgressCallback progressCallback, IAnyAsyncCallback callback) {
        return this.service.asyncRequest(request, fileStorePath, progressCallback, callback);
    }

    public boolean cancelRequest(AnyRequestId requestId) {
        return this.service.cancelRequest(requestId);
    }
}
