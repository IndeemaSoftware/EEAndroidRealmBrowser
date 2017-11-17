package com.indeema.realmbrowser.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.indeema.realmbrowser.RealmBrowser;
import com.indeema.realmbrowser.adapters.PageFilter;
import com.indeema.realmbrowser.entities.Theme;
import com.indeema.realmbrowser.entities.ThemeType;

import java.util.Set;

/**
 *Created by ruslanstosyk on 11/6/17.
 */

public class PreferenceUtils {

    private static final String PREFERENCE = "REALM_BROWSER_PREF";
    private static final String KEY_THEME = "KEY_THEME";
    private static final String KEY_PAGINATION_ENABLE = "KEY_PAGINATION_ENABLE";
    private static final String KEY_PAGE_SIZE = "KEY_PAGE_SIZE";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
    }

    public static void saveTheme(Context context, @ThemeType int theme) {
        getPreferences(context).edit().putInt(KEY_THEME, theme).apply();
    }

    public static @ThemeType int getTheme(Context context) {
        return getPreferences(context).getInt(KEY_THEME, Theme.DEFAULT);
    }

    public static void saveCheckedFields(Context context, String name, Set<String> options) {
        getPreferences(context).edit().putStringSet(name, options).apply();
    }

    public static Set<String> getCheckedFields(Context context, String name) {
        return getPreferences(context).getStringSet(name, null);
    }

    public static void savePaginationEnable(Context context, boolean isPaginationEnable) {
        getPreferences(context).edit().putBoolean(KEY_PAGINATION_ENABLE, isPaginationEnable).apply();
    }

    public static boolean isPaginationEnable(Context context) {
        return getPreferences(context).getBoolean(KEY_PAGINATION_ENABLE, true);
    }

    public static void savePageSize(Context context, int pageSize) {
        getPreferences(context).edit().putInt(KEY_PAGE_SIZE, pageSize).apply();
    }

    public static int getPageSize(Context context) {
        return getPreferences(context).getInt(KEY_PAGE_SIZE, RealmBrowser.DEFAULT_PAGE_SIZE);
    }

}
