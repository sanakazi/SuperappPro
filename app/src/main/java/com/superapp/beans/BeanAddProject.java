package com.superapp.beans;

/**
 * Created by inte on 9/1/2016.
 */
public class BeanAddProject {

    private String projectName;
    private String typeOfProject;
    private String designStartDate;
    private String executionStartDate;
    private String estimatedStartDate;
    private String budget;
    private String spentMoneyToggle;
    private long userId;
    private long clientId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }


    public BeanAddress getLocation() {
        return location;
    }

    public void setLocation(BeanAddress location) {
        this.location = location;
    }

    private BeanAddress location;

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getEstimatedStartDate() {
        return estimatedStartDate;
    }

    public void setEstimatedStartDate(String estimatedStartDate) {
        this.estimatedStartDate = estimatedStartDate;
    }

    public String getExecutionStartDate() {
        return executionStartDate;
    }

    public void setExecutionStartDate(String executionStartDate) {
        this.executionStartDate = executionStartDate;
    }

    public String getDesignStartDate() {
        return designStartDate;
    }

    public void setDesignStartDate(String designStartDate) {
        this.designStartDate = designStartDate;
    }

    public String getTypeOfProject() {
        return typeOfProject;
    }

    public void setTypeOfProject(String typeOfProject) {
        this.typeOfProject = typeOfProject;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getSpentMoneyToggle() {
        return spentMoneyToggle;
    }

    public void setSpentMoneyToggle(String spentMoneyToggle) {
        this.spentMoneyToggle = spentMoneyToggle;
    }
}