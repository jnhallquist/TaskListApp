package com.jnguyen.android.tasklist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jnguyen.android.tasklist.database.TaskBaseHelper;
import com.jnguyen.android.tasklist.database.TaskCursorWrapper;
import com.jnguyen.android.tasklist.database.TaskDbSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Jennifer on 3/24/2016.
 */
public class TaskStore {
    private static TaskStore sTaskStore;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static TaskStore get(Context context) {
        if (sTaskStore == null) {
            sTaskStore = new TaskStore(context);
        }
        return sTaskStore;
    }

    private TaskStore(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new TaskBaseHelper(mContext)
                .getWritableDatabase();
    }

    public void addTask(Task t) {
        ContentValues values = getContentValues(t);

        mDatabase.insert(TaskDbSchema.TaskTable.NAME, null, values);
    }

    public void removeTask(UUID taskId) {
        String uuidString = taskId.toString();
        mDatabase.delete(TaskDbSchema.TaskTable.NAME,
                TaskDbSchema.TaskTable.Cols.UUID + " = ?", new String[]{uuidString});
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();

        TaskCursorWrapper cursor = queryTasks(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                tasks.add(cursor.getTask());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();;
        }

        return tasks;
    }

    public Task getTask(UUID id) {
        TaskCursorWrapper cursor = queryTasks(
                TaskDbSchema.TaskTable.Cols.UUID + " = ?",
                new String[] {id.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getTask();
        } finally {
            cursor.close();
        }
    }

    public void updateTask(Task task) {
        String uuidString = task.getId().toString();
        ContentValues values = getContentValues(task);

        mDatabase.update(TaskDbSchema.TaskTable.NAME, values,
                TaskDbSchema.TaskTable.Cols.UUID + " = ?",
                new String[] { uuidString});
    }

    private static ContentValues getContentValues(Task task) {
        ContentValues values = new ContentValues();
        values.put(TaskDbSchema.TaskTable.Cols.UUID, task.getId().toString());
        values.put(TaskDbSchema.TaskTable.Cols.TITLE, task.getTitle());
        values.put(TaskDbSchema.TaskTable.Cols.DATE, task.getDate().getTime());
        values.put(TaskDbSchema.TaskTable.Cols.COMPLETED, task.isDone() ? 1 : 0);
        values.put(TaskDbSchema.TaskTable.Cols.CONTACT, task.getContact());

        return values;
    }

    private TaskCursorWrapper queryTasks(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                TaskDbSchema.TaskTable.NAME,
                null, // select all columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having
                null  // orderBy
        );

        return new TaskCursorWrapper(cursor);
    }
}
