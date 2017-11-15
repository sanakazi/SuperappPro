package com.superapp.webservice;

import org.json.JSONObject;

public interface Interactor {

    // For Testing Purpose
//    String BASE_API_URL = "https://www.superapp.co.in/supperappapi/"; // Test URL
//    String BASE_URL = BASE_API_URL + "idesignerv2-t.php/";

    // For Live
    String BASE_API_URL = "https://www.superapp.co.in/supperappapi/"; // Live Development URL
    String BASE_URL = BASE_API_URL + "idesignerv22.php/";


    String ABOUT_US_URL = "https://www.artshopy.com/supperapp/supperapp-web/about-us.html";

    String Method_Login = BASE_URL + "Login";
    int RequestCode_Login = 1;
    String Tag_Login = "TAG_Login";

    String Method_Register = BASE_URL + "Register";
    int RequestCode_Register = 2;
    String Tag_Register = "TAG_Register";

    String Method_ForgotPassword = BASE_URL + "ForgotPassword";
    int RequestCode_ForgotPassword = 3;
    String Tag_ForgotPassword = "TAG_ForgotPassword";

    String Method_UpdatePassword = BASE_URL + "UpdatePassword";
    int RequestCode_UpdatePassword = 4;
    String Tag_UpdatePassword = "TAG_UpdatePassword";

    String Method_AddClient = BASE_URL + "AddClient";
    int RequestCode_AddClient = 5;
    String Tag_AddClient = "TAG_AddClient";

    String Method_GetOccupations = BASE_URL + "GetOccupations";
    int RequestCode_GetOccupations = 6;
    String Tag_GetOccupations = "TAG_GetOccupations";

    String Method_AddProject = BASE_URL + "AddProject";
    int RequestCode_AddProject = 7;
    String Tag_AddProject = "TAG_AddProject";

    String Method_GetClients = BASE_URL + "GetClients";
    int RequestCode_GetClients = 8;
    String Tag_GetClients = "TAG_GetClients";

    String Method_GetCountry = BASE_URL + "GetCountry";
    int RequestCode_GetCountry = 9;
    String Tag_GetCountry = "TAG_GetCountry";

    String Method_GetState = BASE_URL + "GetState";
    int RequestCode_GetState = 10;
    String Tag_GetState = "TAG_GetState";

    String Method_GetCity = BASE_URL + "GetCity";
    int RequestCode_GetCity = 11;
    String Tag_GetCity = "TAG_GetCity";

    String Method_GetLocality = BASE_URL + "GetLocality";
    int RequestCode_GetLocality = 12;
    String Tag_GetLocality = "TAG_GetLocality";

    String Method_GetCoWorkers = BASE_URL + "GetCoWorkers";
    int RequestCode_GetCoWorkers = 13;
    String Tag_GetCoWorkers = "TAG_GetCoWorkers";

    String Method_GetProjects = BASE_URL + "GetProjects";
    int RequestCode_GetProjects = 14;
    String Tag_GetProjects = "TAG_GetProjects";

    String Method_AddCoWorker = BASE_URL + "AddCoWorker";
    int RequestCode_AddCoWorker = 15;
    String Tag_AddCoWorker = "TAG_AddCoWorker";

//    String Method_GetCategories = BASE_URL + "GetCategories";
//    int RequestCode_GetCategories = 16;
//    String Tag_GetCategories = "TAG_GetCategories";

    String Method_GetParentCategory = BASE_URL + "GetParentCategory";
    int RequestCode_GetParentCategory = 16;
    String Tag_GetParentCategory = "TAG_GetParentCategory";

    String Method_SubmitChecklist = BASE_URL + "SubmitChecklist";
    int RequestCode_SubmitChecklist = 17;
    String Tag_SubmitChecklist = "TAG_SubmitChecklist";

    String Method_GetAllTasks = BASE_URL + "GetAllTasks";
    int RequestCode_GetAllTasks = 18;
    String Tag_GetAllTasks = "TAG_GetAllTasks";

