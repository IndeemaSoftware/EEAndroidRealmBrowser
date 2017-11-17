package com.indeema.realmbrowser.utils;

import com.indeema.realmbrowser.entities.ValueModel;

/**
 * @author Ivan Savenko
 *         Date: November, 10, 2017
 *         Time: 13:37
 */

public class ValueModelProvider {


    private static final ValueModelProvider sInstance = new ValueModelProvider();

    private ValueModel mValueModel;

    public static ValueModelProvider getInstance() {
        return sInstance;
    }

    public ValueModel getValueModel() {
        return mValueModel;
    }

    public void setValueModel(ValueModel valueModel) {
        mValueModel = valueModel;
    }
}
