package com.indeema.realmbrowser.entities;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.realm.RealmConfiguration;
import io.realm.RealmModel;

/**
 * Created by Ivan Savenko on 21.11.16
 *
 * Updated by Ivan Savenko on 22.11.16
 */

public class RbEntity {

    private final RealmConfiguration mRealmConfiguration;

    private List<Class<? extends RealmModel>> mRealmModels;

    private String title;

    @SafeVarargs
    public RbEntity(@NonNull RealmConfiguration realmConfiguration, Class<? extends RealmModel>... realmModelList) {
        mRealmConfiguration = realmConfiguration;
        setRealmModels(realmModelList);
    }

    public RbEntity(RealmConfiguration realmConfiguration) {
        mRealmConfiguration = realmConfiguration;
        mRealmModels = new ArrayList<>();
    }

    private void sortModelsByName(List<Class<? extends RealmModel>> models) {
        Collections.sort(models, new Comparator<Class<? extends RealmModel>>() {
            @Override
            public int compare(Class<? extends RealmModel> m1, Class<? extends RealmModel> m2) {
                return m1.getSimpleName().compareToIgnoreCase(m2.getSimpleName());
            }
        });
    }

    public final RealmConfiguration getRealmConfiguration() {
        return mRealmConfiguration;
    }

    public final String getRBEntityTitle() {
        return mRealmConfiguration.getRealmFileName() + " v" + mRealmConfiguration.getSchemaVersion();
    }

    public final List<Class<? extends RealmModel>> getRealmModels() {
        if (mRealmModels == null || mRealmModels.size() == 0) {
            mRealmModels = new ArrayList<>();
            mRealmModels.addAll(mRealmConfiguration.getRealmObjectClasses());
            sortModelsByName(mRealmModels);
        }
        return mRealmModels;
    }

    @SafeVarargs
    public final void setRealmModels(Class<? extends RealmModel>... realmModels) {
        mRealmModels = new ArrayList<>();
        mRealmModels.addAll(Arrays.asList(realmModels));
        sortModelsByName(mRealmModels);
    }

    @SafeVarargs
    public final void addRealmModel(Class<? extends RealmModel>... realmModels) {
        mRealmModels.addAll(Arrays.asList(realmModels));
    }

}