    String Method_GetScheduleCategory = BASE_URL + "GetScheduleCategory";
    int RequestCode_GetScheduleCategory = 19;
    String Tag_GetScheduleCategory = "TAG_GetScheduleCategory";

    String Method_GetScheduleSubCategory = BASE_URL + "GetScheduleSubCategory";
    int RequestCode_GetScheduleSubCategory = 20;
    String Tag_GetScheduleSubCategory = "TAG_GetScheduleSubCategory";

    String Method_SubmitSchedule = BASE_URL + "SubmitSchedule";
    int RequestCode_SubmitSchedule = 21;
    String Tag_SubmitSchedule = "TAG_SubmitSchedule";

    String Method_GetAllScheduleList = BASE_URL + "GetAllScheduleList";
    int RequestCode_GetAllScheduleList = 22;
    String Tag_GetAllScheduleList = "TAG_GetAllScheduleList";

    String Method_GetAllCommunication = BASE_URL + "GetAllCommunication";
    int RequestCode_GetAllCommunication = 23;
    String Tag_GetAllCommunication = "TAG_GetAllCommunication";

    String Method_GetClientDetail = BASE_URL + "GetClientDetail";
    int RequestCode_GetClientDetail = 24;
    String Tag_GetClientDetail = "TAG_GetClientDetail";

    String Method_GetProjectStaffList = BASE_URL + "GetProjectStaffList";
    int RequestCode_GetProjectStaffList = 25;
    String Tag_GetProjectStaffList = "TAG_GetProjectStaffList";

    String Method_GetCoworkerDetail = BASE_URL + "GetCoWorkerDetail";
    int RequestCode_GetCoworkerDetail = 26;
    String Tag_GetCoworkerDetail = "TAG_GetCoworkerDetail";

    String Method_AddEditCommunication = BASE_URL + "AddEditCommunication";
    int RequestCode_AddEditCommunication = 27;
    String Tag_AddEditCommunication = "TAG_AddEditCommunication";

    String Method_GetProjectDetail = BASE_URL + "GetProjectDetail";
    int RequestCode_GetProjectDetail = 28;
    String Tag_GetProjectDetail = "TAG_GetProjectDetail";

    String Method_GetAllTaskOfProject = BASE_URL + "GetAllTaskOfProject";
    int RequestCode_GetAllTaskOfProject = 29;
    String Tag_GetAllTaskOfProject = "TAG_GetAllTaskOfProject";

    String Method_SendFeedBack = BASE_URL + "SendFeedBack";
    int RequestCode_SendFeedBack = 30;
    String Tag_SendFeedBack = "TAG_SendFeedBack";

    String Method_AddUpdateTransaction = BASE_URL + "AddUpdateTransaction";
    int RequestCode_AddUpdateTransaction = 31;
    String Tag_AddUpdateTransaction = "TAG_AddUpdateTransaction";

    String Method_GetAllTransaction = BASE_URL + "GetAllTransaction";
    int RequestCode_GetAllTransaction = 32;
    String Tag_GetAllTransaction = "TAG_GetAllTransaction";

    String Method_UpdateTaskStatus = BASE_URL + "UpdateTaskStatus";
    int RequestCode_UpdateTaskStatus = 33;
    String Tag_UpdateTaskStatus = "TAG_UpdateTaskStatus";

    String Method_EditProfile = BASE_URL + "EditProfile";
    int RequestCode_EditProfile = 34;
    String Tag_EditProfile = "TAG_EditProfile";

    String Method_DeleteProject = BASE_URL + "DeleteProject";
    int RequestCode_DeleteProject = 35;
    String Tag_DeleteProject = "TAG_DeleteProject";

    String Method_ChangePassword = BASE_URL + "ChangePassword";
    int RequestCode_ChangePassword = 36;
    String Tag_ChangePassword = "TAG_ChangePassword";

