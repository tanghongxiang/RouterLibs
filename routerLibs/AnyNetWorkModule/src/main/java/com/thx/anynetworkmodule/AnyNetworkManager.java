package com.thx.anynetworkmodule;

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
public class AnyNetworkManager {
    private static AnyNetworkManager ourInstance = new AnyNetworkManager();
    private AnyNetwork globalAnyNetWork;

    public static AnyNetworkManager getInstance() {
        return ourInstance;
    }

    public AnyNetworkManager() {
    }

    public AnyNetwork getGlobalAnyNetWork() {
        if (this.globalAnyNetWork == null) {
            throw new IllegalArgumentException("globalAnyNetWork == null");
        }
        return this.globalAnyNetWork;
    }

    public void setGlobalAnyNetWork(AnyNetwork globalAnyNetWork) {
        this.globalAnyNetWork = globalAnyNetWork;
    }
}
