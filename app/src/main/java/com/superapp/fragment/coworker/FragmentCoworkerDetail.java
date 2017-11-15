package com.superapp.fragment.coworker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.superapp.utils.PopupUtils;
import com.superapp.utils.PrefSetup;
import com.superapp.utils.ProjectUtils;
import com.superapp.utils.Utilities;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;

/**
 * Created by APPNWEB on 03-09-2016.
 */
public class FragmentCoworkerDetail extends BaseFragment {

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
        ((ActivityMain) activity).setHeaderText("Co-worker detail");
        ((ActivityMain) activity).changeHeaderButton(true);
        ((ActivityMain) activity).showHideNotificationButton(true);
        ((ActivityMain) activity).showHideFloatingButton(false, 0);
        ((ActivityMain) activity).showAd(false);
    }

    ProgressBar progressBarPhoto, progressBarID;
    ImageView iv_Photo, iv_ID;
    TextView tv_rating, tv_email, tv_mobileNo, tv_address, tv_categoryTitle, tv_name, tv_nativeAddress, tv_referredBy, tv_panNo, tv_emailText, tv_panNoText, tv_referredByText, tv_nativeAddressText;

    @Override
    public void initView() {
        super.initView();

        progressBarPhoto = (ProgressBar) fragmentView.findViewById(R.id.progressBarPhoto);
        progressBarID = (ProgressBar) fragmentView.findViewById(R.id.progressBarID);
        iv_Photo = (ImageView) fragmentView.findViewById(R.id.iv_Photo);
        iv_ID = (ImageView) fragmentView.findViewById(R.id.iv_ID);

        tv_rating = (TextView) fragmentView.findViewById(R.id.tv_rating);
        tv_email = (TextView) fragmentView.findViewById(R.id.tv_email);
        tv_mobileNo = (TextView) fragmentView.findViewById(R.id.tv_mobileNo);
        tv_address = (TextView) fragmentView.findViewById(R.id.tv_address);
        tv_categoryTitle = (TextView) fragmentView.findViewById(R.id.tv_categoryTitle);
        tv_name = (TextView) fragmentView.findViewById(R.id.tv_name);
        tv_panNo = (TextView) fragmentView.findViewById(R.id.tv_panNo);
        tv_referredBy = (TextView) fragmentView.findViewById(R.id.tv_referredBy);
        tv_nativeAddressText = (TextView) fragmentView.findViewById(R.id.tv_nativeAddressText);
        tv_nativeAddress = (TextView) fragmentView.findViewById(R.id.tv_nativeAddress);

        tv_emailText = (TextView) fragmentView.findViewById(R.id.tv_emailText);
        tv_panNoText = (TextView) fragmentView.findViewById(R.id.tv_panNoText);
        tv_referredByText = (TextView) fragmentView.findViewById(R.id.tv_referredByText);

        getCoworkerDetail();
    }

    private void updateUI(CoworkerBean detailBean) {
        if (detailBean != null) {
            tv_name.setText(detailBean.getName());
            tv_categoryTitle.setText(detailBean.getOccupation());
            tv_address.setText(ProjectUtils.getInstance().getFormattedAddress(detailBean.getAddress()).toString());
            tv_nativeAddress.setText(detailBean.getNativeAddress());
            if (TextUtils.isEmpty(detailBean.getNativeAddress())) {
                tv_nativeAddress.setVisibility(View.GONE);
                tv_nativeAddress.setVisibility(View.GONE);
                tv_nativeAddressText.setVisibility(View.GONE);

            } else {
                tv_nativeAddress.setVisibility(View.VISIBLE);
                tv_nativeAddress.setVisibility(View.VISIBLE);
                tv_nativeAddressText.setVisibility(View.VISIBLE);
//                tv_nativeAddress.setText(detailBean.getNativeAddress());
                tv_nativeAddress.setText(ProjectUtils.getInstance().getFormattedAddress(detailBean.getNativeAddress()).toString());
            }

            tv_mobileNo.setText(detailBean.getMobile());
            tv_mobileNo.setOnClickListener(v -> {
                Utilities.getInstance().doCall(activity, detailBean.getMobile());
            });

            tv_email.setText(detailBean.getEmail());

            if (TextUtils.isEmpty(detailBean.getEmail())) {
                tv_emailText.setVisibility(View.GONE);
                tv_email.setVisibility(View.GONE);
            } else {
                tv_emailText.setVisibility(View.VISIBLE);
                tv_email.setVisibility(View.VISIBLE);
                tv_email.setText(detailBean.getEmail());
            }

            tv_rating.setText(Float.parseFloat(detailBean.getRating()) + "");

//            tv_panNo.setText(detailBean.getPanNumber());
            if (TextUtils.isEmpty(detailBean.getPanNumber())) {
                tv_panNoText.setVisibility(View.GONE);
                tv_panNo.setVisibility(View.GONE);
            } else {
                tv_panNoText.setVisibility(View.VISIBLE);
                tv_panNo.setVisibility(View.VISIBLE);
                tv_panNo.setText(detailBean.getPanNumber());
            }

//            tv_referredBy.setText(detailBean.getReferredBy());
            if (TextUtils.isEmpty(detailBean.getReferredBy())) {
                tv_referredByText.setVisibility(View.GONE);
                tv_referredBy.setVisibility(View.GONE);
            } else {
                tv_referredByText.setVisibility(View.VISIBLE);
                tv_referredBy.setVisibility(View.VISIBLE);
                tv_referredBy.setText(detailBean.getReferredBy());
            }

            ApplicationContext.getInstance().loadImage(detailBean.getPhotoId(), iv_Photo, progressBarPhoto, R.drawable.no_image);
            ApplicationContext.getInstance().loadImage(detailBean.getIdCard(), iv_ID, progressBarID, R.drawable.no_image);

            iv_Photo.setOnClickListener(v ->
                    PopupUtils.getInstance().showImageDialog(activity, detailBean.getPhotoId()));

            iv_ID.setOnClickListener(v ->
                    PopupUtils.getInstance().showImageDialog(activity, detailBean.getIdCard()));
        }
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
        new InteractorImpl(activity, FragmentCoworkerDetail.this, Interactor.RequestCode_GetCoworkerDetail, Interactor.Tag_GetCoworkerDetail)
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
