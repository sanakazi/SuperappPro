package com.superapp.activity.registration;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.superapp.R;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.ErrorType;
import com.superapp.activity.ActivityMain;
import com.superapp.utils.PrefSetup;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;

/**
 * Created by inte on 10-Nov-16.
 */

public class ActivityOtpVerification extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_registration);
        initView();
    }

    private LinearLayout ll_submitOtp;
    private ImageView iv_back;
    private TextView tv_title, tv_submitOtp, tv_resendOtp, tv_mobile;
    private EditText et_otp;

    String otp;
    long userID;

    public void initView() {

        tv_title = (TextView) findViewById(R.id.tv_Title);
        tv_title.setText(getString(R.string.numberVerification));

        iv_back = (ImageView) setClick(R.id.iv_back);

        ll_submitOtp = (LinearLayout) findViewById(R.id.ll_submitOtp);

        tv_mobile = (TextView) findViewById(R.id.tv_mobile);
        Intent intent = getIntent();
        String tv_mobileNo = intent.getStringExtra("mobileNumber");
        userID = (intent.getLongExtra("userId", 0));
        otp = intent.getStringExtra("OTP");
        tv_mobile.setText(tv_mobileNo);

        tv_resendOtp = (TextView) findViewById(R.id.tv_resendOtp);
        setTouchNClick(R.id.tv_resendOtp);

        tv_submitOtp = (TextView) findViewById(R.id.tv_submitOtp);
        setTouchNClick(R.id.tv_submitOtp);

        et_otp = (EditText) findViewById(R.id.et_otp);

        if (userID == 0) {
            onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.tv_resendOtp:
                doResendOTPRegistration();
                break;

            case R.id.tv_submitOtp:
                if (validateOtp()) {
                    doConfirmRegistration();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        openRegisterScreen();
    }

    private void openRegisterScreen() {
        Intent intent1 = new Intent(ActivityOtpVerification.this, ActivityRegistration.class);
        startActivity(intent1);
    }

    public void navigateToHome() {
        //TODO open Home activity when login successfully or any other condition
        Intent intent = new Intent(ActivityOtpVerification.this, ActivityMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

    public boolean validateOtp() {
        if (TextUtils.isEmpty(et_otp.getText().toString().trim())) {
            et_otp.setError(getString(R.string.requiredOtp));
            et_otp.requestFocus();
            return false;
        }/* else if (!et_otp.getText().toString().trim().equalsIgnoreCase(otp)) {
            et_otp.setError(getString(R.string.wrongOtp));
            et_otp.requestFocus();
            return false;
        } */ else {
            et_otp.setError(null);
        }
        return true;
    }

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
        if (Interactor.RequestCode_ConfirmRegistration == requestCode) {
            if (responsePacket.getErrorCode() == 0) {
                makeToast(responsePacket.getMessage());
                PrefSetup.getInstance().setUserDetail(responsePacket.getUserDetail());
                navigateToHome();
            } else if (responsePacket.getErrorCode() != 0) {
                makeToast(responsePacket.getMessage());
            }
        } else if (Interactor.RequestCode_ResendOTPRegistration == requestCode) {
            if (responsePacket.getErrorCode() == 0) {
//                makeToast(responsePacket.getMessage());
            } else if (responsePacket.getErrorCode() != 0) {
                makeToast(responsePacket.getMessage());
            }
        }
    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {
        super.onError(requestCode, errorType);
    }

    private void doConfirmRegistration() {
        // Call web service to Register a user.
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userID);
            jsonObject.put("otp", et_otp.getText().toString().trim());
            jsonObject.put("loginType", PrefSetup.getInstance().getREGISTER_TYPE());
            jsonObject.put("deviceToken", "TOKEN");
            jsonObject.put("deviceType", "ANDROID");
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(ActivityOtpVerification.this, ActivityOtpVerification.this, Interactor.RequestCode_ConfirmRegistration, Interactor.Tag_ConfirmRegistration)
                .makeJsonPostRequest(Interactor.Method_ConfirmRegistration, jsonObject, true);
    }

    private void doResendOTPRegistration() {
        // Call web service to Register a user.
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userID);
            jsonObject.put("loginType", PrefSetup.getInstance().getREGISTER_TYPE());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(ActivityOtpVerification.this, ActivityOtpVerification.this, Interactor.RequestCode_ResendOTPRegistration, Interactor.Tag_ResendOTPRegistration)
                .makeJsonPostRequest(Interactor.Method_ResendOTPRegistration, jsonObject, true);
    }
}
