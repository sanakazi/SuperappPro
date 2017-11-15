package com.superapp.webservice;

import android.content.Context;
import android.widget.Toast;

import com.superapp.activity.base.ErrorType;
import com.superapp.utils.PrefSetup;

import org.json.JSONObject;

public class SurveyService {

    public static void getSurvey(final Context context, final OnSuccessListener listener) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("membershipId", PrefSetup.getInstance().getUSER_MEMBERSHIPID());
            sendApiRequest(context, Interactor.Method_getSurvey, jsonObject, listener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getSurveyQuestions(final Context context, long surveyId, String dynamicAns, final OnSuccessListener listener) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("surveyId", surveyId);
            jsonObject.put("dynamicAns", dynamicAns);
            sendApiRequest(context, Interactor.Method_getSurveyQuestion, jsonObject, listener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void surveySubmission(final Context context, JSONObject postData, final OnSuccessListener listener) {
        try {
            postData.put("userId", PrefSetup.getInstance().getUserId());
            postData.put("membershipId", PrefSetup.getInstance().getUSER_MEMBERSHIPID());
            postData.put("loginType", PrefSetup.getInstance().getUserLoginType());
            sendApiRequest(context, Interactor.Method_submitSurveyList, postData, listener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendApiRequest(Context context, String method, JSONObject postData, OnSuccessListener listener) {
        int mCode = 0;
        String mTag = "";
        if (method.equals(Interactor.Method_getSurvey)) {
            mCode = Interactor.RequestCode_getSurvey;
            mTag = Interactor.Tag_getSurvey;

        } else if (method.equals(Interactor.Method_getSurveyQuestion)) {
            mCode = Interactor.RequestCode_getSurveyQuestion;
            mTag = Interactor.Tag_getSurveyQuestion;

        } else if (method.equals(Interactor.Method_submitSurveyList)) {
            mCode = Interactor.RequestCode_submitSurveyList;
            mTag = Interactor.Tag_submitSurveyList;
        }
        new InteractorImpl(context, new OnResponseListener() {
            @Override
            public void onSuccess(int requestCode, ResponsePacket responsePacket) {
                if (responsePacket.getErrorCode() == 0) {
                    if (method.equals(Interactor.Method_getSurvey)) {
                        if (responsePacket.getSurveyDetail() != null)
                            listener.OnSuccess(responsePacket.getSurveyDetail());
                        else
                            listener.OnSuccess(responsePacket);

                    } else if (method.equals(Interactor.Method_getSurveyQuestion)) {
                        listener.OnSuccess(responsePacket);

                    } else if (method.equals(Interactor.Method_submitSurveyList)) {
                        if (responsePacket.isNextSurvey().equals("t"))
                            listener.OnSuccess(responsePacket.getSurveyDetail());
                        else
                            listener.OnSuccess(responsePacket);
                    }
                } else {
                    listener.OnSuccess(null);
                }
                if (responsePacket.getErrorCode() != 0) {
                    Toast.makeText(context, responsePacket.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(int requestCode, ErrorType errorType) {
                listener.OnSuccess(null);
            }
        }, mCode, mTag).makeJsonPostRequest(method, postData, true);
    }

    public interface OnSuccessListener {
        void OnSuccess(Object obj);
    }
}
