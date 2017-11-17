package com.indeema.realmbrowser.entities;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;

import com.indeema.realmbrowser.R;

/**
 * @author Ivan Savenko
 *         Date: November, 15, 2017
 *         Time: 18:21
 */

public class Theme {

    public static final int DEFAULT = 0;
    public static final int DRACULA = 1;

    private @ThemeType int mType;

    private @ColorInt int mPrimaryColor;
    private @ColorInt int mPrimaryDarkColor;
    private @ColorInt int mAccentColor;
    private @ColorInt int mTextPrimaryColor;
    private @ColorInt int mTextSecondaryColor;
    private @ColorInt int mListItemBackgroundLightColor;
    private @ColorInt int mListItemBackgroundDarkColor;

    public Theme(Context context, @ThemeType int type, @ColorRes int primaryColorRes, @ColorRes int primaryDarkColorRes,
                 @ColorRes int accentColorRes, @ColorRes int textPrimaryColor, @ColorRes int textSecondaryColor,
                 @ColorRes int listItemBackgroundLightColorRes, @ColorRes int listItemBackgroundDarkColorRes) {
        mType = type;
        mPrimaryColor = ContextCompat.getColor(context, primaryColorRes);
        mPrimaryDarkColor = ContextCompat.getColor(context, primaryDarkColorRes);
        mAccentColor = ContextCompat.getColor(context, accentColorRes);
        mTextPrimaryColor = ContextCompat.getColor(context, textPrimaryColor);
        mTextSecondaryColor = ContextCompat.getColor(context, textSecondaryColor);
        mListItemBackgroundLightColor = ContextCompat.getColor(context, listItemBackgroundLightColorRes);
        mListItemBackgroundDarkColor = ContextCompat.getColor(context, listItemBackgroundDarkColorRes);
    }

    public Theme(@ColorInt int primaryColor, @ThemeType int type, @ColorInt int primaryDarkColor,
                 @ColorInt int accentColor, @ColorInt int textPrimaryColor, @ColorInt int textSecondaryColor,
                 @ColorInt int listItemBackgroundLightColor, @ColorInt int listItemBackgroundDarkColor) {
        mType = type;
        mPrimaryColor = primaryColor;
        mPrimaryDarkColor = primaryDarkColor;
        mAccentColor = accentColor;
        mTextPrimaryColor = textPrimaryColor;
        mTextSecondaryColor = textSecondaryColor;
        mListItemBackgroundLightColor = listItemBackgroundLightColor;
        mListItemBackgroundDarkColor = listItemBackgroundDarkColor;
    }

    public static Theme newInstance(Context context, @ThemeType int themeType) {
        switch (themeType) {
            case DEFAULT:
                return newInstance(context, themeType, R.color.colorBrowserPrimary, R.color.colorBrowserPrimaryDark,
                        R.color.colorBrowserAccent, R.color.colorBrowserTextPrimary, R.color.colorBrowserTextSecondary,
                        R.color.colorBrowserListItemBackgroundLight, R.color.colorBrowserListItemBackgroundDark);
            case DRACULA:
                return newInstance(context, themeType, R.color.colorDracula, R.color.colorDraculaDark,
                        R.color.colorDraculaAccent, R.color.colorDraculaTextPrimary, R.color.colorDraculaTextSecondary,
                        R.color.colorDraculaListItemBackgroundLight, R.color.colorDraculaListItemBackgroundDark);
            default:
                return newInstance(context, themeType, R.color.colorBrowserPrimary, R.color.colorBrowserPrimaryDark,
                        R.color.colorBrowserAccent, R.color.colorBrowserTextPrimary, R.color.colorBrowserTextSecondary,
                        R.color.colorBrowserListItemBackgroundLight, R.color.colorBrowserListItemBackgroundDark);
        }
    }

    private static Theme newInstance(Context context, @ThemeType int type, @ColorRes int primaryColorRes, @ColorRes int primaryDarkColorRes,
                             @ColorRes int accentColorRes, @ColorRes int textPrimaryColorRes, @ColorRes int textSecondaryColorRes,
                             @ColorRes int listItemBackgroundLightColorRes, @ColorRes int listItemBackgroundDarkColorRes) {
        return new Theme(context, type, primaryColorRes, primaryDarkColorRes, accentColorRes, textPrimaryColorRes,
                textSecondaryColorRes, listItemBackgroundLightColorRes, listItemBackgroundDarkColorRes);
    }

    public static Theme newInstance(@ThemeType int type, @ColorInt int primaryColor, @ColorInt int primaryDarkColor,
                                    @ColorInt int accentColor, @ColorInt int textPrimaryColor,
                                    @ColorInt int textSecondaryColor, @ColorInt int listItemBackgroundLightColor,
                                    @ColorInt int listItemBackgroundDarkColor) {
        return new Theme(type, primaryColor, primaryDarkColor, accentColor, textPrimaryColor,
                textSecondaryColor, listItemBackgroundLightColor, listItemBackgroundDarkColor);
    }

    public @ThemeType int getType() {
        return mType;
    }

    public @ColorInt int getPrimaryColor() {
        return mPrimaryColor;
    }

    public @ColorInt int getPrimaryDarkColor() {
        return mPrimaryDarkColor;
    }

    public @ColorInt int getAccentColor() {
        return mAccentColor;
    }

    public @ColorInt int getTextPrimaryColor() {
        return mTextPrimaryColor;
    }

    public @ColorInt int getTextSecondaryColor() {
        return mTextSecondaryColor;
    }

    public @ColorInt int getListItemBackgroundLightColor() {
        return mListItemBackgroundLightColor;
    }

    public @ColorInt int getListItemBackgroundDarkColor() {
        return mListItemBackgroundDarkColor;
    }

    public @DrawableRes int getButtonBackgroundRes() {
        switch (mType) {
            case DEFAULT:
                return  R.drawable.bg_button_default;
            case DRACULA:
                return  R.drawable.bg_button_dracula;
            default:
                return  R.drawable.bg_button_default;
        }
    }

    public @DrawableRes int getListItemButtonBackgroundRes(boolean isDark) {
        switch (mType) {
            case DEFAULT:
                return  isDark ? R.drawable.bg_dark_list_item_button_default : R.drawable.bg_light_list_item_button_default;
            case DRACULA:
                return  isDark ? R.drawable.bg_dark_list_item_button_dracula : R.drawable.bg_light_list_item_button_dracula;
            default:
                return  isDark ? R.drawable.bg_dark_list_item_button_default : R.drawable.bg_light_list_item_button_default;
        }
    }
}