    String Method_UpdateScheduleStatus = BASE_URL + "UpdateScheduleStatus";
    int RequestCode_UpdateScheduleStatus = 37;
    String Tag_UpdateScheduleStatus = "TAG_UpdateScheduleStatus";

    String Method_GetAllNotification = BASE_URL + "GetAllNotification";
    int RequestCode_GetAllNotification = 38;
    String Tag_GetAllNotification = "TAG_GetAllNotification";

    String Method_GetSuperSearchDetailList = BASE_URL + "GetSuperSearchDetailList";
    int RequestCode_GetSuperSearchDetailList = 39;
    String Tag_GetSuperSearchDetailList = "TAG_GetSuperSearchDetailList";

    String Method_DeleteClient = BASE_URL + "DeleteClient";
    int RequestCode_DeleteClient = 40;
    String Tag_DeleteClient = "TAG_DeleteClient";

    String Method_DeleteCoworker = BASE_URL + "DeleteCoworker";
    int RequestCode_DeleteCoworker = 41;
    String Tag_DeleteCoworker = "TAG_DeleteCoworker";

    String Method_SendID = BASE_URL + "SendID";
    int RequestCode_SendID = 42;
    String Tag_SendID = "TAG_SendID";

    String Method_ToggleNotification = BASE_URL + "ToggleNotification";
    int RequestCode_ToggleNotification = 43;
    String Tag_ToggleNotification = "TAG_ToggleNotification";

    String Method_ProjectComplete = BASE_URL + "ProjectComplete";
    int RequestCode_ProjectComplete = 44;
    String Tag_ProjectComplete = "TAG_ProjectComplete";

    String Method_submitRating = BASE_URL + "submitRating";
    int RequestCode_submitRating = 45;
    String Tag_submitRating = "TAG_submitRating";

    String Method_GetMaterial = BASE_URL + "GetMaterial";
    int RequestCode_GetMaterial = 46;
    String Tag_GetMaterial = "TAG_GetMaterial";

    String Method_checkBalanceMoney = BASE_URL + "checkBalanceMoney";
    int RequestCode_checkBalanceMoney = 47;
    String Tag_checkBalanceMoney = "TAG_checkBalanceMoney";

    String Method_ConfirmRegistration = BASE_URL + "ConfirmRegistration";
    int RequestCode_ConfirmRegistration = 48;
    String Tag_ConfirmRegistration = "TAG_ConfirmRegistration";

    String Method_ResendOTPRegistration = BASE_URL + "ResendOTPRegistration";
    int RequestCode_ResendOTPRegistration = 49;
    String Tag_ResendOTPRegistration = "TAG_ResendOTPRegistration";

    String Method_ResendOTPForgot = BASE_URL + "ResendOTPForgot";
    int RequestCode_ResendOTPForgot = 50;
    String Tag_ResendOTPForgot = "TAG_ResendOTPForgot";

    String Method_changeNumber = BASE_URL + "changeNumber";
    int RequestCode_changeNumber = 51;
    String Tag_changeNumber = "TAG_changeNumber";

    String Method_updateMobile = BASE_URL + "updateMobile";
    int RequestCode_updateMobile = 52;
    String Tag_updateMobile = "TAG_updateMobile";

    String Method_getAllAds = BASE_URL + "getAllAds";
    int RequestCode_getAllAds = 53;
    String Tag_getAllAds = "TAG_getAllAds";

    String Method_UpdateSuperSearch = BASE_URL + "UpdateSuperSearch";
    int RequestCode_UpdateSuperSearch = 54;
    String Tag_UpdateSuperSearch = "TAG_UpdateSuperSearch";

    String Method_getCoworkerSuperSearchDetail = BASE_URL + "getCoworkerSuperSearchDetail";
    int RequestCode_getCoworkerSuperSearchDetail = 55;
    String Tag_getCoworkerSuperSearchDetail = "TAG_getCoworkerSuperSearchDetail";

    String Method_getSurvey = BASE_URL + "getSurvey";
    int RequestCode_getSurvey = 56;
    String Tag_getSurvey = "TAG_getSurvey";

