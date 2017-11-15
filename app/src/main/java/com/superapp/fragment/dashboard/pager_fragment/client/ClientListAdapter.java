package com.superapp.fragment.dashboard.pager_fragment.client;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.superapp.R;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.OnItemClickListener;
import com.superapp.beans.ClientBean;
import com.superapp.swipe.SwipeLayout;
import com.superapp.swipe.adapter.RecyclerSwipeAdapter;
import com.superapp.utils.ProjectUtils;
import com.superapp.utils.Utilities;

import java.util.ArrayList;

public class ClientListAdapter extends RecyclerSwipeAdapter<ClientListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<ClientBean> clients;
    private boolean showCompleteDetail;
    private OnItemClickListener onItemClickListener;

    public ClientListAdapter(Context context, ArrayList<ClientBean> clients, boolean showCompleteDetail, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.clients = clients;
        this.showCompleteDetail = showCompleteDetail;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_list_row, parent, false);
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
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (position < clients.size()) {
            final ClientBean client = clients.get(position);

            viewHolder.ll_nameAddress.setOnClickListener(v -> {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(client, 1);
            });

            viewHolder.tv_clientName.setText(client.getName());
            if (!TextUtils.isEmpty(client.getAddress())) {
                viewHolder.tv_clientAddress.setText(ProjectUtils.getInstance().getFormattedAddress(client.getAddress()).toString());
            } else {
                viewHolder.tv_clientAddress.setText("No Address Available");
            }


            viewHolder.iv_clientPhone.setOnTouchListener(BaseAppCompatActivity.imageTouch);
            viewHolder.iv_clientPhone.setOnClickListener(v -> Utilities.getInstance().doCall(mContext, client.getPhone()));

            viewHolder.iv_editClient.setOnTouchListener(BaseAppCompatActivity.imageTouch);
            viewHolder.iv_editClient.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(client, 0);
                }
            });

            viewHolder.iv_deleteClient.setOnTouchListener(BaseAppCompatActivity.imageTouch);
            viewHolder.iv_deleteClient.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(client, 2);
                }
            });


            //----------------------Swipe Handling----------------
            viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
            viewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onStartOpen(SwipeLayout layout) {
                    ((ImageView) layout.findViewById(R.id.imgShowOption)).setImageResource(R.drawable.right_arrow_black);
                }

                @Override
                public void onOpen(SwipeLayout layout) {

                }

                @Override
                public void onStartClose(SwipeLayout layout) {
                    ((ImageView) layout.findViewById(R.id.imgShowOption)).setImageResource(R.drawable.left_arrow_black);
                }

                @Override
                public void onClose(SwipeLayout layout) {

                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

                }
            });
            viewHolder.imgShowOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (viewHolder.swipeLayout.getOpenStatus() == SwipeLayout.Status.Open) {
                        viewHolder.swipeLayout.close(true);
                    } else {
                        viewHolder.swipeLayout.open(true);
                    }
                }
            });

            mItemManger.bind(viewHolder.itemView, position);
        }
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.slClientListRow;
    }

    @Override
    public int getItemCount() {
//        return clients.size();
        return showCompleteDetail ? (clients.size() + 1) : clients.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        ImageView imgShowOption;
        TextView tv_clientName, tv_clientAddress;
        ImageView iv_clientPhone, iv_editClient, iv_deleteClient;
        LinearLayout ll_nameAddress, ll_callEditDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.slClientListRow);
            imgShowOption = (ImageView) itemView.findViewById(R.id.imgShowOption);

            ll_nameAddress = (LinearLayout) itemView.findViewById(R.id.ll_nameAddress);
            tv_clientName = (TextView) itemView.findViewById(R.id.tv_clientName);
            tv_clientAddress = (TextView) itemView.findViewById(R.id.tv_clientAddress);
            ll_callEditDelete = (LinearLayout) itemView.findViewById(R.id.ll_callEditDelete);
            iv_clientPhone = (ImageView) itemView.findViewById(R.id.iv_clientPhone);
            iv_editClient = (ImageView) itemView.findViewById(R.id.iv_editClient);
            iv_deleteClient = (ImageView) itemView.findViewById(R.id.iv_deleteClient);
        }
    }


}
