package com.elf.common;

import android.util.Log;

/**
 * Created by yangyi on 13-7-30.
 */
public class LogUtils {
    public static String TAG = "yangyi";

    public static void info(String message) {
        Log.i(TAG, message);
    }


    public static void error(String message) {
        Log.e(TAG, message);
    }


    public static void error(Throwable e) {
        Log.e(TAG, Log.getStackTraceString(e));
    }
}
