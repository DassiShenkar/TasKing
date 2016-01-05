package com.tasking.tasking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Grisha on 1/2/2016.
 */
public class TaskDAO implements ITaskDAO {

    private static TaskDAO taskDAO;
    private TasKingDBHelper DBHelper;
    private Context context;

    private TaskDAO(Context context){
        DBHelper = null;
        this.context = context;
    }

    public static TaskDAO getInstance(Context context){
        if(taskDAO == null){
            taskDAO = new TaskDAO(context);
        }
        return taskDAO;
    }


    @Override
    public void addTeamMember(TeamMember member) {
        SQLiteDatabase db = null;
        try{
            db = DBHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_NAME, member.getName());
            values.put(TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_USERNAME, member.getUserName());
            values.put(TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_PASSWORD, member.getPassword());
            db.insert(TasKingDBNames.MemberEntry.TABLE_NAME, null, values);
            db.close();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void removeTeamMember(String userName) {
        SQLiteDatabase db = null;
        try {
            db = DBHelper.getWritableDatabase();
            db.delete(TasKingDBNames.MemberEntry.TABLE_NAME,
                    TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_USERNAME + " = ?",
                    new String[]{userName});
        }finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public TeamMember getTeamMember(String userName) {
        SQLiteDatabase db = null;
        TeamMember member = null;
        try {
            db = DBHelper.getReadableDatabase();
            Cursor cursor = db.query(TasKingDBNames.MemberEntry.TABLE_NAME,
                    new String[]{TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_NAME,
                            TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_USERNAME,
                            TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_PASSWORD},
                    TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_USERNAME + "=?",
                    new String[]{userName}, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                member = new TeamMember(cursor.getString(0), cursor.getString(1), cursor.getString(2));
                cursor.close();
            }
            return member;
        }finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public ArrayList<TeamMember> getTeamMembers() {
        ArrayList<TeamMember> members;
        SQLiteDatabase db = null;
        String selectQuery = "SELECT  * FROM " + TasKingDBNames.MemberEntry.TABLE_NAME;
        try {
            db = DBHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            members = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    TeamMember member = new TeamMember();
                    member.setId(Integer.valueOf(cursor.getString(0)));
                    member.setName((cursor.getString(1)));
                    member.setUserName(cursor.getString(2));
                    member.setPassword(cursor.getString(3));
                    members.add(member);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return members;
        }finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public int updateTeamMember(TeamMember member) {
        SQLiteDatabase db = null;
        try{
            db = DBHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_NAME, member.getName());
            values.put(TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_USERNAME, member.getUserName());
            values.put(TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_PASSWORD, member.getPassword());
            db.update(TasKingDBNames.MemberEntry.TABLE_NAME,
                    values, TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_USERNAME + " = ?",
                    new String[] { String.valueOf(member.getId()) });
            db.close();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return 0;
    }

    @Override
    public void addTask(Task task, int userId) {
        SQLiteDatabase db = null;
        try{
            db = DBHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(TasKingDBNames.TaskEntry.COLUMN_TASK_USER_ID, userId);
            values.put(TasKingDBNames.TaskEntry.COLUMN_TASK_NAME, task.getName());
            values.put(TasKingDBNames.TaskEntry.COLUMN_TASK_DUE_DATE, task.getDueTime());
            values.put(TasKingDBNames.TaskEntry.COLUMN_TASK_CATEGORY, task.getCategory());
            values.put(TasKingDBNames.TaskEntry.COLUMN_TASK_PRIORITY, task.getPriority());
            values.put(TasKingDBNames.TaskEntry.COLUMN_TASK_LOCATION, task.getLocation());
            db.insert(TasKingDBNames.TaskEntry.TABLE_NAME, null, values);
            db.close();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void removeTask(String taskName) {
        SQLiteDatabase db = null;
        try {
            db = DBHelper.getWritableDatabase();
            db.delete(TasKingDBNames.TaskEntry.TABLE_NAME,
                    TasKingDBNames.TaskEntry.COLUMN_TASK_NAME + " = ?", //todo: find better search criteria
                    new String[]{taskName});
        }finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public Task getTask(String name) {
        return null;
    }

    @Override
    public ArrayList<Task> getTasks(String name) {
        return null;
    }

    @Override
    public int getTaskCount(int userID) {
        return 0;
    }

    @Override
    public int updateTask(Task task) {
        return 0;
    }

    @Override
    public ArrayList<Task> getMemberTasks(int userID) {
        return null;
    }
}
