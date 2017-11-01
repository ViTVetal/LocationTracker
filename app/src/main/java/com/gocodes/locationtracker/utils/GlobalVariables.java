package com.gocodes.locationtracker.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by vit-vetal- on 01.11.17.
 */

public class GlobalVariables {
    public static final String SERVER_URL = "https://55z4akhs63.execute-api.us-west-2.amazonaws.com/UpdateAssetLocation2";

    public static final String[] FREQUENCIES = {"1", "2", "4","8", "12", "24", "48"};

    private static final String TRACKER_PREFERENCES = "TRACKER_PREFERENCES";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String ASSERT_ID = "assert_id";
    private static final String UPDATE_FREQUENCY = "frequency";
    private static final String UPDATE_ON_MOVE = "update_on_move";
    private static final String UPDATE_HISTORY = "update_history";
    private static final String UPDATE_REAL_TIME = "update_real_time";

    public static void setEmail(String email, Context context) {
        SharedPreferences pref = context.getSharedPreferences(TRACKER_PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(EMAIL, email);
        edit.apply();
    }

    public static String getEmail(Context context) {
        SharedPreferences pref = context.getSharedPreferences(TRACKER_PREFERENCES,
                Context.MODE_PRIVATE);
        return pref.getString(EMAIL, "");
    }

    public static void setPassword(String password, Context context) {
        SharedPreferences pref = context.getSharedPreferences(TRACKER_PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(PASSWORD, password);
        edit.apply();
    }

    public static String getPassword(Context context) {
        SharedPreferences pref = context.getSharedPreferences(TRACKER_PREFERENCES,
                Context.MODE_PRIVATE);
        return pref.getString(PASSWORD, "");
    }

    public static void setAssertID(String assertId, Context context) {
        SharedPreferences pref = context.getSharedPreferences(TRACKER_PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(ASSERT_ID, assertId);
        edit.apply();
    }

    public static String getAssertId(Context context) {
        SharedPreferences pref = context.getSharedPreferences(TRACKER_PREFERENCES,
                Context.MODE_PRIVATE);
        return pref.getString(ASSERT_ID, "");
    }

    public static void setFrequencyIndex(int frequncyIndex, Context context) {
        SharedPreferences pref = context.getSharedPreferences(TRACKER_PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt(UPDATE_FREQUENCY, frequncyIndex);
        edit.apply();
    }

    public static int getFrequencyIndex(Context context) {
        SharedPreferences pref = context.getSharedPreferences(TRACKER_PREFERENCES,
                Context.MODE_PRIVATE);
        return pref.getInt(UPDATE_FREQUENCY, 0);
    }

    public static void setUpdateOnMove(boolean updateOnMove, Context context) {
        SharedPreferences pref = context.getSharedPreferences(TRACKER_PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean(UPDATE_ON_MOVE, updateOnMove);
        edit.apply();
    }

    public static boolean isUpdateOnMove(Context context) {
        SharedPreferences pref = context.getSharedPreferences(TRACKER_PREFERENCES,
                Context.MODE_PRIVATE);
        return pref.getBoolean(UPDATE_ON_MOVE, false);
    }

    public static void setUpdateHistory(boolean updateHistory, Context context) {
        SharedPreferences pref = context.getSharedPreferences(TRACKER_PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean(UPDATE_HISTORY, updateHistory);
        edit.apply();
    }

    public static boolean isUpdateHistory(Context context) {
        SharedPreferences pref = context.getSharedPreferences(TRACKER_PREFERENCES,
                Context.MODE_PRIVATE);
        return pref.getBoolean(UPDATE_HISTORY, false);
    }

    public static void setRealTimeUpdate(boolean realTimeUpdate, Context context) {
        SharedPreferences pref = context.getSharedPreferences(TRACKER_PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean(UPDATE_REAL_TIME, realTimeUpdate);
        edit.apply();
    }

    public static boolean isRealTimeUpdate(Context context) {
        SharedPreferences pref = context.getSharedPreferences(TRACKER_PREFERENCES,
                Context.MODE_PRIVATE);
        return pref.getBoolean(UPDATE_REAL_TIME, false);
    }
}