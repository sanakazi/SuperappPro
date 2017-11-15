package com.superapp.fragment.dashboard.pager_fragment.tasklist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.OnItemClickListener;
import com.superapp.activity.base.OnViewClickListener;
import com.superapp.beans.BeanMaterial;
import com.superapp.beans.BeanTaskList;
import com.superapp.utils.PopupUtils;
import com.superapp.utils.PrefSetup;
import com.superapp.utils.ProjectUtils;
import com.superapp.utils.Utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class AdapterTaskList extends RecyclerView.Adapter<AdapterTaskList.ViewHolder> {

    Context activity;
    ArrayList<BeanTaskList> dashboardCoWorkers;
    OnItemClickListener onItemClickListener;
    boolean isOngoing;

    public AdapterTaskList(Context activity, ArrayList<BeanTaskList> dashboardCoWorkers, boolean isOngoing, OnItemClickListener onItemClickListener) {
        this.activity = activity;
        this.dashboardCoWorkers = dashboardCoWorkers;
        this.onItemClickListener = onItemClickListener;
        this.isOngoing = isOngoing;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_Address, tv_ProjectName, tv_ProjectType, tv_time, tv_date, tv_taskDescription, tv_status, tv_designerName,
                tv_designerCompanyName, tv_materials, tv_taskNote;

        ImageView iv_designerPhone;

        LinearLayout ll_Delayed, ll_Pending, ll_Process, ll_Accept, ll_Request, ll_taskDescription, ll_dateTime, ll_taskNote;

        Button bt_Delayed_JobDone, bt_Pending_JobDone, bt_Pending, bt_Delayed, bt_Process_JobDone, bt_Process, bt_Accept, bt_Reject;

        public ViewHolder(View itemView) {
            super(itemView);

            ll_Delayed = (LinearLayout) itemView.findViewById(R.id.ll_Delayed);
            ll_Pending = (LinearLayout) itemView.findViewById(R.id.ll_Pending);
            ll_Process = (LinearLayout) itemView.findViewById(R.id.ll_Process);
            ll_Accept = (LinearLayout) itemView.findViewById(R.id.ll_Accept);
            ll_Request = (LinearLayout) itemView.findViewById(R.id.ll_Request);
            ll_taskDescription = (LinearLayout) itemView.findViewById(R.id.ll_taskDescription);
            ll_dateTime = (LinearLayout) itemView.findViewById(R.id.ll_dateTime);
            ll_taskNote = (LinearLayout) itemView.findViewById(R.id.ll_taskNote);

            tv_ProjectName = (TextView) itemView.findViewById(R.id.tv_ProjectName);
            tv_ProjectType = (TextView) itemView.findViewById(R.id.tv_ProjectType);
            tv_Address = (TextView) itemView.findViewById(R.id.tv_Address);
            tv_taskDescription = (TextView) itemView.findViewById(R.id.tv_taskDescription);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            tv_designerName = (TextView) itemView.findViewById(R.id.tv_designerName);
            tv_designerCompanyName = (TextView) itemView.findViewById(R.id.tv_designerCompanyName);
            tv_materials = (TextView) itemView.findViewById(R.id.tv_materials);
            tv_taskNote = (TextView) itemView.findViewById(R.id.tv_taskNote);

            iv_designerPhone = (ImageView) itemView.findViewById(R.id.iv_designerPhone);

            bt_Delayed_JobDone = (Button) itemView.findViewById(R.id.bt_Delayed_JobDone);
            bt_Pending_JobDone = (Button) itemView.findViewById(R.id.bt_Pending_JobDone);
            bt_Pending = (Button) itemView.findViewById(R.id.bt_Pending);
            bt_Delayed = (Button) itemView.findViewById(R.id.bt_Delayed);
            bt_Process_JobDone = (Button) itemView.findViewById(R.id.bt_Process_JobDone);
            bt_Process = (Button) itemView.findViewById(R.id.bt_Process);
            bt_Accept = (Button) itemView.findViewById(R.id.bt_Accept);
            bt_Reject = (Button) itemView.findViewById(R.id.bt_Reject);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_tasklist_items, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < dashboardCoWorkers.size()) {
            return 1;
        } else if (position == dashboardCoWorkers.size()) {
            return 2;
        } else {
            return 0;
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // TODO user holder directly
        if (position < dashboardCoWorkers.size()) {
            final BeanTaskList taskItem = dashboardCoWorkers.get(position);

            holder.tv_ProjectName.setText(taskItem.getProjectName());
            holder.tv_ProjectType.setText(taskItem.getProjectType());
            if (!TextUtils.isEmpty(taskItem.getAddress()))
                holder.tv_Address.setText(ProjectUtils.getInstance().getFormattedAddress(taskItem.getAddress()).toString());

            if (!taskItem.getTaskDescription().isEmpty()) {
                holder.ll_taskDescription.setVisibility(View.VISIBLE);
                holder.tv_taskDescription.setText(taskItem.getTaskDescription());
            } else {
                holder.ll_taskDescription.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(taskItem.getNote())) {
                holder.ll_taskNote.setVisibility(View.VISIBLE);
                holder.tv_taskNote.setText(taskItem.getTaskDescription());
            } else {
                holder.ll_taskNote.setVisibility(View.GONE);
            }


            if (taskItem.getSuitableDateTime() != 0) {
                holder.ll_dateTime.setVisibility(View.VISIBLE);
                Calendar calendar = Calendar.getInstance();
//            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                calendar.setTimeInMillis(taskItem.getSuitableDateTime() * 1000);
                holder.tv_date.setText(ProjectUtils.getInstance().getFormattedDate(calendar));
                holder.tv_time.setText(ProjectUtils.getInstance().getFormattedTime(calendar));
            } else {
                holder.ll_dateTime.setVisibility(View.GONE);
            }


////            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//            Calendar calendar = Calendar.getInstance();
////            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//            Calendar cal = Calendar.getInstance();
//            calendar.setTimeInMillis(taskItem.getSuitableDate() * 1000);
//            cal.setTimeInMillis(taskItem.getSuitableTime() * 1000);
//
//            holder.tv_date.setText(ProjectUtils.getInstance().getFormattedDate(calendar));
//            holder.tv_time.setText(ProjectUtils.getInstance().getFormattedTime(cal));


            holder.tv_designerCompanyName.setText(taskItem.getDesignerCompanyName());
            holder.tv_designerName.setText(taskItem.getDesignerName());

            if (taskItem.getMaterials().size() > 0) {
                String materialText = "";
                int i = 1;
                for (BeanMaterial data : taskItem.getMaterials()) {
                    if (materialText.length() > 0) {
                        materialText += "\n";
                    }
                    materialText += i++ + ". " + data;
//                    if ("".equals(materialText))
//                        materialText = data;
//                    else
//                        materialText = ", " + data;
                    holder.tv_materials.setText(materialText);
                }

//                holder.tv_material.setText(materialText);
            } else {
                holder.tv_materials.setText(activity.getString(R.string.noMaterialAdded));
            }
//            if (taskItem.getMaterials().size() > 0) {
//                String materialText = "";
//                int i = 1;
//                for (BeanMaterial material : taskItem.getMaterials()) {
//                    if (materialText.length() > 0) {
//                        materialText += "\n";
//                    }
//                    materialText += i++ + ". " + material.getTitle();
//                }
//                holder.tv_materials.setText(materialText);
//            } else {
//                holder.tv_materials.setText("No Material Added");
//            }

//            StringBuilder material = new StringBuilder("");
//            int i = 1;
//            for (BeanMaterial mat : taskItem.getMaterials()) {
//                if (material.length() > 0) {
//                    material.append("\n");
//                }
//                material.append(i++ + ". " + mat.getTitle());
//            }
//            holder.tv_materials.setText(material.toString());

            holder.ll_Delayed.setVisibility(View.GONE);
            holder.ll_Pending.setVisibility(View.GONE);
            holder.ll_Process.setVisibility(View.GONE);
            holder.ll_Accept.setVisibility(View.GONE);
            holder.ll_Request.setVisibility(View.GONE);

            if (isOngoing) {
                if (taskItem.getStatus().equalsIgnoreCase("Request")) {
                    holder.ll_Request.setVisibility(View.VISIBLE);
                    holder.tv_status.setText(taskItem.getStatus());

                } else if (taskItem.getStatus().equalsIgnoreCase("Accept")) {
                    holder.ll_Accept.setVisibility(View.VISIBLE);
                    holder.tv_status.setText(activity.getString(R.string.accepted));

                } else if (taskItem.getStatus().equalsIgnoreCase("Process")) {
                    holder.ll_Process.setVisibility(View.VISIBLE);
                    holder.tv_status.setText(taskItem.getStatus());

                } else if (taskItem.getStatus().equalsIgnoreCase("Pending")) {
                    holder.ll_Pending.setVisibility(View.VISIBLE);
                    holder.tv_status.setText(taskItem.getStatus());

                } else if (taskItem.getStatus().equalsIgnoreCase("Delayed")) {
                    holder.ll_Delayed.setVisibility(View.VISIBLE);
                    holder.tv_status.setText(taskItem.getStatus());

                }
                holder.bt_Delayed_JobDone.setOnClickListener(v -> {
                    if (onItemClickListener != null) {
                        taskItem.setStatusForUpdate("Job Done");
                        onItemClickListener.onItemClick(taskItem, position);
                    }
                });
                holder.bt_Pending_JobDone.setOnClickListener(v -> {
                    if (onItemClickListener != null) {
                        taskItem.setStatusForUpdate("Job Done");
                        onItemClickListener.onItemClick(taskItem, position);
                    }
                });
                holder.bt_Pending.setOnClickListener(v -> {
                    if (onItemClickListener != null) {
                        taskItem.setStatusForUpdate("Pending");
                        onItemClickListener.onItemClick(taskItem, position);
                    }
                });
                holder.bt_Delayed.setOnClickListener(v -> {
                    if (onItemClickListener != null) {
                        taskItem.setStatusForUpdate("Delayed");
                        onItemClickListener.onItemClick(taskItem, position);
                    }
                });
                holder.bt_Process_JobDone.setOnClickListener(v -> {
                    if (onItemClickListener != null) {
                        taskItem.setStatusForUpdate("Job Done");
                        onItemClickListener.onItemClick(taskItem, position);
                    }
                });
                holder.bt_Process.setOnClickListener(v -> {
                    if (onItemClickListener != null) {
                        taskItem.setStatusForUpdate("Process");
                        onItemClickListener.onItemClick(taskItem, position);
                    }
                });
                holder.bt_Accept.setOnClickListener(v -> {
                    if (onItemClickListener != null) {
                        taskItem.setStatusForUpdate("Accept");
                        onItemClickListener.onItemClick(taskItem, position);
                    }
                });
                holder.bt_Reject.setOnClickListener(v -> {
                    if (onItemClickListener != null) {
                        PopupUtils.getInstance().showDialogWithEditText(activity, "Reject Task", stringReason -> {
                            taskItem.setStatusForUpdate("Reject");
                            taskItem.setRejectReason((String) stringReason);
                            onItemClickListener.onItemClick(taskItem, position);
                        });
                    }
                });
            }

            holder.iv_designerPhone.setOnTouchListener(BaseAppCompatActivity.imageTouch);
            holder.iv_designerPhone.setOnClickListener(v -> Utilities.getInstance().doCall(activity, taskItem.getDesignerPhone()));

        } else if (position == dashboardCoWorkers.size()) {

        }
    }


    @Override
    public int getItemCount() {
        return dashboardCoWorkers.size();
    }

}
