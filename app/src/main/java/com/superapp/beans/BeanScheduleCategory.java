package com.superapp.beans;

/**
 * Created by inte on 9/9/2016.
 */
public class BeanScheduleCategory {

    private long id;
    private String item;

    public BeanScheduleCategory(long id, String item) {
        this.id = id;
        this.item = item;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
