package com.indeema.realmbrowser.entities;

import java.lang.reflect.Field;

/**
 * Created by Ruslan Stosyk on 07.11.17
 */

public class ValueModel {

    public static final int UNKNOWN = 0;
    public static final int BOOLEAN = 1;
    public static final int BYTE = 2;
    public static final int BINARY = 3;
    public static final int SHORT = 4;
    public static final int INTEGER = 5;
    public static final int LONG = 6;
    public static final int FLOAT = 7;
    public static final int DOUBLE = 8;
    public static final int STRING = 9;
    public static final int DATE = 10;
    public static final int OBJECT = 11;
    public static final int GENERIC_OBJECT = 12;

    private Object mValue;
    private String mValueToString;
    private Field mField;
    private @ValueType int mType;

    public ValueModel(Object value, Field field, @ValueType int type){
        this(value, value != null ? value.toString() : "", field, type);
    }

    public ValueModel(Object value, String valueToString, Field field, @ValueType int type){
        mValue = value;
        mValueToString = valueToString;
        mField = field;
        mType = type;
    }

    public Object getValue() {
        return mValue;
    }

    public String getValueToString() {
        return mValueToString;
    }

    public Field getField() {
        return mField;
    }

    public @ValueType int getType() {
        return mType;
    }

    public String typeToString() {
        switch (mType) {
            case BOOLEAN: return "BOOLEAN";
            case BYTE: return "BYTE";
            case BINARY: return "BINARY";
            case SHORT: return "SHORT";
            case INTEGER: return "INTEGER";
            case LONG: return "LONG";
            case FLOAT: return "FLOAT";
            case DOUBLE: return "DOUBLE";
            case STRING: return "STRING";
            case DATE: return "DATE";
            case OBJECT: return "OBJECT";
            case GENERIC_OBJECT: return "GENERIC_OBJECT";
            default: return "UNKNOWN";
        }
    }

    @Override
    public String toString() {
        return "ValueModel{" +
                "type=" + mType + "(" + typeToString() + ")" +
                ", value=" + (mValue != null ? mValue.getClass() : "null") +
                ", valueToString=" + mValueToString +
                '}';
    }

}
