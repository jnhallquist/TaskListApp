package com.jnguyen.android.tasklist.database;

/**
 * Created by Jennifer on 3/25/2016.
 */
public class TaskDbSchema {

    public static final class TaskTable {
        public static final String NAME = "tasks";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String COMPLETED = "completed";
            public static final String CONTACT = "contact";
        }
    }
}

