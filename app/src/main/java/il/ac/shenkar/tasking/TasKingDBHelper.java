package il.ac.shenkar.tasking;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class TasKingDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 23;

    // Database Name
    private static final String DATABASE_NAME = "TasKingDB";

    // Table names
    private static final String TABLE_EMPLOYEES = "employees";
    private static final String TABLE_TASKS = "tasks";

    //Employee Table Column names
    private static final String KEY_USERNAME = "username";
    private static final String KEY_UID = "uid";
    private static final String KEY_MANAGER_ID = "manager_id";

    // Tasks Table Columns names
    private static final String KEY_TASK_NAME = "name";
    private static final String KEY_TIME = "time";
    private static final String KEY_DATE = "date";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_PRIORITY = "priority";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_STATUS = "status";
    private static final String KEY_ASSIGNEE = "assignee";
    private static final String KEY_FIREBASE_ID = "firebase_id";
    private static final String KEY_PICTURE = "picture";
    private static final String KEY_ACCEPT_STATUS = "accept_status";
    private static final String KEY_TIME_STAMP = "time_stamp";
    private static final String KEY_ASSIGNEE_UID = "assignee_uid";
    private static final String KEY_MANAGER_UID = "manager_uid";

    // Tables create statement
    private static final String CREATE_EMPLOYEES_TABLE = "CREATE TABLE "
            + TABLE_EMPLOYEES + "(" + KEY_USERNAME + " TEXT," + KEY_UID + " TEXT PRIMARY KEY, "
            + KEY_MANAGER_ID + " TEXT);";
    private static final String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
            + KEY_TASK_NAME + " TEXT," + KEY_TIME + " TEXT,"+ KEY_DATE + " TEXT,"
            + KEY_CATEGORY + " TEXT," + KEY_PRIORITY + " TEXT," + KEY_STATUS + " TEXT,"
            + KEY_ASSIGNEE + " TEXT," + KEY_FIREBASE_ID + " TEXT PRIMARY KEY,"
            + KEY_PICTURE + " TEXT," + KEY_ACCEPT_STATUS + " TEXT," + KEY_TIME_STAMP + " TEXT,"
            + KEY_MANAGER_UID + " TEXT," + KEY_ASSIGNEE_UID + " TEXT," + KEY_LOCATION + " TEXT);";

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
}
