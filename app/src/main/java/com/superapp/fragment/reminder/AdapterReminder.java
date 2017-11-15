package com.superapp.fragment.reminder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.superapp.R;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.OnItemClickListener;
import com.superapp.beans.ReminderBean;
import com.superapp.utils.ProjectUtils;

import java.util.ArrayList;
import java.util.Calendar;

public class AdapterReminder extends RecyclerView.Adapter<AdapterReminder.ViewHolder> {

    private BaseAppCompatActivity activity;
    private ArrayList<ReminderBean> reminderArrayList;
    private OnItemClickListener onItemClickListener;

    public AdapterReminder(BaseAppCompatActivity activity, ArrayList<ReminderBean> reminderArrayList, OnItemClickListener onItemClickListener) {
        this.activity = activity;
        this.reminderArrayList = reminderArrayList;
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_reminderText, tv_reminderDate;
        LinearLayout ll_reminderItem;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_reminderText = (TextView) itemView.findViewById(R.id.tv_reminderText);
            tv_reminderDate = (TextView) itemView.findViewById(R.id.tv_reminderDate);
            ll_reminderItem = (LinearLayout) itemView.findViewById(R.id.ll_reminderItem);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_reminder_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // TODO user holder directly
        if (position < reminderArrayList.size()) {
            ReminderBean reminderBean = reminderArrayList.get(position);
            Calendar dateTime = Calendar.getInstance();
            dateTime.setTimeInMillis(reminderBean.getEndDate() * 1000);
            holder.tv_reminderText.setText(reminderBean.getMessage());
            holder.tv_reminderDate.setText(ProjectUtils.getInstance().getFormattedDate(dateTime));

            holder.ll_reminderItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(reminderBean, position);

                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return reminderArrayList.size();
    }

}
