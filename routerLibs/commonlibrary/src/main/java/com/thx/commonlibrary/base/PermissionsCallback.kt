package com.thx.commonlibrary.base

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
interface PermissionsCallback {
    fun getPermissionsSuccess()//请求权限成功
    fun getPermissionsFailed()//请求权限失败
    fun forbidPermissions()//禁止弹权限框
}