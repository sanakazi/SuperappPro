package com.superapp.fragment.client_detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.superapp.R;
import com.superapp.activity.base.ErrorType;
import com.superapp.activity.ActivityMain;
import com.superapp.beans.ClientBean;
import com.superapp.fragment.base.BaseFragment;
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
public class FragmentClientDetail extends BaseFragment {

    public View fragmentView;
    public String clientId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        fragmentView = inflater.inflate(R.layout.fragment_client_detail, container, false);
        try {
            clientId = getArguments().getString("ClientId");
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
        ((ActivityMain) activity).setHeaderText("Client detail");
        ((ActivityMain) activity).changeHeaderButton(true);
        ((ActivityMain) activity).showHideNotificationButton(true);
        ((ActivityMain) activity).showHideFloatingButton(false, 0);
        ((ActivityMain) activity).showAd(false);
    }

    TextView tv_alternateContactMobile, tv_alternateContact, tv_address, tv_mobileNo, tv_email, tv_gender,
            tv_name, tv_clientAddressType, tv_alternateContactText, tv_alternateContactMobileText, tv_alternateContactMobile2,
            tv_addressText, tv_clientAddressTypeText;

    LinearLayout ll_alternateContactMobile;


    @Override
    public void initView() {
        super.initView();

        tv_alternateContactMobile = (TextView) fragmentView.findViewById(R.id.tv_alternateContactMobile);
        tv_alternateContact = (TextView) fragmentView.findViewById(R.id.tv_alternateContact);
        tv_address = (TextView) fragmentView.findViewById(R.id.tv_address);
        tv_mobileNo = (TextView) fragmentView.findViewById(R.id.tv_mobileNo);
        tv_email = (TextView) fragmentView.findViewById(R.id.tv_email);
        tv_gender = (TextView) fragmentView.findViewById(R.id.tv_gender);
        tv_name = (TextView) fragmentView.findViewById(R.id.tv_name);
        tv_clientAddressType = (TextView) fragmentView.findViewById(R.id.tv_clientAddressType);
        tv_alternateContactMobileText = (TextView) fragmentView.findViewById(R.id.tv_alternateContactMobileText);
        tv_alternateContactText = (TextView) fragmentView.findViewById(R.id.tv_alternateContactText);
        tv_alternateContactMobile2 = (TextView) fragmentView.findViewById(R.id.tv_alternateContactMobile2);
        tv_addressText = (TextView) fragmentView.findViewById(R.id.tv_addressText);
        tv_clientAddressTypeText = (TextView) fragmentView.findViewById(R.id.tv_clientAddressTypeText);

        ll_alternateContactMobile = (LinearLayout) fragmentView.findViewById(R.id.ll_alternateContactMobile);

        getClientDetail();
    }

    private void updateUI(ClientBean detailBean) {
        tv_name.setText(detailBean.getName());

        if (detailBean.getGender().equalsIgnoreCase("m")) {
            tv_gender.setText("Male");
        } else {
            tv_gender.setText("Female");
        }
//        tv_gender.setText(detailBean.getGender());
        tv_email.setText(detailBean.getEmail());

        tv_mobileNo.setText(detailBean.getPhone());
        tv_mobileNo.setOnClickListener(v -> {
            Utilities.getInstance().doCall(activity, detailBean.getPhone());
        });

        if (!TextUtils.isEmpty(detailBean.getClientAddressType())) {
            tv_clientAddressType.setText(detailBean.getClientAddressType());
        } else {
            tv_clientAddressTypeText.setVisibility(View.GONE);
            tv_clientAddressType.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(detailBean.getAddress())) {
            tv_address.setText(ProjectUtils.getInstance().getFormattedAddress(detailBean.getAddress()).toString());
        } else {
            tv_addressText.setVisibility(View.GONE);
            tv_address.setVisibility(View.GONE);
        }


        if (!TextUtils.isEmpty(detailBean.getAlternetContact())) {
            tv_alternateContact.setText(detailBean.getAlternetContact());
        } else {
            tv_alternateContactText.setVisibility(View.GONE);
            tv_alternateContact.setVisibility(View.GONE);
        }


        if (!TextUtils.isEmpty(detailBean.getAlternateContactMobile())) {
            tv_alternateContactMobile.setText(detailBean.getAlternateContactMobile());
        } else {
            ll_alternateContactMobile.setVisibility(View.GONE);
            tv_alternateContactMobileText.setVisibility(View.GONE);
            tv_alternateContactMobile2.setVisibility(View.GONE);
            tv_alternateContactMobile.setVisibility(View.GONE);
        }


//        if ((tv_alternateContact.getText().toString().isEmpty())) {
//            tv_alternateContactText.setVisibility(View.GONE);
//        } else {
//            tv_alternateContactText.setText(activity.getString(R.string.alternateContactName));
//        }
//        tv_alternateContact.setText(detailBean.getAlternetContact());
//
//        if ((tv_alternateContactMobile.getText().toString().isEmpty())) {
//            tv_alternateContactMobile.setVisibility(View.GONE);
//            tv_alternateContactMobile2.setVisibility(View.GONE);
//        } else {
//            tv_alternateContactMobileText.setText(activity.getString(R.string.alternateContactMobile));
//        }
//
//        tv_alternateContactMobile.setText(detailBean.getAlternateContactMobile());
    }

    private void getClientDetail() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("clientId", clientId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(activity, FragmentClientDetail.this, Interactor.RequestCode_GetClientDetail, Interactor.Tag_GetClientDetail)
                .makeJsonPostRequest(Interactor.Method_GetClientDetail, jsonObject, true);
    }

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
        if (responsePacket.getErrorCode() == 410) {
            ((ActivityMain) getActivity()).logoutUser();
            return;
        }

        if (responsePacket.getErrorCode() == 0) {
            if (Interactor.RequestCode_GetClientDetail == requestCode) {
                if (responsePacket.getClientDetail() != null)
                    updateUI(responsePacket.getClientDetail());
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
