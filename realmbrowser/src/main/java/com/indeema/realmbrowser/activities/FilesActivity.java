package com.indeema.realmbrowser.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.indeema.realmbrowser.R;
import com.indeema.realmbrowser.RealmBrowser;
import com.indeema.realmbrowser.adapters.FilesAdapter;
import com.indeema.realmbrowser.adapters.SpaceItemDecoration;
import com.indeema.realmbrowser.entities.RbEntity;

/**
 * Created by Ivan Savenko on 15.11.16
 *
 * Updated by Ivan Savenko on 22.11.16
 *
 * Updated by Ivan Savenko on 09.11.17
 */

public class FilesActivity extends BaseActivity implements FilesAdapter.AdapterListener {

    public static void start(@NonNull Activity activity) {
        Intent intent = new Intent(activity, FilesActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rb_activity_list);
        showToolbarBackButton(R.drawable.ic_close_white_48dp);
        setTitle("RB: " + getTitle());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rbRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        int bottomSpace = (int) (getResources().getDisplayMetrics().density * 2);
        recyclerView.addItemDecoration(new SpaceItemDecoration(0, bottomSpace, 0, 0));

        FilesAdapter adapter = new FilesAdapter(this, mTheme, RealmBrowser.getInstance().getRbEntities(), this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(@NonNull RbEntity rbEntity, int position) {
        TablesActivity.start(this, position, true);
    }

}
