package com.superapp.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;

import com.superapp.R;
import com.superapp.activity.base.BaseAppCompatActivity;

public class ActivityWebView extends BaseAppCompatActivity {
    WebView wv_download_project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_about_us);
        initView();
    }

    public void initView() {
        String filePath = getIntent().getStringExtra("filePath");
        wv_download_project = (WebView) findViewById(R.id.wv_aboutUs);
        wv_download_project.loadUrl(filePath);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}
