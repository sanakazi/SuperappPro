package com.superapp.webservice;

import android.os.Parcel;
import android.os.Parcelable;

import com.superapp.beans.BeanAd;
import com.superapp.beans.BeanAddClient;
import com.superapp.beans.BeanAddProject;
import com.superapp.beans.BeanAddress;
import com.superapp.beans.CoworkerSSBean;
import com.superapp.beans.NotificationInfoBean;
import com.superapp.beans.Region;
import com.superapp.beans.BeanMaterial;
import com.superapp.beans.BeanScheduleCategory;
import com.superapp.beans.BeanSelectCategory;
import com.superapp.beans.BeanTaskList;
import com.superapp.beans.BeanTotalBalanceMoney;
import com.superapp.beans.BeanTransaction;
import com.superapp.beans.BeanUserDetail;
import com.superapp.beans.ClientBean;
import com.superapp.beans.CommunicationBean;
import com.superapp.beans.CoworkerBean;
import com.superapp.beans.NotificationBean;
import com.superapp.beans.ProjectBean;
import com.superapp.beans.ReminderBean;
import com.superapp.beans.SQuestion;
import com.superapp.beans.ScheduleBean;
import com.superapp.beans.SuperSearchBean;
import com.superapp.beans.Survey;
import com.superapp.fragment.dashboard.add_forms.coworker_occupation.BeanOccupation;

import java.util.ArrayList;

public class ResponsePacket implements Parcelable {

    public ResponsePacket(String status, int errorCode) {
        this.status = status;
        this.errorCode = errorCode;
    }

    private String responsePacket;
    private String status;
    private int errorCode;
    private String message;
    private String state;
    private BeanUserDetail userDetail;
    private ArrayList<ClientBean> clientList;
    private ArrayList<CoworkerBean> coWorkerList;
    private ArrayList<ProjectBean> projectList;
    private ArrayList<BeanTaskList> taskList;
    private ArrayList<BeanOccupation> occupationArray;
    private ArrayList<Region> countryArray;
    private ArrayList<Region> stateList;
    private ArrayList<Region> cityList;
    private ArrayList<Region> localityList;
    private ArrayList<BeanSelectCategory> category;
    //    private ArrayList<BeanSelectCategory> materialCategory;
    private ArrayList<BeanScheduleCategory> scheduleCategoryList;
    private ArrayList<BeanScheduleCategory> scheduleSubCatList;
    private ArrayList<ScheduleBean> scheduleList;
    private ArrayList<CommunicationBean> communicationList;
    private ClientBean clientDetail;
    private CoworkerBean coworkerDetail;
    private CoworkerSSBean coworkerSSDetail;
    private ProjectBean projectDetail;
    private BeanUserDetail designerDetail;
    private ArrayList<BeanTransaction> transactionList;
    private ArrayList<NotificationBean> NotificationList;
    private ArrayList<SuperSearchBean> SuperSearchDetailList;
    private ArrayList<BeanMaterial> materialList;
    private BeanTotalBalanceMoney TotalBalanceMoney;
    private String aboutContent;
    private ArrayList<BeanAd> AdDetail;
    private ArrayList<SQuestion> surveyQuestions;
    private Survey surveyDetail;
    private String isNextSurvey;
    public ArrayList<BeanSelectCategory> material;
    public ArrayList<BeanSelectCategory> subMaterials;
    public ArrayList<BeanSelectCategory> brand;
    public ArrayList<BeanSelectCategory> color;
    public ArrayList<BeanSelectCategory> size;
    public ArrayList<BeanSelectCategory> measurements;
    private NotificationInfoBean NotificationInfo;
    private ArrayList<ReminderBean> ReminderList;
    int counter;
    private String versionCode;
    private String CHECKSUMHASH;
    private ArrayList<Survey> surveyDetailList;

