package com.superapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baselib.image_cropping.ActivityImageCropping;
import com.baselib.image_cropping.CropImageView;
import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.ActivityMain;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.ErrorType;
import com.superapp.beans.BeanAddress;
import com.superapp.fragment.base.BaseFragment;
import com.superapp.utils.PopupUtils;
import com.superapp.utils.PrefSetup;
import com.superapp.utils.ProjectUtils;
import com.superapp.utils.Utilities;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.ResponsePacket;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FragEditProfile extends BaseFragment {
    private View fragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentView = inflater.inflate(R.layout.edit_profile, container, false);

        initView();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActivityMain) activity).setHeaderText("Edit Profile");
        ((ActivityMain) activity).changeHeaderButton(true);
        ((ActivityMain) activity).showHideFloatingButton(false, R.drawable.add);

        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
            ((ActivityMain) activity).showHIdeSearchButton(true);
        } else {
            ((ActivityMain) activity).showHIdeSearchButton(false);
        }

        ((ActivityMain) activity).showAd(false);
    }

    private TextInputLayout textInputLayoutName, textInputLayoutDesignation, textInputLayoutCompanyName,
            textInputLayoutCompanyStrength, textInputLayoutEmail, textInputLayoutMobile,
            textInputLayoutOfficeNo, textInputLayoutDateOfBirth, textInputLayoutCompanyAddress,
            textInputLayoutResidentialAddress, textInputLayoutLogo, textInputLayoutPhoto;
    private EditText et_residentialAddress, et_companyAddress, et_dateOfBirth, et_officeNo, et_companyStrength, et_companyName, et_name, et_designation;
    private CheckBox checkbox_sameAsAddress_editProfile;
    private TextView tv_mobile, tv_email;
    private ImageView iv_uploadPhoto, iv_uploadLogo;
    private LinearLayout ll_sameAsAddress;

    private Button bt_saveChanges;
    File uploadLogo, uploadPhoto;
    private static final int CAMERA_PIC_REQUEST = 2500;

    @Override
    public void initView() {

        ll_sameAsAddress = (LinearLayout) fragmentView.findViewById(R.id.ll_sameAsAddress);

        textInputLayoutName = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutEmail);
        textInputLayoutMobile = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutMobile);

        et_name = (EditText) fragmentView.findViewById(R.id.et_name);

        tv_email = (TextView) fragmentView.findViewById(R.id.tv_email);
        tv_mobile = (TextView) fragmentView.findViewById(R.id.tv_mobile);

        textInputLayoutDesignation = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutDesignation);
        textInputLayoutCompanyName = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutCompanyName);
        textInputLayoutCompanyStrength = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutCompanyStrength);
        textInputLayoutOfficeNo = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutOfficeNo);
        textInputLayoutDateOfBirth = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutDateOfBirth);
        textInputLayoutCompanyAddress = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutCompanyAddress);
        textInputLayoutResidentialAddress = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutResidentialAddress);
        textInputLayoutLogo = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutLogo);
        textInputLayoutPhoto = (TextInputLayout) fragmentView.findViewById(R.id.textInputLayoutPhoto);

        et_designation = (EditText) fragmentView.findViewById(R.id.et_designation);
        et_companyName = (EditText) fragmentView.findViewById(R.id.et_companyName);
        et_companyStrength = (EditText) fragmentView.findViewById(R.id.et_companyStrength);
        et_officeNo = (EditText) fragmentView.findViewById(R.id.et_officeNo);
        et_dateOfBirth = (EditText) setTouchNClick(R.id.et_dateOfBirth, fragmentView);
        et_companyAddress = (EditText) setTouchNClick(R.id.et_companyAddress, fragmentView);
        et_residentialAddress = (EditText) setTouchNClick(R.id.et_residentialAddress, fragmentView);

        iv_uploadLogo = (ImageView) setTouchNClick(R.id.iv_uploadLogo, fragmentView);
        iv_uploadPhoto = (ImageView) setTouchNClick(R.id.iv_uploadPhoto, fragmentView);

        bt_saveChanges = (Button) setTouchNClick(R.id.bt_saveChanges, fragmentView);

