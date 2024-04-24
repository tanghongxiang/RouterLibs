package com.thx.commonlibrary.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * @author thx
 * @Description
 * @since 2020/11/10 11:31 AM
 */
public class ToastUtils {
    protected static Toast toast = null;
    private static long oneTime = 0;
    private static long twoTime = 0;

    public static void showToast(Context context, String s) {
        if (TextUtils.isEmpty(s)) return;
        if (toast == null) {
            toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            long tempTime = System.currentTimeMillis();
            toast.setText(s);
            if (tempTime - oneTime > 2000) {
                toast.show();
                twoTime = System.currentTimeMillis();
                oneTime = twoTime;
            }
        }
    }

    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }

}
