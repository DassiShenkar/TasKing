package com.tasking;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Grisha on 1/5/2016.
 */
public class TasKingDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "TasKingDB";

    // Table names
    private static final String TABLE_EMPLOYEES = "employees";
    private static final String TABLE_TASKS = "tasks";
    private static final String TABLE_TASK_ASSIGNEES = "task_assignees";

    //Employee Table Column names
    private static final String KEY_EMPLOYEE_ID = "id";
    private static final String KEY_EMPLOYEE_NAME = "name";
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

    //Task Assignees Column names
    private static final String KEY_ASSIGNEES_ID = "id";
    private static final String KEY_TASK_A_NAME = "task_name";
    private static final String KEY_EMPLOYEE_A_NAME = "employee_name";


    // Tables create statement
    private static final String CREATE_EMPLOYEES_TABLE = "CREATE TABLE " + TABLE_EMPLOYEES
            + "(" + KEY_EMPLOYEE_ID + " INTEGER PRIMARY KEY," + KEY_EMPLOYEE_NAME + " TEXT,"
            + KEY_USERNAME + " TEXT," + KEY_PASSWORD
            + " TEXT" + KEY_IS_MANAGER + " INTEGER" + ")";
    private static final String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
            + KEY_TASK_ID + " INTEGER PRIMARY KEY," + KEY_TASK_NAME + " TEXT," + KEY_DUE_DATE
            + " TEXT," + KEY_CATEGORY + " TEXT," + KEY_PRIORITY
            + " TEXT," + KEY_LOCATION + " TEXT" + ")";
    private static final String CREATE_TASKS_ASSIGNEES_TABLE = "CREATE TABLE " + TABLE_TASK_ASSIGNEES + "("
            + KEY_ASSIGNEES_ID + " INTEGER PRIMARY KEY," + KEY_TASK_A_NAME + " TEXT," + KEY_EMPLOYEE_A_NAME
            + " TEXT" + ")";

    public TasKingDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EMPLOYEES_TABLE);
        db.execSQL(CREATE_TASKS_TABLE);
        db.execSQL(CREATE_TASKS_ASSIGNEES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK_ASSIGNEES);
        onCreate(db);
    }
}
