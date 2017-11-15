package com.superapp.database;

public interface DBConstants {
    String DB_NAME = "SuperApp.db";

    //	Table Names
    String NOTIFICATION_TABLE = "notification";

    //	Fields of Tables
    String ID = "id";

    String PROJECT_ID = "project_id";
    String TYPE = "type";
    String SUBSCRIBER_ID = "subscriber_id";
    String MESSAGE = "message";
    String ADDED_DATE = "added_date";
    String TO_TYPE = "to_type";


    //  Create Notification Table
    String NOTIFICATION_TABLE_CREATE_QUERY = "create table "
            + NOTIFICATION_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PROJECT_ID + " TEXT, "
            + TYPE + " TEXT, "
            + SUBSCRIBER_ID + " TEXT, "
            + MESSAGE + " TEXT, "
            + ADDED_DATE + " TEXT, "
            + TO_TYPE + " TEXT)";
    //  Create Menu Table

    String[] TABLES = {NOTIFICATION_TABLE};

    String CREATE_TABLES[] = {NOTIFICATION_TABLE_CREATE_QUERY};
}
