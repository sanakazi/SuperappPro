package com.superapp.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by APPNWEB on 03-09-2016.
 */
public class CommunicationBean implements Parcelable {

    private long id;
    private String type;
    private String description;
    private String clientReply;
    private String status;

    // For only approvals
    private String subCategory;
    private ArrayList<FileBean> documents;
    private ArrayList<FileBean> images;
    private String category;
    private long ApprovedDocumentid;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClientReply() {
        return clientReply;
    }

    public void setClientReply(String clientReply) {
        this.clientReply = clientReply;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public ArrayList<FileBean> getDocuments() {
        return documents != null ? documents : new ArrayList<FileBean>();
    }

    public void setDocuments(ArrayList<FileBean> documents) {
        this.documents = documents;
    }

    public ArrayList<FileBean> getImages() {
        return images != null ? images : new ArrayList<FileBean>();
    }

    public void setImages(ArrayList<FileBean> images) {
        this.images = images;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getApprovedDocumentid() {
        return ApprovedDocumentid;
    }

    public void setApprovedDocumentid(long approvedDocumentid) {
        ApprovedDocumentid = approvedDocumentid;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.type);
        dest.writeString(this.description);
        dest.writeString(this.clientReply);
        dest.writeString(this.status);
        dest.writeString(this.subCategory);
        dest.writeTypedList(this.documents);
        dest.writeTypedList(this.images);
        dest.writeString(this.category);
        dest.writeLong(this.ApprovedDocumentid);
    }

    public CommunicationBean() {
    }

    protected CommunicationBean(Parcel in) {
        this.id = in.readLong();
        this.type = in.readString();
        this.description = in.readString();
        this.clientReply = in.readString();
        this.status = in.readString();
        this.subCategory = in.readString();
        this.documents = in.createTypedArrayList(FileBean.CREATOR);
        this.images = in.createTypedArrayList(FileBean.CREATOR);
        this.category = in.readString();
        this.ApprovedDocumentid = in.readLong();
    }

    public static final Creator<CommunicationBean> CREATOR = new Creator<CommunicationBean>() {
        @Override
        public CommunicationBean createFromParcel(Parcel source) {
            return new CommunicationBean(source);
        }

        @Override
        public CommunicationBean[] newArray(int size) {
            return new CommunicationBean[size];
        }
    };
}

