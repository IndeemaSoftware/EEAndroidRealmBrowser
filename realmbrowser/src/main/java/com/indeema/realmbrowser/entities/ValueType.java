package com.indeema.realmbrowser.entities;

import android.support.annotation.IntDef;

/**
 * @author Ivan Savenko
 *         Date: November, 09, 2017
 *         Time: 17:16
 */

@IntDef({ValueModel.UNKNOWN,
        ValueModel.BOOLEAN,
        ValueModel.BYTE,
        ValueModel.BINARY,
        ValueModel.SHORT,
        ValueModel.INTEGER,
        ValueModel.LONG,
        ValueModel.FLOAT,
        ValueModel.DOUBLE,
        ValueModel.STRING,
        ValueModel.DATE,
        ValueModel.OBJECT,
        ValueModel.GENERIC_OBJECT})
public @interface ValueType {

}