    protected ResponsePacket(Parcel in) {
        responsePacket = in.readString();
        status = in.readString();
        errorCode = in.readInt();
        message = in.readString();
        state = in.readString();
        communicationList = in.createTypedArrayList(CommunicationBean.CREATOR);
        SuperSearchDetailList = in.createTypedArrayList(SuperSearchBean.CREATOR);
        materialList = in.createTypedArrayList(BeanMaterial.CREATOR);
        aboutContent = in.readString();
        AdDetail = in.createTypedArrayList(BeanAd.CREATOR);
        isNextSurvey = in.readString();
        counter = in.readInt();
        versionCode = in.readString();
        CHECKSUMHASH = in.readString();
        otp = in.readLong();
    }

    public static final Creator<ResponsePacket> CREATOR = new Creator<ResponsePacket>() {
        @Override
        public ResponsePacket createFromParcel(Parcel in) {
            return new ResponsePacket(in);
        }

        @Override
        public ResponsePacket[] newArray(int size) {
            return new ResponsePacket[size];
        }
    };

    public ArrayList<BeanTransaction> getTransactionList() {
        return transactionList != null ? transactionList : new ArrayList<BeanTransaction>();
    }

    public void setTransactionList(ArrayList<BeanTransaction> transactionList) {
        this.transactionList = transactionList;
    }

    public CoworkerBean getCoworkerDetail() {
        return coworkerDetail;
    }

    public void setCoworkerDetail(CoworkerBean coworkerDetail) {
        this.coworkerDetail = coworkerDetail;
    }

    public CoworkerSSBean getCoworkerSSDetail() {
        return coworkerSSDetail;
    }

    public void setCoworkerSSDetail(CoworkerSSBean coworkerSSDetail) {
        this.coworkerSSDetail = coworkerSSDetail;
    }

    public ClientBean getClientDetail() {
        return clientDetail;
    }

    public void setClientDetail(ClientBean clientDetail) {
        this.clientDetail = clientDetail;
    }


    public ArrayList<ScheduleBean> getScheduleList() {
        return scheduleList != null ? scheduleList : new ArrayList<ScheduleBean>();
    }

    public void setScheduleList(ArrayList<ScheduleBean> scheduleList) {
        this.scheduleList = scheduleList;
    }

    public ArrayList<BeanScheduleCategory> getScheduleSubCatList() {
        return scheduleSubCatList != null ? scheduleSubCatList : new ArrayList<BeanScheduleCategory>();
    }

    public void setScheduleSubCatList(ArrayList<BeanScheduleCategory> scheduleSubCatList) {
        this.scheduleSubCatList = scheduleSubCatList;
    }

    public ArrayList<BeanScheduleCategory> getScheduleCategoryList() {
        return scheduleCategoryList;
    }

    public void setScheduleCategoryList(ArrayList<BeanScheduleCategory> scheduleCategoryList) {
        this.scheduleCategoryList = scheduleCategoryList;
    }

//    public ArrayList<BeanSelectCategory> getCategoryList() {
//        return categoryList;
//    }
//
//    public void setCategoryList(ArrayList<BeanSelectCategory> categoryList) {
//        this.categoryList = categoryList;
//    }


    public ArrayList<BeanSelectCategory> getCategory() {
        return category != null ? category : new ArrayList<BeanSelectCategory>();
    }

    public void setCategory(ArrayList<BeanSelectCategory> category) {
        this.category = category;
    }


//    public ArrayList<BeanSelectCategory> getMaterialCategory() {
//        return materialCategory;
//    }
//
//    public void setMaterialCategory(ArrayList<BeanSelectCategory> materialCategory) {
//        this.materialCategory = materialCategory;
//    }

    public ArrayList<Region> getCountryArray() {
        return countryArray != null ? countryArray : new ArrayList<Region>();
    }

    public void setCountryArray(ArrayList<Region> countryArray) {
        this.countryArray = countryArray;
    }

    public ArrayList<Region> getStateList() {
        return stateList != null ? stateList : new ArrayList<Region>();
    }

    public void setStateList(ArrayList<Region> stateList) {
        this.stateList = stateList;
    }

    public ArrayList<Region> getCityList() {
        return cityList != null ? cityList : new ArrayList<Region>();
    }

    public void setCityList(ArrayList<Region> cityList) {
        this.cityList = cityList;
    }

