package com.superapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.superapp.ApplicationContext;
import com.superapp.beans.BeanUserDetail;


public class PrefSetup {
    private static PrefSetup instance;

    private PrefSetup(Context context) {
        sp = context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE);
    }

    public static synchronized PrefSetup getInstance() {
        if (instance != null)
            return instance;

        instance = new PrefSetup(ApplicationContext.getInstance());
        return instance;
    }

    public SharedPreferences sp;

    // Header Key
    private String HEADER_KEY = "splalgoval";

    private String REGISTER_TYPE = "registerType";
    private String SUPER_SERACH_CHECK = "isSuperSearch";

    private String USER_ID = "id";
    private String USER_NAME = "name";
    private String USER_COMPANY_NAME = "companyName";
    private String USER_COMPANY_LOGO = "companyLogo";
    private String USER_EMAIL = "email";
    private String USER_CONTACT_NO = "contactNo";
    private String USER_LOGIN_TYPE = "loginType";
    private String USER_OTP = "OTP";

    private String USER_DESIGNATION = "designation";
    private String USER_COMPANY_STRENGTH = "companyStrength";
    private String USER_OFFICE_NUMBER = "officeNumber";
    private String USER_DOB = "dob";
    private String USER_COMPANY_ADDRESS = "companyAddress";
    private String USER_RESINDENTI_ALADDRESS = "resindentialAddress";
    private String USER_UPLOAD_PHOTO = "uploadPhoto";
    private String USER_MEMBERSHIPID = "membershipId";
    private String USER_MEMBERSHIP = "membership";
    private String USER_MEMBER_SUBSCRIPTION_START_DATE = "memberSubscriptionStartDate";
    private String USER_MEMBER_SUBSCRIPTION_EXP_DATE = "memberSubscriptionExpDate";

    private String LOGIN_CHECK = "loginCheck";

    private String Remember_Email = "RememberEmail";
    private String Remember_Password = "RememberPassword";
    private String Remember_Me_Login = "isRememberMe";
    private String Forgot_password = "ForgotPassword";

    private String SCHEDULE_END_DATE_TIME = "endDateTime";
    private String SCHEDULE_DESCRIPTION = "description";


    // ToDo Client
    private String CLIENT_ID = "clientId";
    private String CLIENT_GENDER = "gender";
    private String CLIENT_ADDRESSTYPE = "clientAddressType";
    private String CLIENT_ADDRESS = "address";
    private String CLIENT_ALTERNETCONTACT = "alternetContact";
    private String CLIENT_ALTERNATEMOBILE = "alternateMobile";


    // ToDo Coworker
    private String COWORKER_OCCUPATIONID = "occupationId";
    private String COWORKER_OCCUPATIONTITLE = "occupationTitle";
    private String COWORKER_NATIVEADDRESS = "nativeAddress";
    private String COWORKER_PANNUMBER = "pannumber";
    private String COWORKER_CARDURL = "cardUrl";
    private String COWORKER_PHOTOURL = "photoUrl";
    private String COWORKER_REFERREDBY = "reference";
    private String COWORKER_RATING = "rating";
    private String fcmId = "FCM_ID";
    private String TEMP_IMAGE = "temp_image";


    public String getREGISTER_TYPE() {
        return REGISTER_TYPE;
    }

    public void setREGISTER_TYPE(String REGISTER_TYPE) {
        this.REGISTER_TYPE = REGISTER_TYPE;
    }

    public String getSUPER_SERACH_CHECK() {
        return sp.getString(SUPER_SERACH_CHECK, "");
    }

    public void setSUPER_SERACH_CHECK(String SUPER_SERACH_CHECK) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SUPER_SERACH_CHECK, SUPER_SERACH_CHECK);
        editor.commit();
    }

    public long getUserId() {
        return sp.getLong(USER_ID, 0L);
    }

    public void setUserId(long userId) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(USER_ID, userId);
        editor.commit();
    }

    public String getUserName() {
        return sp.getString(USER_NAME, "");
    }

    public void setUserName(String userName) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_NAME, userName);
        editor.commit();
    }

    public String getUserCompanyName() {
        return sp.getString(USER_COMPANY_NAME, "");
    }

    public void setUserCompanyName(String userCompanyName) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_COMPANY_NAME, userCompanyName);
        editor.commit();
    }

    public String getUserCompanyLogo() {
        return sp.getString(USER_COMPANY_LOGO, "");
    }

    public void setUserCompanyLogo(String userCompanyLogo) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_COMPANY_LOGO, userCompanyLogo);
        editor.commit();
    }

    public String getUserEmail() {
        return sp.getString(USER_EMAIL, "");
    }

    public void setUserEmail(String userEmail) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_EMAIL, userEmail);
        editor.commit();
    }

    public String getUserContactNo() {
        return sp.getString(USER_CONTACT_NO, "");
    }

    public void setUserContactNo(String userContactNumber) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_CONTACT_NO, userContactNumber);
        editor.commit();
    }

    public String getUserLoginType() {
        return sp.getString(USER_LOGIN_TYPE, "");
    }

    public void setUserLoginType(String loginType) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_LOGIN_TYPE, loginType);
        editor.commit();
    }


    public String getUSER_MEMBERSHIPID() {
        return sp.getString(USER_MEMBERSHIPID, "1");
    }

    public void setUSER_MEMBERSHIPID(String USER_MEMBERSHIPID) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_MEMBERSHIPID, USER_MEMBERSHIPID);
        editor.commit();
    }

    public String getUSER_MEMBERSHIP() {
        return sp.getString(USER_MEMBERSHIP, "");
    }

    public void setUSER_MEMBERSHIP(String USER_MEMBERSHIP) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_MEMBERSHIP, USER_MEMBERSHIP);
        editor.commit();
    }


    public long getUSER_MEMBER_SUBSCRIPTION_START_DATE() {
        return sp.getLong(USER_MEMBER_SUBSCRIPTION_START_DATE, 0L);
    }

    public void setUSER_MEMBER_SUBSCRIPTION_START_DATE(long userMemberSubscriptionStartDate) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(USER_MEMBER_SUBSCRIPTION_START_DATE, userMemberSubscriptionStartDate);
        editor.commit();
    }

    public long getUSER_MEMBER_SUBSCRIPTION_EXP_DATE() {
        return sp.getLong(USER_MEMBER_SUBSCRIPTION_EXP_DATE, 0L);
    }

    public void setUSER_MEMBER_SUBSCRIPTION_EXP_DATE(long userMemberSubscriptionExpDate) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(USER_MEMBER_SUBSCRIPTION_EXP_DATE, userMemberSubscriptionExpDate);
        editor.commit();
    }


    public void setUserDetail(BeanUserDetail userDetail) {
        if (userDetail != null) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong(USER_ID, userDetail.getId());
            editor.putString(USER_NAME, userDetail.getName());
            editor.putString(USER_COMPANY_NAME, userDetail.getCompanyName());
            editor.putString(USER_COMPANY_LOGO, userDetail.getLogo());
            editor.putString(USER_EMAIL, userDetail.getEmail());
            editor.putString(USER_CONTACT_NO, userDetail.getContactNo());
            if (!TextUtils.isEmpty(userDetail.getSplalgoval())) {
                editor.putString(HEADER_KEY, userDetail.getSplalgoval());
            }
            if (!TextUtils.isEmpty(userDetail.getLoginType())) {
                editor.putString(USER_LOGIN_TYPE, userDetail.getLoginType());
            }

            editor.putString(SUPER_SERACH_CHECK, userDetail.getIsSuperSearch());

            editor.putString(USER_DESIGNATION, userDetail.getDesignation());
            editor.putString(USER_COMPANY_STRENGTH, userDetail.getCompanyStrength());
            editor.putString(USER_OFFICE_NUMBER, userDetail.getOfficeNumber());
            editor.putString(USER_DOB, userDetail.getDob());
            editor.putString(USER_COMPANY_ADDRESS, userDetail.getCompanyAddress());
            editor.putString(USER_RESINDENTI_ALADDRESS, userDetail.getResindentialAddress());
            editor.putString(USER_UPLOAD_PHOTO, userDetail.getUploadPhoto());
            editor.putString(USER_MEMBERSHIPID, userDetail.getMembershipId());
            editor.putString(USER_MEMBERSHIP, userDetail.getMembership());
            editor.putLong(USER_MEMBER_SUBSCRIPTION_START_DATE, userDetail.getMemberSubscriptionStartDate());
            editor.putLong(USER_MEMBER_SUBSCRIPTION_EXP_DATE, userDetail.getMemberSubscriptionExpDate());

            editor.putString(LOGIN_CHECK, userDetail.getLoginCheck());

            editor.putString(CLIENT_GENDER, userDetail.getGender());
            editor.putString(CLIENT_ADDRESSTYPE, userDetail.getClientAddressType());
            editor.putString(CLIENT_ADDRESS, userDetail.getAddress());
            editor.putString(CLIENT_ALTERNETCONTACT, userDetail.getAlternetContact());
            editor.putString(CLIENT_ALTERNATEMOBILE, userDetail.getAlternateMobile());

            if (userDetail.getOccupation() != null) {
                editor.putLong(COWORKER_OCCUPATIONID, userDetail.getOccupation().getId());
                editor.putString(COWORKER_OCCUPATIONTITLE, userDetail.getOccupation().getTitle());
            }
            editor.putString(COWORKER_NATIVEADDRESS, userDetail.getNativeAddress());
            editor.putString(COWORKER_PANNUMBER, userDetail.getPannumber());
            editor.putString(COWORKER_CARDURL, userDetail.getCardUrl());
            editor.putString(COWORKER_PHOTOURL, userDetail.getPhotoUrl());
            editor.putString(COWORKER_REFERREDBY, userDetail.getReference());
            editor.putString(COWORKER_RATING, userDetail.getRating());

            editor.commit();
        }
    }

    public String getUserDesignation() {
        return sp.getString(USER_DESIGNATION, "Designer");
    }

    public void setLoginCheck(String loginCheck) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(LOGIN_CHECK, loginCheck);
        editor.commit();
    }

    public String getLoginCheck() {
        return sp.getString(LOGIN_CHECK, "f");
    }

    public String getUserCompanyStrength() {
        return sp.getString(USER_COMPANY_STRENGTH, "");
    }

    public String getUserOfficeNumber() {
        return sp.getString(USER_OFFICE_NUMBER, "");
    }

    public String getUserDob() {
        return sp.getString(USER_DOB, "0");
    }

    public String getUserCompanyAddress() {
        return sp.getString(USER_COMPANY_ADDRESS, "");
    }

    public String getUserResindentialAddress() {
        return sp.getString(USER_RESINDENTI_ALADDRESS, "");
    }

    public String getUserUploadPhoto() {
        return sp.getString(USER_UPLOAD_PHOTO, "");
    }


    public String getHeaderKey() {
        return sp.getString(HEADER_KEY, "");
    }

    public void setHeaderKey(String headerKey) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(HEADER_KEY, headerKey);
        editor.commit();
    }

    public boolean isRememberMe() {
        return sp.getBoolean(Remember_Me_Login, false);
    }

    public void setRememberMe(boolean isRememberMe) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Remember_Me_Login, isRememberMe);
        editor.commit();
    }

    public String getSCHEDULE_END_DATE_TIME() {
        return SCHEDULE_END_DATE_TIME;
    }

    public void setSCHEDULE_END_DATE_TIME(String SCHEDULE_END_DATE_TIME) {
        this.SCHEDULE_END_DATE_TIME = SCHEDULE_END_DATE_TIME;
    }

    public String getSCHEDULE_DESCRIPTION() {
        return SCHEDULE_DESCRIPTION;
    }

    public void setSCHEDULE_DESCRIPTION(String SCHEDULE_DESCRIPTION) {
        this.SCHEDULE_DESCRIPTION = SCHEDULE_DESCRIPTION;
    }

    public String getRememberEmail() {
        return sp.getString(Remember_Email, "");
    }

    public String getRememberPassword() {
        return sp.getString(Remember_Password, "");
    }

    public void setRememberEmail(String email) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Remember_Email, email);
        editor.commit();
    }

    public void setRememberPassword(String password) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Remember_Password, password);
        editor.commit();
    }

    public void clearPrefSetup() {
        String email = getRememberEmail();
        String password = getRememberPassword();
        boolean isRememberMe = isRememberMe();
        sp.edit().clear().commit();
        setRememberMe(isRememberMe);
        setRememberEmail(email);
        setRememberPassword(password);
    }

    public String getUSER_OTP() {
        return sp.getString(USER_OTP, "");
    }

    public void setUSER_OTP(String USER_OTP) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_OTP, USER_OTP);
        editor.commit();
    }


