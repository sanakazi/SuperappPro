package com.superapp.activity;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.DataSenderInterface;
import com.superapp.activity.base.ErrorType;
import com.superapp.activity.base.ViewMain;
import com.superapp.activity.supersearch.ActivitySuperSearch;
import com.superapp.beans.BeanAd;
import com.superapp.beans.BeanMaterial;
import com.superapp.beans.CoworkerBean;
import com.superapp.beans.NotificationBean;
import com.superapp.database.DBHelper;
import com.superapp.fcm.Config;
import com.superapp.fragment.FragEditProfile;
import com.superapp.fragment.FragEditProfileClient;
import com.superapp.fragment.FragEditProfileCoworker;
import com.superapp.fragment.FragSSEditProfileCoworker;
import com.superapp.fragment.aboutus.FragmentAboutUs;
import com.superapp.fragment.base.BaseFragment;
import com.superapp.fragment.base.FragmentNames;
import com.superapp.fragment.checklist.FragmentAddCheckList;
import com.superapp.fragment.checklist.FragmentCheckList;
import com.superapp.fragment.client_detail.FragmentClientDetail;
import com.superapp.fragment.communications.approval.FragmentApprovals;
import com.superapp.fragment.communications.clarifications.FragmentClarifications;
import com.superapp.fragment.communications.clarifications.FragmentClarificationsCompany;
import com.superapp.fragment.communications.recomandations.FragmentRecommendations;
import com.superapp.fragment.communications.recomandations.FragmentRecommendationsCompany;
import com.superapp.fragment.coworker.FragmentCoworkerDetail;
import com.superapp.fragment.dashboard.FragmentClientDashboard;
import com.superapp.fragment.dashboard.FragmentCoWorkerDashboard;
import com.superapp.fragment.dashboard.FragmentDesignerDashboard;
import com.superapp.fragment.dashboard.add_forms.FragmentAddClient;
import com.superapp.fragment.dashboard.add_forms.FragmentAddCoWorker;
import com.superapp.fragment.dashboard.add_forms.FragmentAddProject;
import com.superapp.fragment.dashboard.pager_fragment.archived_project.FragDashArchivedProject;
import com.superapp.fragment.feedback.FragmentFeedback;
import com.superapp.fragment.history.FragmentHistory;
import com.superapp.fragment.material.FragmentAddMaterial;
import com.superapp.fragment.membership.FragDowngradeMembership;
import com.superapp.fragment.membership.FragExpiredMembership;
import com.superapp.fragment.membership.FragUpgradeMembership;
import com.superapp.fragment.notification.FragmentNotification;
import com.superapp.fragment.project.FragmentArchivedProject;
import com.superapp.fragment.project.FragmentProject;
import com.superapp.fragment.reminder.FragmentReminder;
import com.superapp.fragment.schedule.FragmentSchedule;
import com.superapp.fragment.settings.FragmentSettings;
import com.superapp.fragment.survey.FragmentSurvey;
import com.superapp.fragment.team.FragmentTeam;
import com.superapp.fragment.transactions.FragmentTransactions;
import com.superapp.utils.PrefSetup;
import com.superapp.utils.Utilities;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.OnResponseListener;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityMain extends BaseAppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        ViewMain, OnResponseListener, DataSenderInterface {
    BaseFragment fragment;
    FloatingActionButton fab;
    static TextView tv_notificationCounter;
    int currentVerCode;

    //    DBHelper helper;
    NotificationBean bean;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    FragExpiredMembership fragExpiredMembership;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean finish = getIntent().getBooleanExtra("finish", false);
        if (finish) {
            startActivity(new Intent(ApplicationContext.getInstance(), ActivityLogin.class));
            finish();
            return;
        }

        if (PrefSetup.getInstance().getUserId() == 0) {
            startActivity(new Intent(ApplicationContext.getInstance(), ActivityLogin.class));
            finish();
            return;
        }

//        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d"))
//            if (!PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("1")) {
//                startActivity(new Intent(ActivityMain.this, ActivityExpiredMembership.class));
//            }

//        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d"))
//            if (!PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("1")) {
//                replaceFragment(FragmentNames.FragUpgradeMembership, null, false, false);
//            }

        Long tsLong = System.currentTimeMillis() / 1000;

        try {
            if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d"))
//            if (PrefSetup.getInstance().getUSER_MEMBER_SUBSCRIPTION_EXP_DATE() / 1000 < System.currentTimeMillis() / 1000) {
                if (!PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("1")) {
                    if (tsLong > PrefSetup.getInstance().getUSER_MEMBER_SUBSCRIPTION_EXP_DATE()) {
                        FragmentManager fm = getSupportFragmentManager();
                        fragExpiredMembership = new FragExpiredMembership();
                        fm.beginTransaction().add(R.id.fragMainContainer, fragExpiredMembership).commit();
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
//            }

//        helper = new DBHelper(this);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setImageResource(R.drawable.add);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.floatingButtonColor)));
        // fab.setColorPressed(getResources().getColor(R.color.primary_pressed));
        fab.setRippleColor(getResources().getColor(R.color.floatingButtonColor));
        fab.setOnClickListener(v -> {
            if (getLiveFragment() != null)
                getLiveFragment().onFloatingButtonClick();
        });

        // TODO Local Broad Cast Receiver
        bManager = LocalBroadcastManager.getInstance(this);
        intentFilter.addAction(Config.PUSH_NOTIFICATION);
        bManager.registerReceiver(bReceiver, intentFilter);
//        startService(new Intent(this, ServiceNotification.class));
        initView();

        if (!PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
            if (PrefSetup.getInstance().getLoginCheck().equalsIgnoreCase("t")) {
                startActivity(new Intent(ActivityMain.this, ActivityChangePassword.class));
            }
        }

        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
            if (PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("1"))
                startActivity(new Intent(ActivityMain.this, ActivitySurvey.class));
        }

        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("w")) {
            mAdView = new AdView(this);
            mAdView.setAdUnitId("ca-app-pub-5433915648159447/4352654817");
            mAdView.setAdSize(AdSize.BANNER);
            LinearLayout layout = (LinearLayout) findViewById(R.id.adView);
            layout.addView(mAdView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        } else {
            mAdView = new AdView(this);
            mAdView.setAdUnitId("ca-app-pub-5433915648159447/4352654817");
            mAdView.setAdSize(AdSize.MEDIUM_RECTANGLE);
            LinearLayout layout = (LinearLayout) findViewById(R.id.adView);
            layout.addView(mAdView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }


//        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d") || PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("c")) {
//            AdView adView = new AdView(this);
//            adView.setAdUnitId("ca-app-pub-5433915648159447/4352654817");
//            adView.setAdSize(AdSize.MEDIUM_RECTANGLE);

//        mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
//        }

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String currentVersion = pInfo.versionName;
            currentVerCode = pInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateView();

//        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
//                    // new push notification is received
////                    bean = (NotificationBean) intent.getSerializableExtra("message");
////
////                    showNotificationPopup(true);
////                    Bundle bundle = new Bundle();
////                    bundle.putBoolean("isNotification", true);
////                    replaceFragment(FragmentNames.FragmentNotification, bundle, false, false);
//
//                }
//            }
//        };
    }

    public void removeExpView() {
        getSupportFragmentManager().beginTransaction().remove(fragExpiredMembership).commit();
    }

    LocalBroadcastManager bManager;
    IntentFilter intentFilter = new IntentFilter();
    private BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
//                Utilities.getInstance().vibrateMobileForMilliSecond(ActivityMain.this, 100);
                showNotificationPopup(true);
            }
        }
    };

    private void showNotificationPopup(final boolean isBroadCasted) {
        try {
            updateNotificationCounter();
            try {
                if (getLiveFragment() != null) {
                    getLiveFragment().updateView();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }

    public static void updateNotificationCounter() {
        try {
            int notificationCounter = ApplicationContext.count;
            if (notificationCounter < 10) {
                if (notificationCounter == 0) {
                    tv_notificationCounter.setVisibility(View.GONE);
                } else {
                    tv_notificationCounter.setVisibility(View.VISIBLE);
                }
                tv_notificationCounter.setText("0" + notificationCounter);
            } else {
                tv_notificationCounter.setText(notificationCounter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showHideFloatingButton(boolean isShowing, int icon) {
        if (isShowing) {
            fab.setVisibility(View.VISIBLE);
            fab.setImageResource(icon);
        } else {
            fab.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewClick(View v) {
        super.onViewClick(v);
        Utilities.getInstance().hideKeyboard(ActivityMain.this);
        switch (v.getId()) {
            case R.id.iv_sliderButton:
                if (getLiveFragment() != null)
                    getLiveFragment().onSliderButtonClick(v);
                break;

            case R.id.iv_back:
               /* if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
                    if (!PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("1")) {
                        FragmentManager fm = getSupportFragmentManager();
                        fragExpiredMembership = new FragExpiredMembership();
                        fm.beginTransaction().add(R.id.fragMainContainer, fragExpiredMembership).commit();
                    }
                } else*/
                if (getLiveFragment() != null)
                    getLiveFragment().onBackButtonClick(v);
                break;

            case R.id.iv_settingButton:
                if (getLiveFragment() != null)
                    getLiveFragment().onSliderButtonClick(v);
                break;

            case R.id.iv_homeButton:
               /* if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
                    if (!PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("1")) {
                        FragmentManager fm = getSupportFragmentManager();
                        fragExpiredMembership = new FragExpiredMembership();
                        fm.beginTransaction().add(R.id.fragMainContainer, fragExpiredMembership).commit();
                    }
                } else*/

                if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
                    replaceFragment(FragmentNames.FragmentDesignerDashboard, null, true, true);
                } else if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("c")) {
                    replaceFragment(FragmentNames.FragmentClientDashboard, null, true, true);
                } else if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("w")) {
                    replaceFragment(FragmentNames.FragmentCoWorkerDashboard, null, true, true);
                }
                break;

            case R.id.iv_notification:
                Bundle bundle = new Bundle();
                bundle.putBoolean("isNotification", true);
                replaceFragment(FragmentNames.FragmentNotification, bundle, false, false);
                ApplicationContext.count = 0;
                break;

            case R.id.iv_search:
                Intent intent = new Intent(ActivityMain.this, ActivitySuperSearch.class);
                startActivity(intent);
                break;

            case R.id.iv_logo:
//                Intent intent = new Intent(ActivityMain.this, ActivitySuperSearch.class);
//                startActivity(intent);
                Utilities.getInstance().openWebUrl("http://www.ssspl.biz/", ActivityMain.this);
                break;
        }
    }

    private TextView tv_inner_title, tv_companyName;
    private ImageView iv_sliderButton, iv_back, iv_settingButton, iv_notification,
            iv_search, iv_companyLogo, iv_homeButton, iv_ad_top, iv_logo;
    private RelativeLayout rl_notification;
    private LinearLayout ll_logoName, ll_ad_top;
    private AdView mAdView;
    private FrameLayout fragMainContainer;

    @Override
    public void initView() {
        tv_inner_title = (TextView) findViewById(R.id.tv_inner_title);
        tv_companyName = (TextView) findViewById(R.id.tv_companyName);
        iv_companyLogo = (ImageView) findViewById(R.id.iv_companyLogo);
        rl_notification = (RelativeLayout) findViewById(R.id.rl_notification);
        tv_notificationCounter = (TextView) findViewById(R.id.tv_notificationCounter);
        iv_sliderButton = (ImageView) setTouchNClickSrc(R.id.iv_sliderButton);
        iv_back = (ImageView) setTouchNClickSrc(R.id.iv_back);
        iv_search = (ImageView) setTouchNClickSrc(R.id.iv_search);
        iv_settingButton = (ImageView) findViewById(R.id.iv_settingButton);
        setTouchNClickSrc(R.id.iv_settingButton);

        fragMainContainer = (FrameLayout) findViewById(R.id.fragMainContainer);

        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
            replaceFragment(FragmentNames.FragmentDesignerDashboard, null, true, false);
        } else if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("c")) {
            replaceFragment(FragmentNames.FragmentClientDashboard, null, true, false);
        } else if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("w")) {
            replaceFragment(FragmentNames.FragmentCoWorkerDashboard, null, true, false);
        }

        try {
            if (getIntent().getSerializableExtra("message") != null) {
//                bean = (NotificationBean) getIntent().getSerializableExtra("message");

                Bundle bundle = new Bundle();
                bundle.putSerializable("isNotification", true);
                replaceFragment(FragmentNames.FragmentNotification, null, false, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        iv_homeButton = (ImageView) setTouchNClickSrc(R.id.iv_homeButton);
        iv_logo = (ImageView) setTouchNClickSrc(R.id.iv_logo);
        rl_notification = (RelativeLayout) findViewById(R.id.rl_notification);
        iv_notification = (ImageView) setTouchNClickSrc(R.id.iv_notification);

        ll_logoName = (LinearLayout) findViewById(R.id.ll_logoName);
        ll_ad_top = (LinearLayout) findViewById(R.id.ll_ad_top);
        iv_ad_top = (ImageView) findViewById(R.id.iv_ad_top);

        updateHeaderDetails();
    }

    @Override
    public void updateView() {
        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
            if (PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("1"))
                mAdView.setVisibility(View.GONE);
        } else {
            mAdView.setVisibility(View.GONE);
        }
    }

    public void showAd(boolean isShowAd) {
        if (isShowAd) {
          //  mAdView.setVisibility(View.VISIBLE);
            mAdView.setVisibility(View.GONE);
        } else {
            mAdView.setVisibility(View.GONE);
        }
    }

    public void changeHeaderButton(boolean isBack) {
        if (isBack) {
            iv_sliderButton.setVisibility(View.GONE);
            iv_back.setVisibility(View.VISIBLE);
            iv_settingButton.setVisibility(View.GONE);
        } else {
            iv_back.setVisibility(View.GONE);
            iv_sliderButton.setVisibility(View.VISIBLE);
            iv_settingButton.setVisibility(View.VISIBLE);
        }
    }

    public void addimage(ImageView imageView) {

    }

    public void updateHeaderDetails() {
        String loginType = PrefSetup.getInstance().getUserLoginType();
        if (loginType.equalsIgnoreCase("d")) {
            // TODO
            if (PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("1")) {
                ll_logoName.setVisibility(View.GONE);
                ll_ad_top.setVisibility(View.VISIBLE);

            } else if (PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("2")) {
                iv_companyLogo.setVisibility(View.GONE);
                tv_companyName.setVisibility(View.VISIBLE);
                tv_companyName.setText(PrefSetup.getInstance().getUserCompanyName());
            } else if (PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("3")) {
                iv_companyLogo.setVisibility(View.VISIBLE);
                ApplicationContext.getInstance().loadImage(PrefSetup.getInstance().getUserCompanyLogo(), iv_companyLogo, null, R.drawable.no_image);
//                tv_companyName.setVisibility(View.VISIBLE);
//                tv_companyName.setText(PrefSetup.getInstance().getUserCompanyName());
            }
        } else if (loginType.equalsIgnoreCase("c")) {
            iv_companyLogo.setVisibility(View.GONE);
            tv_companyName.setVisibility(View.INVISIBLE);
        } else if (loginType.equalsIgnoreCase("w")) {
            iv_companyLogo.setVisibility(View.GONE);
            tv_companyName.setVisibility(View.INVISIBLE);
        }
    }

    public void setHeaderText(String title) {
        tv_inner_title.setText(title);
    }

    public void setHeaderText(int titleResource) {
        getSupportActionBar().setTitle(getString(titleResource));
    }

    public void showHideNotificationButton(boolean isShowButton) {
        if (isShowButton) {
            rl_notification.setVisibility(View.VISIBLE);
        } else {
            rl_notification.setVisibility(View.GONE);
        }
    }

    public void showHIdeSearchButton(boolean isShowButton) {
        if (isShowButton) {
            iv_search.setVisibility(View.VISIBLE);
        } else {
            iv_search.setVisibility(View.GONE);
        }
    }

    public void showHIdeSliderButton(boolean isShowButton) {
        if (isShowButton) {
            iv_sliderButton.setVisibility(View.VISIBLE);
        } else {
            iv_sliderButton.setVisibility(View.GONE);
        }
    }

    public void showHIdeHOmeButton(boolean isShowButton) {
        if (isShowButton) {
            iv_homeButton.setVisibility(View.VISIBLE);
        } else {
            iv_homeButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void showProgressing() {
        showProgressingView();
    }

    @Override
    public void hideProgressing() {
        hideProgressingView();
    }

    public void replaceFragment(FragmentNames fragmentName, Bundle bundle, boolean isFirst, boolean clearBackStack) {
        Utilities.getInstance().hideKeyboard(this);

        switch (fragmentName) {
            case FragmentDesignerDashboard:
                fragment = new FragmentDesignerDashboard();
                break;

            case FragmentAddClient:
                fragment = new FragmentAddClient();
                break;

            case FragmentAddProject:
                fragment = new FragmentAddProject();
                break;

            case FragmentAddCoWorker:
                fragment = new FragmentAddCoWorker();
                break;

            case FragmentProject:
                fragment = new FragmentProject();
                break;

            case FragmentSettings:
                fragment = new FragmentSettings();
                break;

            case FragEditProfile:
                fragment = new FragEditProfile();
                break;

            case FragmentTransactions:
                fragment = new FragmentTransactions();
                break;

            case FragmentNotification:
                fragment = new FragmentNotification();
                break;

            case FragmentSchedule:
                fragment = new FragmentSchedule();
                break;

            case FragmentAddCheckList:
                fragment = new FragmentAddCheckList();
                break;

            case FragmentClarificationsCompany:
                fragment = new FragmentClarificationsCompany();
                break;

            case FragmentRecommendationsCompany:
                fragment = new FragmentRecommendationsCompany();
                break;

            case FragmentTeam:
                fragment = new FragmentTeam();
                break;

            case FragmentApproval:
                fragment = new FragmentApprovals();
                break;

            case FragmentRecommendations:
                fragment = new FragmentRecommendations();
                break;

            case FragmentClarifications:
                fragment = new FragmentClarifications();
                break;

            case FragmentClientDashboard:
                fragment = new FragmentClientDashboard();
                break;

            case FragmentCoWorkerDashboard:
                fragment = new FragmentCoWorkerDashboard();
                break;

            case FragmentClientDetail:
                fragment = new FragmentClientDetail();
                break;

            case FragmentCoworkerDetail:
                fragment = new FragmentCoworkerDetail();
                break;

            case FragmentCheckList:
                fragment = new FragmentCheckList();
                break;

            case FragmentFeedback:
                fragment = new FragmentFeedback();
                break;

            case FragmentAboutUs:
                fragment = new FragmentAboutUs();
                break;

            case FragmentHistory:
                fragment = new FragmentHistory();
                break;

            case FragEditProfileClient:
                fragment = new FragEditProfileClient();
                break;

            case FragEditProfileCoworker:
                fragment = new FragEditProfileCoworker();
                break;

            case FragSSEditProfileCoworker:
                fragment = new FragSSEditProfileCoworker();
                break;

            case FragDowngradeMembership:
                fragment = new FragDowngradeMembership();
                break;

            case FragExpiredMembership:
                fragment = new FragExpiredMembership();
                break;

            case FragUpgradeMembership:
                fragment = new FragUpgradeMembership();
                break;

            case FragmentAddMaterial:
                fragment = new FragmentAddMaterial();
                break;

            case FragmentReminder:
                fragment = new FragmentReminder();
                break;

            case FragmentSurvey:
                fragment = new FragmentSurvey();
                break;

            case FragDashArchivedProject:
                fragment = new FragDashArchivedProject();
                break;

            case FragmentArchivedProject:
                fragment = new FragmentArchivedProject();
                break;

        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        // Code for Animation
//        ft.setCustomAnimations(R.anim.fade_out, R.anim.fade_in, R.anim.fade_out, R.anim.fade_in);
//        if (fragment != null)
//            ft.detach(fragment);

        if (clearBackStack) {
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        // if user passes the @bundle in not null, then can be added to the fragment

        try {
            if (bundle == null)
                bundle = new Bundle();
            fragment.setArguments(bundle);
        } catch (IllegalStateException e) {
            if (bundle != null && fragment != null && fragment.getArguments() != null) {
                // For String
                String[] keyArray = new String[]{
                        "projectId", "projectNAme", "description", "requestAmount",
                };
                for (String key : keyArray) {
                    if (bundle.containsKey(key)) {
                        fragment.getArguments().putString(key, bundle.getString(key));
                    }
                }
                // For Bool
                String[] keyBoolArray = new String[]{
                        "isNotification",
                };
                for (String key : keyBoolArray) {
                    if (bundle.containsKey(key)) {
                        fragment.getArguments().putBoolean(key, bundle.getBoolean(key));
                    }
                }

                // For SuperSearch
                String[] keySuperSearchArray = new String[]{
                        "companyName",
                        "ownerName",
                        "rating",
                        "address",
                        "mobileNumber",
                        "telephoneNumber",
                        "mailAddress",
                        "webUrl",
                        "photoUrl"
                };
                for (String key : keySuperSearchArray) {
                    if (bundle.containsKey(key)) {
                        fragment.getArguments().putBoolean(key, bundle.getBoolean(key));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // this is for the very first fragment not to be added into the back stack.
        if (!isFirst)
            ft.addToBackStack(fragment.getClass().getName() + "");

        fragment.fragmentType = fragmentName.getValue();

        ft.replace(R.id.main_frame_container, fragment);

        try {
            ft.commit();
        } catch (Exception e) {
            // TODO For a Test purpose
            ft.commitAllowingStateLoss();
            ft.commit();
        }
    }

    BeanAd beanAd;

    public void BeanAd(BeanAd beanAd) {
        this.beanAd = beanAd;
    }

    boolean doubleBackToExitPressedOnce = false;
    public boolean directBack = false;

    @Override
    public void onBackPressed() {
        Utilities.getInstance().hideKeyboard(ActivityMain.this);

        if (!Utilities.getInstance().isOnline(ActivityMain.this)) {
            super.onBackPressed();
        }

        if (directBack) {
            super.onBackPressed();
            directBack = false;
            return;
        }

        if (getLiveFragment() != null && (
                getLiveFragment() instanceof FragmentAddProject ||
                        getLiveFragment() instanceof FragmentAddCoWorker ||
                        getLiveFragment() instanceof FragmentAddClient
        )) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        } else if (getLiveFragment() != null && (
                getLiveFragment() instanceof FragmentDesignerDashboard
                        || getLiveFragment() instanceof FragmentClientDashboard ||
                        getLiveFragment() instanceof FragmentCoWorkerDashboard)) {
            if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")
                    && PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("1")) {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
//                if (beanAd != null) {
//                    if (!TextUtils.isEmpty(beanAd.getShowAd()) && beanAd.getShowAd().equalsIgnoreCase("close")) {
//                        Intent intent = new Intent(ActivityMain.this, ActivityAds.class);
//                        startActivity(intent);
//                    }
//                } else {
//                    super.onBackPressed();
//                }
                getAllAds();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2);
            } else {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        } else {
            super.onBackPressed();
        }
    }

    public void getAllAds() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
            jsonObject.put("membershipId", PrefSetup.getInstance().getUSER_MEMBERSHIPID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(ActivityMain.this, ActivityMain.this, Interactor.RequestCode_getAllAds, Interactor.Tag_getAllAds)
                .makeJsonPostRequest(Interactor.Method_getAllAds, jsonObject, false);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
//        int id = item.getItemId();
        return true;
    }

    public void logoutUser() {

        try {
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
            nMgr.cancel(1);
        } catch (Exception e) {
//                    e.printStackTrace();
        }

        Intent loginIntent = new Intent(ActivityMain.this, ActivityLogin.class);
        PrefSetup.getInstance().clearPrefSetup();
        DBHelper helper = new DBHelper(ActivityMain.this);
        helper.clearDataBaseTables();
//        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(loginIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNotificationCounter();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (getLiveFragment() != null)
                getLiveFragment().onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        hideProgressing();
        if (responsePacket.getErrorCode() == 410) {
            logoutUser();
        }

        if (Interactor.RequestCode_getAllAds == requestCode)
            if (responsePacket.getErrorCode() == 0) {
                try {
                    ArrayList<BeanAd> beanAd = responsePacket.getAdDetail();
                    for (BeanAd beanAd1 : beanAd) {
                        if (beanAd1.getShowAd().equalsIgnoreCase("close")) {
//                            ApplicationContext.getInstance().loadImage(beanAd1.getImage(), iv_ads_fullScreen, null, R.drawable.no_image_new);
                            Intent intent = new Intent(ActivityMain.this, ActivityAds.class);
                            for (BeanAd beanAd2 : responsePacket.getAdDetail()) {
                                intent.putExtra("image", beanAd2.getImage());
                            }

                            startActivity(intent);
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
    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {
        hideProgressing();
    }

    @Override
    public void sendData(BeanMaterial addMaterial) {
//        FragmentAddCheckList checkList = new FragmentAddCheckList();
//        checkList.updateData(addMaterial);
    }

    @Override
    public void sendCoWorkerData(CoworkerBean coworkerBean) {
//        FragmentAddCheckList checkList = new FragmentAddCheckList();
//        checkList.getCoworkerData(coworkerBean);
    }

//    @Override
//    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
//        super.onSuccess(requestCode, responsePacket);
//
//    }


}