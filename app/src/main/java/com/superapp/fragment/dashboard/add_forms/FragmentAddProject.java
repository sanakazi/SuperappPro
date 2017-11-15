
package com.superapp.fragment.dashboard.add_forms;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.superapp.R;
import com.superapp.activity.base.ErrorType;
import com.superapp.activity.ActivityMain;
import com.superapp.beans.BeanAddProject;
import com.superapp.beans.BeanAddress;
import com.superapp.beans.ClientBean;
import com.superapp.fragment.base.BaseFragment;
import com.superapp.fragment.base.FragmentNames;
import com.superapp.fragment.dashboard.add_forms.coworker_occupation.BeanOccupation;
import com.superapp.fragment.dashboard.pager_fragment.client.FragmentDashboardClient;
import com.superapp.utils.PopupUtils;
import com.superapp.utils.PrefSetup;
import com.superapp.utils.ProjectUtils;
import com.superapp.utils.Utilities;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.ResponsePacket;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONObject;

import java.util.Calendar;

public class FragmentAddProject extends BaseFragment {

    public View fragmentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentView = inflater.inflate(R.layout.fragment_add_project, container, false);
        initView();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (activity instanceof ActivityMain) {
            ((ActivityMain) activity).setHeaderText(getString(R.string.createProject));
            ((ActivityMain) activity).showHideFloatingButton(false, R.drawable.add);
            ((ActivityMain) activity).changeHeaderButton(true);
            ((ActivityMain) activity).showAd(false);
        }
        updateView();
    }

    @Override
    public void updateView() {
        super.updateView();
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private EditText et_projectSetBudget, et_projectHandoverDate,
            et_projectExecutionStartDate, et_projectDesignStartDate,
            et_projectTypeOfProject, et_projectLocation, et_projectProjectName, et_projectSelectClient;
    private TextInputLayout textInputLayoutProjectProjectName, textInputLayoutProjectLocation, textInputLayoutProjectTypeOfProject,
            textInputLayoutProjectDesignStartDate, textInputLayoutProjectExecutionStartDate,
            textInputLayoutProjectHandoverDate, textInputLayoutProjectSetBudget, textInputLayoutProjectSelectClient;
    private Button bt_addProject;
    private ImageView iv_addProject;
    private CheckBox checkbox_spentMoneyToggleForClient;

    @Override
    public void initView() {
        iv_addProject = (ImageView) setTouchNClick(R.id.iv_addProject, fragmentView);
        setClick(R.id.ll_addProject, fragmentView);
        et_projectProjectName = (EditText) fragmentView.findViewById(R.id.et_projectProjectName);
        et_projectLocation = (EditText) setTouchNClick(R.id.et_projectLocation, fragmentView);
        et_projectTypeOfProject = (EditText) setTouchNClick(R.id.et_projectTypeOfProject, fragmentView);
        et_projectDesignStartDate = (EditText) setTouchNClick(R.id.et_projectDesignStartDate, fragmentView);
        et_projectExecutionStartDate = (EditText) setTouchNClick(R.id.et_projectExecutionStartDate, fragmentView);
        et_projectHandoverDate = (EditText) setTouchNClick(R.id.et_projectHandoverDate, fragmentView);
        et_projectSetBudget = (EditText) fragmentView.findViewById(R.id.et_projectSetBudget);
        et_projectSelectClient = (EditText) setTouchNClick(R.id.et_projectSelectClient, fragmentView);
        setTouchNClickSrc(R.id.iv_addMoreClient, fragmentView);
        textInputLayoutProjectSelectClient = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutProjectSelectClient);
        textInputLayoutProjectProjectName = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutProjectProjectName);
        textInputLayoutProjectLocation = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutProjectLocation);
        textInputLayoutProjectTypeOfProject = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutProjectTypeOfProject);
        textInputLayoutProjectDesignStartDate = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutProjectDesignStartDate);
        textInputLayoutProjectExecutionStartDate = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutProjectExecutionStartDate);
        textInputLayoutProjectHandoverDate = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutProjectHandoverDate);
        textInputLayoutProjectSetBudget = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutProjectSetBudget);

        checkbox_spentMoneyToggleForClient = (CheckBox) fragmentView.findViewById(R.id.checkbox_spentMoneyToggleForClient);

        et_projectProjectName.requestFocus();
        Utilities.getInstance().showKeyboard(activity);
        et_projectProjectName.requestFocus();
    }

    private boolean validateFields() {
        if (TextUtils.isEmpty(et_projectProjectName.getText().toString())) {
            textInputLayoutProjectProjectName.setErrorEnabled(true);
            textInputLayoutProjectProjectName.setError(getString(R.string.projectNameRequired));
            et_projectProjectName.requestFocus();
            return false;
        } else {
            textInputLayoutProjectProjectName.setErrorEnabled(false);
            textInputLayoutProjectProjectName.setError(null);
        }
        if (TextUtils.isEmpty(et_projectLocation.getText().toString())) {
            textInputLayoutProjectLocation.setErrorEnabled(true);
            textInputLayoutProjectLocation.setError(getString(R.string.locationRequired));
            et_projectLocation.requestFocus();
            return false;
        } else {
            textInputLayoutProjectLocation.setErrorEnabled(false);
            textInputLayoutProjectLocation.setError(null);
        }

        if (TextUtils.isEmpty(et_projectTypeOfProject.getText().toString())) {
            textInputLayoutProjectTypeOfProject.setErrorEnabled(true);
            textInputLayoutProjectTypeOfProject.setError(getString(R.string.selectTypeOfProjectRequired));
            et_projectTypeOfProject.requestFocus();
            return false;
        } else {
            textInputLayoutProjectTypeOfProject.setErrorEnabled(false);
            textInputLayoutProjectTypeOfProject.setError(null);
        }

        if (TextUtils.isEmpty(et_projectDesignStartDate.getText().toString())) {
            textInputLayoutProjectDesignStartDate.setErrorEnabled(true);
            textInputLayoutProjectDesignStartDate.setError(getString(R.string.designStartDateRequired));
            et_projectDesignStartDate.requestFocus();
            return false;
        } else {
            textInputLayoutProjectDesignStartDate.setErrorEnabled(false);
            textInputLayoutProjectDesignStartDate.setError(null);
        }
        if (TextUtils.isEmpty(et_projectExecutionStartDate.getText().toString())) {
            textInputLayoutProjectExecutionStartDate.setErrorEnabled(true);
            textInputLayoutProjectExecutionStartDate.setError(getString(R.string.executionStartDateRequired));
            et_projectExecutionStartDate.requestFocus();
            return false;
        } else {
            textInputLayoutProjectExecutionStartDate.setErrorEnabled(false);
            textInputLayoutProjectExecutionStartDate.setError(null);
        }
        if (TextUtils.isEmpty(et_projectHandoverDate.getText().toString().trim())) {
            textInputLayoutProjectHandoverDate.setErrorEnabled(true);
            textInputLayoutProjectHandoverDate.setError(getString(R.string.estimatedStartDateRequired));
            et_projectHandoverDate.requestFocus();
            return false;
        } else {
            textInputLayoutProjectHandoverDate.setErrorEnabled(false);
            textInputLayoutProjectHandoverDate.setError(null);
        }
        if (TextUtils.isEmpty(et_projectSetBudget.getText().toString().trim())) {
            textInputLayoutProjectSetBudget.setErrorEnabled(true);
            textInputLayoutProjectSetBudget.setError(getString(R.string.setBudgetRequired));
            et_projectSetBudget.requestFocus();
            return false;
        } else {
            textInputLayoutProjectSetBudget.setErrorEnabled(false);
            textInputLayoutProjectSetBudget.setError(null);
        }

        if (clientBean == null) {
            activity.makeToast(getString(R.string.selectClientError));
            showClientListPopup();
            return false;
        }
        return true;
    }

    private BeanAddress projectLocation;
    ClientBean clientBean;
    Calendar designStartDate, executionStartDate, handoverDate;

    @Override
    public void onClick(View view) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = null;
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_addProject:
            case R.id.ll_addProject:
                if (validateFields()) {
                    addProject();
                }
                break;

            case R.id.et_projectTypeOfProject:
                showProjectTypePopUp(activity);
                break;

            case R.id.et_projectLocation:
                PopupUtils.getInstance().showClientAddressPopup(activity, object -> {
                    projectLocation = (BeanAddress) object;
                    et_projectLocation.setText(projectLocation.toString());
                }, projectLocation, "Location");
                break;

            case R.id.et_projectSelectClient:
                showClientListPopup();
                break;

            case R.id.iv_addMoreClient:
                new FragmentAddClient().showAddClientPopUp(activity, (object, position) -> {
                    clientBean = (ClientBean) object;
                    et_projectSelectClient.setText(clientBean != null ? clientBean.getName() : "");
                }, null);
