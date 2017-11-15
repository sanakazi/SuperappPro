package com.superapp.beans;

/**
 * Created by inte on 8/30/2016.
 */
public class BeanAddClient {
    private long userId;
    private String loginType;
    private long clientId;
    private String fullName;
    private String gender;
    private String email;
    private String mobile;
    private String alternateContact;
    private String alternateContactMobile;
    private String clientAddressType;

    private BeanAddress address;

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAlternateContact() {
        return alternateContact;
    }

    public void setAlternateContact(String alternateContact) {
        this.alternateContact = alternateContact;
    }

    public String getAlternateContactMobile() {
        return alternateContactMobile;
    }

    public void setAlternateContactMobile(String alternateContactMobile) {
        this.alternateContactMobile = alternateContactMobile;
    }

    public BeanAddress getAddress() {
        return address;
    }

    public void setAddress(BeanAddress address) {
        this.address = address;
    }

    public String getClientAddressType() {
        return clientAddressType;
    }

    public void setClientAddressType(String clientAddressType) {
        this.clientAddressType = clientAddressType;
    }
}
