package com.superapp.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.ErrorType;
import com.superapp.beans.BeanAd;
import com.superapp.utils.PrefSetup;
import com.superapp.utils.Utilities;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityAds extends BaseAppCompatActivity {

    ImageView iv_close, iv_ads_fullScreen;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ads_fullscreen);
        initializeUI();

    }

    public void initializeUI() {
        iv_close = (ImageView) setTouchNClickSrc(R.id.iv_close);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        iv_ads_fullScreen = (ImageView) findViewById(R.id.iv_ads_fullScreen);
//        getAllAds();
        ApplicationContext.getInstance().loadImage(getIntent().getStringExtra("image"), iv_ads_fullScreen, null, R.drawable.no_image_new);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Utilities.getInstance().hideKeyboard(ActivityAds.this);
        switch (v.getId()) {
            case R.id.iv_close:
//                Intent intent = new Intent();
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                moveTaskToBack(true);
                finish();
                break;
        }
    }

    /*public void getAllAds() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
            jsonObject.put("membershipId", PrefSetup.getInstance().getUSER_MEMBERSHIPID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(ActivityAds.this, ActivityAds.this, Interactor.RequestCode_getAllAds, Interactor.Tag_getAllAds)
                .makeJsonPostRequest(Interactor.Method_getAllAds, jsonObject, false);
    }*/


 /*   @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
//        if (responsePacket.getStatus().equalsIgnoreCase("success")) {

        if (Interactor.RequestCode_getAllAds == requestCode)
            if (responsePacket.getErrorCode() == 0) {
                try {
                    ArrayList<BeanAd> beanAd = responsePacket.getAdDetail();
                    for (BeanAd beanAd1 : beanAd) {
                        if (beanAd1.getShowAd().equalsIgnoreCase("close")) {
                            ApplicationContext.getInstance().loadImage(beanAd1.getImage(), iv_ads_fullScreen, null, R.drawable.no_image_new);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (responsePacket.getErrorCode() != 0) {
                finish();
                moveTaskToBack(true);
            }
    }*/
/*
    @Override
    public void onError(int requestCode, ErrorType errorType) {
        super.onError(requestCode, errorType);
    }*/


}