//                ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentAddClient, null, false, false);
                break;

            case R.id.et_projectDesignStartDate:
                if (designStartDate != null)
                    now = designStartDate;
                dpd = DatePickerDialog.newInstance((view1, year, monthOfYear, dayOfMonth) -> {
                    designStartDate = Calendar.getInstance();
                    designStartDate.set(Calendar.YEAR, year);
                    designStartDate.set(Calendar.MONTH, monthOfYear);
                    designStartDate.set(Calendar.DATE, dayOfMonth);
                    et_projectDesignStartDate.setText(ProjectUtils.getInstance().getFormattedDate(designStartDate));

                    executionStartDate = null;
                    et_projectExecutionStartDate.setText("");
                    handoverDate = null;
                    et_projectHandoverDate.setText("");
                }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
                dpd.show(activity.getFragmentManager(), "Timepickerdialog");
                break;

            case R.id.et_projectExecutionStartDate:
                if (designStartDate == null) {
                    activity.makeToast(activity.getString(R.string.pleaseSelectDesignStartDateFirst));
                    return;
                }
                if (executionStartDate != null)
                    now = executionStartDate;
                dpd = DatePickerDialog.newInstance((view1, year, monthOfYear, dayOfMonth) -> {
                    executionStartDate = Calendar.getInstance();
                    executionStartDate.set(Calendar.YEAR, year);
                    executionStartDate.set(Calendar.MONTH, monthOfYear);
                    executionStartDate.set(Calendar.DATE, dayOfMonth);
                    et_projectExecutionStartDate.setText(ProjectUtils.getInstance().getFormattedDate(executionStartDate));

                    handoverDate = null;
                    et_projectHandoverDate.setText("");
                }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
                dpd.setMinDate(designStartDate);
                dpd.show(activity.getFragmentManager(), "Timepickerdialog");
                break;

            case R.id.et_projectHandoverDate:
                if (executionStartDate == null) {
                    activity.makeToast(activity.getString(R.string.pleaseSelectExecutionStartDateFirst));
                    return;
                }
                if (handoverDate != null)
                    now = handoverDate;
                dpd = DatePickerDialog.newInstance((view1, year, monthOfYear, dayOfMonth) -> {
                    handoverDate = Calendar.getInstance();
                    handoverDate.set(Calendar.YEAR, year);
                    handoverDate.set(Calendar.MONTH, monthOfYear);
                    handoverDate.set(Calendar.DATE, dayOfMonth);
                    et_projectHandoverDate.setText(ProjectUtils.getInstance().getFormattedDate(handoverDate));
                }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
                dpd.setMinDate(executionStartDate);
                dpd.show(activity.getFragmentManager(), "Timepickerdialog");
                break;
        }
    }

    private void showClientListPopup() {
        new FragmentDashboardClient().showClientPopUp(activity, (object, position) -> {
            clientBean = (ClientBean) object;
            et_projectSelectClient.setText(clientBean.getName());
        }, "no");
    }

    BeanOccupation occupation;
    PopupWindow projectTypePopUp, projectPopUpLocation;
    private TextView tv_projectTypeCommercial, tv_projectTypeResidential;

    private void showProjectTypePopUp(final Context context) {
        final Dialog projectTypeDialog = new Dialog(context);
        projectTypeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_projecttype, null, false);
        projectTypeDialog.setContentView(view);
        final Window window = projectTypeDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        projectTypeDialog.show();

        final ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);

        iv_close.setOnClickListener(v -> projectTypeDialog.dismiss());

        tv_projectTypeCommercial = (TextView) view.findViewById(R.id.tv_projectTypeCommercial);
        tv_projectTypeCommercial.setOnClickListener(v -> {
            et_projectTypeOfProject.setText(tv_projectTypeCommercial.getText().toString());
            projectTypeDialog.dismiss();
        });
        tv_projectTypeResidential = (TextView) view.findViewById(R.id.tv_projectTypeResidential);
        tv_projectTypeResidential.setOnClickListener(v -> {
            et_projectTypeOfProject.setText(tv_projectTypeResidential.getText().toString());
            projectTypeDialog.dismiss();
        });
    }

    public void addProject() {
        BeanAddProject addProject = new BeanAddProject();
        addProject.setUserId(PrefSetup.getInstance().getUserId());
        addProject.setLocation(projectLocation);
        addProject.setProjectName(et_projectProjectName.getText().toString());
        addProject.setTypeOfProject(et_projectTypeOfProject.getText().toString());

        addProject.setSpentMoneyToggle(checkbox_spentMoneyToggleForClient.isChecked() ? "t" : "f");

        ProjectUtils.getInstance().resetTimeTo00(designStartDate);
        addProject.setDesignStartDate((designStartDate.getTimeInMillis() / 1000) + "");

        ProjectUtils.getInstance().resetTimeTo00(executionStartDate);
        addProject.setExecutionStartDate((executionStartDate.getTimeInMillis() / 1000) + "");

        ProjectUtils.getInstance().resetTimeTo00(handoverDate);
        addProject.setEstimatedStartDate((handoverDate.getTimeInMillis() / 1000) + "");
        addProject.setBudget(et_projectSetBudget.getText().toString());
        addProject.setClientId(clientBean.getId());
        try {
            new InteractorImpl(activity, FragmentAddProject.this, Interactor.RequestCode_AddProject, Interactor.Tag_AddProject)
                    .makeJsonPostRequest(Interactor.Method_AddProject, new JSONObject(new Gson().toJson(addProject)), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
        if (responsePacket.getStatus().equalsIgnoreCase("success")) {
            if (Interactor.RequestCode_AddProject == requestCode) {
                if (activity instanceof ActivityMain)
                    ((ActivityMain) activity).directBack = true;
                activity.onBackPressed();
            }

        }
        activity.makeToast(responsePacket.getMessage());
    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {
        super.onError(requestCode, errorType);
        if (requestCode == Interactor.RequestCode_AddProject) {

        }
    }

}
