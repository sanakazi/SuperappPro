package com.superapp.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baselib.image_cropping.ActivityImageCropping;
import com.baselib.image_cropping.CropImageView;
import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.ActivityMain;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.ErrorType;
import com.superapp.beans.BeanSelectCategory;
import com.superapp.beans.CoworkerSSBean;
import com.superapp.beans.Region;
import com.superapp.fragment.base.BaseFragment;
import com.superapp.fragment.dashboard.add_forms.coworker_occupation.BeanOccupation;
import com.superapp.multipleimage.helpers.Constants;
import com.superapp.utils.Constant;
import com.superapp.utils.PickerDialog;
import com.superapp.utils.PopupUtils;
import com.superapp.utils.PrefSetup;
import com.superapp.utils.ProjectUtils;
import com.superapp.utils.SaveCameraImage;
import com.superapp.utils.Utilities;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.RegionService;
import com.superapp.webservice.ResponsePacket;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.superapp.activity.base.BaseAppCompatActivity.REQUEST_EXTERNAL_PERMISSION;
import static com.superapp.activity.base.BaseAppCompatActivity.SELECT_PHOTO;
import static com.superapp.activity.base.BaseAppCompatActivity.imageTouch;

public class FragSSEditProfileCoworker extends BaseFragment {
    public View fragmentView;

    CoworkerSSBean coworkerSSBean;
    BeanOccupation occupation;
    Region region, regionState, regionCity;

    private TextInputLayout til_ss_personName, til_ss_occupation, til_ss_selectCategory, til_ss_mobile, til_ss_email, til_ss_addressLine1, til_ss_addressLine2, til_ss_selectCountry,
            til_ss_selectState, til_ss_selectCity, til_ss_locality, til_ss_closedOn, til_ss_preferredLocation;

    private LinearLayout ll_ss_mobile, ll_ss_photos, ll_ss_photo, ll_ss_updateCoworker;

    private EditText et_ss_selectCategory, et_ss_business, et_ss_personName, et_ss_occupation, /*et_ss_mobile,*/
            et_ss_officeNumber, et_ss_email,
            et_ss_panNumber, et_ss_website, et_ss_addressLine1, et_ss_addressLine2, et_ss_selectCountry, et_ss_selectState, et_ss_selectCity, et_ss_locality, et_ss_pinCode, et_ss_specialFeatures,
            et_ss_workingHour_from, et_ss_workingHour_to, et_ss_closedOn, et_ss_referredBy, et_ss_preferredLocation;

    private TextView tv_ss_mobile;
    private ImageView iv_ss_addMore, iv_ss_Photo, iv_ss_id;

    private Calendar timeFrom, timeTo;
    private boolean isStartTimeSated, isEndTimeSated;

