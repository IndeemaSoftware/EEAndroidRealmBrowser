package com.indeema.realmbrowser.adapters;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author Ivan Savenko
 *         Date: September, 05, 2017
 *         Time: 17:36
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int mTopSpace;
    private final int mBottomSpace;
    private final int mLeftSpace;
    private final int mRightSpace;

    public SpaceItemDecoration(int space) {
        mTopSpace = space;
        mBottomSpace = space;
        mLeftSpace = space;
        mRightSpace = space;
    }

    public SpaceItemDecoration(int topSpace, int bottomSpace, int leftSpace, int rightSpace) {
        mTopSpace = topSpace;
        mBottomSpace = bottomSpace;
        mLeftSpace = leftSpace;
        mRightSpace = rightSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = mTopSpace;
        } else {
            outRect.top = mTopSpace / 2;
        }

        if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = mBottomSpace;
        } else {
            outRect.bottom = mBottomSpace / 2;
        }

        outRect.right = mRightSpace;
        outRect.left = mLeftSpace;
    }


}