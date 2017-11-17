package com.indeema.realmbrowser.sample.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indeema.realmbrowser.sample.R;
import com.indeema.realmbrowser.sample.entities.User;

import java.util.List;

/**
 * Created by Ivan Savenko on 17.11.16
 */

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    private OnItemClickListener mListener;

    public UsersListAdapter(Context context, List<User> users, OnItemClickListener listener) {
        mContext = context;
        mUsers = users;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_user, parent, false);
        UsersListAdapter.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(mUsers.get(position));
        holder.setOnClickListener(position);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public void setData(List<User> users) {
        mUsers = users;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView mUserIdTv;
        private final TextView mUserTv;
        private final TextView mDeviceTv;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mUserIdTv = (TextView) view.findViewById(R.id.user_id_tv);
            mUserTv = (TextView) view.findViewById(R.id.user_tv);
            mDeviceTv = (TextView) view.findViewById(R.id.device_tv);
        }

        public void setData(User user) {
            mUserIdTv.setText("ID: " + user.getId());
            mUserTv.setText("User: " + user.getName() + " from " + user.getAddress());
            if (user.getDevice() != null) {
                mDeviceTv.setText("Device: " + user.getDevice().getManufacturer() + " - " + user.getDevice().getModel());
            } else {
                mDeviceTv.setText("Device: -");
            }
        }

        public void setOnClickListener(final int position) {
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onClick(mUsers.get(position), position);
                }
            });

            mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mListener.onLongClick(mUsers.get(position), position);
                    return false;
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onClick(User user, int position);
        void onLongClick(User user, int position);
    }


}
