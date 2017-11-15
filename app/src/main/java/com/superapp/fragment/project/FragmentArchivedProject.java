package com.superapp.fragment.project;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;

import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.ActivityLogin;
import com.superapp.activity.ActivityMain;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.ErrorType;
import com.superapp.beans.BeanUserDetail;
import com.superapp.beans.ClientBean;
import com.superapp.beans.CoworkerBean;
import com.superapp.beans.ProjectBean;
import com.superapp.fragment.base.BaseFragment;
import com.superapp.fragment.base.FragmentNames;
import com.superapp.utils.PopupUtils;
import com.superapp.utils.PrefSetup;
import com.superapp.utils.ProjectUtils;
import com.superapp.utils.Utilities;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.OnResponseListener;
import com.superapp.webservice.ResponsePacket;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FragmentArchivedProject extends BaseFragment {

    public View fragmentView;
    private long projectId;
    private int reminderCounter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentView = inflater.inflate(R.layout.fragment_project_detail, container, false);
        projectId = ApplicationContext.getInstance().project.getProjectId();
        initView();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (activity instanceof ActivityMain) {
            ((ActivityMain) activity).changeHeaderButton(true);
            ((ActivityMain) activity).setHeaderText(getString(R.string.projectDetail));
            ((ActivityMain) activity).showHideFloatingButton(false, R.drawable.add);
            ((ActivityMain) activity).showHideNotificationButton(false);
            ((ActivityMain) activity).showHIdeSearchButton(true);
            ((ActivityMain) activity).showAd(false);
        }
        getProjectDetail();
    }

    @Override
    public void onFloatingButtonClick() {
        super.onFloatingButtonClick();
    }

    @Override
    public void updateView() {
        super.updateView();
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initView() {
        initProjectView();
        initClientView();
        initDesignerView();
        initCoworkerView();
        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
            updateClientDetail();
        } else if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("c")) {
            updateDesignerDetail();
        }
        getAllReminders();
    }

    //    Project Section
    ImageView iv_projectShowHide, iv_editEstimatedHandoverDate, iv_download;
    LinearLayout ll_projectDetail, ll_percentageBar, ll_balanceMoneyWithYou, ll_overBudget, ll_totalMoneyPaid;
    TextView tv_projectName, tv_projectType, tv_projectAddress, tv_projectDesignStartDate, tv_projectDesignStartedDays, tv_projectExecutionStartDate,
            tv_projectExecutionStartedDays, tv_projectHandoverDate, tv_projectHandoverDaysLeft, tv_projectClientPaidAmount, tv_projectBalanceAmount,
            tv_projectDaysAlert, tv_projectCompletedPercent, tv_overBudget;
    View viewEmpty1, viewEmpty2, viewFill2, viewFill1;
    TextView tv_completeProject;

    private void initProjectView() {
        iv_projectShowHide = (ImageView) setTouchNClickSrc(R.id.iv_projectShowHide, fragmentView);
        iv_editEstimatedHandoverDate = (ImageView) setTouchNClickSrc(R.id.iv_editEstimatedHandoverDate, fragmentView);
        iv_download = (ImageView) setTouchNClickSrc(R.id.iv_download, fragmentView);

        iv_editEstimatedHandoverDate.setVisibility(View.GONE);

        ll_projectDetail = (LinearLayout) fragmentView.findViewById(R.id.ll_projectDetail);
        ll_totalMoneyPaid = (LinearLayout) fragmentView.findViewById(R.id.ll_totalMoneyPaid);
        ll_balanceMoneyWithYou = (LinearLayout) fragmentView.findViewById(R.id.ll_balanceMoneyWithYou);

        ll_overBudget = (LinearLayout) fragmentView.findViewById(R.id.ll_overBudget);
        tv_overBudget = (TextView) fragmentView.findViewById(R.id.tv_overBudget);

        tv_completeProject = (TextView) setClick(R.id.tv_completeProject, fragmentView);

        tv_projectName = (TextView) fragmentView.findViewById(R.id.tv_projectName);
        tv_projectType = (TextView) fragmentView.findViewById(R.id.tv_projectType);
        tv_projectAddress = (TextView) fragmentView.findViewById(R.id.tv_projectAddress);
        tv_projectDesignStartDate = (TextView) fragmentView.findViewById(R.id.tv_projectDesignStartDate);
        tv_projectDesignStartedDays = (TextView) fragmentView.findViewById(R.id.tv_projectDesignStartedDays);
        tv_projectExecutionStartDate = (TextView) fragmentView.findViewById(R.id.tv_projectExecutionStartDate);
        tv_projectExecutionStartedDays = (TextView) fragmentView.findViewById(R.id.tv_projectExecutionStartedDays);
        tv_projectHandoverDate = (TextView) fragmentView.findViewById(R.id.tv_projectHandoverDate);
        tv_projectHandoverDaysLeft = (TextView) fragmentView.findViewById(R.id.tv_projectHandoverDaysLeft);
        tv_projectClientPaidAmount = (TextView) fragmentView.findViewById(R.id.tv_projectClientPaidAmount);
        tv_projectBalanceAmount = (TextView) fragmentView.findViewById(R.id.tv_projectBalanceAmount);
        tv_projectDaysAlert = (TextView) fragmentView.findViewById(R.id.tv_projectDaysAlert);
        tv_projectCompletedPercent = (TextView) fragmentView.findViewById(R.id.tv_projectCompletedPercent);

        ll_percentageBar = (LinearLayout) fragmentView.findViewById(R.id.ll_percentageBar);

        viewEmpty1 = fragmentView.findViewById(R.id.viewEmpty1);
        viewEmpty2 = fragmentView.findViewById(R.id.viewEmpty2);
        viewFill2 = fragmentView.findViewById(R.id.viewFill2);
        viewFill1 = fragmentView.findViewById(R.id.viewFill1);
    }

    private void updateProjectView(ProjectBean projectDetail) {
        tv_projectName.setText(projectDetail.getProjectName());
        tv_projectType.setText(projectDetail.getProjectType());
        tv_projectAddress.setText(ProjectUtils.getInstance().getFormattedAddress(projectDetail.getLocation()).toString());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(projectDetail.getProjectStartedDays() * 1000);
        tv_projectDesignStartDate.setText(ProjectUtils.getInstance().getFormattedDate(calendar));

        long projectStartDays = ProjectUtils.getInstance().getDaysDifferenceFromCurrentDate(calendar.getTimeInMillis());
        tv_projectDesignStartedDays.setText((projectStartDays < 0 ? 0 : projectStartDays) + "");

        calendar.setTimeInMillis(projectDetail.getWorkStartedDays() * 1000);
        tv_projectExecutionStartDate.setText(ProjectUtils.getInstance().getFormattedDate(calendar));
        long workStartDays = ProjectUtils.getInstance().getDaysDifferenceFromCurrentDate(calendar.getTimeInMillis());
        tv_projectExecutionStartedDays.setText((workStartDays < 0 ? 0 : workStartDays) + "");

        calendar.setTimeInMillis(projectDetail.getHandoverdate() * 1000);
        tv_projectHandoverDate.setText(ProjectUtils.getInstance().getFormattedDate(calendar));
        long handOverDays = ProjectUtils.getInstance().getDaysDifferenceFromCurrentDate(calendar.getTimeInMillis());
        tv_projectHandoverDaysLeft.setText((handOverDays < 0 ? handOverDays * -1 : 0) + "");
//        tv_projectHandoverDaysLeft.setText((handOverDays < 0 ? handOverDays * -1 : handOverDays) + "");

        tv_projectClientPaidAmount.setText("Rs. " + (long) projectDetail.getTotalPaidByClient() + "/-");

        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("c")) {
            if (projectDetail.getToggleNotification().equalsIgnoreCase("t")) {
                ll_totalMoneyPaid.setVisibility(View.VISIBLE);
                ll_balanceMoneyWithYou.setVisibility(View.VISIBLE);
            } else {
                ll_totalMoneyPaid.setVisibility(View.GONE);
                ll_balanceMoneyWithYou.setVisibility(View.GONE);
            }
        } else {
            ll_totalMoneyPaid.setVisibility(View.VISIBLE);
            ll_balanceMoneyWithYou.setVisibility(View.VISIBLE);
        }

        tv_projectBalanceAmount.setText("Rs. " + (long) projectDetail.getTotalBalance() + "/-");

        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
            if (projectDetail.getOverBudget() > 0) {
                ll_overBudget.setVisibility(View.VISIBLE);
                tv_overBudget.setText("Rs. " + (long) projectDetail.getOverBudget() + "/-");
            } else {
                ll_overBudget.setVisibility(View.GONE);
            }
        }

        long behindDays = ProjectUtils.getInstance().getDaysDifferenceFromCurrentDate(calendar.getTimeInMillis());
        if (behindDays > 0) {
            tv_projectDaysAlert.setVisibility(View.VISIBLE);
            tv_projectDaysAlert.setText("You Are " + behindDays + " Days Behind Schedule");
        } else {
            tv_projectDaysAlert.setVisibility(View.GONE);
        }

        // Dynamically Reset View according to percentage completed.
        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
            if (PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("3")) {
                if (projectDetail.getPercentCompleted().equals("100")) {
                    iv_download.setVisibility(View.VISIBLE);
                } else {
                    iv_download.setVisibility(View.GONE);
                }
            }
        }

        tv_projectCompletedPercent.setText(projectDetail.getPercentCompleted() + "%");

        int percentCompleted = Integer.parseInt(projectDetail.getPercentCompleted());
        LinearLayout.LayoutParams paramViewFill = new LinearLayout.LayoutParams(0, 3);
        paramViewFill.weight = percentCompleted;
        viewFill1.setLayoutParams(paramViewFill);
        viewFill2.setLayoutParams(paramViewFill);
        LinearLayout.LayoutParams paramViewEmpty = new LinearLayout.LayoutParams(0, 3);
        paramViewEmpty.weight = 100 - percentCompleted;
        viewEmpty1.setLayoutParams(paramViewEmpty);
        viewEmpty2.setLayoutParams(paramViewEmpty);
        ll_percentageBar.invalidate();

        ApplicationContext.getInstance().project = projectDetail;

        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
            if (projectDetail.getIsComplete().equalsIgnoreCase("t")) {
                tv_completeProject.setVisibility(View.GONE);
            } else {
                tv_completeProject.setVisibility(View.GONE);
            }
        } else {
            tv_completeProject.setVisibility(View.GONE);
        }

