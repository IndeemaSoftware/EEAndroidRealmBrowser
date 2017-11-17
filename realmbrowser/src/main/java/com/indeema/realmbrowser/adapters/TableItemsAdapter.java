package com.indeema.realmbrowser.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.indeema.realmbrowser.entities.TableModel;
import com.indeema.realmbrowser.R;
import com.indeema.realmbrowser.entities.Theme;
import com.indeema.realmbrowser.entities.ValueModel;
import com.indeema.realmbrowser.utils.PreferenceUtils;

import java.util.List;

public class TableItemsAdapter extends RecyclerView.Adapter<TableItemsAdapter.ViewHolder> {

    private static final String TAG = TableItemsAdapter.class.getSimpleName();

    private final AdapterListener mListener;
    private Context mContext;
    private Theme mTheme;
    private PageFilter mFilter;


    public TableItemsAdapter(@NonNull Context context, Theme theme, List<TableModel> data, AdapterListener listener) {
        mContext = context;
        mTheme = theme;
        mListener = listener;

        boolean isPaginationEnable = PreferenceUtils.isPaginationEnable(mContext);
        int pageSize = PreferenceUtils.getPageSize(mContext);
        int currentPage = 1;
        mFilter = new PageFilter(this, currentPage, pageSize, isPaginationEnable, data);
        mFilter.performFiltering();
    }

    public void setFieldList(List<TableModel> data) {
        boolean isPaginationEnable = PreferenceUtils.isPaginationEnable(mContext);
        int pageSize = PreferenceUtils.getPageSize(mContext);
        int currentPage = 1;
        if (mFilter != null) {
            currentPage = mFilter.getCurrentPage();
        }
        mFilter = new PageFilter(this, currentPage, pageSize, isPaginationEnable, data);
        mFilter.performFiltering();
    }

    public PageFilter getFilter() {
        return mFilter;
    }

    public List<TableModel> getFilteredList() {
        return mFilter.getFilteredList();
    }

    public List<TableModel> getOriginalList() {
        return mFilter.getOriginalList();
    }

