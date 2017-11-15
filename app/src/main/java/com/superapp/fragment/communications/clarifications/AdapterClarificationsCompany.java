package com.superapp.fragment.communications.clarifications;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.superapp.R;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.beans.ClarificationsCompanyBean;

import java.util.ArrayList;

public class AdapterClarificationsCompany extends RecyclerView.Adapter<AdapterClarificationsCompany.ViewHolder> {

    BaseAppCompatActivity activity;
    ArrayList<ClarificationsCompanyBean> clarificationsArrayList;


    public AdapterClarificationsCompany(BaseAppCompatActivity activity, ArrayList<ClarificationsCompanyBean> clarificationsArrayList) {
        this.activity = activity;
        this.clarificationsArrayList = clarificationsArrayList;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_clarificationItem;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_clarificationItem = (ImageView) itemView.findViewById(R.id.iv_clarificationItem);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_clarificationscompanyitems, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // TODO user holder directly
        final ClarificationsCompanyBean companyBean = clarificationsArrayList.get(position);
        holder.iv_clarificationItem.setImageResource(companyBean.getImageClarification());

    }

    @Override
    public int getItemCount() {
        return clarificationsArrayList.size();
    }

}
