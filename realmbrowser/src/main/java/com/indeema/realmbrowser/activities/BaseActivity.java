package com.indeema.realmbrowser.activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.indeema.realmbrowser.R;
import com.indeema.realmbrowser.entities.Theme;
import com.indeema.realmbrowser.utils.PreferenceUtils;


/**
 * Created by Ivan Savenko on 21.11.16
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName();

    public static final String EXTRAS_REALM_DB_ENTITY_INDEX = "EXTRAS_REALM_DB_ENTITY_INDEX";
    public static final String EXTRAS_REALM_MODEL_INDEX = "REALM_MODEL_INDEX";
    public static final String EXTRAS_REALM_MODEL_CLASS_NAME = "REALM_MODEL_CLASS_NAME";
    public static final String EXTRAS_REALM_TABLE_PARENT = "REALM_TABLE_PARENT";
    public static final String EXTRAS_SHOW_BACK_BUTTON = "SHOW_BACK_BUTTON";

    protected FrameLayout mBaseContentLayout;
    protected RelativeLayout mToolbar;
    protected TextView mTitleTv;
    protected ImageView mBackBtn;
    protected ImageView mRightBtn;
    protected FrameLayout mToolbarContainer;

    protected Theme mTheme;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        setChildContentView(getLayoutInflater().inflate(layoutResID, null));
    }

    protected void setBaseContentView() {
        setChildContentView(null);
    }

    protected void setChildContentView(View view) {
        View fullLayout = getLayoutInflater().inflate(R.layout.rb_activity_base, null);
        mBaseContentLayout = (FrameLayout) fullLayout.findViewById(R.id.rb_base_content_layout);
        if (view != null) {
            mBaseContentLayout.addView(view);
        }
        initializeToolbar(fullLayout);
        defineTheme();
        applyTheme();
        super.setContentView(fullLayout);
    }

    protected void initializeToolbar(View view) {
        mToolbarContainer = (FrameLayout) view.findViewById(R.id.rb_content_toolbar_layout);
        mToolbar = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.rb_toolbar, null, false);
        mToolbarContainer.addView(mToolbar);
        setZ(8, TypedValue.COMPLEX_UNIT_DIP, mToolbarContainer);
        initializeToolbarChild();
    }

    protected void defineTheme() {
        int themeType = PreferenceUtils.getTheme(this);
        mTheme = Theme.newInstance(this, themeType);
    }

    protected void applyTheme() {
        if (mTheme == null) {
            defineTheme();
        }
        mBaseContentLayout.setBackgroundColor(mTheme.getPrimaryDarkColor());
        setStatusBarColor(mTheme.getPrimaryDarkColor());
        mToolbar.setBackgroundColor(mTheme.getPrimaryColor());
        mTitleTv.setTextColor(mTheme.getTextPrimaryColor());
        mBackBtn.setColorFilter(mTheme.getTextPrimaryColor(), PorterDuff.Mode.MULTIPLY);
        mRightBtn.setColorFilter(mTheme.getTextPrimaryColor(), PorterDuff.Mode.MULTIPLY);
    }

    protected void initializeToolbarChild() {
        mTitleTv = (TextView) mToolbar.findViewById(R.id.rb_toolbar_title_tv);
        mBackBtn = (ImageView) mToolbar.findViewById(R.id.rb_toolbar_back_btn);
        mBackBtn.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        mBackBtn.setOnClickListener(v -> onBackPressed());
        mRightBtn = (ImageView) mToolbar.findViewById(R.id.rb_toolbar_right_btn);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitleTv.setVisibility(View.VISIBLE);
        mTitleTv.setText(title);
    }

    public Theme getBrowserTheme() {
        return mTheme;
    }

    public RelativeLayout getToolbar() {
        return mToolbar;
    }

    public TextView getTitleView() {
        return mTitleTv;
    }

    public String getTitleText() {
        return (String) mTitleTv.getText();
    }

    public ImageView getBackBtn() {
        return mBackBtn;
    }

    public void hideBackBtn() {
        mBackBtn.setVisibility(View.GONE);
    }

    public void showBackBtn() {
        mBackBtn.setVisibility(View.VISIBLE);
    }

    public void showToolbarBackButton(@DrawableRes int iconResId) {
        getBackBtn().setImageResource(iconResId);
        showBackBtn();
    }

    public ImageView getRightBtn() {
        return mRightBtn;
    }

    public void showRightBtn() {
        mRightBtn.setVisibility(View.VISIBLE);
    }

    public void hideRightBtn() {
        mRightBtn.setVisibility(View.GONE);
    }

    public void showToolbarRightButton(@DrawableRes int iconResId) {
        getRightBtn().setImageResource(iconResId);
        showRightBtn();
    }

    protected void setStatusBarColor(@ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(color);
        }
    }

    public void setZ(float value, int unit, View... views) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for (View view : views) {
                view.setZ(TypedValue.applyDimension(unit, value, getResources().getDisplayMetrics()));
            }
        }
    }

}
