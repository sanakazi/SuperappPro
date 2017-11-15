package com.superapp.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.superapp.R;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.utils.PrefSetup;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;

public class ActivityChangeMNumber extends BaseAppCompatActivity {

    LinearLayout ll_submitMobileNumber;
    TextView tv_title, tv_oldMobileNumber;
    EditText et_newMobileNumber, et_otp;

    Button btn_verify, btn_submit, btn_resendOtp;

    ImageView iv_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    Dialog dialog;

    public void initView() {

        dialog = new Dialog(ActivityChangeMNumber.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = (LayoutInflater) ActivityChangeMNumber.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_cahnge_mobile_number, null, false);
        dialog.setContentView(view);

        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setOnDismissListener((dialog) -> ActivityChangeMNumber.this.finish());

        ll_submitMobileNumber = (LinearLayout) view.findViewById(R.id.ll_submitMobileNumber);

        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.changeMobileNumber));

        tv_oldMobileNumber = (TextView) view.findViewById(R.id.tv_oldMobileNumber);
        tv_oldMobileNumber.setText(PrefSetup.getInstance().getUserContactNo());

        et_newMobileNumber = (EditText) view.findViewById(R.id.et_newMobileNumber);
        et_otp = (EditText) view.findViewById(R.id.et_otp);

        btn_verify = (Button) view.findViewById(R.id.btn_verify);
        btn_verify.setOnClickListener(this);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        btn_resendOtp = (Button) view.findViewById(R.id.btn_resendOtp);
        btn_resendOtp.setOnClickListener(this);

        iv_close = (ImageView) view.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);

        showSendOtpView();

        dialog.show();
    }

    private void showSendOtpView() {
        btn_verify.setVisibility(View.VISIBLE);
        ll_submitMobileNumber.setVisibility(View.GONE);
        et_newMobileNumber.setClickable(true);
        et_newMobileNumber.setFocusable(true);
        et_newMobileNumber.setFocusableInTouchMode(true);
    }

    private void showVerifyOtpView() {
        btn_verify.setVisibility(View.GONE);
        ll_submitMobileNumber.setVisibility(View.VISIBLE);
        et_newMobileNumber.setClickable(false);
        et_newMobileNumber.setFocusable(false);
        et_newMobileNumber.setFocusableInTouchMode(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.iv_close:
                dialog.dismiss();
                break;

            case R.id.btn_resendOtp:
            case R.id.btn_verify:
                changeNumber();
                break;

            case R.id.btn_submit:
                updateMobile();
                break;
        }
    }

    private void changeNumber() {
        if (TextUtils.isEmpty(et_newMobileNumber.getText().toString())) {
            et_newMobileNumber.setError("This field is required");
            et_newMobileNumber.requestFocus();
            return;
        } else {
            et_newMobileNumber.setError(null);
        }

        if (et_newMobileNumber.getText().toString().length() < 10) {
            et_newMobileNumber.setError(getString(R.string.invalidMobileNo));
            et_newMobileNumber.requestFocus();
            return;
        } else {
            et_newMobileNumber.setError(null);
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("newNumber", et_newMobileNumber.getText().toString());
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(this, this, Interactor.RequestCode_changeNumber, Interactor.Tag_changeNumber)
                .makeJsonPostRequest(Interactor.Method_changeNumber, jsonObject, true);
    }

    private void updateMobile() {
        if (TextUtils.isEmpty(et_otp.getText().toString())) {
            et_otp.setError("This field is required");
            et_otp.requestFocus();
            return;
        } else {
            et_otp.setError(null);
        }
        if (otp == 0 || !et_otp.getText().toString().equalsIgnoreCase(otp + "")) {
            et_otp.setError("Invalid OTP");
            et_otp.requestFocus();
            return;
        } else {
            et_otp.setError(null);
        }


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("newNumber", et_newMobileNumber.getText().toString());
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(this, this, Interactor.RequestCode_updateMobile, Interactor.Tag_updateMobile)
                .makeJsonPostRequest(Interactor.Method_updateMobile, jsonObject, true);
    }

    long otp = 0;

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
        if (requestCode == Interactor.RequestCode_changeNumber) {
            if (responsePacket.getErrorCode() == 0) {
                otp = responsePacket.getOtp();
                showVerifyOtpView();
            } else {
                Toast.makeText(ActivityChangeMNumber.this, responsePacket.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == Interactor.RequestCode_updateMobile) {
            if (responsePacket.getErrorCode() == 0) {
                PrefSetup.getInstance().setUserContactNo(et_newMobileNumber.getText().toString());
                dialog.dismiss();
            }
            Toast.makeText(ActivityChangeMNumber.this, responsePacket.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
