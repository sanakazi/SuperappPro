package com.superapp.fragment.schedule;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.ActivityMain;
import com.superapp.activity.base.ErrorType;
import com.superapp.beans.BeanScheduleCategory;
import com.superapp.beans.ScheduleBean;
import com.superapp.fragment.base.BaseFragment;
import com.superapp.swipe.Attributes;
import com.superapp.utils.PopupUtils;
import com.superapp.utils.PrefSetup;
import com.superapp.utils.ProjectUtils;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.OnResponseListener;
import com.superapp.webservice.ResponsePacket;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class FragmentSchedule extends BaseFragment {

    View fragmentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentView = inflater.inflate(R.layout.fragment_schedule, container, false);
        initView();

        return fragmentView;
    }

    RecyclerView rv_schedule;
    AdapterSchedule adapterSchedule;
    TextView tv_noDataFound;

    @Override
    public void initView() {
        tv_noDataFound = (TextView) fragmentView.findViewById(R.id.tv_noDataFound);
        rv_schedule = (RecyclerView) fragmentView.findViewById(R.id.rv_scheduleList);
        GridLayoutManager manager = new GridLayoutManager(ApplicationContext.getInstance(), 1);
        rv_schedule.setHasFixedSize(true);
        rv_schedule.setLayoutManager(manager);
        rv_schedule.setItemAnimator(new DefaultItemAnimator());
        getAllScheduleList();
    }

