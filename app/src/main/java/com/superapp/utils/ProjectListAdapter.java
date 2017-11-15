package com.superapp.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.superapp.R;
import com.superapp.activity.base.OnItemClickListener;
import com.superapp.activity.base.OnListItemClickListener;
import com.superapp.beans.ProjectBean;

import java.util.ArrayList;

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ViewHolder> {

    private Context activity;
    private ArrayList<ProjectBean> projectList;
    private OnListItemClickListener<ProjectBean> onItemClickListener;
    private int count = 0;
    private int downGradeType;

    public ProjectListAdapter(Context activity, ArrayList<ProjectBean> projectList, OnListItemClickListener<ProjectBean> onItemClickListener, int downGradeType) {
        this.activity = activity;
        this.projectList = projectList;
        this.onItemClickListener = onItemClickListener;
        this.downGradeType = downGradeType;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox cb_projectList;
        LinearLayout ll_listItem;

        public ViewHolder(View itemView) {
            super(itemView);
            cb_projectList = (CheckBox) itemView.findViewById(R.id.cb_projectList);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.projectlist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // TODO user holder directly
        final ProjectBean project = projectList.get(position);

        holder.cb_projectList.setText(project.getProjectName());
        holder.cb_projectList.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(project, position);
                if (downGradeType == 1) {
                    if (2 <= count && holder.cb_projectList.isChecked()) {
                        holder.cb_projectList.setChecked(false);
                        Toast.makeText(activity, "maximum project selected", Toast.LENGTH_SHORT).show();
                    } else {
                        if (holder.cb_projectList.isChecked()) {
                            count++;

                        } else {
                            count--;
                            holder.cb_projectList.setChecked(false);

                        }
                    }
                } else if (downGradeType == 2) {
                    if (5 <= count && holder.cb_projectList.isChecked()) {
                        holder.cb_projectList.setChecked(false);
                        Toast.makeText(activity, "maximum project selected", Toast.LENGTH_SHORT).show();
                    } else {
                        if (holder.cb_projectList.isChecked()) {
                            count++;
                        } else {
                            count--;
                            holder.cb_projectList.setChecked(false);
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

}