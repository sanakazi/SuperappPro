package com.superapp.beans;

import java.io.Serializable;

/**
 * Created by ss on 18-Jan-17.
 */

public class Survey implements Serializable{
    private long id;
    private String name;
    private String type;
    private String description;
    private SQuestion question;

    public Survey(){}

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SQuestion getQuestion() {
        return question;
    }

    public void setQuestion(SQuestion question) {
        this.question = question;
    }
}
