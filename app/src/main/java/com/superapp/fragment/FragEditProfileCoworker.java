package com.superapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.baselib.image_cropping.ActivityImageCropping;
import com.baselib.image_cropping.CropImageView;
import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.ActivityMain;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.ErrorType;
import com.superapp.beans.BeanAddress;
import com.superapp.beans.BeanSelectCategory;
import com.superapp.beans.CoworkerBean;
import com.superapp.fragment.base.BaseFragment;
import com.superapp.fragment.dashboard.add_forms.coworker_occupation.BeanOccupation;
import com.superapp.utils.PopupUtils;
import com.superapp.utils.PrefSetup;
import com.superapp.utils.ProjectUtils;
import com.superapp.utils.SaveCameraImage;
import com.superapp.utils.Utilities;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragEditProfileCoworker extends BaseFragment {
    public View fragmentView;

    File coWorkerImage, coWorkerID;
    BeanSelectCategory beanSelectCategory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentView = inflater.inflate(R.layout.fragment_add_coworker, container, false);
        initView();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (activity instanceof ActivityMain) {
            ((ActivityMain) activity).setHeaderText(activity.getString(R.string.updateCoWorker));
            ((ActivityMain) activity).showHideFloatingButton(false, R.drawable.add);
            ((ActivityMain) activity).changeHeaderButton(true);
            ((ActivityMain) activity).showAd(false);
        }
    }

    CoworkerBean coworker;
    BeanOccupation occupation;
    BeanAddress address, nativeAddress;

    private EditText et_coWorkerName, et_coWorkerOccupation, et_coWorkerMobile, et_coWorkerEmail,
            et_coWorkerAddress, et_coWorkerNativeAddress, et_coWorkerPanNumber, et_coWorkerReferredBy;

    private TextInputLayout textInputLayoutCoWorkerName, textInputLayoutCoWorkerOccupation, textInputLayoutCoWorkerMobile,
            textInputLayoutCoWorkerEmail, textInputLayoutCoWorkerAddress, textInputLayoutCoWorkerNativeAddress,
            textInputLayoutCoWorkerPanNumber, textInputLayoutCoWorkerReferredBy;
    private ImageView iv_addCoWorkerPhoto, iv_addCoWorkerPanNumberPhotoId;
    private CheckBox checkbox_sameAsAddress;
    private static final int CAMERA_PIC_REQUEST = 2500;
    private ListView lv_occupationList;
    TextView bt_addCoWorkerProject;
    RatingBar ratingBar;
    private LinearLayout ll_coworkerRating;

    public void initView() {
        setClick(R.id.ll_addCoworker, fragmentView);
        et_coWorkerReferredBy = (EditText) fragmentView.findViewById(R.id.et_coWorkerReferredBy);
        et_coWorkerPanNumber = (EditText) fragmentView.findViewById(R.id.et_coWorkerPanNumber);
        et_coWorkerNativeAddress = (EditText) setTouchNClick(R.id.et_coWorkerNativeAddress, fragmentView);
        et_coWorkerAddress = (EditText) setTouchNClick(R.id.et_coWorkerAddress, fragmentView);
        et_coWorkerEmail = (EditText) fragmentView.findViewById(R.id.et_coWorkerEmail);
        et_coWorkerMobile = (EditText) fragmentView.findViewById(R.id.et_coWorkerMobile);
        et_coWorkerOccupation = (EditText) setTouchNClick(R.id.et_coWorkerOccupation, fragmentView);
        et_coWorkerName = (EditText) fragmentView.findViewById(R.id.et_coWorkerName);
        iv_addCoWorkerPhoto = (ImageView) setTouchNClick(R.id.iv_addCoWorkerPhoto, fragmentView);
        iv_addCoWorkerPanNumberPhotoId = (ImageView) setTouchNClick(R.id.iv_addCoWorkerPanNumberPhotoId, fragmentView);
        bt_addCoWorkerProject = (TextView) fragmentView.findViewById(R.id.bt_addCoWorkerProject);

        ll_coworkerRating = (LinearLayout) fragmentView.findViewById(R.id.ll_coworkerRating);
        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("w")) {
            ll_coworkerRating.setVisibility(View.GONE);
        }

        textInputLayoutCoWorkerName = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutCoWorkerName);
        textInputLayoutCoWorkerOccupation = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutCoWorkerOccupation);
        textInputLayoutCoWorkerMobile = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutCoWorkerMobile);
        textInputLayoutCoWorkerEmail = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutCoWorkerEmail);
        textInputLayoutCoWorkerAddress = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutCoWorkerAddress);
        textInputLayoutCoWorkerNativeAddress = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutCoWorkerNativeAddress);
        textInputLayoutCoWorkerPanNumber = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutCoWorkerPanNumber);
        textInputLayoutCoWorkerReferredBy = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutCoWorkerReferredBy);
        ratingBar = (RatingBar) fragmentView.findViewById(R.id.ratingBar);

        checkbox_sameAsAddress = (CheckBox) fragmentView.findViewById(R.id.checkbox_sameAsAddress);
        checkbox_sameAsAddress.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (address != null) {
                    nativeAddress = address;
                    et_coWorkerNativeAddress.setText(nativeAddress.toString());
                }
            } else {
                nativeAddress = null;
                et_coWorkerNativeAddress.setText("");
            }
        });

        getCoworkerDetail();
    }

    private boolean validateFields() {

        if (TextUtils.isEmpty(et_coWorkerName.getText().toString())) {
            textInputLayoutCoWorkerName.setErrorEnabled(true);
            textInputLayoutCoWorkerName.setError(getString(R.string.nameRequired));
            et_coWorkerName.requestFocus();
            return false;
        } else {
            textInputLayoutCoWorkerName.setErrorEnabled(false);
            textInputLayoutCoWorkerName.setError(null);
        }
        if (TextUtils.isEmpty(et_coWorkerOccupation.getText().toString())) {
            textInputLayoutCoWorkerOccupation.setErrorEnabled(true);
            textInputLayoutCoWorkerOccupation.setError(getString(R.string.selectOccupation));
            et_coWorkerOccupation.requestFocus();
            return false;
        } else {
            textInputLayoutCoWorkerOccupation.setErrorEnabled(false);
            textInputLayoutCoWorkerOccupation.setError(null);
        }
        if (TextUtils.isEmpty(et_coWorkerMobile.getText().toString())) {
            textInputLayoutCoWorkerMobile.setErrorEnabled(true);
            textInputLayoutCoWorkerMobile.setError(getString(R.string.mobileNumberRequired));
            et_coWorkerMobile.requestFocus();
            return false;
        } else {
            textInputLayoutCoWorkerMobile.setErrorEnabled(false);
            textInputLayoutCoWorkerMobile.setError(null);
        }

        if (!TextUtils.isEmpty(et_coWorkerEmail.getText().toString()) && !Utilities.getInstance().isValidEmail(et_coWorkerEmail.getText().toString())) {
            textInputLayoutCoWorkerEmail.setErrorEnabled(true);
            textInputLayoutCoWorkerEmail.setError(getString(R.string.invalidEmail));
            et_coWorkerEmail.requestFocus();
            return false;
        } else {
            textInputLayoutCoWorkerEmail.setErrorEnabled(false);
            textInputLayoutCoWorkerEmail.setError(null);
        }

        if (TextUtils.isEmpty(et_coWorkerAddress.getText().toString())) {
            textInputLayoutCoWorkerAddress.setErrorEnabled(true);
            textInputLayoutCoWorkerAddress.setError(getString(R.string.addressRequired));
            et_coWorkerAddress.requestFocus();
            return false;
        } else {
            textInputLayoutCoWorkerAddress.setErrorEnabled(false);
            textInputLayoutCoWorkerAddress.setError(null);
        }
        return true;
    }


    private int ImageSelection = 0;

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == activity.RESULT_OK) {
//            switch (requestCode) {
//                case BaseAppCompatActivity.CAPTURE_PHOTO:
//                case BaseAppCompatActivity.SELECT_PHOTO:
//                    saveImageInCache(data);
//                    break;
//
//                case BaseAppCompatActivity.CROP_IMAGE:
//                    Bundle extra = data.getExtras();
//                    if (ImageSelection == 1) {
//                        coWorkerID(extra.getString(ActivityImageCropping.RETURN_IMAGE_PATH));
//                    } else if (ImageSelection == 2) {
//                        addCameraImage(extra.getString(ActivityImageCropping.RETURN_IMAGE_PATH));
//                    }
//                    break;
//            }
//        }
//    }

    private void cropImage(Uri uri) {
        Intent intent = new Intent();
        intent.setClass(getContext(), ActivityImageCropping.class);
        intent.putExtra("selectedImageUri", uri);
        intent.putExtra("ratio", CropImageView.CropMode.RATIO_FREE.getId());
        startActivityForResult(intent, BaseAppCompatActivity.CROP_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == BaseAppCompatActivity.CROP_IMAGE) {
                    if (ImageSelection == 1) {
                        coWorkerID = new File(data.getExtras().getString(ActivityImageCropping.RETURN_IMAGE_PATH));
                        Bitmap bitmap = BitmapFactory.decodeFile(coWorkerID.getAbsolutePath());
                        iv_addCoWorkerPanNumberPhotoId.setImageBitmap(bitmap);
                    } else {
                        coWorkerImage = new File(data.getExtras().getString(ActivityImageCropping.RETURN_IMAGE_PATH));
                        Bitmap bitmap = BitmapFactory.decodeFile(coWorkerImage.getAbsolutePath());
                        iv_addCoWorkerPhoto.setImageBitmap(bitmap);
                    }
                } else {
                    if (requestCode == BaseAppCompatActivity.CAPTURE_PHOTO) {
                        Uri uri = Uri.parse(PrefSetup.getInstance().getTempImage());
                        getActivity().getContentResolver().notifyChange(uri, null);
                        cropImage(Uri.parse(PrefSetup.getInstance().getTempImage()));
                    } else if (requestCode == BaseAppCompatActivity.SELECT_PHOTO) {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                        activity.saveImage(bitmap, activity.getTempImage());
                        cropImage(Uri.parse(PrefSetup.getInstance().getTempImage()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        Utilities.getInstance().hideKeyboard(activity);
        switch (view.getId()) {
            case R.id.ll_addCoworker:
                if (validateFields()) {
                    addCoWorker();
                }
                break;

            case R.id.et_coWorkerOccupation:
                PopupUtils.getInstance().showOccupationPopUp(activity, 1, (object) -> {
                    beanSelectCategory = ((BeanSelectCategory) object);
                    et_coWorkerOccupation.setText(beanSelectCategory.getTitle());
                }, "Select Occupation", false);
                break;

            case R.id.et_coWorkerAddress:
                PopupUtils.getInstance().showClientAddressPopup(activity, object -> {
                    address = (BeanAddress) object;
                    et_coWorkerAddress.setText(address.toString());
                    if (checkbox_sameAsAddress.isChecked()) {
                        nativeAddress = address;
                        et_coWorkerNativeAddress.setText(nativeAddress.toString());
                    }
                }, address, "Address");
                break;

            case R.id.et_coWorkerNativeAddress:
                PopupUtils.getInstance().showClientAddressPopup(activity, object -> {
                    nativeAddress = (BeanAddress) object;
                    et_coWorkerNativeAddress.setText(nativeAddress.toString());
                }, nativeAddress, "Native Address");
                break;

            case R.id.iv_addCoWorkerPanNumberPhotoId:
                ImageSelection = 1;
                activity.showImageGettingDialog();
                break;

            case R.id.iv_addCoWorkerPhoto:
                ImageSelection = 2;
                activity.showImageGettingDialog();
                break;
        }
    }

    private void getCoworkerDetail() {
        long coworkerId = getArguments().getLong("coworkerId");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
            jsonObject.put("coworkerId", coworkerId);
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(activity, FragEditProfileCoworker.this, Interactor.RequestCode_GetCoworkerDetail, Interactor.Tag_GetCoworkerDetail)
                .makeJsonPostRequest(Interactor.Method_GetCoworkerDetail, jsonObject, true);
    }

    private void updateFields(final CoworkerBean coworkerBean) {
        if (coworkerBean != null) {
            et_coWorkerName.setText(coworkerBean.getName());

            beanSelectCategory = new BeanSelectCategory();
            beanSelectCategory.setId(coworkerBean.getOccupationId());
            beanSelectCategory.setTitle(coworkerBean.getOccupation());
            et_coWorkerOccupation.setText(coworkerBean.getOccupation());

            et_coWorkerMobile.setText(coworkerBean.getMobile());
            et_coWorkerMobile.setFocusable(false);
            et_coWorkerMobile.setFocusableInTouchMode(false);
            et_coWorkerMobile.setClickable(false);

            et_coWorkerEmail.setText(coworkerBean.getEmail());

            address = ProjectUtils.getInstance().getFormattedAddress(coworkerBean.getAddress());
            et_coWorkerAddress.setText(address.toString());

            if (!coworkerBean.getNativeAddress().isEmpty()) {
                nativeAddress = ProjectUtils.getInstance().getFormattedAddress(coworkerBean.getNativeAddress());
                et_coWorkerNativeAddress.setText(nativeAddress.toString());
            } else {
                et_coWorkerNativeAddress.setText("");
            }

            if (et_coWorkerNativeAddress.getText().toString().isEmpty()) {
                checkbox_sameAsAddress.setChecked(false);
            } else {
                if (nativeAddress.toString().equalsIgnoreCase(address.toString())) {
                    checkbox_sameAsAddress.setChecked(true);
                }
            }

            et_coWorkerPanNumber.setText(coworkerBean.getPanNumber());
            et_coWorkerReferredBy.setText(coworkerBean.getReferredBy());

            System.out.println("*******************" + coworkerBean.getIdCard());
            ApplicationContext.getInstance().loadImage(coworkerBean.getIdCard(), iv_addCoWorkerPanNumberPhotoId, null, R.drawable.no_image);

            System.out.println("*******************" + coworkerBean.getPhotoId());
            ApplicationContext.getInstance().loadImage(coworkerBean.getPhotoId(), iv_addCoWorkerPhoto, null, R.drawable.no_image);

            bt_addCoWorkerProject.setText(activity.getString(R.string.updateCoWorker));
            ratingBar.setRating(Float.parseFloat(coworkerBean.getRating()));
        } else {

        }
    }


    public void addCoWorker() {
        //todo call web service here
        Map<String, String> map = new HashMap<>();

        map.put("userId", String.valueOf(PrefSetup.getInstance().getUserId()));
        map.put("loginType", PrefSetup.getInstance().getUserLoginType());

        if (coworker != null) {
            map.put("coworkerId", coworker.getId() + "");
        }
        map.put("occupationId", beanSelectCategory.getId() + "");
        map.put("occupation", beanSelectCategory.getTitle() + "");
        map.put("name", et_coWorkerName.getText().toString());
        map.put("mobile", et_coWorkerMobile.getText().toString());
        map.put("email", et_coWorkerEmail.getText().toString());
        map.put("address", address.getMergedAddress());
        if (nativeAddress != null) {
            map.put("nativeAddress", nativeAddress.getMergedAddress());
        } else {
            map.put("nativeAddress", "");
        }
        map.put("panNumber", et_coWorkerPanNumber.getText().toString());
        map.put("referredBy", et_coWorkerReferredBy.getText().toString());
        map.put("rating", ratingBar.getRating() + "");

        ArrayList<String> fileNames = new ArrayList<>();
        ArrayList<File> files = new ArrayList<>();

        if (coWorkerImage != null) {
            fileNames.add("photoId");
            files.add(coWorkerImage);
        } else {
            coWorkerImage = (File) iv_addCoWorkerPhoto.getTag();
            fileNames.add("photoId");
            files.add(coWorkerImage);
        }
        if (coWorkerID != null) {
            fileNames.add("idCard");
            files.add(coWorkerID);
        } else {
            coWorkerID = (File) iv_addCoWorkerPanNumberPhotoId.getTag();
            fileNames.add("idCard");
            files.add(coWorkerID);
        }
        new InteractorImpl(activity, FragEditProfileCoworker.this, Interactor.RequestCode_AddCoWorker, Interactor.Tag_AddCoWorker)
                .makeFileUploadingRequest(Interactor.Method_AddCoWorker, fileNames, files, map, true);
    }

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);

        if (Interactor.RequestCode_AddCoWorker == requestCode) {
            if (responsePacket.getErrorCode() == 0) {
                activity.makeToast(responsePacket.getMessage());
                activity.onBackPressed();
            } else {
                activity.makeToast(responsePacket.getMessage());
            }
        }

        if (Interactor.RequestCode_GetCoworkerDetail == requestCode) {
            if (responsePacket.getErrorCode() == 0) {
                coworker = responsePacket.getCoworkerDetail();
                updateFields(coworker);
            } else {
                activity.makeToast(responsePacket.getMessage());
            }
        }
    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {
        super.onError(requestCode, errorType);

    }
}
