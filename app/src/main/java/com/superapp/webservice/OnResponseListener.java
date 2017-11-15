package com.superapp.webservice;


import com.superapp.activity.base.ErrorType;

/**
 * Created by GladiatoR on 25/11/15.
 */
public interface OnResponseListener {
    void onSuccess(int requestCode, ResponsePacket responsePacket);
    void onError(int requestCode, ErrorType errorType);

}
