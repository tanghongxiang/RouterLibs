package com.thx.commonlibrary.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;


import java.util.ArrayList;
import java.util.List;

/**
 * @author: luanxu
 * @createTime: on 2017/9/30 09:46
 * @description: 权限动态获取的工具类
 * @changed by:
 */
public class PermissionUtil {
    public static int REQUEST_CODE = 2017;
    //权限请求结果的回调
    public static PermissionResultListener permissionResultListener;
    //权限的数据
    private static String[] PERMISSIONS;
    //权限名称的数组
    private static String[] PERMISSIONS_NAME;

    /**
     * 请求权限
     *
     * @param context        上下文对象
     * @param permission     权限的数组，如：{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION}
     * @param permissionName 权限名称的数组，如：{"相机权限","位置权限"}
     * @param listener       权限请求结果的回调
     */
    public static void requestPermission(Activity context, String[] permission, String[] permissionName, PermissionResultListener listener) {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        permissionResultListener = listener;
        PERMISSIONS = permission;
        PERMISSIONS_NAME = permissionName;
        //所有权限是否全部打开的判断
        boolean judge = true;
        for (String aPermission : permission) {
            // 检查该权限是否已经获取
            boolean isSuccess = getPermissionState(context, aPermission);
            if (!isSuccess) {
                judge = false;
            }
        }

        if (!judge) {
            if (ManifestUtil.getTargetSdkVersion(context) >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(context, permission, REQUEST_CODE);
            }
        }

        if (listener != null) {
            //返回权限判断状态
            permissionResultListener.permissionResult(Build.VERSION.SDK_INT < Build.VERSION_CODES.M || judge);
        }
    }

    /**
     * 获取权限状态
     *
     * @param context    上下文对象
     * @param permission 权限
     * @return
     */
    public static boolean getPermissionState(Activity context, String permission) {
        boolean result = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ManifestUtil.getTargetSdkVersion(context) >= Build.VERSION_CODES.M) {
                result = ContextCompat.checkSelfPermission(context, permission)
                        == PackageManager.PERMISSION_GRANTED;
            } else {
                result = PermissionChecker.checkSelfPermission(context, permission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        }
        return result;
    }



    /**
     * 从应用设置页面返回
     *
     * @param act         上下文对象
     * @param requestCode 请求码
     */
    public static void goToSettingResult(Activity act, int requestCode) {
        try {
            if (requestCode == REQUEST_CODE) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    for (String PERMISSION : PERMISSIONS) {
                        // 检查该权限是否已经获取
                        int checkSelfPermission = ContextCompat.checkSelfPermission(act, PERMISSION);
                        // 权限是否已经 授权 GRANTED---授权  DENIED---拒绝
                        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                            permissionResultListener.permissionResult(false);
                            return;
                        }
                    }
                    permissionResultListener.permissionResult(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface PermissionResultListener {
        void permissionResult(boolean result);
    }
}
