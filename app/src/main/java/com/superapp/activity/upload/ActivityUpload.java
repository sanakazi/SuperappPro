package com.superapp.activity.upload;

import android.Manifest;
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
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baselib.image_cropping.ActivityImageCropping;
import com.baselib.image_cropping.CropImageView;
import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.ErrorType;
import com.superapp.activity.base.OnViewClickListener;
import com.superapp.activity.base.ViewMain;
import com.superapp.beans.BeanSelectCategory;
import com.superapp.beans.CommunicationBean;
import com.superapp.multipleimage.activities.AlbumSelectActivity;
import com.superapp.multipleimage.helpers.Constants;
import com.superapp.multipleimage.models.Image;
import com.superapp.utils.Constant;
import com.superapp.utils.PopupUtils;
import com.superapp.utils.PrefSetup;
import com.superapp.utils.SaveCameraImage;
import com.superapp.utils.Utilities;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.ResponsePacket;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ActivityUpload extends BaseAppCompatActivity implements ViewMain {

    private LayoutInflater inflater;
    private final int BROWSE_DOC_LIMIT = 3;
    private final int BROWSE_IMAGE_LIMIT = 5;

    public enum COMMUNICATION_TYPE {

        APPROVAL(1),
        RECOMMENDATION(2),
        CLARIFICATION(3);

        private final int value;

        COMMUNICATION_TYPE(final int newValue) {
            value = newValue;
        }

        public int getValue() {
            return value;
        }
    }

    int communicationType;
    boolean isFeedBack = false;
    CommunicationBean communicationBean = null;
    ArrayList<File> allPhotos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setStatusBarColor();
        try {
            Bundle bundle = getIntent().getExtras();
            communicationType = bundle.getInt("communicationType");
            if (bundle.containsKey("isFeedBack")) {
                isFeedBack = bundle.getBoolean("isFeedBack");
                communicationBean = bundle.getParcelable("CommunicationBean");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    TextView et_description, tv_browse;
    ScrollView scrollView;
    ImageView iv_addMore;

    LinearLayout ll_photos, ll_documents;

    TextView tv_headingText, tv_title;
    LinearLayout ll_document, ll_photo, ll_subCategory, ll_category;
    LinearLayout ll_oldCommunication, ll_newCommunication;
    TextView tv_description;
    EditText et_feedBack;

    //    Spinner spinner_category, spinner_subCategory;
    TextView tv_category, tv_subCategory;

    BeanSelectCategory scheduleCategory, scheduleSubCategory;

    @Override
    public void initView() {
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_headingText = (TextView) findViewById(R.id.tv_headingText);
        setTouchNClickSrc(R.id.iv_back);
        setClick(R.id.bt_saveChangesEditProfile);
        setClick(R.id.tv_accept);
        setClick(R.id.tv_decline);
        setClick(R.id.tv_notConvinced);

        if (COMMUNICATION_TYPE.APPROVAL.getValue() == communicationType) {
            tv_headingText.setText(getString(R.string.approvals));
            tv_title.setText(getString(R.string.addApproval));
        } else if (COMMUNICATION_TYPE.RECOMMENDATION.getValue() == communicationType) {
            tv_headingText.setText(getString(R.string.recommendations));
            tv_title.setText(getString(R.string.addRecommendation));
        } else if (COMMUNICATION_TYPE.CLARIFICATION.getValue() == communicationType) {
            tv_headingText.setText(getString(R.string.clarifications));
            tv_title.setText(getString(R.string.addClarification));
        }

        ll_oldCommunication = (LinearLayout) findViewById(R.id.ll_oldCommunication);
        ll_newCommunication = (LinearLayout) findViewById(R.id.ll_newCommunication);

        if (!isFeedBack) {
            showNewView();
        } else {
            showFeedBackView();
        }
    }

    private void showNewView() {
        ll_oldCommunication.setVisibility(View.GONE);
        ll_newCommunication.setVisibility(View.VISIBLE);
        et_description = (TextView) findViewById(R.id.et_description);
        et_description.setOnTouchListener((v, event) -> {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    // Disallow ScrollView to intercept touch events.
                    scrollView.requestDisallowInterceptTouchEvent(true);
                    break;

                case MotionEvent.ACTION_UP:
                    // Allow ScrollView to intercept touch events.
                    scrollView.requestDisallowInterceptTouchEvent(false);
                    break;
            }
            // Handle ListView touch events.
            v.onTouchEvent(event);
            return true;
        });

        iv_addMore = (ImageView) setTouchNClickSrc(R.id.iv_addMore);
        ll_photos = (LinearLayout) findViewById(R.id.ll_photos);

        ll_documents = (LinearLayout) findViewById(R.id.ll_documents);
        tv_browse = (TextView) setTouchNClick(R.id.tv_browse);

        ll_document = (LinearLayout) findViewById(R.id.ll_document);
        ll_photo = (LinearLayout) findViewById(R.id.ll_photo);
        ll_subCategory = (LinearLayout) findViewById(R.id.ll_subCategory);
        ll_category = (LinearLayout) findViewById(R.id.ll_category);

        tv_category = (TextView) setClick(R.id.tv_category);
        tv_subCategory = (TextView) setClick(R.id.tv_subCategory);


//        spinner_category = (Spinner) findViewById(R.id.spinner_category);
//        spinner_subCategory = (Spinner) findViewById(R.id.spinner_subCategory);
//        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
//                if (i == 0) {
//                    scheduleCategory = null;
//                    return;
//                }
//                scheduleCategory = (BeanScheduleCategory) adapterView.getAdapter().getItem(i);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//        spinner_subCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
//                if (i == 0) {
//                    scheduleSubCategory = null;
//                    return;
//                }
//                scheduleSubCategory = (BeanScheduleCategory) adapterView.getAdapter().getItem(i);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        if (COMMUNICATION_TYPE.APPROVAL.getValue() == communicationType) {
            ll_document.setVisibility(View.VISIBLE);
            ll_photo.setVisibility(View.VISIBLE);
            ll_subCategory.setVisibility(View.VISIBLE);
            ll_category.setVisibility(View.VISIBLE);


//            getCategory();
//            getSubCatList();
        } else if (COMMUNICATION_TYPE.RECOMMENDATION.getValue() == communicationType) {
            ll_document.setVisibility(View.GONE);
            ll_photo.setVisibility(View.VISIBLE);
            ll_subCategory.setVisibility(View.GONE);
            ll_category.setVisibility(View.GONE);
        } else if (COMMUNICATION_TYPE.CLARIFICATION.getValue() == communicationType) {
            ll_document.setVisibility(View.GONE);
            ll_photo.setVisibility(View.VISIBLE);
            ll_subCategory.setVisibility(View.GONE);
            ll_category.setVisibility(View.GONE);
        }
    }

//    private void getCategory() {
//        JSONObject jsonObject = new JSONObject();
//
//        new InteractorImpl(ActivityUpload.this, new OnResponseListener() {
//            @Override
//            public void onSuccess(int requestCode, ResponsePacket responsePacket) {
//                if (responsePacket.getErrorCode() == 0) {
//                    ArrayList<BeanScheduleCategory> categories = new ArrayList<>();
//                    categories.add(new BeanScheduleCategory(-1, "Select"));
//                    categories.addAll(responsePacket.getScheduleCategoryList());
//                    AdapterScheduleCategory adapter = new AdapterScheduleCategory(ActivityUpload.this, categories);
//                    spinner_category.setAdapter(adapter);
//                }
//            }
//
//            @Override
//            public void onError(int requestCode, ErrorType errorType) {
//            }
//        }, Interactor.RequestCode_GetScheduleCategory, Interactor.Tag_GetScheduleCategory)
//                .makeJsonPostRequest(Interactor.Method_GetScheduleCategory, jsonObject, true);
//    }

//    public void getSubCatList() {
//        JSONObject jsonObject = new JSONObject();
//
//        new InteractorImpl(ActivityUpload.this, new OnResponseListener() {
//            @Override
//            public void onSuccess(int requestCode, ResponsePacket responsePacket) {
//                if (responsePacket.getErrorCode() == 0) {
//                    ArrayList<BeanScheduleCategory> subCategories = new ArrayList<>();
//                    subCategories.add(new BeanScheduleCategory(-1, "Select"));
//                    subCategories.addAll(responsePacket.getScheduleSubCatList());
//                    AdapterScheduleCategory adapter = new AdapterScheduleCategory(ActivityUpload.this, subCategories);
//                    spinner_subCategory.setAdapter(adapter);
//                }
//            }
//
//            @Override
//            public void onError(int requestCode, ErrorType errorType) {
//
//            }
//        }, Interactor.RequestCode_GetScheduleSubCategory, Interactor.Tag_GetScheduleSubCategory)
//                .makeJsonPostRequest(Interactor.Method_GetScheduleSubCategory, jsonObject, true);
//    }

    private void showFeedBackView() {
        ll_newCommunication.setVisibility(View.GONE);
        ll_oldCommunication.setVisibility(View.VISIBLE);
        tv_description = (TextView) findViewById(R.id.tv_description);
        et_feedBack = (EditText) findViewById(R.id.et_feedBack);
        et_feedBack.setOnTouchListener((v, event) -> {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    // Disallow ScrollView to intercept touch events.
                    scrollView.requestDisallowInterceptTouchEvent(true);
                    break;

                case MotionEvent.ACTION_UP:
                    // Allow ScrollView to intercept touch events.
                    scrollView.requestDisallowInterceptTouchEvent(false);
                    break;
            }

            // Handle ListView touch events.
            v.onTouchEvent(event);
            return true;
        });
        tv_description.setText(communicationBean.getDescription());
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.iv_addMore:
//                selectImage();
                showImageGettingDialog();
                break;

            case R.id.tv_browse:
                pickFile();
                break;

            case R.id.bt_saveChangesEditProfile:
                if (isValidCommunication())
                    submitNewCommunication();
                break;
            case R.id.tv_accept:
                if (isValidFeedback())
                    submitFeedBack("Accept");
                break;

            case R.id.tv_decline:
                et_feedBack.setText("");
                submitFeedBack("Decline");
                break;

            case R.id.tv_notConvinced:
                et_feedBack.setText("");
                submitFeedBack("Not Convince");
                break;

            case R.id.tv_category:
                PopupUtils.getInstance().showSelectCategoryPopUp(ActivityUpload.this, 0, "Approval", new OnViewClickListener() {
                    @Override
                    public void onViewItemClick(Object object) {
                        scheduleCategory = (BeanSelectCategory) object;
                        scheduleSubCategory = null;
                        tv_category.setText(scheduleCategory.getTitle());
                        System.out.print("-------------" + scheduleCategory.getId() + "---------------");
                        tv_subCategory.setText("Select Sub Category*");

                        if (scheduleCategory.getId() == 3) {
                            ll_subCategory.setVisibility(View.GONE);
                        } else {
                            ll_subCategory.setVisibility(View.VISIBLE);
                        }
                    }
                }, "Select Category");
                break;

            case R.id.tv_subCategory:
                if (scheduleCategory == null) {
                    makeToast(getString(R.string.pleaseSelectCategory));
                    return;
                }

                PopupUtils.getInstance().showOccupationPopUp(ActivityUpload.this, scheduleCategory.getId(),
                        new OnViewClickListener() {
                            @Override
                            public void onViewItemClick(Object object) {
                                scheduleSubCategory = (BeanSelectCategory) object;
                                tv_subCategory.setText(scheduleSubCategory.getTitle());
                            }
                        }, "Select Sub Category", false);
                break;
        }
    }

    private boolean isValidCommunication() {
        if (COMMUNICATION_TYPE.APPROVAL.getValue() == communicationType) {
            if (scheduleCategory == null) {
                makeToast(getString(R.string.pleaseSelectCategory));
                return false;
            }

            if (scheduleCategory.getId() != 3)
                if (scheduleSubCategory == null) {
                    makeToast(getString(R.string.pleaseSelectSubCategoryFirst));
                    return false;
                }

            if (TextUtils.isEmpty(et_description.getText().toString())) {
                et_description.setError(getString(R.string.descriptionRequired));
                return false;
            }

            if (COMMUNICATION_TYPE.APPROVAL.getValue() == communicationType) {
                if (allPhotos.size() <= 0 && allDocuments.size() <= 0) {
                    makeToast(getString(R.string.pleaseAddAtLeastOneImage));
                    return false;
                }
            }

//            if (allPhotos.size() <= 0) {
//                makeToast(getString(R.string.pleaseAddAtLeastOneImage));
//                return false;
//            }
        } else if (COMMUNICATION_TYPE.RECOMMENDATION.getValue() == communicationType || COMMUNICATION_TYPE.CLARIFICATION.getValue() == communicationType) {
            if (TextUtils.isEmpty(et_description.getText().toString())) {
                et_description.setError(getString(R.string.descriptionRequired));
                et_description.requestFocus();
                return false;
            }
        }
        return true;
    }

    private boolean isValidFeedback() {
//        if (COMMUNICATION_TYPE.APPROVAL.getValue() == communicationType) {
//            return false;
//        } else if (COMMUNICATION_TYPE.RECOMMENDATION.getValue() == communicationType || COMMUNICATION_TYPE.CLARIFICATION.getValue() == communicationType) {
        if (TextUtils.isEmpty(et_feedBack.getText().toString())) {
            et_feedBack.setError(getString(R.string.feedbackRequired));
            et_feedBack.requestFocus();
            return false;
        }
//        }
        return true;
    }

    private void submitNewCommunication() {
        Map<String, String> params = new HashMap<>();
        ArrayList<File> files = new ArrayList<>();
        ArrayList<String> fileName = new ArrayList<>();
        try {
            params.put("userId", PrefSetup.getInstance().getUserId() + "");
            params.put("loginType", PrefSetup.getInstance().getUserLoginType() + "");
            params.put("projectId", ApplicationContext.getInstance().project.getProjectId() + "");

            if (COMMUNICATION_TYPE.APPROVAL.getValue() == communicationType) {
                params.put("type", "Approval");
                params.put("category", scheduleCategory.getId() + "");

                if (scheduleCategory.getId() != 3) {
                    params.put("subCategory", scheduleSubCategory.getId() + "");
                    params.put("subCategoryText", scheduleSubCategory.getTitle() + "");
                } else {
                    params.put("subCategory", 0 + "");
                }
            } else if (COMMUNICATION_TYPE.RECOMMENDATION.getValue() == communicationType) {
                params.put("type", "Recommendations");
            } else if (COMMUNICATION_TYPE.CLARIFICATION.getValue() == communicationType) {
                params.put("type", "Clarification");
            }

            for (File photo : allPhotos) {
                files.add(photo);
                fileName.add("PhotoA" + photo.getName());
            }

            for (File doc : allDocuments) {
                files.add(doc);
                fileName.add("DocA" + System.currentTimeMillis() + doc.getName());
            }

            params.put("description", et_description.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(ActivityUpload.this, ActivityUpload.this, Interactor.RequestCode_AddEditCommunication, Interactor.Tag_AddEditCommunication)
                .makeFileUploadingRequest(Interactor.Method_AddEditCommunication, fileName, files, params, true);
    }

    private void submitFeedBack(String actionType) {
        Map<String, String> params = new HashMap<>();
        ArrayList<File> files = new ArrayList<>();
        try {
            params.put("userId", PrefSetup.getInstance().getUserId() + "");
            params.put("loginType", PrefSetup.getInstance().getUserLoginType() + "");
            params.put("projectId", ApplicationContext.getInstance().project.getProjectId() + "");

            if (COMMUNICATION_TYPE.APPROVAL.getValue() == communicationType) {
                params.put("type", "Approval");
            } else if (COMMUNICATION_TYPE.RECOMMENDATION.getValue() == communicationType) {
                params.put("type", "Recommendations");
            } else if (COMMUNICATION_TYPE.CLARIFICATION.getValue() == communicationType) {
                params.put("type", "Clarification");
            }

            params.put("communicationId", communicationBean.getId() + "");
            params.put("userFeedBack", et_feedBack.getText().toString());
            params.put("status", actionType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(ActivityUpload.this, ActivityUpload.this, Interactor.RequestCode_AddEditCommunication, Interactor.Tag_AddEditCommunication)
                .makeFileUploadingRequest(Interactor.Method_AddEditCommunication, null, files, params, true);
    }

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
        if (responsePacket.getErrorCode() == 0) {
            if (Interactor.RequestCode_AddEditCommunication == requestCode) {
                makeToast(responsePacket.getMessage());
                finish();
            }
        } else {
            makeToast(responsePacket.getMessage());
        }
    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {
        super.onError(requestCode, errorType);
    }

    File files;
    Bitmap bitmap;
    Uri uri;


//    private void saveImageInCache(Intent data) {
//        SaveCameraImage.saveImage(this, data, new SaveCameraImage.IImageSaved() {
//            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
//            @Override
//            public void onImageSaved(Uri uri) {
//                openImageCropActivityGallery(uri);
//            }
//        });
//    }

    private void openImageCropActivity(Uri uri) {
        Intent cropImage = new Intent(this, ActivityImageCropping.class);
        cropImage.putExtra("selectedImageUri", uri);
        cropImage.putExtra("ratio", CropImageView.CropMode.RATIO_FREE.getId());
        startActivityForResult(cropImage, BaseAppCompatActivity.CROP_IMAGE);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void openImageCropActivityGallery(Uri uri) {
        Intent intent = new Intent(ActivityUpload.this, AlbumSelectActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 5);
        intent.putExtra("ratio", CropImageView.CropMode.RATIO_FREE.getId());
        intent.putExtra("selectedImageUri", uri);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    Bitmap bitmaps;

    public void addImageInLinearLayout(String imagePath) {
        try {
            View view = inflater.inflate(R.layout.activity_upload_image_item, null);
            Bitmap bitmapImage = Utilities.getInstance().getBitmapFromPath(imagePath);
            final File imageFile = Utilities.getInstance().getFileFromPath(imagePath);
            TextView tv_fileName = (TextView) view.findViewById(R.id.tv_fileName);
            tv_fileName.setText(imageFile.getName());
            final LinearLayout ll_parentView = (LinearLayout) view.findViewById(R.id.ll_parentView);
            ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
//            bitmaps = Bitmap.createScaledBitmap(bitmapImage, 60, 60, true);
            bitmaps = getCompressedBitmap(bitmapImage, 1080f * 2, 720f * 2);
            if (allPhotos.size() < 5)
                iv_image.setImageBitmap(bitmaps);
            else
                return;
            ImageView iv_deleteImage = (ImageView) view.findViewById(R.id.iv_deleteImage);
            iv_deleteImage.setVisibility(View.VISIBLE);
            iv_deleteImage.setOnTouchListener(imageTouch);
            iv_deleteImage.setOnClickListener(v -> {
                ll_photos.removeView(ll_parentView);
                allPhotos.remove(imageFile);
                showHideAddMoreImagesButton();
            });
            ll_photos.addView(view);
            allPhotos.add(imageFile);
            showHideAddMoreImagesButton();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ArrayList<File> allDocuments = new ArrayList<>();

    private void addDocument(String filePath) {
        try {
            final File file = Utilities.getInstance().getFileFromPath(filePath);
            if (file != null && file.exists()) {
                int resId;
                if (file.getName().endsWith(DOC_EXT) || file.getName().endsWith(DOCX_EXT)) {
                    resId = R.drawable.word;
                } else if (file.getName().endsWith(PDF_EXT)) {
                    resId = R.drawable.pdf;
                } else if (file.getName().endsWith(XLS_EXT) || file.getName().endsWith(XLSX_EXT)) {
                    resId = R.drawable.xls;
                } else {
                    makeLongToast(getString(R.string.fileSelectionErrorMsg));
                    return;
                }

                View view = inflater.inflate(R.layout.activity_upload_image_item, null);
                final LinearLayout ll_parentView = (LinearLayout) view.findViewById(R.id.ll_parentView);
                ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
                TextView tv_fileName = (TextView) view.findViewById(R.id.tv_fileName);
                tv_fileName.setText(file.getName());

                iv_image.setImageResource(resId);
                ImageView iv_deleteImage = (ImageView) view.findViewById(R.id.iv_deleteImage);
                iv_deleteImage.setVisibility(View.VISIBLE);
                iv_deleteImage.setOnTouchListener(imageTouch);
                iv_deleteImage.setOnClickListener(v -> {
                    ll_documents.removeView(ll_parentView);
                    allDocuments.remove(file);
                    showHideBrowseButton();
                });
                ll_documents.addView(view);
                allDocuments.add(file);
                showHideBrowseButton();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showHideAddMoreImagesButton() {
        iv_addMore.setVisibility(allPhotos.size() < BROWSE_IMAGE_LIMIT ? View.VISIBLE : View.GONE);
    }

    private void showHideBrowseButton() {
        tv_browse.setVisibility(allDocuments.size() < BROWSE_DOC_LIMIT ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showProgressing() {
        super.showProgressingView();
    }

    @Override
    public void hideProgressing() {
        super.hideProgressingView();
    }

    @Override
    public void updateView() {

    }

    Uri picUri;

//    private void selectImage() {
//        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityUpload.this);
//        builder.setTitle("Add Photo!");
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                if (options[item].equals("Take Photo")) {
//
//                  /*  Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(takePicture, 0);*/
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
//                   /* Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//
//                    File file = getOutputMediaFile(1);
//                    picUri = Uri.fromFile(file); // create
//                    i.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file
//
//                    startActivityForResult(i, 121);*/
//                   /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//                    startActivityForResult(intent, 1);*/
//                } else if (options[item].equals("Choose from Gallery")) {
//                  /*  Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 5);
//                    startActivityForResult(intent, 2);*/
////                    showImageGettingDialog();
//                   /* Intent intent = new Intent(Intent.ACTION_PICK,
//                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 5);
//                    intent.putExtra("ratio", CropImageView.CropMode.RATIO_FREE.getId());
//                    intent.putExtra("selectedImageUri", uri);
//                    startActivityForResult(intent, Constants.REQUEST_CODE);*/
//                   /* Intent intent;
//                    if (Build.VERSION.SDK_INT < 19) {
//                        intent = new Intent();
//                        intent.setType("image*//*");
//                        intent.setAction(Intent.ACTION_PICK);
//                        startActivityForResult(Intent.createChooser(intent, Constant.APP_NAME), SELECT_PHOTO);
//                    }*/
//                    showGalleryDialog();
//
//                } else if (options[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
//    }

    public void openGallary() {
        file = null;
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

//    public void showGalleryDialog() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!checkStoragePermission()) {
//                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_PERMISSION);
//                return;
//            }
//        }
//
//        openGallary();
//    }

//    private boolean checkStoragePermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                return true;
//            } else {
//                return false;
//            }
//        } else {
//            return true;
//        }
//    }

//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
//    private File getOutputMediaFile(int type) {
//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), "MyApplication");
//
//        /**Create the storage directory if it does not exist*/
//        if (!mediaStorageDir.exists()) {
//            if (!mediaStorageDir.mkdirs()) {
//                return null;
//            }
//        }
//
//        /**Create a media file name*/
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        File mediaFile;
//        if (type == 1) {
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
//                    "IMG_" + timeStamp + ".png");
//        } else {
//            return null;
//        }
//
//        return mediaFile;
//    }

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


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case BaseAppCompatActivity.CAPTURE_PHOTO:
                    Uri uri = Uri.parse(PrefSetup.getInstance().getTempImage());
                    getContentResolver().notifyChange(uri, null);
                    openImageCropActivity(Uri.parse(PrefSetup.getInstance().getTempImage()));
                    break;

                case BaseAppCompatActivity.SELECT_PHOTO:
//                    saveImageInCache(data);
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    saveImage(bitmap, getTempImage());
                    openImageCropActivityGallery(Uri.parse(PrefSetup.getInstance().getTempImage()));
                    break;

                case Constants.REQUEST_CODE:
                    ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
                    for (int i = 0, l = images.size(); i < l; i++) {
                        files = new File(images.get(i).path);
//                        bitmap = Bitmap.createScaledBitmap(bitmap, 512, 100, true);
                        bitmap = getCompressedBitmap(BitmapFactory.decodeFile(files.getAbsolutePath()), 1080f * 2, 720f * 2);
//                        bitmap = BitmapFactory.decodeFile(files.getAbsolutePath());
                        addImageInLinearLayout(files.toString());
                    }
                    break;

                case 121:
                    //THIS IS YOUR Uri
                    Uri uri1 = picUri;
                    addImageInLinearLayout(uri1.getPath());
                    break;

                case BaseAppCompatActivity.CROP_IMAGE:
                    Bundle bundle = data.getExtras();
                    addImageInLinearLayout(bundle.getString(ActivityImageCropping.RETURN_IMAGE_PATH));

                case PICK_FILE_ACTIVITY_REQUEST_CODE:
                    if (data != null && data.getData() != null) {
                        try {
                            String filePath = com.baselib.image_cropping.Utilities.getInstance().getPath(this, data.getData());
                            addDocument(filePath);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }
}