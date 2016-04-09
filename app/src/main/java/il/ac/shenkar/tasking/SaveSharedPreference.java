package il.ac.shenkar.tasking;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {
    static final String PREF_UID= "uid";
    static final String PREF_USERNAME= "username";
    static final String PREF_PASSWORD= "password";
    static final String PREF_IS_MANAGER= "isManager";
    static final String PREF_MANAGER_ID= "mangerUid";
    static final String PREF_TEAM_NAME= "teamName";

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

    public static void setUsername(Context ctx, String username)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USERNAME, username);
        editor.apply();
    }

    public static String getUsername(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USERNAME, "");
    }

    public static void setPassword(Context ctx, String password)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_PASSWORD, password);
        editor.apply();
    }

    public static String getPassword(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_PASSWORD, "");
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
        editor.putBoolean(PREF_IS_MANAGER, isManager);
        editor.apply();
    }

    public static boolean getIsManager(Context ctx)
    {
        return getSharedPreferences(ctx).getBoolean(PREF_IS_MANAGER, false);
    }

    public static void setTeamName(Context ctx, String teamNAme)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_TEAM_NAME, teamNAme);
        editor.apply();
    }

    public static String getTeamName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_TEAM_NAME, "");
    }
}
