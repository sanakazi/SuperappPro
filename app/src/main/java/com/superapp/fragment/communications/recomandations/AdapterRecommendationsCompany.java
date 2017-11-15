package com.superapp.fragment.communications.recomandations;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.superapp.R;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.beans.RecommendationsCompanyBean;

import java.util.ArrayList;


public class AdapterRecommendationsCompany  extends RecyclerView.Adapter<AdapterRecommendationsCompany.ViewHolder> {

    BaseAppCompatActivity activity;
    ArrayList<RecommendationsCompanyBean> recommendationsArrayList;


    public AdapterRecommendationsCompany(BaseAppCompatActivity activity, ArrayList<RecommendationsCompanyBean> recommendationsArrayList) {
        this.activity = activity;
        this.recommendationsArrayList = recommendationsArrayList;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_recommendationItem;
        public ViewHolder(View itemView) {
            super(itemView);
            iv_recommendationItem = (ImageView)itemView.findViewById(R.id.iv_recommendationItem);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_recommendationcompanyitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // TODO user holder directly
        final RecommendationsCompanyBean recommendationsCompanyBean = recommendationsArrayList.get(position);
        holder.iv_recommendationItem.setImageResource(recommendationsCompanyBean.getImgRecommendations());

    }

    @Override
    public int getItemCount() {
        return recommendationsArrayList.size();
    }

}
