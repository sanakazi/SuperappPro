package com.superapp.activity.supersearch;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.superapp.R;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.OnItemClickListener;
import com.superapp.beans.SuperSearchBean;
import com.superapp.utils.Utilities;

import java.util.ArrayList;

public class SSearchListAdapter extends RecyclerView.Adapter<SSearchListAdapter.ViewHolder> {

    private BaseAppCompatActivity activity;
    private ArrayList<SuperSearchBean> superSearchArrayList;
    private OnItemClickListener onItemClickListener;

    public SSearchListAdapter(BaseAppCompatActivity activity, ArrayList<SuperSearchBean> superSearchArrayList, OnItemClickListener onItemClickListener) {
        this.activity = activity;
        this.superSearchArrayList = superSearchArrayList;
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_superSearchOccupation, tv_superSearchOwnerName, tv_superSearchRating;
        ImageView iv_clientPhone;
        LinearLayout ll_superSearchItem;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_superSearchOccupation = (TextView) itemView.findViewById(R.id.tv_superSearchOccupation);
            tv_superSearchOwnerName = (TextView) itemView.findViewById(R.id.tv_superSearchOwnerName);
            tv_superSearchRating = (TextView) itemView.findViewById(R.id.tv_superSearchRating);
            ll_superSearchItem = (LinearLayout) itemView.findViewById(R.id.ll_superSearchItem);
            iv_clientPhone = (ImageView) itemView.findViewById(R.id.iv_clientPhone);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_super_search_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blank_footer, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < superSearchArrayList.size()) {
            return 1;
        } else if (position == superSearchArrayList.size()) {
            return 2;
        } else {
            return 0;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (position < superSearchArrayList.size()) {
            final SuperSearchBean supersearchBean = superSearchArrayList.get(position);
            holder.tv_superSearchOccupation.setText(supersearchBean.getOccupation());
            holder.tv_superSearchOwnerName.setText(supersearchBean.getOwnerName());
            holder.tv_superSearchRating.setText(supersearchBean.getAvgRating());
            holder.iv_clientPhone.setOnTouchListener(BaseAppCompatActivity.imageTouch);
            holder.iv_clientPhone.setOnClickListener(v -> Utilities.getInstance().doCall(activity, supersearchBean.getMobileNumber()));
            holder.ll_superSearchItem.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(supersearchBean, position);
                }
            });
        }
//        else if (position == superSearchArrayList.size())
//        {
//        }
    }

    @Override
    public int getItemCount() {
        return superSearchArrayList.size() + 1;
    }
}