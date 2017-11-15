package com.superapp.beans;

import android.text.TextUtils;

/**
 * Created by inte on 9/2/2016.
 */
public class Region {

    private long id;
    private String title;

    private String country;
    private String state;
    private String city;
    private String locality;

    public Region() {
    }

    public Region(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getTitle() {
        if (!TextUtils.isEmpty(country)) {
            return country;
        }
        if (!TextUtils.isEmpty(state)) {
            return state;
        }
        if (!TextUtils.isEmpty(city)) {
            return city;
        }
        if (!TextUtils.isEmpty(locality)) {
            return locality;
        }
        return title;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
