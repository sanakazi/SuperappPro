package com.superapp.beans;

import android.text.TextUtils;

public class BeanAddress {
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String city;
    private String pincode;

    public String getAddressLine1() {
        return addressLine1 != null ? addressLine1 : "";
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2 != null ? addressLine2 : "";
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
//        return !addressLine3.isEmpty() ? addressLine3 : "N/A";
//        return addressLine3 != null ? addressLine3 : "";
        return TextUtils.isEmpty(addressLine3) ? "N/A" : addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getCity() {
        return city != null ? city : "";
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode != null ? pincode : "";
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    @Override
    public String toString() {
        String address = "";
        if (addressLine1 != null) {
            address += addressLine1 + ", ";
        }

        if (addressLine2 != null) {
            address += addressLine2 + ", ";
        }

//        if (addressLine3 != null) {
//            address += addressLine3 + ", ";
//        }

        if (!addressLine3.isEmpty()) {
            if (addressLine3.equalsIgnoreCase("N/A")) {


            } else
                address += addressLine3 + ", ";
        }

        if (city != null) {
            address += city + ", ";
        }

        if (pincode != null) {
            if (pincode.contains("0"))
                address += pincode + ", ";
            else
                address += pincode + ", ";
        }
        return (address.length() > 0 ? address.substring(0, address.length() - 2) : address);
//        return getAddressLine1() + ", " + getAddressLine2() + ", " + getAddressLine3() + ", " + getCity() + ", " + getPincode();    }
    }

    public String getMergedAddress() {
        return getAddressLine1() + "###" + getAddressLine2() + "###" + getAddressLine3() + "###" + getCity() + "###" + getPincode();
    }
}
