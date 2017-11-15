package com.superapp.fragment.communications.recomandations;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.ActivityMain;
import com.superapp.beans.RecommendationsCompanyBean;
import com.superapp.fragment.base.BaseFragment;

import java.util.ArrayList;


public class FragmentRecommendationsCompany extends BaseFragment {

    View fragmentView;
    private ArrayList<RecommendationsCompanyBean> beanArrayList;

    private static final Integer[] image = {R.drawable.send, R.drawable.send, R.drawable.send, R.drawable.send};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentView = inflater.inflate(R.layout.fragment_recommendationscompany, container, false);
        initView();
        return fragmentView;
    }

    private RecyclerView rv_recommendationList;
    private AdapterRecommendationsCompany adapterRecommendations;
    private TextView tv_recommendationAccept, tv_recommendationDecline, tv_recommendationNotConvinced;

    @Override
    public void initView() {

        tv_recommendationAccept = (TextView) fragmentView.findViewById(R.id.tv_recommendationAccept);
        tv_recommendationDecline = (TextView) fragmentView.findViewById(R.id.tv_recommendationDecline);
        tv_recommendationNotConvinced = (TextView) fragmentView.findViewById(R.id.tv_recommendationNotConvinced);

        tv_recommendationAccept.setPadding(20,15,20,15);
        tv_recommendationDecline.setPadding(20,15,20,15);
        tv_recommendationNotConvinced.setPadding(20,15,20,15);

        rv_recommendationList = (RecyclerView) fragmentView.findViewById(R.id.rv_recommendationList);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(ApplicationContext.getInstance(), LinearLayoutManager.HORIZONTAL, false);
        rv_recommendationList.setLayoutManager(layoutManager);

        beanArrayList = new ArrayList<RecommendationsCompanyBean>();
        for (int i = 0; i < image.length; i++) {
            RecommendationsCompanyBean recommendationsCompanyBean = new RecommendationsCompanyBean();
            recommendationsCompanyBean.setImgRecommendations(image[i]);
            beanArrayList.add(recommendationsCompanyBean);
        }
        adapterRecommendations = new AdapterRecommendationsCompany(activity, beanArrayList);
        rv_recommendationList.setAdapter(adapterRecommendations);
    }


    @Override
    public void onBackButtonClick(View view) {
        super.onBackButtonClick(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (activity instanceof ActivityMain) {
            ((ActivityMain) activity).setHeaderText("Communications");
            ((ActivityMain) activity).changeHeaderButton(true);
            ((ActivityMain) activity).showAd(false);
        }
        updateView();
    }

    @Override
    public void updateView() {
        super.updateView();
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
