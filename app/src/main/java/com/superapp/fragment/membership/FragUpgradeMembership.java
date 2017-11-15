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
import com.superapp.activity.ActivityMain;
import com.superapp.activity.base.ErrorType;
import com.superapp.activity.base.OnViewClickListener;
import com.superapp.beans.ProjectBean;
import com.superapp.fragment.base.BaseFragment;
import com.superapp.paytm.MerchantActivity;
import com.superapp.utils.PopupUtils;
import com.superapp.utils.PrefSetup;
import com.superapp.webservice.ResponsePacket;

import java.util.ArrayList;
import java.util.HashSet;

public class FragUpgradeMembership extends BaseFragment {

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

    ArrayList<String> arr = new ArrayList<>();

    @Override
    public void initView() {
        ll_premium = (LinearLayout) fragmentView.findViewById(R.id.ll_premium);
        ll_paid = (LinearLayout) fragmentView.findViewById(R.id.ll_paid);
        ll_free = (LinearLayout) fragmentView.findViewById(R.id.ll_free);

        chkBox_premium = (CheckBox) setTouchNClick(R.id.chkBox_premium, fragmentView);
        chkBox_paid = (CheckBox) setTouchNClick(R.id.chkBox_paid, fragmentView);
        chkBox_free = (CheckBox) setTouchNClick(R.id.chkBox_free, fragmentView);

        btn_submit = (Button) setTouchNClick(R.id.btn_submit, fragmentView);

        if (PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("1")) {
            ll_premium.setVisibility(View.VISIBLE);
            ll_paid.setVisibility(View.VISIBLE);
            ll_free.setVisibility(View.GONE);
        } else if (PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("2")) {
            ll_premium.setVisibility(View.VISIBLE);
            ll_paid.setVisibility(View.GONE);
            ll_free.setVisibility(View.GONE);
        } else if (PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("3")) {
            ll_premium.setVisibility(View.GONE);
            ll_paid.setVisibility(View.GONE);
            ll_free.setVisibility(View.GONE);
        }

        if (PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("1")) {
            chkBox_premium.setChecked(true);
        } else if (PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("2")) {
            chkBox_premium.setChecked(true);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActivityMain) activity).setHeaderText(getString(R.string.upgradeMembership));
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

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch (arg0.getId()) {
            case R.id.btn_submit:
                PopupUtils.getInstance().showYesNoDialog(getActivity(), "Upgrade Membership", "Are you want to upgrade membership?", object -> {
                    for (ProjectBean list : ApplicationContext.getInstance().getProjectList()) {
                        arr.add(String.valueOf(list.getProjectId()));
                    }

                    Intent intent = new Intent(getActivity(), MerchantActivity.class);
                    intent.putExtra("memberShipType", chkBox_paid.isChecked() ? "Paid" : chkBox_premium.isChecked() ? "Premium" : "0");
                    intent.putExtra("transactionAmount", chkBox_paid.isChecked() ? "60" : chkBox_premium.isChecked() ? "550" : "0");
                    intent.putExtra("downgradeType", chkBox_paid.isChecked() ? 2 : chkBox_premium.isChecked() ? 3 : 0);
                    intent.putExtra("projectsList", arr.toString().replace("[", "").replace("]", ""));
                    startActivity(intent);
                });
                break;

            case R.id.chkBox_premium:
                setCheckPremium();
                break;

            case R.id.chkBox_paid:
                setCheckPaid();
                break;

        }
    }


    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
        if (responsePacket.getErrorCode() == 410) {

        }
    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {
        super.onError(requestCode, errorType);
    }
}
