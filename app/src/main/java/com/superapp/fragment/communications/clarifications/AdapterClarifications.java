//package com.superapp.fragment.communications.clarifications;
//
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.superapp.R;
//import com.superapp.activity.base.BaseAppCompatActivity;
//import com.superapp.activity.base.OnItemClickListener;
//import com.superapp.beans.CommunicationBean;
//import com.superapp.utils.PrefSetup;
//
//import java.util.ArrayList;
//
///**
// * Created by APPNWEB on 03-09-2016.
// */
//public class AdapterClarifications extends RecyclerView.Adapter<AdapterClarifications.ViewHolder> {
//    String loginType = null;
//    BaseAppCompatActivity activity;
//    ArrayList<CommunicationBean> clarificationsArrayList;
//    OnItemClickListener onItemClickListener;
//
//    public AdapterClarifications(BaseAppCompatActivity activity, ArrayList<CommunicationBean> clarificationArrayList, OnItemClickListener onItemClickListener) {
//        this.activity = activity;
//        loginType = PrefSetup.getInstance().getUserLoginType();
//        this.clarificationsArrayList = clarificationArrayList;
//        this.onItemClickListener = onItemClickListener;
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        TextView tv_clarificationsText;
//        LinearLayout ll_clarificationsItem,ll_feedback;
//
//        TextView tv_feedBackText, tv_feedBack,tv_status;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            tv_clarificationsText = (TextView) itemView.findViewById(R.id.tv_clarificationsText);
//            tv_feedBackText = (TextView) itemView.findViewById(R.id.tv_feedBackText);
//            tv_feedBack = (TextView) itemView.findViewById(R.id.tv_feedBack);
//            ll_clarificationsItem = (LinearLayout) itemView.findViewById(R.id.ll_clarificationsItem);
//            ll_feedback = (LinearLayout) itemView.findViewById(R.id.ll_feedback);
//            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
//
//        }
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view;
//        if (viewType == 1) {
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_clarifications_items, parent, false);
//        } else {
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blank_footer, parent, false);
//        }
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if (position < clarificationsArrayList.size()) {
//            return 1;
//        } else if (position == clarificationsArrayList.size()) {
//            return 2;
//        } else {
//            return 0;
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, final int position) {
//        // TODO user holder directly
//        if (position < clarificationsArrayList.size()) {
//            final CommunicationBean communicationBean = clarificationsArrayList.get(position);
//            holder.tv_clarificationsText.setText(communicationBean.getDescription());
//            holder.tv_feedBackText.setVisibility(View.GONE);
//           // holder.tv_feedBack.setVisibility(View.GONE);
//            holder.ll_feedback.setVisibility(View.GONE);
//            holder.tv_status.setVisibility(View.GONE);
//
//            if (!TextUtils.isEmpty(communicationBean.getClientReply())) {
//                holder.tv_feedBackText.setVisibility(View.VISIBLE);
//                holder.tv_feedBackText.setText(communicationBean.getClientReply());
//            } else {
//                if (loginType.equalsIgnoreCase("d")) {
//                    //holder.tv_feedBack.setVisibility(View.VISIBLE);
//                    holder.ll_feedback.setVisibility(View.VISIBLE);
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
//            if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
//                holder.tv_status.setVisibility(View.VISIBLE);
//                holder.tv_status.setText(communicationBean.getStatus());
//
//                if (communicationBean.getStatus().equalsIgnoreCase("Request")) {
//                    holder.tv_status.setTextColor(activity.getResources().getColor(R.color.floatingButtonColor));
//                    holder.ll_feedback.setVisibility(View.VISIBLE);
//                } else if (communicationBean.getStatus().equalsIgnoreCase("Decline")) {
//                    holder.tv_status.setTextColor(activity.getResources().getColor(R.color.colorAccentDark));
//                    holder.ll_feedback.setVisibility(View.GONE);
//                } else if (communicationBean.getStatus().equalsIgnoreCase("Accept")) {
//                    holder.tv_status.setTextColor(activity.getResources().getColor(R.color.green));
//                    holder.ll_feedback.setVisibility(View.GONE);
//                } else if (communicationBean.getStatus().equalsIgnoreCase("Not Convinced")) {
//                    holder.tv_status.setTextColor(activity.getResources().getColor(R.color.colorAccentDark));
//                    holder.ll_feedback.setVisibility(View.GONE);
//                } else {
//                    holder.tv_status.setTextColor(activity.getResources().getColor(R.color.black));
//                    holder.ll_feedback.setVisibility(View.GONE);
//                }
//            }
//
//        } else if (position == clarificationsArrayList.size()) {
//
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return clarificationsArrayList.size() + 1;
//    }
//
//}
//
