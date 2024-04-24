package com.thx.commonlibrary.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.thx.commonlibrary.base.RouterFrameApplication;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;


/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
public class DeviceUtil {
    /**
     * 获得屏幕宽度，单位px
     *
     * @param context 上下文
     * @return 屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        if (context != null && context.getResources() != null) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            return dm.widthPixels;
        } else {
            return 0;
        }
    }

    /**
     * 获得屏幕高度
     *
     * @param context 上下文
     * @return 屏幕高度
     */
    public static int getScreenHeight(Context context) {
        if (context != null && context.getResources() != null) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            return dm.heightPixels;
        } else {
            return 0;
        }
    }

    /**
     * 获取屏幕高度，包含虚拟底部状态栏
     *
     * @param context
     * @return
     */
    public static int getScreenHeightTwo(Activity context) {
        int height = 0;
        Display display = context.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        Class c;
        try {
            c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            height = dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return height;
    }

    /**
     * 主页面的高度
     */
    public static int screenHeight;

    /**
     * 获取屏幕的高度，根据主页面的高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeightThree(Activity context) {
        if (screenHeight > 0) {
            return screenHeight;
        } else {
            return getScreenHeight(context);
        }
    }


    /**
     * 获取状态栏的高度
     *
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        //应用区域  
        Rect outRect1 = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect1);
        //状态栏高度=屏幕高度-应用区域高度
        return Math.abs(dm.heightPixels - outRect1.height());
    }

    /**
     * 获取状态栏的高度
     * 上方法 键盘 弹出时搞不不准确
     */
    public static int getStatusBarHeight2(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获得手机名称
     *
     * @return 手机名称
     */
    public static String getMobileName() {
        return Build.MANUFACTURER + " " + Build.MODEL;
    }

    /**
     * 获得手机类型
     *
     * @return 手机类型
     */
    public static String getPhoneType() {
        return Build.MODEL;
    }

    /**
     * 获得系统版本号
     *
     * @return 系统版本号
     */
    public static int getOSVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机号，几乎获取不到
     *
     * @param context 上下文
     * @return 手机号，几乎获取不到
     */
    public static String getPhoneNumber(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context
                .getApplicationContext().getSystemService(
                        Context.TELEPHONY_SERVICE);
        @SuppressLint({"HardwareIds", "MissingPermission"}) String phoneNum = mTelephonyMgr.getLine1Number();
        return TextUtils.isEmpty(phoneNum) ? "" : phoneNum;
    }

    /**
     * 获得IMEI号(国际移动设备身份码)
     *
     * @param context 上下文
     * @return IMEI号
     */
    public static String getPhoneIMEI(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context
                .getApplicationContext().getSystemService(
                        Context.TELEPHONY_SERVICE);
        @SuppressLint({"HardwareIds", "MissingPermission"}) String phoneImei = mTelephonyMgr.getDeviceId();
        return TextUtils.isEmpty(phoneImei) ? "" : phoneImei;
    }

    /**
     * 获取imsi号(国际移动用户识别码)
     *
     * @param context 上下文
     * @return IMSI号
     */
    public static String getPhoneIMSI(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context
                .getApplicationContext().getSystemService(
                        Context.TELEPHONY_SERVICE);
        @SuppressLint({"HardwareIds", "MissingPermission"}) String phoneImsi = mTelephonyMgr.getSubscriberId();

        return TextUtils.isEmpty(phoneImsi) ? "" : phoneImsi;
    }

    /**
     * 获得ipv4类型的ip地址
     *
     * @return ip地址
     */
    public static String getLocalIPAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得Mac物理地址
     *
     * @return Mac物理地址
     */
    public static String getLocalMacAddress() {
        String Mac = null;
        try {
            String path = "sys/class/net/wlan0/address";
            if ((new File(path)).exists()) {
                FileInputStream fis = new FileInputStream(path);
                byte[] buffer = new byte[8192];
                int byteCount = fis.read(buffer);
                if (byteCount > 0) {
                    Mac = new String(buffer, 0, byteCount, "utf-8");
                }
                fis.close();
            }

            if (Mac == null || Mac.length() == 0) {
                path = "sys/class/net/eth0/address";
                FileInputStream fis = new FileInputStream(path);
                byte[] buffer_name = new byte[8192];
                int byteCount_name = fis.read(buffer_name);
                if (byteCount_name > 0) {
                    Mac = new String(buffer_name, 0, byteCount_name, "utf-8");
                }
                fis.close();
            }

            if (Mac == null || Mac.length() == 0) {
                return "";
            } else if (Mac.endsWith("\n")) {
                Mac = Mac.substring(0, Mac.length() - 1);
            }
        } catch (Exception ignored) {
        }

        return TextUtils.isEmpty(Mac) ? "" : Mac;
    }

    /**
     * 获得序列号
     *
     * @return 序列号
     */
    @SuppressLint("HardwareIds")
    public static String getSerialNum() {
        String serialNum = null;
        try {
            @SuppressLint("PrivateApi") Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            serialNum = (String) (get.invoke(c, "ro.serialno", "unknown"));
        } catch (Exception ignored) {
        }

        return serialNum;
    }

    /**
     * 获得AndroidId
     *
     * @param context 上下文
     * @return AndroidId
     */
    public static String getAndroidId(Context context) {
        @SuppressLint("HardwareIds") String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }

    /**
     * 1,代表中国移动，2，代表中国联通，3，代表中国电信，0，代表未知
     *
     * @param context
     * @return
     */
    public static String getOperators(Context context) {
        // 移动设备网络代码（英语：Mobile Network Code，MNC）是与移动设备国家代码（Mobile Country Code，MCC）（也称为“MCC /
        // MNC”）相结合, 例如46000，前三位是MCC，后两位是MNC 获取手机服务商信息
        String OperatorsName = "0";
        try {
            @SuppressLint({"HardwareIds", "MissingPermission"}) String IMSI = ((TelephonyManager) (context.getSystemService(Context.TELEPHONY_SERVICE))).getSubscriberId();
            // IMSI号前面3位460是国家，紧接着后面2位00 运营商代码
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
                OperatorsName = "1";
            } else if (IMSI.startsWith("46001") || IMSI.startsWith("46006")) {
                OperatorsName = "2";
            } else if (IMSI.startsWith("46003") || IMSI.startsWith("46005")) {
                OperatorsName = "3";
            }
        } catch (Exception ignored) {
        }

        return OperatorsName;
    }

    /**
     * 获取操作系统版本号
     *
     * @return
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }


    /**
     * APP是否处于前台唤醒状态
     *
     * @return
     */
    public static boolean isAppOnForeground(Activity context) {
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    private static final int PORTRAIT = 0;
    private static final int LANDSCAPE = 1;


    private volatile static Point[] mRealSizes = new Point[2];


    public static int getScreenRealHeight(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return getScreenHeight(context);
        }

        int orientation = context != null
                ? context.getResources().getConfiguration().orientation
                : RouterFrameApplication.Companion.getInstance().getResources().getConfiguration().orientation;
        orientation = orientation == Configuration.ORIENTATION_PORTRAIT ? PORTRAIT : LANDSCAPE;

        if (mRealSizes[orientation] == null) {
            WindowManager windowManager = context != null
                    ? (WindowManager) context.getSystemService(Context.WINDOW_SERVICE)
                    : (WindowManager) RouterFrameApplication.Companion.getInstance().getSystemService(Context.WINDOW_SERVICE);
            if (windowManager == null) {
                return getScreenHeight(context);
            }
            Display display = windowManager.getDefaultDisplay();
            Point point = new Point();
            display.getRealSize(point);
            mRealSizes[orientation] = point;
        }
        return mRealSizes[orientation].y;
    }

    private volatile static boolean mHasCheckAllScreen;
    private volatile static boolean mIsAllScreenDevice;

    public static boolean isAllScreenDevice() {
        if (mHasCheckAllScreen) {
            return mIsAllScreenDevice;
        }
        mHasCheckAllScreen = true;
        mIsAllScreenDevice = false;
        // 低于 API 21的，都不会是全面屏。。。
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return false;
        }
        WindowManager windowManager = (WindowManager) RouterFrameApplication.Companion.getInstance().getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            Display display = windowManager.getDefaultDisplay();
            Point point = new Point();
            display.getRealSize(point);
            float width, height;
            if (point.x < point.y) {
                width = point.x;
                height = point.y;
            } else {
                width = point.y;
                height = point.x;
            }
            if (height / width >= 1.97f) {
                mIsAllScreenDevice = true;
            }
        }
        return mIsAllScreenDevice;
    }

    public static int getFullActivityHeight(Context context) {
        if (!isAllScreenDevice()) {
            return getScreenHeight(context);
        }
        return getScreenRealHeight(context);
    }

    /**
     * 底部导航栏的高度
     *
     * @param activity
     * @return
     */
    public static int getNavigationBarHeight(Activity activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    /**
     * 判断是否存在底部导航栏
     *
     * @param context
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if ((Settings.Global.getInt(context.getContentResolver(), "force_fsg_nav_bar", 0) == 0)) {
                return true;
            } else {
                return false;
            }
        } else {
            boolean hasNavigationBar = false;
            Resources rs = context.getResources();
            int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
            if (id > 0) {
                hasNavigationBar = rs.getBoolean(id);
            }
            try {
                Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
                Method m = systemPropertiesClass.getMethod("get", String.class);
                String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
                if ("1".equals(navBarOverride)) {
                    hasNavigationBar = false;
                } else if ("0".equals(navBarOverride)) {
                    hasNavigationBar = true;
                }
            } catch (Exception e) {

            }
            return hasNavigationBar;
        }
    }
}
