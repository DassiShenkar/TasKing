package il.ac.shenkar.tasking;

import android.provider.BaseColumns;


public class TasKingDBNames {

    public static final class MemberEntry implements BaseColumns {
        public static final String TABLE_NAME = "employees";
        public static final String COLUMN_EMPLOYEE_USERNAME = "username";
        public static final String COLUMN_EMPLOYEE_UID = "uid";
        public static final String COLUMN_MANAGER_ID = "manager_id";
    }

    public static final class TaskEntry implements BaseColumns{
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_TASK_NAME = "name";
        public static final String COLUMN_TASK_TIME = "time";
        public static final String COLUMN_TASK_DATE = "date";
        public static final String COLUMN_TASK_CATEGORY = "category";
        public static final String COLUMN_TASK_PRIORITY = "priority";
        public static final String COLUMN_TASK_LOCATION = "location";
        public static final String COLUMN_TASK_STATUS = "status";
        public static final String COLUMN_TASK_ASSIGNEE = "assignee";
        public static final String COLUMN_TASK_FIREBASE_ID = "firebase_id";
        public static final String COLUMN_TASK_PICTURE = "picture";
        public static final String COLUMN_TASK_ACCEPT_STATUS = "accept_status";
        public static final String COLUMN_TASK_TIME_STAMP = "time_stamp";
        public static final String COLUMN_TASK_MANAGER_ID = "manager_uid";
        public static final String COLUMN_TASK_ASSIGNEE_ID = "assignee_uid";
    }
}
