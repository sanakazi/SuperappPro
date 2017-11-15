package com.superapp.beans;

/**
 * Created by APPNWEB on 01-09-2016.
 */
public class BeanCountry {

    public BeanCountry() {
    }

    private long id;
    private String title;

    public BeanCountry(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