    @Override
    public int getItemCount() {
        return getFilteredList().size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rb_item_table_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (!getFilteredList().isEmpty()) {
            if (getFilteredList().get(0).getValues().size() != holder.mItemsCount) {
                holder.updateContainerItems();
            }
            holder.setData(getFilteredList().get(position), position, mListener);
        }
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView textView = (TextView) v;
            Toast.makeText(mContext, textView.getText(), Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("RB", ((TextView) v).getText());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(mContext, "Copied to Clip Board", Toast.LENGTH_SHORT).show();
            return false;
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mItemsContainer;
        public int mItemsCount;

        public ViewHolder(View v) {
            super(v);
            mItemsContainer = (LinearLayout) v.findViewById(R.id.itemsContainer);
            mItemsContainer.setMinimumWidth(mContext.getResources().getDisplayMetrics().widthPixels);
            initContainerItems();
        }

        public void initContainerItems() {
            for (int i = 0; i < getFilteredList().get(0).getValues().size() + 3; i++) {
                View view;
                if (i == getFilteredList().get(0).getValues().size() + 2) {
                    view = createImageView(R.drawable.ic_delete_white_18dp);
                } else if (i == getFilteredList().get(0).getValues().size() + 1) {
                    view = createImageView(R.drawable.ic_edit_white_18dp);
                } else {
                    view = createTextView(true, i == 0);
                }

                view.setTag(i);
                mItemsContainer.addView(view);
            }
            mItemsCount = getFilteredList().get(0).getValues().size();
        }

        public void updateContainerItems() {
            mItemsContainer.removeAllViews();
            initContainerItems();
        }

        public void setData(TableModel model, int position, final AdapterListener listener) {
            boolean isDarkItem = position % 2 != 0;
            itemView.setBackgroundColor(isDarkItem ? mTheme.getListItemBackgroundDarkColor() : mTheme.getListItemBackgroundLightColor());

            if (model != null) {
                for (int index = 0; index < model.getValues().size() + 3; index++) {
                    if (index == 0) {
                        TextView txtColumn = (TextView) mItemsContainer.findViewWithTag(index);
                        Integer id = model.getId();
                        txtColumn.setText(String.valueOf(id));

                    } else if (index == model.getValues().size() + 1) {
                        View view = mItemsContainer.findViewWithTag(index);
                        view.setBackgroundResource(mTheme.getListItemButtonBackgroundRes(isDarkItem));
                        view.setOnClickListener(v -> {
                            if (listener != null) {
                                listener.onRowEditClick(model);
                            }
                        });

                    } else if (index == getFilteredList().get(0).getValues().size() + 2) {
                        View view = mItemsContainer.findViewWithTag(index);
                        view.setBackgroundResource(mTheme.getListItemButtonBackgroundRes(isDarkItem));
                        view.findViewWithTag(index).setOnClickListener(v -> {
                            if (listener != null) {
                                listener.onRowDeleteClick(model);
                            }
                        });

                    } else {
                        TextView txtColumn = (TextView) mItemsContainer.findViewWithTag(index);
                        final int valueIndex = index - 1;
                        final ValueModel valueModel = model.getValues().get(valueIndex);
                        if (valueModel != null) {
//                            String value = valueModel.getValue().toString();
                            String value = valueModel.getValueToString();
                            txtColumn.setText(value);


                            txtColumn.setOnClickListener(v -> {
                                if (listener != null) {
                                    listener.onRowItemClick(model, valueModel, position, valueIndex);
                                }
                            });

                            txtColumn.setOnLongClickListener(v -> {
                                if (listener != null) {
                                    listener.onRowItemLongClick(model, valueModel, position, valueIndex);
                                }
                                return false;
                            });

                        } else {
                            txtColumn.setText("-");
                        }

                    }

                }
            }

        }

    }

    private ImageView createImageView(@DrawableRes int resId) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(resId);
        imageView.setColorFilter(mTheme.getTextSecondaryColor(), PorterDuff.Mode.MULTIPLY);
        imageView.setAlpha(0.66f);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                mContext.getResources().getDimensionPixelSize(R.dimen.edit_image_view_width),
                mContext.getResources().getDimensionPixelSize(R.dimen.edit_image_view_width));

        int margin = mContext.getResources().getDimensionPixelSize(R.dimen.edit_image_view_margin);
        layoutParams.setMarginStart(margin);
        layoutParams.setMarginEnd(margin);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;

        imageView.setLayoutParams(layoutParams);

        int padding = mContext.getResources().getDimensionPixelSize(R.dimen.edit_image_view_padding);
        imageView.setPadding(padding, padding, padding, padding);

        return imageView;
    }

    private TextView createTextView(boolean wrapText, boolean isNumColumn) {
        TextView textView = new TextView(mContext);
        textView.setSingleLine(!wrapText);
        textView.setTextColor(mTheme.getTextSecondaryColor());
        textView.setPadding(mContext.getResources().getDimensionPixelSize(R.dimen.table_text_padding),
                0,
                mContext.getResources().getDimensionPixelSize(R.dimen.table_text_padding),
                0);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

        LinearLayout.LayoutParams layoutParams;
        if (isNumColumn) {
            layoutParams = new LinearLayout.LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.table_num_column_width),
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            layoutParams = new LinearLayout.LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.table_column_width),
                    ViewGroup.LayoutParams.MATCH_PARENT);
        }
        textView.setLayoutParams(layoutParams);
        return textView;
    }

    public interface AdapterListener {
        //        void onRowItemClick(@NonNull RealmObject realmObject, @NonNull Field field);
        void onRowItemClick(@NonNull TableModel model, ValueModel valueModel, int position, int valueIndex);

        void onRowItemLongClick(@NonNull TableModel model, ValueModel valueModel, int position, int valueIndex);

        void onRowEditClick(@NonNull TableModel model);

        void onRowDeleteClick(@NonNull TableModel model);
    }
}
