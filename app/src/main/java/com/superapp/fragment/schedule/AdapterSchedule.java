package com.superapp.fragment.schedule;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.superapp.R;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.OnItemClickListener;
import com.superapp.beans.ScheduleBean;
import com.superapp.utils.ProjectUtils;

import java.util.ArrayList;
import java.util.Calendar;

public class AdapterSchedule extends RecyclerView.Adapter<AdapterSchedule.ViewHolder> {

    BaseAppCompatActivity activity;
    ArrayList<ScheduleBean> scheduleArrayList;
    OnItemClickListener onItemClickListener;

    AdapterSchedule context;

    public AdapterSchedule(BaseAppCompatActivity activity, ArrayList<ScheduleBean> scheduleArrayList, OnItemClickListener onItemClickListener) {
        this.activity = activity;
        this.scheduleArrayList = scheduleArrayList;
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_scheduleLineOfWork, tv_scheduleSubLineOfWork, tv_scheduleDescription,
                tv_StartTime, tv_endTime, tv_startDate, tv_endDate;
        LinearLayout ll_schedule, ll_detail_leftArrow, ll_icon_RightArrow, ll_rightArrow, ll_leftArrow;
        ImageView iv_edit, iv_delete;
        TextView tv_completed;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_scheduleLineOfWork = (TextView) itemView.findViewById(R.id.tv_lineOfWork);
            tv_scheduleSubLineOfWork = (TextView) itemView.findViewById(R.id.tv_lineOfWorkSubCategory);
            tv_StartTime = (TextView) itemView.findViewById(R.id.tv_StartTime);
            tv_endTime = (TextView) itemView.findViewById(R.id.tv_endTime);
            tv_startDate = (TextView) itemView.findViewById(R.id.tv_startDate);
            tv_endDate = (TextView) itemView.findViewById(R.id.tv_endDate);
            iv_edit = (ImageView) itemView.findViewById(R.id.iv_edit);
            iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);
            tv_completed = (TextView) itemView.findViewById(R.id.tv_completed);
            tv_scheduleDescription = (TextView) itemView.findViewById(R.id.tv_scheduleDescription);

            ll_schedule = (LinearLayout) itemView.findViewById(R.id.ll_schedule);
            ll_detail_leftArrow = (LinearLayout) itemView.findViewById(R.id.ll_detail_leftArrow);
            ll_icon_RightArrow = (LinearLayout) itemView.findViewById(R.id.ll_icon_RightArrow);
            ll_leftArrow = (LinearLayout) itemView.findViewById(R.id.ll_leftArrow);
            ll_rightArrow = (LinearLayout) itemView.findViewById(R.id.ll_rightArrow);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_scheduleitems, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blank_footer, parent, false);
        }
        return new AdapterSchedule.ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < scheduleArrayList.size()) {
            return 1;
        } else if (position == scheduleArrayList.size()) {
            return 2;
        } else {
            return 0;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // TODO user holder directly
        if (position < scheduleArrayList.size()) {
            final ScheduleBean scheduleBean = scheduleArrayList.get(position);

            holder.ll_leftArrow.setOnClickListener(v -> {
                holder.ll_detail_leftArrow.setVisibility(View.GONE);
                holder.ll_icon_RightArrow.setVisibility(View.VISIBLE);
            });

            holder.ll_rightArrow.setOnClickListener(v -> {
                holder.ll_detail_leftArrow.setVisibility(View.VISIBLE);
                holder.ll_icon_RightArrow.setVisibility(View.GONE);
            });

            holder.tv_scheduleLineOfWork.setText(scheduleBean.getCategoryTitle());
            holder.tv_scheduleSubLineOfWork.setText(scheduleBean.getSubCategoryTitle());

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(scheduleBean.getStartDate() * 1000);
            holder.tv_startDate.setText(ProjectUtils.getInstance().getFormattedDate(cal));
            holder.tv_StartTime.setText(ProjectUtils.getInstance().getFormattedTime(cal));

            cal.setTimeInMillis(scheduleBean.getEndDate() * 1000);
            holder.tv_endDate.setText(ProjectUtils.getInstance().getFormattedDate(cal));
            holder.tv_endTime.setText(ProjectUtils.getInstance().getFormattedTime(cal));

            if (!TextUtils.isEmpty(scheduleBean.getDetail())) {
                holder.tv_scheduleDescription.setText(scheduleBean.getDetail());
            } else {
                holder.tv_scheduleDescription.setVisibility(View.GONE);
            }

            holder.iv_delete.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(scheduleBean, 2);
                }
            });

            if (scheduleBean.getStatus().equalsIgnoreCase("Pending")) {
                holder.tv_completed.setVisibility(View.VISIBLE);
                holder.tv_completed.setOnClickListener(v -> {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(scheduleBean, 1);
                    }
                });
                holder.iv_edit.setVisibility(View.VISIBLE);
                holder.iv_edit.setOnClickListener(v -> {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(scheduleBean, 0);
                    }
                });
            } else {
                holder.ll_leftArrow.setVisibility(View.GONE);
                holder.tv_completed.setVisibility(View.GONE);
                holder.iv_edit.setVisibility(View.GONE);
            }
        } else if (position == scheduleArrayList.size()) {

        }
    }

    @Override
    public int getItemCount() {
        return scheduleArrayList.size() + 1;
    }

}