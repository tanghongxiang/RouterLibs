package com.thx.commonlibrary.utils;


import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用于读取AndroidManifest.xml文件中的值
 */
public class ManifestUtil {
    /**
     * 获得包名
     *
     * @param context 上下文
     * @return 包名
     */
    public static String getPackageName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得版本名
     *
     * @param context 上下文
     * @return 版本名
     */
    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得版本号
     *
     * @param context 上下文
     * @return 版本号
     */
    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取application层级的metadata
     *
     * @param context 上下文
     * @param key     key
     * @return value
     */
    public static String getApplicationMetaData(Context context, String key) {
        try {
            Bundle metaData = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData;
            return metaData.get(key).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取application层级的provider
     *
     * @param context 上下文
     * @return value
     */
    public static String getApplicationProviderData(Context context) {
        try {
            @SuppressLint("WrongConstant") ProviderInfo providerInfo = context.getPackageManager().getProviderInfo(new ComponentName(context, "android.support.v4.content.FileProvider"), PackageManager.GET_PROVIDERS);
            return providerInfo.authority;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断应用是否申明某个权限
     *
     * @param context    上下文
     * @param permission Manifest.permission里的值
     * @return 是否申明某个权限
     */
    public static boolean hasPermission(Context context, String permission) {
        return (PackageManager.PERMISSION_GRANTED) == (context.getPackageManager().checkPermission(permission, context.getPackageName()));
    }

    /**
     * 获得应用申明的所有权限列表
     *
     * @param context 上下文
     * @return 获得应用申明的所有权限列表
     */
    public static List<String> getPermissions(Context context) {
        List<String> permissions = new ArrayList<>();
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            permissions.addAll(Arrays.asList(packageInfo.requestedPermissions));

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return permissions;
    }


    /**
     * 判断当前应用程序处于后台运行
     *
     * @param context
     * @return true  在后台运行
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            return !topActivity.getPackageName().equals(context.getPackageName());
        }
        return false;
    }

    /**
     * 判定应用是否在前台运行(以是否在前台可见为标准).
     *
     * @param context
     * @return true  在前台; false 在后台或被杀死
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static boolean isTheForeground(Context context) {

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                // 某些 Android 5.0之前的手机需要在此处再作判断
                return appProcess.importance <= ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE && !isApplicationBroughtToBackground(context);
            }
        }
        return false;
    }

    /**
     * 获取当前SDK版本
     *
     * @param context
     * @return
     */
    public static int getTargetSdkVersion(Context context) {
        int targetSdkVersion = 0;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return targetSdkVersion;
    }

    /**
     * 获取应用图标
     *
     * @param context
     */
    public static synchronized Bitmap getAppIconBitmap(Context context) {
        Bitmap bitmap = null;
        try {
            PackageManager packageManager = context.getApplicationContext()
                    .getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(
                    context.getPackageName(), 0);

            Drawable drawable = packageManager.getApplicationIcon(applicationInfo);
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            bitmap = bitmapDrawable.getBitmap();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


}
