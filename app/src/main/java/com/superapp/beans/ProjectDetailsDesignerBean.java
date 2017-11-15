package com.superapp.beans;

/**
 * Created by APPNWEB on 07-09-2016.
 */
public class ProjectDetailsDesignerBean {

    String designerName;
    String designerAddress;
    String designerEmail;
    String designerMobile;

    public ProjectDetailsDesignerBean(String designerName, String designerAddress, String designerEmail, String designerMobile) {
        this.designerName = designerName;
        this.designerAddress = designerAddress;
        this.designerEmail = designerEmail;
        this.designerMobile = designerMobile;
    }

    public String getDesignerName() {
        return designerName;
    }

    public void setDesignerName(String designerName) {
        this.designerName = designerName;
    }

    public String getDesignerAddress() {
        return designerAddress;
    }

    public void setDesignerAddress(String designerAddress) {
        this.designerAddress = designerAddress;
    }

    public String getDesignerEmail() {
        return designerEmail;
    }

    public void setDesignerEmail(String designerEmail) {
        this.designerEmail = designerEmail;
    }

    public String getDesignerMobile() {
        return designerMobile;
    }

    public void setDesignerMobile(String designerMobile) {
        this.designerMobile = designerMobile;
    }
}