//    private void setSchedule(ArrayList<ScheduleBean> scheduleBean) {
//        if (scheduleBean.size() > 0) {
//            rv_schedule.setVisibility(View.VISIBLE);
//            tv_noDataFound.setVisibility(View.GONE);
//            adapterSchedule = new AdapterSchedule(activity, scheduleBean, (object, taskType) -> {
//                if (taskType == 0) {
//                    showScheduleItemPopUp((ScheduleBean) object, "Edit Schedule");
//                } else if (taskType == 2) {
//                    PopupUtils.getInstance().showYesNoDialog(activity, "Delete  Schedule", "Do you want to delete schedule?", object1 ->
//                            deleteSchedule(((ScheduleBean) object).getScheduleId()));
//                } else {
//                    updateScheduledStatus((ScheduleBean) object);
//                }
//            });
//            rv_schedule.setAdapter(adapterSchedule);
//        } else {
//            rv_schedule.setVisibility(View.GONE);
//            tv_noDataFound.setVisibility(View.VISIBLE);
//        }
//    }

    boolean addingClientWithResponse = false;

    private void setSchedule(ArrayList<ScheduleBean> scheduleBean) {
        if (scheduleBean.size() > 0) {
            rv_schedule.setVisibility(View.VISIBLE);
            tv_noDataFound.setVisibility(View.GONE);
            ScheduleListAdapter adapterSchedule = new ScheduleListAdapter(activity, scheduleBean, !addingClientWithResponse, (object, taskType) -> {
                if (taskType == 0) {
                    showScheduleItemPopUp((ScheduleBean) object, "Edit Schedule");
                } else if (taskType == 2) {
                    PopupUtils.getInstance().showYesNoDialog(activity, "Delete  Schedule", "Do you want to delete schedule?", object1 ->
                            deleteSchedule(((ScheduleBean) object).getScheduleId()));
                } else {
                    updateScheduledStatus((ScheduleBean) object);
                }
            });
            ((ScheduleListAdapter) adapterSchedule).setMode(Attributes.Mode.Single);
            rv_schedule.setAdapter(adapterSchedule);
        } else {
            rv_schedule.setVisibility(View.GONE);
            tv_noDataFound.setVisibility(View.VISIBLE);
        }
    }


    public void deleteSchedule(Long id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("scheduleId", id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new InteractorImpl(activity, new OnResponseListener() {
            @Override
            public void onSuccess(int requestCode, ResponsePacket responsePacket) {
                if (responsePacket.getErrorCode() == 0) {

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(FragmentSchedule.this).attach(FragmentSchedule.this).commit();

                    Toast.makeText(activity, responsePacket.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(int requestCode, ErrorType errorType) {

            }
        }, Interactor.RequestCode_DeleteSchedule, Interactor.Tag_DeleteSchedule)
                .makeJsonPostRequest(Interactor.Method_DeleteSchedule, jsonObject, true);
    }

    public void getAllScheduleList() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("projectId", ApplicationContext.getInstance().project.getProjectId());

        } catch (Exception e) {
            e.printStackTrace();
        }

        new InteractorImpl(activity, FragmentSchedule.this, Interactor.RequestCode_GetAllScheduleList, Interactor.Tag_GetAllScheduleList)
                .makeJsonPostRequest(Interactor.Method_GetAllScheduleList, jsonObject, true);
    }

    public void updateScheduledStatus(ScheduleBean objBean) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("projectId", ApplicationContext.getInstance().project.getProjectId());
            jsonObject.put("scheduledId", objBean.getScheduleId() + "");
            jsonObject.put("status", "Completed");
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(activity, FragmentSchedule.this, Interactor.RequestCode_UpdateScheduleStatus, Interactor.Tag_UpdateScheduleStatus)
                .makeJsonPostRequest(Interactor.Method_UpdateScheduleStatus, jsonObject, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActivityMain) activity).setHeaderText("Schedule");
        ((ActivityMain) activity).changeHeaderButton(true);
        ((ActivityMain) activity).showHideFloatingButton(true, R.drawable.add);
        if (ApplicationContext.getInstance().project.getIsComplete().equalsIgnoreCase("f")) {
            ((ActivityMain) activity).showHideFloatingButton(true, R.drawable.add);
        } else if (ApplicationContext.getInstance().project.getIsComplete().equalsIgnoreCase("t")) {
            ((ActivityMain) activity).showHideFloatingButton(false, R.drawable.add);
        }
        ((ActivityMain) activity).showAd(false);
    }

    private void editSchedule() {
//        et_pwScheduleDescription.setText(PrefSetup.getInstance().getSCHEDULE_DESCRIPTION());

//        if (!TextUtils.isEmpty(PrefSetup.getInstance().getSCHEDULE_END_DATE_TIME())) {
//            Calendar dateOfBirth = Calendar.getInstance();
//            dateOfBirth.setTimeInMillis(Long.parseLong(PrefSetup.getInstance().getSCHEDULE_END_DATE_TIME()) * 1000);
//            et_pwScheduleEndTime.setText(ProjectUtils.getInstance().getFormattedDate(dateOfBirth));
//        }
    }

    Spinner spinner_pwScheduleLineOfWork, spinner_pwScheduleFurtherCategories;
    EditText et_pwScheduleDescription, et_pwScheduleEndDate/*, et_pwScheduleEndTime*/,
            et_StartTime, et_endTime, et_endDate, et_StartDate;
    TextView tv_setTitle;
    Button bt_ScheduleSubmit;
    Calendar startDateTime, endDateTime/*,startTime,endTime*/; /*= Calendar.getInstance();*/
    boolean isStartDateSated, isStartTimeSated, isEndDateSated, isEndTimeSated;
    Dialog scheduleItemDialog;
    LinearLayout ll_otherCat, ll_otherSubCat;
    EditText et_otherCategory, et_otherSubCategory;

    DatePickerDialog dpd;
    Calendar now = Calendar.getInstance();

    private void showScheduleItemPopUp(final ScheduleBean scheduleBean, String title) {

        scheduleItemDialog = new Dialog(activity);
        scheduleItemDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_schedule, null, false);
        scheduleItemDialog.setContentView(view);
        final Window window = scheduleItemDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        scheduleItemDialog.show();

        tv_setTitle = (TextView) view.findViewById(R.id.tv_setTitle);
        tv_setTitle.setText(title);
        spinner_pwScheduleLineOfWork = (Spinner) view.findViewById(R.id.spinner_ScheduleLineOfWork);
        spinner_pwScheduleFurtherCategories = (Spinner) view.findViewById(R.id.spinner_ScheduleFurtherCategories);
        et_pwScheduleEndDate = (EditText) view.findViewById(R.id.et_ScheduleEndDate);
//        et_pwScheduleEndTime = (EditText) view.findViewById(R.id.et_ScheduleEndTime);
        et_pwScheduleDescription = (EditText) view.findViewById(R.id.et_ScheduleDescription);
        et_StartTime = (EditText) view.findViewById(R.id.et_startTime);
        et_StartDate = (EditText) view.findViewById(R.id.et_StartDate);
        et_endTime = (EditText) view.findViewById(R.id.et_endTime);
        et_endDate = (EditText) view.findViewById(R.id.et_endDate);

        ll_otherCat = (LinearLayout) view.findViewById(R.id.ll_otherCat);
        et_otherCategory = (EditText) view.findViewById(R.id.et_otherCategory);

        ll_otherSubCat = (LinearLayout) view.findViewById(R.id.ll_otherSubCat);
        et_otherSubCategory = (EditText) view.findViewById(R.id.et_otherSubCategory);