    private static final int CAMERA_PIC_REQUEST = 2500;
    private int ImageSelection = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentView = inflater.inflate(R.layout.frag_ss_editprofile_coworker, container, false);
        initView();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (activity instanceof ActivityMain) {
            ((ActivityMain) activity).setHeaderText(activity.getString(R.string.editProfile));
            ((ActivityMain) activity).showHideFloatingButton(false, R.drawable.add);
            ((ActivityMain) activity).changeHeaderButton(true);
            ((ActivityMain) activity).showAd(false);
        }
    }

    public void initView() {
        try {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            til_ss_personName = (TextInputLayout) fragmentView.findViewById(R.id.til_ss_personName);
            til_ss_occupation = (TextInputLayout) fragmentView.findViewById(R.id.til_ss_occupation);
            til_ss_selectCategory = (TextInputLayout) fragmentView.findViewById(R.id.til_ss_selectCategory);
            til_ss_mobile = (TextInputLayout) fragmentView.findViewById(R.id.til_ss_mobile);
            til_ss_email = (TextInputLayout) fragmentView.findViewById(R.id.til_ss_email);
            til_ss_addressLine1 = (TextInputLayout) fragmentView.findViewById(R.id.til_ss_addressLine1);
            til_ss_addressLine2 = (TextInputLayout) fragmentView.findViewById(R.id.til_ss_addressLine2);
            til_ss_selectCountry = (TextInputLayout) fragmentView.findViewById(R.id.til_ss_selectCountry);
            til_ss_selectState = (TextInputLayout) fragmentView.findViewById(R.id.til_ss_selectState);
            til_ss_selectCity = (TextInputLayout) fragmentView.findViewById(R.id.til_ss_selectCity);
            til_ss_locality = (TextInputLayout) fragmentView.findViewById(R.id.til_ss_locality);
            til_ss_preferredLocation = (TextInputLayout) fragmentView.findViewById(R.id.til_ss_preferredLocation);
            til_ss_closedOn = (TextInputLayout) fragmentView.findViewById(R.id.til_ss_closedOn);

            et_ss_personName = (EditText) fragmentView.findViewById(R.id.et_ss_personName);
            et_ss_business = (EditText) fragmentView.findViewById(R.id.et_ss_business);
            et_ss_occupation = (EditText) fragmentView.findViewById(R.id.et_ss_occupation);
            et_ss_selectCategory = (EditText) fragmentView.findViewById(R.id.et_ss_selectCategory);
//        et_ss_mobile = (EditText) fragmentView.findViewById(R.id.et_ss_mobile);
            et_ss_officeNumber = (EditText) fragmentView.findViewById(R.id.et_ss_officeNumber);
            et_ss_email = (EditText) fragmentView.findViewById(R.id.et_ss_email);
            et_ss_panNumber = (EditText) fragmentView.findViewById(R.id.et_ss_panNumber);
            et_ss_website = (EditText) fragmentView.findViewById(R.id.et_ss_website);
            et_ss_addressLine1 = (EditText) fragmentView.findViewById(R.id.et_ss_addressLine1);
            et_ss_addressLine2 = (EditText) fragmentView.findViewById(R.id.et_ss_addressLine2);
            et_ss_selectCountry = (EditText) fragmentView.findViewById(R.id.et_ss_selectCountry);
            et_ss_selectState = (EditText) fragmentView.findViewById(R.id.et_ss_selectState);
            et_ss_selectCity = (EditText) fragmentView.findViewById(R.id.et_ss_selectCity);
            et_ss_locality = (EditText) fragmentView.findViewById(R.id.et_ss_locality);
            et_ss_preferredLocation = (EditText) fragmentView.findViewById(R.id.et_ss_preferredLocation);
            et_ss_pinCode = (EditText) fragmentView.findViewById(R.id.et_ss_pinCode);
            et_ss_specialFeatures = (EditText) fragmentView.findViewById(R.id.et_ss_specialFeatures);

            et_ss_workingHour_from = (EditText) setTouchNClick(R.id.et_ss_workingHour_from, fragmentView);
            et_ss_workingHour_to = (EditText) setTouchNClick(R.id.et_ss_workingHour_to, fragmentView);

            et_ss_closedOn = (EditText) fragmentView.findViewById(R.id.et_ss_closedOn);
            et_ss_referredBy = (EditText) fragmentView.findViewById(R.id.et_ss_referredBy);

            tv_ss_mobile = (TextView) fragmentView.findViewById(R.id.tv_ss_mobile);

            iv_ss_id = (ImageView) fragmentView.findViewById(R.id.iv_ss_id);
            iv_ss_Photo = (ImageView) fragmentView.findViewById(R.id.iv_ss_Photo);
            iv_ss_addMore = (ImageView) setTouchNClickSrc(R.id.iv_ss_addMore, fragmentView);

            ll_ss_mobile = (LinearLayout) fragmentView.findViewById(R.id.ll_ss_mobile);
            ll_ss_photos = (LinearLayout) fragmentView.findViewById(R.id.ll_ss_photos);
            ll_ss_photo = (LinearLayout) fragmentView.findViewById(R.id.ll_ss_photo);
            ll_ss_updateCoworker = (LinearLayout) fragmentView.findViewById(R.id.ll_ss_updateCoworker);

            setTouchNClick(R.id.et_ss_occupation, fragmentView);
            setTouchNClick(R.id.et_ss_selectCategory, fragmentView);
            setTouchNClick(R.id.et_ss_selectCountry, fragmentView);
            setTouchNClick(R.id.et_ss_selectState, fragmentView);
            setTouchNClick(R.id.et_ss_selectCity, fragmentView);
            setTouchNClick(R.id.et_ss_closedOn, fragmentView);

            setTouchNClick(R.id.iv_ss_id, fragmentView);
            setTouchNClick(R.id.iv_ss_Photo, fragmentView);

            setTouchNClick(R.id.ll_ss_updateCoworker, fragmentView);

            getCoworkerSSDetail();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validateFields() {

        if (TextUtils.isEmpty(et_ss_personName.getText().toString())) {
            til_ss_personName.setErrorEnabled(true);
            til_ss_personName.setError(getActivity().getString(R.string.personNameRequired));
            et_ss_personName.requestFocus();
            return false;
        } else {
            til_ss_personName.setErrorEnabled(false);
            til_ss_personName.setError(null);
        }

        if (TextUtils.isEmpty(et_ss_occupation.getText().toString())) {
            til_ss_occupation.setErrorEnabled(true);
            til_ss_occupation.setError(getActivity().getString(R.string.selectOccupation));
            et_ss_occupation.requestFocus();
            return false;
        } else {
            til_ss_occupation.setErrorEnabled(false);
            til_ss_occupation.setError(null);
        }

        if (TextUtils.isEmpty(et_ss_selectCategory.getText().toString())) {
            til_ss_selectCategory.setErrorEnabled(true);
            til_ss_selectCategory.setError(getActivity().getString(R.string.pleaseSelectCategory));
            et_ss_selectCategory.requestFocus();
            return false;
        } else {
            til_ss_selectCategory.setErrorEnabled(false);
            til_ss_selectCategory.setError(null);
        }

//        if (TextUtils.isEmpty(et_ss_mobile.getText().toString())) {
//            til_ss_mobile.setErrorEnabled(true);
//            til_ss_mobile.setError(getActivity().getString(R.string.mobileNumberRequired));
//            et_ss_mobile.requestFocus();
//            return false;
//        } else {
//            til_ss_mobile.setErrorEnabled(false);
//            til_ss_mobile.setError(null);
//        }

        if (!TextUtils.isEmpty(et_ss_email.getText().toString()) && !Utilities.getInstance().isValidEmail(et_ss_email.getText().toString())) {
            til_ss_email.setErrorEnabled(true);
            til_ss_email.setError(getActivity().getString(R.string.invalidEmail));
            et_ss_email.requestFocus();
            return false;
        } else {
            til_ss_email.setErrorEnabled(false);
            til_ss_email.setError(null);
        }

        if (TextUtils.isEmpty(et_ss_addressLine1.getText().toString())) {
            til_ss_addressLine1.setErrorEnabled(true);
            til_ss_addressLine1.setError(getActivity().getString(R.string.thisFieldIsRequired));
            et_ss_addressLine1.requestFocus();
            return false;
        } else {
            til_ss_addressLine1.setErrorEnabled(false);
            til_ss_addressLine1.setError(null);
        }

        if (TextUtils.isEmpty(et_ss_addressLine2.getText().toString())) {
            til_ss_addressLine2.setErrorEnabled(true);
            til_ss_addressLine2.setError(getActivity().getString(R.string.thisFieldIsRequired));
            et_ss_addressLine2.requestFocus();
            return false;
        } else {
            til_ss_addressLine2.setErrorEnabled(false);
            til_ss_addressLine2.setError(null);
        }

        if (TextUtils.isEmpty(et_ss_selectCountry.getText().toString())) {
            til_ss_selectCountry.setErrorEnabled(true);
            til_ss_selectCountry.setError(getActivity().getString(R.string.thisFieldIsRequired));
            et_ss_selectCountry.requestFocus();
            return false;
        } else {
            til_ss_selectCountry.setErrorEnabled(false);
            til_ss_selectCountry.setError(null);
        }


        if (TextUtils.isEmpty(et_ss_selectState.getText().toString())) {
            til_ss_selectState.setErrorEnabled(true);
            til_ss_selectState.setError(getActivity().getString(R.string.thisFieldIsRequired));
            et_ss_selectState.requestFocus();
            return false;
        } else {
            til_ss_selectState.setErrorEnabled(false);
            til_ss_selectState.setError(null);
        }

        if (TextUtils.isEmpty(et_ss_selectCity.getText().toString())) {
            til_ss_selectCity.setErrorEnabled(true);
            til_ss_selectCity.setError(getActivity().getString(R.string.thisFieldIsRequired));
            et_ss_selectCity.requestFocus();
            return false;
        } else {
            til_ss_selectCity.setErrorEnabled(false);
            til_ss_selectCity.setError(null);
        }

        if (TextUtils.isEmpty(et_ss_locality.getText().toString())) {
            til_ss_locality.setErrorEnabled(true);
            til_ss_locality.setError(getActivity().getString(R.string.thisFieldIsRequired));
            et_ss_locality.requestFocus();
            return false;
        } else {
            til_ss_locality.setErrorEnabled(false);
            til_ss_locality.setError(null);
        }

        if (TextUtils.isEmpty(et_ss_closedOn.getText().toString())) {
            til_ss_closedOn.setErrorEnabled(true);
            til_ss_closedOn.setError(getActivity().getString(R.string.thisFieldIsRequired));
            et_ss_closedOn.requestFocus();
            return false;
        } else {
            til_ss_closedOn.setErrorEnabled(false);
            til_ss_closedOn.setError(null);
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        Utilities.getInstance().hideKeyboard(activity);
        switch (view.getId()) {
            case R.id.ll_ss_updateCoworker:
                if (validateFields()) {
                    addSSCoWorker();
                }
                break;

            case R.id.et_ss_occupation:
                getOccupationList();
                break;

            case R.id.et_ss_selectCategory:
//                showCategoryDialog();
                break;

            case R.id.et_ss_selectCountry:
                getCountryList();
                break;

            case R.id.et_ss_selectState:
                getStateList();
                break;

            case R.id.et_ss_selectCity:
                getCityList();
                break;

            case R.id.et_ss_workingHour_from:
                workingHourFrom();
                break;

            case R.id.et_ss_workingHour_to:
                workingHourTo();
                break;

            case R.id.et_ss_closedOn:
                showClosedOnDialog();
                break;

            case R.id.iv_ss_id:
                ImageSelection = 1;
                activity.showImageGettingDialog();
                break;

            case R.id.iv_ss_Photo:
                ImageSelection = 2;
                activity.showImageGettingDialog();
                break;

            case R.id.iv_ss_addMore:
                ImageSelection = 3;
                activity.showImageGettingDialog();
//                selectCameraImage();
                break;
        }
    }

    BeanSelectCategory beanSelectCategory;

    private void getOccupationList() {
        PopupUtils.getInstance().showOccupationPopUp(activity, 1, (object) -> {
            beanSelectCategory = (BeanSelectCategory) object;
            et_ss_occupation.setText(beanSelectCategory.getTitle());
        }, "Select Occupation", false);
    }

    private void getCountryList() {
        RegionService.getRegion(getActivity(), 0, RegionService.REGION_COUNTRY, list ->
                PopupUtils.getInstance().showRegionListPopup(activity, list, "Select Country", object -> {
                    region = ((Region) object);
                    et_ss_selectCountry.setText(region.getTitle());
                    et_ss_selectCountry.setTag(region.getId());
                    et_ss_selectState.setText("");
                    et_ss_selectCity.setText("");
                }));
    }

    private void getStateList() {
//        if (et_ss_selectCountry.getTag() != null) {
        if (!TextUtils.isEmpty(et_ss_selectCountry.getText().toString())) {
//            long countryId = (long) et_ss_selectCountry.getTag();
            RegionService.getRegion(getActivity(), 1, RegionService.REGION_STATE, list ->
                    PopupUtils.getInstance().showRegionListPopup(activity, list, "Select State", object -> {
                        regionState = ((Region) object);
                        et_ss_selectState.setText(regionState.getTitle());
                        et_ss_selectState.setTag(regionState.getId());
                        et_ss_selectCity.setText("");
                    }));
        } else {
            activity.makeToast(getActivity().getString(R.string.pleaseSelectCountryFirst));
        }
    }

    private void getCityList() {
        if (et_ss_selectState.getTag() != null) {
            long stateId = (long) et_ss_selectState.getTag();
            RegionService.getRegion(getActivity(), stateId, RegionService.REGION_CITY, list ->
                    PopupUtils.getInstance().showRegionListPopup(activity, list, "Select City", object -> {
                        regionCity = ((Region) object);
                        et_ss_selectCity.setText(regionCity.getTitle());
                        et_ss_selectCity.setTag(regionCity.getId());
                    }));
        } else {
            activity.makeToast(getActivity().getString(R.string.pleaseSelectStateFirst));
        }
    }

    private void workingHourFrom() {
        timeFrom = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance((view1, hourOfDay, minute, second) -> {
            timeFrom.set(Calendar.HOUR_OF_DAY, hourOfDay);
            timeFrom.set(Calendar.MINUTE, minute);
            et_ss_workingHour_from.setText(ProjectUtils.getInstance().getFormattedTime(timeFrom));
            isStartTimeSated = true;
        }, timeFrom.get(Calendar.HOUR_OF_DAY), timeFrom.get(Calendar.MINUTE), false);
        tpd.show(activity.getFragmentManager(), "TimePickerDialog");
    }

    private void workingHourTo() {
        timeTo = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance((view1, hourOfDay, minute, second) -> {
            timeTo.set(Calendar.HOUR_OF_DAY, hourOfDay);
            timeTo.set(Calendar.MINUTE, minute);
            et_ss_workingHour_to.setText(ProjectUtils.getInstance().getFormattedTime(timeTo));
            isEndTimeSated = true;
        }, timeTo.get(Calendar.HOUR_OF_DAY), timeTo.get(Calendar.MINUTE), false);
        tpd.show(activity.getFragmentManager(), "TimePickerDialog");
    }


    private void openImageCropActivity(Uri uri) {
        String fileNamePrefix = "";
        switch (ImageSelection) {
            case 1:
                fileNamePrefix = "idCard-";
                break;
            case 2:
                fileNamePrefix = "photoId-";
                break;
            case 3:
                fileNamePrefix = "SSI-";
                break;
            default:
                fileNamePrefix = "";
        }
        Intent cropImage = new Intent(activity, ActivityImageCropping.class);
        cropImage.putExtra("selectedImageUri", uri);
        cropImage.putExtra("ratio", CropImageView.CropMode.RATIO_FREE.getId());
        cropImage.putExtra("fileNamePrefix", fileNamePrefix);
        startActivityForResult(cropImage, BaseAppCompatActivity.CROP_IMAGE);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == BaseAppCompatActivity.CROP_IMAGE) {
                if (ImageSelection == 1) {
                    coWorkerID = new File(data.getExtras().getString(ActivityImageCropping.RETURN_IMAGE_PATH));
                    Bitmap bitmap = BitmapFactory.decodeFile(coWorkerID.getAbsolutePath());
                    iv_ss_id.setImageBitmap(bitmap);
                } else if (ImageSelection == 2) {
                    coWorkerImage = new File(data.getExtras().getString(ActivityImageCropping.RETURN_IMAGE_PATH));
                    Bitmap bitmap = BitmapFactory.decodeFile(coWorkerImage.getAbsolutePath());
                    iv_ss_Photo.setImageBitmap(bitmap);
                } else {
                    Bundle extra = data.getExtras();
                    addImageInLinearLayout(extra.getString(ActivityImageCropping.RETURN_IMAGE_PATH));
                }

            } else {
                if (requestCode == BaseAppCompatActivity.CAPTURE_PHOTO) {
                    Uri uri = Uri.parse(PrefSetup.getInstance().getTempImage());
                    getActivity().getContentResolver().notifyChange(uri, null);
                    openImageCropActivity(Uri.parse(PrefSetup.getInstance().getTempImage()));
                } else if (requestCode == BaseAppCompatActivity.SELECT_PHOTO) {
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    activity.saveImage(bitmap, activity.getTempImage());
                    openImageCropActivity(Uri.parse(PrefSetup.getInstance().getTempImage()));
                }
            }

            switch (requestCode) {
//                case BaseAppCompatActivity.CAPTURE_PHOTO:
//                    saveImageInCache(data);
//                    break;

//                case BaseAppCompatActivity.SELECT_PHOTO:
//
//                    break;

//                case SELECT_PHOTO:
//                break;

//                case BaseAppCompatActivity.CROP_IMAGE:
//                    Bundle extra = data.getExtras();
//                    if (ImageSelection == 1) {
//                        coWorkerID(extra.getString(ActivityImageCropping.RETURN_IMAGE_PATH));
//                    } else if (ImageSelection == 2) {
//                        addCameraImage(extra.getString(ActivityImageCropping.RETURN_IMAGE_PATH));
//                    } else if (ImageSelection == 3)
//                        addImageInLinearLayoutGallery(extra.getString(ActivityImageCropping.RETURN_IMAGE_PATH));
//                    break;

                case Constants.REQUEST_CODE:
                    break;

                case 121:
                    //THIS IS YOUR Uri
                    Uri uri = picUri;
                    addImageInLinearLayout(uri.getPath());
                    break;

            }
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
//        Intent cropImage = new Intent(activity, ActivityImageCropping.class);
//        cropImage.putExtra("selectedImageUri", uri);
//        if (ImageSelection == 1) {
//            cropImage.putExtra("ratio", CropImageView.CropMode.RATIO_FREE.getId());
//        } else if (ImageSelection == 2) {
//            cropImage.putExtra("ratio", CropImageView.CropMode.RATIO_FREE.getId());
//        }
//        activity.startActivityForResult(cropImage, BaseAppCompatActivity.CROP_IMAGE);
//    }

    ArrayList<File> allPhotos = new ArrayList<>();
    private LayoutInflater inflater;

    public void addImageInLinearLayout(String imagePath) {
        try {
            View view = inflater.inflate(R.layout.activity_upload_image_item, null);
            Bitmap bitmapImage = Utilities.getInstance().getBitmapFromPath(imagePath);
            final File imageFile = Utilities.getInstance().getFileFromPath(imagePath);
            TextView tv_fileName = (TextView) view.findViewById(R.id.tv_fileName);
            tv_fileName.setText(imageFile.getName());
            final LinearLayout ll_parentView = (LinearLayout) view.findViewById(R.id.ll_parentView);
            ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
            iv_image.setImageBitmap(bitmapImage);
            ImageView iv_deleteImage = (ImageView) view.findViewById(R.id.iv_deleteImage);
            iv_deleteImage.setVisibility(View.VISIBLE);
            iv_deleteImage.setOnTouchListener(imageTouch);
            iv_deleteImage.setOnClickListener(v -> {
                ll_ss_photo.removeView(ll_parentView);
                allPhotos.remove(imageFile);
                showHideAddMoreImagesButton();
            });
            ll_ss_photo.addView(view);
            allPhotos.add(imageFile);
            showHideAddMoreImagesButton();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final int BROWSE_IMAGE_LIMIT = 5;

    private void showHideAddMoreImagesButton() {
        iv_ss_addMore.setVisibility(allPhotos.size() < BROWSE_IMAGE_LIMIT ? View.VISIBLE : View.GONE);
    }

    File coWorkerID, coWorkerImage;

    public void coWorkerID(String imagePath) {
        try {
            Bitmap bitmapImage = Utilities.getInstance().getBitmapFromPath(imagePath);
            iv_ss_id.setImageBitmap(bitmapImage);
            coWorkerID = Utilities.getInstance().getFileFromPath(imagePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addCameraImage(String imagePath) {
        try {
            Bitmap bitmapImage = Utilities.getInstance().getBitmapFromPath(imagePath);
            iv_ss_Photo.setImageBitmap(bitmapImage);
            coWorkerImage = Utilities.getInstance().getFileFromPath(imagePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCategoryDialog() {
        String[] categories = getResources().getStringArray(R.array.ssCategory);
        PickerDialog dialog = PickerDialog.show(getContext(), categories, object ->
                et_ss_selectCategory.setText(object.toString())
        );
        dialog.setTitle(getResources().getString(R.string.selectCategory));
    }

    private void showClosedOnDialog() {
        String[] closedOn = getResources().getStringArray(R.array.ssClosedOn);
        PickerDialog dialog = PickerDialog.show(getContext(), closedOn, object ->
                et_ss_closedOn.setText(object.toString())
        );
        dialog.setTitle(getResources().getString(R.string.closedOn));
    }

    public void addSSCoWorker() {
        try {
            //todo call web service here
            Map<String, String> map = new HashMap<>();
            ArrayList<String> fileName = new ArrayList<>();
            ArrayList<String> fileNames = new ArrayList<>();
            ArrayList<File> files = new ArrayList<>();

//        map.put("userId", String.valueOf(PrefSetup.getInstance().getUserId()));
//        map.put("loginType", PrefSetup.getInstance().getUserLoginType());

//        if (coworkerSSBean != null) {
            map.put("userId", PrefSetup.getInstance().getUserId() + "");
//        }
            map.put("name", et_ss_personName.getText().toString());
            map.put("businessName", et_ss_business.getText().toString());

            if (occupation != null) {
                map.put("occupationId", occupation.getId() + "");
                map.put("occupation", occupation.getTitle() + "");
            }

//            map.put("occupation", et_ss_occupation.getText().toString());
            map.put("category", et_ss_selectCategory.getText().toString() + "");
            map.put("mobile", tv_ss_mobile.getText().toString());
            map.put("officeNumber", et_ss_officeNumber.getText().toString());
            map.put("email", et_ss_email.getText().toString());
            map.put("panNumber", et_ss_panNumber.getText().toString());
            map.put("website", et_ss_website.getText().toString());
            map.put("address1", et_ss_addressLine1.getText().toString());
            map.put("address2", et_ss_addressLine2.getText().toString());

            map.put("closedWeek", et_ss_closedOn.getText().toString());
            map.put("sfQuality", et_ss_specialFeatures.getText().toString());
            map.put("stiming", et_ss_workingHour_from.getText().toString());
            map.put("etiming", et_ss_workingHour_to.getText().toString());
            map.put("locality", et_ss_locality.getText().toString());
            map.put("pinCode", et_ss_pinCode.getText().toString());

            if (region != null)
                map.put("countryId", region.getId() + "");
            if (regionState != null)
                map.put("stateId", regionState.getId() + "");
            if (regionCity != null) {
                map.put("cityId", regionCity.getId() + "");
                map.put("cityName", regionCity.getCity() + "");
            }

            if (coWorkerImage != null) {
                fileNames.add("photoId");
                files.add(coWorkerImage);
            }

            if (coWorkerID != null) {
                fileNames.add("idCard");
                files.add(coWorkerID);
            }

            for (File photo : allPhotos) {
                files.add(photo);
                fileNames.add("SSI-" + photo.getName());
            }

            map.put("referredBy", et_ss_referredBy.getText().toString());

            new InteractorImpl(getActivity(), FragSSEditProfileCoworker.this, Interactor.RequestCode_UpdateSuperSearch, Interactor.Tag_UpdateSuperSearch)
                    .makeFileUploadingRequest(Interactor.Method_UpdateSuperSearch, fileNames, files, map, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCoworkerSSDetail() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(activity, FragSSEditProfileCoworker.this, Interactor.RequestCode_getCoworkerSuperSearchDetail, Interactor.Tag_getCoworkerSuperSearchDetail)
                .makeJsonPostRequest(Interactor.Method_getCoworkerSuperSearchDetail, jsonObject, true);
    }

    public void updateFields(final CoworkerSSBean coworkerSSBean) {
        try {
            et_ss_personName.setText(coworkerSSBean.getName());
            et_ss_business.setText(coworkerSSBean.getBusinessName());

            occupation = new BeanOccupation();
            occupation.setId(coworkerSSBean.getOccupationId());
            occupation.setTitle(coworkerSSBean.getOccupation());
            et_ss_occupation.setText(coworkerSSBean.getOccupation());

            et_ss_selectCategory.setText(coworkerSSBean.getCategory());

            tv_ss_mobile.setText(coworkerSSBean.getMobile());
            tv_ss_mobile.setFocusable(false);
            tv_ss_mobile.setFocusableInTouchMode(false);
            tv_ss_mobile.setClickable(false);

            et_ss_officeNumber.setText(coworkerSSBean.getOfficeNumber());
            et_ss_email.setText(coworkerSSBean.getEmail());
            et_ss_panNumber.setText(coworkerSSBean.getPanNumber());
            et_ss_website.setText(coworkerSSBean.getWebsite());
            et_ss_addressLine1.setText(coworkerSSBean.getAddress1());
            et_ss_addressLine2.setText(coworkerSSBean.getAddress2());

            region = new Region();
            region.setId(region.getId());
            region.setCountry(region.getTitle());
            et_ss_selectCountry.setText(coworkerSSBean.getCountry());

            regionState = new Region();
            regionState.setId(regionState.getId());
            regionState.setState(regionState.getTitle());
            et_ss_selectState.setText(coworkerSSBean.getState());

            regionCity = new Region();
            regionCity.setId(regionCity.getId());
            regionCity.setCity(regionCity.getTitle());
            et_ss_selectCity.setText(coworkerSSBean.getCity());

            et_ss_locality.setText(coworkerSSBean.getLocality());
            et_ss_preferredLocation.setText(coworkerSSBean.getPreferredLocation());
            et_ss_pinCode.setText(coworkerSSBean.getPinCode());
            et_ss_specialFeatures.setText(coworkerSSBean.getSfQuality());
            et_ss_workingHour_from.setText(coworkerSSBean.getStiming());
            et_ss_workingHour_to.setText(coworkerSSBean.getEtiming());
            et_ss_closedOn.setText(coworkerSSBean.getClosedWeek());
            et_ss_referredBy.setText(coworkerSSBean.getReferredBy());

            System.out.println("*******************" + coworkerSSBean.getIdCard());
            ApplicationContext.getInstance().loadImage(coworkerSSBean.getIdCard(), iv_ss_id, null, R.drawable.no_image);
            System.out.println("*******************" + coworkerSSBean.getPhotoId());
            ApplicationContext.getInstance().loadImage(coworkerSSBean.getPhotoId(), iv_ss_Photo, null, R.drawable.no_image);


            if (coworkerSSBean.getGalleryImages().size() > 0) {
                ll_ss_photo.setVisibility(View.VISIBLE);
                showImages();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
//            if (responsePacket.getErrorCode() == 0 && Interactor.RequestCode_UpdateSuperSearch == requestCode) {
//                activity.onBackPressed();
//            }

        if (Interactor.RequestCode_getCoworkerSuperSearchDetail == requestCode) {
            if (responsePacket.getErrorCode() == 0) {
                coworkerSSBean = (responsePacket.getCoworkerSSDetail());
                updateFields(coworkerSSBean);
            } else {
                activity.makeToast(responsePacket.getMessage());
            }
        }

        if (Interactor.RequestCode_UpdateSuperSearch == requestCode) {
            if (responsePacket.getErrorCode() == 0) {
                activity.makeToast(responsePacket.getMessage());
                activity.onBackPressed();
            } else {
                activity.makeToast(responsePacket.getMessage());
            }
        }

    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {
        super.onError(requestCode, errorType);

    }


    Bitmap bitmaps;

    public void addImageInLinearLayoutGallery(String imagePath) {
        try {
            View view = inflater.inflate(R.layout.activity_upload_image_item, null);
            Bitmap bitmapImage = Utilities.getInstance().getBitmapFromPath(imagePath);
            final File imageFile = Utilities.getInstance().getFileFromPath(imagePath);
            TextView tv_fileName = (TextView) view.findViewById(R.id.tv_fileName);
            tv_fileName.setText(imageFile.getName());
            final LinearLayout ll_parentView = (LinearLayout) view.findViewById(R.id.ll_parentView);
            ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
            bitmaps = getCompressedBitmap(bitmapImage, 1080f * 2, 720f * 2);
            iv_image.setImageBitmap(bitmaps);
            ImageView iv_deleteImage = (ImageView) view.findViewById(R.id.iv_deleteImage);
            iv_deleteImage.setVisibility(View.VISIBLE);
            iv_deleteImage.setOnTouchListener(activity.imageTouch);
            iv_deleteImage.setOnClickListener(v -> {
                ll_ss_photo.removeView(ll_parentView);
                allPhotos.remove(imageFile);
                showHideAddMoreImagesButton();
            });
            ll_ss_photo.addView(view);
            allPhotos.add(imageFile);
            showHideAddMoreImagesButton();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveImageInCache(Intent data) {
        SaveCameraImage.saveImage(activity, data, new SaveCameraImage.IImageSaved() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onImageSaved(Uri uri) {
                openImageCropActivity(uri);
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyApplication");

        /**Create the storage directory if it does not exist*/
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        /**Create a media file name*/
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".png");
        } else {
            return null;
        }

        return mediaFile;
    }

    Uri picUri;

    public Bitmap getCompressedBitmap(Bitmap originalBitmap, float maxWidth, float maxHeight) {
        Bitmap compressedBitmap = null;
        int actualHeight = originalBitmap.getHeight();
        int actualWidth = originalBitmap.getWidth();
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        try {
            if (actualWidth <= 0 || actualHeight <= 0) {
                return null;
            }
            compressedBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
            return null;
        }

        float ratioX = actualWidth / (float) originalBitmap.getWidth();
        float ratioY = actualHeight / (float) originalBitmap.getHeight();
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(compressedBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(originalBitmap, middleX - originalBitmap.getWidth() / 2, middleY - originalBitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        compressedBitmap = Bitmap.createBitmap(compressedBitmap, 0, 0, compressedBitmap.getWidth(), compressedBitmap.getHeight(), scaleMatrix, true);
        return compressedBitmap;
    }

//    private void selectCameraImage() {
//
//        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//        builder.setTitle("Add Photo!");
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                if (options[item].equals("Take Photo")) {
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (!checkStoragePermission()) {
//                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_PERMISSION);
//                            return;
//                        }
//                    }
//                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    //camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
////                    activity.checkStorageReadPermission();
//                    File file = getOutputMediaFile(1);
//                    picUri = Uri.fromFile(file);
//                    camera.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
//                    startActivityForResult(camera, 121);
//                  /*  Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//
//                    File file = getOutputMediaFile(1);
//                    picUri = Uri.fromFile(file); // create
//                    i.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file
//
//                    startActivityForResult(i, 121);*/
//
////                    if (activity.checkStorageReadPermission()) {
////
////                    }
//
//                } else if (options[item].equals("Choose from Gallery")) {
//
//                    showGalleryViewDialog();
//
//                } else if (options[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
//    }

    public void openGallary() {
        activity.file = null;
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, Constant.APP_NAME), SELECT_PHOTO);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, Constant.APP_NAME), SELECT_PHOTO);
        }
    }

//    public void showGalleryViewDialog() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!checkStoragePermission()) {
//                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_PERMISSION);
//                return;
//            }
//        }
//        openGallary();
//    }


//    private boolean checkStoragePermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                return true;
//            } else {
//                return false;
//            }
//        } else {
//            return true;
//        }
//    }

    private View previousView;

    private void showImages() {
//        ll_ss_photo.removeAllViews();
        for (final String image : coworkerSSBean.getGalleryImages()) {
            final View viewItem = inflater.inflate(R.layout.activity_upload_image_item, null);
            final LinearLayout ll_parentView = (LinearLayout) viewItem.findViewById(R.id.ll_parentView);
            final ImageView iv_image = (ImageView) viewItem.findViewById(R.id.iv_image);
            final ImageView iv_deleteImage = (ImageView) viewItem.findViewById(R.id.iv_deleteImage);

            iv_image.setOnClickListener(v ->
                    PopupUtils.getInstance().showImageDialog(activity, image));

            TextView tv_fileName = (TextView) viewItem.findViewById(R.id.tv_fileName);
            String fileName = image.substring(image.lastIndexOf('/') + 1, image.length());
            tv_fileName.setText(fileName);

            ProgressBar progressBar = (ProgressBar) viewItem.findViewById(R.id.progressBar);
            ApplicationContext.getInstance().loadImage(image, iv_image, progressBar, R.drawable.no_image);

            iv_deleteImage.setVisibility(View.VISIBLE);
            iv_deleteImage.setOnClickListener(v -> {
                ll_ss_photo.removeView(ll_parentView);
                allPhotos.remove(image);
                showHideAddMoreImagesButton();
            });

            ll_ss_photo.addView(viewItem);
        }
    }

}
