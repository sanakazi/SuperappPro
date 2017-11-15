package com.superapp.webservice;

import android.content.Context;

import com.superapp.activity.base.ErrorType;
import com.superapp.beans.Region;

import org.json.JSONObject;

import java.util.ArrayList;

public class RegionService {
    public static String REGION_COUNTRY = "Select Country";
    public static String REGION_STATE = "Select State";
    public static String REGION_CITY = "Select City";

    public static void getRegion(Context context, long regionId, final String regionType, OnSuccessListener listener) {
        try {
            int mCode = 0;
            String mName = "";
            String mTag = "";
            JSONObject postData = new JSONObject();
            ArrayList<Region> list = new ArrayList<>();
            if (regionType.equals(REGION_COUNTRY)) {
                mCode = Interactor.RequestCode_GetCountry;
                mName = Interactor.Method_GetCountry;
                mTag = Interactor.Tag_GetCountry;

            } else if (regionType.equals(REGION_STATE)) {
                postData.put("countryId", regionId);
                mCode = Interactor.RequestCode_GetState;
                mName = Interactor.Method_GetState;
                mTag = Interactor.Tag_GetState;

            } else if (regionType.equals(REGION_CITY)) {
                postData.put("stateId", regionId);
                mCode = Interactor.RequestCode_GetCity;
                mName = Interactor.Method_GetCity;
                mTag = Interactor.Tag_GetCity;
            }

            new InteractorImpl(context, new OnResponseListener() {
                @Override
                public void onSuccess(int requestCode, ResponsePacket responsePacket) {
                    if (responsePacket.getErrorCode() == 0) {
                        ArrayList<Region> list = new ArrayList<>();
                        list.add(new Region(-1, regionType));

                        if (regionType.equals(REGION_COUNTRY)) {
                            list.addAll(responsePacket.getCountryArray());

                        } else if (regionType.equals(REGION_STATE)) {
                            list.addAll(responsePacket.getStateList());

                        } else if (regionType.equals(REGION_CITY)) {
                            list.addAll(responsePacket.getCityList());
                        }
                        listener.OnSuccess(list);
                    }
                }

                @Override
                public void onError(int requestCode, ErrorType errorType) {
                    listener.OnSuccess(list);
                }
            }, mCode, mTag).makeJsonPostRequest(mName, postData, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public interface OnSuccessListener {
        void OnSuccess(ArrayList<Region> list);
    }
}
