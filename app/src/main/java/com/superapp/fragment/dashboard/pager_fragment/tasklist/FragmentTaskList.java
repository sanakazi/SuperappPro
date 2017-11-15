package com.superapp.fragment.dashboard.pager_fragment.tasklist;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.superapp.R;
import com.superapp.activity.base.ErrorType;
import com.superapp.activity.ActivityMain;
import com.superapp.beans.BeanTaskList;
import com.superapp.utils.PrefSetup;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.OnResponseListener;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentTaskList extends Fragment implements OnResponseListener {

    private View fragmentView;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_tasklist, container, false);

        context = getActivity();
        initView();
        return fragmentView;
    }

    private RecyclerView rv_taskList;
    private TextView tv_noDataFound, tv_onGoingTask, tv_history;

    public void initView() {
        tv_noDataFound = (TextView) fragmentView.findViewById(R.id.tv_noDataFound);
        rv_taskList = (RecyclerView) fragmentView.findViewById(R.id.rv_taskList);
        GridLayoutManager manager = new GridLayoutManager(context, 1);
        rv_taskList.setHasFixedSize(true);
        rv_taskList.setLayoutManager(manager);
        rv_taskList.setItemAnimator(new DefaultItemAnimator());
        tv_history = (TextView) fragmentView.findViewById(R.id.tv_history);
        tv_onGoingTask = (TextView) fragmentView.findViewById(R.id.tv_onGoingTask);

        tv_history.setOnClickListener(v -> showHistoryTaskScreen());

        tv_onGoingTask.setOnClickListener(v -> showOnGoingTaskScreen());
        showOnGoingTaskScreen();
//        getAllTasks();
    }

    boolean isOngoing = true;

    private void showOnGoingTaskScreen() {
        isOngoing = true;
        rv_taskList.setVisibility(View.GONE);
        tv_onGoingTask.setBackgroundColor(context.getResources().getColor(R.color.colorLightGray));
        tv_history.setBackgroundColor(context.getResources().getColor(R.color.colorTextHintColor));
        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("w")) {
            getAllTasks();
        }
    }

    private void showHistoryTaskScreen() {
        isOngoing = false;
        rv_taskList.setVisibility(View.GONE);
        tv_history.setBackgroundColor(context.getResources().getColor(R.color.colorLightGray));
        tv_onGoingTask.setBackgroundColor(context.getResources().getColor(R.color.colorTextHintColor));
        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("w")) {
            getAllTasks();
        }
    }

    public void updateView() {
        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("w")) {
            getAllTasks();
        }
    }

    private void getAllTasks() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("taskType", isOngoing ? TaskType.Pending.getValue() : TaskType.History.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(context, FragmentTaskList.this, Interactor.RequestCode_GetAllTasks, Interactor.Tag_GetAllTasks)
                .makeJsonPostRequest(Interactor.Method_GetAllTasks, jsonObject, true);
    }

    private void updateTaskStatus(BeanTaskList taskItem) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("status", taskItem.getStatusForUpdate());
            if (!TextUtils.isEmpty(taskItem.getRejectReason())) {
                jsonObject.put("rejectReason", taskItem.getRejectReason());
            }
            jsonObject.put("taskId", taskItem.getTaskId());

        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(context, FragmentTaskList.this, Interactor.RequestCode_UpdateTaskStatus, Interactor.Tag_UpdateTaskStatus)
                .makeJsonPostRequest(Interactor.Method_UpdateTaskStatus, jsonObject, true);
    }

    AdapterTaskList adapterTaskList;

    private void setTaskListAdapter(ArrayList<BeanTaskList> taskLists) {
        if (taskLists.size() > 0) {
            tv_noDataFound.setVisibility(View.GONE);
            rv_taskList.setVisibility(View.VISIBLE);
            adapterTaskList = new AdapterTaskList(getActivity(), taskLists, isOngoing, (object, position) -> updateTaskStatus((BeanTaskList) object));
            rv_taskList.setAdapter(adapterTaskList);
        } else {
            tv_noDataFound.setVisibility(View.VISIBLE);
            rv_taskList.setVisibility(View.GONE);
        }
    }


    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        if (context instanceof ActivityMain) {
            if (responsePacket.getErrorCode() == 410) {
                ((ActivityMain) context).logoutUser();
                return;
            }
        }
        if (responsePacket.getErrorCode() == 0) {
            if (Interactor.RequestCode_GetAllTasks == requestCode) {
                setTaskListAdapter(responsePacket.getTaskList());
                tv_noDataFound.setText(responsePacket.getMessage());
            } else if (Interactor.RequestCode_UpdateTaskStatus == requestCode) {
                if (isOngoing) {
                    showOnGoingTaskScreen();
                } else {
                    showHistoryTaskScreen();
                }
                getAllTasks();
            }
        } else {
            Toast.makeText(context, responsePacket.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {
        if (ErrorType.ERROR500 == errorType) {
            Toast.makeText(context, context.getString(R.string.error500), Toast.LENGTH_SHORT).show();
        } else if (ErrorType.ERROR == errorType) {
            Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT).show();
        }
    }
}
