package com.superapp.beans;

import android.text.TextUtils;
import android.view.TextureView;

/**
 * Created by Rakesh on 04-Aug-16.
 */
public class ProjectBean {

    // For project listing
    private long projectId;
    private String projectName;
    private String location;
    private long projectStartedDays;
    private long workStartedDays;
    private String notificationCounter;
    private String percentCompleted;
    private String toggleNotification;
    private String avgRating;
    private String checkRating;
    private long updatedHandoverDate;

    // For Project Detail
    private String clientId;
    private String projectType;
    private long handoverdate;
    private long budget;

    // For internal use of client
    private ClientBean client;

    //    private int handoverdate;
    private int daysFromStartDate;
    private int daysFromExecutionDate;

    private double totalPaidByClient;
    private double totalBalance;
    private double overBudget;

    private String isComplete;

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getNotificationCounter() {
        return notificationCounter;
    }

    public void setNotificationCounter(String notificationCounter) {
        this.notificationCounter = notificationCounter;
    }

    public String getPercentCompleted() {
        return percentCompleted != null ? percentCompleted : "0";
    }

    public void setPercentCompleted(String percentCompleted) {
        this.percentCompleted = percentCompleted;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public void setHandoverdate(long handoverdate) {
        this.handoverdate = handoverdate;
    }

    public long getProjectStartedDays() {
        return projectStartedDays;
    }

    public void setProjectStartedDays(long projectStartedDays) {
        this.projectStartedDays = projectStartedDays;
    }

    public long getWorkStartedDays() {
        return workStartedDays;
    }

    public void setWorkStartedDays(long workStartedDays) {
        this.workStartedDays = workStartedDays;
    }

    public long getHandoverdate() {
        return handoverdate;
    }

    public long getBudget() {
        return budget;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public ClientBean getClient() {
        return client;
    }

    public void setClient(ClientBean client) {
        this.client = client;
    }

    public int getDaysFromStartDate() {
        return daysFromStartDate;
    }

    public void setDaysFromStartDate(int daysFromStartDate) {
        this.daysFromStartDate = daysFromStartDate;
    }

    public int getDaysFromExecutionDate() {
        return daysFromExecutionDate;
    }

    public void setDaysFromExecutionDate(int daysFromExecutionDate) {
        this.daysFromExecutionDate = daysFromExecutionDate;
    }

    public double getTotalPaidByClient() {
        return totalPaidByClient;
    }

    public void setTotalPaidByClient(double totalPaidByClient) {
        this.totalPaidByClient = totalPaidByClient;
    }

    public double getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(double totalBalance) {
        this.totalBalance = totalBalance;
    }

    public double getOverBudget() {
        return overBudget;
    }

    public void setOverBudget(double overBudget) {
        this.overBudget = overBudget;
    }

    public String getToggleNotification() {
        return TextUtils.isEmpty(toggleNotification) ? "f" : toggleNotification;
    }

    public void setToggleNotification(String toggleNotification) {
        this.toggleNotification = toggleNotification;
    }

    public String getIsComplete() {
        return TextUtils.isEmpty(isComplete) ? "f" : isComplete;
    }

    public void setIsComplete(String isComplete) {
        this.isComplete = isComplete;
    }

    public String getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(String avgRating) {
        this.avgRating = avgRating;
    }

    public String getCheckRating() {
        return checkRating;
    }

    public void setCheckRating(String checkRating) {
        this.checkRating = checkRating;
    }


    public long getUpdatedHandoverDate() {
        return updatedHandoverDate;
    }

    public void setUpdatedHandoverDate(long updatedHandoverDate) {
        this.updatedHandoverDate = updatedHandoverDate;
    }
}
