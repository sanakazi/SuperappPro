package com.superapp.webservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.base.ErrorType;
import com.superapp.utils.PrefSetup;
import com.superapp.utils.Utilities;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by GladiatoR on 25/11/15.
 */
public class InteractorImpl implements Interactor {

    private String TAG = "------" + this.getClass().getName() + "------";
    private String headerKey;
    Context context;
    OnResponseListener callBack;
    private int requestCode;
    private String requestTag;
    private String tag_json_obj = "jobj_req";
    private int socketTimeout = 10000;

    private ProgressDialog pdialog;

    public InteractorImpl(Context context, OnResponseListener callBack, int requestCode, String requestTag) {
        this.context = context;
        this.callBack = callBack;
        this.requestCode = requestCode;
        headerKey = PrefSetup.getInstance().getHeaderKey();
        this.requestTag = requestTag;
    }

    private void showDialog(String msg) {
        try {
            if (pdialog != null && pdialog.isShowing()) {
                return;
            }
            pdialog = new ProgressDialog(context);
            if (msg == null)
                pdialog.setMessage("Loading....");
            else
                pdialog.setMessage(msg);
            pdialog.setIndeterminate(true);
            pdialog.setCancelable(false);
            pdialog.show();
        } catch (Exception e) {
//            Crashlytics.logException(e);
        }
    }

    private void dismissDialog() {
        try {
            if (pdialog != null && pdialog.isShowing())
                pdialog.dismiss();
        } catch (Exception e) {
//            Crashlytics.logException(e);
        }
    }

