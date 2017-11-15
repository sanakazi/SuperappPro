package com.superapp.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.superapp.R;
import com.superapp.activity.ActivityMain;
import com.superapp.activity.base.ErrorType;
import com.superapp.beans.BeanAddClient;
import com.superapp.beans.BeanAddress;
import com.superapp.beans.ClientBean;
import com.superapp.fragment.base.BaseFragment;
import com.superapp.utils.PopupUtils;
import com.superapp.utils.PrefSetup;
import com.superapp.utils.ProjectUtils;
import com.superapp.utils.Utilities;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;

public class FragEditProfileClient extends BaseFragment {
    public View fragmentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentView = inflater.inflate(R.layout.fragment_add_client, container, false);
        initView();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (activity instanceof ActivityMain) {
            ((ActivityMain) activity).setHeaderText(activity.getString(R.string.updateClient));
            ((ActivityMain) activity).showHideFloatingButton(false, R.drawable.add);
            ((ActivityMain) activity).changeHeaderButton(true);
            ((ActivityMain) activity).showAd(false);
        }
    }

    ClientBean client;
    BeanAddress clientAddress;

    private TextView tv_addClient;
    private EditText et_clientFullName, et_clientGender, et_clientEmail, et_clientMobile,
            et_clientAddress, et_clientAlternateContact, et_clientAlternateContactsMobile, et_clientAddressType;
    private TextInputLayout textInputLayoutClientFullName, textInputLayoutClientGender, textInputLayoutClientEmail,
            textInputLayoutClientMobile, textInputLayoutClientAddress, textInputLayoutClientAlternateContact,
            textInputLayoutClientAlternateContactsMobile, textInputLayoutClientAddressType;
    private ImageView iv_addClient;
    private LinearLayout ll_addClient;


    public void initView() {
        tv_addClient = (TextView) fragmentView.findViewById(R.id.tv_addClient);
        iv_addClient = (ImageView) setTouchNClick(R.id.iv_addClient, fragmentView);
        setClick(R.id.ll_addClient, fragmentView);
        et_clientFullName = (EditText) fragmentView.findViewById(R.id.et_clientFullName);
        et_clientGender = (EditText) setTouchNClick(R.id.et_clientGender, fragmentView);
        et_clientEmail = (EditText) fragmentView.findViewById(R.id.et_clientEmail);
        et_clientMobile = (EditText) fragmentView.findViewById(R.id.et_clientMobile);
        et_clientAddress = (EditText) setTouchNClick(R.id.et_clientAddress, fragmentView);
        et_clientAddressType = (EditText) setTouchNClick(R.id.et_clientAddressType, fragmentView);
        et_clientAlternateContact = (EditText) fragmentView.findViewById(R.id.et_clientAlternateContact);
        et_clientAlternateContactsMobile = (EditText) fragmentView.findViewById(R.id.et_clientAlternateContactsMobile);
        textInputLayoutClientFullName = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutClientFullName);
        textInputLayoutClientGender = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutClientGender);
        textInputLayoutClientEmail = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutClientEmail);
        textInputLayoutClientMobile = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutClientMobile);
        textInputLayoutClientAddress = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutClientAddress);
        textInputLayoutClientAlternateContact = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutClientAlternateContact);
        textInputLayoutClientAlternateContactsMobile = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutClientAlternateContactsMobile);
        textInputLayoutClientAddressType = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutClientAddressType);

        ll_addClient = (LinearLayout) fragmentView.findViewById(R.id.ll_addClient);

        getClientDetail();
    }

    private boolean validateFields() {
        if (TextUtils.isEmpty(et_clientFullName.getText().toString())) {
            textInputLayoutClientFullName.setErrorEnabled(true);
            textInputLayoutClientFullName.setError(getString(R.string.fullNameRequired));
            et_clientFullName.requestFocus();
            return false;
        } else {
            textInputLayoutClientFullName.setErrorEnabled(false);
            textInputLayoutClientFullName.setError(null);
        }
        if (TextUtils.isEmpty(et_clientGender.getText().toString())) {
            textInputLayoutClientGender.setErrorEnabled(true);
            textInputLayoutClientGender.setError(getString(R.string.selectGender));
            et_clientGender.requestFocus();
            return false;
        } else {
            textInputLayoutClientGender.setErrorEnabled(false);
            textInputLayoutClientGender.setError(null);
        }

        if (TextUtils.isEmpty(et_clientEmail.getText().toString())) {
            textInputLayoutClientEmail.setErrorEnabled(true);
            textInputLayoutClientEmail.setError(getString(R.string.emailRequired));
            et_clientEmail.requestFocus();
            return false;
        } else {
            textInputLayoutClientEmail.setErrorEnabled(false);
            textInputLayoutClientEmail.setError(null);
        }

        if (!Utilities.getInstance().isValidEmail(et_clientEmail.getText().toString())) {
            textInputLayoutClientEmail.setErrorEnabled(true);
            textInputLayoutClientEmail.setError(getString(R.string.invalidEmail));
            et_clientEmail.requestFocus();
            return false;
        } else {
            textInputLayoutClientEmail.setErrorEnabled(false);
            textInputLayoutClientEmail.setError(null);
        }
        if (TextUtils.isEmpty(et_clientMobile.getText().toString())) {
            textInputLayoutClientMobile.setErrorEnabled(true);
            textInputLayoutClientMobile.setError(getString(R.string.mobileNumberRequired));
            et_clientMobile.requestFocus();
            return false;
        } else {
            textInputLayoutClientMobile.setErrorEnabled(false);
            textInputLayoutClientMobile.setError(null);
        }

//        if (TextUtils.isEmpty(et_clientAddressType.getText().toString())) {
//            textInputLayoutClientAddressType.setErrorEnabled(true);
//            textInputLayoutClientAddressType.setError(getString(R.string.addressTypeRequired));
//            et_clientAddressType.requestFocus();
//            return false;
//        } else {
//            textInputLayoutClientAddressType.setErrorEnabled(false);
//            textInputLayoutClientAddressType.setError(null);
//        }

//        if (TextUtils.isEmpty(et_clientAddress.getText().toString())) {
//            textInputLayoutClientAddress.setErrorEnabled(true);
//            textInputLayoutClientAddress.setError(getString(R.string.addressRequired));
//            et_clientAddress.requestFocus();
//            return false;
//        } else {
//            textInputLayoutClientAddress.setErrorEnabled(false);
//            textInputLayoutClientAddress.setError(null);
//        }
        return true;
    }


    private TextView tv_genderMale, tv_genderFemale;

    private void showClientGenderPopUp(final Context activity) {
        final Dialog tncDialog = new Dialog(activity);
        tncDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragmentaddclient_genderpopup, null, false);
        tncDialog.setContentView(view);
        final Window window = tncDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tncDialog.show();

        final ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);

        iv_close.setOnClickListener(v -> tncDialog.dismiss());

        tv_genderMale = (TextView) view.findViewById(R.id.tv_genderMale);
        tv_genderMale.setOnClickListener(v -> {
            et_clientGender.setText(tv_genderMale.getText().toString());
            tncDialog.dismiss();
        });

        tv_genderFemale = (TextView) view.findViewById(R.id.tv_genderFemale);
        tv_genderFemale.setOnClickListener(v -> {
            et_clientGender.setText(tv_genderFemale.getText().toString());
            tncDialog.dismiss();
        });
    }

    TextView tv_home, tv_office;

    private void showClientAddressTypePopUp(final Context activity) {
        final Dialog tncDialog = new Dialog(activity);
        tncDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_addresstype, null, false);
        tncDialog.setContentView(view);
        final Window window = tncDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tncDialog.show();

        final ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);

        iv_close.setOnClickListener(v -> tncDialog.dismiss());
        tv_home = (TextView) view.findViewById(R.id.tv_home);
        tv_home.setOnClickListener(v -> {
            et_clientAddressType.setText(tv_home.getText().toString());
            tncDialog.dismiss();
        });

        tv_office = (TextView) view.findViewById(R.id.tv_office);
        tv_office.setOnClickListener(v -> {
            et_clientAddressType.setText(tv_office.getText().toString());
            tncDialog.dismiss();
        });
    }

    public void addClient() {
        // Call web service to addClient.
        BeanAddClient addClient = new BeanAddClient();

        addClient.setLoginType(PrefSetup.getInstance().getUserLoginType());

        if (client != null) {
            addClient.setClientId(client.getId());
        }

        addClient.setUserId(PrefSetup.getInstance().getUserId());
        addClient.setAddress(clientAddress);
        addClient.setFullName(et_clientFullName.getText().toString());
        addClient.setGender(et_clientGender.getText().toString().equalsIgnoreCase("Male") ? "M" : "F");
        addClient.setEmail(et_clientEmail.getText().toString());
        addClient.setClientAddressType(et_clientAddressType.getText().toString());
        addClient.setMobile(et_clientMobile.getText().toString());
        addClient.setAlternateContact(et_clientAlternateContact.getText().toString());
        addClient.setAlternateContactMobile(et_clientAlternateContactsMobile.getText().toString());
        try {
            new InteractorImpl(activity, FragEditProfileClient.this, Interactor.RequestCode_AddClient, Interactor.Tag_AddClient)
                    .makeJsonPostRequest(Interactor.Method_AddClient, new JSONObject(new Gson().toJson(addClient)), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        Utilities.getInstance().hideKeyboard(activity);
        switch (v.getId()) {

            case R.id.ll_addClient:
//            case R.id.iv_addClient:
                if (validateFields()) {
                    addClient();
                }
                break;
            case R.id.et_clientAddress:
                PopupUtils.getInstance().showClientAddressPopup(activity, object -> {
                    clientAddress = (BeanAddress) object;
                    et_clientAddress.setText(object.toString());
                }, clientAddress, "Address");
                break;

            case R.id.et_clientGender:
                showClientGenderPopUp(activity);
                break;

            case R.id.et_clientAddressType:
                showClientAddressTypePopUp(activity);
                break;
        }
    }

    private void updateFields(final ClientBean clientBean) {
        try {
            et_clientFullName.setText(clientBean.getName());

            if (clientBean.getGender() != null)
                et_clientGender.setText(clientBean.getGender().equalsIgnoreCase("m") ? "Male" : "Female");
            et_clientEmail.setText(clientBean.getEmail());
            et_clientMobile.setText(clientBean.getPhone());

            et_clientMobile.setFocusable(false);
            et_clientMobile.setFocusableInTouchMode(false);
            et_clientMobile.setClickable(false);

            et_clientAddressType.setText(clientBean.getClientAddressType());

            clientAddress = ProjectUtils.getInstance().getFormattedAddress(clientBean.getAddress());
            et_clientAddress.setText(clientAddress.toString());

            et_clientAlternateContact.setText(clientBean.getAlternetContact());
            et_clientAlternateContactsMobile.setText(clientBean.getAlternateContactMobile());

            tv_addClient.setText(getString(R.string.updateClient));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getClientDetail() {
        long clientId = getArguments().getLong("clientId");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
            jsonObject.put("clientId", clientId);
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());

        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(activity, FragEditProfileClient.this, Interactor.RequestCode_GetClientDetail, Interactor.Tag_GetClientDetail)
                .makeJsonPostRequest(Interactor.Method_GetClientDetail, jsonObject, true);
    }


    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);

        if (Interactor.RequestCode_AddClient == requestCode) {
            if (responsePacket.getErrorCode() == 0) {
                activity.makeToast(responsePacket.getMessage());
                getActivity().onBackPressed();
            } else {
                activity.makeToast(responsePacket.getMessage());
            }
        }

        if (Interactor.RequestCode_GetClientDetail == requestCode) {
            if (responsePacket.getErrorCode() == 0) {
                client = responsePacket.getClientDetail();
                updateFields(client);
            } else {
                activity.makeToast(responsePacket.getMessage());
            }
        }
    }


    @Override
    public void onError(int requestCode, ErrorType errorType) {
        super.onError(requestCode, errorType);

    }
}
