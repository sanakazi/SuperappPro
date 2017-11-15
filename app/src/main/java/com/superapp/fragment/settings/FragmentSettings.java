package com.superapp.fragment.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.ActivityChangeMNumber;
import com.superapp.activity.ActivityChangePassword;
import com.superapp.activity.ActivityMain;
import com.superapp.activity.base.ErrorType;
import com.superapp.activity.base.OnItemClickListener;
import com.superapp.activity.base.OnViewClickListener;
import com.superapp.beans.ProjectBean;
import com.superapp.beans.SettingsBean;
import com.superapp.fragment.base.BaseFragment;
import com.superapp.utils.PopupUtils;
import com.superapp.utils.PrefSetup;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.OnResponseListener;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;

public class FragmentSettings extends BaseFragment {
    private View fragmentView;

    RecyclerView rv_settingsList;
    AdapterSetting adapterSetting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentView = inflater.inflate(R.layout.fragment_settings, container, false);
        initView();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
//        ((ActivityMain) activity).setHeaderText("Settings");
        ((ActivityMain) activity).setHeaderText("Projects");
        ((ActivityMain) activity).changeHeaderButton(true);
        ((ActivityMain) activity).showHideFloatingButton(false, R.drawable.add);

        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
            ((ActivityMain) activity).showHIdeSearchButton(true);
        } else {
            ((ActivityMain) activity).showHIdeSearchButton(false);
        }
        ((ActivityMain) activity).showAd(false);
    }

    TextView tv_noDataFound, tv_changePassword, tv_changeMobileNumber;

    @Override
    public void initView() {

        tv_noDataFound = (TextView) fragmentView.findViewById(R.id.tv_noDataFound);
        rv_settingsList = (RecyclerView) fragmentView.findViewById(R.id.rv_settingsList);
        GridLayoutManager manager = new GridLayoutManager(ApplicationContext.getInstance(), 1);
        rv_settingsList.setHasFixedSize(true);
        rv_settingsList.setLayoutManager(manager);
        rv_settingsList.setItemAnimator(new DefaultItemAnimator());
        tv_changePassword = (TextView) setTouchNClick(R.id.tv_changePassword, fragmentView);
        tv_changeMobileNumber = (TextView) setTouchNClick(R.id.tv_changeMobileNumber, fragmentView);

        setAllProjectsList();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_changePassword:
                Intent i = new Intent(getActivity(), ActivityChangePassword.class);
                startActivity(i);
                break;

            case R.id.tv_changeMobileNumber:
                Intent i1 = new Intent(activity, ActivityChangeMNumber.class);
                activity.startActivity(i1);
                break;
        }
    }

    private void setAllProjectsList() {
        if (ApplicationContext.getInstance().getProjectList().size() > 0) {
            rv_settingsList.setVisibility(View.VISIBLE);
            tv_noDataFound.setVisibility(View.GONE);
            adapterSetting = new AdapterSetting(activity, ApplicationContext.getInstance().getProjectList(), new OnItemClickListener() {
                @Override
                public void onItemClick(Object object, int position) {
                    ProjectBean projectBean = (ProjectBean) object;
                    deleteProject(projectBean);
                }
            });
            rv_settingsList.setAdapter(adapterSetting);
        } else {
            rv_settingsList.setVisibility(View.GONE);
            tv_noDataFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
        if (getActivity() instanceof ActivityMain) {
            if (responsePacket.getErrorCode() == 410) {
                return;
            }
        }
        if (responsePacket.getErrorCode() == 0) {
        } else {
            Toast.makeText(getActivity(), responsePacket.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {
        super.onError(requestCode, errorType);
    }

    View dialogView;

    public void deleteProject(final ProjectBean projectBean) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) ApplicationContext.getInstance()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialogView = inflater.inflate(R.layout.setting_dialogbox, null, false);
        dialogBuilder.setView(dialogView);
        final AlertDialog dialog = dialogBuilder.show();
        Button bt_execute = (Button) dialogView.findViewById(R.id.bt_execute);
        final RadioButton checkBox_deleteFull = (RadioButton) dialogView.findViewById(R.id.radio_deleteFull);
        final RadioButton checkBox_isArchive = (RadioButton) dialogView.findViewById(R.id.radio_isArchive);
        TextView tv_projectName = (TextView) dialogView.findViewById(R.id.tv_projectName);

        tv_projectName.setText(projectBean.getProjectName());
        final ImageView iv_close = (ImageView) dialogView.findViewById(R.id.iv_close);

        iv_close.setOnClickListener(v -> dialog.dismiss());
        bt_execute.setOnClickListener(v -> {
            if (checkBox_deleteFull.isChecked()) {
                PopupUtils.getInstance().showYesNoDialog(activity, "Delete Project", "Are you want to sure delete project?", new OnViewClickListener() {
                    @Override
                    public void onViewItemClick(Object object) {
                        deleteProjects(true, projectBean);
                    }
                });

            } else if (checkBox_isArchive.isChecked()) {
                PopupUtils.getInstance().showYesNoDialog(activity, "Archive Project", "Are you want to sure archive project?", new OnViewClickListener() {
                    @Override
                    public void onViewItemClick(Object object) {
                        deleteProjects(false, projectBean);
                    }
                });
            }
            dialog.dismiss();
        });
    }

    SettingsBean settingsBean;

    public void deleteProjects(boolean isDelete, final ProjectBean projectBean) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("projectId", projectBean.getProjectId());
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            if (isDelete)
                jsonObject.put("deleteFullProject", "t");
            else
                jsonObject.put("isArchive", "t");

//            jsonObject.put("deleteFullProject", settingsBean.getDeleteFullProject());

        } catch (Exception e) {
            e.printStackTrace();
        }

        new InteractorImpl(activity, new OnResponseListener() {
            @Override
            public void onSuccess(int requestCode, ResponsePacket responsePacket) {
                if (responsePacket.getErrorCode() == 0) {
                    if (ApplicationContext.getInstance().getProjectList() != null) {
                        ApplicationContext.getInstance().getProjectList().remove(projectBean);
                        setAllProjectsList();
                    }
                }
            }

            @Override
            public void onError(int requestCode, ErrorType errorType) {

            }
        }, Interactor.RequestCode_DeleteProject, Interactor.Tag_DeleteProject)
                .makeJsonPostRequest(Interactor.Method_DeleteProject, jsonObject, true);
    }

