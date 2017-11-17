package com.indeema.realmbrowser.activities;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.indeema.realmbrowser.R;
import com.indeema.realmbrowser.RealmBrowser;
import com.indeema.realmbrowser.adapters.TableItemsAdapter;
import com.indeema.realmbrowser.entities.RbEntity;
import com.indeema.realmbrowser.entities.TableModel;
import com.indeema.realmbrowser.entities.ValueModel;
import com.indeema.realmbrowser.utils.PreferenceUtils;
import com.indeema.realmbrowser.utils.RbUtils;
import com.indeema.realmbrowser.utils.TableModelProvider;
import com.indeema.realmbrowser.utils.ValueModelProvider;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmResults;


/**
 * Created by Ivan Savenko on 15.11.16
 */

public class TableItemsActivity extends BaseActivity implements TableItemsAdapter.AdapterListener {

    private Realm mRealm;
    private DynamicRealm mDynamicRealm;

    private Class<? extends RealmModel> mRealmModelClass;

    private RealmResults<DynamicRealmObject> mResults;

    //    private AbstractList<? extends RealmModel> mRealmObjects;
    private ValueModel mValueModel;

    private TableItemsAdapter mAdapter;
    private LinearLayout mTableHeaderContainer;
    private RelativeLayout mTablePageController;
    private TextView mPreviousPageTv;
    private TextView mNextPageTv;
    private TextView mCurrentPageTv;

    private List<Field> mTmpSelectedFieldList;
    private List<Field> mSelectedFieldList;
    private List<Field> mFieldsList;
    private Set<String> mCheckedFields = null;
    private List<TableModel> mData;

    private boolean isSorted = false;
    private int mRealmDBEntityIndex;


    public static void start(@NonNull Activity activity, @NonNull Integer realmDBEntityIndex, @NonNull Integer realmModelIndex) {
        Intent intent = new Intent(activity, TableItemsActivity.class);
        intent.putExtra(EXTRAS_REALM_DB_ENTITY_INDEX, realmDBEntityIndex);
        intent.putExtra(EXTRAS_REALM_MODEL_INDEX, realmModelIndex);
        activity.startActivity(intent);
    }

