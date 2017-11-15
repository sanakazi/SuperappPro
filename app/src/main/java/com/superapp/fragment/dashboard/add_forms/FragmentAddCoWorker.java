package com.superapp.fragment.dashboard.add_forms;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.baselib.image_cropping.ActivityImageCropping;
import com.baselib.image_cropping.CropImageView;
import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.ActivityMain;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.DataSenderInterface;
import com.superapp.activity.base.ErrorType;
import com.superapp.activity.base.OnItemClickListener;
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

public class FragmentAddCoWorker extends BaseFragment {

    private BaseAppCompatActivity context;
    public View fragmentView;
    long selectCategoryId = 0;
    String selectCategoryText = "";
    DataSenderInterface senderInterface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentView = inflater.inflate(R.layout.fragment_add_coworker, container, false);
        context = activity;
        if (getArguments() != null) {
            selectCategoryId = getArguments().getLong("selectCategoryId");
            selectCategoryText = getArguments().getString("selectCategoryText");
        }
        initView();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (context instanceof ActivityMain) {
            ((ActivityMain) context).setHeaderText(context.getString(R.string.createCoWorker));
            ((ActivityMain) context).showHideFloatingButton(false, R.drawable.add);
            ((ActivityMain) context).changeHeaderButton(true);
            ((ActivityMain) activity).showAd(false);
        }
        updateView();
    }

    @Override
    public void updateView() {
        super.updateView();
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private EditText et_coWorkerName, et_coWorkerOccupation, et_coWorkerMobile, et_coWorkerEmail,
            et_coWorkerAddress, et_coWorkerNativeAddress, et_coWorkerPanNumber, et_coWorkerReferredBy;

    private TextInputLayout textInputLayoutCoWorkerName, textInputLayoutCoWorkerOccupation, textInputLayoutCoWorkerMobile,
            textInputLayoutCoWorkerEmail, textInputLayoutCoWorkerAddress, textInputLayoutCoWorkerNativeAddress,
            textInputLayoutCoWorkerPanNumber, textInputLayoutCoWorkerReferredBy, textInputLayoutCoWorkerPhoto, textInputLayoutCoWorkerPanNumberPhotoId;
    private ImageView iv_addCoWorkerPhoto, iv_addCoWorkerPanNumberPhotoId;
    private CheckBox checkbox_sameAsAddress;
    private static final int CAMERA_PIC_REQUEST = 2500;
    private ListView lv_occupationList;
    TextView bt_addCoWorkerProject;
    RatingBar ratingBar;

    @Override
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

        textInputLayoutCoWorkerName = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutCoWorkerName);
        textInputLayoutCoWorkerOccupation = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutCoWorkerOccupation);
        textInputLayoutCoWorkerMobile = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutCoWorkerMobile);
        textInputLayoutCoWorkerEmail = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutCoWorkerEmail);
        textInputLayoutCoWorkerAddress = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutCoWorkerAddress);
        textInputLayoutCoWorkerNativeAddress = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutCoWorkerNativeAddress);
        textInputLayoutCoWorkerPanNumber = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutCoWorkerPanNumber);
        textInputLayoutCoWorkerReferredBy = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutCoWorkerReferredBy);
        textInputLayoutCoWorkerPhoto = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutCoWorkerPhoto);
        textInputLayoutCoWorkerPanNumberPhotoId = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutCoWorkerPanNumberPhotoId);
        ratingBar = (RatingBar) fragmentView.findViewById(R.id.ratingBar);

        et_coWorkerName.requestFocus();
        Utilities.getInstance().showKeyboard(context);
        et_coWorkerName.requestFocus();

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

        if (!TextUtils.isEmpty(selectCategoryText) || selectCategoryId > 0) {
            textInputLayoutCoWorkerPhoto.setVisibility(View.GONE);
            textInputLayoutCoWorkerPanNumberPhotoId.setVisibility(View.GONE);
            textInputLayoutCoWorkerOccupation.setVisibility(View.GONE);
        } else {
            textInputLayoutCoWorkerPhoto.setVisibility(View.VISIBLE);
            textInputLayoutCoWorkerPanNumberPhotoId.setVisibility(View.VISIBLE);
            textInputLayoutCoWorkerOccupation.setVisibility(View.VISIBLE);
        }

        senderInterface = (DataSenderInterface) getActivity();
    }

    private boolean validateFields() {
        if (TextUtils.isEmpty(et_coWorkerName.getText().toString())) {
            textInputLayoutCoWorkerName.setErrorEnabled(true);
            textInputLayoutCoWorkerName.setError(context.getString(R.string.nameRequired));
            et_coWorkerName.requestFocus();
            return false;
        } else {
            textInputLayoutCoWorkerName.setErrorEnabled(false);
            textInputLayoutCoWorkerName.setError(null);
        }
        if (selectCategoryId == 0) {
            if (TextUtils.isEmpty(et_coWorkerOccupation.getText().toString())) {
                textInputLayoutCoWorkerOccupation.setErrorEnabled(true);
                textInputLayoutCoWorkerOccupation.setError(context.getString(R.string.selectOccupation));
                et_coWorkerOccupation.requestFocus();
                return false;
            } else {
                textInputLayoutCoWorkerOccupation.setErrorEnabled(false);
                textInputLayoutCoWorkerOccupation.setError(null);
            }
        }

        if (TextUtils.isEmpty(et_coWorkerMobile.getText().toString())) {
            textInputLayoutCoWorkerMobile.setErrorEnabled(true);
            textInputLayoutCoWorkerMobile.setError(context.getString(R.string.mobileNumberRequired));
            et_coWorkerMobile.requestFocus();
            return false;
        } else {
            textInputLayoutCoWorkerMobile.setErrorEnabled(false);
            textInputLayoutCoWorkerMobile.setError(null);
        }

        if (et_coWorkerMobile.getText().toString().length() != 10) {
            textInputLayoutCoWorkerMobile.setErrorEnabled(true);
            textInputLayoutCoWorkerMobile.setError(context.getString(R.string.invalidMobileNo));
            et_coWorkerMobile.requestFocus();
            return false;
        } else {
            textInputLayoutCoWorkerMobile.setErrorEnabled(false);
            textInputLayoutCoWorkerMobile.setError(null);
        }

//        if (TextUtils.isEmpty(et_coWorkerEmail.getText().toString())) {
//            textInputLayoutCoWorkerEmail.setErrorEnabled(true);
//            textInputLayoutCoWorkerEmail.setError(context.getString(R.string.emailRequired));
//            et_coWorkerEmail.requestFocus();
//            return false;
//        } else {
//            textInputLayoutCoWorkerEmail.setErrorEnabled(false);
//            textInputLayoutCoWorkerEmail.setError(null);
//        }
//
//        if (!Utilities.getInstance().isValidEmail(et_coWorkerEmail.getText().toString())) {
//            textInputLayoutCoWorkerEmail.setErrorEnabled(true);
//            textInputLayoutCoWorkerEmail.setError(context.getString(R.string.invalidEmail));
//            et_coWorkerEmail.requestFocus();
//            return false;
//        } else {
//            textInputLayoutCoWorkerEmail.setErrorEnabled(false);
//            textInputLayoutCoWorkerEmail.setError(null);
//        }

        if (!TextUtils.isEmpty(et_coWorkerEmail.getText().toString()) && !Utilities.getInstance().isValidEmail(et_coWorkerEmail.getText().toString())) {
            textInputLayoutCoWorkerEmail.setErrorEnabled(true);
            textInputLayoutCoWorkerEmail.setError(context.getString(R.string.invalidEmail));
            et_coWorkerEmail.requestFocus();
            return false;
        } else {
            textInputLayoutCoWorkerEmail.setErrorEnabled(false);
            textInputLayoutCoWorkerEmail.setError(null);
        }

        if (TextUtils.isEmpty(et_coWorkerAddress.getText().toString())) {
            textInputLayoutCoWorkerAddress.setErrorEnabled(true);
            textInputLayoutCoWorkerAddress.setError(context.getString(R.string.addressRequired));
            et_coWorkerAddress.requestFocus();
            return false;
        } else {
            textInputLayoutCoWorkerAddress.setErrorEnabled(false);
            textInputLayoutCoWorkerAddress.setError(null);
        }
//        if (TextUtils.isEmpty(et_coWorkerNativeAddress.getText().toString().trim())) {
//            textInputLayoutCoWorkerNativeAddress.setErrorEnabled(true);
//            textInputLayoutCoWorkerNativeAddress.setError(context.getString(R.string.nativeAddressRequired));
//            et_coWorkerNativeAddress.requestFocus();
//            return false;
//        } else {
//            textInputLayoutCoWorkerNativeAddress.setErrorEnabled(false);
//            textInputLayoutCoWorkerNativeAddress.setError(null);
//        }
//        if (TextUtils.isEmpty(et_coWorkerPanNumber.getText().toString().trim())) {
//            textInputLayoutCoWorkerPanNumber.setErrorEnabled(true);
//            textInputLayoutCoWorkerPanNumber.setError(context.getString(R.string.panNumberRequired));
//            et_coWorkerPanNumber.requestFocus();
//            return false;
//        } else {
//            textInputLayoutCoWorkerPanNumber.setErrorEnabled(false);
//            textInputLayoutCoWorkerPanNumber.setError(null);
//        }

//        if (TextUtils.isEmpty(et_coWorkerReferredBy.getText().toString().trim())) {
//            textInputLayoutCoWorkerReferredBy.setErrorEnabled(true);
//            textInputLayoutCoWorkerReferredBy.setError(context.getString(R.string.referredByRequired));
//            et_coWorkerReferredBy.requestFocus();
//            return false;
//        } else {
//            textInputLayoutCoWorkerReferredBy.setErrorEnabled(false);
//            textInputLayoutCoWorkerReferredBy.setError(null);
//        }

//        if (coWorkerImage == null) {
//            try {
//                context.makeToast(context.getString(R.string.uploadPhotoId));
//                context.showImageGettingDialog();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return false;
//        }
        return true;
    }


    BeanOccupation occupation;
    BeanSelectCategory beanSelectCategory;
    private int ImageSelection = 0;

    @Override
    public void onClick(View view) {
        super.onClick(view);
        Utilities.getInstance().hideKeyboard(activity);
        switch (view.getId()) {
            case R.id.ll_addCoworker:
                if (!TextUtils.isEmpty(selectCategoryText) || selectCategoryId > 0) {
                    sendCoWorkerData();
                } else if (validateFields()) {
                    addCoWorker();
                }
                break;

            case R.id.et_coWorkerOccupation:
                PopupUtils.getInstance().showOccupationPopUp(context, 1, (object) -> {
                    beanSelectCategory = ((BeanSelectCategory) object);
                    et_coWorkerOccupation.setText(beanSelectCategory.getTitle());
                }, "Select Occupation", false);
//                PopupUtils.getInstance().showCoWorkerOccupationPopUp(context, new OnViewClickListener() {
//                    @Override
//                    public void onViewItemClick(Object object) {
//                        occupation = ((BeanOccupation) object);
//                        et_coWorkerOccupation.setText(occupation.getTitle());
//                    }
//                });
                break;

            case R.id.et_coWorkerAddress:
                PopupUtils.getInstance().showClientAddressPopup(context, object -> {
                    address = (BeanAddress) object;
                    et_coWorkerAddress.setText(address.toString());
                    if (checkbox_sameAsAddress.isChecked()) {
                        nativeAddress = address;
                        et_coWorkerNativeAddress.setText(nativeAddress.toString());
                    }
                }, address, "Address");
//                PopupUtils.getInstance().showClientAddressPopup(context, new OnViewClickListener() {
//                    @Override
//                    public void onViewItemClick(Object object) {
//                        address = (BeanAddress) object;
//                        et_coWorkerAddress.setText(address.toString());
//
//                        if (checkbox_sameAsAddress.isChecked()) {
//                            nativeAddress = address;
//                            et_coWorkerNativeAddress.setText(nativeAddress.toString());
//                        }
//                    }
//                }, address);
                break;

            case R.id.et_coWorkerNativeAddress:
                PopupUtils.getInstance().showClientAddressPopup(context, object -> {
                    nativeAddress = (BeanAddress) object;
                    et_coWorkerNativeAddress.setText(nativeAddress.toString());
                }, nativeAddress, "Native Address");
                break;

            case R.id.iv_addCoWorkerPanNumberPhotoId:
                ImageSelection = 1;
                context.showImageGettingDialog();
                break;

            case R.id.iv_addCoWorkerPhoto:
                ImageSelection = 2;
                context.showImageGettingDialog();
                break;
        }
    }

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == context.RESULT_OK) {
//            switch (requestCode) {
//                case BaseAppCompatActivity.CAPTURE_PHOTO:
//
//                case BaseAppCompatActivity.SELECT_PHOTO:
//                    Intent captureImage = new Intent(context, ActivityImageCropping.class);
//                    Uri uri = context.getImageBitmapFromIntentData(data);
//                    captureImage.putExtra("selectedImageUri", uri);
//                    captureImage.putExtra("ratio", CropImageView.CropMode.RATIO_16_9.getId());
//                    context.startActivityForResult(captureImage, BaseAppCompatActivity.CROP_IMAGE);
//                    break;
//
//                case BaseAppCompatActivity.CROP_IMAGE:
//                    Bundle extras = data.getExtras();
//                    addCameraImage(extras.getString(ActivityImageCropping.RETURN_IMAGE_PATH));
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


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
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
        }

    }

