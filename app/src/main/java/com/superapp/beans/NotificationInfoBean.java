package com.superapp.beans;

import java.io.Serializable;

/**
 * Created by ss on 23-Mar-17.
 */

public class NotificationInfoBean implements Serializable {

    private long id;
    private String title;
    private String description;
    private String link;
    private String image;


    public NotificationInfoBean(long id, String title, String description, String link, String image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.link = link;
        this.image = image;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
