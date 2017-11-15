package com.superapp.fragment.communications.clarifications;

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
import com.superapp.fragment.communications.recomandations.AdapterRecommendations;
import com.superapp.utils.PrefSetup;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentClarifications extends BaseFragment {

    public View fragmentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        fragmentView = inflater.inflate(R.layout.fragment_clarifications, container, false);
        initView();

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActivityMain) activity).setHeaderText("Clarifications");
        ((ActivityMain) activity).changeHeaderButton(true);
        ((ActivityMain) activity).showHideNotificationButton(true);
//        ((ActivityMain) activity).showHideFloatingButton(PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("c"), R.drawable.add);
//        ((ActivityMain) activity).showHideFloatingButton(true, R.drawable.add);
//        ((ActivityMain) activity).showHIdeSearchButton(PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("c") );
        if (ApplicationContext.getInstance().project.getIsComplete().equalsIgnoreCase("f") && PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("c")) {
            ((ActivityMain) activity).showHideFloatingButton(true, R.drawable.add);
        } else if (ApplicationContext.getInstance().project.getIsComplete().equalsIgnoreCase("t")) {
            ((ActivityMain) activity).showHideFloatingButton(false, R.drawable.add);
        }
        ((ActivityMain) activity).showAd(false);

        getAllClarifications();
    }

    RecyclerView rv_clarifications;
    TextView tv_noDataFound;
    AdapterRecommendations adapterClarifications;

    @Override
    public void initView() {
        super.initView();
        tv_noDataFound = (TextView) fragmentView.findViewById(R.id.tv_noDataFound);

        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
            tv_noDataFound.setText(activity.getString(R.string.designer_Clarifications_NoDataFound));
        } else {
            tv_noDataFound.setText(activity.getString(R.string.client_Clarifications_NoDataFound));
        }

        rv_clarifications = (RecyclerView) fragmentView.findViewById(R.id.rv_clarifications);
        GridLayoutManager manager = new GridLayoutManager(ApplicationContext.getInstance(), 1);
        rv_clarifications.setHasFixedSize(true);
        rv_clarifications.setLayoutManager(manager);
        rv_clarifications.setItemAnimator(new DefaultItemAnimator());
    }


    @Override
    public void updateView() {
        super.updateView();
        try {
            getAllClarifications();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAllClarifications() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("projectId", ApplicationContext.getInstance().project.getProjectId());
            jsonObject.put("type", "Clarification");
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(activity, FragmentClarifications.this, Interactor.RequestCode_GetAllCommunication, Interactor.Tag_GetAllCommunication)
                .makeJsonPostRequest(Interactor.Method_GetAllCommunication, jsonObject, true);
    }

    private void setAllClarifications(ArrayList<CommunicationBean> clarificationsBean) {
        if (clarificationsBean.size() > 0) {
            tv_noDataFound.setVisibility(View.GONE);
            rv_clarifications.setVisibility(View.VISIBLE);
            adapterClarifications = new AdapterRecommendations(activity, clarificationsBean, (object, position) -> {
                Intent uploadApproval = new Intent(activity, ActivityViewAttachment.class);
                uploadApproval.putExtra("isFeedBack", true);
                uploadApproval.putExtra("CommunicationBean", (CommunicationBean) object);
                uploadApproval.putExtra("communicationType", ActivityViewAttachment.COMMUNICATION_TYPE.CLARIFICATION.getValue());
                activity.startActivity(uploadApproval);
            });
            rv_clarifications.setAdapter(adapterClarifications);
        } else {
            rv_clarifications.setVisibility(View.GONE);
            tv_noDataFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFloatingButtonClick() {
        super.onFloatingButtonClick();

        Intent uploadApproval = new Intent(activity, ActivityUpload.class);
        uploadApproval.putExtra("communicationType", ActivityUpload.COMMUNICATION_TYPE.CLARIFICATION.getValue());
        activity.startActivity(uploadApproval);
    }

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
        if (responsePacket.getErrorCode() == 410) {
            ((ActivityMain) getActivity()).logoutUser();
            return;
        }

        if (responsePacket.getErrorCode() == 0) {
            if (Interactor.RequestCode_GetAllCommunication == requestCode) {
                if (responsePacket.getErrorCode() == 0) {
                    setAllClarifications(responsePacket.getCommunicationList());
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

