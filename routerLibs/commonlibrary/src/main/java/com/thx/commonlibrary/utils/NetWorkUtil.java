package com.thx.commonlibrary.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;

/**
 * 网络相关工具类
 *
 * @author fjh
 */
public class NetWorkUtil {
    /**
     * Unknown network class
     */
    public static final String NETWORK_CLASS_UNKNOWN = "0";

    /**
     * wifi net work
     */
    public static final String NETWORK_WIFI = "1";

    /**
     * "2G" networks
     */
    public static final String NETWORK_CLASS_2_G = "2";

    /**
     * "3G" networks
     */
    public static final String NETWORK_CLASS_3_G = "3";

    /**
     * "4G" networks
     */
    public static final String NETWORK_CLASS_4_G = "4";

    /**
     * 是否连接上网络
     *
     * @param ctx 上下文
     * @return 连接上true，未连接上false
     * @date: 2017-1-16 下午17:52
     */
    public static boolean isNetworkConnected(Context ctx) {
        // 网络连接的状态
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            // 建立网络数组
            NetworkInfo[] net_info = connectivityManager.getAllNetworkInfo();
            if (net_info != null) {
                for (NetworkInfo aNet_info : net_info) {
                    // 判断获得的网络状态是否是处于连接状态
                    if (aNet_info.getState() == NetworkInfo.State.CONNECTED) {
                        isConnected = true;
                    }
                }
            }
        }
        return isConnected;

    }

    /**
     * 判断当前网络是否为wifi状态
     *
     * @param ctx 上下文
     * @return wifi状态true，非wifi状态false
     * @date: 2017-1-16 下午17:52
     */
    public static boolean isWifi(Context ctx) {
        // 判断是否为wifi的状态
        boolean isWifi = false;
        if (isNetworkConnected(ctx)) {
            ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();

            if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {

                isWifi = true;
            }
        }
        return isWifi;
    }

    @SuppressLint("MissingPermission")
    private static String getNetWorkClass(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return NETWORK_CLASS_2_G;

            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return NETWORK_CLASS_3_G;

            case TelephonyManager.NETWORK_TYPE_LTE:
                return NETWORK_CLASS_4_G;

            default:
                return NETWORK_CLASS_UNKNOWN;
        }
    }

    /**
     * 获取当前网络类型
     *
     * @param context
     * @return
     */
    public static String getNetWorkStatus(Context context) {
        String netWorkType = NETWORK_CLASS_UNKNOWN;

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            int type = networkInfo.getType();

            if (type == ConnectivityManager.TYPE_WIFI) {
                netWorkType = NETWORK_WIFI;
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                netWorkType = getNetWorkClass(context);
            }
        }

        return netWorkType;
    }


    /**
     * 获取本地手机的IP
     */
    public static String getLocalIpAddress() {

        String ip = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    String mIP = inetAddress.getHostAddress().substring(0, 3);
                    if (mIP.equals("192")) {
                        ip = inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } finally {
            return ip;
        }
    }

    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }


            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }


    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    public static String getConnectWifiSsid(Context context) {
        WifiManager my_wifiManager = ((WifiManager) context.getSystemService(WIFI_SERVICE));
        WifiInfo wifiInfo = my_wifiManager.getConnectionInfo();
        String ssid = wifiInfo.getSSID();
        int networkId = wifiInfo.getNetworkId();
        @SuppressLint("MissingPermission") List<WifiConfiguration> configuredNetworks = my_wifiManager.getConfiguredNetworks();

        for (WifiConfiguration wifiConfiguration : configuredNetworks) {

            if (wifiConfiguration.networkId == networkId) {
                ssid = wifiConfiguration.SSID;
                break;
            }
        }

        if (!TextUtils.isEmpty(ssid)) {
            if (ssid.startsWith("\"")) {
                ssid = ssid.substring(1);
            }
            if (ssid.endsWith("\"")) {
                ssid = ssid.substring(0, ssid.length() - 1);
            }

        }

        return ssid;
    }
}
