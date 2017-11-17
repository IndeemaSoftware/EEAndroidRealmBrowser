package com.indeema.realmbrowser.entities;

import java.util.List;

import io.realm.DynamicRealmObject;
import io.realm.RealmModel;

/**
 *Created by ruslanstosyk on 11/7/17.
 */

public class TableModel {

    private Integer mId;

    private DynamicRealmObject mDynamicRealmObject;

    private List<ValueModel> mValues;

    public TableModel(Integer id, DynamicRealmObject dynamicRealmObject, List<ValueModel> values) {
        mId = id;
        mDynamicRealmObject = dynamicRealmObject;
        mValues = values;
    }

    public List<ValueModel> getValues() {
        return mValues;
    }

    public Integer getId() {
        return mId;
    }

    public DynamicRealmObject getDynamicRealmObject() {
        return mDynamicRealmObject;
    }
}