    private static void start(@NonNull Activity activity, String parent, @NonNull Integer realmDBEntityIndex) {
        Intent intent = new Intent(activity, TableItemsActivity.class);
        intent.putExtra(EXTRAS_REALM_DB_ENTITY_INDEX, realmDBEntityIndex);
        intent.putExtra(EXTRAS_REALM_TABLE_PARENT, parent);
        activity.startActivity(intent);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRealmDBEntityIndex = getIntent().getIntExtra(EXTRAS_REALM_DB_ENTITY_INDEX, 0);
        RbEntity rbEntity = RealmBrowser.getInstance().getRbEntities().get(mRealmDBEntityIndex);

        mRealm = Realm.getInstance(rbEntity.getRealmConfiguration());
        mDynamicRealm = DynamicRealm.getInstance(rbEntity.getRealmConfiguration());

        if (getIntent().getExtras().containsKey(EXTRAS_REALM_MODEL_INDEX)) {
            int realmModelIndex = getIntent().getIntExtra(EXTRAS_REALM_MODEL_INDEX, 0);
            mRealmModelClass = rbEntity.getRealmModels().get(realmModelIndex);
            if (mRealmModelClass != null) {
                mResults = mDynamicRealm.where(mRealmModelClass.getSimpleName()).findAll();
            }

        } else {
            mValueModel = ValueModelProvider.getInstance().getValueModel();
            if (RbUtils.isGeneric(mValueModel.getField())) {
                ParameterizedType pType = (ParameterizedType) mValueModel.getField().getGenericType();
                Class<?> pTypeClass = (Class<?>) pType.getActualTypeArguments()[0];
                mRealmModelClass = (Class<? extends RealmModel>) pTypeClass;
            } else {
                mRealmModelClass = (Class<? extends RealmModel>) mValueModel.getField().getType();
            }

        }

        if (mValueModel == null && (mResults == null || mResults.size() == 0)) {
            setContentView(R.layout.rb_activity_table_empty);

        } else {

            setContentView(R.layout.rb_activity_table);

            mTableHeaderContainer = (LinearLayout) findViewById(R.id.tableHeaderContainer);
            mTableHeaderContainer.setBackgroundColor(mTheme.getPrimaryDarkColor());
            setZ(4, TypedValue.COMPLEX_UNIT_DIP, mTableHeaderContainer);

            mTablePageController = (RelativeLayout) findViewById(R.id.tablePageController);
            mTablePageController.setBackgroundColor(mTheme.getPrimaryColor());
            setZ(8, TypedValue.COMPLEX_UNIT_DIP, mTablePageController);

            TextView currentPageLabelTv = (TextView) findViewById(R.id.currentPageLabelTv);
            currentPageLabelTv.setTextColor(mTheme.getTextPrimaryColor());

            mCurrentPageTv = (TextView) findViewById(R.id.currentPageTv);
            mCurrentPageTv.setTextColor(mTheme.getTextPrimaryColor());

            mPreviousPageTv = (TextView) findViewById(R.id.previousPageTv);
            mPreviousPageTv.setBackgroundResource(mTheme.getButtonBackgroundRes());
            mPreviousPageTv.setTextColor(mTheme.getTextPrimaryColor());
            mPreviousPageTv.setOnClickListener(v -> {
                mAdapter.getFilter().performPreviousPageFiltering();
                updatePageControls();
            });

            mNextPageTv = (TextView) findViewById(R.id.nextPageTv);
            mNextPageTv.setBackgroundResource(mTheme.getButtonBackgroundRes());
            mNextPageTv.setTextColor(mTheme.getTextPrimaryColor());
            mNextPageTv.setOnClickListener(v -> {
                mAdapter.getFilter().performNextPageFiltering();
                updatePageControls();
            });

            mSelectedFieldList = new ArrayList<>();
            mTmpSelectedFieldList = new ArrayList<>();
            mFieldsList = new ArrayList<>();

//            mFieldsList.addAll(Arrays.asList(mRealmModelClass.getDeclaredFields()));
            for (Field field : mRealmModelClass.getDeclaredFields()) {
                if (!field.getName().equalsIgnoreCase("$change") &&
                        !field.getName().equalsIgnoreCase("serialVersionUID")) {
                    mFieldsList.add(field);
                }
            }

            initTableData();
        }

        setTitle();
        showBackBtn();
        showToolbarRightButton(R.drawable.ic_more_vert_white_48dp);
        getRightBtn().setOnClickListener(v -> {
            createPopupMenu(TableItemsActivity.this, getRightBtn());
        });
    }

    public void setTitle() {
        String parent = getIntent().getStringExtra(EXTRAS_REALM_TABLE_PARENT);
        String tableName = mRealmModelClass != null ? mRealmModelClass.getSimpleName() : "-";
        boolean hasParent = !TextUtils.isEmpty(parent);
        if (hasParent) {
            getTitleView().setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            setTitle(parent + "\n -- " + tableName);
        } else {
            setTitle(tableName);
        }
    }

    private void initTableData() {
        initFields();
        getTableData();
        initRecyclerView();
        updatePageControls();
        updateColumnTitle(mSelectedFieldList);
    }

    private void getTableData() {
        if (mResults != null) {
            mData = RbUtils.getTableModels(mResults, mSelectedFieldList);
        } else if (mValueModel != null) {
            if (mValueModel.getType() == ValueModel.OBJECT && mValueModel.getValue() instanceof DynamicRealmObject) {
                mData = RbUtils.getTableModels((DynamicRealmObject) mValueModel.getValue(), mSelectedFieldList);
            } else if (mValueModel.getType() == ValueModel.GENERIC_OBJECT && mValueModel.getValue() instanceof RealmList) {
                mData = RbUtils.getTableModels((RealmList<DynamicRealmObject>) mValueModel.getValue(), mSelectedFieldList);
            } else {
                mData = new ArrayList<>();
            }
        } else {
            mData = new ArrayList<>();
        }
    }

