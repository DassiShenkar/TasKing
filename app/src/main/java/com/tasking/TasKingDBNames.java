package com.tasking;

import android.provider.BaseColumns;

/**
 * Created by Grisha on 1/5/2016.
 */
public class TasKingDBNames {
    public static final class MemberEntry implements BaseColumns {
        public static final String TABLE_NAME = "employees";
        public static final String COLUMN_EMPLOYEE_ID = "id";
        public static final String COLUMN_EMPLOYEE_NAME = "name";
        public static final String COLUMN_EMPLOYEE_USERNAME = "username";
        public static final String COLUMN_EMPLOYEE_PASSWORD = "password";
        public static final String COLUMN_EMPLOYEE_IS_MANAGER = "is_manager";
    }
    public static final class TaskEntry implements BaseColumns{
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_TASK_ID = "id";
        public static final String COLUMN_TASK_USER_ID = "user_id";
        public static final String COLUMN_TASK_NAME = "name";
        public static final String COLUMN_TASK_DUE_DATE = "due_date";
        public static final String COLUMN_TASK_CATEGORY = "category";
        public static final String COLUMN_TASK_PRIORITY = "priority";
        public static final String COLUMN_TASK_LOCATION = "location";
    }
}
