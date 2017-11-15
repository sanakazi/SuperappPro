package com.superapp.beans;

/**
 * Created by APPNWEB on 05-09-2016.
 */
public class ApprovalsBean {

    String Description;
    String Documents;
    String Occupation;
    String Photos;
    String Category;
    String clientComments;

    public ApprovalsBean(String category, String photos, String occupation, String documents, String description) {
        Description = description;
        Documents = documents;
        Occupation = occupation;
        Photos = photos;
        Category = category;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDocuments() {
        return Documents;
    }

    public void setDocuments(String documents) {
        Documents = documents;
    }

    public String getOccupation() {
        return Occupation;
    }

    public void setOccupation(String occupation) {
        Occupation = occupation;
    }

    public String getPhotos() {
        return Photos;
    }

    public void setPhotos(String photos) {
        Photos = photos;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getClientComments() {
        return clientComments;
    }

    public void setClientComments(String clientComments) {
        this.clientComments = clientComments;
    }
}
