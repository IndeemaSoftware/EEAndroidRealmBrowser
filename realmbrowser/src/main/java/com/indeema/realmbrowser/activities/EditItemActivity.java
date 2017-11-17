package com.indeema.realmbrowser.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.SwitchCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.indeema.realmbrowser.R;
import com.indeema.realmbrowser.entities.TableModel;
import com.indeema.realmbrowser.entities.ValueModel;
import com.indeema.realmbrowser.utils.TableModelProvider;

import java.lang.reflect.Field;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.RealmObjectSchema;

/**
 * @author Ruslan Stosyk
 * Date: November, 14, 2017
 * Time: 11:46 AM
 */

public class EditItemActivity extends BaseActivity {

    public static final int REQUEST_CODE_EDIT_ITEM = 1;

    private TableModel mModel;
    private LinearLayout mContainer;
    private String mRealmModelClassName;

    public static void start(@NonNull Activity activity) {
        Intent intent = new Intent(activity, EditItemActivity.class);
        activity.startActivity(intent);
    }

    public static void startForResult(@NonNull Activity activity, String realmModelClassName, int requestCode) {
        Intent intent = new Intent(activity, EditItemActivity.class);
        intent.putExtra(EXTRAS_REALM_MODEL_CLASS_NAME, realmModelClassName);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rb_activity_edit);

        mRealmModelClassName = getIntent().getStringExtra(EXTRAS_REALM_MODEL_CLASS_NAME);

        setTitle("Edit " + mRealmModelClassName);
        showBackBtn();
        showToolbarBackButton(R.drawable.ic_close_white_48dp);
        showToolbarRightButton(R.drawable.ic_check_white_48dp);
        getRightBtn().setOnClickListener(v -> {
            save();
        });

        mContainer = (LinearLayout) findViewById(R.id.edit_container);
        mContainer.setBackgroundColor(mTheme.getListItemBackgroundDarkColor());

