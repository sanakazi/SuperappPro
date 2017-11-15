package com.superapp.fragment.communications.recomandations;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.base.ErrorType;
import com.superapp.activity.ActivityMain;
import com.superapp.activity.upload.ActivityUpload;
import com.superapp.activity.upload.ActivityViewAttachment;
import com.superapp.beans.CommunicationBean;
import com.superapp.fragment.base.BaseFragment;
import com.superapp.utils.PrefSetup;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentRecommendations extends BaseFragment {

    public View fragmentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        fragmentView = inflater.inflate(R.layout.fragment_recommendations, container, false);
        initView();

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActivityMain) activity).setHeaderText("Client Suggestions");
        ((ActivityMain) activity).changeHeaderButton(true);
        ((ActivityMain) activity).showHideNotificationButton(true);
//        ((ActivityMain) activity).showHideFloatingButton(PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("c"), R.drawable.add);
        ((ActivityMain) activity).showHIdeSearchButton(PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d"));
        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("c") && ApplicationContext.getInstance().project.getIsComplete().equalsIgnoreCase("f")) {
            ((ActivityMain) activity).showHideFloatingButton(true, R.drawable.add);
        } else if (ApplicationContext.getInstance().project.getIsComplete().equalsIgnoreCase("t")) {
            ((ActivityMain) activity).showHideFloatingButton(false, R.drawable.add);
        }

        ((ActivityMain) activity).showAd(false);

        getAllRecommendations();
    }

    RecyclerView rv_recommendations;
    TextView tv_noDataFound;
    AdapterRecommendations adapterRecommendations;

    @Override
    public void initView() {
        super.initView();
        tv_noDataFound = (TextView) fragmentView.findViewById(R.id.tv_noDataFound);

        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
            tv_noDataFound.setText(activity.getString(R.string.designer_Recommendations_NoDataFound));
        } else {
            tv_noDataFound.setText(activity.getString(R.string.client_Recommendations_NoDataFound));
        }

        rv_recommendations = (RecyclerView) fragmentView.findViewById(R.id.rv_recommendations);
        GridLayoutManager manager = new GridLayoutManager(ApplicationContext.getInstance(), 1);
        rv_recommendations.setHasFixedSize(true);
        rv_recommendations.setLayoutManager(manager);
        rv_recommendations.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void updateView() {
        super.updateView();
        try {
            getAllRecommendations();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAllRecommendations() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("projectId", ApplicationContext.getInstance().project.getProjectId());
            jsonObject.put("type", "Recommendations");
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(activity, FragmentRecommendations.this, Interactor.RequestCode_GetAllCommunication, Interactor.Tag_GetAllCommunication)
                .makeJsonPostRequest(Interactor.Method_GetAllCommunication, jsonObject, true);
    }

    private void setAllRecommendations(ArrayList<CommunicationBean> communicationBean) {
        if (communicationBean.size() > 0) {
            tv_noDataFound.setVisibility(View.GONE);
            rv_recommendations.setVisibility(View.VISIBLE);
            adapterRecommendations = new AdapterRecommendations(activity, communicationBean, (object, position) -> {
                Intent uploadApproval = new Intent(activity, ActivityViewAttachment.class);
                uploadApproval.putExtra("isFeedBack", true);
                uploadApproval.putExtra("CommunicationBean", (CommunicationBean) object);
                uploadApproval.putExtra("communicationType", ActivityViewAttachment.COMMUNICATION_TYPE.RECOMMENDATION.getValue());
                activity.startActivity(uploadApproval);
            });
            rv_recommendations.setAdapter(adapterRecommendations);
        } else {
            tv_noDataFound.setVisibility(View.VISIBLE);
            rv_recommendations.setVisibility(View.GONE);
        }

    }

    @Override
    public void onFloatingButtonClick() {
        super.onFloatingButtonClick();
        Intent uploadApproval = new Intent(activity, ActivityUpload.class);
        uploadApproval.putExtra("communicationType", ActivityUpload.COMMUNICATION_TYPE.RECOMMENDATION.getValue());
        activity.startActivity(uploadApproval);
    }

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
        if (responsePacket.getErrorCode() == 410) {
            return;
        }

        if (responsePacket.getErrorCode() == 0) {
            if (Interactor.RequestCode_GetAllCommunication == requestCode) {
                if (responsePacket.getErrorCode() == 0) {
                    setAllRecommendations(responsePacket.getCommunicationList());
                    tv_noDataFound.setText(responsePacket.getMessage());
                }
            }
        } else {
            activity.makeToast(responsePacket.getMessage());
        }

    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {
        super.onError(requestCode, errorType);
    }

}
