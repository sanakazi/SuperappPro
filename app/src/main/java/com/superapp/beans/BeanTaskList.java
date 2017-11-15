package com.superapp.beans;

import java.util.ArrayList;

/**
 * Created by inte on 9/8/2016.
 */
public class BeanTaskList {

    private long projectId;
    private long clientId;
    private String projectName;
    private String projectType;
    private String address;
    private long taskId;
    private String taskDescription;
    private long suitableDateTime;
    private long suitableDate;
    private long suitableTime;
    //    private String suitableDate;
//    private String suitableTime;
    private String status;
    private String staffComment;
    private String designerCompanyName;
    private String designerName;
    private String designerPhone;

    // This is used for internal ure of the app, not for the service call
    private String statusForUpdate;

    private String rejectReason;
    private String Category;
    private String name;
    private String checkListType;
    private String mobile;
    private String note;
    //    private ArrayList<BeanMaterial> materials;
    // List Parameters
    private String subCategory;
    private CoworkerBean coworkerDetail;
    private ArrayList<BeanMaterial> materials;

    public boolean isOpen = false;


    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStaffComment() {
        return staffComment;
    }

    public void setStaffComment(String staffComment) {
        this.staffComment = staffComment;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public CoworkerBean getCoworkerDetail() {
        return coworkerDetail;
    }

    public void setCoworkerDetail(CoworkerBean coworkerDetail) {
        this.coworkerDetail = coworkerDetail;
    }

    public String getStatusForUpdate() {
        return statusForUpdate;
    }

    public void setStatusForUpdate(String statusForUpdate) {
        this.statusForUpdate = statusForUpdate;
    }

    public String getDesignerCompanyName() {
        return designerCompanyName;
    }

    public void setDesignerCompanyName(String designerCompanyName) {
        this.designerCompanyName = designerCompanyName;
    }

    public String getDesignerName() {
        return designerName;
    }

    public void setDesignerName(String designerName) {
        this.designerName = designerName;
    }

    public String getDesignerPhone() {
        return designerPhone;
    }

    public void setDesignerPhone(String designerPhone) {
        this.designerPhone = designerPhone;
    }

//    public ArrayList<BeanMaterial> getMaterials() {
//        return materials != null ? materials : new ArrayList<>();
//    }
//
//    public void setMaterials(ArrayList<BeanMaterial> materials) {
//        this.materials = materials;
//    }


    public long getSuitableDateTime() {
        return suitableDateTime;
    }

    public void setSuitableDateTime(long suitableDateTime) {
        this.suitableDateTime = suitableDateTime;
    }

    public long getSuitableDate() {
        return suitableDate;
    }

    public void setSuitableDate(long suitableDate) {
        this.suitableDate = suitableDate;
    }

    public long getSuitableTime() {
        return suitableTime;
    }

    public void setSuitableTime(long suitableTime) {
        this.suitableTime = suitableTime;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }


    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCheckListType() {
        return checkListType;
    }

    public void setCheckListType(String checkListType) {
        this.checkListType = checkListType;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ArrayList<BeanMaterial> getMaterials() {
        return materials;
    }

    public void setMaterials(ArrayList<BeanMaterial> materials) {
        this.materials = materials;
    }
}
