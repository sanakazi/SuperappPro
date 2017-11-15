package com.superapp.fragment.dashboard.pager_fragment.client;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.OnItemClickListener;
import com.superapp.beans.ClientBean;
import com.superapp.utils.ProjectUtils;
import com.superapp.utils.Utilities;

import java.util.ArrayList;

public class AdapterClient extends RecyclerView.Adapter<AdapterClient.ViewHolder> {

    private ApplicationContext activity;
    private ArrayList<ClientBean> clients;
    private boolean showCompleteDetail;
    private OnItemClickListener onItemClickListener;

    public AdapterClient(ApplicationContext activity, ArrayList<ClientBean> clients, boolean showCompleteDetail, OnItemClickListener onItemClickListener) {
        this.activity = activity;
        this.clients = clients;
        this.showCompleteDetail = showCompleteDetail;
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_clientName, tv_clientAddress;
        ImageView iv_clientPhone, iv_editClient, iv_deleteClient;
        LinearLayout ll_nameAddressLeftArrow, ll_callEditDeleteRightArrow, ll_nameAddress, ll_callEditDelete, ll_leftArrow, ll_rightArrow;

        public ViewHolder(View itemView) {
            super(itemView);
            ll_nameAddressLeftArrow = (LinearLayout) itemView.findViewById(R.id.ll_nameAddressLeftArrow);
            ll_nameAddress = (LinearLayout) itemView.findViewById(R.id.ll_nameAddress);
            ll_leftArrow = (LinearLayout) itemView.findViewById(R.id.ll_leftArrow);
            tv_clientName = (TextView) itemView.findViewById(R.id.tv_clientName);
            tv_clientAddress = (TextView) itemView.findViewById(R.id.tv_clientAddress);

            ll_callEditDeleteRightArrow = (LinearLayout) itemView.findViewById(R.id.ll_callEditDeleteRightArrow);
            ll_callEditDelete = (LinearLayout) itemView.findViewById(R.id.ll_callEditDelete);
            ll_rightArrow = (LinearLayout) itemView.findViewById(R.id.ll_rightArrow);
            iv_clientPhone = (ImageView) itemView.findViewById(R.id.iv_clientPhone);
            iv_editClient = (ImageView) itemView.findViewById(R.id.iv_editClient);
            iv_deleteClient = (ImageView) itemView.findViewById(R.id.iv_deleteClient);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_dashboard_client_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blank_footer, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < clients.size()) {
            return 1;
        } else if (position == clients.size()) {
            return 2;
        } else {
            return 0;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // TODO user holder directly
        try {
            if (position < clients.size()) {
                final ClientBean client = clients.get(position);

                holder.ll_nameAddress.setOnClickListener(v -> {
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(client, 1);
                });

                holder.ll_leftArrow.setOnClickListener(v -> {
                    holder.ll_nameAddressLeftArrow.setVisibility(View.GONE);
                    holder.ll_callEditDeleteRightArrow.setVisibility(View.VISIBLE);
                });

                holder.ll_rightArrow.setOnClickListener(v -> {
                    holder.ll_nameAddressLeftArrow.setVisibility(View.VISIBLE);
                    holder.ll_callEditDeleteRightArrow.setVisibility(View.GONE);
                });

                holder.tv_clientName.setText(client.getName());

                if (!TextUtils.isEmpty(client.getAddress())) {
                    holder.tv_clientAddress.setText(ProjectUtils.getInstance().getFormattedAddress(client.getAddress()).toString());
                } else {
                    holder.tv_clientAddress.setText("No Address Available");
                }

                holder.iv_clientPhone.setOnTouchListener(BaseAppCompatActivity.imageTouch);
                holder.iv_clientPhone.setOnClickListener(v ->
                        Utilities.getInstance().doCall(activity, client.getPhone()));

//            if (showCompleteDetail) {
//                holder.tv_clientAddress.setVisibility(View.VISIBLE);
//                holder.iv_clientPhone.setVisibility(View.VISIBLE);
//                holder.tv_clientAddress.setText(ProjectUtils.getInstance().getFormattedAddress(client.getAddress()).toString());
//                holder.iv_clientPhone.setOnTouchListener(BaseAppCompatActivity.imageTouch);
//                holder.iv_clientPhone.setOnClickListener(v -> Utilities.getInstance().doCall(activity, client.getPhone()));
//


                holder.iv_editClient.setOnTouchListener(BaseAppCompatActivity.imageTouch);
                holder.iv_editClient.setOnClickListener(v -> {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(client, 0);
                    }
                });

                holder.iv_deleteClient.setOnTouchListener(BaseAppCompatActivity.imageTouch);
                holder.iv_deleteClient.setOnClickListener(v -> {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(client, 2);
                    }
                });

//            } else {
//                holder.ll_nameAddress.setVisibility(View.GONE);
//            }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return showCompleteDetail ? (clients.size() + 1) : clients.size();

    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
