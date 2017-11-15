package com.superapp.fragment.communications.clarifications;

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
import com.superapp.beans.ClarificationsCompanyBean;
import com.superapp.fragment.base.BaseFragment;

import java.util.ArrayList;


public class FragmentClarificationsCompany extends BaseFragment {

    View fragmentView;
    private ArrayList<ClarificationsCompanyBean> beanArrayList;

    private static final Integer[] image = {R.drawable.send, R.drawable.send, R.drawable.send, R.drawable.send};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentView = inflater.inflate(R.layout.fragment_clarificationscompany, container, false);
        initView();
        return fragmentView;
    }

    private RecyclerView rv_clarificationList;
    private AdapterClarificationsCompany adapterClarifications;
    private TextView tv_clarificationAccept, tv_clarificationDecline, tv_clarificationNotConvinced;

    @Override
    public void initView() {


        tv_clarificationAccept = (TextView) fragmentView.findViewById(R.id.tv_clarificationAccept);
        tv_clarificationDecline = (TextView) fragmentView.findViewById(R.id.tv_clarificationDecline);
        tv_clarificationNotConvinced = (TextView) fragmentView.findViewById(R.id.tv_clarificationNotConvinced);

        tv_clarificationAccept.setPadding(20,15,20,15);
        tv_clarificationDecline.setPadding(20,15,20,15);
        tv_clarificationNotConvinced.setPadding(20,15,20,15);

        rv_clarificationList = (RecyclerView) fragmentView.findViewById(R.id.rv_clarificationList);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(ApplicationContext.getInstance(), LinearLayoutManager.HORIZONTAL, false);
        rv_clarificationList.setLayoutManager(layoutManager);

        beanArrayList = new ArrayList<ClarificationsCompanyBean>();
        for (int i = 0; i < image.length; i++) {
            ClarificationsCompanyBean companyBean = new ClarificationsCompanyBean();
            companyBean.setImageClarification(image[i]);
            beanArrayList.add(companyBean);
        }
        adapterClarifications = new AdapterClarificationsCompany(activity, beanArrayList);
        rv_clarificationList.setAdapter(adapterClarifications);
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

    private void validateFields(){

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
