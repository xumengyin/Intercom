package com.jerry.simpleui.utils;

import com.orhanobut.logger.Logger;


/**
 * @author batman
 */
public class Logs {

    public static boolean DEBUG = true;

    public static void v(String tag, String msg) {
        if (DEBUG) {
            Logger.v(tag, msg);
        }
    }
    public static void dMsg(String msg) {
        if (DEBUG) {
            Logger.d(msg);
        }
    }

    public static void d(String tag, String msg) {
        boolean isDebug = DEBUG;
        if (DEBUG) {
           // Logger.log(Logger.DEBUG,tag, msg,null);
            Logger.d(msg);
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUG) {
            Logger.log(Logger.INFO,tag, msg,null);
        }
    }

    public static void w(String tag, String msg) {
        if (DEBUG) {
            Logger.log(Logger.WARN,tag, msg,null);
        }
    }

    public static void e(String tag, String msg, Throwable throwable) {
        if (DEBUG) {
            Logger.log(Logger.ERROR,tag, msg,throwable);
        }
    }
//
//    public static void logi(String message, Object... args) {
//        if (DEBUG) {
//            Logger.i("LogUtils", message);
//        }
//    }
//
//    public static void logd(String message, Object... args) {
//        if (DEBUG) {
//            Logger.d("LogUtils", message);
//        }
//    }
//
//    public static void loge(String message, Object... args) {
//        if (DEBUG) {
//            Logger.e("LogUtils", message);
//        }
//    }

}
