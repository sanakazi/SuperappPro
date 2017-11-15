package com.superapp;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.superapp.beans.ProjectBean;
import com.superapp.utils.Constant;
import com.superapp.utils.PrefSetup;

import java.io.File;
import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

public class ApplicationContext extends Application {

    private static ApplicationContext mInstance;
    public static final String TAG = ApplicationContext.class.getSimpleName();
    public static Integer count = 0;
    File ROOT_DIR;
    File CACHE_DIR;

    public ProjectBean project;
    private ArrayList<ProjectBean> projectList;

    public ArrayList<ProjectBean> getProjectList() {
        return projectList != null ? projectList : new ArrayList<ProjectBean>();
    }

    public void setProjectList(ArrayList<ProjectBean> projectList) {
        this.projectList = projectList;
    }
    //    public BeanTransaction transaction;

    private RequestQueue mRequestQueue;
    //    private ImageLoader mImageLoader;
    ImageLoader imageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Fabric.with(this, new Crashlytics());
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
        mInstance = this;
        ROOT_DIR = new File(Environment.getExternalStorageDirectory(), Constant.APP_NAME);
        CACHE_DIR = new File(ROOT_DIR, "Cache");
        PrefSetup.getInstance();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mInstance = null;
    }

    public static synchronized ApplicationContext getInstance() {
        return mInstance;
    }

    public File getCACHE_DIR() {
        return CACHE_DIR;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    private ImageLoader getImageLoader() {
        if (imageLoader == null) {
            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheOnDisc(true).cacheInMemory(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .showImageForEmptyUri(R.drawable.no_image)
                    .showImageOnFail(R.drawable.no_image)
                    .displayer(new FadeInBitmapDisplayer(300)).build();

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(ApplicationContext.this).defaultDisplayImageOptions(defaultOptions)
                    .memoryCache(new WeakMemoryCache())
                    .discCacheSize(100 * 1024 * 1024).build();

            ImageLoader.getInstance().init(config);
            imageLoader = ImageLoader.getInstance();
        }
        return imageLoader;
    }

    public void loadImage(String imageUrl, ImageView imageView, final ProgressBar progressBar, final int defaultImageResource) {
        if (imageView == null) {
            return;
        }
        if (imageUrl == null) {
            if (defaultImageResource != 0) {
                imageView.setImageResource(defaultImageResource);
            } else {
                imageView.setImageResource(R.drawable.no_image);
            }
            if (progressBar != null)
                progressBar.setVisibility(View.GONE);
            return;
        }
        ApplicationContext.getInstance().getImageLoader().displayImage(imageUrl, imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                if (progressBar != null)
                    progressBar.setVisibility(View.VISIBLE);
                if (view != null) {
                    view.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                if (progressBar != null)
                    progressBar.setVisibility(View.GONE);
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                    if (defaultImageResource != 0) {
                        ((ImageView) view).setImageResource(defaultImageResource);
                    }
                }
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                if (progressBar != null)
                    progressBar.setVisibility(View.GONE);
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                    ((ImageView) view).setImageBitmap(bitmap);
                    if (!TextUtils.isEmpty(s)) {
                        File file = ApplicationContext.getInstance().getImageLoader().getDiscCache().get(s);
                        ((ImageView) view).setTag(file);
                    }
                }
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                if (progressBar != null)
                    progressBar.setVisibility(View.GONE);
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                    if (defaultImageResource != 0) {
                        ((ImageView) view).setImageResource(defaultImageResource);
                    }
                }
            }
        });
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void catException(Exception e) {
        System.err.println("***************Error***************");
        e.printStackTrace();
        System.err.println("***************Error***************");
    }


}
