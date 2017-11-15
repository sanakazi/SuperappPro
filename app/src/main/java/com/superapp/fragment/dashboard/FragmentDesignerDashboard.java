package com.superapp.fragment.dashboard;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.ActivityMain;
import com.superapp.activity.base.ErrorType;
import com.superapp.beans.BeanAd;
import com.superapp.fragment.base.BaseFragment;
import com.superapp.fragment.base.FragmentNames;
import com.superapp.fragment.dashboard.pager_fragment.CustomViewPager;
import com.superapp.fragment.dashboard.pager_fragment.client.FragmentDashboardClient;
import com.superapp.fragment.dashboard.pager_fragment.coworkers.FragmentDashboardCoWorker;
import com.superapp.fragment.dashboard.pager_fragment.project.FragmentDashboardProject;
import com.superapp.utils.PopupUtils;
import com.superapp.utils.PrefSetup;
import com.superapp.utils.Utilities;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rakesh on 09-Aug-16.
 */
public class FragmentDesignerDashboard extends BaseFragment {

    public View fragmentView;
    ImageView iv_coWorkerSelection, iv_projectSelection, iv_clientSelection, iv_ad_bottom_left, iv_ad_bottom_right;

    LinearLayout ll_clients, ll_coWorker, ll_projects, ll_ad_bottom, ll_ad_bottom_left, ll_ad_bottom_right;
    private CustomViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentView = inflater.inflate(R.layout.activity_dashboard, container, false);

        initView();
        return fragmentView;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_clients:
                viewPager.setCurrentItem(0);
                break;

            case R.id.ll_projects:
                viewPager.setCurrentItem(1);
                break;

            case R.id.ll_coWorker:
                viewPager.setCurrentItem(2);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (activity instanceof ActivityMain) {
            ((ActivityMain) activity).setHeaderText(getString(R.string.dashboard));
            ((ActivityMain) activity).showHideFloatingButton(true, R.drawable.add);
            ((ActivityMain) activity).showHIdeSliderButton(false);
            ((ActivityMain) activity).showHideNotificationButton(true);
            if (PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("1")) {
                ((ActivityMain) activity).showAd(true);
            } else {
                ((ActivityMain) activity).showAd(false);
            }
        }
        updateView();
    }

    @Override
    public void onFloatingButtonClick() {
        super.onFloatingButtonClick();
        if (selectedPosition == 0) {
            ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentAddClient, null, false, false);

        } else if (selectedPosition == 1) {
            if (PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("1")) {
                if (ApplicationContext.getInstance().getProjectList().size() < 2) {
                    ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentAddProject, null, false, false);
                } else {
                    activity.makeLongToast(activity.getString(R.string.pleaseSubscribeForMore));
                }
            } else if (PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("2")) {
                if (ApplicationContext.getInstance().getProjectList().size() < 5) {
                    ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentAddProject, null, false, false);
                } else {
                    activity.makeLongToast(activity.getString(R.string.pleaseSubscribeForMore));
                }
            } else if (PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("3")) {
                ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentAddProject, null, false, false);
            }

        } else if (selectedPosition == 2) {
            ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentAddCoWorker, null, false, false);
        }
    }

    ImageView imageView;

    @Override
    public void initView() {
        super.initView();

        iv_coWorkerSelection = (ImageView) fragmentView.findViewById(R.id.iv_coWorkerSelection);
        iv_projectSelection = (ImageView) fragmentView.findViewById(R.id.iv_projectSelection);
        iv_clientSelection = (ImageView) fragmentView.findViewById(R.id.iv_clientSelection);

        viewPager = (CustomViewPager) fragmentView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        ll_clients = (LinearLayout) setTouchNClick(R.id.ll_clients, fragmentView);
        ll_coWorker = (LinearLayout) setTouchNClick(R.id.ll_coWorker, fragmentView);
        ll_projects = (LinearLayout) setTouchNClick(R.id.ll_projects, fragmentView);

        imageView = (ImageView) getActivity().findViewById(R.id.iv_ad_top);

        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
            getAllAds();
        } else {

        }
    }

    private int selectedPosition = 0;

    private void setSelected(int position) {
        selectedPosition = position;
        if (position == 0) {
            iv_clientSelection.setVisibility(View.VISIBLE);
            iv_projectSelection.setVisibility(View.INVISIBLE);
            iv_coWorkerSelection.setVisibility(View.INVISIBLE);
        } else if (position == 1) {
            iv_clientSelection.setVisibility(View.INVISIBLE);
            iv_projectSelection.setVisibility(View.VISIBLE);
            iv_coWorkerSelection.setVisibility(View.INVISIBLE);
        } else if (position == 2) {
            iv_clientSelection.setVisibility(View.INVISIBLE);
            iv_projectSelection.setVisibility(View.INVISIBLE);
            iv_coWorkerSelection.setVisibility(View.VISIBLE);
        }
    }

    private void setupViewPager(CustomViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(activity.getSupportFragmentManager());
        adapter.addFragment(new FragmentDashboardClient(), "Client");
        adapter.addFragment(new FragmentDashboardProject(), "Project");
        adapter.addFragment(new FragmentDashboardCoWorker(), "Co-Worker");
        viewPager.setPagingEnabled(true);
        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPager.setCurrentItem(1);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            if (observer != null) {
                super.unregisterDataSetObserver(observer);
            }
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void updateView() {
        super.updateView();
        try {
            for (Fragment fragment : ((ViewPagerAdapter) viewPager.getAdapter()).mFragmentList) {
                if (fragment instanceof FragmentDashboardProject) {
                    ((FragmentDashboardProject) fragment).updateView();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : ((ViewPagerAdapter) viewPager.getAdapter()).mFragmentList) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSliderButtonClick(View view) {
        super.onSliderButtonClick(view);
        float centreX = 0;//(view.getX() + view.getWidth()) / 2;
        float centreY = Utilities.getInstance().getValueInDP(120, activity);
        switch (view.getId()) {
            case R.id.iv_settingButton:
                try {
                    PopupUtils.getInstance().setEditProfilePopUp(activity, centreX, centreY);
                } catch (Exception e) {
                    ApplicationContext.getInstance().catException(e);
                }
                break;
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
        new InteractorImpl(getActivity(), FragmentDesignerDashboard.this, Interactor.RequestCode_getAllAds, Interactor.Tag_getAllAds)
                .makeJsonPostRequest(Interactor.Method_getAllAds, jsonObject, false);
    }


    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
        if (Interactor.RequestCode_getAllAds == requestCode)
            if (responsePacket.getErrorCode() == 0) {
                try {
                    ArrayList<BeanAd> beanAd = responsePacket.getAdDetail();
                    for (BeanAd beanAd1 : beanAd) {
                        if (beanAd1.getShowAd().equalsIgnoreCase("top")) {
                            ApplicationContext.getInstance().loadImage(beanAd1.getImage(), imageView, null, R.drawable.no_image_new);
                            ((ActivityMain)activity).BeanAd(beanAd1);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {
        super.onError(requestCode, errorType);
    }
}
