package com.superapp.fragment.checklist;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.ActivityMain;
import com.superapp.activity.base.ErrorType;
import com.superapp.beans.BeanMaterial;
import com.superapp.beans.BeanSelectCategory;
import com.superapp.beans.CoworkerBean;
import com.superapp.fragment.base.BaseFragment;
import com.superapp.fragment.dashboard.add_forms.FragmentAddCoWorker;
import com.superapp.fragment.dashboard.pager_fragment.coworkers.FragmentDashboardCoWorker;
import com.superapp.utils.PopupUtils;
import com.superapp.utils.PrefSetup;
import com.superapp.utils.ProjectUtils;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.ResponsePacket;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentAddCheckList extends BaseFragment {
    View fragmentView;

    private EditText et_checkListJobType, et_checkListJobCategory, et_checkListCoWorkerName,
            et_suitableDate, et_suitableTime, et_checkListDescription, et_checkListNote;

    private TextInputLayout textInputLayoutCheckListJobType, textInputLayoutCheckListJobCategory,
            textInputLayoutCheckListCoWorkerName, textInputLayoutCheckListDescription,
            textInputLayoutCheckListStartDate, textInputLayoutCheckListEndDate, textInputLayoutCheckListNote;

    private Button bt_checkListSubmit;
    private LinearLayout ll_checkListJobCategory, ll_materialItems, ll_addMaterial;

    long materialCategoryId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentView = inflater.inflate(R.layout.fragment_add_checklist, container, false);
        initView();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActivityMain) activity).setHeaderText("Add Checklist");
        ((ActivityMain) activity).changeHeaderButton(true);
        ((ActivityMain) activity).showHideFloatingButton(false, R.drawable.add);
        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
            ((ActivityMain) activity).showHIdeSearchButton(true);
        } else {
            ((ActivityMain) activity).showHIdeSearchButton(false);
        }
        ((ActivityMain) activity).showAd(false);

        if (coWorker != null) {
            et_checkListCoWorkerName.setText(coWorker.getName());
        }
//        if (materialList != null) {
//            addMaterials();
//        }
    }

    @Override
    public void initView() {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ll_checkListJobCategory = (LinearLayout) setTouchNClick(R.id.ll_checkListJobCategory, fragmentView);
        ll_materialItems = (LinearLayout) setTouchNClick(R.id.ll_materialItems, fragmentView);
        ll_addMaterial = (LinearLayout) setTouchNClick(R.id.ll_addMaterial, fragmentView);

        bt_checkListSubmit = (Button) setTouchNClick(R.id.bt_checkListSubmit, fragmentView);

        et_checkListDescription = (EditText) fragmentView.findViewById(R.id.et_checkListDescription);
//        et_checkListJobType = (EditText) setTouchNClick(R.id.et_checkListJobType, fragmentView);
        et_checkListJobCategory = (EditText) setTouchNClick(R.id.et_checkListJobCategory, fragmentView);
        et_checkListCoWorkerName = (EditText) setTouchNClick(R.id.et_checkListCoWorkerName, fragmentView);
        setTouchNClickSrc(R.id.iv_addMoreCoworker, fragmentView);
        setTouchNClickSrc(R.id.ll_addMaterial, fragmentView);
        et_suitableDate = (EditText) setTouchNClick(R.id.et_suitableDate, fragmentView);
        et_suitableTime = (EditText) setTouchNClick(R.id.et_suitableTime, fragmentView);

//        textInputLayoutCheckListJobType = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutCheckListJobType);
        textInputLayoutCheckListJobCategory = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutCheckListJobCategory);
        textInputLayoutCheckListCoWorkerName = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutCheckListCoWorkerName);
        textInputLayoutCheckListStartDate = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutCheckListStartDate);
        textInputLayoutCheckListEndDate = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutCheckListEndDate);
        textInputLayoutCheckListDescription = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutCheckListDescription);

