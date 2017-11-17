package com.indeema.realmbrowser.sample.managers;

import android.util.Log;

import com.indeema.realmbrowser.sample.entities.User;
import com.indeema.realmbrowser.sample.entities.realm.Primitives;
import com.indeema.realmbrowser.sample.entities.realm.UserRealm;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Ivan Savenko on 17.11.16
 */

public class DbManager {

    private static final String TAG = DbManager.class.getSimpleName();

    public static void savePrimitivesTestObjects(int count) {
        Realm realmDB = Realm.getDefaultInstance();
        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (int i = 0; i < count; i++) {
                    Primitives primitives = new Primitives();
                    primitives.setBooleanValue(i % 2 == 0);
                    primitives.setByteValue((byte) i);
                    primitives.setShortValue((short) (100 + i));
                    primitives.setIntValue(1000 * i);
                    primitives.setLongValue(1000000L * i);
                    primitives.setFloatValue(1.01f * i);
                    primitives.setDoubleValue(100.0001d * i);
                    primitives.setStringValue("index: " + i);
                    realm.copyToRealm(primitives);
                }
            }
        });
        realmDB.close();
    }

    public static void saveUser(final User user) {
        Realm realmDB = Realm.getDefaultInstance();
        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.d(TAG, "saveUser -> " + user);
                UserRealm userRealm = new UserRealm(user);
                realm.copyToRealmOrUpdate(userRealm);
            }
        });
        realmDB.close();
    }

    public static List<User> getAllUsers() {
        Log.d(TAG, "getAllUsers...");

        Realm realmDB = Realm.getDefaultInstance();
        RealmResults<UserRealm> results = realmDB.where(UserRealm.class).findAll();

        Log.d(TAG, "getAllUsers -> results.size(): " + results.size());

        List<User> users = new ArrayList<>();
        for (UserRealm userRealm : results) {
            User testInfo = userRealm.convertToUser();
            Log.d(TAG, "getAllUsers -> " + testInfo);
            users.add(testInfo);
        }
        realmDB.close();

        return users;
    }

    public static void clearTable(final Class<? extends RealmObject> dbTable) {
        Realm realmDB = Realm.getDefaultInstance();
        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(dbTable);
            }
        });
        realmDB.close();
    }


}
