package com.superapp.fragment.communications.approval;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.superapp.R;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.OnItemClickListener;
import com.superapp.beans.CommunicationBean;
import com.superapp.utils.PrefSetup;

import java.util.ArrayList;

public class AdapterApprovals extends RecyclerView.Adapter<AdapterApprovals.ViewHolder> {

    BaseAppCompatActivity activity;
    ArrayList<CommunicationBean> approvalsArrayList;
    OnItemClickListener onItemClickListener;
    String loginType = null;

    public AdapterApprovals(BaseAppCompatActivity activity, ArrayList<CommunicationBean> approvalArrayList, OnItemClickListener onItemClickListener) {
        this.activity = activity;
        this.approvalsArrayList = approvalArrayList;
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_category;
        TextView tv_photoCount;
        TextView tv_occupation;
        TextView tv_documentCount;
        TextView tv_description;
        LinearLayout ll_item;
        TextView tv_feedBackText, tv_status;

        LinearLayout ll_feedback, ll_description, ll_feedBackText, ll_approvals_next, ll_approvalsList;
        TextView tv_feedBack;

        public ViewHolder(View itemView) {
            super(itemView);
            loginType = PrefSetup.getInstance().getUserLoginType();
            tv_category = (TextView) itemView.findViewById(R.id.tv_category);
            tv_photoCount = (TextView) itemView.findViewById(R.id.tv_photoCount);
            tv_occupation = (TextView) itemView.findViewById(R.id.tv_occupation);
            tv_documentCount = (TextView) itemView.findViewById(R.id.tv_documentCount);
            tv_description = (TextView) itemView.findViewById(R.id.tv_description);
            tv_feedBack = (TextView) itemView.findViewById(R.id.tv_feedBack);
            tv_feedBackText = (TextView) itemView.findViewById(R.id.tv_feedBackText);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);

            ll_item = (LinearLayout) itemView.findViewById(R.id.ll_item);
            ll_feedback = (LinearLayout) itemView.findViewById(R.id.ll_feedback);
            ll_approvals_next = (LinearLayout) itemView.findViewById(R.id.ll_approvals_next);
            ll_feedBackText = (LinearLayout) itemView.findViewById(R.id.ll_feedBackText);

            ll_approvalsList = (LinearLayout) itemView.findViewById(R.id.ll_approvalsList);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_approvals_items, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blank_footer, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < approvalsArrayList.size()) {
            return 1;
        } else if (position == approvalsArrayList.size()) {
            return 2;
        } else {
            return 0;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // TODO user holder directly
        if (position < approvalsArrayList.size()) {
            final CommunicationBean approvalsBean = approvalsArrayList.get(position);
            //NotificationBean notification = notificationArrayList.get(position);

//            if ((PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("c"))) {

            holder.ll_item.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    // 1 is used for complete item click
                    onItemClickListener.onItemClick(approvalsBean, 1);
                }
            });
//            }

            holder.tv_category.setText(approvalsBean.getCategory());
            holder.tv_photoCount.setText(approvalsBean.getImages().size() + "");
            holder.tv_occupation.setText(approvalsBean.getSubCategory());
            holder.tv_documentCount.setText(approvalsBean.getDocuments().size() + "");
            holder.tv_description.setText(approvalsBean.getDescription());
            holder.tv_status.setText(approvalsBean.getStatus());
            holder.ll_feedBackText.setVisibility(View.GONE);
            holder.ll_feedback.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(approvalsBean.getClientReply())) {
                holder.ll_feedBackText.setVisibility(View.VISIBLE);
                holder.tv_feedBackText.setText(approvalsBean.getClientReply());
            } else {
                if (loginType.equalsIgnoreCase("c")) {
                    holder.ll_feedback.setVisibility(View.GONE);
                }
            }

//            if (loginType.equalsIgnoreCase("c")) {
//                if (TextUtils.isEmpty(holder.tv_feedBackText.getText().toString())) {
//                    holder.ll_feedBackText.setVisibility(View.GONE);
//                    holder.tv_feedBackText.setVisibility(View.GONE);
//                    holder.ll_feedback.setVisibility(View.VISIBLE);
//                } else {
//                    holder.ll_feedBackText.setVisibility(View.VISIBLE);
//                    holder.ll_feedback.setVisibility(View.GONE);
//                }
//            }

            if (approvalsBean.getStatus().equalsIgnoreCase("Request")) {
                holder.tv_status.setTextColor(activity.getResources().getColor(R.color.colorYellow));
                holder.ll_approvals_next.setBackgroundColor(activity.getResources().getColor(R.color.colorYellow));
                holder.tv_status.setText(approvalsBean.getStatus());
            } else if (approvalsBean.getStatus().equalsIgnoreCase("Decline")) {
                holder.tv_status.setTextColor(activity.getResources().getColor(R.color.colorOrange));
                holder.ll_approvals_next.setBackgroundColor(activity.getResources().getColor(R.color.colorOrange));
                holder.tv_status.setText(approvalsBean.getStatus());
            } else if (approvalsBean.getStatus().equalsIgnoreCase("Accept")) {
                holder.tv_status.setTextColor(activity.getResources().getColor(R.color.green));
                holder.ll_approvals_next.setBackgroundColor(activity.getResources().getColor(R.color.green));
                holder.tv_status.setText(activity.getString(R.string.accepted));
            } else if (approvalsBean.getStatus().equalsIgnoreCase("Not Convinced")) {
                holder.tv_status.setTextColor(activity.getResources().getColor(R.color.colorRed));
                holder.ll_approvals_next.setBackgroundColor(activity.getResources().getColor(R.color.colorRed));
                holder.tv_status.setText(approvalsBean.getStatus());
            } else {
                holder.tv_status.setTextColor(activity.getResources().getColor(R.color.black));
                holder.ll_approvals_next.setBackgroundColor(activity.getResources().getColor(R.color.black));
                holder.tv_status.setText(approvalsBean.getStatus());
            }


//                    holder.tv_feedBack.setVisibility(View.GONE);
            holder.tv_feedBack.setOnTouchListener(BaseAppCompatActivity.TOUCH);
//                    holder.tv_feedBack.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (onItemClickListener != null) {
//                                // 0 is used for button click
//                               // onItemClickListener.onItemClick(approvalsBean, 0);
//                            }
//                        }
//                    });

        } else if (position == approvalsArrayList.size()) {

        }
    }

    @Override
    public int getItemCount() {
        return approvalsArrayList.size() + 1;
    }

}

