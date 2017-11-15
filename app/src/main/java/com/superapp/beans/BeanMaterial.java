package com.superapp.beans;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by inte on 8/30/2016.
 */
public class BeanMaterial implements Serializable,Parcelable {
    private long id;
    private String title;
    private String header;
    private JSONObject jsonData;


    public transient boolean isChecked = false;

    public BeanMaterial() {
    }



    public BeanMaterial(long materialId, String title) {
        this.id = materialId;
        this.title = title;
    }

    public BeanMaterial(String title) {
        this.title = title;
    }

    public BeanMaterial(long id, String title, String header) {
        this.id = id;
        this.title = title;
        this.header = header;
    }

//    public BeanMaterial(String header) {
//        this.header = header;
//    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return getTitle();
    }


    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.title);
        dest.writeString(this.header);
    }

    protected BeanMaterial(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.header = in.readString();
    }

    public static final Creator<BeanMaterial> CREATOR = new Creator<BeanMaterial>() {
        @Override
        public BeanMaterial createFromParcel(Parcel source) {
            return new BeanMaterial(source);
        }

        @Override
        public BeanMaterial[] newArray(int size) {
            return new BeanMaterial[size];
        }
    };

    public JSONObject getJsonData() {
        return jsonData;
    }

    public void setJsonData(JSONObject jsonData) {
        this.jsonData = jsonData;
    }
}
