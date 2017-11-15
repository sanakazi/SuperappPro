package com.superapp.fragment.schedule;

import android.content.Context;
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
import com.superapp.beans.ClientBean;
import com.superapp.beans.ScheduleBean;
import com.superapp.swipe.SwipeLayout;
import com.superapp.swipe.adapter.RecyclerSwipeAdapter;
import com.superapp.utils.ProjectUtils;
import com.superapp.utils.Utilities;

import java.util.ArrayList;
import java.util.Calendar;

public class ScheduleListAdapter extends RecyclerSwipeAdapter<ScheduleListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<ScheduleBean> schedules;
    private boolean showCompleteDetail;
    private OnItemClickListener onItemClickListener;

    public ScheduleListAdapter(Context context, ArrayList<ScheduleBean> schedules, boolean showCompleteDetail, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.schedules = schedules;
        this.showCompleteDetail = showCompleteDetail;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_scheduleitems_new, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blank_footer, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < schedules.size()) {
            return 1;
        } else if (position == schedules.size()) {
            return 2;
        } else {
            return 0;
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        try {
            if (position < schedules.size()) {
                final ScheduleBean scheduleBean = schedules.get(position);

                viewHolder.tv_scheduleLineOfWork.setText(scheduleBean.getCategoryTitle());
                viewHolder.tv_scheduleSubLineOfWork.setText(scheduleBean.getSubCategoryTitle());

                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(scheduleBean.getStartDate() * 1000);
                viewHolder.tv_startDate.setText(ProjectUtils.getInstance().getFormattedDate(cal));
                viewHolder.tv_StartTime.setText(ProjectUtils.getInstance().getFormattedTime(cal));

                cal.setTimeInMillis(scheduleBean.getEndDate() * 1000);
                viewHolder.tv_endDate.setText(ProjectUtils.getInstance().getFormattedDate(cal));
                viewHolder.tv_endTime.setText(ProjectUtils.getInstance().getFormattedTime(cal));

                if (!TextUtils.isEmpty(scheduleBean.getDetail())) {
                    viewHolder.tv_scheduleDescription.setText(scheduleBean.getDetail());
                } else {
                    viewHolder.tv_scheduleDescription.setVisibility(View.GONE);
                }

                viewHolder.iv_delete.setOnClickListener(v -> {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(scheduleBean, 2);
                    }
                });

                if (scheduleBean.getStatus().equalsIgnoreCase("Pending")) {
                    viewHolder.tv_completed.setVisibility(View.VISIBLE);
                    viewHolder.tv_completed.setOnClickListener(v -> {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(scheduleBean, 1);
                        }
                    });
                    viewHolder.iv_edit.setVisibility(View.VISIBLE);
                    viewHolder.iv_edit.setOnClickListener(v -> {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(scheduleBean, 0);
                        }
                    });
                } else {
                    viewHolder.imgShowOption.setVisibility(View.GONE);
                    viewHolder.tv_completed.setVisibility(View.GONE);
                    viewHolder.iv_edit.setVisibility(View.GONE);
                }


                //----------------------Swipe Handling----------------
                viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
                viewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                    @Override
                    public void onStartOpen(SwipeLayout layout) {
                        ((ImageView) layout.findViewById(R.id.imgShowOption)).setImageResource(R.drawable.right_arrow_black);
                    }

                    @Override
                    public void onOpen(SwipeLayout layout) {

                    }

                    @Override
                    public void onStartClose(SwipeLayout layout) {
                        ((ImageView) layout.findViewById(R.id.imgShowOption)).setImageResource(R.drawable.left_arrow_black);
                    }

                    @Override
                    public void onClose(SwipeLayout layout) {

                    }

                    @Override
                    public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

                    }

                    @Override
                    public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

                    }
                });
                viewHolder.imgShowOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (viewHolder.swipeLayout.getOpenStatus() == SwipeLayout.Status.Open) {
                            viewHolder.swipeLayout.close(true);
                        } else {
                            viewHolder.swipeLayout.open(true);
                        }
                    }
                });

                mItemManger.bind(viewHolder.itemView, position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.slScheduleListRow;
    }

    @Override
    public int getItemCount() {
//        return clients.size();
        return showCompleteDetail ? (schedules.size() + 1) : schedules.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        ImageView imgShowOption;

        LinearLayout ll_item;
        TextView tv_scheduleLineOfWork, tv_scheduleSubLineOfWork, tv_scheduleDescription,
                tv_StartTime, tv_endTime, tv_startDate, tv_endDate;
        ImageView iv_edit, iv_delete;
        TextView tv_completed;

        public ViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.slScheduleListRow);
            imgShowOption = (ImageView) itemView.findViewById(R.id.imgShowOption);

            ll_item = (LinearLayout) itemView.findViewById(R.id.ll_item);

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

        }
    }


}
