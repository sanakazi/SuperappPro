package com.superapp.fragment.settings;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.superapp.R;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.OnItemClickListener;
import com.superapp.beans.ProjectBean;

import java.util.ArrayList;

public class AdapterSetting extends RecyclerView.Adapter<AdapterSetting.ViewHolder> {

    BaseAppCompatActivity activity;
    ArrayList<ProjectBean> projectList;
    OnItemClickListener onItemClickListener;

    public AdapterSetting(BaseAppCompatActivity activity, ArrayList<ProjectBean> projectList, OnItemClickListener onItemClickListener) {
        this.activity = activity;
        this.projectList = projectList;
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ll_projectItem;
        TextView tv_projectItemCounter, tv_projectItemName;

        public ViewHolder(View itemView) {
            super(itemView);
            ll_projectItem = (LinearLayout) itemView.findViewById(R.id.ll_projectItem);
            tv_projectItemCounter = (TextView) itemView.findViewById(R.id.tv_projectItemCounter);
            tv_projectItemName = (TextView) itemView.findViewById(R.id.tv_projectItemName);
            ll_projectItem.setBackgroundResource(R.color.colorLightGray);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // TODO user holder directly
        final ProjectBean project = projectList.get(position);
        holder.tv_projectItemCounter.setVisibility(View.GONE);
        holder.tv_projectItemName.setText(project.getProjectName());
        holder.tv_projectItemName.setGravity(Gravity.LEFT);

        holder.ll_projectItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(project, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

}
