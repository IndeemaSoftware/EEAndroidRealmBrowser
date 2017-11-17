package com.indeema.realmbrowser.adapters;

import com.indeema.realmbrowser.entities.TableModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Ivan Savenko
 *         Date: November, 15, 2017
 *         Time: 14:15
 */

public class PageFilter {

    private final TableItemsAdapter mAdapter;
    private final List<TableModel> mOriginalList;
    private final List<TableModel> mFilteredList;
    private boolean mIsPaginationEnable;
    private int mPagesCount;
    private int mPageSize;
    private int mCurrentPage;

//    public PageFilter(TableItemsAdapter adapter, List<TableModel> originalList) {
//        this(adapter, 1, DEFAULT_PAGE_SIZE, true, originalList);
//    }
//
//    public PageFilter(TableItemsAdapter adapter, int page, List<TableModel> originalList) {
//        this(adapter, page, DEFAULT_PAGE_SIZE, true, originalList);
//    }

    public PageFilter(TableItemsAdapter adapter, int page, int pageSize, boolean isPaginationEnable, List<TableModel> originalList) {
        mAdapter = adapter;
        mOriginalList = new ArrayList<>(originalList);
        mFilteredList = new ArrayList<>();
        mIsPaginationEnable = isPaginationEnable;
        mPageSize = pageSize;


        int fullPagesCount = mOriginalList.size() / mPageSize;
//        boolean hasNonFullPage = mOriginalList.size() - (fullPagesCount * mPageSize);
        boolean hasNonFullPage = mOriginalList.size() % mPageSize != 0;
        mPagesCount = fullPagesCount + (hasNonFullPage ? 1 : 0);

        if (page <= 0) {
            mCurrentPage = 1;
        } else if (page > mPagesCount) {
            mCurrentPage = mPagesCount;
        } else {
            mCurrentPage = page;
        }
    }

    public boolean hasPreviousPage() {
        return mCurrentPage >= 2;
    }

    public boolean hasNextPage() {
        return mCurrentPage + 1 <= mPagesCount;
    }

    public void performPreviousPageFiltering() {
        if (hasPreviousPage()) {
            mCurrentPage--;
            performFiltering();
        }
    }

    public void performNextPageFiltering() {
        if (hasNextPage()) {
            mCurrentPage++;
            performFiltering();
        }
    }


    public void performFiltering() {
        mFilteredList.clear();

        if (!mIsPaginationEnable) {
            mFilteredList.addAll(mOriginalList);

        } else {
            int pageStartIndex = (mCurrentPage - 1) * mPageSize;
            int pageEndIndex = pageStartIndex + mPageSize;
            if (pageEndIndex > mOriginalList.size()) {
                pageEndIndex = mOriginalList.size();
            }

            for (int i = pageStartIndex; i < pageEndIndex; i++) {
                TableModel model = mOriginalList.get(i);
                mFilteredList.add(model);
            }
        }

        mAdapter.notifyDataSetChanged();
    }


    public List<TableModel> getOriginalList() {
        return mOriginalList;
    }

    public List<TableModel> getFilteredList() {
        return mFilteredList;
    }

    public boolean isPaginationEnable() {
        return mIsPaginationEnable;
    }

    public int getPageSize() {
        return mPageSize;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public int getPagesCount() {
        return mPagesCount;
    }
}
