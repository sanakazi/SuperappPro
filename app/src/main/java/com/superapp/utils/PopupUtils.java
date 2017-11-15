package com.superapp.utils;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.ActivityChangeMNumber;
import com.superapp.activity.ActivityChangePassword;
import com.superapp.activity.ActivityMain;
import com.superapp.activity.ActivitySurvey;
import com.superapp.activity.ActivityWebView;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.ErrorType;
import com.superapp.activity.base.OnViewClickListener;
import com.superapp.beans.BeanAddress;
import com.superapp.beans.BeanSelectCategory;
import com.superapp.beans.Region;
import com.superapp.fragment.base.FragmentNames;
import com.superapp.fragment.checklist.category.AdapterSelectCategory;
import com.superapp.paytm.MerchantActivity;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.OnResponseListener;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class PopupUtils {
    private PopupUtils() {
    }

    private static PopupUtils instance;

    HashSet<String> hashSet = new HashSet<>();

    public static PopupUtils getInstance() {
        if (null == instance) {
            instance = new PopupUtils();
        }
        return instance;
    }


    public void showImageDialog(final Context activity, final String url) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_image, null, false);
        dialog.setContentView(view);

        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        ImageView iv_fullScreenImage = (ImageView) view.findViewById(R.id.iv_fullScreenImage);
        ApplicationContext.getInstance().loadImage(url, iv_fullScreenImage, progressBar, 0);

        view.findViewById(R.id.iv_close).setOnClickListener(v ->
                dialog.dismiss()
        );
    }


    public void showUpdateAppDialog(final Context activity, final String title, final String message, final String btnNo, final String btnYes, OnViewClickListener listener) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_ok_cancel, null, false);
        dialog.setContentView(view);

        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        TextView tv_popupTitle = (TextView) view.findViewById(R.id.tv_popupTitle);
        tv_popupTitle.setText(title);
        TextView tv_message = (TextView) view.findViewById(R.id.tv_message);
        tv_message.setText(message);

        Button btn_no = (Button) view.findViewById(R.id.btn_no);
        btn_no.setText(btnNo);
        btn_no.setOnClickListener(v -> dialog.dismiss());


        Button btn_yes = (Button) view.findViewById(R.id.btn_yes);
        btn_yes.setText(btnYes);
        btn_yes.setOnClickListener(v -> {
            if (listener != null) {
                listener.onViewItemClick(null);
            }
            dialog.dismiss();
        });

        view.findViewById(R.id.iv_close).setOnClickListener(v ->
                dialog.dismiss()
        );
    }


    public void showYesNoDialog(final Context activity, final String title, final String message, OnViewClickListener listener) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_ok_cancel, null, false);
        dialog.setContentView(view);

        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        TextView tv_popupTitle = (TextView) view.findViewById(R.id.tv_popupTitle);
        tv_popupTitle.setText(title);
        TextView tv_message = (TextView) view.findViewById(R.id.tv_message);
        tv_message.setText(message);
        view.findViewById(R.id.btn_no).setOnClickListener(v ->
                dialog.dismiss()
        );

        view.findViewById(R.id.btn_yes).setOnClickListener(v -> {
            if (listener != null) {
                listener.onViewItemClick(null);
            }
            dialog.dismiss();
        });

        view.findViewById(R.id.iv_close).setOnClickListener(v ->
                dialog.dismiss()
        );
    }

    public void showDialogWithEditText(final Context activity, final String title, OnViewClickListener listener) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_with_edit_text, null, false);
        dialog.setContentView(view);

        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        TextView tv_popupTitle = (TextView) view.findViewById(R.id.tv_popupTitle);
        tv_popupTitle.setText(title);

        EditText et_reason = (EditText) view.findViewById(R.id.et_reason);

        view.findViewById(R.id.btn_submit).setOnClickListener(v -> {
            if (TextUtils.isEmpty(et_reason.getText().toString())) {
                et_reason.requestFocus();
                et_reason.setError(activity.getString(R.string.thisFieldIsRequired));
                return;
            }

            if (listener != null) {
                listener.onViewItemClick(et_reason.getText().toString());
            }
            dialog.dismiss();
        });

        view.findViewById(R.id.iv_close).setOnClickListener(v ->
                dialog.dismiss()
        );
    }

    public void showClientAddressPopup(final BaseAppCompatActivity activity, final OnViewClickListener onDoneButtonClick, BeanAddress address, final String title) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_address, null, false);
        dialog.setContentView(view);

        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        final TextView tv_addressTitle = (TextView) view.findViewById(R.id.tv_addressTitle);
        tv_addressTitle.setText(title);
        final EditText et_addressLine01 = (EditText) view.findViewById(R.id.et_addressLine01);
        final EditText et_addressLine02 = (EditText) view.findViewById(R.id.et_addressLine02);
        final EditText et_addressLine03 = (EditText) view.findViewById(R.id.et_addressLine03);
        final EditText et_city = (EditText) view.findViewById(R.id.et_city);
        final EditText et_pinCode = (EditText) view.findViewById(R.id.et_pinCode);

        try {
            if (address != null) {
                et_addressLine01.setText(address.getAddressLine1());
                et_addressLine02.setText(address.getAddressLine2());
                if (!address.getAddressLine3().equalsIgnoreCase("N/A")) {
                    et_addressLine03.setText(address.getAddressLine3());
                }
                et_city.setText(address.getCity());
                et_pinCode.setText(address.getPincode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);

        iv_close.setOnClickListener(v -> {
            Utilities.getInstance().hideKeyboard(activity);
            dialog.dismiss();
        });

        Button btn_popUpAddressDone = (Button) view.findViewById(R.id.btn_popUpAddressDone);
        btn_popUpAddressDone.setOnClickListener(v -> {
            if (onDoneButtonClick != null) {
                if (et_addressLine01.getText().toString().trim().equals("")) {
                    et_addressLine01.setError("This field is required");
                    et_addressLine01.requestFocus();
                    return;
                } else {
                    et_addressLine01.setError(null);
                }

                if (et_addressLine02.getText().toString().trim().equals("")) {
                    et_addressLine02.setError("This field is required");
                    et_addressLine02.requestFocus();
                    return;
                } else {
                    et_addressLine02.setError(null);
                }

//                if (et_addressLine03.getText().toString().trim().equals("")) {
//                    et_addressLine03.setError("This field is required");
//                    et_addressLine03.requestFocus();
//                    return;
//                } else {
//                    et_addressLine03.setError(null);
//                }

                if (et_city.getText().toString().trim().equals("")) {
                    et_city.setError("This field is required");
                    et_city.requestFocus();
                    return;
                } else {
                    et_city.setError(null);
                }

                if (et_pinCode.getText().toString().trim().equals("")) {
                    et_pinCode.setError("This field is required");
                    et_pinCode.requestFocus();
                    return;
                } else {
                    et_pinCode.setError(null);
                }

                BeanAddress addressTemp = new BeanAddress();
                addressTemp.setAddressLine1(et_addressLine01.getText().toString());
                addressTemp.setAddressLine2(et_addressLine02.getText().toString());
                if (TextUtils.isEmpty(et_addressLine03.getText().toString())) {
                    addressTemp.setAddressLine3("N/A");
                } else {
                    addressTemp.setAddressLine3(et_addressLine03.getText().toString());
                }
                addressTemp.setCity(et_city.getText().toString());
                addressTemp.setPincode(et_pinCode.getText().toString());
                onDoneButtonClick.onViewItemClick(addressTemp);
            }
            dialog.dismiss();
        });
        et_addressLine01.requestFocus();
        Utilities.getInstance().showKeyboard(activity);
        et_addressLine01.requestFocus();
    }

//    public void showCoWorkerOccupationPopUp(final Context activity, final OnViewClickListener onItemClick) {
//        final Dialog dialog = new Dialog(activity);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.popup_list_with_other, null, false);
//        dialog.setContentView(view);
//
//        final Window window = dialog.getWindow();
//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.show();
//
//        final ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
//        final LinearLayout ll_other = (LinearLayout) view.findViewById(R.id.ll_other);
//        final EditText et_otherOccupation = (EditText) view.findViewById(R.id.et_otherOccupation);
//        final TextView tv_done = (TextView) view.findViewById(R.id.tv_done);
//        ll_other.setVisibility(View.GONE);
//
//        iv_close.setOnClickListener((View v) -> dialog.dismiss());
//
//        final ListView lv_occupationList = (ListView) view.findViewById(R.id.lv_occupationList);
//        lv_occupationList.setOnItemClickListener((adapterView, view12, i, l) -> {
//            BeanOccupation beanOccupation = (BeanOccupation) ((ListView) adapterView).getAdapter().getItem(i);
//            if (beanOccupation.getId() == -1) {
//                ll_other.setVisibility(View.VISIBLE);
//                et_otherOccupation.requestFocus();
//                return;
//            } else {
//                ll_other.setVisibility(View.GONE);
//            }
//            if (onItemClick != null) {
//                onItemClick.onViewItemClick(beanOccupation);
//            }
//            dialog.dismiss();
//        });
//
//        tv_done.setOnClickListener(view1 -> {
//            if (TextUtils.isEmpty(et_otherOccupation.getText().toString())) {
//                et_otherOccupation.setError("Required filed");
//                et_otherOccupation.requestFocus();
//                return;
//            }
//            BeanOccupation beanOccupation = new BeanOccupation(0, et_otherOccupation.getText().toString());
//            if (onItemClick != null) {
//                onItemClick.onViewItemClick(beanOccupation);
//            }
//            dialog.dismiss();
//        });
//
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
//            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        new InteractorImpl(activity, new OnResponseListener() {
//            @Override
//            public void onSuccess(int requestCode, ResponsePacket responsePacket) {
//                ArrayList<BeanOccupation> list = responsePacket.getOccupationArray();
//                BeanOccupation bean = new BeanOccupation(-1, "Other");
//                list.add(bean);
//                AdapterOccupation adapterOccupation = new AdapterOccupation(ApplicationContext.getInstance(), list);
//                lv_occupationList.setAdapter(adapterOccupation);
//            }
//
//            @Override
//            public void onError(int requestCode, ErrorType errorType) {
//
//            }
//        }, Interactor.RequestCode_GetOccupations, Interactor.Tag_GetOccupations)
//                .makeJsonPostRequest(Interactor.Method_GetOccupations, jsonObject, true);
//    }

    private PopupWindow editProfilePopUp;

    public void setEditProfilePopUp(final BaseAppCompatActivity activity, float x, float y) {
        // LinearLayout viewGroup = (LinearLayout)findViewById(R.id.ll_projectEditProfile);
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.project_detail_edit_profile, null, false);

        editProfilePopUp = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        editProfilePopUp.setBackgroundDrawable(new ColorDrawable());
        editProfilePopUp.setTouchable(true);
        editProfilePopUp.setOutsideTouchable(true);
        editProfilePopUp.setFocusable(true);
        editProfilePopUp.showAtLocation(layout, Gravity.NO_GRAVITY, (int) x, (int) y);

        LinearLayout ll_settings = (LinearLayout) layout.findViewById(R.id.ll_settings);
        LinearLayout ll_settingsList = (LinearLayout) layout.findViewById(R.id.ll_settingsList);

        TextView tv_upgradeMembership = (TextView) layout.findViewById(R.id.tv_upgradeMembership);
        TextView tv_renewMembership = (TextView) layout.findViewById(R.id.tv_renewMembership);
        TextView tv_downgradeMembership = (TextView) layout.findViewById(R.id.tv_downgradeMembership);
        TextView tv_editProfile = (TextView) layout.findViewById(R.id.tv_editProfile);
        TextView tv_changePassword = (TextView) layout.findViewById(R.id.tv_changePassword);
        TextView tv_changeMobileNumber = (TextView) layout.findViewById(R.id.tv_changeMobileNumber);

        LinearLayout ll_about = (LinearLayout) layout.findViewById(R.id.ll_about);

        LinearLayout ll_support = (LinearLayout) layout.findViewById(R.id.ll_support);
        LinearLayout ll_supportOptions = (LinearLayout) layout.findViewById(R.id.ll_supportOptions);

        TextView tv_helpLearning = (TextView) layout.findViewById(R.id.tv_helpLearning);
        TextView tv_feedback = (TextView) layout.findViewById(R.id.tv_feedback);
        TextView tv_checkForUpdates = (TextView) layout.findViewById(R.id.tv_checkForUpdates);

        TextView tv_termsOfUse = (TextView) layout.findViewById(R.id.tv_termsOfUse);
        TextView tv_privacyPolicies = (TextView) layout.findViewById(R.id.tv_privacyPolicies);
        TextView tv_patentInformation = (TextView) layout.findViewById(R.id.tv_patentInformation);


        LinearLayout ll_legal = (LinearLayout) layout.findViewById(R.id.ll_legal);
        LinearLayout ll_legalOptions = (LinearLayout) layout.findViewById(R.id.ll_legalOptions);

        TextView tv_logOutPopUpWindow = (TextView) layout.findViewById(R.id.tv_logOutPopUpWindow);

        LinearLayout ll_project = (LinearLayout) layout.findViewById(R.id.ll_project);
        final LinearLayout ll_projectList = (LinearLayout) layout.findViewById(R.id.ll_projectList);

        TextView tv_rateUsOnPlayStore = (TextView) layout.findViewById(R.id.tv_rateUsOnPlayStore);
        TextView tv_inviteFriends = (TextView) layout.findViewById(R.id.tv_inviteFriends);

        TextView tv_survey = (TextView) layout.findViewById(R.id.tv_survey);

        TextView tv_archivedProject = (TextView) layout.findViewById(R.id.tv_archivedProject);

        ll_projectList.setVisibility(View.GONE);

        ll_projectList.removeAllViews();


        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("c")) {
            tv_upgradeMembership.setVisibility(View.GONE);
            tv_renewMembership.setVisibility(View.GONE);
            tv_downgradeMembership.setVisibility(View.GONE);
            ll_project.setVisibility(View.GONE);
            tv_survey.setVisibility(View.GONE);
            tv_archivedProject.setVisibility(View.GONE);

        } else if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("w")) {
            tv_upgradeMembership.setVisibility(View.GONE);
            tv_renewMembership.setVisibility(View.GONE);
            tv_downgradeMembership.setVisibility(View.GONE);
            ll_project.setVisibility(View.GONE);
            tv_survey.setVisibility(View.GONE);
            tv_archivedProject.setVisibility(View.GONE);
        }

        ll_settings.setOnClickListener(v -> {
            if (ll_settingsList.getVisibility() == View.VISIBLE)
                ll_settingsList.setVisibility(View.GONE);
            else
                ll_settingsList.setVisibility(View.VISIBLE);
        });

        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
            if (PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("1")) {
                tv_upgradeMembership.setVisibility(View.VISIBLE);
                tv_renewMembership.setVisibility(View.GONE);
                tv_downgradeMembership.setVisibility(View.GONE);
                tv_survey.setVisibility(View.GONE);
            } else if (PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("2")) {
                tv_upgradeMembership.setVisibility(View.VISIBLE);
                tv_renewMembership.setVisibility(View.VISIBLE);
                tv_downgradeMembership.setVisibility(View.VISIBLE);
            } else if (PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("3")) {
                tv_upgradeMembership.setVisibility(View.GONE);
                tv_renewMembership.setVisibility(View.VISIBLE);
                tv_downgradeMembership.setVisibility(View.VISIBLE);
            }
        }

        tv_upgradeMembership.setOnClickListener(v -> {
            editProfilePopUp.dismiss();
            ((ActivityMain) activity).replaceFragment(FragmentNames.FragUpgradeMembership, null, false, false);
        });

        tv_downgradeMembership.setOnClickListener(v -> {
            editProfilePopUp.dismiss();
            ((ActivityMain) activity).replaceFragment(FragmentNames.FragDowngradeMembership, null, false, false);
        });

        tv_renewMembership.setOnClickListener(v -> {
            Intent intent = new Intent(activity, MerchantActivity.class);
            if (PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("2")) {
                intent.putExtra("memberShipType", "Paid");
                intent.putExtra("transactionAmount", "60");
            } else if (PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("3")) {
                intent.putExtra("memberShipType", "Premium");
                intent.putExtra("transactionAmount", "550");
            }
            activity.startActivity(intent);
            editProfilePopUp.dismiss();
        });


        tv_editProfile.setOnClickListener(v -> {
            if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
                ((ActivityMain) activity).replaceFragment(FragmentNames.FragEditProfile, null, false, false);
            } else if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("c")) {
                ((ActivityMain) activity).replaceFragment(FragmentNames.FragEditProfileClient, null, false, false);
            } else if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("w")) {
                if (PrefSetup.getInstance().getSUPER_SERACH_CHECK().equalsIgnoreCase("t")) {
                    ((ActivityMain) activity).replaceFragment(FragmentNames.FragSSEditProfileCoworker, null, false, false);
                } else {
                    ((ActivityMain) activity).replaceFragment(FragmentNames.FragEditProfileCoworker, null, false, false);
                }
            }
            editProfilePopUp.dismiss();
        });

        tv_changePassword.setOnClickListener(v -> {
            Intent intent = new Intent(activity, ActivityChangePassword.class);
            activity.startActivity(intent);
            editProfilePopUp.dismiss();
        });

        tv_changeMobileNumber.setOnClickListener(v -> {
            Intent intent = new Intent(activity, ActivityChangeMNumber.class);
            activity.startActivity(intent);
            editProfilePopUp.dismiss();
        });

        ll_about.setOnClickListener(v -> {
            if (ll_support.getVisibility() == View.VISIBLE && ll_legal.getVisibility() == View.VISIBLE) {
                ll_supportOptions.setVisibility(View.GONE);
                ll_support.setVisibility(View.GONE);
                ll_legalOptions.setVisibility(View.GONE);
                ll_legal.setVisibility(View.GONE);
            } else {
                ll_support.setVisibility(View.VISIBLE);
                ll_legal.setVisibility(View.VISIBLE);
            }

            ll_support.setOnClickListener(v1 -> {
                if (ll_supportOptions.getVisibility() == View.VISIBLE) {
                    ll_supportOptions.setVisibility(View.GONE);
                } else {
                    ll_supportOptions.setVisibility(View.VISIBLE);
                }
            });

            ll_legal.setOnClickListener(v1 -> {
                if (ll_legalOptions.getVisibility() == View.VISIBLE) {
                    ll_legalOptions.setVisibility(View.GONE);
                } else {
                    ll_legalOptions.setVisibility(View.VISIBLE);
                }
            });
        });

        ll_project.setOnClickListener(v -> {
            editProfilePopUp.dismiss();
            ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentSettings, null, false, false);
        });

        tv_archivedProject.setOnClickListener(v -> {
            editProfilePopUp.dismiss();
            ((ActivityMain) activity).replaceFragment(FragmentNames.FragDashArchivedProject, null, false, false);
        });

        tv_survey.setOnClickListener(v -> {
//            Intent i = new Intent(activity, ActivitySurvey.class);
//            activity.startActivity(i);
            editProfilePopUp.dismiss();
            ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentSurvey, null, false, false);

        });


        tv_inviteFriends.setOnClickListener(v -> {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Super App");
                String sAux = "\nLet me recommend you this application\n\n";
//                sAux = sAux + "https://play.google.com/store/apps/details?id=com.superapp \n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=com.superapppro&hl=en \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                activity.startActivity(Intent.createChooser(i, "choose one"));
            } catch (Exception e) { //e.toString();
            }
            editProfilePopUp.dismiss();
        });


        tv_helpLearning.setOnClickListener(v -> {
//            Intent intent = new Intent(activity, ActivityWebView.class);
//            intent.putExtra("filePath", "http://ssspl.biz/our-products/superapp/superapppro-user-guide/");
//            activity.startActivity(intent);
            Utilities.getInstance().openWebUrl("http://ssspl.biz/our-products/superapp/superapppro-user-guide/", activity);
            editProfilePopUp.dismiss();
        });

        tv_feedback.setOnClickListener(v -> {
            ((ActivityMain) activity).replaceFragment(FragmentNames.FragmentFeedback, null, false, false);
            editProfilePopUp.dismiss();
        });

        tv_checkForUpdates.setOnClickListener(v -> {
            checkForUpdate(activity);
            editProfilePopUp.dismiss();
        });

        tv_termsOfUse.setOnClickListener(v -> {
            Intent intent = new Intent(activity, ActivityWebView.class);
            intent.putExtra("filePath", "https://www.superapp.co.in/mobile-terms-of-use.html");
            activity.startActivity(intent);
            editProfilePopUp.dismiss();
        });

        tv_privacyPolicies.setOnClickListener(v -> {
            Intent intent = new Intent(activity, ActivityWebView.class);
            intent.putExtra("filePath", "https://www.superapp.co.in/mobile-privacy-policies.html");
            activity.startActivity(intent);
            editProfilePopUp.dismiss();
        });

        tv_patentInformation.setOnClickListener(v -> {
            Intent intent = new Intent(activity, ActivityWebView.class);
            intent.putExtra("filePath", "https://www.superapp.co.in/mobile-patent-information.html");
            activity.startActivity(intent);
            editProfilePopUp.dismiss();
        });


        tv_rateUsOnPlayStore.setOnClickListener(v -> {
            Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                activity.startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                activity.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + activity.getPackageName())));
            }
            editProfilePopUp.dismiss();
        });

        tv_logOutPopUpWindow.setOnClickListener(v -> {
            editProfilePopUp.dismiss();
            ((ActivityMain) activity).logoutUser();
        });

    }


    public void checkForUpdate(final Context activity) {
        int code = Utilities.getBuildVersionCode(activity);
        if (code > -1) {

            Map<String, String> map = new HashMap<>();
            map.put("versionType", "android");

            new InteractorImpl(activity, new OnResponseListener() {
                @Override
                public void onSuccess(int requestCode, ResponsePacket responsePacket) {
                    if (Interactor.RequestCode_checkVersionCodeUser == requestCode) {
                        if (responsePacket.getStatus().equalsIgnoreCase("success")) {
                            try {
                                if (Integer.valueOf(responsePacket.getVersionCode()) > Utilities.getBuildVersionCode(activity)) {
                                    PopupUtils.getInstance().showUpdateAppDialog(activity, "Superapp Pro", "New version of Superapp Pro is now available, please update from Play Store.", "Later", "Update", object -> {

                                        Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
                                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

                                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                        try {
                                            activity.startActivity(goToMarket);
                                        } catch (ActivityNotFoundException e) {
                                            activity.startActivity(new Intent(Intent.ACTION_VIEW,
                                                    Uri.parse("http://play.google.com/store/apps/details?id=" + activity.getPackageName())));
                                        }
                                    });
                                } else {
                                    Toast.makeText(activity, "You are using latest version of Superapp pro.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                            }
                        }
                    }
                }

                @Override
                public void onError(int requestCode, ErrorType errorType) {

                }
            }, Interactor.RequestCode_checkVersionCodeUser, Interactor.Tag_checkVersionCodeUser)
                    .makeFileUploadingRequest(Interactor.Method_checkVersionCodeUser, null, null, map, true);
        }
    }


    public void showSelectCategoryPopUp(final Context activity, final long parentId, final String type, final OnViewClickListener onItemClick, final String title) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_list, null, false);
        dialog.setContentView(view);

        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        final ListView lv_setCategoryList = (ListView) view.findViewById(R.id.lv_setCategoryList);
        final TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText(title);
        lv_setCategoryList.setOnItemClickListener((adapterView, view1, i, l) -> {
            if (onItemClick != null) {
                onItemClick.onViewItemClick(((ListView) adapterView).getAdapter().getItem(i));
            }
            dialog.dismiss();
        });

        final ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(v -> dialog.dismiss());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
//            jsonObject.put("parentCatId", parentId);
            if (!TextUtils.isEmpty(type))
                jsonObject.put("type", type);
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        new InteractorImpl(activity, new OnResponseListener() {
            @Override
            public void onSuccess(int requestCode, ResponsePacket responsePacket) {
                if (responsePacket.getErrorCode() == 0) {
                    if (responsePacket.getCategory() != null) {
                        AdapterSelectCategory selectCategory = new AdapterSelectCategory(ApplicationContext.getInstance(),
                                responsePacket.getCategory());
                        lv_setCategoryList.setAdapter(selectCategory);
                    }
                }
            }

            @Override
            public void onError(int requestCode, ErrorType errorType) {

            }
        }, Interactor.RequestCode_GetParentCategory, Interactor.Tag_GetParentCategory)
                .makeJsonPostRequest(Interactor.Method_GetParentCategory, jsonObject, true);
    }

    public void showSelectSubCategoryPopUp(final Context activity, final long parentId, final String type, final OnViewClickListener onItemClick, final String title) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_list, null, false);
        dialog.setContentView(view);

        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        final ListView lv_setCategoryList = (ListView) view.findViewById(R.id.lv_setCategoryList);
        final TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText(title);
        lv_setCategoryList.setOnItemClickListener((adapterView, view1, i, l) -> {
            if (onItemClick != null) {
                onItemClick.onViewItemClick(((ListView) adapterView).getAdapter().getItem(i));
            }
            dialog.dismiss();
        });

        final ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(v -> dialog.dismiss());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("categoryType", parentId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(activity, new OnResponseListener() {
            @Override
            public void onSuccess(int requestCode, ResponsePacket responsePacket) {
                if (responsePacket.getErrorCode() == 0) {
                    if (responsePacket.getCategory() != null) {
                        AdapterSelectCategory selectCategory = new AdapterSelectCategory(ApplicationContext.getInstance(),
                                responsePacket.getCategory());
                        lv_setCategoryList.setAdapter(selectCategory);
                    }
                }
            }

            @Override
            public void onError(int requestCode, ErrorType errorType) {

            }
        }, Interactor.RequestCode_GetAllCategory, Interactor.Tag_GetAllCategory)
                .makeJsonPostRequest(Interactor.Method_GetAllCategory, jsonObject, true);
    }

    public void showOccupationPopUp(final Context activity, final long parentId,
                                    final OnViewClickListener onItemClick, final String title, boolean searchEnable) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_list_with_other, null, false);
        dialog.setContentView(view);

        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        final ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
        final LinearLayout ll_other = (LinearLayout) view.findViewById(R.id.ll_other);
        final LinearLayout ll_searchMaterial = (LinearLayout) view.findViewById(R.id.ll_searchMaterial);
        final EditText et_otherOccupation = (EditText) view.findViewById(R.id.et_otherOccupation);
        final TextView tv_done = (TextView) view.findViewById(R.id.tv_done);
        final TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        final EditText et_searchMaterial = (EditText) view.findViewById(R.id.et_searchMaterial);
        if (searchEnable) {
            ll_searchMaterial.setVisibility(View.VISIBLE);
        } else {
            ll_searchMaterial.setVisibility(View.GONE);
        }
        tv_title.setText(title);
        ll_other.setVisibility(View.GONE);

        iv_close.setOnClickListener((View v) -> dialog.dismiss());

        final ListView lv_occupationList = (ListView) view.findViewById(R.id.lv_occupationList);
        lv_occupationList.setOnItemClickListener((adapterView, view12, i, l) -> {
            BeanSelectCategory beanSelectCategory = (BeanSelectCategory) ((ListView) adapterView).getAdapter().getItem(i);
            if (beanSelectCategory.getId() == -1) {
                ll_other.setVisibility(View.VISIBLE);
                et_otherOccupation.requestFocus();
                return;
            } else {
                ll_other.setVisibility(View.GONE);
            }
            if (onItemClick != null) {
                onItemClick.onViewItemClick(beanSelectCategory);
            }
            dialog.dismiss();
        });

        tv_done.setOnClickListener(view1 -> {
            if (TextUtils.isEmpty(et_otherOccupation.getText().toString())) {
                et_otherOccupation.setError("Required filed");
                et_otherOccupation.requestFocus();
                return;
            }

            BeanSelectCategory beanOccupation = new BeanSelectCategory(0, et_otherOccupation.getText().toString());
            if (onItemClick != null) {
                onItemClick.onViewItemClick(beanOccupation);
            }
            dialog.dismiss();
        });

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("categoryType", parentId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(activity, new OnResponseListener() {
            @Override
            public void onSuccess(int requestCode, ResponsePacket responsePacket) {
                ArrayList<BeanSelectCategory> list = responsePacket.getCategory();
                BeanSelectCategory bean = new BeanSelectCategory(-1, "Other");
                list.add(bean);
                AdapterSelectCategory adapterSelectCategory = new AdapterSelectCategory(ApplicationContext.getInstance(), list);
                lv_occupationList.setAdapter(adapterSelectCategory);
                et_searchMaterial.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // Call back the Adapter with current character to Filter
                        adapterSelectCategory.getFilter().filter(s.toString());
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
            }

            @Override
            public void onError(int requestCode, ErrorType errorType) {

            }
        }, Interactor.RequestCode_GetAllCategory, Interactor.Tag_GetAllCategory)
                .makeJsonPostRequest(Interactor.Method_GetAllCategory, jsonObject, true);
    }


    public void showMaterialListPopup(Context activity, final String title, final String hint, final OnViewClickListener onItemClick, ArrayList<BeanSelectCategory> data, boolean show) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_up_materiallist, null, false);
        dialog.setContentView(view);

        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        final ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
        final LinearLayout ll_other = (LinearLayout) view.findViewById(R.id.ll_other);
        final EditText et_otherOccupation = (EditText) view.findViewById(R.id.et_otherOccupation);
        final EditText et_searchMaterial = (EditText) view.findViewById(R.id.et_searchMaterial);
        EditText et_measurements = (EditText) view.findViewById(R.id.et_measurements);
        final TextView tv_done = (TextView) view.findViewById(R.id.tv_done);
        final TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText(title);
        et_searchMaterial.setHint(hint);
        ll_other.setVisibility(View.GONE);

        iv_close.setOnClickListener((View v) -> dialog.dismiss());
        ListView list = (ListView) view.findViewById(R.id.lv_occupationList);

        list.setOnItemClickListener((adapterView, view12, i, l) -> {
            BeanSelectCategory beanOccupation = (BeanSelectCategory) ((ListView) adapterView).getAdapter().getItem(i);
            if (beanOccupation.getId() == -1) {
                ll_other.setVisibility(View.VISIBLE);
                et_otherOccupation.requestFocus();
                return;
            } else {
                ll_other.setVisibility(View.GONE);
            }
            if (onItemClick != null) {
                onItemClick.onViewItemClick(beanOccupation);
            }
            dialog.dismiss();
        });

        tv_done.setOnClickListener(view1 -> {
            if (TextUtils.isEmpty(et_otherOccupation.getText().toString())) {
                et_otherOccupation.setError("Required filed");
                et_otherOccupation.requestFocus();
                return;
            }
            if (show) {
                if (TextUtils.isEmpty(et_measurements.getText().toString())) {
                    et_measurements.setError("Required filed");
                    et_measurements.requestFocus();
                    return;
                }

                BeanSelectCategory beanOccupation = new BeanSelectCategory(0, et_otherOccupation.getText().toString() + " " + et_measurements.getText().toString());

                if (onItemClick != null) {
                    onItemClick.onViewItemClick(beanOccupation);
                }
            } else {
                BeanSelectCategory beanOccupation = new BeanSelectCategory(0, et_otherOccupation.getText().toString());

                if (onItemClick != null) {
                    onItemClick.onViewItemClick(beanOccupation);
                }
            }
            dialog.dismiss();
        });

        BeanSelectCategory bean = new BeanSelectCategory(-1, "Other");
        data.add(bean);
        AdapterSelectCategory adapterOccupation = new AdapterSelectCategory(ApplicationContext.getInstance(), data);
        list.setAdapter(adapterOccupation);

        et_searchMaterial.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                adapterOccupation.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    public void showRegionListPopup(final Context activity, ArrayList<Region> data, final String title, final OnViewClickListener onItemClick) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_list, null, false);
        dialog.setContentView(view);

        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText(title);

        view.findViewById(R.id.iv_close).setOnClickListener(v ->
                dialog.dismiss());

        data.remove(0);
        final ListView list = (ListView) view.findViewById(R.id.list);
        ArrayAdapter<?> adapter = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, data);
        list.setAdapter(adapter);
        list.setOnItemClickListener((adapterView, view1, i, l) -> {
            if (onItemClick != null) {
                onItemClick.onViewItemClick(((ListView) adapterView).getAdapter().getItem(i));
            }
            dialog.dismiss();
        });
    }


    public void showSelectProjectPopup(final Context activity, final String title, final OnViewClickListener onItemClick, int downGradeType) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        hashSet.clear();
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_list_with_button, null, false);
        dialog.setContentView(view);

        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText(title);

        Button submit = (Button) view.findViewById(R.id.submit);

        view.findViewById(R.id.iv_close).setOnClickListener(v ->
                dialog.dismiss());


