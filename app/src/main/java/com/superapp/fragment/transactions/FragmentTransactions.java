package com.superapp.fragment.transactions;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baselib.image_cropping.ActivityImageCropping;
import com.baselib.image_cropping.CropImageView;
import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.ActivityMain;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.ErrorType;
import com.superapp.beans.BeanSelectCategory;
import com.superapp.beans.BeanTotalBalanceMoney;
import com.superapp.beans.BeanTransaction;
import com.superapp.fragment.base.BaseFragment;
import com.superapp.multipleimage.helpers.Constants;
import com.superapp.utils.Constant;
import com.superapp.utils.PopupUtils;
import com.superapp.utils.PrefSetup;
import com.superapp.utils.SaveCameraImage;
import com.superapp.utils.Utilities;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.OnResponseListener;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.superapp.activity.base.BaseAppCompatActivity.REQUEST_EXTERNAL_PERMISSION;
import static com.superapp.activity.base.BaseAppCompatActivity.SELECT_PHOTO;

public class FragmentTransactions extends BaseFragment {
    View fragmentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentView = inflater.inflate(R.layout.fragment_transaction, container, false);
        initView();
        return fragmentView;
    }

    private RecyclerView rv_transaction;
    private TextView tv_noDataFound;
    private AdapterTransaction adapterTransaction;
    TextView tv_requestMoney, tv_spentMoney;
    //    Switch toggleButton;
    LinearLayout ll_transactionItem, ll_moneyButtons;

    private boolean updateToggle = false;

    @Override
    public void initView() {
        tv_noDataFound = (TextView) fragmentView.findViewById(R.id.tv_noDataFound);
        rv_transaction = (RecyclerView) fragmentView.findViewById(R.id.rv_transaction);
        ll_transactionItem = (LinearLayout) fragmentView.findViewById(R.id.ll_transactionItem);
        ll_moneyButtons = (LinearLayout) fragmentView.findViewById(R.id.ll_moneyButtons);
        GridLayoutManager manager = new GridLayoutManager(ApplicationContext.getInstance(), 1);
        rv_transaction.setHasFixedSize(true);
        rv_transaction.setLayoutManager(manager);
        rv_transaction.setItemAnimator(new DefaultItemAnimator());
        tv_requestMoney = (TextView) setTouchNClick(R.id.tv_requestMoney, fragmentView);
        tv_spentMoney = (TextView) setTouchNClick(R.id.tv_spentMoney, fragmentView);
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        toggleButton = (Switch) fragmentView.findViewById(R.id.toggleButton);
//        toggleButton.setVisibility(View.VISIBLE);
//        toggleButton.setChecked(ApplicationContext.getInstance().project.getToggleNotification().equalsIgnoreCase("t"));
//        toggleButton.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
//            if (updateToggle) {
//                updateToggle = false;
//                return;
//            }
//            if (isChecked) {
//                toggleNotification(true);
//            } else {
//                toggleNotification(false);
//            }
//        });
        showRequestMoneyScreen();
    }


    public void notificationView() {

    }

    @Override
    public void onBackButtonClick(View view) {
        super.onBackButtonClick(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (activity instanceof ActivityMain) {
            ((ActivityMain) activity).setHeaderText("TRANSACTIONS");
            ((ActivityMain) activity).changeHeaderButton(true);
            if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
                ((ActivityMain) activity).showHIdeSearchButton(true);
//                ((ActivityMain) activity).showHideFloatingButton(true, R.drawable.add);

                if (ApplicationContext.getInstance().project.getIsComplete().equalsIgnoreCase("f")) {
                    ((ActivityMain) activity).showHideFloatingButton(true, R.drawable.add);
                } else if (ApplicationContext.getInstance().project.getIsComplete().equalsIgnoreCase("t")) {
                    ((ActivityMain) activity).showHideFloatingButton(false, R.drawable.add);
                }
                ll_moneyButtons.setVisibility(View.VISIBLE);
            } else {
                ((ActivityMain) activity).showHIdeSearchButton(false);
                ((ActivityMain) activity).showHideFloatingButton(false, R.drawable.add);
                if (ApplicationContext.getInstance().project != null && ApplicationContext.getInstance().project.getToggleNotification().equalsIgnoreCase("t")) {
                    ll_moneyButtons.setVisibility(View.VISIBLE);
                } else {
                    ll_moneyButtons.setVisibility(View.GONE);
                }
            }
            ((ActivityMain) activity).showAd(false);
            updateView();
        }
    }

    @Override
    public void updateView() {
        super.updateView();
        try {
            getAllTransactions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_requestMoney:
                showRequestMoneyScreen();
                break;
            case R.id.tv_spentMoney:
                showSpentMoneyScreen();
                break;
        }
    }

