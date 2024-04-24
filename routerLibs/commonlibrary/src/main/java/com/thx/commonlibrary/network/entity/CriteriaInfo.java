package com.thx.commonlibrary.network.entity;

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
public class CriteriaInfo {
    private boolean paging;
    private int offset;
    private int limit;
    private String mobile;
    private String loginPwd;

    public CriteriaInfo(boolean paging, int offset, int limit, String mobile, String loginPwd) {
        this.paging = paging;
        this.offset = offset;
        this.limit = limit;
        this.mobile = mobile;
        this.loginPwd = loginPwd;
    }
}