//    public void changePassword() {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
//            jsonObject.put("password", et_newPassword.getText().toString());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        new InteractorImpl(activity, new OnResponseListener() {
//            @Override
//            public void onSuccess(int requestCode, ResponsePacket responsePacket) {
//                if (responsePacket.getErrorCode() == 0) {
//                    activity.makeToast(responsePacket.getMessage());
//                }
//            }
//
//            @Override
//            public void onError(int requestCode, ErrorType errorType) {
//
//            }
//        }, Interactor.RequestCode_ChangePassword, Interactor.Tag_ChangePassword)
//                .makeJsonPostRequest(Interactor.Method_ChangePassword, jsonObject, true);
//    }

//    EditText et_confirmPassword, et_newPassword;
//
//    public void setChangePassword() {
//
//        AlertDialog.Builder changePassword = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.popupwindow_changepassword, null, false);
//        changePassword.setView(view);
//        final AlertDialog dialog = changePassword.show();
//
//        et_confirmPassword = (EditText) view.findViewById(R.id.et_confirmPassword);
//        et_newPassword = (EditText) view.findViewById(R.id.et_newPassword);
//
//        et_newPassword.requestFocus();
//        Utilities.getInstance().showKeyboard(activity);
//        et_newPassword.requestFocus();
//
//        Button bt_update = (Button) view.findViewById(R.id.bt_update);
//
//        final ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
//
//        iv_close.setOnClickListener(v -> dialog.dismiss());
//        bt_update.setOnClickListener(v -> {
//
//            if (et_newPassword.getText().toString().trim().equals("")) {
//                et_newPassword.setError("This field is required");
//                et_newPassword.requestFocus();
//                return;
//            } else {
//                et_newPassword.setError(null);
//            }
//
//            if (et_confirmPassword.getText().toString().trim().equals("")) {
//                et_confirmPassword.setError("This field is required");
//                et_confirmPassword.requestFocus();
//                return;
//            } else {
//                et_confirmPassword.setError(null);
//            }
//            if (!et_confirmPassword.getText().toString().trim().equals(et_newPassword.getText().toString().trim())) {
//                et_confirmPassword.setError(getString(R.string.noMatchPassword));
//                et_confirmPassword.requestFocus();
//                return;
//            } else {
//                et_confirmPassword.setError(null);
//            }
//            changePassword();
//            dialog.dismiss();
//        });
//    }


}
