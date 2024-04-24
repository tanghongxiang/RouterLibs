package com.thx.commonlibrary.network.entity;

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
public class ParamInfo {
    public String token;
    public String apiType;
    public String channel;
    public String version;
    public String model;
    public String os;
    public String net;
    public String carrier;

    public String mac;

    public ParamInfo() {
    }


    public ParamInfo(String token, String apiType, String channel, String version, String model, String os, String net, String carrier) {
        this.token = token;
        this.apiType = apiType;
        this.channel = channel;
        this.version = version;
        this.model = model;
        this.os = os;
        this.net = net;
        this.carrier = carrier;
    }
}

