package com.superapp.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ss on 22-Sep-16.
 */
public class FileBean implements Parcelable {

    private long id;
    private String fileUrl;
    private String fileName;
    private String fileType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.fileUrl);
        dest.writeString(this.fileName);
        dest.writeString(this.fileType);
    }

    public FileBean() {
    }

    protected FileBean(Parcel in) {
        this.id = in.readLong();
        this.fileUrl = in.readString();
        this.fileName = in.readString();
        this.fileType = in.readString();
    }

    public static final Creator<FileBean> CREATOR = new Creator<FileBean>() {
        @Override
        public FileBean createFromParcel(Parcel source) {
            return new FileBean(source);
        }

        @Override
        public FileBean[] newArray(int size) {
            return new FileBean[size];
        }
    };
}
