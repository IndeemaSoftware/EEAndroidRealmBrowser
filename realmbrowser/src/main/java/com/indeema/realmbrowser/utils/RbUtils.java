package com.indeema.realmbrowser.utils;

import android.support.annotation.NonNull;

import com.indeema.realmbrowser.entities.ValueModel;
import com.indeema.realmbrowser.entities.TableModel;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.DynamicRealmObject;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Ivan Savenko on 15.11.16
 * <p>
 * Updated by Ivan Savenko on 09.11.17
 */

public class RbUtils {

    private static String TAG = RbUtils.class.getSimpleName();

    public static String getGetterMethodFromField(@NonNull Field field) {
        String fieldName = field.getName();
        if (fieldName.substring(0, 1).equals("m") && Character.isUpperCase(fieldName.charAt(1))) {
            fieldName = field.getName().substring(1);
        }

        if (!field.getType().equals(boolean.class)) {
            return addMethodPrefix(fieldName, "get");
        } else if (!fieldName.contains("is")) {
            return addMethodPrefix(fieldName, "is");
        } else {
            return fieldName;
        }
    }

    public static List<TableModel> getTableModels(RealmResults<DynamicRealmObject> result, List<Field> fields) {
        List<TableModel> data = new ArrayList<>();
        if (!fields.isEmpty()) {
            for (int i = 0; i < result.size(); i++) {
                DynamicRealmObject dynamicRealmObject = result.get(i);

                List<ValueModel> values = new ArrayList<>();
                for (int j = 0; j < fields.size(); j++) {
                    if (dynamicRealmObject.hasField(fields.get(j).getName())) {
                        values.add(getValueModel(dynamicRealmObject, fields.get(j)));
                    }
                }
                TableModel model = new TableModel(i, dynamicRealmObject, values);
                data.add(model);
            }
        }
        return data;
    }

    public static List<TableModel> getTableModels(RealmList<DynamicRealmObject> realmList, List<Field> fields) {
        List<TableModel> data = new ArrayList<>();
        if (!fields.isEmpty()) {
            for (int i = 0; i < realmList.size(); i++) {
                DynamicRealmObject dynamicRealmObject = realmList.get(i);

                List<ValueModel> values = new ArrayList<>();
                for (int j = 0; j < fields.size(); j++) {
                    if (dynamicRealmObject.hasField(fields.get(j).getName())) {
                        values.add(getValueModel(dynamicRealmObject, fields.get(j)));
                    }
                }
                TableModel model = new TableModel(i, dynamicRealmObject, values);
                data.add(model);
            }
        }
        return data;
    }

    public static List<TableModel> getTableModels(DynamicRealmObject dynamicRealmObject, List<Field> fields) {
        List<TableModel> data = new ArrayList<>();
        if (!fields.isEmpty()) {
            List<ValueModel> values = new ArrayList<>();
            for (int j = 0; j < fields.size(); j++) {
                if (dynamicRealmObject.hasField(fields.get(j).getName())) {
                    values.add(getValueModel(dynamicRealmObject, fields.get(j)));
                }
            }
            TableModel model = new TableModel(0, dynamicRealmObject, values);
            data.add(model);
        }
        return data;
    }

    private static String addMethodPrefix(String baseName, String prefix) {
        return prefix + Character.toUpperCase(baseName.charAt(0)) + baseName.substring(1);
    }

