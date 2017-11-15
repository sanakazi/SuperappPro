package com.superapp.fragment.team;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.base.ErrorType;
import com.superapp.activity.ActivityMain;
import com.superapp.beans.CoworkerBean;
import com.superapp.fragment.base.BaseFragment;
import com.superapp.fragment.base.FragmentNames;
import com.superapp.fragment.dashboard.pager_fragment.coworkers.AdapterCoWorker;
import com.superapp.fragment.dashboard.pager_fragment.coworkers.CoworkerListAdapter;
import com.superapp.swipe.Attributes;
import com.superapp.utils.PrefSetup;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentTeam extends BaseFragment {

    View fragmentView;
    boolean addingCoWorkerWithResponse = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentView = inflater.inflate(R.layout.fragment_team, container, false);
        initView();

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActivityMain) activity).setHeaderText("Team");
        ((ActivityMain) activity).changeHeaderButton(true);
        ((ActivityMain) activity).showHideNotificationButton(true);
        ((ActivityMain) activity).showHideFloatingButton(false, R.drawable.add);
        ((ActivityMain) activity).showAd(false);
    }

    RecyclerView rv_teamList;
    TextView tv_noDataFound;
    CoworkerListAdapter adapterTeam;

    @Override
    public void initView() {

        tv_noDataFound = (TextView) fragmentView.findViewById(R.id.tv_noDataFound);
        rv_teamList = (RecyclerView) fragmentView.findViewById(R.id.rv_teamList);
        GridLayoutManager manager = new GridLayoutManager(ApplicationContext.getInstance(), 1);
        rv_teamList.setHasFixedSize(true);
        rv_teamList.setLayoutManager(manager);
        rv_teamList.setItemAnimator(new DefaultItemAnimator());
        getTeam();
    }


    private void getTeam() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("projectId", ApplicationContext.getInstance().project.getProjectId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(activity, FragmentTeam.this, Interactor.RequestCode_GetProjectStaffList, Interactor.Tag_GetProjectStaffList)
                .makeJsonPostRequest(Interactor.Method_GetProjectStaffList, jsonObject, true);
    }

    boolean hideArrow = false;

    private void setAllNotifications(ArrayList<CoworkerBean> teamBean) {
        if (teamBean.size() > 0) {
            rv_teamList.setVisibility(View.VISIBLE);
            tv_noDataFound.setVisibility(View.GONE);
            adapterTeam = new CoworkerListAdapter(ApplicationContext.getInstance(), teamBean,
                    !addingCoWorkerWithResponse, !hideArrow, (Object object, int position) -> {

                Bundle bundle = new Bundle();
                bundle.putString("hideArrow", "hideArrow");

                FragmentTeam.this.onItemClick((CoworkerBean) object);

            });
            ((CoworkerListAdapter) adapterTeam).setMode(Attributes.Mode.Single);
            rv_teamList.setAdapter(adapterTeam);
        } else {
            rv_teamList.setVisibility(View.GONE);
            tv_noDataFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFloatingButtonClick() {
        super.onFloatingButtonClick();

        ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentAddCoWorker, null, false, false);
    }

    private void onItemClick(CoworkerBean coworkerBean) {
        Bundle bundle = new Bundle();
        bundle.putString("CoworkerId", coworkerBean.getId() + "");

        if (getActivity() instanceof ActivityMain) {
            ((ActivityMain) getActivity()).replaceFragment(FragmentNames.FragmentCoworkerDetail, bundle, false, false);
        }

    }


    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
        if (getActivity() instanceof ActivityMain) {
            if (responsePacket.getErrorCode() == 410) {
                ((ActivityMain) getActivity()).logoutUser();
                return;
            }
        }
        if (responsePacket.getErrorCode() == 0) {
            if (Interactor.RequestCode_GetProjectStaffList == requestCode) {
                if (responsePacket.getErrorCode() == 0) {
                    if (responsePacket.getCoWorkerList() != null && responsePacket.getCoWorkerList().size() > 0) {
                        setAllNotifications(responsePacket.getCoWorkerList());

                    }
                    tv_noDataFound.setText(responsePacket.getMessage());
                }
            }
        } else {
            activity.makeToast(responsePacket.getMessage());
        }
    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {
        super.onError(requestCode, errorType);
    }
}