        mModel = TableModelProvider.getInstance().getTableModel();
        createEditorItem();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    private void save() {
        DynamicRealmObject realmObject = mModel.getDynamicRealmObject();
        final DynamicRealm dynamicRealm = (DynamicRealm) realmObject.realmGet$proxyState().getRealm$realm();
        dynamicRealm.executeTransaction(realm -> {
            boolean isSaved = true;
            for (int i = 0; i < mContainer.getChildCount(); i++) {
                View view = mContainer.getChildAt(i);
                ValueModel valueModel = (ValueModel) view.getTag();
                if (valueModel != null && valueModel.getType() != ValueModel.DATE && valueModel.getType() != ValueModel.OBJECT &&
                        valueModel.getType() != ValueModel.GENERIC_OBJECT && valueModel.getType() != ValueModel.UNKNOWN) {

//                    Object object = realmObject.get(valueModel.getField().getName());

                    if (valueModel.getType() == ValueModel.BOOLEAN) {
                        SwitchCompat switchCompat = (SwitchCompat) view.findViewById(R.id.boolean_object_switch);
                        if (switchCompat != null) {
                            realmObject.setBoolean(valueModel.getField().getName(), switchCompat.isChecked());
                        }

                    } else {
                        TextInputEditText editText = (TextInputEditText) view.findViewById(R.id.edit_input_edit_text);
                        if (editText != null) {
                            String value = editText.getText().toString();
                            try {
                                switch (valueModel.getType()) {
                                    case ValueModel.SHORT:
                                        realmObject.setShort(valueModel.getField().getName(), Short.valueOf(value));
                                        break;
                                    case ValueModel.INTEGER:
                                        realmObject.setInt(valueModel.getField().getName(), Integer.valueOf(value));
                                        break;
                                    case ValueModel.LONG:
                                        realmObject.setLong(valueModel.getField().getName(), Long.valueOf(value));
                                        break;
                                    case ValueModel.FLOAT:
                                        realmObject.setFloat(valueModel.getField().getName(), Float.valueOf(value));
                                        break;
                                    case ValueModel.DOUBLE:
                                        realmObject.setDouble(valueModel.getField().getName(), Double.valueOf(value));
                                        break;
                                    case ValueModel.STRING:
                                        realmObject.setString(valueModel.getField().getName(), value);
                                        break;
                                }

                            } catch (NumberFormatException ex) {
                                ex.printStackTrace();
                                isSaved = false;
                            }

                        }
                    }
                }

            }

            if (isSaved) {
                sendResult();
            } else {
                Toast.makeText(this, "Save object error! Check data format.", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void sendResult() {
        setResult(RESULT_OK);
        finish();
    }

    private void createEditorItem() {
        for (int i = 0; i < mModel.getValues().size(); i++) {
            ValueModel valueModel = mModel.getValues().get(i);
            String className = valueModel.getField().getType().getSimpleName();
            String name = className + "  " + valueModel.getField().getName();

            DynamicRealmObject realmObject = mModel.getDynamicRealmObject();
            DynamicRealm dynamicRealm = (DynamicRealm) realmObject.realmGet$proxyState().getRealm$realm();
            RealmObjectSchema objectSchema = dynamicRealm.getSchema().get(mRealmModelClassName);
            boolean hasField = objectSchema.hasField(valueModel.getField().getName());

            if (hasField) {
                if (valueModel.getType() == ValueModel.BOOLEAN) {
                    mContainer.addView(createSwitchView(valueModel, name, (Boolean) valueModel.getValue()));

                } else {
                    boolean isPrimaryKey = objectSchema.isPrimaryKey(valueModel.getField().getName());
                    if (isPrimaryKey || valueModel.getType() == ValueModel.DATE ||
                            valueModel.getType() == ValueModel.OBJECT || valueModel.getType() == ValueModel.GENERIC_OBJECT) {
                        mContainer.addView(createValueView(valueModel, name, valueModel.getValueToString(), isPrimaryKey));
                    } else if (valueModel.getType() != ValueModel.UNKNOWN) {
                        mContainer.addView(createEditView(valueModel, name, valueModel.getValueToString()));
                    }
                }
            }
            mContainer.addView(createDividerView());
        }
    }

    private View createDividerView() {
        View view = new View(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
        view.setLayoutParams(params);
        view.setBackgroundColor(mTheme.getTextPrimaryColor());
        view.setAlpha(0.5f);
        return view;
    }

    private View createValueView(ValueModel valueModel, String name, String value, boolean isPrimaryKey) {
        View view = View.inflate(this, R.layout.rb_item_realm_object_value, null);
        ImageView primaryKeyIv = (ImageView) view.findViewById(R.id.primary_key_iv);
        if (isPrimaryKey) {
            primaryKeyIv.setColorFilter(mTheme.getTextPrimaryColor(), PorterDuff.Mode.MULTIPLY);
            primaryKeyIv.setVisibility(View.VISIBLE);
        } else {
            primaryKeyIv.setVisibility(View.GONE);
        }

        TextView textViewName = (TextView) view.findViewById(R.id.object_name);
        textViewName.setTextColor(mTheme.getTextSecondaryColor());
        textViewName.setText(name);

        TextView textViewValue = (TextView) view.findViewById(R.id.object_value);
        textViewValue.setTextColor(mTheme.getTextPrimaryColor());
        textViewValue.setText(value);

        view.setTag(valueModel);
        return view;
    }

    private View createSwitchView(ValueModel valueModel, String name, boolean value) {
        View view = View.inflate(this, R.layout.rb_item_realm_object_switch, null);
        TextView textViewName = (TextView) view.findViewById(R.id.boolean_object_name);
        textViewName.setTextColor(mTheme.getTextSecondaryColor());
        textViewName.setText(name);

        TextView textViewValue = (TextView) view.findViewById(R.id.boolean_object_value);
        textViewValue.setTextColor(mTheme.getTextPrimaryColor());
        textViewValue.setText(String.valueOf(value));

        SwitchCompat switchCompat = (SwitchCompat) view.findViewById(R.id.boolean_object_switch);
        switchCompat.setTextColor(mTheme.getTextPrimaryColor());
        switchCompat.setChecked(value);
        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            textViewValue.setText(String.valueOf(isChecked));
        });

        int[] thumbColors = new int[] {mTheme.getTextSecondaryColor(), mTheme.getTextPrimaryColor()};
        int[] trackColors = new int[] {mTheme.getPrimaryColor(), mTheme.getPrimaryDarkColor()};
        setSwitchCompatColor(switchCompat, thumbColors, trackColors);

        view.setTag(valueModel);
        return view;
    }

    private View createEditView(ValueModel valueModel, String name, String value) {
        View view = View.inflate(this, R.layout.rb_item_realm_object_edit, null);
        TextInputLayout textInputLayout = (TextInputLayout) view.findViewById(R.id.edit_text_input_layout);
        textInputLayout.setHint(name);
        textInputLayout.setHintAnimationEnabled(false);
        setInputTextLayoutColor(textInputLayout, mTheme.getTextSecondaryColor());

        TextInputEditText textInputEditText = (TextInputEditText) view.findViewById(R.id.edit_input_edit_text);
        textInputEditText.setText(value);
        textInputEditText.setTextColor(mTheme.getTextPrimaryColor());
        textInputEditText.setHintTextColor(mTheme.getTextSecondaryColor());
        textInputEditText.setHighlightColor(mTheme.getTextSecondaryColor());

        setInputTextLineColor(textInputEditText, mTheme.getTextSecondaryColor());


        switch (valueModel.getType()) {
            case ValueModel.SHORT:
                textInputEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                textInputEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
                break;

            case ValueModel.INTEGER:
                textInputEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                textInputEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                break;

            case ValueModel.LONG:
                textInputEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                textInputEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(19)});
                break;

            case ValueModel.FLOAT:
                textInputEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                textInputEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
                break;

            case ValueModel.DOUBLE:
                textInputEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                textInputEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
                break;

            case ValueModel.STRING:
                textInputEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                textInputEditText.setSingleLine(false);
                break;
        }
        view.setTag(valueModel);

        textInputLayout.setHintAnimationEnabled(true);

        return view;
    }

    public static void setSwitchCompatColor(SwitchCompat switchCompat, int[] thumbColors, int[] trackColors) {
        int[][] states = new int[][] {new int[] {-android.R.attr.state_checked}, new int[] {android.R.attr.state_checked}};
//        AppCompatCheckBox checkBox = (AppCompatCheckBox) findViewById(R.id.checkbox);
//        checkBox.setSupportButtonTintList(new ColorStateList(states, thumbColors));
        DrawableCompat.setTintList(DrawableCompat.wrap(switchCompat.getThumbDrawable()), new ColorStateList(states, thumbColors));
        DrawableCompat.setTintList(DrawableCompat.wrap(switchCompat.getTrackDrawable()), new ColorStateList(states, trackColors));
    }

    public static void setInputTextLineColor(TextInputEditText editText, @ColorInt int color) {
        Drawable background = editText.getBackground();
        DrawableCompat.setTint(background, color);
        editText.setBackground(background);
    }

    public static void setInputTextLayoutColor(TextInputEditText editText, @ColorInt int color) {
        TextInputLayout til = (TextInputLayout) editText.getParent();
        setInputTextLayoutColor(til, color);
    }

    public static void setInputTextLayoutColor(TextInputLayout til, @ColorInt int color) {
        try {
            Field fDefaultTextColor = TextInputLayout.class.getDeclaredField("mDefaultTextColor");
            fDefaultTextColor.setAccessible(true);
            fDefaultTextColor.set(til, new ColorStateList(new int[][]{{0}}, new int[]{ color }));

            Field fFocusedTextColor = TextInputLayout.class.getDeclaredField("mFocusedTextColor");
            fFocusedTextColor.setAccessible(true);
            fFocusedTextColor.set(til, new ColorStateList(new int[][]{{0}}, new int[]{ color }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