    String Method_getSurveyQuestion = BASE_URL + "getSurveyQuestion";
    int RequestCode_getSurveyQuestion = 57;
    String Tag_getSurveyQuestion = "TAG_getSurveyQuestion";

    String Method_submitSurveyList = BASE_URL + "submitSurveyList";
    int RequestCode_submitSurveyList = 58;
    String Tag_submitSurveyList = "TAG_submitSurveyList";

    String Method_updateMembership = BASE_URL + "updateMembership";
    int RequestCode_updateMembership = 59;
    String Tag_updateMembership = "TAG_updateMembership";

    String Method_GetAllCategory = BASE_URL + "GetAllCategory";
    int RequestCode_GetAllCategory = 60;
    String Tag_GetAllCategory = "TAG_GetAllCategory";

    String Method_GetAllMaterial = BASE_URL + "GetAllMaterial";
    int RequestCode_GetAllMaterial = 61;
    String Tag_GetAllMaterial = "TAG_GetAllMaterial";

    String Method_GetAllSubmaterials = BASE_URL + "GetAllSubmaterials";
    int RequestCode_GetAllSubmaterials = 62;
    String Tag_GetAllSubmaterials = "TAG_GetAllSubmaterials";

    String Method_GetAllBrand = BASE_URL + "GetAllBrand";
    int RequestCode_GetAllBrand = 63;
    String Tag_GetAllBrand = "TAG_GetAllBrand";

    String Method_GetAllColors = BASE_URL + "GetAllColors";
    int RequestCode_GetAllColors = 64;
    String Tag_GetAllColors = "TAG_GetAllColors";

    String Method_GetAllSize = BASE_URL + "GetAllSize";
    int RequestCode_GetAllSize = 65;
    String Tag_GetAllSize = "TAG_GetAllSize";

    String Method_GetAllMeasurements = BASE_URL + "GetAllMeasurements";
    int RequestCode_GetAllMeasurements = 66;
    String Tag_GetAllMeasurements = "TAG_GetAllMeasurements";

    String Method_changeHandoverDate = BASE_URL + "changeHandoverDate";
    int RequestCode_changeHandoverDate = 67;
    String Tag_changeHandoverDate = "TAG_changeHandoverDate";

    String Method_GetAllAdminNotification = BASE_URL + "GetAllAdminNotification";
    int RequestCode_GetAllAdminNotification = 68;
    String Tag_GetAllAdminNotification = "TAG_GetAllAdminNotification";

    String Method_GetAllReminder = BASE_URL + "GetAllReminder";
    int RequestCode_GetAllReminder = 69;
    String Tag_GetAllReminder = "TAG_GetAllReminder";

    String Method_checkVersionCodeUser = BASE_URL + "checkVersionCodeUser";
    int RequestCode_checkVersionCodeUser = 70;
    String Tag_checkVersionCodeUser = "TAG_checkVersionCodeUser";

    String Method_GenerateChecksum = BASE_URL + "GenerateChecksum";
    int RequestCode_GenerateChecksum = 71;
    String Tag_GenerateChecksum = "TAG_GenerateChecksum";

    String Method_DeleteSchedule = BASE_URL + "DeleteSchedule";
    int RequestCode_DeleteSchedule = 72;
    String Tag_DeleteSchedule = "TAG_DeleteSchedule";

    String Method_getSurveyList = BASE_URL + "getSurveyList";
    int RequestCode_getSurveyList = 73;
    String Tag_getSurveyList = "TAG_getSurveyList";

    String Method_GetArchiveProjects = BASE_URL + "GetArchiveProjects";
    int RequestCode_GetArchiveProjects = 74;
    String Tag_GetArchiveProjects = "TAG_GetArchiveProjects";


    void makeStringGetRequest(String url, boolean showDialog);

    void makeJsonPostRequest(String url, JSONObject jsonRequest, boolean showDialog);
}
