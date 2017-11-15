package com.superapp.fragment.membership;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.superapp.R;
import com.superapp.activity.ActivityMain;
import com.superapp.activity.base.ErrorType;
import com.superapp.fragment.base.BaseFragment;
import com.superapp.fragment.base.FragmentNames;
import com.superapp.paytm.MerchantActivity;
import com.superapp.utils.PrefSetup;
import com.superapp.utils.ProjectUtils;
import com.superapp.webservice.ResponsePacket;

import java.util.Calendar;

public class FragExpiredMembership extends BaseFragment {
    private View fragmentView;
    TextView tv_expiredMessage, tv_upgradeMembership, tv_downgradeMembership, tv_renewMembership;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentView = inflater.inflate(R.layout.frag_expired_membership, container, false);
        initView();
        return fragmentView;
    }


    @Override
    public void onResume() {
        super.onResume();
        ((ActivityMain) activity).setHeaderText(getString(R.string.renewMembership));
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


    @Override
    public void initView() {
//        long date = PrefSetup.getInstance().getUSER_MEMBER_SUBSCRIPTION_EXP_DATE();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(PrefSetup.getInstance().getUSER_MEMBER_SUBSCRIPTION_EXP_DATE() * 1000);

        tv_expiredMessage = (TextView) fragmentView.findViewById(R.id.tv_expiredMessage);
        tv_expiredMessage.setText(" Your subscription is expired on " + ProjectUtils.getInstance().getFormattedDate(calendar) + ". " + "Please renew / upgrade / downgrade your membership to continue.");

        tv_upgradeMembership = (TextView) fragmentView.findViewById(R.id.tv_upgradeMembership);
        tv_upgradeMembership.setOnClickListener(this);

        tv_downgradeMembership = (TextView) fragmentView.findViewById(R.id.tv_downgradeMembership);
        tv_downgradeMembership.setOnClickListener(this);
        tv_renewMembership = (TextView) fragmentView.findViewById(R.id.tv_renewMembership);
        tv_renewMembership.setOnClickListener(this);

        if (PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("3")) {
            tv_upgradeMembership.setVisibility(View.GONE);
        } else {
            tv_upgradeMembership.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch (arg0.getId()) {
            case R.id.tv_upgradeMembership:
                ((ActivityMain) getActivity()).removeExpView();
                ((ActivityMain) activity).replaceFragment(FragmentNames.FragUpgradeMembership, null, false, false);
                break;

            case R.id.tv_downgradeMembership:
                ((ActivityMain) getActivity()).removeExpView();
                ((ActivityMain) activity).replaceFragment(FragmentNames.FragDowngradeMembership, null, false, false);
                break;

            case R.id.tv_renewMembership:
                Intent intent = new Intent(activity, MerchantActivity.class);
                if (PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("2")) {
                    intent.putExtra("memberShipType", "Paid");
                    intent.putExtra("transactionAmount", "60");
                } else if (PrefSetup.getInstance().getUSER_MEMBERSHIPID().equalsIgnoreCase("3")) {
                    intent.putExtra("memberShipType", "Premium");
                    intent.putExtra("transactionAmount", "550");
                }
                startActivity(intent);
                ((ActivityMain) getActivity()).removeExpView();
                break;
        }
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
