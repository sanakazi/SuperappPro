package com.superapp.fragment.communications.recomandations;

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

/**
 * Created by APPNWEB on 03-09-2016.
 */
public class AdapterRecommendations extends RecyclerView.Adapter<AdapterRecommendations.ViewHolder> {

    String loginType = null;
    BaseAppCompatActivity activity;
    ArrayList<CommunicationBean> recommendationsArrayList;
    OnItemClickListener onItemClickListener;

    public AdapterRecommendations(BaseAppCompatActivity activity, ArrayList<CommunicationBean> recommendationsArrayList, OnItemClickListener onItemClickListener) {
        this.activity = activity;
        this.recommendationsArrayList = recommendationsArrayList;
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_recommendationsText, tv_status;
        TextView tv_feedBackText, tv_feedBack;
        LinearLayout ll_recommendationsItem, ll_feedback, ll_recommendations_next, ll_feedBackText;

        public ViewHolder(View itemView) {
            super(itemView);
            loginType = PrefSetup.getInstance().getUserLoginType();
            tv_recommendationsText = (TextView) itemView.findViewById(R.id.tv_recommendationsText);
            tv_feedBackText = (TextView) itemView.findViewById(R.id.tv_feedBackText);
            tv_feedBack = (TextView) itemView.findViewById(R.id.tv_feedBack);
            ll_recommendationsItem = (LinearLayout) itemView.findViewById(R.id.ll_recommendationsItem);
            ll_feedBackText = (LinearLayout) itemView.findViewById(R.id.ll_feedBackText);
            ll_feedback = (LinearLayout) itemView.findViewById(R.id.ll_feedback);
            ll_recommendations_next = (LinearLayout) itemView.findViewById(R.id.ll_recommendations_next);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_recommendations_items, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blank_footer, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < recommendationsArrayList.size()) {
            return 1;
        } else if (position == recommendationsArrayList.size()) {
            return 2;
        } else {
            return 0;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // TODO user holder directly
        if (position < recommendationsArrayList.size()) {
            final CommunicationBean communicationBean = recommendationsArrayList.get(position);
            holder.tv_recommendationsText.setText(communicationBean.getDescription());

            holder.ll_feedBackText.setVisibility(View.GONE);
            holder.ll_feedback.setVisibility(View.GONE);
            holder.tv_status.setVisibility(View.GONE);
            holder.ll_recommendations_next.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(communicationBean.getClientReply())) {
                holder.ll_feedBackText.setVisibility(View.VISIBLE);
                holder.tv_feedBackText.setText(communicationBean.getClientReply());
            } else {
                if (loginType.equalsIgnoreCase("d")) {
                    if (communicationBean.getStatus().equalsIgnoreCase("Request")) {
                        holder.ll_feedback.setVisibility(View.GONE);
                    }
                }
            }

            holder.tv_status.setVisibility(View.VISIBLE);


            if (communicationBean.getStatus().equalsIgnoreCase("Request")) {
                holder.tv_status.setTextColor(activity.getResources().getColor(R.color.colorYellow));
                holder.ll_recommendations_next.setBackgroundColor(activity.getResources().getColor(R.color.colorYellow));
                holder.tv_status.setText(communicationBean.getStatus());

            } else if (communicationBean.getStatus().equalsIgnoreCase("Decline")) {
                holder.tv_status.setTextColor(activity.getResources().getColor(R.color.colorOrange));
                holder.ll_recommendations_next.setBackgroundColor(activity.getResources().getColor(R.color.colorOrange));
                holder.tv_status.setText(communicationBean.getStatus());

            } else if (communicationBean.getStatus().equalsIgnoreCase("Accept")) {
                holder.tv_status.setTextColor(activity.getResources().getColor(R.color.green));
                holder.ll_recommendations_next.setBackgroundColor(activity.getResources().getColor(R.color.green));
                holder.tv_status.setText(activity.getString(R.string.accepted));

            } else if (communicationBean.getStatus().equalsIgnoreCase("Not Convinced")) {
                holder.tv_status.setTextColor(activity.getResources().getColor(R.color.colorRed));
                holder.ll_recommendations_next.setBackgroundColor(activity.getResources().getColor(R.color.colorRed));
                holder.tv_status.setText(communicationBean.getStatus());

            } else {
                holder.tv_status.setTextColor(activity.getResources().getColor(R.color.black));
                holder.ll_recommendations_next.setBackgroundColor(activity.getResources().getColor(R.color.black));
                holder.tv_status.setText(communicationBean.getStatus());

            }

            holder.ll_recommendationsItem.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(communicationBean, position);
                }
            });
        } else if (position == recommendationsArrayList.size()) {

        }
    }

    @Override
    public int getItemCount() {
        return recommendationsArrayList.size() + 1;
    }

}

