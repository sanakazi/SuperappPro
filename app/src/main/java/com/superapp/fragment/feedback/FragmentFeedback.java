package com.superapp.fragment.feedback;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.superapp.R;
import com.superapp.activity.base.ErrorType;
import com.superapp.activity.ActivityMain;
import com.superapp.fragment.base.BaseFragment;
import com.superapp.utils.PrefSetup;
import com.superapp.utils.Utilities;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;


public class FragmentFeedback extends BaseFragment {

    private View fragmentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentView = inflater.inflate(R.layout.fragment_feedback, container, false);
        initView();
        return fragmentView;
    }

    private EditText et_feedBack;

    @Override
    public void initView() {
        et_feedBack = (EditText) fragmentView.findViewById(R.id.et_feedBack);

        et_feedBack.requestFocus();
        Utilities.getInstance().showKeyboard(activity);
        et_feedBack.requestFocus();
        setTouchNClick(R.id.btn_feedbackSubmit, fragmentView);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActivityMain) activity).setHeaderText(getString(R.string.feedback));
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
        switch (arg0.getId()) {
            case R.id.btn_feedbackSubmit:
                if (TextUtils.isEmpty(et_feedBack.getText().toString())) {
                    et_feedBack.setError(getString(R.string.pleaseAddFeedBack));
                    et_feedBack.requestFocus();
                    return;
                }
                submitFeedBack();
                break;
        }
    }

    private void submitFeedBack() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("feedback", et_feedBack.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(activity, FragmentFeedback.this, Interactor.RequestCode_SendFeedBack, Interactor.Tag_SendFeedBack)
                .makeJsonPostRequest(Interactor.Method_SendFeedBack, jsonObject, true);
    }

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
        if (responsePacket.getErrorCode() == 410) {
            return;
        }

        if (Interactor.RequestCode_SendFeedBack == requestCode) {
            activity.makeToast(responsePacket.getMessage());
            activity.onBackPressed();
        }
    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {
        super.onError(requestCode, errorType);
    }
}
