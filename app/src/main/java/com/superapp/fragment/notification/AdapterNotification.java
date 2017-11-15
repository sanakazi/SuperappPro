package com.superapp.fragment.notification;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.superapp.R;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.OnItemClickListener;
import com.superapp.beans.NotificationBean;
import com.superapp.utils.ProjectUtils;

import java.util.ArrayList;
import java.util.Calendar;


public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.ViewHolder> {

    BaseAppCompatActivity activity;
    ArrayList<NotificationBean> notificationArrayList;
    OnItemClickListener onItemClickListener;

    public AdapterNotification(BaseAppCompatActivity activity, ArrayList<NotificationBean> notificationArrayList, OnItemClickListener onItemClickListener) {
        this.activity = activity;
        this.notificationArrayList = notificationArrayList;
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_notificationText, tv_notificationTime, tv_notificationDate;
        LinearLayout ll_notificationItem;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_notificationText = (TextView) itemView.findViewById(R.id.tv_notificationText);
            tv_notificationDate = (TextView) itemView.findViewById(R.id.tv_notificationDate);
            tv_notificationTime = (TextView) itemView.findViewById(R.id.tv_notificationTime);
            ll_notificationItem = (LinearLayout) itemView.findViewById(R.id.ll_notificationItem);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // TODO user holder directly
        final NotificationBean notificationBean = notificationArrayList.get(position);
        Calendar dateTime = Calendar.getInstance();
        dateTime.setTimeInMillis(notificationBean.getAddeddate() * 1000);
        holder.tv_notificationText.setText(notificationBean.getMessage());
        holder.tv_notificationDate.setText(ProjectUtils.getInstance().getFormattedDate(dateTime));
        holder.tv_notificationTime.setText(ProjectUtils.getInstance().getFormattedTime(dateTime));

        holder.ll_notificationItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(notificationBean, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationArrayList.size();
    }

}