    private void showNoInternetConnection() {
//        Utilities.getInstance().showDialog(context, context.getString(R.string.alert), context.getString(R.string.noInternetAccess)).show();
        Toast.makeText(context, context.getString(R.string.noInternetAccess), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void makeJsonPostRequest(String url, JSONObject jsonRequest, boolean showDialog) {
        System.out.println("--------------------------------------------------------");
        System.out.println("------" +"url=  "+ url + " --------");
        System.out.println("------" + "jsonRequest= " + jsonRequest + " --------");
        System.out.println("---------------" + "headerKey = "+ headerKey + " -----------------");
        System.out.println("--------------------------------------------------------");

        if (Utilities.getInstance().isOnline(context)) {
            if (showDialog) {
                showDialog(null);
            }
            makePostJsonRequest(url, jsonRequest);
        } else {
            showNoInternetConnection();
            callBack.onError(requestCode, ErrorType.NO_INTERNET);
        }
    }

    @Override
    public void makeStringGetRequest(String url, boolean showDialog) {
        System.out.println("--------------------------------------------------------");
        System.out.println("------" + "url= " +  url + " --------");
        System.out.println("--------------------------------------------------------");
        if (Utilities.getInstance().isOnline(context)) {
            if (showDialog) {
                showDialog(null);
            }
            makeGetStringRequest(url);
        } else {
            showNoInternetConnection();
            callBack.onError(requestCode, ErrorType.NO_INTERNET);
        }
    }

    private void makeGetStringRequest(String url) {
        url = url + "?v=" + System.currentTimeMillis();
        if (Utilities.getInstance().isOnline(context)) {
            StringRequest sr = new StringRequest(Request.Method.GET, url, successStringListener, errorListener);
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            sr.setRetryPolicy(policy);
            ApplicationContext.getInstance().addToRequestQueue(sr, tag_json_obj);
        } else {
            showNoInternetConnection();
            callBack.onError(requestCode, ErrorType.NO_INTERNET);
        }
    }

    private void makePostJsonRequest(String url, final JSONObject jsonRequest) {

        url = url + "?v=" + System.currentTimeMillis();

        if (Utilities.getInstance().isOnline(context)) {

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, successJsonListener, errorListener) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> mHeaders = new HashMap<>();
                    try {
                        for (String key : super.getHeaders().keySet()) {
                            mHeaders.put(key, super.getHeaders().get(key));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mHeaders.put("splalgoval", headerKey);
                    return mHeaders;
                    // return super.getHeaders();
                }
            };

            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjReq.setRetryPolicy(policy);
            jsonObjReq.setTag(requestTag);
            ApplicationContext.getInstance().addToRequestQueue(jsonObjReq, requestTag);
        } else {
            showNoInternetConnection();
            callBack.onError(requestCode, ErrorType.NO_INTERNET);
        }
    }

    public void makeFileUploadingRequest(String url, final ArrayList<String> imageName,
                                         final ArrayList<File> imageFile, final Map<String, String> params, boolean showDialog) {
        url = url + "?v=" + System.currentTimeMillis();
        if (Utilities.getInstance().isOnline(context)) {
            if (showDialog) {
                showDialog(null);
            }
            Map<String, String> header = new HashMap<>();

            System.out.println("------" + url + "--------");

            MultiPartReq stringRequest = new MultiPartReq(url, errorListener, successStringListener, imageFile, imageName, params, header) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> mHeaders = new HashMap<>();
//                    super.getHeaders()

                    System.out.println("--------------------------------------------------------");
                    System.out.println("------" + "params = " + params + " --------");
                    System.out.println("------" + "imageName + imageFile "+imageName + imageFile + " --------");
                    System.out.println("--------------------------------------------------------");
                    System.out.println("-------------" +"headerKey = "+ headerKey + "-----------------");

                    try {
                        for (String key : super.getHeaders().keySet()) {
                            mHeaders.put(key, super.getHeaders().get(key));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mHeaders.put("splalgoval", headerKey);
                    return mHeaders;
                }
            };
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            ApplicationContext.getInstance().addToRequestQueue(stringRequest);
        } else {
            showNoInternetConnection();
            callBack.onError(requestCode, ErrorType.NO_INTERNET);
        }
    }

    Response.Listener successStringListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            try {
                System.out.println("-------------------------successStringListener-------------------------------");
                System.out.println("---------" + s);
                System.out.println("----------------------------successStringListener----------------------------");
                dismissDialog();
                ResponsePacket response = new Gson().fromJson(s.toString(), ResponsePacket.class);
                response.setResponsePacket(s);
                callBack.onSuccess(requestCode, response);
            } catch (Exception e) {
                callBack.onSuccess(requestCode, new ResponsePacket("success", 0));
                e.printStackTrace();
            }
        }
    };

    Response.Listener successJsonListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject s) {
            try {
                System.out.println("--------------------------successJsonListener------------------------------");
                System.out.println("---------" + s);
                System.out.println("--------------------------successJsonListener------------------------------");
                dismissDialog();
                ResponsePacket response = new Gson().fromJson(s.toString(), ResponsePacket.class);
                response.setResponsePacket(s.toString());
                callBack.onSuccess(requestCode, response);
            } catch (Exception e) {
                e.printStackTrace();
                callBack.onSuccess(requestCode, new ResponsePacket("success", 0));
            }
        }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(final VolleyError volleyError) {
            System.out.println("------------------------errorListener--------------------------------");
            String err = null;
            dismissDialog();
            try {
                if (volleyError != null) {
                    if (volleyError.getCause() != null && volleyError.getCause().getMessage().equalsIgnoreCase("End of input at character 0 of ")) {
                        callBack.onSuccess(requestCode, new ResponsePacket("success", 0));
                        return;
                    } else {
                        try {
                            if (volleyError.networkResponse.statusCode == 500) {
                                callBack.onError(requestCode, ErrorType.ERROR500);
                                return;
                            } else {
                                err = new String(volleyError.networkResponse.data);
                            }
                        } catch (Exception e) {
//                          Crashlytics.logException(e);
                        }
                        if (err != null) {
                            Toast.makeText(context, err, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            callBack.onError(requestCode, ErrorType.ERROR);
            System.out.println("------------------------errorListener--------------------------------");
        }
    };

}
