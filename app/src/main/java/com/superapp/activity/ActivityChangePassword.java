package com.superapp.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.superapp.R;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.ErrorType;
import com.superapp.utils.PrefSetup;
import com.superapp.utils.Utilities;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.OnResponseListener;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;


public class ActivityChangePassword extends BaseAppCompatActivity {

    EditText et_confirmPassword, et_newPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_change_password);
        initView();
    }

    Dialog dialog;

    public void initView() {
        dialog = new Dialog(ActivityChangePassword.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = (LayoutInflater) ActivityChangePassword.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_change_password, null, false);
        dialog.setContentView(view);

        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setOnDismissListener((dialog) -> ActivityChangePassword.this.finish());

        et_confirmPassword = (EditText) view.findViewById(R.id.et_confirmPassword);
        et_newPassword = (EditText) view.findViewById(R.id.et_newPassword);

        et_newPassword.requestFocus();
        Utilities.getInstance().showKeyboard(ActivityChangePassword.this);
        et_newPassword.requestFocus();

        view.findViewById(R.id.bt_update).setOnClickListener(this);
        view.findViewById(R.id.iv_close).setOnClickListener(this);
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.bt_update:
                if (isValidForm()) {
                    changePassword();
                }
                break;

            case R.id.iv_close:
                onBackPressed();
                break;
        }
    }

    public void changePassword() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("password", et_newPassword.getText().toString());
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());

        } catch (Exception e) {
            e.printStackTrace();
        }

        new InteractorImpl(ActivityChangePassword.this, new OnResponseListener() {
            @Override
            public void onSuccess(int requestCode, ResponsePacket responsePacket) {
                if (responsePacket.getErrorCode() == 0) {
                    ActivityChangePassword.this.makeToast(responsePacket.getMessage());
                    dialog.dismiss();
                }
            }

            @Override
            public void onError(int requestCode, ErrorType errorType) {

            }
        }, Interactor.RequestCode_ChangePassword, Interactor.Tag_ChangePassword)
                .makeJsonPostRequest(Interactor.Method_ChangePassword, jsonObject, true);
    }

    public boolean isValidForm() {

        if (et_newPassword.getText().toString().trim().equals("")) {
            et_newPassword.setError("This field is required");
            et_newPassword.requestFocus();
            return false;
        } else {
            et_newPassword.setError(null);
        }

        if (et_confirmPassword.getText().toString().trim().equals("")) {
            et_confirmPassword.setError("This field is required");
            et_confirmPassword.requestFocus();
            return false;
        } else {
            et_confirmPassword.setError(null);
        }

        if (!et_confirmPassword.getText().toString().trim().equals(et_newPassword.getText().toString().trim())) {
            et_confirmPassword.setError(getString(R.string.noMatchPassword));
            et_confirmPassword.requestFocus();
            return false;
        } else {
            et_confirmPassword.setError(null);
        }
        return true;
    }

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
    }
}
