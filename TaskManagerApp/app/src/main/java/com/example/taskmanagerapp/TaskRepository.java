package com.example.taskmanagerapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {

    private SQLiteDatabase database;
    private TaskDatabaseHelper dbHelper;

    public TaskRepository(Context context) {
        dbHelper = new TaskDatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addTask(String title, String description, String dueDate) {
        ContentValues values = new ContentValues();
        values.put(TaskDatabaseHelper.COLUMN_TITLE, title);
        values.put(TaskDatabaseHelper.COLUMN_DESCRIPTION, description);
        values.put(TaskDatabaseHelper.COLUMN_DUE_DATE, dueDate);

        return database.insert(TaskDatabaseHelper.TABLE_TASKS, null, values);
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        Cursor cursor = database.query(TaskDatabaseHelper.TABLE_TASKS, null, null, null, null, null, TaskDatabaseHelper.COLUMN_DUE_DATE);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Task task = cursorToTask(cursor);
                tasks.add(task);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return tasks;
    }

    private Task cursorToTask(Cursor cursor) {
        Task task = new Task(
                cursor.getLong(cursor.getColumnIndex(TaskDatabaseHelper.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(TaskDatabaseHelper.COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndex(TaskDatabaseHelper.COLUMN_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndex(TaskDatabaseHelper.COLUMN_DUE_DATE))
        );
        return task;
    }

    public int updateTask(long id, String title, String description, String dueDate) {
        ContentValues values = new ContentValues();
        values.put(TaskDatabaseHelper.COLUMN_TITLE, title);
        values.put(TaskDatabaseHelper.COLUMN_DESCRIPTION, description);
        values.put(TaskDatabaseHelper.COLUMN_DUE_DATE, dueDate);

        return database.update(TaskDatabaseHelper.TABLE_TASKS, values, TaskDatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void deleteTask(long id) {
        database.delete(TaskDatabaseHelper.TABLE_TASKS, TaskDatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }
}
