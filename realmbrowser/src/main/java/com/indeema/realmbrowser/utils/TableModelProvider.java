package com.indeema.realmbrowser.utils;

import com.indeema.realmbrowser.entities.TableModel;

/**
 *  @author ruslanstosyk on 11/13/17.
 */

public class TableModelProvider {

    private static final TableModelProvider sInstance = new TableModelProvider();

    private TableModel mTableModel;

    public static TableModelProvider getInstance() {
        return sInstance;
    }

    public TableModel getTableModel() {
        return mTableModel;
    }

    public void setTableModel(TableModel tableModel) {
        mTableModel = tableModel;
    }
}
