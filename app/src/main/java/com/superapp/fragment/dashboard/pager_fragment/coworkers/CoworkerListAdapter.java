package com.superapp.fragment.dashboard.pager_fragment.coworkers;

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
import com.superapp.beans.CoworkerBean;
import com.superapp.swipe.SwipeLayout;
import com.superapp.swipe.adapter.RecyclerSwipeAdapter;
import com.superapp.utils.ProjectUtils;
import com.superapp.utils.Utilities;

import java.util.ArrayList;

public class CoworkerListAdapter extends RecyclerSwipeAdapter<CoworkerListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<CoworkerBean> coWorker;
    private boolean showCompleteDetail;
    private boolean hideArrow;
    private OnItemClickListener onItemClickListener;

    public CoworkerListAdapter(Context context, ArrayList<CoworkerBean> coWorker, boolean showCompleteDetail, boolean hideArrow, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.coWorker = coWorker;
        this.showCompleteDetail = showCompleteDetail;
        this.hideArrow = hideArrow;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coworker_list_row, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blank_footer, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < coWorker.size()) {
            return 1;
        } else if (position == coWorker.size()) {
            return 2;
        } else {
            return 0;
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (position < coWorker.size()) {
            final CoworkerBean coWorkers = coWorker.get(position);

            if (hideArrow) {
                viewHolder.imgShowOption.setVisibility(View.GONE);
            } else {
                viewHolder.imgShowOption.setVisibility(View.VISIBLE);
            }

            viewHolder.ll_item.setOnClickListener(v -> {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(coWorkers, 1);
            });

            viewHolder.tv_coWorkerName.setText(coWorkers.getName());

            viewHolder.tv_coWorkerOccupation.setText(coWorkers.getOccupation());

            if (!TextUtils.isEmpty(coWorkers.getAddress())) {
                viewHolder.tv_coWorkerAddress.setText(ProjectUtils.getInstance().getFormattedAddress(coWorkers.getAddress()).toString());
            } else {
                viewHolder.tv_coWorkerAddress.setText("No Address Available");
            }

            viewHolder.iv_coWorkerPhone.setOnTouchListener(BaseAppCompatActivity.imageTouch);
            viewHolder.iv_coWorkerPhone.setOnClickListener(v ->
                    Utilities.getInstance().doCall(mContext, coWorkers.getMobile()));

            viewHolder.iv_editCoWorker.setOnTouchListener(BaseAppCompatActivity.imageTouch);
            viewHolder.iv_editCoWorker.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(coWorkers, 0);
                }
            });

            viewHolder.iv_deleteCoWorker.setOnTouchListener(BaseAppCompatActivity.imageTouch);
            viewHolder.iv_deleteCoWorker.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(coWorkers, 2);
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
    public int getItemCount() {
//        return coWorker.size();
        return showCompleteDetail ? (coWorker.size() + 1) : coWorker.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.slCoworkerListRow;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        ImageView imgShowOption;
        TextView tv_coWorkerName, tv_coWorkerOccupation, tv_coWorkerAddress;
        ImageView iv_coWorkerPhone, iv_editCoWorker, iv_deleteCoWorker;
        LinearLayout ll_item;

        public ViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.slCoworkerListRow);
            imgShowOption = (ImageView) itemView.findViewById(R.id.imgShowOption);

            ll_item = (LinearLayout) itemView.findViewById(R.id.ll_item);
            tv_coWorkerName = (TextView) itemView.findViewById(R.id.tv_coWorkerName);
//            view = itemView.findViewById(R.id.view);
            tv_coWorkerOccupation = (TextView) itemView.findViewById(R.id.tv_coWorkerOccupation);
            tv_coWorkerAddress = (TextView) itemView.findViewById(R.id.tv_coWorkerAddress);

            iv_coWorkerPhone = (ImageView) itemView.findViewById(R.id.iv_coWorkerPhone);
            iv_editCoWorker = (ImageView) itemView.findViewById(R.id.iv_editCoWorker);
            iv_deleteCoWorker = (ImageView) itemView.findViewById(R.id.iv_deleteCoWorker);
        }
    }


}
