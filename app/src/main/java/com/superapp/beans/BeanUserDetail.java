package com.superapp.beans;

import android.text.TextUtils;

import com.superapp.fragment.dashboard.add_forms.coworker_occupation.BeanOccupation;

/**
 * Created by ss on 30-Aug-16.
 */
public class BeanUserDetail {

    private long id;
    private String name;
    private String email;
    private String contactNo;
    private String splalgoval;
    private String loginType; // d -> Designer / c -> Client / e -> Employee/Co-worker
    private String isSuperSearch;

    // After edit profile
    private String designation;
    private String companyStrength;
    private String officeNumber;
    private String dob;
    private String companyAddress;
    private String resindentialAddress;
    private String uploadPhoto;
    private String loginCheck;

    // Designer
    private String userId;
    private String userName;
    private String companyName;
    private String logo;
    private String registrationNum;
    private String membershipId;
    private String membership;
    private long memberSubscriptionStartDate;
    private long memberSubscriptionExpDate;

    // Client
    private String gender;
    private String clientAddressType;
    private String address;
    private String alternetContact;
    private String alternateMobile;

    // Coworker
    private BeanOccupation occupation;
    private String nativeAddress;
    private String pannumber;
    private String cardUrl;
    private String photoUrl;
    private String reference;
    private String rating;

    // Super Search Coworker
    private String businessName;
    private String category;
    private String website;
    private String address1;
    private String address2;
    private String country;
    private String state;
    private String city;
    private String locality;
    private String pinCode;
    private String sfQuality;
    private String stiming;
    private String etiming;
    private String closedWeek;
    private String referredBy;
    private String superGallery;

    private String OTP;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getSplalgoval() {
        return splalgoval;
    }

    public void setSplalgoval(String splalgoval) {
        this.splalgoval = splalgoval;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }


    public String getIsSuperSearch() {
        return isSuperSearch;
    }

    public void setIsSuperSearch(String isSuperSearch) {
        this.isSuperSearch = isSuperSearch;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getRegistrationNum() {
        return registrationNum;
    }

    public void setRegistrationNum(String registrationNum) {
        this.registrationNum = registrationNum;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCompanyStrength() {
        return companyStrength;
    }

    public void setCompanyStrength(String companyStrength) {
        this.companyStrength = companyStrength;
    }

    public String getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getResindentialAddress() {
        return resindentialAddress;
    }

    public void setResindentialAddress(String resindentialAddress) {
        this.resindentialAddress = resindentialAddress;
    }

    public String getUploadPhoto() {
        return uploadPhoto;
    }

    public void setUploadPhoto(String uploadPhoto) {
        this.uploadPhoto = uploadPhoto;
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    public String getLoginCheck() {
        return loginCheck;
    }

    public void setLoginCheck(String loginCheck) {
        this.loginCheck = loginCheck;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    public long getMemberSubscriptionStartDate() {
        return memberSubscriptionStartDate;
    }

    public void setMemberSubscriptionStartDate(long memberSubscriptionStartDate) {
        this.memberSubscriptionStartDate = memberSubscriptionStartDate;
    }

    public long getMemberSubscriptionExpDate() {
        return memberSubscriptionExpDate;
    }

    public void setMemberSubscriptionExpDate(long memberSubscriptionExpDate) {
        this.memberSubscriptionExpDate = memberSubscriptionExpDate;
    }

// ToDo Client

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getClientAddressType() {
        return clientAddressType;
    }

    public void setClientAddressType(String clientAddressType) {
        this.clientAddressType = clientAddressType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAlternetContact() {
        return alternetContact;
    }

    public void setAlternetContact(String alternetContact) {
        this.alternetContact = alternetContact;
    }

    public String getAlternateMobile() {
        return alternateMobile;
    }

    public void setAlternateMobile(String alternateMobile) {
        this.alternateMobile = alternateMobile;
    }


    // ToDo Coworker

    public BeanOccupation getOccupation() {
        return occupation;
    }

    public void setOccupation(BeanOccupation occupation) {
        this.occupation = occupation;
    }

    public String getNativeAddress() {
        return nativeAddress;
    }

    public void setNativeAddress(String nativeAddress) {
        this.nativeAddress = nativeAddress;
    }

    public String getPannumber() {
        return pannumber;
    }

    public void setPannumber(String pannumber) {
        this.pannumber = pannumber;
    }

    public String getCardUrl() {
        return cardUrl;
    }

    public void setCardUrl(String cardUrl) {
        this.cardUrl = cardUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }


    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getRating() {
        return TextUtils.isEmpty(rating) ? "0" : rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }


    // TODO Coworker Super Search


    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getSfQuality() {
        return sfQuality;
    }

    public void setSfQuality(String sfQuality) {
        this.sfQuality = sfQuality;
    }

    public String getStiming() {
        return stiming;
    }

    public void setStiming(String stiming) {
        this.stiming = stiming;
    }

    public String getEtiming() {
        return etiming;
    }

    public void setEtiming(String etiming) {
        this.etiming = etiming;
    }

    public String getClosedWeek() {
        return closedWeek;
    }

    public void setClosedWeek(String closedWeek) {
        this.closedWeek = closedWeek;
    }

    public String getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(String referredBy) {
        this.referredBy = referredBy;
    }

    public String getSuperGallery() {
        return superGallery;
    }

    public void setSuperGallery(String superGallery) {
        this.superGallery = superGallery;
    }
}
