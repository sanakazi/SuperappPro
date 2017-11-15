package com.superapp.paytm;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.superapp.R;
import com.superapp.activity.ActivityLogin;
import com.superapp.activity.ActivityMain;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.ErrorType;
import com.superapp.utils.PrefSetup;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.OnResponseListener;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the sample app which will make use of the PG SDK. This activity will
 * show the usage of Paytm PG SDK API's.
 **/

public class MerchantActivity extends BaseAppCompatActivity {

    TextView tv_orderId, tv_transactionAmount, tv_membershipType;
    String CHECKSUMHASH;
    String status, responseCode, transactionId, bankTransactionId;
    Long tsLong;
    String ts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity);
        initView();
        generateCheckSum();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    //This is to refresh the order id: Only for the Sample App's purpose.
    @Override
    protected void onStart() {
        super.onStart();
        initView();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    private void initView() {
        tsLong = System.currentTimeMillis() / 1000;
        ts = tsLong.toString() + "-" + PrefSetup.getInstance().getUserId();

        tv_orderId = (TextView) findViewById(R.id.tv_orderId);
        tv_orderId.setText(ts);

        TextView tv_name = (TextView) findViewById(R.id.tv_name);
        tv_name.setText(PrefSetup.getInstance().getUserName());

        TextView tv_companyName = (TextView) findViewById(R.id.tv_companyName);
        tv_companyName.setText(PrefSetup.getInstance().getUserCompanyName());

        TextView tv_email = (TextView) findViewById(R.id.tv_email);
        tv_email.setText(PrefSetup.getInstance().getUserEmail());

        TextView tv_mobile = (TextView) findViewById(R.id.tv_mobile);
        tv_mobile.setText(PrefSetup.getInstance().getUserContactNo());

        tv_membershipType = (TextView) findViewById(R.id.tv_membershipType);
        tv_membershipType.setText(getIntent().getStringExtra("memberShipType"));

        tv_transactionAmount = (TextView) findViewById(R.id.tv_transactionAmount);
        tv_transactionAmount.setText("\u20B9 " + getIntent().getStringExtra("transactionAmount"));

        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(v -> onBackPressed());
    }


    private void generateCheckSum() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderId", tv_orderId.getText().toString());
            jsonObject.put("customerId", PrefSetup.getInstance().getUserId());
            jsonObject.put("amount", getIntent().getStringExtra("transactionAmount"));
            new InteractorImpl(MerchantActivity.this, new OnResponseListener() {
                @Override
                public void onSuccess(int requestCode, ResponsePacket responsePacket) {
                    if (responsePacket.getErrorCode() == 0) {
                        CHECKSUMHASH = responsePacket.getCHECKSUMHASH();
                    }
                }

                @Override
                public void onError(int requestCode, ErrorType errorType) {

                }
            }, Interactor.RequestCode_GenerateChecksum, Interactor.Tag_GenerateChecksum)
                    .makeJsonPostRequest(Interactor.Method_GenerateChecksum, jsonObject, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateMembership() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("membershipId", getIntent().getIntExtra("downgradeType", 0));

            jsonObject.put("orderId", tv_orderId.getText().toString());
            jsonObject.put("projectList", getIntent().getStringExtra("projectsList"));
            jsonObject.put("orderAmount", getIntent().getStringExtra("transactionAmount"));

            if (Integer.parseInt(PrefSetup.getInstance().getUSER_MEMBERSHIPID()) == getIntent().getIntExtra("downgradeType", 0)) {
                jsonObject.put("updateType", "renew");
            } else if (Integer.parseInt(PrefSetup.getInstance().getUSER_MEMBERSHIPID()) < getIntent().getIntExtra("downgradeType", 0)) {
                jsonObject.put("updateType", "upgrade");
            } else if (Integer.parseInt(PrefSetup.getInstance().getUSER_MEMBERSHIPID()) > getIntent().getIntExtra("downgradeType", 0)) {
                jsonObject.put("updateType", "downgrade");
            }

            jsonObject.put("transactionId", transactionId);
            jsonObject.put("bankTrnxId", bankTransactionId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(this, new OnResponseListener() {
            @Override
            public void onSuccess(int requestCode, ResponsePacket responsePacket) {

                if (responsePacket.getErrorCode() == 0) {
                    Toast.makeText(MerchantActivity.this, responsePacket.getMessage(), Toast.LENGTH_SHORT).show();
                    try {
                        if (Integer.parseInt(PrefSetup.getInstance().getUSER_MEMBERSHIPID()) == getIntent().getIntExtra("downgradeType", 0)) {
                            Intent intent = new Intent(MerchantActivity.this, ActivityMain.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            finish();
                            startActivity(intent);
                        } else if (Integer.parseInt(PrefSetup.getInstance().getUSER_MEMBERSHIPID()) < getIntent().getIntExtra("downgradeType", 0)) {
                            Intent intent = new Intent(MerchantActivity.this, ActivityLogin.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            finish();
                            startActivity(intent);
                        } else if (Integer.parseInt(PrefSetup.getInstance().getUSER_MEMBERSHIPID()) > getIntent().getIntExtra("downgradeType", 0)) {
                            if (tsLong < PrefSetup.getInstance().getUSER_MEMBER_SUBSCRIPTION_EXP_DATE()) {
                                Intent intent = new Intent(MerchantActivity.this, ActivityMain.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                finish();
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(MerchantActivity.this, ActivityLogin.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                finish();
                                startActivity(intent);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(int requestCode, ErrorType errorType) {

            }
        }, Interactor.RequestCode_updateMembership, Interactor.Tag_updateMembership)
                .makeJsonPostRequest(Interactor.Method_updateMembership, jsonObject, true);
    }


    public void onStartTransaction(View view) {
        PaytmPGService Service = PaytmPGService.getProductionService();
        Map<String, String> paramMap = new HashMap<String, String>();

        paramMap.put("MID", "Supera99139441069331");
        paramMap.put("ORDER_ID", ((TextView) findViewById(R.id.tv_orderId)).getText().toString());
        paramMap.put("CUST_ID", String.valueOf(PrefSetup.getInstance().getUserId()));
        paramMap.put("INDUSTRY_TYPE_ID", "Retail109");
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("TXN_AMOUNT", getIntent().getStringExtra("transactionAmount"));
        paramMap.put("WEBSITE", "SuperaWAP");
        paramMap.put("CALLBACK_URL", "https://www.superapp.co.in/supperappapi/verifyChecksum.php");
//        paramMap.put("EMAIL", PrefSetup.getInstance().getUserEmail());
//        paramMap.put("MOBILE_NO", PrefSetup.getInstance().getUserContactNo());
        if (!TextUtils.isEmpty(CHECKSUMHASH))
            paramMap.put("CHECKSUMHASH", CHECKSUMHASH);
        PaytmOrder Order = new PaytmOrder(paramMap);

		/*PaytmMerchant Merchant = new PaytmMerchant(
                "https://pguat.paytm.com/paytmchecksum/paytmCheckSumGenerator.jsp",
				"https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");*/

        Service.initialize(Order, null);

        Service.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {

                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        // Some UI Error Occurred in Payment Gateway Activity.
                        // // This may be due to initialization of views in
                        // Payment Gateway Activity or may be due to //
                        // initialization of webview. // Error Message details
                        // the error occurred.

                    }

                    @Override
                    public void onTransactionResponse(Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction : " + inResponse);
//                        Toast.makeText(MerchantActivity.this, "Transaction Detail :" + inResponse, Toast.LENGTH_SHORT).show();

                        try {
                            status = (String) inResponse.get("STATUS");
                            responseCode = (String) inResponse.get("RESPCODE");
                            transactionId = (String) inResponse.get("TXNID");
                            bankTransactionId = (String) inResponse.get("BANKTXNID");

                            if (status.equals("TXN_SUCCESS")) {
                                updateMembership();
                            } else {
                                Intent intent = new Intent(MerchantActivity.this, ActivityMain.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                finish();
                                startActivity(intent);
                                Toast.makeText(MerchantActivity.this, "Transaction failed!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void networkNotAvailable() {
                        // If network is not
                        // available, then this
                        // method gets called.

                        Toast.makeText(MerchantActivity.this, "No internet access, please check your internet connection.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        // This method gets called if client authentication
                        // failed. // Failure may be due to following reasons //
                        // 1. Server error or downtime. // 2. Server unable to
                        // generate checksum or checksum response is not in
                        // proper format. // 3. Server failed to authenticate
                        // that client. That is value of payt_STATUS is 2. //
                        // Error Message describes the reason for failure.
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {

                        Toast.makeText(MerchantActivity.this, "Error loading a web page.", Toast.LENGTH_SHORT).show();
                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        // TODO Auto-generated method stub


                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                        Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                    }

                }
        );
    }


    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
    }

}