//        et_name.requestFocus();
//        Utilities.getInstance().showKeyboard(activity);
//        et_name.requestFocus();


        checkbox_sameAsAddress_editProfile = (CheckBox) fragmentView.findViewById(R.id.checkbox_sameAsAddress_editProfile);
        checkbox_sameAsAddress_editProfile.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (companyAddress != null) {
                    residentialAddress = companyAddress;
                    et_residentialAddress.setText(residentialAddress.toString());
                }
            } else {
                residentialAddress = null;
                et_residentialAddress.setText("");
            }
        });

        updateFields();
    }

    private void updateFields() {
        et_name.setText(PrefSetup.getInstance().getUserName());
        et_designation.setText(PrefSetup.getInstance().getUserDesignation());
        et_companyName.setText(PrefSetup.getInstance().getUserCompanyName());
        et_companyStrength.setText(PrefSetup.getInstance().getUserCompanyStrength());
        tv_email.setText(PrefSetup.getInstance().getUserEmail());
        tv_mobile.setText(PrefSetup.getInstance().getUserContactNo());
        et_officeNo.setText(PrefSetup.getInstance().getUserOfficeNumber());

        if (!TextUtils.isEmpty(PrefSetup.getInstance().getUserCompanyAddress()))
            companyAddress = ProjectUtils.getInstance().getFormattedAddress(PrefSetup.getInstance().getUserCompanyAddress());

        if (!TextUtils.isEmpty(PrefSetup.getInstance().getUserResindentialAddress()))
            residentialAddress = ProjectUtils.getInstance().getFormattedAddress(PrefSetup.getInstance().getUserResindentialAddress());

        if (!TextUtils.isEmpty(PrefSetup.getInstance().getUserDob()) && !PrefSetup.getInstance().getUserDob().equals("0")) {
            dateOfBirth = Calendar.getInstance();
            dateOfBirth.setTimeInMillis(Long.parseLong(PrefSetup.getInstance().getUserDob()) * 1000);
            et_dateOfBirth.setText(ProjectUtils.getInstance().getFormattedDate(dateOfBirth));
        } else {
            dateOfBirth = null;
        }
        if (!TextUtils.isEmpty(PrefSetup.getInstance().getUserCompanyAddress()))
            et_companyAddress.setText(ProjectUtils.getInstance().getFormattedAddress(PrefSetup.getInstance().getUserCompanyAddress()).toString());

        if (!TextUtils.isEmpty(PrefSetup.getInstance().getUserResindentialAddress()))
            et_residentialAddress.setText(ProjectUtils.getInstance().getFormattedAddress(PrefSetup.getInstance().getUserResindentialAddress()).toString());

        if (et_companyAddress.getText().toString().isEmpty()) {
            checkbox_sameAsAddress_editProfile.setChecked(false);
        } else {
            if (PrefSetup.getInstance().getUserCompanyAddress().toString().equalsIgnoreCase(PrefSetup.getInstance().getUserResindentialAddress().toString())) {
                checkbox_sameAsAddress_editProfile.setChecked(true);
            } else {
                checkbox_sameAsAddress_editProfile.setChecked(false);
            }
        }

        System.out.println("*******************" + PrefSetup.getInstance().getUserCompanyLogo());
        ApplicationContext.getInstance().loadImage(PrefSetup.getInstance().getUserCompanyLogo(), iv_uploadLogo, null, R.drawable.no_image);

        System.out.println("*******************" + PrefSetup.getInstance().getUserUploadPhoto());
        ApplicationContext.getInstance().loadImage(PrefSetup.getInstance().getUserUploadPhoto(), iv_uploadPhoto, null, R.drawable.no_image);

    }

    private int ImageSelection = 0;
    BeanAddress companyAddress, residentialAddress;

    Calendar dateOfBirth;

    @Override
    public void onClick(View view) {
        super.onClick(view);

        float centreX = 0;
        float centreY = Utilities.getInstance().getValueInDP(80, activity);
        switch (view.getId()) {
            case R.id.bt_saveChanges:
                if (validateFields()) {
                    editProfile();
                }
                break;

            case R.id.iv_uploadLogo:
                ImageSelection = 1;
                activity.showImageGettingDialog();
                break;

            case R.id.iv_uploadPhoto:
                ImageSelection = 2;
                activity.showImageGettingDialog();
                break;

            case R.id.et_companyAddress:
                PopupUtils.getInstance().showClientAddressPopup(activity, object -> {
                    companyAddress = (BeanAddress) object;
                    et_companyAddress.setText(companyAddress.toString());
                    if (checkbox_sameAsAddress_editProfile.isChecked()) {
                        residentialAddress = companyAddress;
                        et_residentialAddress.setText(residentialAddress.toString());
                    }
                }, companyAddress, "Address");
                break;

            case R.id.et_residentialAddress:
                PopupUtils.getInstance().showClientAddressPopup(activity, object -> {
                    checkbox_sameAsAddress_editProfile.setChecked(false);
                    residentialAddress = (BeanAddress) object;
                    et_residentialAddress.setText(residentialAddress.toString());
                }, residentialAddress, "Address");
                break;

            case R.id.et_dateOfBirth:
                Calendar calSelection;
                if (dateOfBirth != null) {
                    calSelection = dateOfBirth;
                } else {
                    calSelection = Calendar.getInstance();
                    calSelection.add(Calendar.YEAR, -18);
                }

                Calendar calMax = Calendar.getInstance();
                calMax.add(Calendar.YEAR, -18);
                DatePickerDialog dpd = DatePickerDialog.newInstance((view1, year, monthOfYear, dayOfMonth) -> {
                    dateOfBirth = Calendar.getInstance();
                    dateOfBirth.set(Calendar.YEAR, year);
                    dateOfBirth.set(Calendar.MONTH, monthOfYear);
                    dateOfBirth.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    et_dateOfBirth.setText(ProjectUtils.getInstance().getFormattedDate(dateOfBirth));
                }, calSelection.get(Calendar.YEAR), calSelection.get(Calendar.MONTH), calSelection.get(Calendar.DAY_OF_MONTH));
                Calendar calMin = Calendar.getInstance();
                calMin.add(Calendar.YEAR, -100);
                dpd.setMinDate(calMin);
                dpd.setMaxDate(calMax);
                dpd.show(activity.getFragmentManager(), "Datepickerdialog");
                break;
        }
    }

    private boolean validateFields() {
        // cal.setTimeInMillis(scheduleBean.getEndDate() * 1000);
        if (TextUtils.isEmpty(et_name.getText().toString())) {
            textInputLayoutName.setErrorEnabled(true);
            textInputLayoutName.setError(getString(R.string.nameRequired));
            et_name.requestFocus();
            return false;
        } else {
            textInputLayoutName.setErrorEnabled(false);
            textInputLayoutName.setError(null);
        }
        if (TextUtils.isEmpty(et_designation.getText().toString())) {
            textInputLayoutDesignation.setErrorEnabled(true);
            textInputLayoutDesignation.setError(getString(R.string.designationRequired));
            et_designation.requestFocus();
            return false;
        } else {
            textInputLayoutDesignation.setErrorEnabled(false);
            textInputLayoutDesignation.setError(null);
        }

        if (TextUtils.isEmpty(et_companyName.getText().toString())) {
            textInputLayoutCompanyName.setErrorEnabled(true);
            textInputLayoutCompanyName.setError(getString(R.string.companyNameRequired));
            et_companyName.requestFocus();
            return false;
        } else {
            textInputLayoutCompanyName.setErrorEnabled(false);
            textInputLayoutCompanyName.setError(null);
        }
        if (TextUtils.isEmpty(et_companyStrength.getText().toString())) {
            textInputLayoutCompanyStrength.setErrorEnabled(true);
            textInputLayoutCompanyStrength.setError(getString(R.string.companyStrengthRequired));
            et_companyStrength.requestFocus();
            return false;
        } else {
            textInputLayoutCompanyStrength.setErrorEnabled(false);
            textInputLayoutCompanyStrength.setError(null);
        }
        if (TextUtils.isEmpty(et_officeNo.getText().toString())) {
            textInputLayoutOfficeNo.setErrorEnabled(true);
            textInputLayoutOfficeNo.setError(getString(R.string.officeNoRequired));
            et_officeNo.requestFocus();
            return false;
        } else {
            textInputLayoutOfficeNo.setErrorEnabled(false);
            textInputLayoutOfficeNo.setError(null);
        }
        if (dateOfBirth == null) {
            textInputLayoutDateOfBirth.setErrorEnabled(true);
            textInputLayoutDateOfBirth.setError(getString(R.string.dateOfBirthRequired));
            return false;
        } else {
            textInputLayoutDateOfBirth.setErrorEnabled(false);
            textInputLayoutDateOfBirth.setError(null);
        }
        if (TextUtils.isEmpty(et_companyAddress.getText().toString())) {
            textInputLayoutCompanyAddress.setErrorEnabled(true);
            textInputLayoutCompanyAddress.setError(getString(R.string.companyAddressRequired));
            et_companyAddress.requestFocus();
            return false;
        } else {
            textInputLayoutCompanyAddress.setErrorEnabled(false);
            textInputLayoutCompanyAddress.setError(null);
        }
        if (TextUtils.isEmpty(et_residentialAddress.getText().toString())) {
            textInputLayoutResidentialAddress.setErrorEnabled(true);
            textInputLayoutResidentialAddress.setError(getString(R.string.residentialAddressRequired));
            et_residentialAddress.requestFocus();
            return false;
        } else {
            textInputLayoutResidentialAddress.setErrorEnabled(false);
            textInputLayoutResidentialAddress.setError(null);
        }
        return true;
    }

    public void editProfile() {
        //todo call web service here
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(PrefSetup.getInstance().getUserId()));
        map.put("name", et_name.getText().toString());
        map.put("designation", et_designation.getText().toString());
        map.put("companyName", et_companyName.getText().toString());
        map.put("companyStrength", et_companyStrength.getText().toString());
        map.put("companyAddress", companyAddress.getMergedAddress());
        map.put("resindentialAddress", residentialAddress.getMergedAddress());
        map.put("officeNumber", et_officeNo.getText().toString());
        map.put("dateOfBirth", (dateOfBirth.getTimeInMillis() / 1000) + "");

        ArrayList<String> fileNames = new ArrayList<>();
        fileNames.add("uploadLogo");
        fileNames.add("uploadPhoto");
        ArrayList<File> files = new ArrayList<>();
        files.add(uploadLogo);
        files.add(uploadPhoto);
        new InteractorImpl(activity, FragEditProfile.this, Interactor.RequestCode_EditProfile, Interactor.Tag_EditProfile)
                .makeFileUploadingRequest(Interactor.Method_EditProfile, fileNames, files, map, true);
    }

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
        if (responsePacket.getStatus().equalsIgnoreCase("success")) {
            if (responsePacket.getErrorCode() == 0) {
                if (Interactor.RequestCode_EditProfile == requestCode) {
                    activity.makeToast(responsePacket.getMessage());
                    if (responsePacket.getUserDetail() != null)
                        PrefSetup.getInstance().setUserDetail(responsePacket.getUserDetail());
                    ((ActivityMain) activity).updateHeaderDetails();
                } else {
                    // TODO do task on error response.
                }
                activity.onBackPressed();
            }
        }
    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {
        super.onError(requestCode, errorType);
    }

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
                        uploadLogo = new File(data.getExtras().getString(ActivityImageCropping.RETURN_IMAGE_PATH));
                        Bitmap bitmap = BitmapFactory.decodeFile(uploadLogo.getAbsolutePath());
                        iv_uploadLogo.setImageBitmap(bitmap);
                    } else {
                        uploadPhoto = new File(data.getExtras().getString(ActivityImageCropping.RETURN_IMAGE_PATH));
                        Bitmap bitmap = BitmapFactory.decodeFile(uploadPhoto.getAbsolutePath());
                        iv_uploadPhoto.setImageBitmap(bitmap);
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


}
