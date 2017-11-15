package com.superapp.beans;

/**
 * Created by Rakesh on 06-Aug-16.
 */
public class SettingsBean {

    private String isArchive;
    private String deleteFullProject;

    public String getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(String isArchive) {
        this.isArchive = isArchive;
    }

    public String getDeleteFullProject() {
        return deleteFullProject;
    }

    public void setDeleteFullProject(String deleteFullProject) {
        this.deleteFullProject = deleteFullProject;
    }

    public SettingsBean(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public SettingsBean() {
    }

    String projectName;
}
