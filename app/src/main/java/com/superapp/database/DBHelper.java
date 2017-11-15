package com.superapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import com.superapp.beans.NotificationBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Rakesh on 13-Jul-16.
 */
public class DBHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DBConstants.DB_NAME, null, 1);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String create : DBConstants.CREATE_TABLES) {
            db.execSQL(create);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String table : DBConstants.TABLES) {
            db.execSQL("DROP TABLE IF EXISTS " + table);
        }
        onCreate(db);
    }

    public boolean insertNotification(NotificationBean notificationBean) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(DBConstants.PROJECT_ID, notificationBean.getProject_id());
            cv.put(DBConstants.TYPE, notificationBean.getType());
            cv.put(DBConstants.SUBSCRIBER_ID, notificationBean.getSubscriber_id());
            cv.put(DBConstants.MESSAGE, notificationBean.getMessage());
            cv.put(DBConstants.ADDED_DATE, notificationBean.getAddeddate());
            cv.put(DBConstants.TO_TYPE, notificationBean.getTo_type());
            db.insert(DBConstants.NOTIFICATION_TABLE, null, cv);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public ArrayList<NotificationBean> getAllNotifications(final String proId) {
        ArrayList<NotificationBean> list = new ArrayList<NotificationBean>();
        Cursor c;
        if (!TextUtils.isEmpty(proId)) {
            String where = DBConstants.PROJECT_ID + "=?";
            String args[] = {proId};
            c = db.query(DBConstants.NOTIFICATION_TABLE, null, where, args, null, null, DBConstants.ID + " DESC");
        } else {
            c = db.query(DBConstants.NOTIFICATION_TABLE, null, null, null, null, null, DBConstants.ID + " DESC");
        }
        try {
            if (c != null && c.moveToFirst()) {
                do {
                    NotificationBean notificationBean = new NotificationBean();
                    notificationBean.setId(c.getInt(c.getColumnIndex(DBConstants.ID)));
                    notificationBean.setProject_id(Long.parseLong(c.getString(c.getColumnIndex(DBConstants.PROJECT_ID))));
                    notificationBean.setType(c.getString(c.getColumnIndex(DBConstants.TYPE)));
                    notificationBean.setSubscriber_id(Long.parseLong(c.getString(c.getColumnIndex(DBConstants.SUBSCRIBER_ID))));
                    notificationBean.setMessage(c.getString(c.getColumnIndex(DBConstants.MESSAGE)));
                    notificationBean.setAddeddate(Long.parseLong(c.getString(c.getColumnIndex(DBConstants.ADDED_DATE))));
                    notificationBean.setTo_type(c.getString(c.getColumnIndex(DBConstants.TO_TYPE)));
                    list.add(notificationBean);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
        }
        return list;
    }

    public int getNotificationCounter(String projectId) {
        int totalNotification = 0;
        Cursor c;
        if (!TextUtils.isEmpty(projectId)) {
            String args[] = {projectId};
            c = db.rawQuery("SELECT COUNT(" + DBConstants.ID + ") AS totalNotification FROM " + DBConstants.NOTIFICATION_TABLE + " WHERE " + DBConstants.PROJECT_ID + "=?", args);
        } else {
            c = db.rawQuery("SELECT COUNT(" + DBConstants.ID + ") AS totalNotification FROM " + DBConstants.NOTIFICATION_TABLE, null);
        }
        try {
            if (c != null && c.moveToFirst()) {
                totalNotification = c.getInt(c.getColumnIndex("totalNotification"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
        }

        return totalNotification;
    }

//
//    public NotificationBean fetchNotification(String notificationId) {
//        NotificationBean notificationBean = new NotificationBean();
//
//        String where = DBConstants.NOTIFICATION_ID + "=?";
//        String args[] = {notificationId};
//        try {
//            Cursor c = db.query(DBConstants.NOTIFICATION_TABLE, null, where, args, null, null, null);
//            if (c != null && c.moveToFirst()) {
//                notificationBean.setId(c.getInt(c.getColumnIndex(DBConstants.ID)));
//                notificationBean.setNotificationId(c.getString(c.getColumnIndex(DBConstants.NOTIFICATION_ID)));
//                notificationBean.setMessage(c.getString(c.getColumnIndex(DBConstants.MESSAGE)));
//                notificationBean.setOrderId(c.getString(c.getColumnIndex(DBConstants.ORDER_ID)));
//                notificationBean.setType(c.getString(c.getColumnIndex(DBConstants.TYPE)));
//                return notificationBean;
//            } else {
//                return null;
//            }
//        } catch (Exception e) {
//            ApplicationContext.getInstance().logExceptionRecord(e, getClass());
//            return null;
//        }
//    }

    public boolean deleteNotification(long notificationId) {
        String where = DBConstants.ID + "=?";
        String args[] = {notificationId + ""};
        return (db.delete(DBConstants.NOTIFICATION_TABLE, where, args) == 0) ? false : true;
    }

    public void clearDataBaseTables() {
        for (String table : DBConstants.TABLES) {
            System.out.println("Delete : " + table);
            db.execSQL("delete from " + table);
        }
    }

    public void dropDataBase() {
        for (String table : DBConstants.TABLES) {
            System.out.println("Delete : " + table);
            db.execSQL("DROP TABLE IF EXISTS " + table);
        }
    }

    public static void exportDB(Context mContext) {
        File sd = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SuperApp");
        if (!sd.exists())
            sd.mkdirs();
        String currentDBPath = "";
        if (android.os.Build.VERSION.SDK_INT >= 17)
            currentDBPath = mContext.getApplicationInfo().dataDir + "/databases/" + DBConstants.DB_NAME;
        else
            currentDBPath = "/data/" + mContext.getPackageName() + "/databases/" + DBConstants.DB_NAME;
        File currentDB = new File(currentDBPath);
        InputStream in;
        OutputStream out;
        try {
            String copyLocationPath = sd.getPath() + File.separator + DBConstants.DB_NAME;
            File copyDB = new File(copyLocationPath);
            if (!copyDB.exists())
                copyDB.createNewFile();
            in = new FileInputStream(currentDB);
            out = new FileOutputStream(copyDB);
            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            Toast.makeText(mContext, "DB Exported", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}
