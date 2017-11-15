//package com.superapp.service;
//
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.content.pm.ResolveInfo;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.PowerManager;
//import android.os.Vibrator;
//import android.support.v4.content.LocalBroadcastManager;
//import android.support.v7.app.NotificationCompat;
//
//import com.superapp.R;
//import com.superapp.activity.base.ErrorType;
//import com.superapp.activity.ActivityMain;
//import com.superapp.beans.NotificationBean;
//import com.superapp.database.DBHelper;
//import com.superapp.utils.Constant;
//import com.superapp.utils.PrefSetup;
//import com.superapp.utils.Utilities;
//import com.superapp.webservice.Interactor;
//import com.superapp.webservice.InteractorImpl;
//import com.superapp.webservice.OnResponseListener;
//import com.superapp.webservice.ResponsePacket;
//
//import org.json.JSONObject;
//
//import java.util.List;
//
//public class ServiceNotification extends Service implements OnResponseListener {
//
//    DBHelper helper;
//
//    @Override
//    public IBinder onBind(Intent arg0) {
//        return null;
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        // Let it continue running until it is stopped.
////        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
//        helper = new DBHelper(this);
//        mHandlerTask.run();
//        return START_STICKY;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mHandler.removeCallbacks(mHandlerTask);
////        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
//    }
//
//    private boolean isCallingService = false;
//    private final static long INTERVAL = 60000; //20 sec
//    Handler mHandler = new Handler();
//    Runnable mHandlerTask = new Runnable() {
//        @Override
//        public void run() {
//            if (!isCallingService)
//                getNotificationList();
//            mHandler.postDelayed(mHandlerTask, INTERVAL);
//        }
//    };
//
//    private void getNotificationList() {
//        if (Utilities.getInstance().isOnline(this)) {
//            long userId = PrefSetup.getInstance().getUserId();
//            String loginType = PrefSetup.getInstance().getUserLoginType();
//            if (userId > 0) {
////                sendBroadCast();
//                JSONObject jsonObject = new JSONObject();
//                try {
//                    jsonObject.put("userId", userId);
//                    jsonObject.put("loginType", loginType);
//                    String toType = "";
//                    if (loginType.equalsIgnoreCase("d")) {
//                        toType = "Designer";
//                    } else if (loginType.equalsIgnoreCase("c")) {
//                        toType = "Client";
//                    } else if (loginType.equalsIgnoreCase("w")) {
//                        toType = "Co-worker";
//                    }
//                    jsonObject.put("toType", toType);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                isCallingService = true;
//                new InteractorImpl(this, this, Interactor.RequestCode_GetAllNotification, Interactor.Tag_GetAllNotification)
//                        .makeJsonPostRequest(Interactor.Method_GetAllNotification, jsonObject, false);
//            } else {
//                mHandler.removeCallbacks(mHandlerTask);
//                stopSelf();
//            }
//        }
//    }
//
//    @Override
//    public void onSuccess(int requestCode, ResponsePacket responsePacket) {
//        isCallingService = false;
//        if (Interactor.RequestCode_GetAllNotification == requestCode) {
//            for (NotificationBean notification : responsePacket.getNotificationList()) {
//                if (helper.insertNotification(notification)) {
//                    if (Utilities.getInstance().isAppOnForeground(this))/* || Utilities.getInstance().isApplicationSentToBackground(this)) */ {
//                        sendBroadCast();
//                    } else {
////                        generateAlertNotification(notification.getMessage());
//                    }
//                    generateAlertNotification(notification.getMessage());
//                    playDefaultNotificationSound();
//                }
////                    if (Utilities.getInstance().isAppOnForeground(this))/* || Utilities.getInstance().isApplicationSentToBackground(this)) */ {
////                        sendBroadCast();
////                    } else {
////                        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
////                        boolean screenLocked = km.inKeyguardRestrictedInputMode();
////                        if (screenLocked) {
////                            generateAlertNotification(notification.getMessage());
////                        }
////                        openDialog(notification.getMessage());
////                    }
////                    playDefaultNotificationSound();
////                }
//            }
//        } else if (responsePacket.getErrorCode() == 410) {
//            PrefSetup.getInstance().clearPrefSetup();
//            new DBHelper(this).clearDataBaseTables();
//        }
//        setBadge(helper.getNotificationCounter(null));
//    }
//
//    @Override
//    public void onError(int requestCode, ErrorType errorType) {
//        isCallingService = false;
////        setBadge(helper.fetchAllNotifications().size());
//    }
//
//    private void sendBroadCast() {
//        Intent RTRetur = new Intent(Constant.RECEIVE_Notification);
////        RTRetur.putExtra("message", message);
////        RTRetur.putExtra("userid", userid);
////        RTRetur.putExtra("messageId", messageId);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(RTRetur);
//    }
//
//    private void generateAlertNotification(String message) {
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        Intent intent = new Intent(this, ActivityMain.class);
//        intent.putExtra("isNotification", true);
//        int iUniqueId = 1;
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, iUniqueId, intent, 0);
//        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        Notification notification = builder.setContentIntent(pendingIntent)
//                .setSmallIcon(R.drawable.sicon)
//                .setLargeIcon(icon)
//                .setTicker("Notification!")
//                .setWhen(System.currentTimeMillis())
//                .setContentTitle(getString(R.string.app_name))
//                .setContentText(message)
//                .setDefaults(Notification.DEFAULT_SOUND)
//                .setAutoCancel(true)
//                .build();
//
//        notificationManager.notify(iUniqueId, notification);
//    }
//
////    private void openDialog(String message) {
////        Intent dialog = new Intent(this, ActivityDialog.class);
////        dialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        dialog.putExtra("message", message);
////        this.startActivity(dialog);
////    }
//
//    private void playDefaultNotificationSound() {
//        try {
//            Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//            vib.vibrate(500);
////            AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
////            manager.setStreamVolume(AudioManager.STREAM_MUSIC, 10, 0);
////            Uri notification = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/ring");
////            MediaPlayer player = MediaPlayer.create(getApplicationContext(), notification);
////            player.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        gotoSplashLight();
//    }
//
//    private void gotoSplashLight() {
//        try {
//            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//            boolean isScreenOn = pm.isScreenOn();
//            if (isScreenOn == false) {
//                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyLock");
//                wl.acquire(10000);
//                PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyCpuLock");
//                wl_cpu.acquire(10000);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void setBadge(int count) {
//        try {
//            String launcherClassName = getLauncherClassName(this);
//            if (launcherClassName == null) {
//                return;
//            }
//            Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
//            intent.putExtra("badge_count", count);
//            intent.putExtra("badge_count_package_name", getPackageName());
//            intent.putExtra("badge_count_class_name", launcherClassName);
//            sendBroadcast(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private String getLauncherClassName(Context context) {
//        PackageManager pm = context.getPackageManager();
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
//        for (ResolveInfo resolveInfo : resolveInfos) {
//            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
//            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
//                String className = resolveInfo.activityInfo.name;
//                return className;
//            }
//        }
//        return null;
//    }
//}
