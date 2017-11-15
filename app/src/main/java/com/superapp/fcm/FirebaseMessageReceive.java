package com.superapp.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.activity.ActivityAdminNotification;
import com.superapp.activity.ActivityMain;
import com.superapp.activity.base.ErrorType;
import com.superapp.beans.NotificationBean;
import com.superapp.database.DBHelper;
import com.superapp.utils.PrefSetup;
import com.superapp.webservice.Interactor;
import com.superapp.webservice.InteractorImpl;
import com.superapp.webservice.OnResponseListener;
import com.superapp.webservice.ResponsePacket;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class FirebaseMessageReceive extends FirebaseMessagingService {
    private static final String TAG = FirebaseMessageReceive.class.getSimpleName();
    private NotificationUtils notificationUtils;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    Bitmap bitmap;
    public static int NOTIFICATION_ID;
    String imageUrl;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        if (data != null) {
            NotificationBean notification = new NotificationBean();
            notification.setTo_type(data.get("to_type"));
            notification.setMessage(data.get("message"));

            notification.setIs_read(data.get("is_read"));
            notification.setType(data.get("type"));
            notification.setTitle(data.get("title"));
            notification.setLogin_type(data.get("login_type"));
            notification.setDescription(data.get("description"));
            if (data.get("id") != null)
                notification.setId(Long.parseLong(data.get("id")));
            if (data.get("addeddate") != null)
                notification.setAddeddate(Long.parseLong(data.get("addeddate")));
            if (data.get("project_id") != null)
                notification.setProject_id(Long.parseLong(data.get("project_id")));
            if (data.get("subscriber_id") != null)
                notification.setSubscriber_id(Long.parseLong(data.get("subscriber_id")));

            handleDataMessage(notification);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void handleDataMessage(NotificationBean notificationBean) {
        try {
            if (notificationBean.getLogin_type().equals(PrefSetup.getInstance().getUserLoginType())) {
                if (!notificationBean.getType().equalsIgnoreCase("Admin")) {
                    ApplicationContext.count = ApplicationContext.count + 1;
                    handleNotification(notificationBean, notificationBean.getMessage(), "Superapp Pro", "R.drawable.ic_launcher");
                    getAllNotification();
                } else {
                    if (NotificationUtils.isAppIsInBackground(getApplication())) {
                        handleNotification(notificationBean, notificationBean.getDescription(), "Superapp Pro", "R.drawable.ic_launcher");
                    } else {
                        getAllAdminNotification();
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private void handleNotification(NotificationBean notificationBean, String message, String title, String imageUrl) {
        // if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
        try {
            mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationBean.getType().equalsIgnoreCase("Admin")) {
                Intent intent = new Intent(this, ActivityAdminNotification.class);
                NOTIFICATION_ID = 1;

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                builder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentText(message)
                        .setContentTitle(title)
                        .setAutoCancel(true);

                builder.setContentIntent(contentIntent);

            } else {
                Intent intent = new Intent(this, ActivityMain.class);
                NOTIFICATION_ID = 1;

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                builder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentText(message)
                        .setContentTitle(title)
                        .setAutoCancel(true);

                builder.setContentIntent(contentIntent);
            }

            builder.setDefaults(NotificationCompat.DEFAULT_ALL);
            mNotificationManager.notify(NOTIFICATION_ID, builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
        pushNotification.putExtra("isNotification", true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.playNotificationSound();
        // } else {
        // If the app is in background, firebase itself handles the notification
        //  }
    }

    /**
     * Showing notification with text only
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showNotificationMessage(Context context, String message, String title, String addeddate, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        notification.flags |=
//        Log.d("title=", message);
        notificationUtils.showNotificationMessage("SUPER APP", message, addeddate, intent);
    }

    /**
     * Showing notification with text and image
     */

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    DBHelper helper;

    private void getAllAdminNotification() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
            jsonObject.put("deviceType", "ANDROID");
        } catch (Exception e) {
            e.printStackTrace();
        }
        new InteractorImpl(getBaseContext(), new OnResponseListener() {
            @Override
            public void onSuccess(int requestCode, ResponsePacket responsePacket) {
                if (Interactor.RequestCode_GetAllAdminNotification == requestCode) {
                    if (responsePacket.getErrorCode() == 0) {
                        Intent intent = new Intent(getApplicationContext(), ActivityAdminNotification.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (responsePacket.getNotificationInfo() != null) {

                            intent.putExtra("title", responsePacket.getNotificationInfo().getTitle());
                            intent.putExtra("description", responsePacket.getNotificationInfo().getDescription());
                            intent.putExtra("image", responsePacket.getNotificationInfo().getImage());
                            intent.putExtra("link", responsePacket.getNotificationInfo().getLink());
                        }
                        startActivity(intent);
                    }

                    if (responsePacket.getErrorCode() == 1) {

                    }
                }
            }

            @Override
            public void onError(int requestCode, ErrorType errorType) {

            }
        }, Interactor.RequestCode_GetAllAdminNotification, Interactor.Tag_GetAllAdminNotification)
                .makeJsonPostRequest(Interactor.Method_GetAllAdminNotification, jsonObject, false);
    }

    private void getAllNotification() {
//        setAllNotifications(helper.getAllNotifications(isNotification ? null : ApplicationContext.getInstance().project.getProjectId() + ""));

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", PrefSetup.getInstance().getUserId());
            jsonObject.put("loginType", PrefSetup.getInstance().getUserLoginType());
            jsonObject.put("deviceType", "ANDROID");
            String toType = "";
            if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("d")) {
                toType = "Designer";
            } else if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("c")) {
                toType = "Client";
            } else if (PrefSetup.getInstance().getUserLoginType().equalsIgnoreCase("w")) {
                toType = "Co-worker";
            }
            jsonObject.put("toType", toType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new InteractorImpl(getBaseContext(), new OnResponseListener() {
            @Override
            public void onSuccess(int requestCode, ResponsePacket responsePacket) {
                if (Interactor.RequestCode_GetAllNotification == requestCode) {
                    responsePacket.getNotificationList();
                    for (NotificationBean notificationBean : responsePacket.getNotificationList()) {
                        new DBHelper(getApplicationContext()).insertNotification(notificationBean);

                    }
                }
            }

            @Override
            public void onError(int requestCode, ErrorType errorType) {

            }
        }, Interactor.RequestCode_GetAllNotification, Interactor.Tag_GetAllNotification)
                .makeJsonPostRequest(Interactor.Method_GetAllNotification, jsonObject, false);
    }
}
