package com.thx.anynetworkmodule;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.core.app.ActivityCompat;

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
public class NetUtils {
    private static final String ACCESS_NETWORK_STATE = "android.permission.ACCESS_NETWORK_STATE";

    public static boolean isNetConnected(Context context) {
        if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(context, ACCESS_NETWORK_STATE)) {
            return false;
        }
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null) {
                    return networkInfo.isConnected();
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
