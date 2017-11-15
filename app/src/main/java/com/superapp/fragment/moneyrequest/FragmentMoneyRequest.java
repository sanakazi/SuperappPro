//package com.superapp.fragment.moneyrequest;
//
//import android.icu.util.ULocale;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.superapp.ApplicationContext;
//import com.superapp.R;
//import com.superapp.activity.base.ErrorType;
//import com.superapp.activity.ActivityMain;
//import com.superapp.beans.BeanTransaction;
//import com.superapp.fragment.base.BaseFragment;
//import com.superapp.fragment.base.FragmentNames;
//import com.superapp.utils.PrefSetup;
//import com.superapp.webservice.Interactor;
//import com.superapp.webservice.InteractorImpl;
//import com.superapp.webservice.OnResponseListener;
//import com.superapp.webservice.ResponsePacket;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//
//public class FragmentMoneyRequest extends BaseFragment {
//
//    View fragmentView;
//    String description, requestAmount;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        fragmentView = inflater.inflate(R.layout.fragment_moneyrequest, container, false);
//        try {
//            description = getArguments().getString("description");
//            requestAmount = getArguments().getString("requestAmount");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        initView();
//        return fragmentView;
//    }
//
//    private TextView tv_projectName, tv_description, tv_moneyRequestStartDate, tv_moneyRequestEndDate,
//            tv_requestedMoney;
//    private Button bt_reject, bt_accept;
//
//
//    @Override
//    public void initView() {
//        tv_projectName = (TextView) fragmentView.findViewById(R.id.tv_projectName);
//        tv_description = (TextView) fragmentView.findViewById(R.id.tv_description);
//        tv_moneyRequestStartDate = (TextView) fragmentView.findViewById(R.id.tv_moneyRequestStartDate);
//        tv_moneyRequestEndDate = (TextView) fragmentView.findViewById(R.id.tv_moneyRequestEndDate);
//        tv_requestedMoney = (TextView) fragmentView.findViewById(R.id.tv_requestedMoney);
//
//        bt_reject = (Button) fragmentView.findViewById(R.id.bt_reject);
//        bt_accept = (Button) fragmentView.findViewById(R.id.bt_accept);
//        tv_description.setText(description);
//        tv_requestedMoney.setText(requestAmount);
//        tv_projectName.setText(ApplicationContext.getInstance().project.getProjectName());
//
//        fragmentView.findViewById(R.id.bt_accept).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                JSONObject jsonObject = new JSONObject();
//                try {
//                    jsonObject.put("transactionId", transaction.getId());
//                    jsonObject.put("accept", "t");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//
//        fragmentView.findViewById(R.id.bt_reject).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                JSONObject jsonObject = new JSONObject();
//                try {
//                    jsonObject.put("transactionId", transaction.getId());
//                    jsonObject.put("accept", "f");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//        updateTransaction();
//    }
//
//    public void updateTransaction(JSONObject jsonObject) {
//        try {
//            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        new InteractorImpl(activity, new OnResponseListener() {
//            @Override
//            public void onSuccess(int requestCode, ResponsePacket responsePacket) {
//                FragmentMoneyRequest.this.onSuccess(requestCode, responsePacket);
//
//                if (responsePacket.getErrorCode() == 410) {
//                    return;
//                }
//                if (responsePacket.getErrorCode() == 0) {
//                    activity.makeToast(responsePacket.getMessage());
//                }
//            }
//
//            @Override
//            public void onError(int requestCode, ErrorType errorType) {
//                FragmentMoneyRequest.this.onError(requestCode, errorType);
//            }
//        }, Interactor.RequestCode_AddUpdateTransaction, Interactor.Tag_AddUpdateTransaction)
//                .makeJsonPostRequest(Interactor.Method_AddUpdateTransaction, jsonObject, true);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        ((ActivityMain) activity).setHeaderText("Money Request");
//        ((ActivityMain) activity).changeHeaderButton(true);
//        ((ActivityMain) activity).showHideFloatingButton(false, R.drawable.add);
//        ((ActivityMain) activity).showHideNotificationButton(false);
//    }
//}
