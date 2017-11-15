package com.superapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.superapp.R;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.utils.PrefSetup;

public class ActivitySplash extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTransparent();
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(thread, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(thread);
    }

    Handler handler = new Handler();
    Runnable thread = new Runnable() {
        @Override
        public void run() {
            openNextActivity();
        }
    };

    private void openNextActivity() {
        Intent intent;
        if (PrefSetup.getInstance().getUserId() > 0) {
            intent = new Intent(this, ActivityMain.class);
        } else {
            intent = new Intent(this, ActivityLogin.class);
        }
        startActivity(intent);
        finish();
    }
}
