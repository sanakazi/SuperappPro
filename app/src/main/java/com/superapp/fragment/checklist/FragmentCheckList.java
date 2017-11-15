package com.superapp.fragment.checklist;

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
import com.superapp.beans.BeanTaskList;
import com.superapp.fragment.base.BaseFragment;
import com.superapp.fragment.base.FragmentNames;
import com.superapp.utils.PrefSetup;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by APPNWEB on 03-09-2016.
 */
public class FragmentCheckList extends BaseFragment {

    public View fragmentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        fragmentView = inflater.inflate(R.layout.fragment_checklist, container, false);
        initView();

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActivityMain) activity).setHeaderText("CheckList");
        ((ActivityMain) activity).changeHeaderButton(true);
        ((ActivityMain) activity).showHideNotificationButton(true);

        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
            ((ActivityMain) activity).showHIdeSearchButton(true);
            ((ActivityMain) activity).showHideFloatingButton(true, R.drawable.add);
        } else {
            ((ActivityMain) activity).showHIdeSearchButton(false);
            ((ActivityMain) activity).showHideFloatingButton(false, R.drawable.add);
        }

        if (ApplicationContext.getInstance().project != null) {
            if (ApplicationContext.getInstance().project.getIsComplete().equalsIgnoreCase("f")) {
                ((ActivityMain) activity).showHideFloatingButton(true, R.drawable.add);
            } else if (ApplicationContext.getInstance().project.getIsComplete().equalsIgnoreCase("t")) {
                ((ActivityMain) activity).showHideFloatingButton(false, R.drawable.add);
            }
        } else {
            ((ActivityMain) activity).showHideFloatingButton(false, R.drawable.add);
        }

        ((ActivityMain) activity).showAd(false);

        getAllCheckList();
    }

    RecyclerView rv_checklist;
    TextView tv_noDataFound;
    AdapterCheckList adapterchecklist;

    @Override
    public void initView() {
        super.initView();
        tv_noDataFound = (TextView) fragmentView.findViewById(R.id.tv_noDataFound);
        rv_checklist = (RecyclerView) fragmentView.findViewById(R.id.rv_checklist);
        GridLayoutManager manager = new GridLayoutManager(ApplicationContext.getInstance(), 1);
        rv_checklist.setHasFixedSize(true);
        rv_checklist.setLayoutManager(manager);
        rv_checklist.setItemAnimator(new DefaultItemAnimator());
    }

    private void getAllCheckList() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("projectId", ApplicationContext.getInstance().project.getProjectId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(activity, FragmentCheckList.this, Interactor.RequestCode_GetAllTaskOfProject, Interactor.Tag_GetAllTaskOfProject)
                .makeJsonPostRequest(Interactor.Method_GetAllTaskOfProject, jsonObject, true);
    }

    private void setAllCheckList(ArrayList<BeanTaskList> beanTaskList) {
        if (beanTaskList.size() > 0) {
            tv_noDataFound.setVisibility(View.GONE);
            rv_checklist.setVisibility(View.VISIBLE);
            adapterchecklist = new AdapterCheckList(activity, beanTaskList, (object, position) -> {
            });
            rv_checklist.setAdapter(adapterchecklist);
        } else {
            tv_noDataFound.setVisibility(View.VISIBLE);
            rv_checklist.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFloatingButtonClick() {
        super.onFloatingButtonClick();
        if (activity instanceof ActivityMain)
            ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentAddCheckList, null, false, false);
    }

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
        if (responsePacket.getErrorCode() == 410) {
            return;
        }
        if (responsePacket.getErrorCode() == 0) {
            if (Interactor.RequestCode_GetAllTaskOfProject == requestCode) {
                if (responsePacket.getTaskList() != null) {
                    setAllCheckList(responsePacket.getTaskList());
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
