package com.superapp.webservice;

import android.content.Context;
import android.widget.Toast;

import com.superapp.activity.base.ErrorType;
import com.superapp.beans.BeanSelectCategory;
import com.superapp.utils.PrefSetup;

import org.json.JSONObject;

import java.util.ArrayList;


public class MaterialService {

    public static void getGetAllMaterial(Context context, long materialCategoryId, OnSuccessListener listener) {
        try {
            JSONObject postData = new JSONObject();
            postData.put("userId", PrefSetup.getInstance().getUserId());
            postData.put("loginType", PrefSetup.getInstance().getUserLoginType());
            postData.put("materialCategoryId", materialCategoryId);
            ArrayList<BeanSelectCategory> list = new ArrayList<>();

            new InteractorImpl(context, new OnResponseListener() {
                @Override
                public void onSuccess(int requestCode, ResponsePacket responsePacket) {
//                    if (responsePacket.getErrorCode() == 0) {
                    ArrayList<BeanSelectCategory> list = new ArrayList<>();
//                        list.add((list.size() == 0 ? 0:list.size()-1),(new BeanSelectCategory(-1, Material)));
                    list.addAll(responsePacket.getMaterial());
                    listener.OnSuccess(list);
//                    } else {
                    if (responsePacket.getErrorCode() != 0) {
                        Toast.makeText(context, responsePacket.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(int requestCode, ErrorType errorType) {
                    listener.OnSuccess(list);
                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                }
            }, Interactor.RequestCode_GetAllMaterial, Interactor.Tag_GetAllMaterial).makeJsonPostRequest(Interactor.Method_GetAllMaterial, postData, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getGetAllSubMaterials(Context context, long materialId, OnSuccessListener listener) {
        try {
            JSONObject postData = new JSONObject();
            postData.put("userId", PrefSetup.getInstance().getUserId());
            postData.put("loginType", PrefSetup.getInstance().getUserLoginType());
            postData.put("materialId", materialId);
            ArrayList<BeanSelectCategory> list = new ArrayList<>();

            new InteractorImpl(context, new OnResponseListener() {
                @Override
                public void onSuccess(int requestCode, ResponsePacket responsePacket) {
//                    if (responsePacket.getErrorCode() == 0) {
                    ArrayList<BeanSelectCategory> list = new ArrayList<>();
//                        list.add(new BeanSelectCategory(-1, SubMaterials));
                    list.addAll(responsePacket.getSubMaterials());
                    listener.OnSuccess(list);
                    if (responsePacket.getErrorCode() != 0) {
                        Toast.makeText(context, responsePacket.getMessage(), Toast.LENGTH_SHORT).show();
                    }
//                    } else {
//                        Toast.makeText(context, responsePacket.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
                }

                @Override
                public void onError(int requestCode, ErrorType errorType) {
                    listener.OnSuccess(list);
                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                }
            }, Interactor.RequestCode_GetAllSubmaterials, Interactor.Tag_GetAllSubmaterials).makeJsonPostRequest(Interactor.Method_GetAllSubmaterials, postData, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getGetAllBrand(Context context, long subMaterialId, OnSuccessListener listener) {
        try {
            JSONObject postData = new JSONObject();
            postData.put("userId", PrefSetup.getInstance().getUserId());
            postData.put("loginType", PrefSetup.getInstance().getUserLoginType());
            postData.put("subMaterialId", subMaterialId);
            ArrayList<BeanSelectCategory> list = new ArrayList<>();

            new InteractorImpl(context, new OnResponseListener() {
                @Override
                public void onSuccess(int requestCode, ResponsePacket responsePacket) {
//                    if (responsePacket.getErrorCode() == 0) {
                    ArrayList<BeanSelectCategory> list = new ArrayList<>();
//                        list.add(new BeanSelectCategory(-1, Brand));
                    list.addAll(responsePacket.getBrand());
                    listener.OnSuccess(list);
//                    } else {
//                        Toast.makeText(context, responsePacket.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
                    if (responsePacket.getErrorCode() != 0) {
                        Toast.makeText(context, responsePacket.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(int requestCode, ErrorType errorType) {
                    listener.OnSuccess(list);
                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                }
            }, Interactor.RequestCode_GetAllBrand, Interactor.Tag_GetAllBrand).makeJsonPostRequest(Interactor.Method_GetAllBrand, postData, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getGetAllColors(Context context, long brandId, OnSuccessListener listener) {
        try {
            JSONObject postData = new JSONObject();
            postData.put("userId", PrefSetup.getInstance().getUserId());
            postData.put("loginType", PrefSetup.getInstance().getUserLoginType());
            postData.put("brandId", brandId);
            ArrayList<BeanSelectCategory> list = new ArrayList<>();

            new InteractorImpl(context, new OnResponseListener() {
                @Override
                public void onSuccess(int requestCode, ResponsePacket responsePacket) {
//                    if (responsePacket.getErrorCode() == 0) {
                    ArrayList<BeanSelectCategory> list = new ArrayList<>();
//                        list.add(new BeanSelectCategory(-1, Colors));
                    list.addAll(responsePacket.getColor());
                    listener.OnSuccess(list);
//                    } else {
//                        Toast.makeText(context, responsePacket.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
                    if (responsePacket.getErrorCode() != 0) {
                        Toast.makeText(context, responsePacket.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(int requestCode, ErrorType errorType) {
                    listener.OnSuccess(list);
                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                }
            }, Interactor.RequestCode_GetAllColors, Interactor.Tag_GetAllColors).makeJsonPostRequest(Interactor.Method_GetAllColors, postData, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getGetAllMeasurements(Context context, OnSuccessListener listener) {
        try {
            JSONObject postData = new JSONObject();
            postData.put("userId", PrefSetup.getInstance().getUserId());
            postData.put("loginType", PrefSetup.getInstance().getUserLoginType());
            ArrayList<BeanSelectCategory> list = new ArrayList<>();

            new InteractorImpl(context, new OnResponseListener() {
                @Override
                public void onSuccess(int requestCode, ResponsePacket responsePacket) {
//                    if (responsePacket.getErrorCode() == 0) {
                        ArrayList<BeanSelectCategory> list = new ArrayList<>();
//                        list.add(new BeanSelectCategory(-1, Measurements));
                        list.addAll(responsePacket.getMeasurements());
                        listener.OnSuccess(list);
//                    } else {
//                        Toast.makeText(context, responsePacket.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
                    if (responsePacket.getErrorCode() != 0) {
                        Toast.makeText(context, responsePacket.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(int requestCode, ErrorType errorType) {
                    listener.OnSuccess(list);
                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                }
            }, Interactor.RequestCode_GetAllMeasurements, Interactor.Tag_GetAllMeasurements).makeJsonPostRequest(Interactor.Method_GetAllMeasurements, postData, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getGetAllSize(Context context, long measurementId, OnSuccessListener listener) {
        try {
            JSONObject postData = new JSONObject();
            postData.put("loginType", PrefSetup.getInstance().getUserLoginType());
            postData.put("userId", PrefSetup.getInstance().getUserId());
            postData.put("measurementId", measurementId);
//            postData.put("colorId", colorId);
//            postData.put("brandId", brandId);
            ArrayList<BeanSelectCategory> list = new ArrayList<>();

            new InteractorImpl(context, new OnResponseListener() {
                @Override
                public void onSuccess(int requestCode, ResponsePacket responsePacket) {
//                    if (responsePacket.getErrorCode() == 0) {
                    ArrayList<BeanSelectCategory> list = new ArrayList<>();
//                        list.add(new BeanSelectCategory(-1, Size));
                    list.addAll(responsePacket.getSize());
                    listener.OnSuccess(list);
//                    } else {
//                        Toast.makeText(context, responsePacket.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
                    if (responsePacket.getErrorCode() != 0) {
                        Toast.makeText(context, responsePacket.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(int requestCode, ErrorType errorType) {
                    listener.OnSuccess(list);
                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                }
            }, Interactor.RequestCode_GetAllSize, Interactor.Tag_GetAllSize).makeJsonPostRequest(Interactor.Method_GetAllSize, postData, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public interface OnSuccessListener {
        void OnSuccess(ArrayList<BeanSelectCategory> list);
    }

}
