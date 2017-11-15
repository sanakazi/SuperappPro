package com.superapp.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by APPNWEB on 26-08-2016.
 */
public class SuperSearchBean implements Parcelable {

    private long coworkerId;
    private String BusinessName;
    private String ownerName;
    private String avgRating;
    private String coworkerRating;
    private String address;
    private String mobileNumber;
    private String telephoneNumber;
    private String mailAddress;
    private String webUrl;
    private ArrayList<String> photoUrl;

    private String occupation;
    private String catName;
    private String specialFeature;
    private String startTiming;
    private String endTiming;
    private String preferredLocation;
    private String closeWeek;
    private String locality;


    protected SuperSearchBean(Parcel in) {
        coworkerId = in.readLong();
        BusinessName = in.readString();
        ownerName = in.readString();
        avgRating = in.readString();
        coworkerRating = in.readString();
        address = in.readString();
        mobileNumber = in.readString();
        telephoneNumber = in.readString();
        mailAddress = in.readString();
        webUrl = in.readString();
        photoUrl = in.createStringArrayList();
        occupation = in.readString();
        catName = in.readString();
        specialFeature = in.readString();
        startTiming = in.readString();
        endTiming = in.readString();
        preferredLocation = in.readString();
        closeWeek = in.readString();
        locality = in.readString();
    }

    public static final Creator<SuperSearchBean> CREATOR = new Creator<SuperSearchBean>() {
        @Override
        public SuperSearchBean createFromParcel(Parcel in) {
            return new SuperSearchBean(in);
        }

        @Override
        public SuperSearchBean[] newArray(int size) {
            return new SuperSearchBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(coworkerId);
        dest.writeString(BusinessName);
        dest.writeString(ownerName);
        dest.writeString(avgRating);
        dest.writeString(coworkerRating);
        dest.writeString(address);
        dest.writeString(mobileNumber);
        dest.writeString(telephoneNumber);
        dest.writeString(mailAddress);
        dest.writeString(webUrl);
        dest.writeStringList(photoUrl);
        dest.writeString(occupation);
        dest.writeString(catName);
        dest.writeString(specialFeature);
        dest.writeString(startTiming);
        dest.writeString(endTiming);
        dest.writeString(preferredLocation);
        dest.writeString(closeWeek);
        dest.writeString(locality);
    }

    public long getCoworkerId() {
        return coworkerId;
    }

    public void setCoworkerId(long coworkerId) {
        this.coworkerId = coworkerId;
    }

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(String avgRating) {
        this.avgRating = avgRating;
    }

    public String getCoworkerRating() {
        return coworkerRating;
    }

    public void setCoworkerRating(String coworkerRating) {
        this.coworkerRating = coworkerRating;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public ArrayList<String> getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(ArrayList<String> photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getSpecialFeature() {
        return specialFeature;
    }

    public void setSpecialFeature(String specialFeature) {
        this.specialFeature = specialFeature;
    }

    public String getStartTiming() {
        return startTiming;
    }

    public void setStartTiming(String startTiming) {
        this.startTiming = startTiming;
    }

    public String getEndTiming() {
        return endTiming;
    }

    public void setEndTiming(String endTiming) {
        this.endTiming = endTiming;
    }

    public String getPreferredLocation() {
        return preferredLocation;
    }

    public void setPreferredLocation(String preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    public String getCloseWeek() {
        return closeWeek;
    }

    public void setCloseWeek(String closeWeek) {
        this.closeWeek = closeWeek;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }
}
