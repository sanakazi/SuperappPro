package com.superapp.beans;

import java.io.Serializable;

public class NotificationBean implements Serializable {

    private String to_type;
    private String message;
    private long id;
    private long addeddate;
    private long project_id;
    private long subscriber_id;
    private String is_read;
    private String type;
    private String title;
    private String login_type;
    private String description;


    public String getTo_type() {
        return to_type;
    }

    public void setTo_type(String to_type) {
        this.to_type = to_type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAddeddate() {
        return addeddate;
    }

    public void setAddeddate(long addeddate) {
        this.addeddate = addeddate;
    }

    public long getProject_id() {
        return project_id;
    }

    public void setProject_id(long project_id) {
        this.project_id = project_id;
    }

    public long getSubscriber_id() {
        return subscriber_id;
    }

    public void setSubscriber_id(long subscriber_id) {
        this.subscriber_id = subscriber_id;
    }

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
