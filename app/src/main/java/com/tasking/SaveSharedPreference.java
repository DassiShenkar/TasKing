package com.tasking;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {
    static final String PREF_UID= "uid";
    static final String PREF_IS_MANAGER= "isManager";
    static final String PREF_MANAGER_ID= "mangerUid";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUid(Context ctx, String uid)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_UID, uid);
        editor.apply();
    }

    public static String getUid(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_UID, "");
    }

    public static void setManagerId(Context ctx, String uid)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_MANAGER_ID, uid);
        editor.apply();
    }

    public static String getManagerId(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_MANAGER_ID, "");
    }

    public static void setIsManager(Context ctx, boolean isManager)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_UID, isManager);
        editor.apply();
    }

    public static boolean getIsManager(Context ctx)
    {
        return getSharedPreferences(ctx).getBoolean(PREF_IS_MANAGER, false);
    }
}
