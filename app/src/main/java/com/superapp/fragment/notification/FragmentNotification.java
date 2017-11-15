package com.superapp.fragment.notification;

import android.content.Intent;
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
import com.superapp.activity.ActivityAdminNotification;
import com.superapp.activity.ActivityMain;
import com.superapp.activity.base.ErrorType;
import com.superapp.beans.NotificationBean;
import com.superapp.beans.ProjectBean;
import com.superapp.database.DBHelper;
import com.superapp.fragment.base.BaseFragment;
import com.superapp.fragment.base.FragmentNames;
import com.superapp.utils.PrefSetup;
import com.superapp.webservice.ResponsePacket;

import java.util.ArrayList;

public class FragmentNotification extends BaseFragment {

    View fragmentView;
    DBHelper helper;
    RecyclerView rv_notification;
    TextView tv_noDataFound;
    AdapterNotification adapterNotification;

    boolean isNotification = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentView = inflater.inflate(R.layout.fragment_notification, container, false);
        helper = new DBHelper(activity);

        Bundle b = getArguments();
        if (b.containsKey("isNotification")) {
            isNotification = b.getBoolean("isNotification");
        }
        initView();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isNotification) {
            ((ActivityMain) activity).setHeaderText(activity.getString(R.string.notification));
            tv_noDataFound.setText(activity.getString(R.string.noNotificationFound));
//        } else {
//            ((ActivityMain) activity).setHeaderText(activity.getString(R.string.reminder));
//            tv_noDataFound.setText(activity.getString(R.string.noReminderFound));
        }

        ((ActivityMain) activity).changeHeaderButton(true);
        ((ActivityMain) activity).showHideNotificationButton(false);
        ((ActivityMain) activity).showHideFloatingButton(false, R.drawable.add);
        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
            ((ActivityMain) activity).showHIdeSearchButton(true);
        } else {
            ((ActivityMain) activity).showHIdeSearchButton(false);
        }
    }

    @Override
    public void initView() {

        tv_noDataFound = (TextView) fragmentView.findViewById(R.id.tv_noDataFound);
        rv_notification = (RecyclerView) fragmentView.findViewById(R.id.rv_notification);
        GridLayoutManager manager = new GridLayoutManager(ApplicationContext.getInstance(), 1);
        rv_notification.setHasFixedSize(true);
        rv_notification.setLayoutManager(manager);
        rv_notification.setItemAnimator(new DefaultItemAnimator());
        getAllNotification();
    }

    private void getAllNotification() {
        setAllNotifications(helper.getAllNotifications(isNotification ? null : ApplicationContext.getInstance().project.getProjectId() + ""));
    }

    private void setAllNotifications(ArrayList<NotificationBean> notificationList) {
        if (notificationList.size() > 0) {
            rv_notification.setVisibility(View.VISIBLE);
            tv_noDataFound.setVisibility(View.GONE);
            adapterNotification = new AdapterNotification(activity, notificationList, (object, position) -> {
                NotificationBean bean = (NotificationBean) object;
                if (!isNotification) {
                    // This will execute when you come on this screen form Reminder
                    if (bean.getType().equalsIgnoreCase("Transaction")) {
                        ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentTransactions, null, false, false);
                    } else if (bean.getType().equalsIgnoreCase("Approval")) {
                        ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentApproval, null, false, false);
                    } else if (bean.getType().equalsIgnoreCase("Clarification")) {
                        ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentClarifications, null, false, false);
                    } else if (bean.getType().equalsIgnoreCase("Recommendations")) {
                        ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentRecommendations, null, false, false);
                    } else if (bean.getType().equalsIgnoreCase("Schedule")) {
                        ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentSchedule, null, false, false);
                    } else if (bean.getType().equalsIgnoreCase("Task")) {
                        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
                            ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentCheckList, null, false, false);
                        } else if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("w")) {
                            ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentCoWorkerDashboard, null, false, false);
                        }
                    } else if (bean.getType().equalsIgnoreCase("Admin")) {
//                    PopupUtils.getInstance().showAdminNotificationDialog(getContext());
                        startActivity(new Intent(getActivity(), ActivityAdminNotification.class));
                    }
                } else {
                    for (ProjectBean projectBean : ApplicationContext.getInstance().getProjectList()) {
                        if (projectBean.getProjectId() == bean.getProject_id()) {
                            ApplicationContext.getInstance().project = projectBean;
                            break;
                        }
                    }

                    if (bean.getType().equalsIgnoreCase("Transaction")) {
                        ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentTransactions, null, false, false);
                    } else if (bean.getType().equalsIgnoreCase("Approval")) {
                        ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentApproval, null, false, false);
                    } else if (bean.getType().equalsIgnoreCase("Clarification")) {
                        ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentClarifications, null, false, false);
                    } else if (bean.getType().equalsIgnoreCase("Recommendations")) {
                        ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentRecommendations, null, false, false);
                    } else if (bean.getType().equalsIgnoreCase("Schedule")) {
                        ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentSchedule, null, false, false);
                    } else if (bean.getType().equalsIgnoreCase("Task")) {
                        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
                            ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentCheckList, null, false, false);
                        } else if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("w")) {
                            ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentCoWorkerDashboard, null, false, false);
                        }
                    }
                }

                if (ApplicationContext.count != 0)
                    ApplicationContext.count = -1;
                helper.deleteNotification(bean.getId());
                if (activity instanceof ActivityMain) {
                    ((ActivityMain) activity).updateNotificationCounter();
                }
                getAllNotification();
            });
            rv_notification.setAdapter(adapterNotification);
        } else {
            rv_notification.setVisibility(View.GONE);
            tv_noDataFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {
        super.onError(requestCode, errorType);
    }
}