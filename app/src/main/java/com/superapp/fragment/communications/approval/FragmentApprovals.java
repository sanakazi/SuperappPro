package com.superapp.fragment.communications.approval;

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
import com.superapp.activity.base.OnItemClickListener;
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

public class FragmentApprovals extends BaseFragment {

    public View fragmentView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        fragmentView = inflater.inflate(R.layout.fragment_approvals, container, false);
        initView();

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActivityMain) activity).setHeaderText("Approvals");
        ((ActivityMain) activity).changeHeaderButton(true);
        ((ActivityMain) activity).showHideNotificationButton(true);
        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
            ((ActivityMain) activity).showHideFloatingButton(true, R.drawable.add);
        } else {
            ((ActivityMain) activity).showHIdeSearchButton(false);
        }
        if (ApplicationContext.getInstance().project.getIsComplete().equalsIgnoreCase("f") && PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
            ((ActivityMain) activity).showHideFloatingButton(true, R.drawable.add);
        } else if (ApplicationContext.getInstance().project.getIsComplete().equalsIgnoreCase("t")) {
            ((ActivityMain) activity).showHideFloatingButton(false, R.drawable.add);
        }

        ((ActivityMain) activity).showAd(false);

        getAllApprovals();
    }


    @Override
    public void updateView() {
        super.updateView();
        try {
            getAllApprovals();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    RecyclerView rv_approvals;
    AdapterApprovals adapterApprovals;
    TextView tv_noDataFound;

    @Override
    public void initView() {
        super.initView();
        tv_noDataFound = (TextView) fragmentView.findViewById(R.id.tv_noDataFound);

        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
            tv_noDataFound.setText(activity.getString(R.string.designer_Approval_NoDataFound));
        } else {
            tv_noDataFound.setText(activity.getString(R.string.client_Approval_NoDataFound));
        }

        rv_approvals = (RecyclerView) fragmentView.findViewById(R.id.rv_approvals);
        GridLayoutManager manager = new GridLayoutManager(ApplicationContext.getInstance(), 1);
        rv_approvals.setHasFixedSize(true);
        rv_approvals.setLayoutManager(manager);
        rv_approvals.setItemAnimator(new DefaultItemAnimator());
        getAllApprovals();
    }

    @Override
    public void onFloatingButtonClick() {
        super.onFloatingButtonClick();
        Intent uploadApproval = new Intent(activity, ActivityUpload.class);
        uploadApproval.putExtra("communicationType", ActivityUpload.COMMUNICATION_TYPE.APPROVAL.getValue());
        activity.startActivity(uploadApproval);
    }

    private void getAllApprovals() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("projectId", ApplicationContext.getInstance().project.getProjectId());
            jsonObject.put("type", "Approval");
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(activity, FragmentApprovals.this, Interactor.RequestCode_GetAllCommunication, Interactor.Tag_GetAllCommunication)
                .makeJsonPostRequest(Interactor.Method_GetAllCommunication, jsonObject, true);
    }

    private void setAllApprovals(ArrayList<CommunicationBean> communicationBean) {
        if (communicationBean.size() > 0) {
            tv_noDataFound.setVisibility(View.GONE);
            rv_approvals.setVisibility(View.VISIBLE);
            adapterApprovals = new AdapterApprovals(activity, communicationBean, new OnItemClickListener() {
                @Override
                public void onItemClick(Object object, int position) {
                    /*
                        position == 0 // used for button click
                        position == 1 // used for item click
                     */
                    Intent intent = null;
                    if (position == 0) {
                        intent = new Intent(activity, ActivityUpload.class);
                        intent.putExtra("isFeedBack", true);
                    } else {
                        intent = new Intent(activity, ActivityViewAttachment.class);
                    }
                    intent.putExtra("communicationType", ActivityUpload.COMMUNICATION_TYPE.APPROVAL.getValue());
                    intent.putExtra("CommunicationBean", (CommunicationBean) object);
                    activity.startActivity(intent);
                }
            });
            rv_approvals.setAdapter(adapterApprovals);
        } else {
            tv_noDataFound.setVisibility(View.VISIBLE);
            rv_approvals.setVisibility(View.GONE);
        }

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
                    setAllApprovals(responsePacket.getCommunicationList());
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
