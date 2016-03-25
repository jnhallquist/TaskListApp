package com.jnguyen.android.tasklist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jennifer on 3/25/2016.
 */
public class TaskBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "taskBase.db";

    public TaskBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TaskDbSchema.TaskTable.NAME + "(" +
                        " _id integer primary key autoincrement, " +
                        TaskDbSchema.TaskTable.Cols.UUID + ", " +
                        TaskDbSchema.TaskTable.Cols.TITLE + ", " +
                        TaskDbSchema.TaskTable.Cols.DATE + ", " +
                        TaskDbSchema.TaskTable.Cols.COMPLETED + ", " +
                        TaskDbSchema.TaskTable.Cols.CONTACT +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
