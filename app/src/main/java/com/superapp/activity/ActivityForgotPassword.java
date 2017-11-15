package com.superapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.superapp.R;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.ErrorType;
import com.superapp.activity.base.ViewMain;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;

public class ActivityForgotPassword extends BaseAppCompatActivity implements ViewMain {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgot_password);
        initView();
    }

    private ImageView iv_back;
    private EditText et_username, et_otp, et_rePassword, et_password, et_userName_forgot_password;
    private TextInputLayout inputLayout;
    private TextView tv_title, tv_resendOtp, tv_email, tv_mobileUpdatePassword;
    private LinearLayout ll_forgotPassword, ll_submitOtp, ll_updatePassword;

    @Override
    public void initView() {
        et_username = (EditText) findViewById(R.id.et_userName_forgot_password);
        setTouchNClick(R.id.tv_submitForgotPassword);
        tv_title = (TextView) findViewById(R.id.tv_Title);
        iv_back = (ImageView) setTouchNClick(R.id.iv_back);
        tv_title.setText(getString(R.string.forgetPassword));

        ll_forgotPassword = (LinearLayout) findViewById(R.id.ll_forgotPassword);
        ll_submitOtp = (LinearLayout) findViewById(R.id.ll_submitOtp);
        ll_updatePassword = (LinearLayout) findViewById(R.id.ll_updatePassword);

        tv_mobileUpdatePassword = (TextView) findViewById(R.id.tv_mobileUpdatePassword);
        setTouchNClick(R.id.tv_updatePassword);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_resendOtp = (TextView) setTouchNClick(R.id.tv_resendOtp);
        setTouchNClick(R.id.tv_submitOtp);

        et_password = (EditText) findViewById(R.id.et_password);
        et_rePassword = (EditText) findViewById(R.id.et_rePassword);
        et_otp = (EditText) findViewById(R.id.et_otp);
        et_userName_forgot_password = (EditText) findViewById(R.id.et_userName_forgot_password);

        forgotPassword();
    }

    public void forgotPassword() {
        ll_updatePassword.setVisibility(View.GONE);
        ll_submitOtp.setVisibility(View.GONE);
        ll_forgotPassword.setVisibility(View.VISIBLE);
    }

    public void submitOtp() {
        tv_email.setText(et_username.getText().toString().trim());
        ll_updatePassword.setVisibility(View.GONE);
        ll_submitOtp.setVisibility(View.VISIBLE);
        ll_forgotPassword.setVisibility(View.GONE);
    }

    public void updatePassword() {
        ll_updatePassword.setVisibility(View.VISIBLE);
        ll_submitOtp.setVisibility(View.GONE);
        ll_forgotPassword.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_resendOtp:
                if (validateFields()) {
                    doResendOTPForgot();
                }
                break;
            case R.id.tv_submitForgotPassword:
                if (validateFields()) {
                    doForgotPassword();
                }
                break;
            case R.id.iv_back:
                Intent intent1 = new Intent(ActivityForgotPassword.this, ActivityLogin.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                break;
            case R.id.tv_submitOtp:
                if (validateOtp()) {
                    if (otp == Long.parseLong(et_otp.getText().toString().trim())) {
                        updatePassword();
                        tv_mobileUpdatePassword.setText(et_username.getText().toString().trim());
                    } else {
                        et_otp.setError(getString(R.string.invalidOtp));
                        et_otp.requestFocus();
                    }
                }
                break;
            case R.id.tv_updatePassword:
                if (validation()) {
                    doUpdatePassword();
                }
                break;
        }
    }

    public void doForgotPassword() {
        JSONObject object = new JSONObject();
        try {
            object.put("username", et_username.getText().toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(ActivityForgotPassword.this, ActivityForgotPassword.this, Interactor.RequestCode_ForgotPassword, Interactor.Tag_ForgotPassword).
                makeJsonPostRequest(Interactor.Method_ForgotPassword, object, true);
    }

    public void doResendOTPForgot() {
        JSONObject object = new JSONObject();
        try {
            object.put("username", et_username.getText().toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(ActivityForgotPassword.this, ActivityForgotPassword.this, Interactor.RequestCode_ResendOTPForgot, Interactor.Tag_ResendOTPForgot).
                makeJsonPostRequest(Interactor.Method_ResendOTPForgot, object, true);
    }


    public void doUpdatePassword() {
        JSONObject object = new JSONObject();
        try {
            object.put("username", et_username.getText().toString().trim());
            object.put("password", et_password.getText().toString().trim());
            object.put("rePassword", et_rePassword.getText().toString().trim());

        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(ActivityForgotPassword.this, ActivityForgotPassword.this, Interactor.RequestCode_UpdatePassword, Interactor.Tag_UpdatePassword).
                makeJsonPostRequest(Interactor.Method_UpdatePassword, object, true);
    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {
        super.onError(requestCode, errorType);
    }

    long otp;

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
        if (responsePacket.getErrorCode() == 0) {
            if (Interactor.RequestCode_ForgotPassword == requestCode) {
                otp = responsePacket.getOtp();
                submitOtp();
                makeToast(responsePacket.getMessage());
            } else if (Interactor.RequestCode_UpdatePassword == requestCode) {
                makeToast(responsePacket.getMessage());
                Intent intent = new Intent(ActivityForgotPassword.this, ActivityLogin.class);
                startActivity(intent);
                finish();
            }
        } else {
            if (responsePacket.getErrorCode() != 410)
                makeToast(responsePacket.getMessage());
        }
    }

    private boolean validateFields() {

        if (TextUtils.isEmpty(et_username.getText().toString().trim())) {
            et_username.setError(getString(R.string.requiredMobileNo));
            et_username.requestFocus();
            return false;
        } else {
            et_username.setError(null);
        }

        if (et_username.getText().toString().length() < 10) {
            et_username.setError(getString(R.string.invalidMobileNo));
            et_username.requestFocus();
            return false;
        } else {
            et_username.setError(null);
        }

        if (TextUtils.isEmpty(et_username.getText().toString().trim())) {
            et_username.setError(getString(R.string.invalidMobileNo));
            et_username.requestFocus();
            return false;
        } else {
            et_username.setError(null);
        }
        return true;
    }

    private boolean validation() {

        if (TextUtils.isEmpty(et_password.getText().toString())) {
            et_password.setError(getString(R.string.requiredPassword));
            et_password.requestFocus();
            return false;
        } else {
            et_password.setError(null);
        }

        if (TextUtils.isEmpty(et_rePassword.getText().toString())) {
            et_rePassword.setError(getString(R.string.requiredRePassword));
            et_rePassword.requestFocus();
            return false;
        } else {
            et_rePassword.setError(null);
        }

        if (!et_rePassword.getText().toString().trim().equals(et_password.getText().toString().trim())) {
            et_rePassword.setError(getString(R.string.noMatchPassword));
            et_rePassword.requestFocus();
            return false;
        } else {
            et_rePassword.setError(null);
        }
        return true;
    }

    public boolean validateOtp() {
        if (TextUtils.isEmpty(et_otp.getText().toString().trim())) {
            et_otp.setError(getString(R.string.requiredOtp));
            et_otp.requestFocus();
            return false;
        } else {
            et_otp.setError(null);
        }
        return true;
    }

    @Override
    public void showProgressing() {

    }

    @Override
    public void hideProgressing() {

    }


    @Override
    public void updateView() {

    }
}