package com.example.group3;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference
{
    static final String PREF_USER_NAME= "username";
    static final String PREF_TOKEN = "token";
    static final String PREF_EMAIL = "email";
    static final String PREF_STAY_LOGGED = "staylogged";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.apply();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static void setToken(Context ctx, String token)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_TOKEN, token);
        editor.apply();
    }

    public static String getToken(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_TOKEN, "");
    }

    public static void setEmail(Context ctx, String email)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_EMAIL, email);
        editor.apply();
    }

    public static String getEmail(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_EMAIL, "");
    }

    public static void setStayLogged(Context ctx, String stayLogged)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_STAY_LOGGED, stayLogged);
        editor.apply();
    }
    public static String getStayLogged(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_STAY_LOGGED, "");
    }

    public static void clearUser(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.apply();
    }
}
