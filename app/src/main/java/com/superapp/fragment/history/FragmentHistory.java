package com.superapp.fragment.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.superapp.R;
import com.superapp.activity.base.ErrorType;
import com.superapp.activity.ActivityMain;
import com.superapp.fragment.base.BaseFragment;
import com.superapp.utils.PrefSetup;
import com.superapp.webservice.ResponsePacket;


public class FragmentHistory extends BaseFragment {

    private View fragmentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentView = inflater.inflate(R.layout.fragment_history, container, false);
        initView();
        return fragmentView;
    }


    @Override
    public void initView() {
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActivityMain) activity).setHeaderText(getString(R.string.history));
        ((ActivityMain) activity).changeHeaderButton(true);
        ((ActivityMain) activity).showHideFloatingButton(false, R.drawable.add);
        ((ActivityMain) activity).showHideNotificationButton(true);

        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
            ((ActivityMain) activity).showHIdeSearchButton(true);
        } else {
            ((ActivityMain) activity).showHIdeSearchButton(false);
        }

        ((ActivityMain) activity).showAd(false);
    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
    }


    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
//        if (responsePacket.getErrorCode() == 410) {
//            return;
//        }
//
//        if (Interactor.RequestCode_SendFeedBack == requestCode) {
//            activity.makeToast(responsePacket.getMessage());
//            activity.onBackPressed();
//        }
    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {
        super.onError(requestCode, errorType);
    }
}
