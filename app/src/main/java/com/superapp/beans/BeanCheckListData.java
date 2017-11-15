package com.superapp.beans;

import java.util.ArrayList;

/**
 * Created by Abhijeet-PC on 15-Feb-17.
 */

public class BeanCheckListData {

    private long taskId;
    private String Category;
//    private ArrayList<BeanMaterial> materials;
    private String status;
    private String name;
    private long suitableDateTime;
    private String checkListType;
    private String taskDescription;
    private String mobile;


    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

//    public ArrayList<BeanMaterial> getMaterials() {
//        return materials;
//    }
//
//    public void setMaterials(ArrayList<BeanMaterial> materials) {
//        this.materials = materials;
//    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSuitableDateTime() {
        return suitableDateTime;
    }

    public void setSuitableDateTime(long suitableDateTime) {
        this.suitableDateTime = suitableDateTime;
    }

    public String getCheckListType() {
        return checkListType;
    }

    public void setCheckListType(String checkListType) {
        this.checkListType = checkListType;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
