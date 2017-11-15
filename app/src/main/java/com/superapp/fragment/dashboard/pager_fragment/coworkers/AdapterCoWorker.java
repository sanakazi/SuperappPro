package com.superapp.fragment.dashboard.pager_fragment.coworkers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.OnItemClickListener;
import com.superapp.beans.CoworkerBean;
import com.superapp.utils.ProjectUtils;
import com.superapp.utils.Utilities;

import java.util.ArrayList;

public class AdapterCoWorker extends RecyclerView.Adapter<AdapterCoWorker.ViewHolder> {
    ApplicationContext activity;
    ArrayList<CoworkerBean> coWorker;
    private Boolean showCompleteDetail;
    private OnItemClickListener onItemClickListener;

    public AdapterCoWorker(ApplicationContext activity, ArrayList<CoworkerBean> coWorker, Boolean showCompleteDetail, OnItemClickListener onItemClickListener) {
        this.activity = activity;
        this.coWorker = coWorker;
        this.showCompleteDetail = showCompleteDetail;
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_coWorkerName, tv_coWorkerAddress, tv_coWorkerOccupation;
        ImageView iv_coWorkerPhone, iv_editCoWorker, iv_deleteCoWorker;
        View view;
        LinearLayout ll_nameOccupationAddressLeftArrow, ll_item, ll_callEditDeleteRightArrow, ll_leftArrow, ll_rightArrow;

        public ViewHolder(View itemView) {
            super(itemView);
            ll_nameOccupationAddressLeftArrow = (LinearLayout) itemView.findViewById(R.id.ll_nameOccupationAddressLeftArrow);
            ll_item = (LinearLayout) itemView.findViewById(R.id.ll_item);
            ll_leftArrow = (LinearLayout) itemView.findViewById(R.id.ll_leftArrow);
            tv_coWorkerName = (TextView) itemView.findViewById(R.id.tv_coWorkerName);
            view = itemView.findViewById(R.id.view);
            tv_coWorkerOccupation = (TextView) itemView.findViewById(R.id.tv_coWorkerOccupation);
            tv_coWorkerAddress = (TextView) itemView.findViewById(R.id.tv_coWorkerAddress);

            ll_callEditDeleteRightArrow = (LinearLayout) itemView.findViewById(R.id.ll_callEditDeleteRightArrow);
            ll_rightArrow = (LinearLayout) itemView.findViewById(R.id.ll_rightArrow);
            iv_coWorkerPhone = (ImageView) itemView.findViewById(R.id.iv_coWorkerPhone);
            iv_editCoWorker = (ImageView) itemView.findViewById(R.id.iv_editCoWorker);
            iv_deleteCoWorker = (ImageView) itemView.findViewById(R.id.iv_deleteCoWorker);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_dashboard_coworker_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blank_footer, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < coWorker.size()) {
            return 1;
        } else if (position == coWorker.size()) {
            return 2;
        } else {
            return 0;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // TODO user holder directly
        if (position < coWorker.size()) {
            final CoworkerBean coWorkers = coWorker.get(position);
            holder.ll_item.setOnClickListener((View v) -> {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(coWorkers, 1);
            });

            holder.ll_leftArrow.setOnClickListener(v -> {
                holder.ll_nameOccupationAddressLeftArrow.setVisibility(View.GONE);
                holder.ll_callEditDeleteRightArrow.setVisibility(View.VISIBLE);
            });

            holder.ll_rightArrow.setOnClickListener(v -> {
                holder.ll_nameOccupationAddressLeftArrow.setVisibility(View.VISIBLE);
                holder.ll_callEditDeleteRightArrow.setVisibility(View.GONE);
            });


            holder.tv_coWorkerName.setText(coWorkers.getName());
//            if (showCompleteDetail == null || showCompleteDetail) {
//                holder.view.setVisibility(View.VISIBLE);

//                if (showCompleteDetail == null) {
//                    holder.ll_editDelete.setVisibility(View.GONE);
//                } else {
//                    holder.ll_editDelete.setVisibility(View.VISIBLE);
//                }
            holder.tv_coWorkerOccupation.setVisibility(View.VISIBLE);
            holder.tv_coWorkerOccupation.setText(coWorkers.getOccupation());
            holder.tv_coWorkerAddress.setVisibility(View.VISIBLE);
            holder.tv_coWorkerAddress.setText(ProjectUtils.getInstance().getFormattedAddress(coWorkers.getAddress()).toString());
            holder.iv_coWorkerPhone.setVisibility(View.VISIBLE);
            holder.iv_coWorkerPhone.setOnTouchListener(BaseAppCompatActivity.imageTouch);
            holder.iv_coWorkerPhone.setOnClickListener((View v) -> Utilities.getInstance().doCall(activity, coWorkers.getMobile()));


            holder.iv_editCoWorker.setOnTouchListener(BaseAppCompatActivity.imageTouch);
            holder.iv_editCoWorker.setOnClickListener((View v) -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(coWorkers, 0);
                }
            });

            holder.iv_deleteCoWorker.setOnTouchListener(BaseAppCompatActivity.imageTouch);
            holder.iv_deleteCoWorker.setOnClickListener((View v) -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(coWorkers, 2);
                }
            });

//            } else {
//                holder.iv_coWorkerPhone.setVisibility(View.GONE);
//                holder.tv_coWorkerAddress.setVisibility(View.GONE);
//                holder.tv_coWorkerOccupation.setVisibility(View.GONE);
//                holder.iv_mapImage.setVisibility(View.GONE);
//                holder.view.setVisibility(View.GONE);
//                holder.ll_editDelete.setVisibility(View.GONE);
        }
//        } else if (position == coWorker.size()) {
    }

    @Override
    public int getItemCount() {
        return (showCompleteDetail == null || showCompleteDetail) ? (coWorker.size() + 1) : coWorker.size();
    }
}
