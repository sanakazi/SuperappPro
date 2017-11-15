package com.superapp.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.superapp.R;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.ErrorType;
import com.superapp.activity.base.INavigation;
import com.superapp.activity.registration.ActivityRegistration;
import com.superapp.utils.PrefSetup;
import com.superapp.utils.Utilities;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;

public class ActivityLogin extends BaseAppCompatActivity implements INavigation {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        PrefSetup.getInstance().setFcmId(refreshedToken);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private EditText et_userName, et_password;
    private TextView tv_title, tv_logIn_login;
    private ImageView iv_back;

    @Override
    public void initView() {
        // TODO this method is use to initialise total view components.
        et_userName = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        setClick(R.id.tv_registration);
        setClick(R.id.tv_forgot_password);


        tv_logIn_login = (TextView) setTouchNClick(R.id.tv_logIn);
        tv_title = (TextView) findViewById(R.id.tv_Title);
        tv_title.setText("LOG IN");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_logIn:
                if (validateFields()) {
                    doLogin();
                }
                break;
            case R.id.tv_registration:
                showRegisterSelectTypePopup(this);
                break;
            case R.id.tv_forgot_password:
                navigateToForgotPassword();
                break;
        }
    }

    private boolean validateFields() {
        if (TextUtils.isEmpty(et_userName.getText().toString().trim())) {
            et_userName.setError(getString(R.string.requiredMobileNo));
            return false;
        } else {
            et_userName.setError(null);
        }
        if (et_userName.getText().toString().length() < 10) {
            et_userName.setError(getString(R.string.invalidMobileNo));
            return false;
        } else {
            et_userName.setError(null);
        }

        if (TextUtils.isEmpty(et_password.getText().toString().trim())) {
            et_password.setError(getString(R.string.requiredPassword));
            return false;
        } else {
            et_password.setError(null);
        }
        return true;
    }

    private void showRegisterSelectTypePopup(final Context context) {

        final Dialog selectUserType = new Dialog(context);
        selectUserType.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_register_type, null, false);
        selectUserType.setContentView(view);

        final Window window = selectUserType.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        selectUserType.show();

        final CheckBox checkbox_designer_registration = (CheckBox) view.findViewById(R.id.checkbox_designer_registration);
        final CheckBox checkbox_client_registration = (CheckBox) view.findViewById(R.id.checkbox_client_registration);
        final CheckBox checkbox_coWorker_registration = (CheckBox) view.findViewById(R.id.checkbox_coWorker_registration);

        checkbox_designer_registration.setOnClickListener(v -> {
            checkbox_designer_registration.setChecked(true);
            checkbox_client_registration.setChecked(false);
            checkbox_coWorker_registration.setChecked(false);
        });

        checkbox_client_registration.setOnClickListener(v -> {
            checkbox_designer_registration.setChecked(false);
            checkbox_client_registration.setChecked(true);
            checkbox_coWorker_registration.setChecked(false);
        });

        checkbox_coWorker_registration.setOnClickListener(v -> {
            checkbox_designer_registration.setChecked(false);
            checkbox_client_registration.setChecked(false);
            checkbox_coWorker_registration.setChecked(true);
        });


        ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(v -> selectUserType.dismiss());


        Button btn_popUpRegisterDone = (Button) view.findViewById(R.id.btn_popUpRegisterDone);

        btn_popUpRegisterDone.setOnClickListener(v -> {
            if (checkbox_designer_registration.isChecked()) {
                selectUserType.dismiss();
                PrefSetup.getInstance().setREGISTER_TYPE("d");
                navigateToRegister();

            } else if (checkbox_client_registration.isChecked()) {
                selectUserType.dismiss();
                PrefSetup.getInstance().setREGISTER_TYPE("c");
                navigateToRegister();

            } else if (checkbox_coWorker_registration.isChecked()) {
                selectUserType.dismiss();
                PrefSetup.getInstance().setREGISTER_TYPE("w");
                navigateToRegister();
            } else {
                Toast.makeText(ActivityLogin.this, "Please select register type first.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doLogin() {
        // Call web service to login user.
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", et_userName.getText().toString().trim());
            jsonObject.put("password", et_password.getText().toString().trim());
            jsonObject.put("deviceId", Utilities.getDeviceUDID());
            jsonObject.put("deviceToken", PrefSetup.getInstance().getFcmId());
            jsonObject.put("deviceType", "ANDROID");
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(ActivityLogin.this, ActivityLogin.this, Interactor.RequestCode_Login, Interactor.Tag_Login)
                .makeJsonPostRequest(Interactor.Method_Login, jsonObject, true);
    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {
        super.onError(requestCode, errorType);
        if (requestCode == Interactor.RequestCode_Login) {

        }
    }

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
        if (Interactor.RequestCode_Login == requestCode) {
            if (responsePacket.getErrorCode() == 0 && responsePacket.getUserDetail() != null) {
                // Execute the task which is required on success.

                PrefSetup.getInstance().setUserDetail(responsePacket.getUserDetail());
                navigateToHome();
            } else {
                // TODO do task on error response.
                makeToast(responsePacket.getMessage());
            }
        }
    }

    @Override
    public void navigateToHome() {
        //TODO open Home activity when login successfully or any other condition

        Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void navigateToRegister() {
        // TODO open Register Activity for registration

        Intent intent = new Intent(ActivityLogin.this, ActivityRegistration.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void navigateToForgotPassword() {
        Intent intent1 = new Intent(ActivityLogin.this, ActivityForgotPassword.class);
        startActivity(intent1);
    }

    @Override
    public void showProgressing() {
        super.showProgressingView();
    }

    @Override
    public void hideProgressing() {
        super.hideProgressingView();
    }

    @Override
    public void updateView() {
        // TODO for future Use.
    }
}