//        et_pwScheduleDescription.requestFocus();
//        Utilities.getInstance().showKeyboard(activity);
//        et_pwScheduleDescription.requestFocus();

        bt_ScheduleSubmit = (Button) view.findViewById(R.id.bt_ScheduleSubmit);
        final ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);

        iv_close.setOnClickListener(v -> scheduleItemDialog.dismiss());

        et_StartDate.setOnClickListener(v -> {
            startDateTime = Calendar.getInstance();
            dpd = DatePickerDialog.newInstance((view1, year, monthOfYear, dayOfMonth) -> {
                        startDateTime.set(Calendar.YEAR, year);
                        startDateTime.set(Calendar.MONTH, monthOfYear);
                        startDateTime.set(Calendar.DATE, dayOfMonth);
                        et_StartDate.setText(ProjectUtils.getInstance().getFormattedDate(startDateTime));
                        isStartDateSated = true;
                        endDateTime = null;
                        et_StartTime.setText("");
                        et_endDate.setText("");
                        et_endTime.setText("");
                    },
                    startDateTime.get(Calendar.YEAR), startDateTime.get(Calendar.MONTH), startDateTime.get(Calendar.DAY_OF_MONTH));
            dpd.setMinDate(Calendar.getInstance());

            dpd.show(activity.getFragmentManager(), "DatePickerDialog");
        });

        et_endDate.setOnClickListener(v -> {
            if (startDateTime == null) {
                activity.makeToast(activity.getString(R.string.pleaseSelectStartDateFirst));
                return;
            }
            if (endDateTime != null)
                now = endDateTime;
            dpd = DatePickerDialog.newInstance((view1, year, monthOfYear, dayOfMonth) -> {
                        endDateTime = Calendar.getInstance();
                        endDateTime.set(Calendar.YEAR, year);
                        endDateTime.set(Calendar.MONTH, monthOfYear);
                        endDateTime.set(Calendar.DATE, dayOfMonth);
                        et_endDate.setText(ProjectUtils.getInstance().getFormattedDate(endDateTime));
                        isEndDateSated = true;
                        et_endTime.setText("");
                    },
                    now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
            dpd.setMinDate(startDateTime);

            dpd.show(activity.getFragmentManager(), "DatePickerDialog");
        });


        et_StartTime.setOnClickListener(v -> {
            if (startDateTime == null) {
                activity.makeToast(activity.getString(R.string.pleaseSelectStartDateFirst));
                return;
            }
//            startDateTime = Calendar.getInstance();
            TimePickerDialog tpd = TimePickerDialog.newInstance((view1, hourOfDay, minute, second) -> {
                startDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                startDateTime.set(Calendar.MINUTE, minute);
                et_StartTime.setText(ProjectUtils.getInstance().getFormattedTime(startDateTime));
                isStartTimeSated = true;
            }, startDateTime.get(Calendar.HOUR_OF_DAY), startDateTime.get(Calendar.MINUTE), false);
            tpd.show(activity.getFragmentManager(), "TimePickerDialog");
        });

        et_endTime.setOnClickListener(v -> {
            if (startDateTime == null) {
                activity.makeToast(activity.getString(R.string.pleaseSelectStartTimeFirst));
                return;
            }
            if (endDateTime == null) {
                activity.makeToast(activity.getString(R.string.pleaseSelectStartTimeFirst));
                return;
            }
            if (endDateTime != null) {
                TimePickerDialog tpd = TimePickerDialog.newInstance((view1, hourOfDay, minute, second) -> {
                    endDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    endDateTime.set(Calendar.MINUTE, minute);
                    et_endTime.setText(ProjectUtils.getInstance().getFormattedTime(endDateTime));
                    isEndTimeSated = true;
                }, endDateTime.get(Calendar.HOUR_OF_DAY), endDateTime.get(Calendar.MINUTE), false);
                tpd.show(activity.getFragmentManager(), "TimePickerDialog");
            }
        });


        bt_ScheduleSubmit.setOnClickListener(v -> {
            if (scheduleCategory == null) {
                activity.makeToast(activity.getString(R.string.pleaseSelectJob));
                return;
            }

            if (scheduleCategory.getId() == 0) {
                if (TextUtils.isEmpty(et_otherCategory.getText().toString())) {
                    et_otherCategory.setError("Required Other Job");
                    et_otherCategory.requestFocus();
                    return;
                }
                scheduleCategory.setItem(et_otherCategory.getText().toString());
            }

            if (!isStartDateSated) {
                activity.makeToast(activity.getString(R.string.pleaseSelectStartDate));
                return;
            }
            if (!isStartTimeSated) {
                activity.makeToast(activity.getString(R.string.pleaseSelectStartTime));
                return;
            }
            if (!isEndDateSated) {
                activity.makeToast(activity.getString(R.string.pleaseSelectEndDate));
                return;
            }
            if (!isEndTimeSated) {
                activity.makeToast(activity.getString(R.string.pleaseSelectEndTime));
                return;
            }
//                if (et_pwScheduleEndDate.getText().toString().equals("")) {
//                    et_pwScheduleEndDate.setError("This field is required");
//                    et_pwScheduleEndDate.requestFocus();
//                    return;
//                } else {
//                    et_pwScheduleEndDate.setError(null);
//                }

//                if (et_pwScheduleEndTime.getText().toString().equals("")) {
//                    et_pwScheduleEndTime.setError("This field is required");
//                    et_pwScheduleEndTime.requestFocus();
//                    return;
//                } else {
//                    et_pwScheduleEndTime.setError(null);
//                }

            if (scheduleSubCategory == null) {
                activity.makeToast(activity.getString(R.string.pleaseSelectWorkScope));
                return;
            }

            if (scheduleSubCategory.getId() == 0) {
                if (TextUtils.isEmpty(et_otherSubCategory.getText().toString())) {
                    et_otherSubCategory.setError("Required Other Work Scope");
                    et_otherSubCategory.requestFocus();
                    return;
                }
                scheduleSubCategory.setItem(et_otherSubCategory.getText().toString());
            }

//            if (et_pwScheduleDescription.getText().toString().equals("")) {
//                et_pwScheduleDescription.setError("This field is required");
//                et_pwScheduleDescription.requestFocus();
//                return;
//            } else {
//                et_pwScheduleDescription.setError(null);
//            }

            getSubmitSchedule(scheduleBean != null ? scheduleBean.getScheduleId() : 0);
        });

        spinner_pwScheduleLineOfWork.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
                if (i == 0) {
                    scheduleCategory = null;
                    return;
                }
                scheduleCategory = (BeanScheduleCategory) adapterView.getAdapter().getItem(i);
                if (scheduleCategory.getId() == 0) {
                    ll_otherCat.setVisibility(View.VISIBLE);
                } else {
                    ll_otherCat.setVisibility(View.GONE);
                    et_otherCategory.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_pwScheduleFurtherCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
                if (i == 0) {
                    scheduleSubCategory = null;
                    return;
                }
                scheduleSubCategory = (BeanScheduleCategory) adapterView.getAdapter().getItem(i);
                if (scheduleSubCategory.getId() == 0) {
                    ll_otherSubCat.setVisibility(View.VISIBLE);
                } else {
                    ll_otherSubCat.setVisibility(View.GONE);
                    et_otherSubCategory.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        new InteractorImpl(activity, new OnResponseListener() {
            @Override
            public void onSuccess(int requestCode, ResponsePacket responsePacket) {
                if (responsePacket.getErrorCode() == 0) {
                    int selectedId = 0;
                    String selectedText = "Select";
                    if (scheduleBean != null) {
                        selectedText = scheduleBean.getCategoryTitle();
                    }

                    ArrayList<BeanScheduleCategory> categories = new ArrayList<>();
                    categories.add(new BeanScheduleCategory(-1, "Select"));

                    for (int i = 0; i < responsePacket.getScheduleCategoryList().size(); i++) {
                        categories.add(responsePacket.getScheduleCategoryList().get(i));
                        if (responsePacket.getScheduleCategoryList().get(i).getItem().equalsIgnoreCase(selectedText)) {
                            selectedId = i + 1;
                        }
                    }
                    categories.add(new BeanScheduleCategory(0, "Other"));
                    AdapterScheduleCategory adapter = new AdapterScheduleCategory(activity, categories);
                    spinner_pwScheduleLineOfWork.setAdapter(adapter);
                    spinner_pwScheduleLineOfWork.setSelection(selectedId);
                }
            }

            @Override
            public void onError(int requestCode, ErrorType errorType) {
            }
        }, Interactor.RequestCode_GetScheduleCategory, Interactor.Tag_GetScheduleCategory)
                .makeJsonPostRequest(Interactor.Method_GetScheduleCategory, jsonObject, true);

        new InteractorImpl(activity, new OnResponseListener() {
            @Override
            public void onSuccess(int requestCode, ResponsePacket responsePacket) {
                if (responsePacket.getErrorCode() == 0) {
                    ArrayList<BeanScheduleCategory> subCategories = new ArrayList<>();
                    subCategories.add(new BeanScheduleCategory(-1, "Select"));

                    int selectedId = 0;
                    String selectedText = "Select";
                    if (scheduleBean != null) {
                        selectedText = scheduleBean.getSubCategoryTitle();
                    }

                    for (int i = 0; i < responsePacket.getScheduleSubCatList().size(); i++) {
                        subCategories.add(responsePacket.getScheduleSubCatList().get(i));
                        if (responsePacket.getScheduleSubCatList().get(i).getItem().equalsIgnoreCase(selectedText)) {
                            selectedId = i + 1;
                        }
                    }
//                    subCategories.addAll(responsePacket.getScheduleSubCatList());
                    subCategories.add(new BeanScheduleCategory(0, "Other"));
                    AdapterScheduleCategory adapter = new AdapterScheduleCategory(activity, subCategories);
                    spinner_pwScheduleFurtherCategories.setAdapter(adapter);
                    spinner_pwScheduleFurtherCategories.setSelection(selectedId);
                }
            }

            @Override
            public void onError(int requestCode, ErrorType errorType) {

            }
        }, Interactor.RequestCode_GetScheduleSubCategory, Interactor.Tag_GetScheduleSubCategory)
                .makeJsonPostRequest(Interactor.Method_GetScheduleSubCategory, jsonObject, true);

        if (scheduleBean != null) {
            endDateTime = Calendar.getInstance();
            startDateTime = Calendar.getInstance();
            startDateTime.setTimeInMillis(scheduleBean.getStartDate() * 1000);
            endDateTime.setTimeInMillis(scheduleBean.getEndDate() * 1000);
            isStartDateSated = true;
            isStartTimeSated = true;
            isEndDateSated = true;
            isEndTimeSated = true;
            et_StartDate.setText(ProjectUtils.getInstance().getFormattedDate(startDateTime));
            et_StartTime.setText(ProjectUtils.getInstance().getFormattedTime(startDateTime));
            et_endDate.setText(ProjectUtils.getInstance().getFormattedDate(endDateTime));
            et_endTime.setText(ProjectUtils.getInstance().getFormattedTime(endDateTime));
            et_pwScheduleDescription.setText(scheduleBean.getDetail());
            bt_ScheduleSubmit.setText(activity.getString(R.string.update));
        } else {
            isStartDateSated = false;
            isStartTimeSated = false;
            isEndDateSated = false;
            isEndTimeSated = false;
        }
    }

    BeanScheduleCategory scheduleCategory, scheduleSubCategory;

    @Override
    public void onFloatingButtonClick() {
        super.onFloatingButtonClick();
        showScheduleItemPopUp(null, "Set Schedule");
    }


    @Override
    public void onError(int requestCode, ErrorType errorType) {
        super.onError(requestCode, errorType);
    }

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
        if (responsePacket.getErrorCode() == 410) {
            ((ActivityMain) getActivity()).logoutUser();
            return;
        }
        if (responsePacket.getStatus().equalsIgnoreCase("success")) {
            if (Interactor.RequestCode_GetAllScheduleList == requestCode) {
                setSchedule(responsePacket.getScheduleList());
                tv_noDataFound.setText(responsePacket.getMessage());
            } else if (Interactor.RequestCode_SubmitSchedule == requestCode) {
                if (scheduleItemDialog != null && scheduleItemDialog.isShowing()) {
                    scheduleItemDialog.dismiss();
                }
                getAllScheduleList();
            } else if (Interactor.RequestCode_UpdateScheduleStatus == requestCode) {
                getAllScheduleList();
            }
        } else {
            activity.makeToast(responsePacket.getMessage());
        }
    }

    public void getSubmitSchedule(long scheduledId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("scheduleCatId", scheduleCategory.getId());
            jsonObject.put("scheduleCatText", scheduleCategory.getItem());
            jsonObject.put("scheduleSubCatId", scheduleSubCategory.getId());
            jsonObject.put("scheduleSubCatText", scheduleSubCategory.getItem());
            jsonObject.put("projectId", ApplicationContext.getInstance().project.getProjectId());
            jsonObject.put("startDateTime", (startDateTime.getTimeInMillis() / 1000) + "");
            jsonObject.put("endDateTime", (endDateTime.getTimeInMillis() / 1000) + "");
            jsonObject.put("description", et_pwScheduleDescription.getText().toString());
            jsonObject.put("scheduledId", scheduledId + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(activity, this, Interactor.RequestCode_SubmitSchedule, Interactor.Tag_SubmitSchedule)
                .makeJsonPostRequest(Interactor.Method_SubmitSchedule, jsonObject, true);
    }
}
