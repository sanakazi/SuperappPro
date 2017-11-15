package com.superapp.activity.base;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baselib.image_cropping.ImageCompress;
import com.baselib.touch.TouchEffectImageViewSrc;
import com.baselib.touch.TouchEffectOnViewBackground;
import com.superapp.ApplicationContext;
import com.superapp.BuildConfig;
import com.superapp.R;
import com.superapp.fragment.base.BaseFragment;
import com.superapp.utils.Constant;
import com.superapp.utils.PrefSetup;
import com.superapp.utils.Utilities;
import com.superapp.webservice.OnResponseListener;
import com.superapp.webservice.ResponsePacket;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BaseAppCompatActivity extends AppCompatActivity implements OnClickListener, OnResponseListener {
    public static final TouchEffectOnViewBackground TOUCH = new TouchEffectOnViewBackground();
    public static final TouchEffectImageViewSrc imageTouch = new TouchEffectImageViewSrc();

    private BaseFragment liveFragment;
    public BaseAppCompatActivity activity;

    public BaseFragment getLiveFragment() {
        return liveFragment;
    }

    public void setLiveFragment(BaseFragment liveFragment) {
        this.liveFragment = liveFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    public void makeToast(String message) {
        if (!TextUtils.isEmpty(message))
            Toast.makeText(BaseAppCompatActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public void makeLongToast(String message) {
        Toast.makeText(BaseAppCompatActivity.this, message, Toast.LENGTH_LONG).show();
    }

    public void makeSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public void doNothing(View view) {

    }

    public View setClick(int id) {
        View v = findViewById(id);
        v.setOnClickListener(this);
        return v;
    }

    public View setTouchNClick(int id) {
        View v = findViewById(id);
        v.setOnClickListener(this);
        v.setOnTouchListener(TOUCH);
        return v;
    }

    public View setTouchNClickSrc(int id) {
        View v = findViewById(id);
        v.setOnClickListener(this);
        v.setOnTouchListener(imageTouch);
        return v;
    }

    protected boolean singleClick = false;
    protected boolean isOnClickActive = false;

    @Override
    public void onClick(View v) {
        if (singleClick) {
            if (!isOnClickActive) {
                isOnClickActive = true;
                onViewClick(v);
            }
        } else {
            onViewClick(v);
        }
    }

    protected void onViewClick(View v) {
        Utilities.getInstance().hideKeyboard(this);
    }

    public View setText(int id, String value) {
        View v = findViewById(id);
        ((TextView) v).setText(value);
        return v;
    }

    public View setText(int id, int value) {
        View v = findViewById(id);
        ((TextView) v).setText(value);
        return v;
    }

    ViewGroup progressView;
    private boolean isProgressShowing = false;

    public void showProgressingView() {
        if (!isProgressShowing) {
            isProgressShowing = true;
            progressView = (ViewGroup) getLayoutInflater().inflate(R.layout.popup_termsandconditions, null);
            View v = this.findViewById(android.R.id.content).getRootView();
            ViewGroup viewGroup = (ViewGroup) v;
            viewGroup.addView(progressView);
            try {
                progressView.findViewById(R.id.relativeLayout).setOnClickListener(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            setClick(R.id.relativeLayout);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scCords[] = new int[2];
            w.getLocationOnScreen(scCords);
            float x = event.getRawX() + w.getLeft() - scCords[0];
            float y = event.getRawY() + w.getTop() - scCords[1];
            if (event.getAction() == MotionEvent.ACTION_DOWN && (x < w.getLeft() || x >= w.getRight()
                    || y < w.getTop() || y > w.getBottom())) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }

    public void hideProgressingView() {
        View v = this.findViewById(android.R.id.content).getRootView();
        ViewGroup viewGroup = (ViewGroup) v;
        viewGroup.removeView(progressView);
        isProgressShowing = false;
    }

    public boolean isProgressShowing() {
        return isProgressShowing;
    }

    @Override
    public void onBackPressed() {
        if (!isProgressShowing)
            super.onBackPressed();
    }

    public void showHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    public File file;
    public final static int CAPTURE_PHOTO = 100;
    public final static int SELECT_PHOTO = 101;
    public final static int CROP_IMAGE = 1001;
    public final static int REQUEST_EXTERNAL_PERMISSION = 1055;
    public final static int PICK_FILE_ACTIVITY_REQUEST_CODE = 102;
    public final static int READ_EXTERNAL_STORAGE = 002;

    private boolean checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public boolean checkStorageReadPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void showImageGettingDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkStoragePermission()) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_PERMISSION);
                return;
            }
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title
        alertDialogBuilder.setTitle(getString(R.string.uploadImage));
        // set dialog message
        alertDialogBuilder.setItems(R.array.pickerArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    openCamera();
                } else if (which == 1) {
                    openGallary();
                }
            }
        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    public File tempFile = null;

    public Uri getImageBitmapFromIntentData(Intent data) {
        ImageCompress image = new ImageCompress();
        String tempFilePath = "";
        if (file == null || !file.exists()) {
            if (data != null && data.getData() != null)
                tempFilePath = image.compressImage(this, data.getData().toString());
        } else {
            tempFilePath = image.compressImage(this, file.getPath());
            try {
                file.delete();
            } catch (Exception e) {
            }
        }
        if (tempFilePath == null || tempFilePath.equals("")) {
            return null;
        }
        tempFile = new File(tempFilePath);
        if (tempFile == null) {
            makeToast("Error while reading Image File null");
            return null;
        }
        return Uri.fromFile(tempFile);
    }

    private void createFile() {
        file = null;
        if (!ApplicationContext.getInstance().getCACHE_DIR().exists()) {
            ApplicationContext.getInstance().getCACHE_DIR().mkdirs();
        }
        file = new File(ApplicationContext.getInstance().getCACHE_DIR(), System.currentTimeMillis() + "-image.jpg");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void openCamera() {
//        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//        camera.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1024 * 1024);
//        startActivityForResult(camera, CAPTURE_PHOTO);

        Uri photoURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", getTempImage());

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            photoURI = Uri.fromFile(getTempImage());
        }

        Intent cIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(cIntent, CAPTURE_PHOTO);
    }

    public final String DOC_EXT = ".doc";
    public final String DOCX_EXT = ".docx";
    public final String PDF_EXT = ".pdf";
    public final String XLS_EXT = ".xls";
    public final String XLSX_EXT = ".xlsx";

    public void pickFile() {
        Intent pickFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickFileIntent.setType("*/*");
        startActivityForResult(pickFileIntent, PICK_FILE_ACTIVITY_REQUEST_CODE);
    }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (REQUEST_EXTERNAL_PERMISSION == requestCode) {
                showImageGettingDialog();
            }
        } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            if (REQUEST_EXTERNAL_PERMISSION == requestCode) {
                makeLongToast(getString(R.string.storagePermissionNotGranted));
                final Intent i = new Intent();
                i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                i.addCategory(Intent.CATEGORY_DEFAULT);
                i.setData(Uri.parse("package:" + getPackageName()));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(i);
            }
        }
    }

    public final int PERMISSION_REQUEST_CODE_LOCATION = 100;

    public static boolean checkPermission(String strPermission) {
        int result = ContextCompat.checkSelfPermission(ApplicationContext.getInstance(), strPermission);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public void requestPermission(String strPermission, int perCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(BaseAppCompatActivity.this, strPermission)) {
//            Toast.makeText(ApplicationContext.getInstance(), "GPS permission allows us to access location data. Please allow in App FragmentSettings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(BaseAppCompatActivity.this, new String[]{strPermission}, perCode);
        }
    }

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {

    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {
        if (ErrorType.ERROR500 == errorType) {
            makeToast(getString(R.string.error500));
        } else if (ErrorType.ERROR == errorType) {
            makeToast(getString(R.string.error));
        }
    }

    public File getTempImage() {
        try {
            File file = new File(ApplicationContext.getInstance().getExternalCacheDir(),
                    String.valueOf(System.currentTimeMillis()) + ".png");
            if (!file.exists()) {
                file.createNewFile();
            }

            PrefSetup.getInstance().setTempImage(Uri.fromFile(file).toString());
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveImage(Bitmap image, File file) {
        if (file == null) {
            Log.d("Save Image", "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("Save Image", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("Save Image", "Error accessing file: " + e.getMessage());
        }
    }
}