    private static ValueModel getValueModel(final DynamicRealmObject dynamicRealmObject, final Field field) {
        ValueModel result;

        if (isGeneric(field)) {
            ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
            String rawType = getGenericRawType(parameterizedType);
            String actualType = getGenericActualType(parameterizedType);
            String valueToString = rawType + "<" + actualType + ">";

            Object genericObject = null;
            if (dynamicRealmObject.hasField(field.getName())) {
                genericObject = dynamicRealmObject.get(field.getName());
            }
            result = new ValueModel(genericObject, valueToString, field, ValueModel.GENERIC_OBJECT);

        } else {
            Object object = null;
            if (dynamicRealmObject.hasField(field.getName())) {
                object = dynamicRealmObject.get(field.getName());
            }

//            if (object instanceof Boolean) {
//                result = new ValueModel(object, field, ValueModel.BOOLEAN);
//            } else if (object instanceof Byte) {
//                result = new ValueModel(object, field, ValueModel.BYTE);
//            } else if (object instanceof byte[] || object instanceof ByteBuffer) {
//                result = new ValueModel(object, field, ValueModel.BINARY);
//            } else if (object instanceof Short) {
//                result = new ValueModel(object, field, ValueModel.SHORT);
//            } else if (object instanceof Integer) {
//                result = new ValueModel(object, field, ValueModel.INTEGER);
//            } else if (object instanceof Long) {
//                result = new ValueModel(object, field, ValueModel.LONG);
//            } else if (object instanceof Float) {
//                result = new ValueModel(object, field, ValueModel.FLOAT);
//            } else if (object instanceof Double) {
//                result = new ValueModel(object, field, ValueModel.DOUBLE);
//            } else if (object instanceof String) {
//                result = new ValueModel(object, field, ValueModel.STRING);
//            } else if (object instanceof RealmModel) {
//                result = new ValueModel(object, field, ValueModel.OBJECT);
//            } else {
//                result = new ValueModel(object, field, ValueModel.UNKNOWN);
//            }

            if (isBoolean(field)) {
                result = new ValueModel(object, field, ValueModel.BOOLEAN);
            } else if (isByte(field)) {
                result = new ValueModel(object, field, ValueModel.BYTE);
            } else if (isBinary(field)) {
                result = new ValueModel(object, field, ValueModel.BINARY);
            } else if (isShort(field)) {
                result = new ValueModel(object, field, ValueModel.SHORT);
            } else if (isInteger(field)) {
                result = new ValueModel(object, field, ValueModel.INTEGER);
            } else if (isLong(field)) {
                result = new ValueModel(object, field, ValueModel.LONG);
            } else if (isFloat(field)) {
                result = new ValueModel(object, field, ValueModel.FLOAT);
            } else if (isDouble(field)) {
                result = new ValueModel(object, field, ValueModel.DOUBLE);
            } else if (isString(field)) {
                result = new ValueModel(object, field, ValueModel.STRING);
            } else if (isDate(field)) {
                result = new ValueModel(object, field, ValueModel.DATE);
            } else if (isObject(field)) {
                result = new ValueModel(object, field, ValueModel.OBJECT);
            } else {
                result = new ValueModel(object, field, ValueModel.UNKNOWN);
            }

        }

        return result;
    }

    private static String getGenericRawType(@NonNull ParameterizedType parameterizedType) {
        String result = parameterizedType.getRawType().toString();
        int index = result.lastIndexOf(".");
        if (index > 0) {
            result = result.substring(index + 1);
        }
        return result;
    }

    private static String getGenericActualType(@NonNull ParameterizedType parameterizedType) {
        String result = parameterizedType.getActualTypeArguments()[0].toString();
        int index = result.lastIndexOf(".");
        if (index > 0) {
            result = result.substring(index + 1);
        }
        return result;
    }

    private static boolean isObject(@NonNull Field field) {
        return RealmObject.class.isAssignableFrom(field.getType());
    }

    public static boolean isGeneric(@NonNull Field field) {
        return field.getGenericType() instanceof ParameterizedType;
    }

    private static boolean isBoolean(@NonNull Field field) {
        return field.getType().getName().equals(Boolean.class.getName()) ||
                field.getType().getName().equals("boolean");
    }

    private static boolean isNumber(@NonNull Field field) {
        return isByte(field) || isShort(field) || isInteger(field) ||
                isLong(field) || isFloat(field) || isDouble(field);
    }

    private static boolean isByte(@NonNull Field field) {
        return field.getType().getName().equals(Byte.class.getName()) ||
                field.getType().getName().equals("byte");
    }

    private static boolean isBinary(@NonNull Field field) {
        return field.getType().getName().equals(byte[].class.getName());
    }

    private static boolean isShort(@NonNull Field field) {
        return field.getType().getName().equals(Short.class.getName()) ||
                field.getType().getName().equals("short");
    }

    private static boolean isInteger(@NonNull Field field) {
        return field.getType().getName().equals(Integer.class.getName()) ||
                field.getType().getName().equals("int");
    }

    private static boolean isLong(@NonNull Field field) {
        return field.getType().getName().equals(Long.class.getName()) ||
                field.getType().getName().equals("long");
    }

    private static boolean isFloat(@NonNull Field field) {
        return field.getType().getName().equals(Float.class.getName()) ||
                field.getType().getName().equals("float");
    }

    private static boolean isDouble(@NonNull Field field) {
        return field.getType().getName().equals(Double.class.getName()) ||
                field.getType().getName().equals("double");
    }

    private static boolean isString(@NonNull Field field) {
        return field.getType().getName().equals(String.class.getName());
    }

    private static boolean isDate(@NonNull Field field) {
        return field.getType().getName().equals(Date.class.getName());
    }

}