//        ll_checkListJobCategory.setVisibility(View.GONE);
    }

    private boolean validateFields() {
        if (TextUtils.isEmpty(et_checkListJobCategory.getText().toString())) {
            textInputLayoutCheckListJobCategory.setErrorEnabled(true);
            textInputLayoutCheckListJobCategory.setError(activity.getString(R.string.thisFieldIsRequired));
            et_checkListJobCategory.requestFocus();
            return false;
        } else {
            textInputLayoutCheckListJobCategory.setErrorEnabled(false);
            textInputLayoutCheckListJobCategory.setError(null);
        }

        if (TextUtils.isEmpty(et_checkListCoWorkerName.getText().toString())) {
            textInputLayoutCheckListCoWorkerName.setError(activity.getString(R.string.thisFieldIsRequired));
            textInputLayoutCheckListCoWorkerName.setErrorEnabled(true);
            et_checkListCoWorkerName.requestFocus();
            return false;
        } else {
            textInputLayoutCheckListCoWorkerName.setErrorEnabled(false);
            textInputLayoutCheckListCoWorkerName.setError(null);
        }

        return true;
    }

    CoworkerBean coworkerBean;
    //    Calendar selectedDateTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    Calendar selectedDateTime = Calendar.getInstance();
    boolean isDateSeted, isTimeSeted;

    FragmentAddCoWorker fragmentAddCoworker;

    private void openDatePicker() {
        DatePickerDialog dpd = DatePickerDialog.newInstance((view1, year, monthOfYear, dayOfMonth) -> {
            isDateSeted = true;
            selectedDateTime.set(Calendar.YEAR, year);
            selectedDateTime.set(Calendar.MONTH, monthOfYear);
            selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            et_suitableDate.setText(ProjectUtils.getInstance().getFormattedDate(selectedDateTime));
        }, selectedDateTime.get(Calendar.YEAR), selectedDateTime.get(Calendar.MONTH), selectedDateTime.get(Calendar.DAY_OF_MONTH));
        dpd.setMinDate(Calendar.getInstance());
        dpd.show(activity.getFragmentManager(), "Datepickerdialog");
    }

    private void openTimePicker() {
        TimePickerDialog tpd = TimePickerDialog.newInstance((view1, hourOfDay, minute, second) -> {
            isTimeSeted = true;
            selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            selectedDateTime.set(Calendar.MINUTE, minute);
            et_suitableTime.setText(ProjectUtils.getInstance().getFormattedTime(selectedDateTime));
        }, selectedDateTime.get(Calendar.HOUR_OF_DAY), selectedDateTime.get(Calendar.MINUTE), false);
        tpd.show(activity.getFragmentManager(), "Timepickerdialog");
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.et_suitableDate:
                openDatePicker();
                break;

            case R.id.et_suitableTime:
                if (isDateSeted)
                    openTimePicker();
                else
                    openDatePicker();
                break;

            case R.id.bt_checkListSubmit:
                if (validateFields()) {
                    submitTask();
                }
                break;

            case R.id.et_checkListCoWorkerName:
                if (selectCategory == null) {
                    activity.makeToast(getString(R.string.pleaseSelectCategory));
                    return;
                }
                new FragmentDashboardCoWorker().showCoWorkerPopUp(activity, (object, position) -> {
                    coworkerBean = (CoworkerBean) object;
                    et_checkListCoWorkerName.setText(coworkerBean.getName());
                }, selectCategory, "no");
                break;

            case R.id.iv_addMoreCoworker:
                if (selectCategory == null) {
                    activity.makeToast(getString(R.string.pleaseSelectCategory));
                    return;
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putLong("selectCategoryId", selectCategory.getId());
                    bundle.putString("selectCategoryText", selectCategory.getTitle());

                    FragmentAddCoWorker fragmentAddCoWorker = new FragmentAddCoWorker();
                    fragmentAddCoWorker.setArguments(bundle);
                    fragmentAddCoWorker.setTargetFragment(this, 200);
                    FragmentTransaction transactions = getFragmentManager().beginTransaction();
                    transactions.replace(R.id.main_frame_container, fragmentAddCoWorker);
                    transactions.addToBackStack(null);
                    transactions.commit();
                }
                break;

            case R.id.ll_addMaterial:
                if (!TextUtils.isEmpty(et_checkListCoWorkerName.getText().toString())) {
                    NewAddMaterialPopUp();
                } else {
                    activity.makeToast(activity.getString(R.string.pleaseSelectCoworker));
                }
                break;

            case R.id.et_checkListJobCategory:
                if (selectJobType == null) {
                    activity.makeToast(getString(R.string.pleaseSelectJobTypeFirst));
                    return;
                }

                PopupUtils.getInstance().showOccupationPopUp(activity, selectJobType.getId(), object -> {
                    selectCategory = ((BeanSelectCategory) object);
                    et_checkListJobCategory.setText(selectCategory.getTitle());
                    ll_materialItems.removeAllViews();
                    et_checkListCoWorkerName.setText("");
                }, "Select Category", true);
                break;
        }

    }

    CoworkerBean coWorker;
    ArrayList<JSONObject> materialList = new ArrayList<>();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 200) {
                coWorker = (CoworkerBean) data.getSerializableExtra("coworker");
            } else if (requestCode == 300) {
                try {
                    JSONObject object = new JSONObject(data.getStringExtra("materials"));
                    materialList.add(object);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (fragmentAddCoworker != null) {
            fragmentAddCoworker.onActivityResult(requestCode, resultCode, data);
        }
    }

    BeanSelectCategory selectJobType = new BeanSelectCategory(1, "Labour");
    BeanSelectCategory selectCategory;

    private void submitTask() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
            jsonObject.put("categoryId", 1);
            jsonObject.put("subCategoryId", selectCategory.getId());
            jsonObject.put("subCategoryText", selectCategory.getTitle());
            jsonObject.put("taskDescription", et_checkListDescription.getText().toString());
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("projectId", ApplicationContext.getInstance().project.getProjectId());

            if (materialArrayList != null && materialArrayList.size() > 0) {
                JSONArray arrayMaterial = new JSONArray();
                for (BeanMaterial material : materialArrayList) {
//                    JSONObject jsonMaterial = new JSONObject();
//                    jsonMaterial.put("id", material.getId());
//                    jsonMaterial.put("title", material.getTitle());
//                    arrayMaterial.put(jsonMaterial);
                    arrayMaterial.put(material.getTitle());
                }
                jsonObject.put("materials", arrayMaterial);
            }

            if (isDateSeted) {
                jsonObject.put("suitableDateTime", (selectedDateTime.getTimeInMillis() / 1000));
            } else {
                jsonObject.put("suitableDateTime", 0);
            }

            if (coworkerBean != null)
                jsonObject.put("coworkerId", coworkerBean.getId());
            else
                jsonObject.put("coworkerId", 0);
            JSONObject coworkerDetail = new JSONObject();
            if (coWorker != null) {

                coworkerDetail.put("occupationId", coWorker.getOccupationId());
                coworkerDetail.put("occupation", coWorker.getOccupation());
                coworkerDetail.put("mobile", coWorker.getMobile());
                coworkerDetail.put("name", coWorker.getName());
                coworkerDetail.put("email", coWorker.getEmail());
                coworkerDetail.put("address", coWorker.getAddress());
                coworkerDetail.put("nativeAddress", coWorker.getNativeAddress() != null ? coWorker.getNativeAddress() : "");
                coworkerDetail.put("panNumber", coWorker.getPanNumber() != null ? coWorker.getPanNumber() : "");
                coworkerDetail.put("referredBy", coWorker.getReferredBy() != null ? coWorker.getReferredBy() : "");
                coworkerDetail.put("rating", coWorker.getRating() != null ? coWorker.getRating() : "");
                coworkerDetail.put("userId", PrefSetup.getInstance().getUserId());
                coworkerDetail.put("loginType", ApplicationContext.getInstance().project.getProjectId());
            }
            jsonObject.put("coworkerDetail", coworkerDetail);


        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(activity, FragmentAddCheckList.this, Interactor.RequestCode_SubmitChecklist, Interactor.Tag_SubmitChecklist)
                .makeJsonPostRequest(Interactor.Method_SubmitChecklist, jsonObject, true);
    }

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
        if (responsePacket.getErrorCode() == 410) {
            return;
        }

        activity.makeToast(responsePacket.getMessage());

        if (responsePacket.getErrorCode() == 0) {
            if (Interactor.RequestCode_SubmitChecklist == requestCode) {
                activity.onBackPressed();
            }
        } else {
            Toast.makeText(getActivity(), responsePacket.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {
        super.onError(requestCode, errorType);
    }

    ArrayList<BeanMaterial> materials = new ArrayList<>();
    private LayoutInflater inflater;
    EditText et_addMaterial;

    public void NewAddMaterialPopUp() {
        Dialog newAddMaterialDialog = new Dialog(activity);
        newAddMaterialDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.popup_add_material_textbox, null, false);
        newAddMaterialDialog.setContentView(view);
        newAddMaterialDialog.show();
        newAddMaterialDialog.show();
        final ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(v -> newAddMaterialDialog.dismiss());

        final Window window = newAddMaterialDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final EditText et_addMaterial = (EditText) view.findViewById(R.id.et_addMaterial);

        Button btn_popUpAddMaterialSubmit = (Button) view.findViewById(R.id.btn_popUpAddMaterialSubmit);

        btn_popUpAddMaterialSubmit.setOnClickListener(v -> {
            if (TextUtils.isEmpty(et_addMaterial.getText().toString())) {
                et_addMaterial.setError(activity.getString(R.string.requiredField));
                et_addMaterial.requestFocus();
                return;
            }
            addMaterials(et_addMaterial.getText().toString());
            newAddMaterialDialog.dismiss();
        });
        newAddMaterialDialog.show();
    }

    List<BeanMaterial> materialArrayList;

    private void addMaterials(String newMaterial) {
        try {
            if (materialArrayList == null) {
                materialArrayList = new ArrayList<>();
            }
            if (newMaterial != null)
                materialArrayList.add(new BeanMaterial(0, newMaterial));

            ll_materialItems.removeAllViews();
            for (BeanMaterial material : materialArrayList) {
                View materialItem = inflater.inflate(R.layout.fragment_add_checklist_material_item_old, null);
                ((TextView) materialItem.findViewById(R.id.tv_materialItem)).setText(material.getTitle());

                materialItem.findViewById(R.id.iv_delete).setVisibility(View.VISIBLE);
                materialItem.findViewById(R.id.iv_delete).setOnClickListener(view -> {
                    materialArrayList.remove(material);
                    addMaterials(null);
                });

                ll_materialItems.addView(materialItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}