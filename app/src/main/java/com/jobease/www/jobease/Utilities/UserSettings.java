package com.jobease.www.jobease.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Dell on 25/06/2017.
 */

public class UserSettings {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    public void setUserID(Context context, String userToken) {
        sharedPreferences = context.getSharedPreferences("user_data", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("userToken", userToken);
        editor.commit();
    }

    public String getUserID(Context context) {
        sharedPreferences = context.getSharedPreferences("user_data", MODE_PRIVATE);
        return sharedPreferences.getString("userToken", "");
    }

    public void setUserFullName(Context context, String userToken) {
        sharedPreferences = context.getSharedPreferences("user_data", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("userFullName", userToken);
        editor.commit();
    }

    public String getUserFullName(Context context) {
        sharedPreferences = context.getSharedPreferences("user_data", MODE_PRIVATE);
        return sharedPreferences.getString("userFullName", "");
    }

    public void setUserBirthDate(Context context, String userToken) {
        sharedPreferences = context.getSharedPreferences("user_data", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("userBirthDate", userToken);
        editor.commit();
    }

    public String getUserBirthDate(Context context) {
        sharedPreferences = context.getSharedPreferences("user_data", MODE_PRIVATE);
        return sharedPreferences.getString("userBirthDate", "");
    }

    public void setCurrency(Context context, String userToken) {
        sharedPreferences = context.getSharedPreferences("user_data", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("currency", userToken);
        editor.commit();
    }

    public String getCurrency(Context context) {
        sharedPreferences = context.getSharedPreferences("user_data", MODE_PRIVATE);
        return sharedPreferences.getString("currency", "");
    }

    public void setAddress(Context context, String userToken) {
        sharedPreferences = context.getSharedPreferences("user_data", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("address", userToken);
        editor.commit();
    }

    public String getAddress(Context context) {
        sharedPreferences = context.getSharedPreferences("user_data", MODE_PRIVATE);
        return sharedPreferences.getString("address", "");
    }

    public void setPhone(Context context, String userToken) {
        sharedPreferences = context.getSharedPreferences("user_data", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("phone", userToken);
        editor.commit();
    }

    public String getPhone(Context context) {
        sharedPreferences = context.getSharedPreferences("user_data", MODE_PRIVATE);
        return sharedPreferences.getString("phone", "");
    }

    public void setUserImamge(Context context, String userToken) {
        sharedPreferences = context.getSharedPreferences("user_data", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("user_img", userToken);
        editor.commit();
    }

    public String getUserImamge(Context context) {
        sharedPreferences = context.getSharedPreferences("user_data", MODE_PRIVATE);
        return sharedPreferences.getString("user_img", "");
    }
}