//    public void toggleNotification(boolean toggleNotification) {
//
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
//            jsonObject.put("projectId", ApplicationContext.getInstance().project.getProjectId());
//            if (toggleNotification)
//                jsonObject.put("toggleNotification", "t");
//            else
//                jsonObject.put("toggleNotification", "f");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        new InteractorImpl(activity, FragmentTransactions.this, Interactor.RequestCode_ToggleNotification, Interactor.Tag_ToggleNotification)
//                .makeJsonPostRequest(Interactor.Method_ToggleNotification, jsonObject, true);
//    }

    boolean isRequesting = true;

    private void showRequestMoneyScreen() {
        isRequesting = true;
        rv_transaction.setVisibility(View.GONE);
        tv_requestMoney.setBackgroundColor(activity.getResources().getColor(R.color.colorLightGray));
        tv_spentMoney.setBackgroundColor(activity.getResources().getColor(R.color.colorTextHintColor));
//        toggleButton.setVisibility(View.GONE);
        getAllTransactions();
    }

    private void showSpentMoneyScreen() {
        isRequesting = false;
        rv_transaction.setVisibility(View.GONE);
//        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
//           toggleButton.setVisibility(View.VISIBLE);
//        } else
//            toggleButton.setVisibility(View.GONE);

        tv_requestMoney.setBackgroundColor(activity.getResources().getColor(R.color.colorTextHintColor));
        tv_spentMoney.setBackgroundColor(activity.getResources().getColor(R.color.colorLightGray));

        getAllTransactions();
    }

    public void getAllTransactions() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("projectId", ApplicationContext.getInstance().project.getProjectId());
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());

            if (isRequesting)
                jsonObject.put("transactionTye", "Request");
            else
                jsonObject.put("transactionTye", "Spend");

        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(activity, FragmentTransactions.this, Interactor.RequestCode_GetAllTransaction, Interactor.Tag_GetAllTransaction)
                .makeJsonPostRequest(Interactor.Method_GetAllTransaction, jsonObject, true);
    }

    public void setTransaction(final ArrayList<BeanTransaction> transaction) {
        if (transaction.size() > 0) {
            tv_noDataFound.setVisibility(View.GONE);
            rv_transaction.setVisibility(View.VISIBLE);
            ll_transactionItem.setBackgroundResource(R.color.colorLightGrayLevel4);

            adapterTransaction = new AdapterTransaction(activity, transaction, isRequesting, (object, position) -> {
                showTransactionPopup((BeanTransaction) object);
            });
            rv_transaction.setAdapter(adapterTransaction);
        } else {
            tv_noDataFound.setVisibility(View.VISIBLE);
            ll_transactionItem.setBackgroundResource(R.color.colorLightGray);
            rv_transaction.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFloatingButtonClick() {
        super.onFloatingButtonClick();
        if (isRequesting) {
            showTransactionPopup(null);
        } else {
            checkBalanceMoney();
        }
    }

    @Override
    public void onSliderButtonClick(View view) {
        super.onSliderButtonClick(view);
    }


    public void showTransactionPopup(final BeanTransaction transaction) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_transaction, null, false);
        dialog.setContentView(view);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        TextView tv_projectName = (TextView) view.findViewById(R.id.tv_projectName);
        tv_projectName.setText(ApplicationContext.getInstance().project.getProjectName());

        RadioGroup rg_money = (RadioGroup) view.findViewById(R.id.rg_money);
        RadioButton rbtn_requestMoneyFromClient = (RadioButton) view.findViewById(R.id.rbtn_requestMoneyFromClient);
        RadioButton rbtn_addMoneyBypassClient = (RadioButton) view.findViewById(R.id.rbtn_addMoneyBypassClient);
        rbtn_requestMoneyFromClient.setChecked(true);

        LinearLayout ll_submitTransaction = (LinearLayout) view.findViewById(R.id.ll_submitTransaction);
        TextView tv_clientName = (TextView) view.findViewById(R.id.tv_clientName);
        final EditText et_requestMoney = (EditText) view.findViewById(R.id.et_requestMoney);
        final EditText et_description = (EditText) view.findViewById(R.id.et_description);

        LinearLayout ll_feedbackTransactions = (LinearLayout) view.findViewById(R.id.ll_feedbackTransactions);
        TextView tv_description = (TextView) view.findViewById(R.id.tv_description);
        TextView tv_requestedMoney = (TextView) view.findViewById(R.id.tv_requestedMoney);

        ImageView iv_call = (ImageView) view.findViewById(R.id.iv_call);
        final ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);

        iv_close.setOnClickListener(v -> dialog.dismiss());

        iv_call.setOnClickListener(v -> {
            if (ApplicationContext.getInstance().project.getClient() != null)
                Utilities.getInstance().doCall(activity, ApplicationContext.getInstance().project.getClient().getPhone());
        });


        if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
            ll_submitTransaction.setVisibility(View.VISIBLE);
            ll_feedbackTransactions.setVisibility(View.GONE);
            if (ApplicationContext.getInstance().project.getClient() != null)
                tv_clientName.setText(ApplicationContext.getInstance().project.getClient().getName());
            tv_requestMoney.requestFocus();
            Utilities.getInstance().showKeyboard(activity);
            tv_requestMoney.requestFocus();
        } else if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("c")) {
            ll_submitTransaction.setVisibility(View.GONE);
            ll_feedbackTransactions.setVisibility(View.VISIBLE);
            tv_description.setText(transaction.getDescription());
            tv_requestedMoney.setText(transaction.getRequestAmount());
        }

        view.findViewById(R.id.bt_submitTransactions).setOnClickListener(v -> {
            if (et_requestMoney.getText().toString().equals("")) {
                et_requestMoney.setError("This field is required");
                et_requestMoney.requestFocus();
                return;
            } else {
                et_requestMoney.setError(null);
            }

            if (et_description.getText().toString().equals("")) {
                et_description.setError("This field is required");
                et_description.requestFocus();
                return;
            } else {
                et_description.setError(null);
            }

            Map<String, String> params = new HashMap<>();
            try {
                params.put("requestMoney", et_requestMoney.getText().toString());
                params.put("description", et_description.getText().toString());
                params.put("projectId", String.valueOf(ApplicationContext.getInstance().project.getProjectId()));
                params.put("transactionTye", "Request");
                params.put("byPassClient", rbtn_requestMoneyFromClient.isChecked() ? "no" : "yes");

//                if (ApplicationContext.getInstance().project.getClient() != null)
//                    jsonObject.put("clientId", ApplicationContext.getInstance().project.getClient().getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            addUpdateTransaction(dialog, null, null, params);

//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put("requestMoney", et_requestMoney.getText().toString());
//                jsonObject.put("description", et_description.getText().toString());
//                jsonObject.put("projectId", ApplicationContext.getInstance().project.getProjectId());
//                jsonObject.put("transactionTye", "Request");
//                jsonObject.put("byPassClient", rbtn_requestMoneyFromClient.isChecked() ? "no" : "yes");
//
////                if (ApplicationContext.getInstance().project.getClient() != null)
////                    jsonObject.put("clientId", ApplicationContext.getInstance().project.getClient().getId());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            addUpdateTransaction(dialog, jsonObject);
        });

        view.findViewById(R.id.bt_accept).setOnClickListener(v -> {
            Map<String, String> params = new HashMap<>();
            try {
                params.put("transactionId", String.valueOf(transaction.getId()));
                params.put("accept", "t");
                params.put("transactionTye", "Request");
            } catch (Exception e) {
                e.printStackTrace();
            }
            addUpdateTransaction(dialog, null, null, params);
        });
//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put("transactionId", transaction.getId());
//                jsonObject.put("accept", "t");
//                jsonObject.put("transactionTye", "Request");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            addUpdateTransaction(dialog, jsonObject);
//        });

        view.findViewById(R.id.bt_reject).setOnClickListener(v -> {
            Map<String, String> params = new HashMap<>();
            try {
                params.put("transactionId", String.valueOf(transaction.getId()));
                params.put("accept", "f");
                params.put("transactionTye", "Request");
            } catch (Exception e) {
                e.printStackTrace();
            }
            addUpdateTransaction(dialog, null, null, params);
        });
//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put("transactionId", transaction.getId());
//                jsonObject.put("accept", "f");
//                jsonObject.put("transactionTye", "Request");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            addUpdateTransaction(dialog, jsonObject);
//        });
    }

    BeanSelectCategory category, subCategory;

    ArrayList<File> allPhotos;
    ArrayList<File> files;
    ArrayList<String> fileName;

    ImageView iv_addMore;
    LinearLayout ll_photo, ll_photos;

    private LayoutInflater inflater;
    private final int BROWSE_IMAGE_LIMIT = 1;

    public void showSpentMoneyDialog() {
        category = null;
        subCategory = null;
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_spent_money, null, false);
        dialog.setContentView(view);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        allPhotos = new ArrayList<>();
        files = new ArrayList<>();
        fileName = new ArrayList<>();
        final TextView et_category = (TextView) view.findViewById(R.id.et_category);
        final TextView et_subCategory = (TextView) view.findViewById(R.id.et_subCategory);
        final TextView et_spentMoney = (TextView) view.findViewById(R.id.et_spentMoney);
        final TextView et_description = (TextView) view.findViewById(R.id.et_description);

        final TextView tv_availableMoney = (TextView) view.findViewById(R.id.tv_availableMoney);
        final double balanceMoney = (totalBalanceMoney.getTotalRequestMoney() - totalBalanceMoney.getTotalSpentMoney());
        tv_availableMoney.setText(activity.getString(R.string.availableMoney) + " " + String.format("%.2f", balanceMoney));

        final ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
        iv_addMore = (ImageView) view.findViewById(R.id.iv_addMore);

        ll_photo = (LinearLayout) view.findViewById(R.id.ll_photo);
        ll_photos = (LinearLayout) view.findViewById(R.id.ll_photos);

        et_spentMoney.requestFocus();
        Utilities.getInstance().showKeyboard(activity);
        et_spentMoney.requestFocus();

        iv_close.setOnClickListener(v -> dialog.dismiss());
        et_category.setOnClickListener(v ->
                PopupUtils.getInstance().showSelectCategoryPopUp(activity, 0, "Spent", object -> {
                    category = (BeanSelectCategory) object;
                    et_category.setText(category.getTitle());
                }, "Select Category")
        );

        et_subCategory.setOnClickListener(v -> {
            if (category == null) {
                activity.makeToast(activity.getString(R.string.pleaseSelectCategory));
                return;
            }
            PopupUtils.getInstance().showSelectSubCategoryPopUp(activity, category.getId(), null, object -> {
                subCategory = (BeanSelectCategory) object;
                et_subCategory.setText(subCategory.getTitle());
            }, "Select Sub Category");
        });


        iv_addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.getInstance().hideKeyboard(activity);
                activity.showImageGettingDialog();
