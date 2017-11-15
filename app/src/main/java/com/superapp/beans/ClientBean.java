package com.superapp.beans;

public class ClientBean {

    private long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String gender;
    private String alternetContact;
    private String alternateContactMobile;
    private String clientAddressType;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAlternetContact() {
        return alternetContact;
    }

    public void setAlternetContact(String alternetContact) {
        this.alternetContact = alternetContact;
    }

    public String getAlternateContactMobile() {
        return alternateContactMobile;
    }

    public void setAlternateContactMobile(String alternateContactMobile) {
        this.alternateContactMobile = alternateContactMobile;
    }

    public String getClientAddressType() {
        return clientAddressType;
    }

    public void setClientAddressType(String clientAddressType) {
        this.clientAddressType = clientAddressType;
    }
}
