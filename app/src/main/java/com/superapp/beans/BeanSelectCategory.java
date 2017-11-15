package com.superapp.beans;

/**
 * Created by inte on 9/7/2016.
 */
public class BeanSelectCategory {

    private long id;
    private String title;
    private long category;
    private long subCategoryId;
    private String loginType;
    private String isSize;
    private String isColor;
    private String isMeasurement;


    public BeanSelectCategory() {
    }

    public BeanSelectCategory(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public BeanSelectCategory(String title) {
        this.title = title;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public long getCategory() {
        return category;
    }

    public void setCategory(long category) {
        this.category = category;
    }

    public long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getTitle();
    }

    public String getIsSize() {
        return isSize;
    }

    public void setIsSize(String isSize) {
        this.isSize = isSize;
    }

    public String getIsColor() {
        return isColor;
    }

    public void setIsColor(String isColor) {
        this.isColor = isColor;
    }

    public String getIsMeasurement() {
        return isMeasurement;
    }

    public void setIsMeasurement(String isMeasurement) {
        this.isMeasurement = isMeasurement;
    }
}
