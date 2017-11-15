package com.superapp.fragment.reminder;

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
import com.superapp.activity.ActivityMain;
import com.superapp.activity.base.ErrorType;
import com.superapp.activity.base.OnItemClickListener;
import com.superapp.beans.ReminderBean;
import com.superapp.fragment.base.BaseFragment;
import com.superapp.utils.PrefSetup;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentReminder extends BaseFragment {

    View fragmentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentView = inflater.inflate(R.layout.fragment_reminder, container, false);

        initView();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActivityMain) activity).setHeaderText(activity.getString(R.string.reminder));

        ((ActivityMain) activity).changeHeaderButton(true);
        ((ActivityMain) activity).showHideNotificationButton(false);
        ((ActivityMain) activity).showHideFloatingButton(false, R.drawable.add);
        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
            ((ActivityMain) activity).showHIdeSearchButton(true);
        } else {
            ((ActivityMain) activity).showHIdeSearchButton(false);
        }
    }

    RecyclerView rv_reminder;
    TextView tv_noDataFound;
    AdapterReminder adapterReminder;

    @Override
    public void initView() {
        tv_noDataFound = (TextView) fragmentView.findViewById(R.id.tv_noDataFound);
        rv_reminder = (RecyclerView) fragmentView.findViewById(R.id.rv_reminder);
        GridLayoutManager manager = new GridLayoutManager(ApplicationContext.getInstance(), 1);
        rv_reminder.setHasFixedSize(true);
        rv_reminder.setLayoutManager(manager);
        rv_reminder.setItemAnimator(new DefaultItemAnimator());
        getAllReminders();
    }


    private void getAllReminders() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
            jsonObject.put("projectId", ApplicationContext.getInstance().project.getProjectId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(activity, FragmentReminder.this, Interactor.RequestCode_GetAllReminder, Interactor.Tag_GetAllReminder)
                .makeJsonPostRequest(Interactor.Method_GetAllReminder, jsonObject, true);

    }

    private void setAllReminders(ArrayList<ReminderBean> reminderList) {
        if (reminderList.size() > 0) {
            tv_noDataFound.setVisibility(View.GONE);
            rv_reminder.setVisibility(View.VISIBLE);

            adapterReminder = new AdapterReminder(activity, reminderList, new OnItemClickListener() {
                @Override
                public void onItemClick(Object object, int position) {

                }
            });
            rv_reminder.setAdapter(adapterReminder);
        } else {
            tv_noDataFound.setVisibility(View.VISIBLE);
            rv_reminder.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
        if (responsePacket.getErrorCode() == 0) {
            if (Interactor.RequestCode_GetAllReminder == requestCode) {
                if (responsePacket.getReminderList() != null) {
                    setAllReminders(responsePacket.getReminderList());
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