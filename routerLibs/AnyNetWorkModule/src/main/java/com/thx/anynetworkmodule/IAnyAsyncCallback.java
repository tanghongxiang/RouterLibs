package com.thx.anynetworkmodule;

import okhttp3.ResponseBody;

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
public interface IAnyAsyncCallback {

    void onSuccess(String res);

    void onFailure(Throwable throwable);

}
