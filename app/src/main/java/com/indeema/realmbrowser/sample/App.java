package com.indeema.realmbrowser.sample;

import android.app.Application;

import com.indeema.realmbrowser.sample.utils.RealmMigrationUtils;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Ivan Savenko on 16.11.16
 */

public class App extends Application {

    public RealmConfiguration mRealmConfiguration1;
    public RealmConfiguration mRealmConfiguration2;

    @Override
    public void onCreate() {
        super.onCreate();
        initRealmDB();
    }

    private void initRealmDB() {
        Realm.init(getApplicationContext());

        mRealmConfiguration1 = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(mRealmConfiguration1);

        mRealmConfiguration2 = new RealmConfiguration.Builder()
                .schemaVersion(getResources().getInteger(R.integer.realm_schema_version)) // Must be bumped when the schema changes
                .migration(RealmMigrationUtils.getRealmMigration())
                .name(getResources().getString(R.string.realm_file_name))
                .build();
        Realm.setDefaultConfiguration(mRealmConfiguration2);
    }

}
