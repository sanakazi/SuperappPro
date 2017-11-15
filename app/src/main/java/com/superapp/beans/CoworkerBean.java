package com.superapp.beans;

import java.io.Serializable;

public class CoworkerBean implements Serializable {

    private long id;
    private long coworkerId;
    private long occupationId;
    private String occupation;
    private String name;
    private String mobile;
    private String email;
    private String address;
    private String photoId;
    private String rating;
    private String panNumber;
    private String idCard;
    private String nativeAddress;
    private String referredBy;

    public long getId() {
        return id;
    }

    public void setId(long coworkerId) {
        this.id = coworkerId;
    }

    public long getCoworkerId() {
        return coworkerId;
    }

    public void setCoworkerId(long coworkerId) {
        this.coworkerId = coworkerId;
    }

    public long getOccupationId() {
        return occupationId;
    }

    public void setOccupationId(long occupationId) {
        this.occupationId = occupationId;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getNativeAddress() {
        return nativeAddress;
    }

    public void setNativeAddress(String nativeAddress) {
        this.nativeAddress = nativeAddress;
    }

    public String getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(String referredBy) {
        this.referredBy = referredBy;
    }
}