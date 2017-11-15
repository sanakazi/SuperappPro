package com.superapp.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;

import com.superapp.ApplicationContext;
import com.superapp.activity.base.BaseAppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ss on 28-Dec-16.
 */
public class SaveCameraImage extends AsyncTask<Void, Void, Uri> {
    private Context context;
    private Intent data;
    private IImageSaved iImageSaved;

    public static void saveImage(final Context context, final Intent data, IImageSaved iImageSaved) {
        if (data != null) {
            SaveCameraImage saveCameraImage = new SaveCameraImage();
            saveCameraImage.iImageSaved = iImageSaved;
            saveCameraImage.context = context;
            saveCameraImage.data = data;
            saveCameraImage.execute();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ((BaseAppCompatActivity) context).showProgressingView();
    }

    @Override
    protected Uri doInBackground(Void... voids) {
        if (data.getData() instanceof Uri) {
            return data.getData();
        } else {
            File imgFile = getImageFile();
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            if (thumbnail != null) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                FileOutputStream fo;
                try {
                    fo = new FileOutputStream(imgFile);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return Uri.fromFile(imgFile);
        }
    }

    @Override
    protected void onPostExecute(Uri uri) {
        super.onPostExecute(uri);
        ((BaseAppCompatActivity) context).hideProgressingView();
        iImageSaved.onImageSaved(uri);
    }

    public interface IImageSaved {
        public void onImageSaved(Uri uri);
    }

    private File getImageFile() {
        File file = null;
        try {
            if (!ApplicationContext.getInstance().getCACHE_DIR().exists()) {
                ApplicationContext.getInstance().getCACHE_DIR().mkdirs();
            }
            file = new File(ApplicationContext.getInstance().getCACHE_DIR(), System.currentTimeMillis() + "-image.jpg");
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