//        ArrayList<ProjectBean> arrayList = ApplicationContext.getInstance().getProjectList();
//        for (ProjectBean list : ApplicationContext.getInstance().getProjectList()) {
//            arrayList.add(list.getProjectName());
//        }
        final RecyclerView rv_list = (RecyclerView) view.findViewById(R.id.rv_list);
        GridLayoutManager manager = new GridLayoutManager(ApplicationContext.getInstance(), 1);
        rv_list.setHasFixedSize(true);
        rv_list.setLayoutManager(manager);
        rv_list.setItemAnimator(new DefaultItemAnimator());
        if (ApplicationContext.getInstance().getProjectList().size() > 0) {
            ProjectListAdapter adapter = new ProjectListAdapter(activity, ApplicationContext.getInstance().getProjectList(), (object, position) ->
                    setCheckedValue(String.valueOf(object.getProjectId()))
                    , downGradeType);
            rv_list.setAdapter(adapter);
        }

        ArrayList<String> arr = new ArrayList<>();

        view.findViewById(R.id.btn_submit).setOnClickListener(v -> {
            if (onItemClick != null) {
                if (downGradeType == 2) {
                    Intent intent = new Intent(activity, MerchantActivity.class);
                    intent.putExtra("memberShipType", "Paid");
                    intent.putExtra("transactionAmount", "60");
                    intent.putExtra("projectsList", arr.toString().replace("[", "").replace("]", ""));
                    activity.startActivity(intent);
                } else {

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
                        jsonObject.put("userId", PrefSetup.getInstance().getUserId());
                        jsonObject.put("membershipId", downGradeType);
                        if (downGradeType == 1) {
                            jsonObject.put("orderId", "");
                        }
//                        if (downGradeType == 2) {
//                            jsonObject.put("orderId", "");
//                        }
                        jsonObject.put("orderAmount", "0");
                        jsonObject.put("projectList", hashSet.toString().replace("[", "").replace("]", ""));
                        jsonObject.put("updateType", "downgrade");
                        jsonObject.put("transactionId", "");
                        jsonObject.put("bankTrnxId", "");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new InteractorImpl(activity, new OnResponseListener() {
                        @Override
                        public void onSuccess(int requestCode, ResponsePacket responsePacket) {
                            if (responsePacket.getErrorCode() == 0) {

//                                activity.startActivity(new Intent(ApplicationContext.getInstance(), ActivityMain.class));
                                Intent intent = new Intent(activity, ActivityMain.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                activity.startActivity(intent);
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onError(int requestCode, ErrorType errorType) {

                        }
                    }, Interactor.RequestCode_updateMembership, Interactor.Tag_updateMembership)
                            .makeJsonPostRequest(Interactor.Method_updateMembership, jsonObject, true);
                }
            }

        });
    }

    private void setCheckedValue(String value) {
        if (hashSet.contains(value)) {
            hashSet.remove(value);
        } else {
            hashSet.add(value);
        }
    }


}
//    public void showAdminNotificationDialog(final Context activity) {
//
//        final Dialog dialog = new Dialog(activity);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.activity_admin_notification, null, false);
//        dialog.setContentView(view);
//
//        final Window window = dialog.getWindow();
//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.show();
//
//        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
//
//        ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
//        ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
//
//        TextView tv_description = (TextView) view.findViewById(R.id.tv_description);
//        TextView tv_url = (TextView) view.findViewById(R.id.tv_url);
//
//        SpannableString content = new SpannableString("www.superapp.co.in");
//        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
//        tv_url.setText(content);
//
//        view.findViewById(R.id.iv_close).setOnClickListener(v ->
//                dialog.dismiss()
//        );
//
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
//            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
//            jsonObject.put("deviceType", "ANDROID");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        new InteractorImpl(activity, new OnResponseListener() {
//            @Override
//            public void onSuccess(int requestCode, ResponsePacket responsePacket) {
//                if (responsePacket.getErrorCode() == 0) {
//
//                    tv_title.setText(responsePacket.getNotificationInfo().getTitle());
//                    ApplicationContext.getInstance().loadImage(responsePacket.getNotificationInfo().getImage(), iv_image, null, R.drawable.no_image_new);
//
//                    tv_description.setText(responsePacket.getNotificationInfo().getDescription());
//
//                    SpannableString content = new SpannableString(responsePacket.getNotificationInfo().getLink());
//                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
//                    tv_url.setText(content);
//                }
//            }
//
//            @Override
//            public void onError(int requestCode, ErrorType errorType) {
//
//            }
//        }, Interactor.RequestCode_GetAllAdminNotification, Interactor.Tag_GetAllAdminNotification)
//                .makeJsonPostRequest(Interactor.Method_GetAllAdminNotification, jsonObject, true);
//    }