    private void initFields() {
        mCheckedFields = PreferenceUtils.getCheckedFields(this, mRealmModelClass.getSimpleName());
        if (mCheckedFields == null) {
            selectDefaultFields();
        } else {
            restoreCheckedFields();
        }
    }

    private void selectDefaultFields() {
        mSelectedFieldList.clear();
        mSelectedFieldList.addAll(mFieldsList);
    }

    private void restoreCheckedFields() {
        mSelectedFieldList.clear();
        for (Field field : mFieldsList) {
            if (mCheckedFields.contains(field.getName())) {
                mSelectedFieldList.add(field);
            }
        }
    }

    private void initRecyclerView() {
        mAdapter = new TableItemsAdapter(this, mTheme, mData, this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private void updateColumnTitle(List<Field> columnsList) {
        mTableHeaderContainer.removeAllViews();
        for (int i = 0; i < columnsList.size() + 1; i++) {
            if (i == 0) {
                mTableHeaderContainer.addView(createHeaderTextItem("#", true, i));
            } else {
                mTableHeaderContainer.addView(createHeaderTextItem(columnsList.get(i - 1).getName(), false, i));
            }
        }
    }

    @Override
    public void onRowItemClick(@NonNull TableModel model, ValueModel valueModel, int position, int valueIndex) {
        Log.d(TAG, "onRowItemClick -> position: " + position + ", valueIndex: " + valueIndex + ", " + valueModel);
        if (valueModel.getType() == ValueModel.OBJECT || valueModel.getType() == ValueModel.GENERIC_OBJECT) {
            ValueModelProvider.getInstance().setValueModel(valueModel);
            start(this, getTitleText(), mRealmDBEntityIndex);

        } else {
            Toast.makeText(this, valueModel.getValueToString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRowItemLongClick(@NonNull TableModel model, ValueModel valueModel, int position, int valueIndex) {
        Log.d(TAG, "onRowItemLongClick -> position: " + position + ", valueIndex: " + valueIndex + ", " + valueModel);
//        String value = valueModel.getValue().toString();
        String value = valueModel.getValueToString();
        if (!TextUtils.isEmpty(value)) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("RB", value);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Copied to Clip Board", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRowEditClick(@NonNull TableModel model) {
        TableModelProvider.getInstance().setTableModel(model);
//        EditActivity.start(this);
        EditItemActivity.startForResult(this, mRealmModelClass.getSimpleName(), EditItemActivity.REQUEST_CODE_EDIT_ITEM);
    }

    @Override
    public void onRowDeleteClick(@NonNull TableModel model) {
        Log.d(TAG, "onRowDeleteClick -> " + model.toString());
        showDeleteItemDialog(model);
    }

    @Override
    protected void onResume() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mRealm != null) {
            mRealm.close();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EditItemActivity.REQUEST_CODE_EDIT_ITEM) {
            if (resultCode == RESULT_OK) {
                initTableData();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Nullable
    public RealmList<? extends RealmObject> invokeMethod(Object realmObject, String methodName) {
        RealmList<? extends RealmObject> result = null;
        try {
            Method method = realmObject.getClass().getMethod(methodName);
            result = (RealmList<? extends RealmObject>) method.invoke(realmObject);
        } catch (NoSuchMethodException e) {
            Log.e(TAG, e.toString());
        } catch (InvocationTargetException e) {
            Log.e(TAG, e.toString());
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.toString());
        }

        return result;
    }

    private void saveCheckedFields() {
        mCheckedFields = new HashSet<>();
        for (Field filed : mSelectedFieldList) {
            mCheckedFields.add(filed.getName());
        }
        PreferenceUtils.saveCheckedFields(TableItemsActivity.this,
                mRealmModelClass.getSimpleName(), mCheckedFields);
    }

    private TextView createHeaderTextItem(String text, boolean isNumColumn, int tag) {
        TextView textView = new TextView(this);
        textView.setSingleLine(true);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER_VERTICAL);
//        textView.setTextColor(ContextCompat.getColor(this, R.color.rb_table_text_color));
        textView.setTextColor(mTheme.getTextPrimaryColor());
        textView.setTypeface(null, Typeface.BOLD);
        textView.setPadding(getResources().getDimensionPixelSize(R.dimen.table_text_padding), 0, getResources().getDimensionPixelSize(R.dimen.table_text_padding), 0);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        LinearLayout.LayoutParams layoutParams;
        if (isNumColumn) {
            layoutParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.table_num_column_width),
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            layoutParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.table_column_width),
                    ViewGroup.LayoutParams.MATCH_PARENT);
        }
//        layoutParams.topMargin = getResources().getDimensionPixelSize(R.dimen.table_header_margin_top);
        textView.setLayoutParams(layoutParams);
        textView.setTag(tag);
        textView.setOnClickListener(onHeadClickListener);

        int[] attrs = new int[]{android.R.attr.selectableItemBackground};
        TypedArray ta = obtainStyledAttributes(attrs);
        Drawable drawableFromTheme = ta.getDrawable(0);
        ta.recycle();
        textView.setBackground(drawableFromTheme);

        return textView;
    }

    private View.OnClickListener onHeadClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int id = (int) v.getTag();
            Collections.sort(mData, new Comparator<TableModel>() {
                @Override
                public int compare(TableModel model1, TableModel model2) {
                    if (id == 0) {
                        Integer id1 = model1.getId();
                        Integer id2 = model2.getId();
                        return isSorted ? id2.compareTo(id1) : id1.compareTo(id2);

                    } else {
                        int type = model1.getValues().get(id - 1).getType();
                        Object object1 = model1.getValues().get(id - 1).getValue();
                        Object object2 = model2.getValues().get(id - 1).getValue();

                        switch (type) {
                            case ValueModel.BYTE:
                            case ValueModel.SHORT:
                            case ValueModel.INTEGER:
                            case ValueModel.LONG:
                                return isSorted ? ((Long) object2).compareTo((Long) object1) :
                                        ((Long) object1).compareTo((Long) object2);

                            case ValueModel.FLOAT:
                                return isSorted ? ((Float) object2).compareTo((Float) object1) :
                                        ((Float) object1).compareTo((Float) object2);

                            case ValueModel.DOUBLE:
                                return isSorted ? ((Double) object2).compareTo((Double) object1) :
                                        ((Double) object1).compareTo((Double) object2);

                            case ValueModel.BOOLEAN:
                            case ValueModel.DATE:
                            case ValueModel.STRING:
                            case ValueModel.OBJECT:
                                return isSorted ? object2.toString().compareTo(object1.toString()) :
                                        object1.toString().compareTo(object2.toString());
                        }

                        return 0;
                    }
                }
            });
            isSorted = !isSorted;
            updateRecycler();
        }
    };

    private void createPopupMenu(@NonNull Context context, @NonNull View anchor) {
        PopupMenu popup = new PopupMenu(context, anchor);
        popup.getMenuInflater().inflate(R.menu.rb_table_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.set_columns) {
                if (mData != null && mData.size() > 0) {
                    showColumnsDialog();
                }
            }
            return true;
        });
        popup.show();
    }

    private void showColumnsDialog() {
        final String[] items = new String[mFieldsList.size()];
        for (int i = 0; i < items.length; i++) {
            Field field = mFieldsList.get(i);
            items[i] = field.getName();
        }

        boolean[] checkedItems = new boolean[mFieldsList.size()];
        for (int i = 0; i < checkedItems.length; i++) {
            checkedItems[i] = mSelectedFieldList.contains(mFieldsList.get(i));
        }

        mTmpSelectedFieldList.clear();
        mTmpSelectedFieldList.addAll(mSelectedFieldList);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Set Columns to display")
                .setMultiChoiceItems(items, checkedItems, (dialog, indexSelected, isChecked) -> {
                    Field field = mFieldsList.get(indexSelected);
                    if (isChecked) {
                        mTmpSelectedFieldList.add(field);
                    } else if (mTmpSelectedFieldList.contains(field)) {
                        mTmpSelectedFieldList.remove(field);
                    }

                })
                .setPositiveButton("OK", (dialog, id) -> {
                    if (mTmpSelectedFieldList.isEmpty()) {
                        selectDefaultFields();
                    } else {
                        mSelectedFieldList.clear();
                        mSelectedFieldList.addAll(mTmpSelectedFieldList);
                    }
                    updateColumnTitle(mSelectedFieldList);

                    getTableData();

                    updateRecycler();
                    saveCheckedFields();

                })
                .setNegativeButton("Cancel", (dialog, id) -> {

                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteItemDialog(final TableModel model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Delete Item?")
                .setMessage("Warning! Any data changes could affect on functionality of observed app!")
                .setPositiveButton("OK", (dialog, id) -> {
                    DynamicRealmObject realmObject = model.getDynamicRealmObject();

                    //TODO delete all nested objects
                    for (ValueModel valueModel : model.getValues()) {
                        if (valueModel.getType() == ValueModel.OBJECT) {
                            Log.d(TAG, "showDeleteItemDialog -> OBJECT valueModel: " + valueModel);
                            DynamicRealmObject nestedRealmObject = (DynamicRealmObject) valueModel.getValue();
                            deleteDynamicRealmObject(nestedRealmObject);

                        } else if (valueModel.getType() == ValueModel.GENERIC_OBJECT) {
                            Log.d(TAG, "showDeleteItemDialog -> GENERIC_OBJECT valueModel: " + valueModel);
                            RealmList<DynamicRealmObject> nestedRealmList = (RealmList<DynamicRealmObject>) valueModel.getValue();
                            if (nestedRealmList.size() > 0) {
                                deleteDynamicRealmObjects(nestedRealmList);
                            }
                        }
                    }

                    deleteDynamicRealmObject(realmObject);

                    boolean remove = mData.remove(model);
                    Log.d(TAG, "showDeleteItemDialog -> remove: " + remove);

                    updateRecycler();

                })
                .setNegativeButton("Cancel", (dialog, id) -> {

                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteDynamicRealmObject(DynamicRealmObject realmObject) {
        final DynamicRealm realm = (DynamicRealm) realmObject.realmGet$proxyState().getRealm$realm();
        realm.executeTransaction(realm1 -> {
            realmObject.deleteFromRealm();
        });
    }

    private void deleteDynamicRealmObjects(RealmList<DynamicRealmObject> realmList) {
        final DynamicRealm realm = (DynamicRealm) realmList.first().realmGet$proxyState().getRealm$realm();
        realm.executeTransaction(realm1 -> {
            realmList.deleteAllFromRealm();
        });
    }

    private void updateRecycler() {
        if (mAdapter != null) {
            mAdapter.setFieldList(mData);
            updatePageControls();
        }
    }

    private void updatePageControls() {
        if (mAdapter != null && mAdapter.getFilter().isPaginationEnable()) {
            setPreviousPageAcpEnabled(mAdapter.getFilter().hasPreviousPage());
            setNextPageAcpEnabled(mAdapter.getFilter().hasNextPage());
            setCurrentPageText(mAdapter.getFilter().getCurrentPage(), mAdapter.getFilter().getPagesCount());
        }
    }

    private void setPreviousPageAcpEnabled(boolean enabled) {
        mPreviousPageTv.setEnabled(enabled);
        mPreviousPageTv.setAlpha(enabled ? 1.0f : 0.36f);
    }

    private void setNextPageAcpEnabled(boolean enabled) {
        mNextPageTv.setEnabled(enabled);
        mNextPageTv.setAlpha(enabled ? 1.0f : 0.36f);
    }

    private void setCurrentPageText(int currentPage, int pagesCount) {
        String currentPageText = String.format(getResources().getString(R.string.label_current_page), currentPage, pagesCount);
        mCurrentPageTv.setText(currentPageText);
    }




}
