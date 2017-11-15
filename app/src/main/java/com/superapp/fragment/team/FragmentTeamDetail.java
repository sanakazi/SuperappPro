package com.superapp.fragment.team;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.base.ErrorType;
import com.superapp.activity.ActivityMain;
import com.superapp.beans.CoworkerBean;
import com.superapp.fragment.base.BaseFragment;
import com.superapp.utils.PrefSetup;
import com.superapp.utils.ProjectUtils;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;


/**
 * Created by APPNWEB on 03-09-2016.
 */
public class FragmentTeamDetail extends BaseFragment {

    public View fragmentView;
    public String coworkerId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        fragmentView = inflater.inflate(R.layout.fragment_coworker_detail, container, false);
        try {
            coworkerId = getArguments().getString("CoworkerId");
        } catch (Exception e) {
            e.printStackTrace();
            activity.onBackPressed();
            return fragmentView;
        }
        initView();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActivityMain) activity).setHeaderText("Coworker detail");
        ((ActivityMain) activity).changeHeaderButton(true);
        ((ActivityMain) activity).showHideNotificationButton(true);
        ((ActivityMain) activity).showHideFloatingButton(false, 0);
    }

    ProgressBar progressBar;
    ImageView iv_Photo;
    TextView tv_rating, tv_email, tv_mobileNo, tv_address, tv_categoryTitle, tv_name;

    @Override
    public void initView() {
        super.initView();

        progressBar = (ProgressBar) fragmentView.findViewById(R.id.progressBar);
        iv_Photo = (ImageView) fragmentView.findViewById(R.id.iv_Photo);
        tv_rating = (TextView) fragmentView.findViewById(R.id.tv_rating);
        tv_email = (TextView) fragmentView.findViewById(R.id.tv_email);
        tv_mobileNo = (TextView) fragmentView.findViewById(R.id.tv_mobileNo);
        tv_address = (TextView) fragmentView.findViewById(R.id.tv_address);
        tv_categoryTitle = (TextView) fragmentView.findViewById(R.id.tv_categoryTitle);
        tv_name = (TextView) fragmentView.findViewById(R.id.tv_name);

        getCoworkerDetail();
    }

    private void updateUI(CoworkerBean detailBean) {

        tv_name.setText(detailBean.getName());
        tv_categoryTitle.setText(detailBean.getOccupation());
        tv_address.setText(ProjectUtils.getInstance().getFormattedAddress(detailBean.getAddress()).toString());
        tv_mobileNo.setText(detailBean.getMobile());
        tv_email.setText(detailBean.getEmail());
        tv_rating.setText("Rating : " + detailBean.getRating());

        ApplicationContext.getInstance().loadImage(detailBean.getPhotoId(), iv_Photo, progressBar, R.drawable.no_image);
    }

    private void getCoworkerDetail() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("coworkerId", coworkerId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(activity, FragmentTeamDetail.this, Interactor.RequestCode_GetCoworkerDetail, Interactor.Tag_GetCoworkerDetail)
                .makeJsonPostRequest(Interactor.Method_GetCoworkerDetail, jsonObject, true);
    }

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
        if (responsePacket.getErrorCode() == 410) {
            ((ActivityMain) getActivity()).logoutUser();
            return;
        }

        if (responsePacket.getErrorCode() == 0) {
            if (Interactor.RequestCode_GetCoworkerDetail == requestCode) {
                updateUI(responsePacket.getCoworkerDetail());
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
