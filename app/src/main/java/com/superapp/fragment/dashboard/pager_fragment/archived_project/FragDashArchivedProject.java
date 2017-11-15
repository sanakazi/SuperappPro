package com.superapp.fragment.dashboard.pager_fragment.archived_project;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.ActivityMain;
import com.superapp.activity.base.ErrorType;
import com.superapp.beans.ProjectBean;
import com.superapp.fragment.base.BaseFragment;
import com.superapp.fragment.base.FragmentNames;
import com.superapp.utils.PrefSetup;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.OnResponseListener;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;

import java.util.ArrayList;

public class FragDashArchivedProject extends BaseFragment implements OnResponseListener {

    private View fragmentView;
//    DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_dashboard_project, container, false);
//        dbHelper = new DBHelper(getActivity());
        initView();
        return fragmentView;
    }

    RecyclerView rv_projectList;
    ProgressBar progressBarProject;
    TextView tv_noDataFound;
    AdapterArchivedProject adapterArchivedProject;


    @Override
    public void onResume() {
        super.onResume();
        ((ActivityMain) activity).setHeaderText("Archived Projects");
        ((ActivityMain) activity).changeHeaderButton(true);
        ((ActivityMain) activity).showHideFloatingButton(false, R.drawable.add);
        ((ActivityMain) activity).showHideNotificationButton(false);
        ((ActivityMain) activity).showHIdeSearchButton(true);
        ((ActivityMain) activity).showAd(false);
    }

    public void initView() {
        progressBarProject = (ProgressBar) fragmentView.findViewById(R.id.progressBarProject);
        rv_projectList = (RecyclerView) fragmentView.findViewById(R.id.rv_projectList);
        tv_noDataFound = (TextView) fragmentView.findViewById(R.id.tv_noDataFound);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 1);
        rv_projectList.setHasFixedSize(true);
        rv_projectList.setLayoutManager(manager);
        rv_projectList.setItemAnimator(new DefaultItemAnimator());
        ViewGroup.LayoutParams params = rv_projectList.getLayoutParams();
        params.height = 380;
        rv_projectList.setLayoutParams(params);

        getAllArchivedProjects();
    }

    public void updateView() {
        getAllArchivedProjects();
    }

    private void getAllArchivedProjects() {
        progressBarProject.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(getActivity(), FragDashArchivedProject.this, Interactor.RequestCode_GetArchiveProjects, Interactor.Tag_GetArchiveProjects)
                .makeJsonPostRequest(Interactor.Method_GetArchiveProjects, jsonObject, false);
    }

    private void setAllProjects(ArrayList<ProjectBean> projects) {
        if (projects.size() > 0) {
            tv_noDataFound.setVisibility(View.GONE);
            rv_projectList.setVisibility(View.VISIBLE);
            adapterArchivedProject = new AdapterArchivedProject(ApplicationContext.getInstance(), projects, (object, position) -> {
                ProjectBean project = (ProjectBean) object;
                System.out.println("project = [" + project + "], position = [" + position + "]");
                ApplicationContext.getInstance().project = project;
                if (getActivity() instanceof ActivityMain) {
                    String loginType = PrefSetup.getInstance().getUserLoginType();
                    if (loginType.equalsIgnoreCase("d") /*|| loginType.equalsIgnoreCase("c")*/) {
                        ((ActivityMain) getActivity()).replaceFragment(FragmentNames.FragmentArchivedProject, null, false, false);
                    }
//                    else {
//                        ((ActivityMain) getActivity()).replaceFragment(FragmentNames.FragmentCheckList, null, false, false);
//                    }
                }
            });
            rv_projectList.setAdapter(adapterArchivedProject);
        } else {
            tv_noDataFound.setVisibility(View.VISIBLE);
            rv_projectList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        if (getActivity() instanceof ActivityMain) {
            if (responsePacket.getErrorCode() == 410) {
                ((ActivityMain) getActivity()).logoutUser();
                return;
            }
        }
        if (responsePacket.getErrorCode() == 0) {
            if (Interactor.RequestCode_GetArchiveProjects == requestCode) {
                if (responsePacket.getErrorCode() == 0) {
                    progressBarProject.setVisibility(View.GONE);

                    ArrayList<ProjectBean> projectList = new ArrayList<>();
                    for (ProjectBean projectBean : responsePacket.getProjectList()) {
//                    projectBean.setNotificationCounter(dbHelper.getNotificationCounter(projectBean.getProjectId() + "") + "");
                        projectList.add(projectBean);
                    }

                    setAllProjects(projectList);
                    ApplicationContext.getInstance().setProjectList(projectList);
                    tv_noDataFound.setText(responsePacket.getMessage());
                }
            }
        } else {
            Toast.makeText(getActivity(), responsePacket.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {
        if (Interactor.RequestCode_GetArchiveProjects == requestCode) {
            progressBarProject.setVisibility(View.GONE);
        }
    }
}