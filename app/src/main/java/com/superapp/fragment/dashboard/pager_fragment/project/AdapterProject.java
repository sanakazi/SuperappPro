package com.superapp.fragment.dashboard.pager_fragment.project;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.base.OnItemClickListener;
import com.superapp.beans.ProjectBean;
import com.superapp.utils.PrefSetup;
import com.superapp.utils.ProjectUtils;

import java.util.ArrayList;

/**
 * Created by Rakesh on 04-Aug-16.
 */
public class AdapterProject extends RecyclerView.Adapter<AdapterProject.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_projectStartedDays;
        public TextView tv_workStartedDays;
        public TextView tv_projectName;
        public TextView tv_notificationCounter;
        public TextView tv_percentCompleted;
        public View viewFill1;
        public View viewEmpty1;
        public View viewFill2;
        public View viewEmpty2;

        public LinearLayout ll_item, ll_item_forCoworker, ll_percentageBar, ll_days;

        public TextView tv_projectTitle;
        public TextView tv_projectDetailProjectOverViewAddress;

        public ViewHolder(View view) {
            super(view);
            ll_item = (LinearLayout) view.findViewById(R.id.ll_item);
            ll_item_forCoworker = (LinearLayout) view.findViewById(R.id.ll_item_forCoworker);
            tv_projectStartedDays = (TextView) view.findViewById(R.id.tv_projectStartedDays);
            tv_workStartedDays = (TextView) view.findViewById(R.id.tv_workStartedDays);
            tv_projectName = (TextView) view.findViewById(R.id.tv_projectName);
            tv_notificationCounter = (TextView) view.findViewById(R.id.tv_notificationCounter);
            tv_percentCompleted = (TextView) view.findViewById(R.id.tv_percentCompleted);
            viewFill1 = (View) view.findViewById(R.id.viewFill1);
            viewEmpty1 = (View) view.findViewById(R.id.viewEmpty1);
            viewFill2 = (View) view.findViewById(R.id.viewFill2);
            viewEmpty2 = (View) view.findViewById(R.id.viewEmpty2);
            ll_percentageBar = (LinearLayout) view.findViewById(R.id.ll_percentageBar);
            ll_days = (LinearLayout) view.findViewById(R.id.ll_days);

            tv_projectTitle = (TextView) view.findViewById(R.id.tv_projectTitle);
            tv_projectDetailProjectOverViewAddress = (TextView) view.findViewById(R.id.tv_projectDetailProjectOverViewAddress);
        }
    }

    ApplicationContext activity;
    private String loginType;
    ArrayList<ProjectBean> projects;
    OnItemClickListener onItemClickListener;

    public AdapterProject(ApplicationContext activity, ArrayList<ProjectBean> projects, OnItemClickListener onItemClickListener) {
        this.activity = activity;
        this.projects = projects;
        loginType = PrefSetup.getInstance().getUserLoginType();
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_dashboard_project_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blank_footer, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < projects.size()) {
            return 1;
        } else if (position == projects.size()) {
            return 2;
        } else {
            return 0;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (position < projects.size()) {
            final ProjectBean project = projects.get(position);

            LinearLayout ll_item;
            if (loginType.equalsIgnoreCase("d") || loginType.equalsIgnoreCase("c")) {
                ll_item = holder.ll_item;
                holder.ll_item.setVisibility(View.VISIBLE);
                holder.ll_item_forCoworker.setVisibility(View.GONE);

//                long designStartDays = ProjectUtils.getInstance().getDaysDifferenceFromCurrentDate(project.getProjectStartedDays() * 1000);
//                holder.tv_projectStartedDays.setText((designStartDays < 0 ? 0 : designStartDays) + "");
//
//                long workStartDays = ProjectUtils.getInstance().getDaysDifferenceFromCurrentDate(project.getWorkStartedDays() * 1000);
//                holder.tv_workStartedDays.setText((workStartDays < 0 ? 0 : workStartDays) + "");


                long workStartDays = ProjectUtils.getInstance().getDaysDifferenceFromCurrentDate(project.getWorkStartedDays() * 1000);
                holder.tv_projectStartedDays.setText((workStartDays < 0 ? 0 : workStartDays) + "");

//                long workStartDays = ProjectUtils.getInstance().getDaysDifferenceFromCurrentDate(project.getWorkStartedDays() * 1000);
                holder.tv_workStartedDays.setText(project.getHandoverdate() + "");


                holder.tv_projectName.setText(project.getProjectName());
                holder.tv_notificationCounter.setText(project.getNotificationCounter());
                holder.tv_percentCompleted.setText(project.getPercentCompleted() + "%");

                // Dynamically Reset View according to percentage completed.
                int percentCompleted = Integer.parseInt(project.getPercentCompleted());
                LinearLayout.LayoutParams paramViewFill = new LinearLayout.LayoutParams(0, 3);
                paramViewFill.weight = percentCompleted;
                holder.viewFill1.setLayoutParams(paramViewFill);
                holder.viewFill2.setLayoutParams(paramViewFill);
                LinearLayout.LayoutParams paramViewEmpty = new LinearLayout.LayoutParams(0, 3);
                paramViewEmpty.weight = 100 - percentCompleted;
                holder.viewEmpty1.setLayoutParams(paramViewEmpty);
                holder.viewEmpty2.setLayoutParams(paramViewEmpty);

                if (percentCompleted < 100) {
                    ll_item.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorLightGray));
                    holder.ll_percentageBar.setVisibility(View.VISIBLE);
                    holder.ll_days.setBackgroundResource(R.drawable.round_edittext);
                    holder.tv_notificationCounter.setBackgroundResource(R.drawable.round_edittext);
                } else {
                    holder.ll_percentageBar.setVisibility(View.INVISIBLE);
                    ll_item.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorDarkGreen));
                    holder.ll_days.setBackgroundResource(0);
                    holder.tv_notificationCounter.setBackgroundResource(0);
                }

            } else {
                ll_item = holder.ll_item_forCoworker;
                holder.ll_item_forCoworker.setVisibility(View.VISIBLE);
                holder.ll_item.setVisibility(View.GONE);

                holder.tv_projectTitle.setText(project.getProjectName());
                holder.tv_projectDetailProjectOverViewAddress.setText(ProjectUtils.getInstance().getFormattedAddress(project.getLocation()).toString());
            }

            ll_item.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(project, position);
                }
            });


            ll_item.invalidate();
        } else if (position == projects.size()) {
        }
    }

    @Override
    public int getItemCount() {
        return projects.size() + 1;
    }
}