//                selectCameraImage();
            }
        });


        view.findViewById(R.id.btn_spentMoney).setOnClickListener(v -> {
            if (category == null) {
                activity.makeToast(activity.getString(R.string.pleaseSelectCategory));
                return;
            }
            if (subCategory == null) {
                activity.makeToast(activity.getString(R.string.pleaseSelectSubCategoryFirst));
                return;
            }

            if (TextUtils.isEmpty(et_spentMoney.getText().toString())) {
                et_spentMoney.setError(activity.getString(R.string.requiredSpentMoney));
                et_spentMoney.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(et_description.getText().toString())) {
                et_description.setError(activity.getString(R.string.descriptionRequired));
                et_description.requestFocus();
                return;
            }

            if (Double.parseDouble(et_spentMoney.getText().toString()) > balanceMoney) {
                et_spentMoney.setError(activity.getString(R.string.spentMoneyError));
                et_spentMoney.requestFocus();
                return;
            }


            Map<String, String> params = new HashMap<>();

            try {
                params.put("projectId", String.valueOf(ApplicationContext.getInstance().project.getProjectId()));
                params.put("CategoryId", String.valueOf(category.getId()));
                params.put("subCategoryId", String.valueOf(subCategory.getId()));
                params.put("requestMoney", et_spentMoney.getText().toString());

                for (File photo : allPhotos) {
                    files.add(photo);
                    fileName.add("attachment" + photo.getName());
                }

                params.put("description", et_description.getText().toString());
                params.put("transactionTye", "Spend");
                if (ApplicationContext.getInstance().project.getClient() != null)
                    params.put("clientId", String.valueOf(ApplicationContext.getInstance().project.getClient().getId()));

            } catch (Exception e) {
                e.printStackTrace();
            }

            addUpdateTransaction(dialog, fileName, files, params);
            allPhotos.clear();
        });
    }


    private void addUpdateTransaction(final Dialog dialog, ArrayList<String> fileName, ArrayList<File> files, Map<String, String> params) {
        try {
            params.put("userId", String.valueOf(PrefSetup.getInstance().getUserId()));
            params.put("loginType", PrefSetup.getInstance().getUserLoginType());

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(params);
        new InteractorImpl(activity, new OnResponseListener() {
            @Override
            public void onSuccess(int requestCode, ResponsePacket responsePacket) {
                FragmentTransactions.this.onSuccess(requestCode, responsePacket);
                dialog.dismiss();
                if (responsePacket.getErrorCode() == 410) {
                    return;
                }
                if (responsePacket.getErrorCode() == 0) {
                    activity.makeToast(responsePacket.getMessage());
                }
                if (isRequesting) {
                    showRequestMoneyScreen();
                } else {
                    showSpentMoneyScreen();
                }
            }

            @Override
            public void onError(int requestCode, ErrorType errorType) {
                FragmentTransactions.this.onError(requestCode, errorType);
            }
        }, Interactor.RequestCode_AddUpdateTransaction, Interactor.Tag_AddUpdateTransaction)
                .makeFileUploadingRequest(Interactor.Method_AddUpdateTransaction, fileName, files, params, true);
    }


    public void checkBalanceMoney() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("projectId", ApplicationContext.getInstance().project.getProjectId());
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
//            if (ApplicationContext.getInstance().project.getClient() != null)
//                jsonObject.put("clientId", ApplicationContext.getInstance().project.getClient().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(activity, FragmentTransactions.this, Interactor.RequestCode_checkBalanceMoney, Interactor.Tag_checkBalanceMoney)
                .makeJsonPostRequest(Interactor.Method_checkBalanceMoney, jsonObject, true);
    }


    BeanTotalBalanceMoney totalBalanceMoney;

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
        super.onSuccess(requestCode, responsePacket);
        if (responsePacket.getErrorCode() == 410) {
            return;
        }
        if (responsePacket.getErrorCode() == 0) {
            if (Interactor.RequestCode_GetAllTransaction == requestCode) {
                if (responsePacket.getErrorCode() == 0) {
                    setTransaction(responsePacket.getTransactionList());
                    tv_noDataFound.setText(responsePacket.getMessage());
                }
            } else if (Interactor.RequestCode_ToggleNotification == requestCode) {
//                ApplicationContext.getInstance().project.setToggleNotification(toggleButton.isChecked() ? "t" : "f");
                activity.makeToast(responsePacket.getMessage());
            } else if (Interactor.RequestCode_checkBalanceMoney == requestCode) {
                if (responsePacket.getErrorCode() == 0) {
                    totalBalanceMoney = responsePacket.getTotalBalanceMoney();
                    if (totalBalanceMoney.getTotalRequestMoney() - totalBalanceMoney.getTotalSpentMoney() > 0) {
                        showSpentMoneyDialog();
                    } else {
                        Toast.makeText(activity, getString(R.string.spendMoneyEqual), Toast.LENGTH_LONG).show();
                    }
//                    tv_noDataFound.setText(responsePacket.getMessage());
                }
            } else {
                activity.makeToast(responsePacket.getMessage());
            }
        }
    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {
        super.onError(requestCode, errorType);
        if (Interactor.RequestCode_ToggleNotification == requestCode) {
            updateToggle = true;
//            toggleButton.setChecked(!toggleButton.isChecked());
        }
    }

    private void openImageCropActivity(Uri uri) {
        Intent cropImage = new Intent(activity, ActivityImageCropping.class);
        cropImage.putExtra("selectedImageUri", uri);
        cropImage.putExtra("ratio", CropImageView.CropMode.RATIO_FREE.getId());
        startActivityForResult(cropImage, BaseAppCompatActivity.CROP_IMAGE);
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
            bitmaps = getCompressedBitmap(bitmapImage, 1080f * 2, 720f * 2);
            iv_image.setImageBitmap(bitmaps);
            ImageView iv_deleteImage = (ImageView) view.findViewById(R.id.iv_deleteImage);
            iv_deleteImage.setVisibility(View.VISIBLE);
            iv_deleteImage.setOnTouchListener(BaseAppCompatActivity.imageTouch);
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

    public void showGalleryViewDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkStoragePermission()) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_PERMISSION);
                return;
            }
        }
        openGallary();
    }

    private void showHideAddMoreImagesButton() {
        iv_addMore.setVisibility(allPhotos.size() < BROWSE_IMAGE_LIMIT ? View.VISIBLE : View.GONE);
    }


    private boolean checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case BaseAppCompatActivity.CAPTURE_PHOTO:
//                    saveImageInCache(data);
                    Uri uri = Uri.parse(PrefSetup.getInstance().getTempImage());
                    getActivity().getContentResolver().notifyChange(uri, null);
                    openImageCropActivity(Uri.parse(PrefSetup.getInstance().getTempImage()));
                    break;

                case SELECT_PHOTO:
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    activity.saveImage(bitmap, activity.getTempImage());
                    openImageCropActivity(Uri.parse(PrefSetup.getInstance().getTempImage()));
                    break;

                case Constants.REQUEST_CODE:
                    break;

                case 121:
                    //THIS IS YOUR Uri
                    Uri uri1 = picUri;
                    addImageInLinearLayout(uri1.getPath());
                    break;

                case BaseAppCompatActivity.CROP_IMAGE:
                    Bundle extras = data.getExtras();
                    addImageInLinearLayout(extras.getString(ActivityImageCropping.RETURN_IMAGE_PATH));
                    break;
            }
        }
    }
}
