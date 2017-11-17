package com.indeema.realmbrowser.sample.utils;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;

/**
 * Created by Ivan Savenko on 16.11.16
 */

public class RealmMigrationUtils {

    public static RealmMigration getRealmMigration() {
        RealmMigration migration = new RealmMigration() {
            @Override
            public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

            }
        };
        return migration;
    }

}
