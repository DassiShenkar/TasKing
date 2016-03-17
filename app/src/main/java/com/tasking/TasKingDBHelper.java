package com.tasking;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class TasKingDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 13;

    // Database Name
    private static final String DATABASE_NAME = "TasKingDB";
    // Table names
    private static final String TABLE_EMPLOYEES = "employees";
    private static final String TABLE_TASKS = "tasks";
    //Employee Table Column names
    private static final String KEY_USERNAME = "username";
    // Tasks Table Columns names
    private static final String KEY_TASK_ID = "id";
    private static final String KEY_TASK_NAME = "name";
    private static final String KEY_TIME = "time";
    private static final String KEY_DATE = "date";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_PRIORITY = "priority";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_STATUS = "status";
    private static final String KEY_ASSIGNEE = "assignee";
    private static final String KEY_FIREBASE_ID = "firebase_id";

    // Tables create statement
    private static final String CREATE_EMPLOYEES_TABLE = "CREATE TABLE "
            + TABLE_EMPLOYEES + "(" + KEY_USERNAME + " TEXT PRIMARY KEY);";
    private static final String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
            + KEY_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TASK_NAME + " TEXT," + KEY_TIME
            + " TEXT,"+ KEY_DATE + " TEXT," + KEY_CATEGORY + " TEXT," + KEY_PRIORITY
            + " TEXT," + KEY_STATUS + " TEXT," + KEY_ASSIGNEE + " TEXT,"+ KEY_FIREBASE_ID + " TEXT,"
            + KEY_LOCATION + " TEXT);";
    public TasKingDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EMPLOYEES_TABLE);
        db.execSQL(CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }
/*
    private static final int DATABASE_VERSION = 11;

    // Database Name
    private static final String DATABASE_NAME = "TasKingDB";

    // Table names
    private static final String TABLE_EMPLOYEES = "employees";
    private static final String TABLE_TASKS = "tasks";
    private static final String TABLE_TASK_ASSIGNEES = "task_assignees";
    private static final String TABLE_TEAMS = "teams";

    //Employee Table Column names
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IS_MANAGER = "is_manager";

    // Tasks Table Columns names
    private static final String KEY_TASK_ID = "id";
    private static final String KEY_TASK_NAME = "name";
    private static final String KEY_DUE_DATE = "due_date";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_PRIORITY = "priority";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_STATUS = "status";

    //Task Assignees Column names
    private static final String KEY_TASK_A_ID = "task_id";
    private static final String KEY_EMPLOYEE_A_NAME = "employee_name";

    //Teams Column names
    private static final String KEY_TEAMS_MANAGER_NAME = "manager_name";
    private static final String KEY_TEAMS_MEMBER_NAME = "member_name";

    // Tables create statement
    private static final String CREATE_EMPLOYEES_TABLE = "CREATE TABLE "
            + TABLE_EMPLOYEES + "(" + KEY_USERNAME + " TEXT PRIMARY KEY,"
            + KEY_PASSWORD + " TEXT," + KEY_IS_MANAGER + " INTEGER);";
    private static final String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
            + KEY_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TASK_NAME + " TEXT," + KEY_DUE_DATE
            + " TEXT," + KEY_CATEGORY + " TEXT," + KEY_PRIORITY
            + " TEXT," + KEY_STATUS + " TEXT," + KEY_LOCATION + " TEXT);";
    private static final String CREATE_TASKS_ASSIGNEES_TABLE = "CREATE TABLE "
            + TABLE_TASK_ASSIGNEES + "(" + KEY_TASK_A_ID
            + " INTEGER," + KEY_EMPLOYEE_A_NAME + " TEXT);";
    private static final String CREATE_TEAMS_TABLE = "CREATE TABLE " +
            TABLE_TEAMS + "(" + KEY_TEAMS_MANAGER_NAME + " TEXT,"
            + KEY_TEAMS_MEMBER_NAME + " TEXT);";

    public TasKingDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EMPLOYEES_TABLE);
        db.execSQL(CREATE_TASKS_TABLE);
        db.execSQL(CREATE_TASKS_ASSIGNEES_TABLE);
        db.execSQL(CREATE_TEAMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK_ASSIGNEES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAMS);
        onCreate(db);
    }*/
}
