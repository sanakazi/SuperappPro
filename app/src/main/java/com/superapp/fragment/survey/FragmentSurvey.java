package com.superapp.fragment.survey;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.ActivityMain;
import com.superapp.activity.ActivitySurvey;
import com.superapp.activity.base.ErrorType;
import com.superapp.activity.base.OnItemClickListener;
import com.superapp.beans.Survey;
import com.superapp.fragment.base.BaseFragment;
import com.superapp.utils.PrefSetup;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentSurvey extends BaseFragment {
    private View fragmentView;

    RecyclerView rv_surveyList;
    AdapterSurvey adapterSurvey;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentView = inflater.inflate(R.layout.fragment_survey, container, false);
        initView();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActivityMain) activity).setHeaderText("Survey List");
        ((ActivityMain) activity).changeHeaderButton(true);
        ((ActivityMain) activity).showHideFloatingButton(false, R.drawable.add);

        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
            ((ActivityMain) activity).showHIdeSearchButton(true);
        } else {
            ((ActivityMain) activity).showHIdeSearchButton(false);
        }
        ((ActivityMain) activity).showAd(false);
    }

    TextView tv_noDataFound;

    @Override
    public void initView() {

        tv_noDataFound = (TextView) fragmentView.findViewById(R.id.tv_noDataFound);
        rv_surveyList = (RecyclerView) fragmentView.findViewById(R.id.rv_surveyList);
        GridLayoutManager manager = new GridLayoutManager(ApplicationContext.getInstance(), 1);
        rv_surveyList.setHasFixedSize(true);
        rv_surveyList.setLayoutManager(manager);
        rv_surveyList.setItemAnimator(new DefaultItemAnimator());
        setAllSarvey();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
        }
    }

    public void setAllSarvey() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("membershipId", PrefSetup.getInstance().getUSER_MEMBERSHIPID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(activity, FragmentSurvey.this, Interactor.RequestCode_getSurveyList, Interactor.Tag_getSurveyList)
                .makeJsonPostRequest(Interactor.Method_getSurveyList, jsonObject, false);
    }

    private void setAllSurveysList(ArrayList<Survey> surveyList) {
        if (ApplicationContext.getInstance().getProjectList().size() > 0) {
            rv_surveyList.setVisibility(View.VISIBLE);
            tv_noDataFound.setVisibility(View.GONE);
            adapterSurvey = new AdapterSurvey(activity, surveyList, new OnItemClickListener() {
                @Override
                public void onItemClick(Object object, int position) {
                    Intent intent = new Intent(getContext(), ActivitySurvey.class);
                    intent.putExtra("survey", (Survey) object);
                    startActivityForResult(intent, 1050);
                }
            });
            rv_surveyList.setAdapter(adapterSurvey);
        } else {
            rv_surveyList.setVisibility(View.GONE);
            tv_noDataFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
//        if (Interactor.RequestCode_getSurveyList == requestCode) {
        if (Interactor.RequestCode_getSurveyList == requestCode) {
            if (responsePacket.getErrorCode() == 0) {
                if (responsePacket.getSurveyDetailList() != null) {
                    setAllSurveysList(responsePacket.getSurveyDetailList());
                } else {
                    tv_noDataFound.setVisibility(View.VISIBLE);
                    tv_noDataFound.setText(responsePacket.getMessage());
                }
            } else {
                tv_noDataFound.setVisibility(View.VISIBLE);
                tv_noDataFound.setText("Survey detail not found");
            }
        } else {
            Toast.makeText(getActivity(), responsePacket.getMessage(), Toast.LENGTH_SHORT).show();
        }
//        }
    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {
        super.onError(requestCode, errorType);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1050) {
            //TODO
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(FragmentSurvey.this).attach(FragmentSurvey.this).commit();

        }
    }
}