//    private void saveImageInCache(Intent data) {
//        SaveCameraImage.saveImage(getContext(), data, new SaveCameraImage.IImageSaved() {
//            @Override
//            public void onImageSaved(Uri uri) {
//                openImageCropActivity(uri);
//            }
//        });
//    }

//    private void openImageCropActivity(Uri uri) {
//        Intent cropImage = new Intent(context, ActivityImageCropping.class);
//        cropImage.putExtra("selectedImageUri", uri);
//        if (ImageSelection == 1) {
//            cropImage.putExtra("ratio", CropImageView.CropMode.RATIO_FREE.getId());
//        } else if (ImageSelection == 2) {
//            cropImage.putExtra("ratio", CropImageView.CropMode.RATIO_FREE.getId());
//        }
//        context.startActivityForResult(cropImage, BaseAppCompatActivity.CROP_IMAGE);
//    }

    File coWorkerImage, coWorkerID;

//    public void addCameraImage(String imagePath) {
//        try {
//            Bitmap bitmapImage = Utilities.getInstance().getBitmapFromPath(imagePath);
//            iv_addCoWorkerPhoto.setImageBitmap(bitmapImage);
//            coWorkerImage = Utilities.getInstance().getFileFromPath(imagePath);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public void coWorkerID(String imagePath) {
//        try {
//            Bitmap bitmapImage = Utilities.getInstance().getBitmapFromPath(imagePath);
//            iv_addCoWorkerPanNumberPhotoId.setImageBitmap(bitmapImage);
//            coWorkerID = Utilities.getInstance().getFileFromPath(imagePath);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    BeanAddress nativeAddress, address;

    public void addCoWorker() {
        //todo call web service here
        Map<String, String> map = new HashMap<>();

        map.put("userId", String.valueOf(PrefSetup.getInstance().getUserId()));
        map.put("loginType", PrefSetup.getInstance().getUserLoginType());

        try {
            if (coworker != null) {
                map.put("coworkerId", coworker.getId() + "");
            } else {
                map.put("coworkerId", 0 + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        map.put("occupationId", beanSelectCategory.getId() + "");
        map.put("occupation", beanSelectCategory.getTitle() + "");
        map.put("mobile", et_coWorkerMobile.getText().toString());
        map.put("name", et_coWorkerName.getText().toString());
        map.put("email", et_coWorkerEmail.getText().toString());
        map.put("address", address.getMergedAddress());
        map.put("nativeAddress", nativeAddress != null ? nativeAddress.getMergedAddress() : "");
        map.put("panNumber", et_coWorkerPanNumber.getText().toString());
        map.put("referredBy", et_coWorkerReferredBy.getText().toString());
        map.put("rating", ratingBar.getRating() + "");
        ArrayList<String> fileNames = new ArrayList<>();
        ArrayList<File> files = new ArrayList<>();
        if (coWorkerImage != null) {
            fileNames.add("photoId");
            files.add(coWorkerImage);
        }
        if (coWorkerID != null) {
            fileNames.add("idCard");
            files.add(coWorkerID);
        }
        new InteractorImpl(context, FragmentAddCoWorker.this, Interactor.RequestCode_AddCoWorker, Interactor.Tag_AddCoWorker)
                .makeFileUploadingRequest(Interactor.Method_AddCoWorker, fileNames, files, map, true);
    }

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
        if (responsePacket.getErrorCode() == 0) {
            if (Interactor.RequestCode_AddCoWorker == requestCode) {
                context.makeToast(responsePacket.getMessage());
                try {
                    if (onDialogDismiss != null) {
                        onDialogDismiss.onItemClick(responsePacket.getCoworkerDetail(), 0);
                    } else {
                        if (context instanceof ActivityMain)
                            ((ActivityMain) context).directBack = true;
                        context.onBackPressed();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (Interactor.RequestCode_GetCoworkerDetail == requestCode) {
                updateFields(responsePacket.getCoworkerDetail());
            } else {
                context.makeToast(responsePacket.getMessage());
            }
        } else {
            context.makeToast(responsePacket.getMessage());
        }
    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {
        super.onError(requestCode, errorType);
        if (requestCode == Interactor.RequestCode_AddCoWorker) {

        }
    }

    OnItemClickListener onDialogDismiss;

    CoworkerBean coworker;

    public void showAddCoworkerPopUp(BaseAppCompatActivity activity, final OnItemClickListener onItemClick, CoworkerBean coworker) {
        this.context = activity;
        this.coworker = coworker;
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fragmentView = inflater.inflate(R.layout.fragment_add_coworker_popup, null, false);

        fragmentView.findViewById(R.id.iv_back).setOnClickListener(v -> dialog.dismiss());
        dialog.setContentView(fragmentView);

        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        onDialogDismiss = (object, position) -> {
            if (onItemClick != null) {
                onItemClick.onItemClick(object, position);
            }
            dialog.dismiss();
        };

        dialog.show();
        initView();
        if (coworker != null) {
            TextView tv_popupTitle = (TextView) fragmentView.findViewById(R.id.tv_popupTitle);
            tv_popupTitle.setText(context.getString(R.string.updateCoWorker));
            getCoworkerDetail();
        }
    }

    private void getCoworkerDetail() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("coworkerId", coworker.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(context, FragmentAddCoWorker.this, Interactor.RequestCode_GetCoworkerDetail, Interactor.Tag_GetCoworkerDetail)
                .makeJsonPostRequest(Interactor.Method_GetCoworkerDetail, jsonObject, true);
    }

    private void updateFields(final CoworkerBean coworkerBean) {
        et_coWorkerName.setText(coworkerBean.getName());

        beanSelectCategory = new BeanSelectCategory();
        beanSelectCategory.setId(coworkerBean.getOccupationId());
        beanSelectCategory.setTitle(coworkerBean.getOccupation());
        et_coWorkerOccupation.setText(coworkerBean.getOccupation());

        et_coWorkerMobile.setText(coworkerBean.getMobile());
//        et_coWorkerMobile.setFocusable(false);
//        et_coWorkerMobile.setFocusableInTouchMode(false);
//        et_coWorkerMobile.setClickable(false);

        et_coWorkerEmail.setText(coworkerBean.getEmail());

        address = ProjectUtils.getInstance().getFormattedAddress(coworkerBean.getAddress());
        et_coWorkerAddress.setText(address.toString());

        nativeAddress = ProjectUtils.getInstance().getFormattedAddress(coworkerBean.getNativeAddress());
        et_coWorkerNativeAddress.setText(nativeAddress.toString());

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

        bt_addCoWorkerProject.setText(context.getString(R.string.updateCoWorker));
        ratingBar.setRating(Float.parseFloat(coworkerBean.getRating()));
    }

    public void sendCoWorkerData() {
        try {
            CoworkerBean coworkerBean = new CoworkerBean();
            coworkerBean.setMobile(et_coWorkerMobile.getText().toString());
            coworkerBean.setEmail(et_coWorkerEmail.getText().toString());
            coworkerBean.setAddress(address.getMergedAddress());
            coworkerBean.setNativeAddress(nativeAddress != null ? nativeAddress.getMergedAddress() : "");
            coworkerBean.setName(et_coWorkerName.getText().toString());
            coworkerBean.setPanNumber(et_coWorkerPanNumber.getText().toString());
            coworkerBean.setReferredBy(et_coWorkerReferredBy.getText().toString());
            coworkerBean.setRating(ratingBar.getRating() + "");
            coworkerBean.setOccupation(selectCategoryText);
            coworkerBean.setOccupationId(selectCategoryId);
//        senderInterface.sendCoWorkerData(coworkerBean);

//        if (context instanceof ActivityMain)
//            ((ActivityMain) context).directBack = true;
//        context.onBackPressed();
            Intent intent = new Intent();
            intent.putExtra("coworker", coworkerBean);
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
            getFragmentManager().popBackStack();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}