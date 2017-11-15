package com.superapp.fragment.membership;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.ActivityLogin;
import com.superapp.activity.ActivityMain;
import com.superapp.activity.base.ErrorType;
import com.superapp.activity.base.OnViewClickListener;
import com.superapp.beans.ProjectBean;
import com.superapp.fragment.base.BaseFragment;
import com.superapp.paytm.MerchantActivity;
import com.superapp.utils.PopupUtils;
import com.superapp.utils.PrefSetup;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.OnResponseListener;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

public class FragDowngradeMembership extends BaseFragment {
    private View fragmentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentView = inflater.inflate(R.layout.frag_membership, container, false);
        initView();
        return fragmentView;
    }

    LinearLayout ll_premium, ll_paid, ll_free;
    CheckBox chkBox_premium, chkBox_paid, chkBox_free;
    Button btn_submit;

    @Override
    public void initView() {
        ll_premium = (LinearLayout) fragmentView.findViewById(R.id.ll_premium);
        ll_paid = (LinearLayout) fragmentView.findViewById(R.id.ll_paid);
        ll_free = (LinearLayout) fragmentView.findViewById(R.id.ll_free);

        chkBox_premium = (CheckBox) setTouchNClick(R.id.chkBox_premium, fragmentView);
        chkBox_paid = (CheckBox) setTouchNClick(R.id.chkBox_paid, fragmentView);
        chkBox_free = (CheckBox) setTouchNClick(R.id.chkBox_free, fragmentView);

        btn_submit = (Button) fragmentView.findViewById(R.id.btn_submit);
        btn_submit.setVisibility(View.GONE);

        if (PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("1")) {
            ll_premium.setVisibility(View.GONE);
            ll_paid.setVisibility(View.GONE);
            ll_free.setVisibility(View.GONE);
        } else if (PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("2")) {
            ll_premium.setVisibility(View.GONE);
            ll_paid.setVisibility(View.GONE);
            ll_free.setVisibility(View.VISIBLE);
        } else if (PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("3")) {
            ll_premium.setVisibility(View.GONE);
            ll_paid.setVisibility(View.VISIBLE);
            ll_free.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActivityMain) activity).setHeaderText(getString(R.string.downgradeMembership));
        ((ActivityMain) activity).changeHeaderButton(true);
        ((ActivityMain) activity).showHideFloatingButton(false, R.drawable.add);
        ((ActivityMain) activity).showHideNotificationButton(true);

        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
            ((ActivityMain) activity).showHIdeSearchButton(true);
        } else {
            ((ActivityMain) activity).showHIdeSearchButton(false);
        }

        ((ActivityMain) activity).showAd(false);
    }

    private void setCheckPremium() {
        chkBox_premium.setChecked(true);
        chkBox_paid.setChecked(false);
        chkBox_free.setChecked(false);
    }

    private void setCheckPaid() {
        chkBox_paid.setChecked(true);
        chkBox_premium.setChecked(false);
        chkBox_free.setChecked(false);
    }

    private void setCheckFree() {
        chkBox_free.setChecked(true);
        chkBox_premium.setChecked(false);
        chkBox_paid.setChecked(false);
    }

    private void getProjectListPopup(int downGradeType) {
        PopupUtils.getInstance().showSelectProjectPopup(activity, "Select Project", object -> {
        }, downGradeType);
    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch (arg0.getId()) {
//            case R.id.chkBox_premium:
//                setCheckPremium();
//                getProjectListPopup();
//                break;

            case R.id.chkBox_paid:
                setCheckPaid();
                downgrade();
                break;

            case R.id.chkBox_free:
                setCheckFree();
                downgrade();
                break;
        }
    }

    ArrayList<String> arr = new ArrayList<>();

    public void downgrade() {
        if (chkBox_free.isChecked()) {
            if (ApplicationContext.getInstance().getProjectList().size() < 3) {
                PopupUtils.getInstance().showYesNoDialog(activity, "DowngradeMemberShip", "Are you sure you want downgrade your memberShip", new OnViewClickListener() {
                    @Override
                    public void onViewItemClick(Object object) {
                        for (ProjectBean list : ApplicationContext.getInstance().getProjectList()) {
                            arr.add(String.valueOf(list.getProjectId()));
                        }

//                        Intent intent = new Intent(getActivity(), MerchantActivity.class);
//                        intent.putExtra("memberShipType", chkBox_paid.isChecked() ? "Paid" : "0");
//                        intent.putExtra("transactionAmount", chkBox_paid.isChecked() ? "60" : "0");
//                        intent.putExtra("downgradeType", 1);
//                        intent.putExtra("projectsList", arr.toString().replace("[", "").replace("]", ""));
//                        startActivity(intent);
                        updateMembership(1, arr.toString().replace("[", "").replace("]", ""));
                    }
                });
            } else {
                getProjectListPopup(1);
            }

        } else if (chkBox_paid.isChecked()) {
            if (ApplicationContext.getInstance().getProjectList().size() < 6) {
                PopupUtils.getInstance().showYesNoDialog(activity, "DowngradeMemberShip", "Are you sure you want downgrade your memberShip", new OnViewClickListener() {
                    @Override
                    public void onViewItemClick(Object object) {
                        for (ProjectBean list : ApplicationContext.getInstance().getProjectList()) {
                            arr.add(String.valueOf(list.getProjectId()));
                        }

                        Intent intent = new Intent(getActivity(), MerchantActivity.class);
                        intent.putExtra("memberShipType", "Paid");
                        intent.putExtra("transactionAmount", "60");
                        intent.putExtra("downgradeType", 2);
                        intent.putExtra("projectsList", arr.toString().replace("[", "").replace("]", ""));
                        startActivity(intent);
                    }
                });
            } else {
                getProjectListPopup(2);
            }
        }
    }

    HashSet<String> hashSet = new HashSet<>();

    public void updateMembership(int downGradeId, String projectList) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("membershipId", downGradeId);
            if (downGradeId == 1) {
                jsonObject.put("orderId", "");
            }
            jsonObject.put("projectList", projectList);

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
                Intent intent = new Intent(getActivity(), ActivityMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                activity.finish();
//                activity.startActivity(new Intent(ApplicationContext.getInstance(), ActivityMain.class));


            }

            @Override
            public void onError(int requestCode, ErrorType errorType) {

            }
        }, Interactor.RequestCode_updateMembership, Interactor.Tag_updateMembership)
                .makeJsonPostRequest(Interactor.Method_updateMembership, jsonObject, true);
    }


    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
        if (responsePacket.getErrorCode() == 410) {
            return;
        }
    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {
        super.onError(requestCode, errorType);
    }
}
