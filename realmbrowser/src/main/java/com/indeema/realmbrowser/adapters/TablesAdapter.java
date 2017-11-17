package com.indeema.realmbrowser.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indeema.realmbrowser.R;
import com.indeema.realmbrowser.entities.Theme;

import java.util.List;

import io.realm.RealmModel;

/**
 * Created by Ivan Savenko on 09.11.17
 */

public class TablesAdapter extends RecyclerView.Adapter<TablesAdapter.ViewHolder> {

    private static final String TAG = TablesAdapter.class.getSimpleName();

    private final AdapterListener mListener;
    private Context mContext;
    private Theme mTheme;
    private List<Class<? extends RealmModel>> mData;

    public TablesAdapter(@NonNull Context context, Theme theme, List<Class<? extends RealmModel>> data, AdapterListener listener) {
        mContext = context;
        mTheme = theme;
        mData = data;
        mListener = listener;
    }

    public void setRbModels(List<Class<? extends RealmModel>> data) {
        mData = data;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rb_item_simple_text, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(mData.get(position), position);
        holder.setOnClickListener(mData.get(position), position, mListener);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTableTv;

        public ViewHolder(View v) {
            super(v);
            mTableTv = (TextView) v.findViewById(R.id.realmDbTitle);
            mTableTv.setTextColor(mTheme.getTextSecondaryColor());
            mTableTv.setBackgroundResource(mTheme.getButtonBackgroundRes());
        }

        public void setData(@NonNull Class<? extends RealmModel> rbModel, int position) {
            String title = rbModel.getSimpleName();
            mTableTv.setText(title);
        }

        public void setOnClickListener(@NonNull final Class<? extends RealmModel> rbModel, final int position, final AdapterListener listener) {
            mTableTv.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(rbModel, position);
                }
            });
        }

    }

    public interface AdapterListener {
        void onItemClick(@NonNull Class<? extends RealmModel> rbModel, int position);
    }

}
