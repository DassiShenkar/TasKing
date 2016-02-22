package com.tasking;

import android.provider.BaseColumns;


public class TasKingDBNames {
    public static final class MemberEntry implements BaseColumns {
        public static final String TABLE_NAME = "employees";
        public static final String COLUMN_EMPLOYEE_ID = "id";
        public static final String COLUMN_EMPLOYEE_USERNAME = "username";
        public static final String COLUMN_EMPLOYEE_PASSWORD = "password";
        public static final String COLUMN_EMPLOYEE_IS_MANAGER = "is_manager";
    }
    public static final class TaskEntry implements BaseColumns{
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_TASK_ID = "id";
        public static final String COLUMN_TASK_NAME = "name";
        public static final String COLUMN_TASK_DUE_DATE = "due_date";
        public static final String COLUMN_TASK_CATEGORY = "category";
        public static final String COLUMN_TASK_PRIORITY = "priority";
        public static final String COLUMN_TASK_LOCATION = "location";
    }
    public static final class TaskAssigneesEntry implements BaseColumns{
        public static final String TABLE_NAME = "task_assignees";
        public static final String COLUMN_ASSIGNES_ID = "id";
        public static final String COLUMN_TASK_A_NAME = "task id";
        public static final String COLUMN_EMPLOYEE_A_NAME = "employee_id";
    }
    public static final class TeamsEntry implements BaseColumns{
        public static final String TABLE_NAME = "teams";
        public static final String COLUMN_MANAGER_NAME = "manager_name";
        public static final String COLUMN_MEMBER_NAME = "member_name";
    }
}
