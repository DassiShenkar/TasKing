package com.tasking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class TaskDAO implements ITaskDAO {
    private static TaskDAO taskDAO;
    private TasKingDBHelper DBHelper;

    private TaskDAO(Context context) {
        DBHelper = new TasKingDBHelper(context);
    }

    public static TaskDAO getInstance(Context context) {
        if (taskDAO == null) {
            taskDAO = new TaskDAO(context);
        }
        return taskDAO;
    }


    @Override
    public void addTask(Task task) {
        SQLiteDatabase db = null;
        try {
            db = DBHelper.getReadableDatabase();
            ContentValues taskValues = new ContentValues();
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_NAME, task.getName());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_DATE, task.convertDateString());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_TIME, task.convertTimeString());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_CATEGORY, task.getCategory());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_PRIORITY, task.getPriority());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_LOCATION, task.getLocation());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_STATUS, task.getStatus());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_ASSIGNEE, task.getAssignee());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_FIREBASE_ID, task.getFirebaseId());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_PICTURE, task.getPicture());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_ACCEPT_STATUS, task.getAcceptStatus());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_TIME_STAMP, new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(new Date()));
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_MANAGER_ID, task.getManagerUid());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_ASSIGNEE_ID, task.getAssigneeUid());
            db.insert(TasKingDBNames.TaskEntry.TABLE_NAME, null, taskValues);
        }
        finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void updateTask(Task task) {
        SQLiteDatabase db = null;
        try {
            db = DBHelper.getReadableDatabase();
            ContentValues taskValues = new ContentValues();
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_NAME, task.getName());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_DATE, task.convertDateString());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_TIME, task.convertTimeString());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_CATEGORY, task.getCategory());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_PRIORITY, task.getPriority());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_LOCATION, task.getLocation());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_STATUS, task.getStatus());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_ASSIGNEE, task.getAssignee());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_FIREBASE_ID, task.getFirebaseId());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_PICTURE, task.getPicture());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_ACCEPT_STATUS, task.getAcceptStatus());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_TIME_STAMP, new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(new Date()));
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_MANAGER_ID, task.getManagerUid());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_ASSIGNEE_ID, task.getAssigneeUid());
            db.update(TasKingDBNames.TaskEntry.TABLE_NAME,
                    taskValues, TasKingDBNames.TaskEntry.COLUMN_TASK_FIREBASE_ID + "=?",
                    new String[]{task.getFirebaseId()});

        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public Task getTask(String uid) {
        SQLiteDatabase db = null;
        try {
            db = DBHelper.getReadableDatabase();
            String[] columns = new String[]{TasKingDBNames.TaskEntry.COLUMN_TASK_NAME,
                    TasKingDBNames.TaskEntry.COLUMN_TASK_TIME, TasKingDBNames.TaskEntry.COLUMN_TASK_DATE,
                    TasKingDBNames.TaskEntry.COLUMN_TASK_CATEGORY, TasKingDBNames.TaskEntry.COLUMN_TASK_PRIORITY,
                    TasKingDBNames.TaskEntry.COLUMN_TASK_LOCATION, TasKingDBNames.TaskEntry.COLUMN_TASK_STATUS,
                    TasKingDBNames.TaskEntry.COLUMN_TASK_ASSIGNEE, TasKingDBNames.TaskEntry.COLUMN_TASK_FIREBASE_ID,
                    TasKingDBNames.TaskEntry.COLUMN_TASK_PICTURE, TasKingDBNames.TaskEntry.COLUMN_TASK_ACCEPT_STATUS,
                    TasKingDBNames.TaskEntry.COLUMN_TASK_TIME_STAMP, TasKingDBNames.TaskEntry.COLUMN_TASK_MANAGER_ID,
                    TasKingDBNames.TaskEntry.COLUMN_TASK_ASSIGNEE_ID};
            String[] selectionArgs = new String[]{ uid };
            Cursor cursor = db.query(TasKingDBNames.TaskEntry.TABLE_NAME, columns,
                                TasKingDBNames.TaskEntry.COLUMN_TASK_FIREBASE_ID + "=?",
                                selectionArgs , null, null, null, null);
            Task task = new Task();
            if(cursor.moveToFirst()) {
                task.setName(cursor.getString(0));
                task.convertDateFromString(cursor.getString(1), cursor.getString(2));
                task.setCategory(cursor.getString(3));
                task.setPriority(cursor.getString(4));
                task.setLocation(cursor.getString(5));
                task.setStatus(cursor.getString(6));
                task.setAssignee(cursor.getString(7));
                task.setFirebaseId(cursor.getString(8));
                task.setPicture(cursor.getString(9));
                task.setAcceptStatus(cursor.getString(10));
                task.setTimeStamp(cursor.getString(11));
                task.setManagerUid(cursor.getString(12));
                task.setAssigneeUid(cursor.getString(13));
            }
            cursor.close();
            return task;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public ArrayList<Task> getTasks(String uid, boolean isManager) {
        SQLiteDatabase db = null;
        ArrayList<Task> tasks = new ArrayList<>();
        String where;
        if(isManager){
            where = TasKingDBNames.TaskEntry.COLUMN_TASK_MANAGER_ID;
        }
        else{
            where = TasKingDBNames.TaskEntry.COLUMN_TASK_ASSIGNEE_ID;
        }
        try {
            db = DBHelper.getReadableDatabase();
            String[] columns = new String[]{TasKingDBNames.TaskEntry.COLUMN_TASK_NAME,
                    TasKingDBNames.TaskEntry.COLUMN_TASK_TIME, TasKingDBNames.TaskEntry.COLUMN_TASK_DATE,
                    TasKingDBNames.TaskEntry.COLUMN_TASK_CATEGORY, TasKingDBNames.TaskEntry.COLUMN_TASK_PRIORITY,
                    TasKingDBNames.TaskEntry.COLUMN_TASK_LOCATION, TasKingDBNames.TaskEntry.COLUMN_TASK_STATUS,
                    TasKingDBNames.TaskEntry.COLUMN_TASK_ASSIGNEE, TasKingDBNames.TaskEntry.COLUMN_TASK_FIREBASE_ID,
                    TasKingDBNames.TaskEntry.COLUMN_TASK_PICTURE, TasKingDBNames.TaskEntry.COLUMN_TASK_ACCEPT_STATUS,
                    TasKingDBNames.TaskEntry.COLUMN_TASK_TIME_STAMP, TasKingDBNames.TaskEntry.COLUMN_TASK_MANAGER_ID,
                    TasKingDBNames.TaskEntry.COLUMN_TASK_ASSIGNEE_ID};
            String[] selectionArgs = new String[]{ uid };
            Cursor cursor = db.query(TasKingDBNames.TaskEntry.TABLE_NAME, columns,
                            where + "=?", selectionArgs, null, null, null, null);
            if(cursor.moveToFirst()){
                do{
                    Task task = new Task();
                    task.setName(cursor.getString(0));
                    task.convertDateFromString(cursor.getString(1), cursor.getString(2));
                    task.setCategory(cursor.getString(3));
                    task.setPriority(cursor.getString(4));
                    task.setLocation(cursor.getString(5));
                    task.setStatus(cursor.getString(6));
                    task.setAssignee(cursor.getString(7));
                    task.setFirebaseId(cursor.getString(8));
                    task.setPicture(cursor.getString(9));
                    task.setAcceptStatus(cursor.getString(10));
                    task.setTimeStamp(cursor.getString(11));
                    task.setManagerUid(cursor.getString(12));
                    task.setAssigneeUid(cursor.getString(13));
                    tasks.add(task);
                } while(cursor.moveToNext());
                cursor.close();
            }
            return tasks;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void removeTask(String taskUid) {
        SQLiteDatabase db = null;
        try {
            db = DBHelper.getReadableDatabase();
            db.delete(TasKingDBNames.TaskEntry.TABLE_NAME, TasKingDBNames.TaskEntry.COLUMN_TASK_FIREBASE_ID + " =?",
                    new String[]{taskUid});
        }
        finally {
            if (db != null) {
                db.close();
            }
        }
    }


    @Override
    public void addMember(Employee employee, String uid, String mUid) {
        SQLiteDatabase db = null;
        try {
            db = DBHelper.getReadableDatabase();
            ContentValues teamValues = new ContentValues();
            teamValues.put(TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_USERNAME, employee.getUsername());
            teamValues.put(TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_UID, uid);
            teamValues.put(TasKingDBNames.MemberEntry.COLUMN_MANAGER_ID, mUid);
            db.insert(TasKingDBNames.MemberEntry.TABLE_NAME, null, teamValues);
        }
        finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void removeMember(Employee employee) {
        SQLiteDatabase db = null;
        try {
            db = DBHelper.getReadableDatabase();
            db.delete(TasKingDBNames.MemberEntry.TABLE_NAME, TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_USERNAME + " = ?",
                    new String[]{employee.getUsername()});
        }
        finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public ArrayList<Employee> getMembers(String uid) {
        SQLiteDatabase db = null;
        ArrayList<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM " + TasKingDBNames.MemberEntry.TABLE_NAME + " WHERE manager_id = '" + uid + "'";
        try {
            db = DBHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst()){
                do{
                    Employee employee = new Employee();
                    employee.setUsername(cursor.getString(0));
                    employee.setUid(cursor.getString(1));
                    employee.setManagerId(cursor.getString(2));
                    employees.add(employee);
                } while(cursor.moveToNext());
                cursor.close();
            }
            return employees;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
}