//    TODO Client


    public long getCLIENT_ID() {
        return sp.getLong(CLIENT_ID, 0L);
    }

    public void setCLIENT_ID(long CLIENT_ID) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(String.valueOf(CLIENT_ID), CLIENT_ID);
        editor.commit();
    }


    public String getCLIENT_GENDER() {
        return sp.getString(CLIENT_GENDER, "");
    }

    public void setCLIENT_GENDER(String CLIENT_GENDER) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(CLIENT_GENDER, CLIENT_GENDER);
        editor.commit();
    }

    public String getCLIENT_ADDRESSTYPE() {
        return sp.getString(CLIENT_ADDRESSTYPE, "");
    }

    public void setCLIENT_ADDRESSTYPE(String CLIENT_ADDRESSTYPE) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(CLIENT_ADDRESSTYPE, CLIENT_ADDRESSTYPE);
        editor.commit();
    }


    public String getCLIENT_ADDRESS() {
        return sp.getString(CLIENT_ADDRESS, "");
    }

    public void setCLIENT_ADDRESS(String CLIENT_ADDRESS) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(CLIENT_ADDRESS, CLIENT_ADDRESS);
        editor.commit();
    }

    public String getCLIENT_ALTERNETCONTACT() {
        return sp.getString(CLIENT_ALTERNETCONTACT, "");
    }

    public void setCLIENT_ALTERNETCONTACT(String CLIENT_ALTERNETCONTACT) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(CLIENT_ALTERNETCONTACT, CLIENT_ALTERNETCONTACT);
        editor.commit();
    }

    public String getCLIENT_ALTERNATEMOBILE() {
        return sp.getString(CLIENT_ALTERNATEMOBILE, "");
    }

    public void setCLIENT_ALTERNATEMOBILE(String CLIENT_ALTERNATEMOBILE) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(CLIENT_ALTERNATEMOBILE, CLIENT_ALTERNATEMOBILE);
        editor.commit();
    }


    // TODO Coworker


    public long getCOWORKER_OCCUPATIONID() {
        return sp.getLong(COWORKER_OCCUPATIONID, 0L);
    }

    public void setCOWORKER_OCCUPATIONID(long COWORKER_OCCUPATIONID) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(String.valueOf(COWORKER_OCCUPATIONID), COWORKER_OCCUPATIONID);
        editor.commit();
    }

    public String getCOWORKER_OCCUPATIONTITLE() {
        return sp.getString(COWORKER_OCCUPATIONTITLE, "");
    }

    public void setCOWORKER_OCCUPATIONTITLE(String COWORKER_OCCUPATIONTITLE) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(COWORKER_OCCUPATIONTITLE, COWORKER_OCCUPATIONTITLE);
        editor.commit();
    }

    public String getCOWORKER_NATIVEADDRESS() {
        return sp.getString(COWORKER_NATIVEADDRESS, "");
    }

    public void setCOWORKER_NATIVEADDRESS(String COWORKER_NATIVEADDRESS) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(COWORKER_NATIVEADDRESS, COWORKER_NATIVEADDRESS);
        editor.commit();
    }


    public String getCOWORKER_PANNUMBER() {
        return sp.getString(COWORKER_PANNUMBER, "");
    }

    public void setCOWORKER_PANNUMBER(String COWORKER_PANNUMBER) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(COWORKER_PANNUMBER, COWORKER_PANNUMBER);
        editor.commit();
    }

    public String getCOWORKER_CARDURL() {
        return sp.getString(COWORKER_CARDURL, "");
    }

    public void setCOWORKER_CARDURL(String COWORKER_CARDURL) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(COWORKER_CARDURL, COWORKER_CARDURL);
        editor.commit();
    }


    public String getCOWORKER_PHOTOURL() {
        return sp.getString(COWORKER_PHOTOURL, "");
    }

    public void setCOWORKER_PHOTOURL(String COWORKER_PHOTOURL) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(COWORKER_PHOTOURL, COWORKER_PHOTOURL);
        editor.commit();
    }

    public String getCOWORKER_REFERREDBY() {
        return sp.getString(COWORKER_REFERREDBY, "");
    }

    public void setCOWORKER_REFERREDBY(String COWORKER_REFERREDBY) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(COWORKER_REFERREDBY, COWORKER_REFERREDBY);
        editor.commit();
    }


    public String getCOWORKER_RATING() {
        return sp.getString(COWORKER_RATING, "");
    }

    public void setCOWORKER_RATING(String COWORKER_RATING) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(COWORKER_RATING, COWORKER_RATING);
        editor.commit();
    }

    public String getFcmId() {
        return sp.getString(this.fcmId, null);
    }

    public void setFcmId(String FCM_ID) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(this.fcmId, FCM_ID);
        editor.commit();
    }

    public void setTempImage(String filePath) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(this.TEMP_IMAGE, filePath);
        editor.commit();
    }

    public String getTempImage() {
        return sp.getString(this.TEMP_IMAGE, null);
    }
}