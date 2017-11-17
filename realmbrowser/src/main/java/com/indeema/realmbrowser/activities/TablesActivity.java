package com.indeema.realmbrowser.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.indeema.realmbrowser.R;
import com.indeema.realmbrowser.RealmBrowser;
import com.indeema.realmbrowser.adapters.SpaceItemDecoration;
import com.indeema.realmbrowser.adapters.TablesAdapter;
import com.indeema.realmbrowser.entities.RbEntity;

import java.util.List;

import io.realm.RealmModel;

/**
 * Created by Ivan Savenko on 15.11.16
 * <p>
 * Updated by Ivan Savenko on 09.11.17
 */

public class TablesActivity extends BaseActivity implements TablesAdapter.AdapterListener {

    private int mRealmDBEntityIndex;

    public static void start(@NonNull Activity activity, @NonNull Integer realmDBEntityIndex, boolean showBackButton) {
        Intent intent = new Intent(activity, TablesActivity.class);
        intent.putExtra(EXTRAS_REALM_DB_ENTITY_INDEX, realmDBEntityIndex);
        intent.putExtra(EXTRAS_SHOW_BACK_BUTTON, showBackButton);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rb_activity_list);

        boolean showBackButton = getIntent().getBooleanExtra(EXTRAS_SHOW_BACK_BUTTON, false);
        showToolbarBackButton(showBackButton ? R.drawable.ic_arrow_back_white_48dp : R.drawable.ic_close_white_48dp);

        mRealmDBEntityIndex = getIntent().getIntExtra(EXTRAS_REALM_DB_ENTITY_INDEX, 0);
        RbEntity rbEntity = RealmBrowser.getInstance().getRbEntities().get(mRealmDBEntityIndex);

        setTitle("RB: " + rbEntity.getRBEntityTitle());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rbRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        int bottomSpace = (int) (getResources().getDisplayMetrics().density * 2);
        recyclerView.addItemDecoration(new SpaceItemDecoration(0, bottomSpace, 0, 0));

        List<Class<? extends RealmModel>> models = rbEntity.getRealmModels();
        TablesAdapter adapter = new TablesAdapter(this, mTheme, models, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(@NonNull Class<? extends RealmModel> rbModel, int position) {
        TableItemsActivity.start(this, mRealmDBEntityIndex, position);
    }

}
