package com.tasking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;


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
    public void addTeamMember(Employee member) {
        SQLiteDatabase db = null;
        try {
            db = DBHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_USERNAME, member.getUserName());
            values.put(TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_PASSWORD, member.getPassword());
            values.put(TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_IS_MANAGER, member.getIsManager());
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
            db.delete(TasKingDBNames.TaskAssigneesEntry.TABLE_NAME,
                    TasKingDBNames.TaskAssigneesEntry.COLUMN_EMPLOYEE_A_NAME + " = ?",
                    new String[]{userName});
            db.delete(TasKingDBNames.TeamsEntry.TABLE_NAME,
                    TasKingDBNames.TeamsEntry.COLUMN_MEMBER_NAME + " = ?",
                    new String[]{userName});
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public Employee getTeamMember(String userName) {
        SQLiteDatabase db = null;
        Employee member;
        Cursor cursor;
        try {
            db = DBHelper.getReadableDatabase();
            cursor = db.query(TasKingDBNames.MemberEntry.TABLE_NAME,
                    new String[]{TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_USERNAME,
                            TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_PASSWORD,
                            TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_IS_MANAGER},
                    TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_USERNAME + " = ?",
                    new String[]{userName}, null, null, null, null);
            if (cursor.moveToFirst()) {
                if (cursor.getString(3).equals("1")) {
                    member = new Manager(cursor.getString(1),
                            cursor.getString(2),
                            Integer.parseInt(cursor.getString(3)));
                } else {
                    member = new TeamMember(cursor.getString(1),
                            cursor.getString(2),
                            Integer.parseInt(cursor.getString(3)));
                }
                cursor.close();
                return member;
            }
            return null;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public Employee getTeamMember(String userName, String managerName) {
        SQLiteDatabase db = null;
        Employee member = null;
        Cursor cursor;
        String selectQuery = "SELECT * FROM " + TasKingDBNames.TeamsEntry.TABLE_NAME
                + " JOIN " + TasKingDBNames.MemberEntry.TABLE_NAME
                + " ON " + TasKingDBNames.TeamsEntry.TABLE_NAME + "."
                + TasKingDBNames.TeamsEntry.COLUMN_MANAGER_NAME + "="
                + TasKingDBNames.MemberEntry.TABLE_NAME + "."
                + TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_USERNAME
                + " WHERE " + TasKingDBNames.TeamsEntry.TABLE_NAME + "."
                + TasKingDBNames.TeamsEntry.COLUMN_MANAGER_NAME + " = '" + managerName
                + "' AND " + TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_IS_MANAGER
                + " = 0 AND " + TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_USERNAME +
                " = '" + userName + "'";
        try {
            db = DBHelper.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                if (cursor.getString(3).equals("1")) {
                    member = new Manager(cursor.getString(1),
                            cursor.getString(2),
                            Integer.parseInt(cursor.getString(3)));
                } else {
                    member = new TeamMember(cursor.getString(1),
                            cursor.getString(2),
                            Integer.parseInt(cursor.getString(3)));
                }
                cursor.close();
            }
            return member;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public ArrayList<Employee> getTeamMembers(String userName) {
        ArrayList<Employee> members;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TasKingDBNames.TeamsEntry.TABLE_NAME
                             + " JOIN " + TasKingDBNames.MemberEntry.TABLE_NAME
                             + " ON " + TasKingDBNames.TeamsEntry.TABLE_NAME + "."
                             + TasKingDBNames.TeamsEntry.COLUMN_MANAGER_NAME + "="
                             + TasKingDBNames.MemberEntry.TABLE_NAME + "."
                             + TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_USERNAME
                             + " WHERE " + TasKingDBNames.TeamsEntry.TABLE_NAME + "."
                             + TasKingDBNames.TeamsEntry.COLUMN_MANAGER_NAME + " = '" + userName
                             + "' AND " + TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_IS_MANAGER
                             + "= 0";
        try {
            db = DBHelper.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            members = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    TeamMember member = new TeamMember();
                    member.setUserName(cursor.getString(1));
                    member.setPassword(cursor.getString(2));
                    member.setIsManager((Integer.parseInt(cursor.getString(3))));
                    members.add(member);
                } while (cursor.moveToNext());
                return members;
            }
            return null;
        } finally {
            if (cursor != null){
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public int getMemberCount() {
        String countQuery = "SELECT  * FROM " + TasKingDBNames.MemberEntry.TABLE_NAME;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try{
            db = DBHelper.getReadableDatabase();
            cursor = db.rawQuery(countQuery, null);
            return cursor.getCount();
        } finally {
            if (db != null) {
                db.close();
            }
            if (cursor != null){
                cursor.close();
            }
        }
    }

    @Override
    public void updateTeamMember(Employee member) {
        SQLiteDatabase db = null;
    try {
        db = DBHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_USERNAME, member.getName());
        values.put(TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_PASSWORD, member.getUserName());
        values.put(TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_IS_MANAGER, member.getPassword());
        db.update(TasKingDBNames.MemberEntry.TABLE_NAME,
                values, TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_USERNAME + " = ?",
                new String[]{member.getUserName()});
        db.close();
    } finally {
        if (db != null) {
            db.close();
        }
    }
}

    @Override
    public void addTask(Task task, ArrayList<String> userNames) {
        SQLiteDatabase db = null;
        try {
            db = DBHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(TasKingDBNames.TaskEntry.COLUMN_TASK_NAME, task.getName());
            values.put(TasKingDBNames.TaskEntry.COLUMN_TASK_DUE_DATE, task.getDueDate());
            values.put(TasKingDBNames.TaskEntry.COLUMN_TASK_CATEGORY, task.getCategory());
            values.put(TasKingDBNames.TaskEntry.COLUMN_TASK_PRIORITY, task.getPriority());
            values.put(TasKingDBNames.TaskEntry.COLUMN_TASK_LOCATION, task.getLocation());
            values.put(TasKingDBNames.TaskEntry.COLUMN_TASK_STATUS, task.getStatus());
            db.insert(TasKingDBNames.TaskEntry.TABLE_NAME, null, values);
            int taskId = this.getTask(task.getId()).getId();
            for (String userName : userNames) {
                values.put(TasKingDBNames.TaskAssigneesEntry.COLUMN_EMPLOYEE_A_NAME, userName);
                values.put(TasKingDBNames.TaskAssigneesEntry.COLUMN_TASK_A_ID, taskId);
                db.insert(TasKingDBNames.TaskAssigneesEntry.TABLE_NAME, null, values);
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void removeTask(int taskId) {
        SQLiteDatabase db = null;
        try {
            db = DBHelper.getWritableDatabase();
            db.delete(TasKingDBNames.TaskEntry.TABLE_NAME,
                    TasKingDBNames.TaskEntry.COLUMN_TASK_ID + " = ?",
                    new String[]{String.valueOf(taskId)});
            db.delete(TasKingDBNames.TaskAssigneesEntry.TABLE_NAME,
                    TasKingDBNames.TaskAssigneesEntry.COLUMN_TASK_A_ID + " = ?",
                    new String[]{String.valueOf(taskId)});
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public Task getTask(int taskId) {
        SQLiteDatabase db = null;
        Task task = null;
        try {
            db = DBHelper.getReadableDatabase();
            Cursor cursor = db.query(TasKingDBNames.TaskEntry.TABLE_NAME,
                    new String[]{TasKingDBNames.TaskEntry.COLUMN_TASK_ID,
                            TasKingDBNames.TaskEntry.COLUMN_TASK_NAME,
                            TasKingDBNames.TaskEntry.COLUMN_TASK_DUE_DATE,
                            TasKingDBNames.TaskEntry.COLUMN_TASK_CATEGORY,
                            TasKingDBNames.TaskEntry.COLUMN_TASK_PRIORITY,
                            TasKingDBNames.TaskEntry.COLUMN_TASK_LOCATION,
                            TasKingDBNames.TaskEntry.COLUMN_TASK_STATUS},
                    TasKingDBNames.TaskEntry.COLUMN_TASK_ID + " = ?",
                    new String[]{String.valueOf(taskId)}, null, null, null, null);
            if (cursor != null) {
                task = new Task();
                task.setId(Integer.parseInt(cursor.getString(0)));
                task.setName(cursor.getString(1));
                task.setDueDate(cursor.getString(2));
                task.setCategory(cursor.getString(3));
                task.setPriority(cursor.getString(4));
                task.setLocation(cursor.getString(5));
                task.setStatus(cursor.getString(6));
                cursor.close();
            }
            Cursor cursor2 = null;
            if(task != null) {
                cursor2 = db.query(TasKingDBNames.TaskAssigneesEntry.TABLE_NAME,
                        new String[]{TasKingDBNames.TaskAssigneesEntry.COLUMN_TASK_A_ID},
                        TasKingDBNames.TaskAssigneesEntry.COLUMN_TASK_A_ID + " = ?",
                        new String[]{String.valueOf(task.getId())}, null, null, null, null);
            }
            ArrayList<TeamMember> assignees = new ArrayList<>();
            if(cursor2 != null) {
                while (cursor2.moveToNext()) {
                    TeamMember employee = (TeamMember) this.getTeamMember(cursor2.getString(0));
                    assignees.add(employee);
                }
                task.setAssignees(assignees);
                cursor2.close();
                return task;
            }
            return null;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public ArrayList<Task> getTasks(String username) {
        ArrayList<Task> tasks;
        SQLiteDatabase db = null;
        Cursor cursor;
        Cursor cursor2;
        //                String selectQuery = "SELECT  * FROM " + TasKingDBNames.TaskEntry.TABLE_NAME;
        String selectQuery = "SELECT * FROM " + TasKingDBNames.TaskAssigneesEntry.TABLE_NAME
                + " JOIN " + TasKingDBNames.TaskEntry.TABLE_NAME
                + " ON " + TasKingDBNames.TaskAssigneesEntry.TABLE_NAME + "."
                + TasKingDBNames.TaskAssigneesEntry.COLUMN_TASK_A_ID
                + "=" + TasKingDBNames.TaskEntry.TABLE_NAME + "."
                + TasKingDBNames.TaskEntry.COLUMN_TASK_ID
                + " WHERE " + TasKingDBNames.TaskAssigneesEntry.COLUMN_EMPLOYEE_A_NAME + "=" + username;

        try {
            db = DBHelper.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            tasks = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    Task task = new Task();
                    task.setId(Integer.parseInt(cursor.getString(0)));
                    task.setName((cursor.getString(1)));
                    task.setDueDate(cursor.getString(2));
                    task.setCategory(cursor.getString(3));
                    task.setPriority(cursor.getString(4));
                    task.setLocation(cursor.getString(5));
                    task.setStatus(cursor.getString(6));
                    cursor2 = db.query(TasKingDBNames.TaskAssigneesEntry.TABLE_NAME,
                            new String[]{TasKingDBNames.TaskAssigneesEntry.COLUMN_TASK_A_ID},
                            TasKingDBNames.TaskAssigneesEntry.COLUMN_TASK_A_ID + " = ?",
                            new String[]{String.valueOf(task.getId())}, null, null, null, null);
                    ArrayList<TeamMember> assignees = new ArrayList<>();
                    try {
                        while (cursor2.moveToNext()) {
                            TeamMember employee = (TeamMember) this.getTeamMember(cursor2.getString(0));
                            assignees.add(employee);
                        }
                    } finally {
                        cursor2.close();
                    }
                    task.setAssignees(assignees);
                    tasks.add(task);
                } while (cursor.moveToNext());
                cursor.close();
                return tasks;
            }
            return null;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public int getTaskCount(int userID) {
        String countQuery = "SELECT  * FROM " + TasKingDBNames.TaskEntry.TABLE_NAME;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try{
            db = DBHelper.getReadableDatabase();
            cursor = db.rawQuery(countQuery, null);
            return cursor.getCount();
        } finally {
            if (cursor != null){
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void updateTask(Task task, ArrayList<Employee> employees) {
        SQLiteDatabase db = null;
        try {
            db = DBHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(TasKingDBNames.TaskEntry.COLUMN_TASK_ID, task.getId());
            values.put(TasKingDBNames.TaskEntry.COLUMN_TASK_NAME, task.getName());
            values.put(TasKingDBNames.TaskEntry.COLUMN_TASK_DUE_DATE, task.getDueDate());
            values.put(TasKingDBNames.TaskEntry.COLUMN_TASK_CATEGORY, task.getCategory());
            values.put(TasKingDBNames.TaskEntry.COLUMN_TASK_PRIORITY, task.getPriority());
            values.put(TasKingDBNames.TaskEntry.COLUMN_TASK_LOCATION, task.getLocation());
            values.put(TasKingDBNames.TaskEntry.COLUMN_TASK_STATUS, task.getStatus());
            db.update(TasKingDBNames.TaskEntry.TABLE_NAME,
                    values, TasKingDBNames.TaskEntry.COLUMN_TASK_NAME + " = ?",
                    new String[]{String.valueOf(task.getId())});
            db.delete(TasKingDBNames.TaskAssigneesEntry.TABLE_NAME,
                    TasKingDBNames.TaskAssigneesEntry.COLUMN_TASK_A_ID + " = ?",
                    new String[]{String.valueOf(task.getId())});
            for(Employee assignee : employees){
                ContentValues values2 = new ContentValues();
                values2.put(TasKingDBNames.TaskAssigneesEntry.COLUMN_EMPLOYEE_A_NAME, task.getName());
                values2.put(TasKingDBNames.TaskAssigneesEntry.COLUMN_TASK_A_ID, assignee.getUserName());
                db.update(TasKingDBNames.TaskAssigneesEntry.TABLE_NAME,
                        values2, TasKingDBNames.TaskAssigneesEntry.COLUMN_TASK_A_ID + " = ?",
                        new String[]{String.valueOf(task.getId())});
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public boolean hasMembers(String name){
        String countQuery = "SELECT * FROM " + TasKingDBNames.TeamsEntry.TABLE_NAME
                            + " WHERE manager_name = '" + name + "'";
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try{
            db = DBHelper.getReadableDatabase();
            cursor = db.rawQuery(countQuery, null);
            return cursor.getCount() > 0;
        } finally {
            if (cursor != null){
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    public void addMemberManager(String member, String manager){
        SQLiteDatabase db = null;
        try {
            db = DBHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(TasKingDBNames.TeamsEntry.COLUMN_MEMBER_NAME, member);
            values.put(TasKingDBNames.TeamsEntry.COLUMN_MANAGER_NAME, manager);
            db.insert(TasKingDBNames.TeamsEntry.TABLE_NAME, null, values);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
}