    public ArrayList<Region> getLocalityList() {
        return localityList != null ? localityList : new ArrayList<Region>();
    }

    public void setLocalityList(ArrayList<Region> localityList) {
        this.localityList = localityList;
    }

    public ArrayList<BeanOccupation> getOccupationArray() {
        return occupationArray != null ? occupationArray : new ArrayList<BeanOccupation>();
    }

    public void setOccupationArray(ArrayList<BeanOccupation> occupationArray) {
        this.occupationArray = occupationArray;
    }

    public BeanAddProject getAddProject() {
        return addProject;
    }

    public void setAddProject(BeanAddProject addProject) {
        this.addProject = addProject;
    }

    private BeanAddProject addProject;

    public BeanAddClient getBeanAddClient() {
        return beanAddClient;
    }

    public void setBeanAddClient(BeanAddClient beanAddClient) {
        this.beanAddClient = beanAddClient;
    }

    private BeanAddClient beanAddClient;
    private BeanAddress addClientServiceAddress;

    public long getOtp() {
        return otp;
    }

    public void setOtp(long otp) {
        this.otp = otp;
    }

    private long otp;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getResponsePacket() {
        return responsePacket;
    }

    public void setResponsePacket(String responsePacket) {
        this.responsePacket = responsePacket;
    }

