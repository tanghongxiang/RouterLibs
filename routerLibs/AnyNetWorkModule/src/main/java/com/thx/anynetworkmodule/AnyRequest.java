package com.thx.anynetworkmodule;

import android.util.ArrayMap;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
public class AnyRequest {
    private Map<String, String> paramMap;
    private Map<String, File> fileMap;
    private Map<String, String> headMap;
    private int requestType;
    private String url;
    private boolean fileDownloadType = false;
    private String requestBody = "";

    public AnyRequest() {
        this.paramMap = new ArrayMap<>();
        this.fileMap = new ArrayMap<>();
        this.headMap = new ArrayMap<>();
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setFileDownloadType(boolean fileDownloadType) {
        this.fileDownloadType = fileDownloadType;
    }

    public void addParam(String key, Object value) {
        if (value instanceof String) {
            this.paramMap.put(key, (String) value);
        } else if (value instanceof File) {
            this.fileMap.put(key, (File) value);
        } else if (value != null) {
            throw new IllegalArgumentException("unSupport type: " + value.getClass());
        }
    }

    public void addParams(Map paramMap) {
        Iterator iterator = paramMap.keySet().iterator();
        while (iterator.hasNext()) {
            Object key = iterator.next();
            this.addParam((String) key, paramMap.get(key));
        }
    }

    public void setRequestBody(String body) {
        this.requestBody = body;
    }

    public RequestBody getRequestBody() {
        return RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"), requestBody);
    }

    public void addHeaders(String key, String value) {
        this.headMap.put(key, value);
    }

    public void addHeaders(Map<String, String> headers) {
        if (headers == null) return;
        this.headMap.putAll(headers);
    }

    public Map<String, String> getHeadersMap() {
        return headMap;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public Map<String, File> getFileMap() {
        return fileMap;
    }

    public String getUrl() {
        return url;
    }

    public int getRequestType() {
        return requestType;
    }

    public boolean isFileDownloadType() {
        return fileDownloadType;
    }

}
