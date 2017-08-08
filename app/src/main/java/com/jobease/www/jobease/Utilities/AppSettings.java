package com.jobease.www.jobease.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Dell on 25/02/2017.
 */

public class AppSettings {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    public void setIsFirstLogin(Context context, boolean isFirstTime) {
        sharedPreferences = context.getSharedPreferences("AppStatus", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean("isFirstTime", isFirstTime);
        editor.commit();
    }

    public boolean getIsFirstLogin(Context context) {
        sharedPreferences = context.getSharedPreferences("AppStatus", MODE_PRIVATE);
        return sharedPreferences.getBoolean("isFirstTime", true);
    }

    public void setAppLanguage(Context context, String language) {
        sharedPreferences = context.getSharedPreferences("AppStatus", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("language", language);
        editor.commit();
    }

    public String getAppLanguage(Context context) {
        sharedPreferences = context.getSharedPreferences("AppStatus", MODE_PRIVATE);
        return sharedPreferences.getString("language", "");
    }


}