    public BeanUserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(BeanUserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public ArrayList<ClientBean> getClientList() {
        return clientList != null ? clientList : new ArrayList<ClientBean>();
    }

    public void setClientList(ArrayList<ClientBean> clientList) {
        this.clientList = clientList;
    }

    public BeanAddress getAddClientServiceAddress() {
        return addClientServiceAddress;
    }

    public void setAddClientServiceAddress(BeanAddress addClientServiceAddress) {
        this.addClientServiceAddress = addClientServiceAddress;
    }

    public ArrayList<CoworkerBean> getCoWorkerList() {
        return coWorkerList != null ? coWorkerList : new ArrayList<CoworkerBean>();
    }

    public void setCoWorkerList(ArrayList<CoworkerBean> coWorkerList) {
        this.coWorkerList = coWorkerList;
    }

    public ArrayList<ProjectBean> getProjectList() {
        return projectList != null ? projectList : new ArrayList<ProjectBean>();
    }

    public void setProjectList(ArrayList<ProjectBean> projectList) {
        this.projectList = projectList;
    }

    public ArrayList<BeanTaskList> getTaskList() {
        return taskList != null ? taskList : new ArrayList<BeanTaskList>();
    }

    public void setTaskList(ArrayList<BeanTaskList> taskList) {
        this.taskList = taskList;
    }

    public ArrayList<CommunicationBean> getCommunicationList() {
        return communicationList != null ? communicationList : new ArrayList<CommunicationBean>();
    }

    public void setCommunicationList(ArrayList<CommunicationBean> communicationList) {
        this.communicationList = communicationList;
    }

    public ProjectBean getProjectDetail() {
        return projectDetail;
    }

    public void setProjectDetail(ProjectBean projectDetail) {
        this.projectDetail = projectDetail;
    }

    public BeanUserDetail getDesignerDetail() {
        return designerDetail;
    }

    public void setDesignerDetail(BeanUserDetail designerDetail) {
        this.designerDetail = designerDetail;
    }

    public ArrayList<NotificationBean> getNotificationList() {
        return NotificationList != null ? NotificationList : new ArrayList<NotificationBean>();
    }

    public void setNotificationList(ArrayList<NotificationBean> notificationList) {
        NotificationList = notificationList;
    }

    public ArrayList<SuperSearchBean> getSuperSearchDetailList() {
        return SuperSearchDetailList != null ? SuperSearchDetailList : new ArrayList<SuperSearchBean>();
    }

    public void setSuperSearchDetailList(ArrayList<SuperSearchBean> superSearchDetailList) {
        SuperSearchDetailList = superSearchDetailList;
    }

    public ArrayList<BeanMaterial> getMaterialList() {
        return materialList != null ? materialList : new ArrayList<>();
    }

    public void setMaterialList(ArrayList<BeanMaterial> materialList) {
        this.materialList = materialList;
    }

    public BeanTotalBalanceMoney getTotalBalanceMoney() {
        return TotalBalanceMoney;
    }

    public void setTotalBalanceMoney(BeanTotalBalanceMoney totalBalanceMoney) {
        TotalBalanceMoney = totalBalanceMoney;
    }

    public String getAboutContent() {
        return aboutContent;
    }

    public void setAboutContent(String aboutContent) {
        this.aboutContent = aboutContent;
    }


    public ArrayList<BeanAd> getAdDetail() {
        return AdDetail != null ? AdDetail : new ArrayList<>();
    }

    public void setAdDetail(ArrayList<BeanAd> adDetail) {
        AdDetail = adDetail;
    }

    public ArrayList<SQuestion> getSurveyQuestions() {
        return surveyQuestions;
    }

    public Survey getSurveyDetail() {
        return surveyDetail;
    }

    public String isNextSurvey() {
        return isNextSurvey == null ? "f" : isNextSurvey;
    }

    public ArrayList<BeanSelectCategory> getMaterial() {
        return material != null ? material : new ArrayList<>();
    }

    public void setMaterial(ArrayList<BeanSelectCategory> material) {
        this.material = material;
    }

    public ArrayList<BeanSelectCategory> getSubMaterials() {
        return subMaterials != null ? subMaterials : new ArrayList<>();
    }

    public void setSubMaterials(ArrayList<BeanSelectCategory> subMaterials) {
        this.subMaterials = subMaterials;
    }

    public ArrayList<BeanSelectCategory> getBrand() {
        return brand != null ? brand : new ArrayList<>();
    }

    public void setBrand(ArrayList<BeanSelectCategory> brand) {
        this.brand = brand;
    }

    public ArrayList<BeanSelectCategory> getColor() {
        return color != null ? color : new ArrayList<>();
    }

    public void setColor(ArrayList<BeanSelectCategory> color) {
        this.color = color;
    }

    public ArrayList<BeanSelectCategory> getSize() {
        return size != null ? size : new ArrayList<>();
    }

    public void setSize(ArrayList<BeanSelectCategory> size) {
        this.size = size;
    }

    public ArrayList<BeanSelectCategory> getMeasurements() {
        return measurements != null ? measurements : new ArrayList<>();
    }

    public void setMeasurements(ArrayList<BeanSelectCategory> measurements) {
        this.measurements = measurements;
    }

    public NotificationInfoBean getNotificationInfo() {
        return NotificationInfo;
    }

    public void setNotificationInfo(NotificationInfoBean notificationInfo) {
        NotificationInfo = notificationInfo;
    }

    public ArrayList<ReminderBean> getReminderList() {
        return ReminderList != null ? ReminderList : new ArrayList<ReminderBean>();
    }

    public void setReminderList(ArrayList<ReminderBean> reminderList) {
        ReminderList = reminderList;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }


    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getCHECKSUMHASH() {
        return CHECKSUMHASH;
    }

    public void setCHECKSUMHASH(String CHECKSUMHASH) {
        this.CHECKSUMHASH = CHECKSUMHASH;
    }

    public ArrayList<Survey> getSurveyDetailList() {
        return surveyDetailList != null ? surveyDetailList : new ArrayList<Survey>();
    }

    public void setSurveyDetailList(ArrayList<Survey> surveyDetailList) {
        this.surveyDetailList = surveyDetailList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(responsePacket);
        dest.writeString(status);
        dest.writeInt(errorCode);
        dest.writeString(message);
        dest.writeString(state);
        dest.writeTypedList(communicationList);
        dest.writeTypedList(SuperSearchDetailList);
        dest.writeTypedList(materialList);
        dest.writeString(aboutContent);
        dest.writeTypedList(AdDetail);
        dest.writeString(isNextSurvey);
        dest.writeInt(counter);
        dest.writeString(versionCode);
        dest.writeString(CHECKSUMHASH);
        dest.writeLong(otp);
    }
}