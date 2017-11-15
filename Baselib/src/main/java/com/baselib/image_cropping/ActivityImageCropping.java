package com.baselib.image_cropping;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.baselib.R;

import java.io.File;
import java.io.FileOutputStream;


public class ActivityImageCropping extends AppCompatActivity {

    private Uri selectedImageUri;
    private int ratio;
    CropImageView cropImageView;
    int minFrameSize;
    public static final String RETURN_IMAGE_PATH = "RETURN_IMAGE_PATH";
    private String fileNamePrefix = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_cropping_activity);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            selectedImageUri = bundle.getParcelable("selectedImageUri");
            ratio = bundle.getInt("ratio");
            minFrameSize = bundle.getInt("minFrameSize", 120);
            fileNamePrefix = bundle.getString("fileNamePrefix", "");
        }

        if (selectedImageUri == null) {
            finish();
        }

        cropImageView = (CropImageView) findViewById(R.id.cropImageView);
        Bitmap photo = Utilities.getInstance().getBitmapFromUri(this, selectedImageUri);
        if (photo == null) {
            Toast.makeText(this, "Getting error while reading image", Toast.LENGTH_SHORT).show();
            finish();
        }
        cropImageView.setImageBitmap(photo);
        switch (ratio) {
            case 0:
                cropImageView.setCropMode(CropImageView.CropMode.RATIO_FIT_IMAGE);
                break;
            case 1:
                cropImageView.setCropMode(CropImageView.CropMode.RATIO_4_3);
                break;
            case 2:
                cropImageView.setCropMode(CropImageView.CropMode.RATIO_3_4);
                break;
            case 3:
                cropImageView.setCropMode(CropImageView.CropMode.RATIO_1_1);
                break;
            case 4:
                cropImageView.setCropMode(CropImageView.CropMode.RATIO_16_9);
                break;
            case 5:
                cropImageView.setCropMode(CropImageView.CropMode.RATIO_9_16);
                break;
            case 6:
                cropImageView.setCropMode(CropImageView.CropMode.RATIO_FREE);
                break;
            case 7:
                cropImageView.setCropMode(CropImageView.CropMode.RATIO_CUSTOM);
                break;
            case 8:
                cropImageView.setCropMode(CropImageView.CropMode.CIRCLE);
                break;
        }
        cropImageView.setMinFrameSizeInPx(Utilities.getInstance().getValueInDP(minFrameSize, this));
    }

    MenuItem action_cancel, action_done,
            action_ratio_fit_image, action_ratio_4_3,
            action_ratio_3_4, action_ratio_1_1,
            action_ratio_16_9, action_ratio_9_16,
            action_ratio_free, action_circle;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cropping_menu, menu);

        action_ratio_fit_image = menu.findItem(R.id.action_ratio_fit_image);
        action_ratio_4_3 = menu.findItem(R.id.action_ratio_4_3);
        action_ratio_3_4 = menu.findItem(R.id.action_ratio_3_4);
        action_ratio_1_1 = menu.findItem(R.id.action_ratio_1_1);
        action_ratio_16_9 = menu.findItem(R.id.action_ratio_16_9);
        action_ratio_9_16 = menu.findItem(R.id.action_ratio_9_16);
        action_ratio_free = menu.findItem(R.id.action_ratio_free);
        action_circle = menu.findItem(R.id.action_circle);

        action_cancel = menu.findItem(R.id.action_cancel);
        action_done = menu.findItem(R.id.action_done);

        if (ratio > 0) {
            action_ratio_fit_image.setVisible(false);
            action_ratio_4_3.setVisible(false);
            action_ratio_3_4.setVisible(false);
            action_ratio_1_1.setVisible(false);
            action_ratio_16_9.setVisible(false);
            action_ratio_9_16.setVisible(false);
            action_ratio_free.setVisible(false);
            action_circle.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == action_cancel.getItemId())
            setDataOnFinish(null);
        else if (item.getItemId() == action_done.getItemId())
            setDataOnFinish(cropImageView.getCroppedBitmap());
        else if (item.getItemId() == action_ratio_fit_image.getItemId())
            cropImageView.setCropMode(CropImageView.CropMode.RATIO_FIT_IMAGE);
        else if (item.getItemId() == action_ratio_4_3.getItemId())
            cropImageView.setCropMode(CropImageView.CropMode.RATIO_4_3);
        else if (item.getItemId() == action_ratio_3_4.getItemId())
            cropImageView.setCropMode(CropImageView.CropMode.RATIO_3_4);
        else if (item.getItemId() == action_ratio_1_1.getItemId())
            cropImageView.setCropMode(CropImageView.CropMode.RATIO_1_1);
        else if (item.getItemId() == action_ratio_16_9.getItemId())
            cropImageView.setCropMode(CropImageView.CropMode.RATIO_16_9);
        else if (item.getItemId() == action_ratio_9_16.getItemId())
            cropImageView.setCropMode(CropImageView.CropMode.RATIO_9_16);
        else if (item.getItemId() == action_ratio_free.getItemId())
            cropImageView.setCropMode(CropImageView.CropMode.RATIO_FREE);
        else if (item.getItemId() == action_circle.getItemId())
            cropImageView.setCropMode(CropImageView.CropMode.CIRCLE);
        return super.onOptionsItemSelected(item);
    }

    private void setDataOnFinish(Bitmap originalBitmap) {

        if (originalBitmap != null) {
            Bitmap bitmap = getCompressedBitmap(originalBitmap, 1080f * 2, 720f * 2);
            Intent intent = new Intent();
            intent.putExtra(RETURN_IMAGE_PATH, saveImage(this, bitmap));
            setResult(RESULT_OK, intent);
        }
        onBackPressed();
    }

    public String saveImage(Context context, Bitmap bitmap) {
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        // path to /data/data/your app/app_data/imageDir
        File directory = cw.getDir("cacheImage", context.MODE_PRIVATE);
        // Create imageDir
        File myPath = new File(directory, fileNamePrefix + System.currentTimeMillis() + ".png");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return myPath.getAbsolutePath();
    }

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


}
