package com.superapp.activity.supersearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.ErrorType;
import com.superapp.activity.base.ViewMain;
import com.superapp.beans.SuperSearchBean;
import com.superapp.utils.PrefSetup;
import com.superapp.utils.Utilities;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;

/**
 * Created by APPNWEB on 26-08-2016.
 */
public class ActivitySearchItemDetail extends BaseAppCompatActivity implements ViewMain {
    LayoutInflater inflater;
    SuperSearchBean superSearchBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item_detail);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setStatusBarColor();
        try {
            superSearchBean = getIntent().getExtras().getParcelable("superSearchBean");
        } catch (Exception e) {
            e.printStackTrace();
            onBackPressed();
        }
        initView();
    }

    @Override
    public void showProgressing() {
        showProgressingView();
    }

    @Override
    public void hideProgressing() {
        hideProgressingView();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    private ImageView iv_searchItemDetail, iv_callSuperSearchDetail, iv_telephoneSuperSearchDetail, iv_mailSuperSearchDetail,
            iv_webSuperSearchDetail;
    private LinearLayout ll_searchItemDetailImage, ll_ratingIcon, ll_rating, ll_coworkersRating, ll_specialFeatures,
            ll_workingHours, ll_preferredLocation, ll_locality, ll_superSearchDetailBusinessName;
    private RatingBar ratingBar;
    private Button bt_submit;
    private TextView tv_superSearchDetailBusinessName, tv_superSearchRating, tv_superSearchOwnerName,
            tv_clientAddressSuperSearchDetail, tv_superSearchDetailCoworkersRating,
            tv_superSearchDetailCategory, tv_superSearchDetailOccupation, tv_superSearchDetailSpclFeatures,
            tv_superSearchDetailClosedOn, tv_superSearchDetailLocation, tv_superSearchDetailFrom, tv_superSearchDetailTo,
            tv_superSearchDetailLocality;

    @Override
    public void initView() {
        try {
            setTouchNClickSrc(R.id.iv_back);

            ll_searchItemDetailImage = (LinearLayout) findViewById(R.id.ll_searchItemDetailImage);
            ll_ratingIcon = (LinearLayout) findViewById(R.id.ll_ratingIcon);
            setTouchNClickSrc(R.id.ll_ratingIcon);
            ll_rating = (LinearLayout) findViewById(R.id.ll_rating);
            ll_coworkersRating = (LinearLayout) findViewById(R.id.ll_coworkersRating);

            ll_specialFeatures = (LinearLayout) findViewById(R.id.ll_specialFeatures);
            ll_workingHours = (LinearLayout) findViewById(R.id.ll_workingHours);
            ll_preferredLocation = (LinearLayout) findViewById(R.id.ll_preferredLocation);
            ll_locality = (LinearLayout) findViewById(R.id.ll_locality);
            ll_superSearchDetailBusinessName = (LinearLayout) findViewById(R.id.ll_superSearchDetailBusinessName);

            ratingBar = (RatingBar) findViewById(R.id.ratingBar);

            bt_submit = (Button) findViewById(R.id.bt_submit);

            tv_superSearchDetailBusinessName = (TextView) findViewById(R.id.tv_superSearchDetailBusinessName);
            tv_superSearchRating = (TextView) findViewById(R.id.tv_superSearchDetailRating);
            tv_superSearchOwnerName = (TextView) findViewById(R.id.tv_superSearchDetailOwnerName);
            tv_clientAddressSuperSearchDetail = (TextView) findViewById(R.id.tv_clientAddressSuperSearchDetail);

            tv_superSearchDetailCategory = (TextView) findViewById(R.id.tv_superSearchDetailCategory);
            tv_superSearchDetailOccupation = (TextView) findViewById(R.id.tv_superSearchDetailOccupation);
            tv_superSearchDetailSpclFeatures = (TextView) findViewById(R.id.tv_superSearchDetailSpclFeatures);
            tv_superSearchDetailClosedOn = (TextView) findViewById(R.id.tv_superSearchDetailClosedOn);
            tv_superSearchDetailLocation = (TextView) findViewById(R.id.tv_superSearchDetailLocation);
            tv_superSearchDetailFrom = (TextView) findViewById(R.id.tv_superSearchDetailFrom);
            tv_superSearchDetailTo = (TextView) findViewById(R.id.tv_superSearchDetailTo);
            tv_superSearchDetailLocality = (TextView) findViewById(R.id.tv_superSearchDetailLocality);
            tv_superSearchDetailCoworkersRating = (TextView) findViewById(R.id.tv_superSearchDetailCoworkersRating);
            tv_superSearchDetailBusinessName = (TextView) findViewById(R.id.tv_superSearchDetailBusinessName);

            iv_searchItemDetail = (ImageView) findViewById(R.id.iv_searchItemDetail);

            iv_callSuperSearchDetail = (ImageView) findViewById(R.id.iv_callSuperSearchDetail);
            iv_telephoneSuperSearchDetail = (ImageView) findViewById(R.id.iv_telephoneSuperSearchDetail);
            iv_mailSuperSearchDetail = (ImageView) findViewById(R.id.iv_mailSuperSearchDetail);
            iv_webSuperSearchDetail = (ImageView) findViewById(R.id.iv_webSuperSearchDetail);
            iv_callSuperSearchDetail.setOnTouchListener(BaseAppCompatActivity.imageTouch);
            iv_telephoneSuperSearchDetail.setOnTouchListener(BaseAppCompatActivity.imageTouch);
            iv_mailSuperSearchDetail.setOnTouchListener(BaseAppCompatActivity.imageTouch);
            iv_webSuperSearchDetail.setOnTouchListener(BaseAppCompatActivity.imageTouch);


            iv_callSuperSearchDetail.setOnClickListener(v -> Utilities.getInstance().doCall(ActivitySearchItemDetail.this, superSearchBean.getMobileNumber()));
            iv_telephoneSuperSearchDetail.setOnClickListener(v -> Utilities.getInstance().doCall(ActivitySearchItemDetail.this, superSearchBean.getTelephoneNumber()));
            iv_mailSuperSearchDetail.setOnClickListener(v -> Utilities.getInstance().sendEmail(ActivitySearchItemDetail.this, superSearchBean.getMailAddress(), "", ""));
            iv_webSuperSearchDetail.setOnClickListener(v -> Utilities.getInstance().openWebUrl(superSearchBean.getWebUrl(), ActivitySearchItemDetail.this));

            addImages();

//            String s = superSearchBean.getCoworkerRating();
//            long l = Long.parseLong(s);

            if (Long.parseLong(superSearchBean.getCoworkerRating()) > 0) {
//            if (l > 0) {
                ll_ratingIcon.setVisibility(View.GONE);
                ll_rating.setVisibility(View.GONE);
                ll_coworkersRating.setVisibility(View.VISIBLE);
            }

            ll_ratingIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ll_ratingIcon.setVisibility(View.GONE);
                    ll_rating.setVisibility(View.VISIBLE);
                    ll_coworkersRating.setVisibility(View.GONE);
                }
            });

            bt_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitRating();
                }
            });

            updateView();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateView() {

        tv_superSearchDetailCategory.setText(superSearchBean.getCatName());

        if (!TextUtils.isEmpty(superSearchBean.getBusinessName())) {
            tv_superSearchDetailBusinessName.setText(superSearchBean.getBusinessName());
        } else {
            ll_superSearchDetailBusinessName.setVisibility(View.GONE);
        }

        tv_superSearchOwnerName.setText(superSearchBean.getOwnerName());
        tv_superSearchDetailOccupation.setText("Occupation/What do we sell! : " + superSearchBean.getOccupation());
        tv_superSearchRating.setText(superSearchBean.getAvgRating());
        tv_clientAddressSuperSearchDetail.setText(superSearchBean.getAddress());
        tv_superSearchDetailCoworkersRating.setText(superSearchBean.getCoworkerRating());
        tv_superSearchDetailClosedOn.setText(superSearchBean.getCloseWeek());


        if (!TextUtils.isEmpty(superSearchBean.getCloseWeek())) {
            tv_superSearchDetailClosedOn.setText(superSearchBean.getCloseWeek());
        } else {
            tv_superSearchDetailClosedOn.setText("N/A");
        }

        if (!TextUtils.isEmpty(superSearchBean.getAddress())) {
            tv_clientAddressSuperSearchDetail.setText(superSearchBean.getAddress());
        } else {
            tv_clientAddressSuperSearchDetail.setText("No Address Available");
        }

        if (!TextUtils.isEmpty(superSearchBean.getSpecialFeature())) {
            tv_superSearchDetailSpclFeatures.setText("Special features / quality : " + superSearchBean.getSpecialFeature());
        } else {
            ll_specialFeatures.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(superSearchBean.getStartTiming())
                && !TextUtils.isEmpty(superSearchBean.getEndTiming())) {
            tv_superSearchDetailFrom.setText(superSearchBean.getStartTiming());
            tv_superSearchDetailTo.setText(superSearchBean.getEndTiming());
        } else {
            ll_workingHours.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(superSearchBean.getPreferredLocation())) {
            tv_superSearchDetailLocation.setText(superSearchBean.getPreferredLocation());
        } else {
            ll_preferredLocation.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(superSearchBean.getLocality())) {
            tv_superSearchDetailLocality.setText(superSearchBean.getLocality());
        } else {
            tv_superSearchDetailLocality.setVisibility(View.GONE);
        }

    }


    private void addImages() {
//        ArrayList<String> imageUrls = new ArrayList<>();
//        imageUrls.add("http://www.etiles.net.au/images/products/210_1_3.jpg");
//        imageUrls.add("http://www.sketchuptextureclub.com/public/texture_d/0010-porcelain-floor-tiles-cm-100x100-texture-seamless-hr.jpg");
//        imageUrls.add("http://www.sketchuptextureclub.com/public/texture_d/0007-porcelain-floor-tiles-cm-100x100-texture-seamless-hr.jpg");
//        imageUrls.add("https://sc01.alicdn.com/kf/HTB1gqgaLVXXXXXBXXXXq6xXFXXXY/Yellow-wood-jade-design-100x100-floor-tiles.jpg_200x200.jpg");
//        imageUrls.add("http://image.made-in-china.com/2f0j00jswEDMAkQpqK/100X100-Marble-Pattern-Floor-Design-Tiles-with-Pictures-C1616-01H-.jpg");
//        imageUrls.add("http://image.made-in-china.com/43f34j00OZAQSUyJpNka/100X100-Marble-Pattern-Floor-Design-Tiles-with-Pictures-C1616-01H-.jpg");
//        imageUrls.add("http://www.sketchuptextureclub.com/public/texture_m/0006-porcelain-floor-tiles-cm-100x100-texture-seamless-specular.jpg");

        boolean isSet = false;
        for (String url : superSearchBean.getPhotoUrl()) {
            View view = inflater.inflate(R.layout.activity_upload_image_item, null);
            final ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
            ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            ApplicationContext.getInstance().loadImage(url, iv_image, progressBar, R.drawable.no_image);
            iv_image.setOnClickListener(v -> iv_searchItemDetail.setImageDrawable(iv_image.getDrawable()));
            ll_searchItemDetailImage.addView(view);

            if (!isSet) {
                isSet = true;
                ApplicationContext.getInstance().loadImage(url, iv_searchItemDetail, progressBar, R.drawable.no_image_new);
            }
        }
    }

    private void submitRating() {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("coworkerId", superSearchBean.getCoworkerId());
            jsonObject.put("rating", ratingBar.getRating() + "");

            new InteractorImpl(ActivitySearchItemDetail.this, ActivitySearchItemDetail.this, Interactor.RequestCode_submitRating, Interactor.Tag_submitRating)
                    .makeJsonPostRequest(Interactor.Method_submitRating, jsonObject, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);

        if (Interactor.RequestCode_submitRating == requestCode) {
            if (responsePacket.getErrorCode() == 0) {
                ll_ratingIcon.setVisibility(View.GONE);
                ll_rating.setVisibility(View.GONE);
                ll_coworkersRating.setVisibility(View.VISIBLE);

                superSearchBean.setCoworkerRating(ratingBar.getRating() + "");
                tv_superSearchDetailCoworkersRating.setText(superSearchBean.getCoworkerRating());
                makeToast(responsePacket.getMessage());
            } else {
                makeToast(responsePacket.getMessage());
            }
        }
    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {
        super.onError(requestCode, errorType);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
