package com.indeema.realmbrowser.sample.entities.realm;

import io.realm.RealmObject;

/**
 * @author Ivan Savenko
 *         Date: November, 14, 2017
 *         Time: 14:31
 */

public class Primitives extends RealmObject {

    private boolean mBooleanValue;
    private byte mByteValue;
    private short mShortValue;
    private int mIntValue;
    private long mLongValue;
    private float mFloatValue;
    private double mDoubleValue;
    private String mStringValue;

    public Primitives() {

    }

    public boolean isBooleanValue() {
        return mBooleanValue;
    }

    public void setBooleanValue(boolean booleanValue) {
        mBooleanValue = booleanValue;
    }

    public byte getByteValue() {
        return mByteValue;
    }

    public void setByteValue(byte byteValue) {
        mByteValue = byteValue;
    }

    public short getShortValue() {
        return mShortValue;
    }

    public void setShortValue(short shortValue) {
        mShortValue = shortValue;
    }

    public int getIntValue() {
        return mIntValue;
    }

    public void setIntValue(int intValue) {
        mIntValue = intValue;
    }

    public long getLongValue() {
        return mLongValue;
    }

    public void setLongValue(long longValue) {
        mLongValue = longValue;
    }

    public float getFloatValue() {
        return mFloatValue;
    }

    public void setFloatValue(float floatValue) {
        mFloatValue = floatValue;
    }

    public double getDoubleValue() {
        return mDoubleValue;
    }

    public void setDoubleValue(double doubleValue) {
        mDoubleValue = doubleValue;
    }

    public String getStringValue() {
        return mStringValue;
    }

    public void setStringValue(String stringValue) {
        mStringValue = stringValue;
    }
}
