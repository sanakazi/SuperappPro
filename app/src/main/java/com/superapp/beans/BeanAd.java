package com.superapp.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by inte on 09-Jan-17.
 */

public class BeanAd implements Parcelable{

    private String startDate;
    private String image;
    private String endDate;
    private String type;
    private String script;
    private String showAd;


    public BeanAd() {
    }

    protected BeanAd(Parcel in) {
        startDate = in.readString();
        image = in.readString();
        endDate = in.readString();
        type = in.readString();
        script = in.readString();
        showAd = in.readString();
    }

    public static final Creator<BeanAd> CREATOR = new Creator<BeanAd>() {
        @Override
        public BeanAd createFromParcel(Parcel in) {
            return new BeanAd(in);
        }

        @Override
        public BeanAd[] newArray(int size) {
            return new BeanAd[size];
        }
    };

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getShowAd() {
        return showAd;
    }

    public void setShowAd(String showAd) {
        this.showAd = showAd;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(startDate);
        dest.writeString(image);
        dest.writeString(endDate);
        dest.writeString(type);
        dest.writeString(script);
        dest.writeString(showAd);
    }
}
