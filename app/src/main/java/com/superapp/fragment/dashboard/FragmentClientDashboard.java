package com.superapp.fragment.dashboard;

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

import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.ActivityMain;
import com.superapp.activity.base.ErrorType;
import com.superapp.fragment.base.BaseFragment;
import com.superapp.fragment.dashboard.pager_fragment.project.FragmentDashboardProject;
import com.superapp.utils.PopupUtils;
import com.superapp.utils.Utilities;
import com.superapp.webservice.ResponsePacket;

import java.util.ArrayList;
import java.util.List;

public class FragmentClientDashboard extends BaseFragment {

    public View fragmentView;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentView = inflater.inflate(R.layout.activity_client_dashboard, container, false);

        initView();
        return fragmentView;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (activity instanceof ActivityMain) {
            ((ActivityMain) activity).setHeaderText(getString(R.string.dashboard));
            ((ActivityMain) activity).showHideFloatingButton(false, R.drawable.add);
            ((ActivityMain) activity).showHIdeSliderButton(false);
            ((ActivityMain) activity).showHIdeSearchButton(false);
            ((ActivityMain) activity).showAd(true);
        }
        updateView();
    }

    @Override
    public void initView() {
        super.initView();
        viewPager = (ViewPager) fragmentView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(activity.getSupportFragmentManager());
        adapter.addFragment(new FragmentDashboardProject(), "Project");
        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
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

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
        if (responsePacket.getStatus().equalsIgnoreCase("success")) {
        }
    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {
        super.onError(requestCode, errorType);
    }
}
