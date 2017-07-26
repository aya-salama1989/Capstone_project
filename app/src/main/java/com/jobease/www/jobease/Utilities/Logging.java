package com.jobease.www.jobease.Utilities;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


public final class Logging {


    public static void log(String s, Context context) {
        Log.e("TAG: " + context.getClass().getSimpleName(), s);
    }

    public static void log(String s) {
        Log.e("TAG: " , s);
    }
    public static void shortToast(Context context, String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    public static void longToast(Context context, String s) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
    }
}