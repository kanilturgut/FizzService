package com.kanilturgut.mylib;

import android.util.Log;

/**
 * Author   : kanilturgut
 * Date     : 07/05/14
 * Time     : 14:35
 */
public class Logs {

    static String TAG = "";

    public Logs(String realTAG) {
        TAG = realTAG;
    }


    public static void i(String tag, String message) {
        //if (BuildConfig.DEBUG)
            Log.i(TAG, "Class = " + tag + " #### " + message);
    }

    public static void e(String tag, String message, Throwable throwable) {
        //if (BuildConfig.DEBUG)
            Log.e(TAG, "Class = " + tag + " #### " + message, throwable);
    }

    public static void e(String tag, String message) {
        //if (BuildConfig.DEBUG)
            Log.e(TAG, "Class = " + tag + " #### " + message);
    }


    public static void d(String tag, String message) {
        //if (BuildConfig.DEBUG)
            Log.d(TAG, "Class = " + tag + " #### " + message);
    }
}
