package com.superapp.fragment.checklist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.superapp.R;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.OnItemClickListener;
import com.superapp.beans.BeanMaterial;
import com.superapp.beans.BeanTaskList;
import com.superapp.utils.PrefSetup;
import com.superapp.utils.ProjectUtils;
import com.superapp.utils.Utilities;

import java.util.ArrayList;
import java.util.Calendar;

public class AdapterCheckList extends RecyclerView.Adapter<AdapterCheckList.ViewHolder> {

    BaseAppCompatActivity activity;
    ArrayList<BeanTaskList> checkListArrayList;
    OnItemClickListener onItemClickListener;
    private String loginType;

    public AdapterCheckList(BaseAppCompatActivity activity, ArrayList<BeanTaskList> checkListArrayList, OnItemClickListener onItemClickListener) {
        this.activity = activity;
        this.checkListArrayList = checkListArrayList;
        this.onItemClickListener = onItemClickListener;
        loginType = PrefSetup.getInstance().getUserLoginType();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ll_checklistItem, ll_coWorkerDetail, ll_checklistDetail;
        TextView tv_coworkerName, tv_suitableDate, tv_tv_suitableTime, tv_TaskSubCategory,
                tv_taskDescription, tv_status, tv_material, tv_taskDescriptionText;
        ImageView iv_call, iv_checklistShowHide;
        TableLayout tl_dateTime;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_coworkerName = (TextView) itemView.findViewById(R.id.tv_coworkerName);
            tv_suitableDate = (TextView) itemView.findViewById(R.id.tv_suitableDate);
            tv_tv_suitableTime = (TextView) itemView.findViewById(R.id.tv_tv_suitableTime);
            tv_TaskSubCategory = (TextView) itemView.findViewById(R.id.tv_TaskSubCategory);
            tv_taskDescriptionText = (TextView) itemView.findViewById(R.id.tv_taskDescriptionText);
            tv_taskDescription = (TextView) itemView.findViewById(R.id.tv_taskDescription);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            tv_material = (TextView) itemView.findViewById(R.id.tv_material);

            ll_checklistItem = (LinearLayout) itemView.findViewById(R.id.ll_checklistItem);
            ll_coWorkerDetail = (LinearLayout) itemView.findViewById(R.id.ll_coWorkerDetail);
            ll_checklistDetail = (LinearLayout) itemView.findViewById(R.id.ll_checklistDetail);

            iv_call = (ImageView) itemView.findViewById(R.id.iv_call);
            iv_checklistShowHide = (ImageView) itemView.findViewById(R.id.iv_checklistShowHide);

            tl_dateTime = (TableLayout) itemView.findViewById(R.id.tl_dateTime);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_checklist_items, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blank_footer, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < checkListArrayList.size()) {
            return 1;
        } else if (position == checkListArrayList.size()) {
            return 2;
        } else {
            return 0;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // TODO user holder directly
        if (position < checkListArrayList.size()) {
            final BeanTaskList beanChecklist = checkListArrayList.get(position);

            if (loginType.equalsIgnoreCase("d")) {
                holder.ll_coWorkerDetail.setVisibility(View.VISIBLE);
            } else {
                holder.ll_coWorkerDetail.setVisibility(View.GONE);
            }
            holder.tv_coworkerName.setText(beanChecklist.getName());
            holder.iv_call.setOnClickListener(v ->
                    Utilities.getInstance().doCall(activity, beanChecklist.getMobile()));

            if (beanChecklist.getSuitableDateTime() != 0) {
                holder.tl_dateTime.setVisibility(View.VISIBLE);
                Calendar calendar = Calendar.getInstance();
//            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                calendar.setTimeInMillis(beanChecklist.getSuitableDateTime() * 1000);
                holder.tv_suitableDate.setText(ProjectUtils.getInstance().getFormattedDate(calendar));
                holder.tv_tv_suitableTime.setText(ProjectUtils.getInstance().getFormattedTime(calendar));
            } else {
                holder.tl_dateTime.setVisibility(View.GONE);
            }

            holder.tv_TaskSubCategory.setText(beanChecklist.getCategory());

            if (!beanChecklist.getTaskDescription().isEmpty()) {
                holder.tv_taskDescription.setVisibility(View.VISIBLE);
                holder.tv_taskDescription.setText(beanChecklist.getTaskDescription());
            } else {
                holder.tv_taskDescriptionText.setVisibility(View.GONE);
                holder.tv_taskDescription.setVisibility(View.GONE);
            }

            holder.tv_status.setText(beanChecklist.getStatus());

            if (beanChecklist.getStatus().equalsIgnoreCase("Pending")) {
                holder.tv_status.setTextColor(activity.getResources().getColor(R.color.pendingColor));
            } else if (beanChecklist.getStatus().equalsIgnoreCase("Reject")) {
                holder.tv_status.setTextColor(activity.getResources().getColor(R.color.black));
            } else if (beanChecklist.getStatus().equalsIgnoreCase("Accept")) {
                holder.tv_status.setText(activity.getString(R.string.acknowledged));
                holder.tv_status.setTextColor(activity.getResources().getColor(R.color.colorPink));
            } else if (beanChecklist.getStatus().equalsIgnoreCase("Process")) {
                holder.tv_status.setText(activity.getString(R.string.inProcessText));
                holder.tv_status.setTextColor(activity.getResources().getColor(R.color.inProcessColor));
            } else if (beanChecklist.getStatus().equalsIgnoreCase("Job Done")) {
                holder.tv_status.setTextColor(activity.getResources().getColor(R.color.colorLightGreen));
            } else if (beanChecklist.getStatus().equalsIgnoreCase("Delayed")) {
                holder.tv_status.setTextColor(activity.getResources().getColor(R.color.delayedColor));
            } else if (beanChecklist.getStatus().equalsIgnoreCase("Not Convinced")) {
                holder.tv_status.setTextColor(activity.getResources().getColor(R.color.black));
            } else {
                holder.tv_status.setTextColor(activity.getResources().getColor(R.color.black));
            }

            if (beanChecklist.getMaterials().size() > 0) {
                String materialText = "";
                int i = 1;
                for (BeanMaterial material : beanChecklist.getMaterials()) {
                    if (materialText.length() > 0) {
                        materialText += "\n";
                    }
                    materialText += i++ + ". " + material.getTitle();
                }
                holder.tv_material.setText(materialText);
            } else {
                holder.tv_material.setText(activity.getString(R.string.noMaterialAdded));
            }

            holder.ll_checklistItem.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(beanChecklist, position);
                }
            });

            if (!beanChecklist.isOpen) {
                holder.ll_checklistDetail.setVisibility(View.INVISIBLE);
                holder.ll_checklistDetail.setVisibility(View.GONE);
                holder.iv_checklistShowHide.setImageResource(R.drawable.pluse);
            } else {
                holder.ll_checklistDetail.setVisibility(View.VISIBLE);
                holder.iv_checklistShowHide.setImageResource(R.drawable.minus);
            }

            holder.iv_checklistShowHide.setOnTouchListener((v, event) -> {
                try {
                    if (holder.ll_checklistDetail.getVisibility() == View.VISIBLE) {
                        beanChecklist.isOpen = false;
                        holder.ll_checklistDetail.setVisibility(View.INVISIBLE);
                        holder.ll_checklistDetail.setVisibility(View.GONE);
                        holder.iv_checklistShowHide.setImageResource(R.drawable.pluse);
                    } else {
                        beanChecklist.isOpen = true;
                        holder.ll_checklistDetail.setVisibility(View.VISIBLE);
                        holder.iv_checklistShowHide.setImageResource(R.drawable.minus);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            });

//            holder.tv_feedBackText.setVisibility(View.GONE);
//            holder.tv_feedBack.setVisibility(View.GONE);
//            if (!TextUtils.isEmpty(communicationBean.getClientReply())) {
//                holder.tv_feedBackText.setVisibility(View.VISIBLE);
//                holder.tv_feedBackText.setText(communicationBean.getClientReply());
//            } else {
//                if (loginType.equalsIgnoreCase("c")) {
//                    holder.tv_feedBack.setVisibility(View.VISIBLE);
//                    holder.tv_feedBack.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (onItemClickListener != null) {
//                                onItemClickListener.onItemClick(communicationBean, position);
//                            }
//                        }
//                    });
//                }
//            }

        } else if (position == checkListArrayList.size()) {

        }
    }

    @Override
    public int getItemCount() {
        return checkListArrayList.size() + 1;
    }
}

