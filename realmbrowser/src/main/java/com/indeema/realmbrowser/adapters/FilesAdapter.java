package com.indeema.realmbrowser.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indeema.realmbrowser.R;
import com.indeema.realmbrowser.entities.RbEntity;
import com.indeema.realmbrowser.entities.Theme;

import java.util.List;

/**
 * Created by Ivan Savenko on 09.11.17
 */

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.ViewHolder> {

    private static final String TAG = FilesAdapter.class.getSimpleName();

    private final AdapterListener mListener;
    private Context mContext;
    private Theme mTheme;
    private List<RbEntity> mData;


    public FilesAdapter(@NonNull Context context, Theme theme, List<RbEntity> data, AdapterListener listener) {
        mContext = context;
        mTheme = theme;
        mData = data;
        mListener = listener;
    }

    public void setRBEntities(List<RbEntity> data) {
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

        private TextView mRealmDbNameTv;

        public ViewHolder(View v) {
            super(v);
            mRealmDbNameTv = (TextView) v.findViewById(R.id.realmDbTitle);
            mRealmDbNameTv.setTextColor(mTheme.getTextSecondaryColor());
            mRealmDbNameTv.setBackgroundResource(mTheme.getButtonBackgroundRes());
        }

        public void setData(@NonNull RbEntity rbEntity, int position) {
            String title = rbEntity.getRBEntityTitle();
            mRealmDbNameTv.setText(title);
        }

        public void setOnClickListener(@NonNull final RbEntity rbEntity, final int position, final AdapterListener listener) {
            mRealmDbNameTv.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(rbEntity, position);
                }
            });
        }

    }

    public interface AdapterListener {
        void onItemClick(@NonNull RbEntity rbEntity, int position);
    }

}
