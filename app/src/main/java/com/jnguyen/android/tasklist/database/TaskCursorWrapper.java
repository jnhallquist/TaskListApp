package com.jnguyen.android.tasklist.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.jnguyen.android.tasklist.Task;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Jennifer on 3/25/2016.
 */
public class TaskCursorWrapper extends CursorWrapper {
    public TaskCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Task getTask() {
        String uuidString = getString(getColumnIndex(TaskDbSchema.TaskTable.Cols.UUID));
        String title = getString(getColumnIndex(TaskDbSchema.TaskTable.Cols.TITLE));
        long date = getLong(getColumnIndex(TaskDbSchema.TaskTable.Cols.DATE));
        int isDone = getInt(getColumnIndex(TaskDbSchema.TaskTable.Cols.COMPLETED));
        String contact = getString(getColumnIndex(TaskDbSchema.TaskTable.Cols.CONTACT));

        Task task = new Task(UUID.fromString(uuidString));
        task.setTitle(title);
        task.setDate(new Date(date));
        task.setDone(isDone != 0);
        task.setContact(contact);

        return task;
    }
}

