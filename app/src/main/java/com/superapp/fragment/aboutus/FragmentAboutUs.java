package com.superapp.fragment.aboutus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.superapp.R;
import com.superapp.activity.ActivityMain;
import com.superapp.fragment.base.BaseFragment;
import com.superapp.utils.PrefSetup;
import com.superapp.webservice.Interactor;


public class FragmentAboutUs extends BaseFragment {

    private View fragmentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentView = inflater.inflate(R.layout.fragment_about_us, container, false);
        initView();
        return fragmentView;
    }

    @Override
    public void initView() {

        WebView wv_aboutUs = (WebView) fragmentView.findViewById(R.id.wv_aboutUs);
        wv_aboutUs.loadUrl(Interactor.ABOUT_US_URL);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActivityMain) activity).setHeaderText(getString(R.string.aboutus));
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

}