//        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("c") &&
//                projectDetail.getIsComplete().equalsIgnoreCase("t") &&
//                projectDetail.getCheckRating().equalsIgnoreCase("f")) {
//            submitRating();
//        }
    }

    Calendar handoverDate;
    DatePickerDialog dpd;
    Calendar calendar1;

    public void changeHandoverDatePopup() {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_change_handoverdate, null, false);
        dialog.setContentView(view);

        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        Calendar calendar = Calendar.getInstance();

        Button bt_update = (Button) view.findViewById(R.id.bt_update);
        ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);

        EditText et_currentHandoverDate = (EditText) view.findViewById(R.id.et_currentHandoverDate);
        EditText et_newHandoverDate = (EditText) view.findViewById(R.id.et_newHandoverDate);

        if (ApplicationContext.getInstance().project != null) {
            calendar.setTimeInMillis(ApplicationContext.getInstance().project.getHandoverdate() * 1000);
            et_currentHandoverDate.setText(ProjectUtils.getInstance().getFormattedDate(calendar));

        }
//        Calendar calendar1 = Calendar.getInstance();
//        calendar1.setTimeInMillis(Long.valueOf(ProjectUtils.getInstance().getFormattedDate(calendar)));


//        dpd.setMinDate(Long.parseLong(ProjectUtils.getInstance().getFormattedDate(calendar)));
        et_newHandoverDate.setOnClickListener(v -> {
            Date dateObj = new Date(ProjectUtils.getInstance().getFormattedDate(calendar));
            Date d = new Date(ApplicationContext.getInstance().project.getHandoverdate() * 1000);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTimeInMillis(d.getTime());
//            dpd.setMinDate(calendar1);

//            handoverDate.setTimeInMillis(d.getTime());
            calendar1 = calendar2;
            if (handoverDate != null)
                calendar1 = handoverDate;
            dpd = DatePickerDialog.newInstance((view1, year, monthOfYear, dayOfMonth) -> {

                handoverDate = Calendar.getInstance();
                handoverDate.set(Calendar.YEAR, year);
                handoverDate.set(Calendar.MONTH, monthOfYear);
                handoverDate.set(Calendar.DATE, dayOfMonth);
                et_newHandoverDate.setText(ProjectUtils.getInstance().getFormattedDate(handoverDate));

            }, calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
            dpd.setMinDate(calendar2);
            dpd.show(activity.getFragmentManager(), "Timepickerdialog");

//            calendar1 = handoverDate;
//            dpd = DatePickerDialog.newInstance((view1, year, monthOfYear, dayOfMonth) -> {
//                dpd.setMinDate(calendar1);
//                handoverDate = Calendar.getInstance();
//                handoverDate.set(Calendar.YEAR, year);
//                handoverDate.set(Calendar.MONTH, monthOfYear);
//                handoverDate.set(Calendar.DATE, dayOfMonth);
//                et_newHandoverDate.setText(ProjectUtils.getInstance().getFormattedDate(handoverDate));
//            }, handoverDate.get(Calendar.YEAR), handoverDate.get(Calendar.MONTH), handoverDate.get(Calendar.DAY_OF_MONTH));
//            dpd.show(activity.getFragmentManager(), "Timepickerdialog");
        });

        bt_update.setOnClickListener(v -> {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
                jsonObject.put("userId", PrefSetup.getInstance().getUserId());
                jsonObject.put("projectId", ApplicationContext.getInstance().project.getProjectId());

                ProjectUtils.getInstance().resetTimeTo00(handoverDate);
                jsonObject.put("updatedHandoverDate", (handoverDate.getTimeInMillis() / 1000) + "");

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                new InteractorImpl(activity, new OnResponseListener() {
                    @Override
                    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
                        if (responsePacket.getErrorCode() == 0) {
                            dialog.dismiss();

                            // Refresh a Fragment Code

                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.detach(FragmentArchivedProject.this).attach(FragmentArchivedProject.this).commit();
                        }
                    }

                    @Override
                    public void onError(int requestCode, ErrorType errorType) {

                    }
                }, Interactor.RequestCode_changeHandoverDate, Interactor.Tag_changeHandoverDate)
                        .makeJsonPostRequest(Interactor.Method_changeHandoverDate, jsonObject, true);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        iv_close.setOnClickListener(v -> {
            dialog.dismiss();

        });
    }

    //    Clint Section
    LinearLayout ll_clientInfo;
    ImageView iv_clientShowHide;
    TextView tv_clientName, tv_clientAddress, tv_clientEmail, tv_clientMobile;

    private void initClientView() {
        ll_clientInfo = (LinearLayout) fragmentView.findViewById(R.id.ll_clientInfo);
        iv_clientShowHide = (ImageView) setTouchNClickSrc(R.id.iv_clientShowHide, fragmentView);
        tv_clientName = (TextView) fragmentView.findViewById(R.id.tv_clientName);
        tv_clientAddress = (TextView) fragmentView.findViewById(R.id.tv_clientAddress);
        tv_clientEmail = (TextView) fragmentView.findViewById(R.id.tv_clientEmail);
        tv_clientMobile = (TextView) fragmentView.findViewById(R.id.tv_clientMobile);
    }

    private void updateClientView(ClientBean clientBean) {
        try {


            ApplicationContext.getInstance().project.setClient(clientBean);
            tv_clientName.setText(clientBean.getName());
            if (!TextUtils.isEmpty(clientBean.getAddress())) {
                tv_clientAddress.setText(ProjectUtils.getInstance().getFormattedAddress(clientBean.getAddress()).toString());
            } else {
                tv_clientAddress.setText("No Address Available");
            }
            tv_clientEmail.setText(clientBean.getEmail());
            tv_clientMobile.setText(clientBean.getPhone());
            clientShowHide();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    Desiger Section
    LinearLayout ll_designerInfo;
    ImageView iv_designerShowHide;
    TextView tv_designerName, tv_designerAddress, tv_designerEmail, tv_designerMobile, tv_rating;

    private void initDesignerView() {
        ll_designerInfo = (LinearLayout) fragmentView.findViewById(R.id.ll_designerInfo);
        iv_designerShowHide = (ImageView) setTouchNClickSrc(R.id.iv_designerShowHide, fragmentView);
        tv_designerName = (TextView) fragmentView.findViewById(R.id.tv_designerName);
        tv_designerAddress = (TextView) fragmentView.findViewById(R.id.tv_designerAddress);
        tv_designerEmail = (TextView) fragmentView.findViewById(R.id.tv_designerEmail);
        tv_designerMobile = (TextView) fragmentView.findViewById(R.id.tv_designerMobile);
        tv_rating = (TextView) fragmentView.findViewById(R.id.tv_rating);
    }

    private void updateDesignerView(BeanUserDetail designerDetail) {
        tv_designerName.setText(designerDetail.getName());
        tv_designerAddress.setText("");
        tv_designerEmail.setText(designerDetail.getEmail());
        tv_designerMobile.setText(designerDetail.getContactNo());
        tv_rating.setText(ApplicationContext.getInstance().project.getAvgRating());
        designerShowHide();
    }

    //    Coworker Section
    LinearLayout ll_coworkerItems;

    private void initCoworkerView() {
        ll_coworkerItems = (LinearLayout) fragmentView.findViewById(R.id.ll_coworkerItems);
    }

    private void updateCoWorkerView(ArrayList<CoworkerBean> coworkerList) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ll_coworkerItems.removeAllViews();
        for (final CoworkerBean coworkerBean : coworkerList) {
            View coWorkerView = inflater.inflate(R.layout.fragment_project_detail_coworker_item, null);

            final LinearLayout ll_coWorker = (LinearLayout) coWorkerView.findViewById(R.id.ll_coWorker);
            final ImageView iv_coworkerShowHide = (ImageView) coWorkerView.findViewById(R.id.iv_coworkerShowHide);
            iv_coworkerShowHide.setOnTouchListener(BaseAppCompatActivity.imageTouch);
            iv_coworkerShowHide.setOnClickListener(v -> {
                if (ll_coWorker.getVisibility() == View.VISIBLE) {
                    ll_coWorker.setVisibility(View.GONE);
                    iv_coworkerShowHide.setImageResource(R.drawable.pluse);
                } else {
                    ll_coWorker.setVisibility(View.VISIBLE);
                    iv_coworkerShowHide.setImageResource(R.drawable.minus);
                }
            });
            ll_coWorker.setVisibility(View.GONE);
            iv_coworkerShowHide.setImageResource(R.drawable.pluse);

            LinearLayout ll_SendID = (LinearLayout) coWorkerView.findViewById(R.id.ll_SendID);

            TextView tv_coworkerName = (TextView) coWorkerView.findViewById(R.id.tv_coworkerName);
            TextView tv_coworkerOccupation = (TextView) coWorkerView.findViewById(R.id.tv_coworkerOccupation);
            TextView tv_coworkerAddress = (TextView) coWorkerView.findViewById(R.id.tv_coworkerAddress);
            TextView tv_coworkerEmailText = (TextView) coWorkerView.findViewById(R.id.tv_coworkerEmailText);
            TextView tv_coworkerEmail = (TextView) coWorkerView.findViewById(R.id.tv_coworkerEmail);
            TextView tv_coworkerMobile = (TextView) coWorkerView.findViewById(R.id.tv_coworkerMobile);
            ImageView iv_coworkerPhotoId = (ImageView) coWorkerView.findViewById(R.id.iv_coworkerPhotoId);
            ImageView iv_sendId = (ImageView) coWorkerView.findViewById(R.id.iv_sendId);
            ApplicationContext.getInstance().loadImage(coworkerBean.getPhotoId(), iv_coworkerPhotoId, null, R.drawable.no_image);

            tv_coworkerName.setText(coworkerBean.getName());
            ll_SendID.setOnClickListener((View v) -> sendIDToClient(coworkerBean.getId()));

            tv_coworkerOccupation.setText(coworkerBean.getOccupation());
            tv_coworkerAddress.setText(ProjectUtils.getInstance().getFormattedAddress(coworkerBean.getAddress()).toString());

            if (!TextUtils.isEmpty(coworkerBean.getEmail())) {
                tv_coworkerEmail.setText(coworkerBean.getEmail());
            } else {
                tv_coworkerEmailText.setVisibility(View.GONE);
                tv_coworkerEmail.setVisibility(View.GONE);
            }
            if (!coworkerBean.getMobile().equals("0"))
                tv_coworkerMobile.setText(coworkerBean.getMobile());

            ll_coworkerItems.addView(coWorkerView);
        }
    }

    public void sendIDToClient(long coworkerId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("coworkerId", coworkerId + "");
            jsonObject.put("projectId", ApplicationContext.getInstance().project.getProjectId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            new InteractorImpl(activity, FragmentArchivedProject.this, Interactor.RequestCode_SendID, Interactor.Tag_SendID)
                    .makeJsonPostRequest(Interactor.Method_SendID, jsonObject, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void projectComplete() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("projectId", ApplicationContext.getInstance().project.getProjectId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            new InteractorImpl(activity, FragmentArchivedProject.this, Interactor.RequestCode_ProjectComplete, Interactor.Tag_ProjectComplete)
                    .makeJsonPostRequest(Interactor.Method_ProjectComplete, jsonObject, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void completProjectDialog() {

        View view;
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.completproject_dialogbox, null, false);
        dialog.setContentView(view);

        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        Button dialog_ok = (Button) view.findViewById(R.id.dialog_ok);
        Button dialog_cancel = (Button) view.findViewById(R.id.dialog_cancel);
        ImageView iv_back = (ImageView) view.findViewById(R.id.iv_back);
        dialog_ok.setOnClickListener(v -> {
            projectComplete();
//                tv_completeProject.setText(activity.getString(R.string.completed));
            dialog.dismiss();
            tv_completeProject.setVisibility(View.GONE);
        });

        dialog_cancel.setOnClickListener(v -> dialog.dismiss());
        iv_back.setOnClickListener(v -> dialog.dismiss());
    }

//    public void DesignerRating() {
//
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
//            jsonObject.put("clientId", ApplicationContext.getInstance().project.getClientId());
//            jsonObject.put("rating", ratingBar.getRating());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            new InteractorImpl(activity, FragmentArchivedProject.this, Interactor.RequestCode_DesignerRating, Interactor.Tag_DesignerRating)
//                    .makeJsonPostRequest(Interactor.Method_DesignerRating, jsonObject, true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    RatingBar ratingBar;

//    public void submitRating() {
//        View view;
//        final Dialog dialog = new Dialog(activity);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
//        view = inflater.inflate(R.layout.popup_submitrating, null, false);
//        dialog.setContentView(view);
//
//        final Window window = dialog.getWindow();
//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.show();
//        Button submit = (Button) view.findViewById(R.id.submit);
//        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
////        String rating = String.valueOf(ratingBar.getRating()+"");
//        submit.setOnClickListener(v -> {
//            DesignerRating();
//            dialog.dismiss();
//        });
//        dialog.setCancelable(false);
//    }

    private void updateClientDetail() {
        fragmentView.findViewById(R.id.ll_company_client).setVisibility(View.VISIBLE);
        fragmentView.findViewById(R.id.ll_company_designer).setVisibility(View.GONE);
        // ToDO update client detail here
    }

    private void updateDesignerDetail() {
        fragmentView.findViewById(R.id.ll_company_client).setVisibility(View.GONE);
        fragmentView.findViewById(R.id.ll_company_designer).setVisibility(View.VISIBLE);
        // ToDO update designer detail here
    }

    PopupWindow changeStatusPopUp;

    private void showStatusPopup(float x, float y) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.projectdetail_popupwindow, null);

        changeStatusPopUp = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        changeStatusPopUp.setBackgroundDrawable(new ColorDrawable());
        changeStatusPopUp.setTouchable(true);
        changeStatusPopUp.setOutsideTouchable(true);
        changeStatusPopUp.setFocusable(true);

        changeStatusPopUp.showAtLocation(layout, Gravity.NO_GRAVITY, (int) x, (int) y);
        final LinearLayout ll_communicationSubMenu = (LinearLayout) layout.findViewById(R.id.ll_communicationSubMenu);
        final TextView tv_clarification = (TextView) layout.findViewById(R.id.tv_clarification);
        final TextView tv_recommendation = (TextView) layout.findViewById(R.id.tv_recommendation);
        final TextView tv_approval = (TextView) layout.findViewById(R.id.tv_approval);

        final LinearLayout ll_addClientPopUp = (LinearLayout) layout.findViewById(R.id.ll_PopUpItemAddClient);
        LinearLayout ll_PopUpItemLogout = (LinearLayout) layout.findViewById(R.id.ll_PopUpItemLogout);
        LinearLayout ll_schedule = (LinearLayout) layout.findViewById(R.id.ll_schedule);
        LinearLayout ll_PopUpItemAddCoWorker = (LinearLayout) layout.findViewById(R.id.ll_PopUpItemAddCoWorker);
        LinearLayout ll_team = (LinearLayout) layout.findViewById(R.id.ll_team);

        LinearLayout ll_checklist = (LinearLayout) layout.findViewById(R.id.ll_checklist);
        LinearLayout ll_communication = (LinearLayout) layout.findViewById(R.id.ll_communication);
        LinearLayout ll_transactions = (LinearLayout) layout.findViewById(R.id.ll_transactions);
        LinearLayout ll_reminder = (LinearLayout) layout.findViewById(R.id.ll_reminder);
        final TextView tv_reminderCounter = (TextView) layout.findViewById(R.id.tv_reminderCounter);

        if (reminderCounter > 0) {
            tv_reminderCounter.setText(String.valueOf(reminderCounter));
        }

        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("c")) {
            ll_schedule.setVisibility(View.GONE);
            ll_checklist.setVisibility(View.GONE);
            ll_reminder.setVisibility(View.GONE);
            ll_team.setVisibility(View.GONE);
        } else if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
            ll_schedule.setVisibility(View.VISIBLE);
            ll_checklist.setVisibility(View.VISIBLE);
            ll_reminder.setVisibility(View.VISIBLE);
            ll_team.setVisibility(View.VISIBLE);

        }
        ll_PopUpItemLogout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ActivityLogin.class);
            intent.putExtra("finish", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            activity.finish();
        });

        ll_addClientPopUp.setOnClickListener(v -> {
            ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentAddClient, null, false, false);
            changeStatusPopUp.dismiss();
        });

        ll_PopUpItemAddCoWorker.setOnClickListener(v -> {
            ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentAddCoWorker, null, false, false);
            changeStatusPopUp.dismiss();
        });

        ll_transactions.setOnClickListener(v -> {
            ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentTransactions, null, false, false);
            changeStatusPopUp.dismiss();
        });

        ll_schedule.setOnClickListener(v -> {
            ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentSchedule, null, false, false);
            changeStatusPopUp.dismiss();
        });

        ll_checklist.setOnClickListener(v -> {
            ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentCheckList, null, false, false);
            changeStatusPopUp.dismiss();
        });

        ll_team.setOnClickListener(v -> {
            ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentTeam, null, false, false);
            changeStatusPopUp.dismiss();
        });

        ll_communication.setOnClickListener(v -> {
            if (ll_communicationSubMenu.getVisibility() == View.VISIBLE)
                ll_communicationSubMenu.setVisibility(View.GONE);
            else
                ll_communicationSubMenu.setVisibility(View.VISIBLE);
        });

        /*tv_approval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatusPopUp.dismiss();
                Intent uploadApproval = new Intent(activity, ActivityUpload.class);
                uploadApproval.putExtra("communicationType", ActivityUpload.COMMUNICATION_TYPE.APPROVAL.getValue());
                activity.startActivity(uploadApproval);
            }
        });
        tv_clarification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatusPopUp.dismiss();
                Intent uploadApproval = new Intent(activity, ActivityUpload.class);
                uploadApproval.putExtra("communicationType", ActivityUpload.COMMUNICATION_TYPE.CLARIFICATION.getValue());
                activity.startActivity(uploadApproval);
            }
        });

        tv_recommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatusPopUp.dismiss();
                Intent uploadApproval = new Intent(activity, ActivityUpload.class);
                uploadApproval.putExtra("communicationType", ActivityUpload.COMMUNICATION_TYPE.RECOMMENDATION.getValue());
                activity.startActivity(uploadApproval);
            }
        });*/

        tv_approval.setOnClickListener(v -> {
            ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentApproval, null, false, false);
            changeStatusPopUp.dismiss();
        });

        tv_clarification.setOnClickListener(v -> {
            ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentClarifications, null, false, false);
            changeStatusPopUp.dismiss();
        });

        tv_recommendation.setOnClickListener(v -> {
            ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentRecommendations, null, false, false);
            changeStatusPopUp.dismiss();
        });

        ll_reminder.setOnClickListener(v -> {
            ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentReminder, null, false, false);
            changeStatusPopUp.dismiss();
        });
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
        new InteractorImpl(activity, FragmentArchivedProject.this, Interactor.RequestCode_GetAllReminder, Interactor.Tag_GetAllReminder)
                .makeJsonPostRequest(Interactor.Method_GetAllReminder, jsonObject, true);

    }


    @Override
    public void onSliderButtonClick(View view) {
        float centreX = 0;//(view.getX() + view.getWidth()) / 2;
        float centreY = Utilities.getInstance().getValueInDP(120, activity);
        super.onSliderButtonClick(view);
        switch (view.getId()) {
            case R.id.iv_sliderButton:
                try {
                    if (changeStatusPopUp != null && changeStatusPopUp.isShowing()) {
                        changeStatusPopUp.dismiss();
                    } else {
                        showStatusPopup(centreX, centreY);
                    }
                } catch (Exception e) {
                    ApplicationContext.getInstance().catException(e);
                }
                break;
            case R.id.iv_settingButton:
                try {
                    PopupUtils.getInstance().setEditProfilePopUp(activity, centreX, centreY);
                } catch (Exception e) {
                    ApplicationContext.getInstance().catException(e);
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.iv_projectShowHide:
                if (ll_projectDetail.getVisibility() == View.VISIBLE) {
                    ll_projectDetail.setVisibility(View.INVISIBLE);
                    ll_projectDetail.setVisibility(View.GONE);
                    iv_projectShowHide.setImageResource(R.drawable.pluse);
                    iv_download.setVisibility(View.GONE);
                } else {
                    ll_projectDetail.setVisibility(View.VISIBLE);
                    iv_projectShowHide.setImageResource(R.drawable.minus);
                    if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
                        if (ApplicationContext.getInstance().project.getPercentCompleted().equalsIgnoreCase("100")) {
                            iv_download.setVisibility(View.VISIBLE);
                        }
                    }
                }
                break;

            case R.id.iv_editEstimatedHandoverDate:
                changeHandoverDatePopup();
                break;

            case R.id.iv_designerShowHide:
                designerShowHide();
                break;
            case R.id.iv_clientShowHide:
                clientShowHide();
                break;
            case R.id.tv_completeProject:
                completProjectDialog();
//                editProjectDetail();
                break;

            case R.id.iv_download:
                Utilities.getInstance().openWebUrl("http://www.superapp.co.in/superapp_v1/interior-designer/index3.php?pid="
                        + ApplicationContext.getInstance().project.getProjectId() + "&uid=" + PrefSetup.getInstance().getUserId(), getActivity());
                break;
        }
    }

    private void clientShowHide() {
        if (ll_clientInfo.getVisibility() == View.VISIBLE) {
            ll_clientInfo.setVisibility(View.GONE);
            iv_clientShowHide.setImageResource(R.drawable.pluse);
        } else {
            ll_clientInfo.setVisibility(View.VISIBLE);
            iv_clientShowHide.setImageResource(R.drawable.minus);
        }
    }

    private void designerShowHide() {
        if (ll_designerInfo.getVisibility() == View.VISIBLE) {
            ll_designerInfo.setVisibility(View.GONE);
            iv_designerShowHide.setImageResource(R.drawable.pluse);
        } else {
            ll_designerInfo.setVisibility(View.VISIBLE);
            iv_designerShowHide.setImageResource(R.drawable.minus);
        }
    }

    private void getProjectDetail() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
            jsonObject.put("projectId", ApplicationContext.getInstance().project.getProjectId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            new InteractorImpl(activity, FragmentArchivedProject.this, Interactor.RequestCode_GetProjectDetail, Interactor.Tag_GetProjectDetail)
                    .makeJsonPostRequest(Interactor.Method_GetProjectDetail, jsonObject, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
        if (responsePacket.getErrorCode() == 0) {
            if (Interactor.RequestCode_GetProjectDetail == requestCode) {
                if (responsePacket.getProjectDetail() != null) {
                    updateProjectView(responsePacket.getProjectDetail());
                }

                if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
                    updateClientDetail();
                    if (responsePacket.getClientDetail() != null) {
                        updateClientView(responsePacket.getClientDetail());
                    }
                    if (responsePacket.getCoWorkerList() != null) {
                        updateCoWorkerView(responsePacket.getCoWorkerList());
                    }
                } else if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("c")) {
                    // TODO
                    updateDesignerDetail();
                    if (responsePacket.getDesignerDetail() != null) {
                        updateDesignerView(responsePacket.getDesignerDetail());
                    }
                }
            } else if (Interactor.RequestCode_SendID == requestCode) {
                activity.makeToast(responsePacket.getMessage());
            } else if (Interactor.RequestCode_ProjectComplete == requestCode) {
                activity.makeToast(responsePacket.getMessage());
//            } else if (Interactor.RequestCode_DesignerRating == requestCode) {
//                activity.makeToast(responsePacket.getMessage());
            } else if (Interactor.RequestCode_GetAllReminder == requestCode) {
                if (responsePacket.getErrorCode() == 0) {
                    reminderCounter = responsePacket.getCounter();
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
