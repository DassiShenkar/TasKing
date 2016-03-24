package com.tasking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;


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
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_DATE, task.getDateString());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_TIME, task.getTimeString());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_CATEGORY, task.getCategory());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_PRIORITY, task.getPriority());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_LOCATION, task.getLocation());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_STATUS, task.getStatus());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_ASSIGNEE, task.getAssignee());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_FIREBASE_ID, task.getFirebaseId());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_PICTURE, task.getPicture());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_ACCEPT_STATUS, task.getAcceptStatus());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_TIME_STAMP, new Date().toString());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_USER_ID, task.getUserId());
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
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_DATE, task.getDateString());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_TIME, task.getTimeString());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_CATEGORY, task.getCategory());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_PRIORITY, task.getPriority());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_LOCATION, task.getLocation());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_STATUS, task.getStatus());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_ASSIGNEE, task.getAssignee());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_FIREBASE_ID, task.getFirebaseId());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_PICTURE, task.getPicture());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_ACCEPT_STATUS, task.getAcceptStatus());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_TIME_STAMP, new Date().toString());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_USER_ID, task.getUserId());
            db.update(TasKingDBNames.TaskEntry.TABLE_NAME,
                    taskValues, TasKingDBNames.TaskEntry.COLUMN_TASK_ID + " = ?",
                    new String[]{String.valueOf(task.getId())});

        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public Task getTask(int id) {
        SQLiteDatabase db = null;
        try {
            db = DBHelper.getReadableDatabase();
            Cursor cursor = db.query(TasKingDBNames.TaskEntry.TABLE_NAME,
                                new String[]{TasKingDBNames.TaskEntry.COLUMN_TASK_ID, TasKingDBNames.TaskEntry.COLUMN_TASK_NAME,
                                        TasKingDBNames.TaskEntry.COLUMN_TASK_TIME, TasKingDBNames.TaskEntry.COLUMN_TASK_DATE,
                                        TasKingDBNames.TaskEntry.COLUMN_TASK_CATEGORY, TasKingDBNames.TaskEntry.COLUMN_TASK_PRIORITY,
                                        TasKingDBNames.TaskEntry.COLUMN_TASK_LOCATION, TasKingDBNames.TaskEntry.COLUMN_TASK_STATUS,
                                        TasKingDBNames.TaskEntry.COLUMN_TASK_ASSIGNEE, TasKingDBNames.TaskEntry.COLUMN_TASK_FIREBASE_ID,
                                        TasKingDBNames.TaskEntry.COLUMN_TASK_PICTURE, TasKingDBNames.TaskEntry.COLUMN_TASK_ACCEPT_STATUS,
                                        TasKingDBNames.TaskEntry.COLUMN_TASK_TIME_STAMP, TasKingDBNames.TaskEntry.COLUMN_TASK_USER_ID},
                                        TasKingDBNames.TaskEntry.COLUMN_TASK_ID + "=?",
                                        new String[]{ String.valueOf(id) }, null, null, null, null);
            if(cursor != null){
                cursor.moveToFirst();
            }
            Task task = new Task();
            if(cursor != null) {
                task.setId(Integer.parseInt(cursor.getString(0)));
                task.setName(cursor.getString(1));
                task.setDateFromString(cursor.getString(2), cursor.getString(3));
                task.setCategory(cursor.getString(4));
                task.setPriority(cursor.getString(5));
                task.setLocation(cursor.getString(6));
                task.setStatus(cursor.getString(7));
                task.setAssignee(cursor.getString(8));
                task.setFirebaseId(cursor.getString(9));
                task.setPicture(cursor.getString(10));
                task.setAcceptStatus(cursor.getString(11));
                task.setTimeStamp(cursor.getString(12));
                task.setUserId(cursor.getString(13));
            }
            if(cursor != null) {
                cursor.close();
            }
            return task;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public ArrayList<Task> getTasks() {
        SQLiteDatabase db = null;
        ArrayList<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM " + TasKingDBNames.TaskEntry.TABLE_NAME;
        try {
            db = DBHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst()){
                do{
                    Task task = new Task();
                    task.setId(Integer.parseInt(cursor.getString(0)));
                    task.setName(cursor.getString(1));
                    task.setDateFromString(cursor.getString(2), cursor.getString(3));
                    task.setCategory(cursor.getString(4));
                    task.setPriority(cursor.getString(5));
                    task.setStatus(cursor.getString(6));
                    task.setAssignee(cursor.getString(7));
                    task.setFirebaseId(cursor.getString(8));
                    task.setPicture(cursor.getString(9));
                    task.setAcceptStatus(cursor.getString(10));
                    task.setTimeStamp(cursor.getString(11));
                    task.setUserId(cursor.getString(12));
                    task.setLocation(cursor.getString(13));
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
    public void addMember(Employee employee) {
        SQLiteDatabase db = null;
        try {
            db = DBHelper.getReadableDatabase();
            ContentValues teamValues = new ContentValues();
            teamValues.put(TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_USERNAME, employee.getUserName());
            teamValues.put(TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_UID, employee.getUid());
            teamValues.put(TasKingDBNames.MemberEntry.COLUMN_MANAGER_ID, employee.getManagerId());
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
                    new String[]{employee.getUserName()});
        }
        finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public ArrayList<Employee> getMembers() {
        SQLiteDatabase db = null;
        ArrayList<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM " + TasKingDBNames.MemberEntry.TABLE_NAME;
        try {
            db = DBHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst()){
                do{
                    Employee employee = new Employee();
                    employee.setUserName(cursor.getString(0));
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


    public Employee getMember(String employeeEmail) {
        SQLiteDatabase db = null;
        String query = "SELECT * FROM " + TasKingDBNames.MemberEntry.TABLE_NAME;
        Employee employee = new Employee();
        try {
            db = DBHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst()){
                do{
                    if(cursor.getString(0).equals(employeeEmail)){
                        employee.setUserName(cursor.getString(0));
                        employee.setUid(cursor.getString(1));
                        employee.setManagerId(cursor.getString(2));
                        employee.setUid(cursor.getString(1));
                        break;
                    }


                } while(cursor.moveToNext());
                cursor.close();
            }
            return employee;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /*
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
                if (cursor.getString(2).equals("1")) {
                    member = new Manager(cursor.getString(0),
                            cursor.getString(1),
                            Integer.parseInt(cursor.getString(2)));
                } else {
                    member = new TeamMember(cursor.getString(0),
                            cursor.getString(1),
                            Integer.parseInt(cursor.getString(2)));
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
    public ArrayList<String> getTeamMembers(String userName) {
        ArrayList<String> members;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String selectQuery = "SELECT " + TasKingDBNames.TeamsEntry.COLUMN_MEMBER_NAME
                             + " FROM " + TasKingDBNames.TeamsEntry.TABLE_NAME
                             + " WHERE " + TasKingDBNames.TeamsEntry.TABLE_NAME + "."
                             + TasKingDBNames.TeamsEntry.COLUMN_MANAGER_NAME + " = '"
                             + userName + "'";
        try {
            db = DBHelper.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            members = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    String member = cursor.getString(0);
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
        values.put(TasKingDBNames.MemberEntry.COLUMN_EMPLOYEE_USERNAME, member.getUserName());
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
    public void addTask(Task task, String managerName, String userName) {
        SQLiteDatabase db = null;
        try {
            db = DBHelper.getReadableDatabase();
            ContentValues taskValues = new ContentValues();
            ContentValues managerValues = new ContentValues();
            ContentValues memberValues = new ContentValues();
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_NAME, task.getName());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_DUE_DATE, task.getDueDate());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_CATEGORY, task.getCategory());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_PRIORITY, task.getPriority());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_LOCATION, task.getLocation());
            taskValues.put(TasKingDBNames.TaskEntry.COLUMN_TASK_STATUS, task.getStatus());
            long id = db.insert(TasKingDBNames.TaskEntry.TABLE_NAME, null, taskValues);
            managerValues.put(TasKingDBNames.TaskAssigneesEntry.COLUMN_EMPLOYEE_A_NAME, managerName);
            managerValues.put(TasKingDBNames.TaskAssigneesEntry.COLUMN_TASK_A_ID, id);
            db.insert(TasKingDBNames.TaskAssigneesEntry.TABLE_NAME, null, managerValues);
            memberValues.put(TasKingDBNames.TaskAssigneesEntry.COLUMN_EMPLOYEE_A_NAME, userName);
            memberValues.put(TasKingDBNames.TaskAssigneesEntry.COLUMN_TASK_A_ID, id);
            db.insert(TasKingDBNames.TaskAssigneesEntry.TABLE_NAME, null, memberValues);
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
            if (cursor.moveToFirst()) {
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
            TeamMember assignee = null;
            if(cursor2 != null) {
                if (cursor2.moveToFirst()) {
                    assignee = (TeamMember) this.getTeamMember(cursor2.getString(0));
                }
                task.setAssignees(assignee);
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
        String selectQuery = "SELECT " + TasKingDBNames.TaskEntry.COLUMN_TASK_ID + ", "
                + TasKingDBNames.TaskEntry.COLUMN_TASK_NAME + ", "
                + TasKingDBNames.TaskEntry.COLUMN_TASK_DUE_DATE + ", "
                + TasKingDBNames.TaskEntry.COLUMN_TASK_CATEGORY + ", "
                + TasKingDBNames.TaskEntry.COLUMN_TASK_PRIORITY + ", "
                + TasKingDBNames.TaskEntry.COLUMN_TASK_LOCATION + ", "
                + TasKingDBNames.TaskEntry.COLUMN_TASK_STATUS
                + " FROM " + TasKingDBNames.TaskAssigneesEntry.TABLE_NAME
                + " JOIN " + TasKingDBNames.TaskEntry.TABLE_NAME
                + " ON " + TasKingDBNames.TaskAssigneesEntry.TABLE_NAME + "."
                + TasKingDBNames.TaskAssigneesEntry.COLUMN_TASK_A_ID
                + "=" + TasKingDBNames.TaskEntry.TABLE_NAME + "."
                + TasKingDBNames.TaskEntry.COLUMN_TASK_ID
                + " WHERE " + TasKingDBNames.TaskAssigneesEntry.COLUMN_EMPLOYEE_A_NAME + "='" + username + "'";

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
                    TeamMember assignee = null;
                    try {
                        if(cursor2.moveToFirst()) {
                            assignee = (TeamMember) this.getTeamMember(cursor2.getString(0));
                        }
                    } finally {
                        cursor2.close();
                    }
                    task.setAssignees(assignee);
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
    public void addMemberTask(Employee employee, int taskId){

    }*/
}