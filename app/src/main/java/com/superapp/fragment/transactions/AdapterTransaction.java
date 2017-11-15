package com.superapp.fragment.transactions;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.OnItemClickListener;
import com.superapp.beans.BeanTransaction;
import com.superapp.utils.PopupUtils;
import com.superapp.utils.PrefSetup;
import com.superapp.utils.ProjectUtils;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by inte on 9/20/2016.
 */
public class AdapterTransaction extends RecyclerView.Adapter<AdapterTransaction.ViewHolder> {

    private BaseAppCompatActivity activity;
    private ArrayList<BeanTransaction> transactions;
    String loginType = null;
    OnItemClickListener onItemClickListener;
    boolean isRequest;

    public AdapterTransaction(BaseAppCompatActivity activity, ArrayList<BeanTransaction> transactions, boolean isRequest, OnItemClickListener onItemClickListener) {
        this.activity = activity;
        this.transactions = transactions;
        this.onItemClickListener = onItemClickListener;
        this.isRequest = isRequest;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_projectName, tv_description, tv_requestMoney, tv_status, tv_dateTime;
        TextView tv_feedBack;
        LinearLayout ll_feedback, ll_category, ll_transactionItems;
        TextView tv_category, tv_subCategory;
        TextView tv_moneyText;
        ImageView iv_previewImage;
        ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            loginType = PrefSetup.getInstance().getUserLoginType();
            tv_projectName = (TextView) itemView.findViewById(R.id.tv_projectName);
            tv_description = (TextView) itemView.findViewById(R.id.tv_description);
            tv_requestMoney = (TextView) itemView.findViewById(R.id.tv_requestMoney);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            tv_dateTime = (TextView) itemView.findViewById(R.id.tv_dateTime);
            tv_feedBack = (TextView) itemView.findViewById(R.id.tv_feedBack);
            ll_feedback = (LinearLayout) itemView.findViewById(R.id.ll_feedback);
            ll_category = (LinearLayout) itemView.findViewById(R.id.ll_category);
            ll_transactionItems = (LinearLayout) itemView.findViewById(R.id.ll_transactionItems);

            tv_category = (TextView) itemView.findViewById(R.id.tv_category);
            tv_subCategory = (TextView) itemView.findViewById(R.id.tv_subCategory);
            tv_moneyText = (TextView) itemView.findViewById(R.id.tv_moneyText);

            iv_previewImage = (ImageView) itemView.findViewById(R.id.iv_previewImage);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_transaction_items, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blank_footer, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < transactions.size()) {
            return 1;
        } else if (position == transactions.size()) {
            return 2;
        } else {
            return 0;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (position < transactions.size()) {
            final BeanTransaction beanTransaction = transactions.get(position);
            holder.tv_description.setText(activity.getString(R.string.description) + " : " + beanTransaction.getDescription());
            holder.tv_requestMoney.setText(beanTransaction.getRequestAmount());
            holder.tv_status.setText(beanTransaction.getStatus());

            Calendar addedDate = Calendar.getInstance();
            addedDate.setTimeInMillis(beanTransaction.getAddedDate() * 1000);
            holder.tv_dateTime.setText(" : " + ProjectUtils.getInstance().getFormattedDate(addedDate));

            if (isRequest)
                holder.tv_moneyText.setText(activity.getString(R.string.requestedMoney) + " : ");
            else
                holder.tv_moneyText.setText(activity.getString(R.string.spentMoney) + " : ");

            if (isRequest) {
                holder.progressBar.setVisibility(View.GONE);
                holder.ll_category.setVisibility(View.GONE);
                holder.tv_status.setVisibility(View.VISIBLE);
                holder.iv_previewImage.setVisibility(View.GONE);
                if (beanTransaction.getStatus().equalsIgnoreCase("Request")) {
                    holder.tv_status.setTextColor(activity.getResources().getColor(R.color.colorYellow));
                } else if (beanTransaction.getStatus().equalsIgnoreCase("Declined")) {
                    holder.tv_status.setTextColor(activity.getResources().getColor(R.color.colorOrange));
                } else if (beanTransaction.getStatus().equalsIgnoreCase("Deposited")) {
                    holder.tv_status.setTextColor(activity.getResources().getColor(R.color.green));
                } else if (beanTransaction.getStatus().equalsIgnoreCase("Pending")) {
                    holder.tv_status.setTextColor(activity.getResources().getColor(R.color.colorRed));
                } else if (beanTransaction.getStatus().equalsIgnoreCase("Accepted")) {
                    holder.tv_status.setTextColor(activity.getResources().getColor(R.color.green));
                } else {
                    holder.tv_status.setTextColor(activity.getResources().getColor(R.color.black));
                }
            } else {
                holder.tv_status.setVisibility(View.GONE);
                holder.ll_category.setVisibility(View.VISIBLE);
                holder.tv_category.setText(beanTransaction.getCategoryName());
                holder.tv_subCategory.setText(" : " + beanTransaction.getSubCategoryName());

                holder.iv_previewImage.setVisibility(View.GONE);
                if (!beanTransaction.getAttachment().isEmpty()) {
                    holder.iv_previewImage.setVisibility(View.VISIBLE);
                    ApplicationContext.getInstance().loadImage(beanTransaction.getAttachment(), holder.iv_previewImage, holder.progressBar, 0);

                    holder.iv_previewImage.setOnClickListener(v -> {
                        PopupUtils.getInstance().showImageDialog(activity, beanTransaction.getAttachment());
                    });
                } else {
                    holder.progressBar.setVisibility(View.GONE);
                    holder.iv_previewImage.setVisibility(View.GONE);
                }
            }

            holder.ll_feedback.setVisibility(View.GONE);
            if (isRequest && beanTransaction.getStatus().equalsIgnoreCase("Request")) {
                if (loginType.equalsIgnoreCase("c")) {
                    holder.ll_feedback.setVisibility(View.VISIBLE);
                    holder.ll_feedback.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onItemClickListener != null) {
                                onItemClickListener.onItemClick(beanTransaction, position);
                            }
                        }
                    });
                }
            }
        } else if (position == transactions.size()) {

        }
    }

    @Override
    public int getItemCount() {
        return transactions.size() + 1;
    }


}





