package com.superapp.activity;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.superapp.ApplicationContext;
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

public class ActivityAdminNotification extends BaseAppCompatActivity {
    private TextView tv_title, tv_description, tv_url;
    private ImageView iv_close, iv_image;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeUI();
        getAllAdminNotification();
    }

    public void initializeUI() {
        setContentView(R.layout.activity_admin_notification);
        try {
            tv_title = (TextView) findViewById(R.id.tv_title);
            iv_close = (ImageView) setClick(R.id.iv_close);
            iv_image = (ImageView) setClick(R.id.iv_image);
            tv_description = (TextView) findViewById(R.id.tv_description);
            tv_url = (TextView) findViewById(R.id.tv_url);
            progressBar = (ProgressBar) findViewById(R.id.progressBar);

            iv_image.setVisibility(View.GONE);

            tv_title.setText(getIntent().getStringExtra("title"));
            tv_description.setText(getIntent().getStringExtra("description"));

            if (!TextUtils.isEmpty(getIntent().getStringExtra("link"))) {
                tv_url.setVisibility(View.VISIBLE);
                SpannableString content = new SpannableString(getIntent().getStringExtra("link"));
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                tv_url.setText(content);
            } else {
                tv_url.setVisibility(View.GONE);
            }

//            tv_url.setText(fromHtml("<a href=" + getIntent().getStringExtra("link") + ">" + getIntent().getStringExtra("link") + "</a>"));
//            tv_url.setMovementMethod(LinkMovementMethod.getInstance());
//                tv_url.setVisibility(View.VISIBLE);
//            }

            /*tv_url.setOnClickListener(v ->
                    Utilities.getInstance().openWebUrl(getIntent().getStringExtra("link"), ActivityAdminNotification.this)
            );*/


            if (!TextUtils.isEmpty(getIntent().getStringExtra("image"))) {
                iv_image.setVisibility(View.VISIBLE);
                ApplicationContext.getInstance().loadImage(getIntent().getStringExtra("image"), iv_image, progressBar, 0);
            } else {
                progressBar.setVisibility(View.GONE);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @SuppressWarnings("deprecation")
//    public static Spanned fromHtml(String html) {
//        Spanned result;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
//        } else {
//            result = Html.fromHtml(html);
//        }
//        return result;
//    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        Utilities.getInstance().hideKeyboard(ActivityAdminNotification.this);
        switch (v.getId()) {
            case R.id.iv_close:
                onBackPressed();
                break;

/*            case R.id.tv_url:
//                Intent i = new Intent(ActivityAdminNotification.this, ActivityWebView.class);
//                i.putExtra("filePath", getIntent().getStringExtra("link"));
//                startActivity(i);
//
                Utilities.getInstance().openWebUrl(getIntent().getStringExtra("link"), ActivityAdminNotification.this);

                break;*/

        }
    }

   /* public void updateView() {
        tv_title.setText();
        tv_description.setText(getIntent().getStringExtra("description"));

        if (!TextUtils.isEmpty(getIntent().getStringExtra("link"))) {
            tv_url.setVisibility(View.VISIBLE);
            SpannableString content = new SpannableString(getIntent().getStringExtra("link"));
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            tv_url.setText(content);
        } else {
            tv_url.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(getIntent().getStringExtra("image"))) {
            iv_image.setVisibility(View.VISIBLE);
            ApplicationContext.getInstance().loadImage(getIntent().getStringExtra("image"), iv_image, progressBar, 0);
        } else {
            progressBar.setVisibility(View.GONE);
        }

    }*/


    private void getAllAdminNotification() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
            jsonObject.put("deviceType", "ANDROID");
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(getBaseContext(), new OnResponseListener() {
            @Override
            public void onSuccess(int requestCode, ResponsePacket responsePacket) {
                if (Interactor.RequestCode_GetAllAdminNotification == requestCode) {
                    if (responsePacket.getErrorCode() == 0) {
                        if (responsePacket.getNotificationInfo() != null) {
                            tv_title.setText(responsePacket.getNotificationInfo().getTitle());
                            tv_description.setText(responsePacket.getNotificationInfo().getDescription());

                            if (!TextUtils.isEmpty(responsePacket.getNotificationInfo().getLink())) {
                                tv_url.setVisibility(View.VISIBLE);
                                SpannableString content = new SpannableString(responsePacket.getNotificationInfo().getLink());
                                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                                tv_url.setText(content);
                            } else {
                                tv_url.setVisibility(View.GONE);
                            }

                            if (!TextUtils.isEmpty(responsePacket.getNotificationInfo().getImage())) {
                                iv_image.setVisibility(View.VISIBLE);
                                ApplicationContext.getInstance().loadImage(responsePacket.getNotificationInfo().getImage(), iv_image, progressBar, 0);
                            } else {
                                progressBar.setVisibility(View.GONE);
                            }

                        }
                    }

                    if (responsePacket.getErrorCode() == 1) {

                    }
                }
            }

            @Override
            public void onError(int requestCode, ErrorType errorType) {

            }
        }, Interactor.RequestCode_GetAllAdminNotification, Interactor.Tag_GetAllAdminNotification)
                .makeJsonPostRequest(Interactor.Method_GetAllAdminNotification, jsonObject, false);
    }


}