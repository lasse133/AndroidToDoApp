package com.example.androidtodoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ShoppingMemoDataSource {
    private static final String LOG_TAG = ShoppingMemoDataSource.class.getSimpleName();
    private SQLiteDatabase database;
    private ShoppingMemoDbHelper dbHelper;
    private String[] columnsUser = {
            ShoppingMemoDbHelper.COLUMN_ID_USER,
            ShoppingMemoDbHelper.COLUMN_NAME,
            ShoppingMemoDbHelper.COLUMN_EMAIL,
            ShoppingMemoDbHelper.COLUMN_PASSWORD
    };
    private String[] columnsTask = {
            ShoppingMemoDbHelper.COLUMN_ID_TASKS,
            ShoppingMemoDbHelper.COLUMN_CHECKED,
            ShoppingMemoDbHelper.COLUMN_TASKS
    };


    public ShoppingMemoDataSource(Context context) {
        Log.d(LOG_TAG, "Our DataSource is creating the dbHelper.");
        dbHelper = new ShoppingMemoDbHelper(context);
    }

    public void open() {
        Log.d(LOG_TAG, "Requesting a reference to the database.");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Database reference obtained. Database path: " + database.getPath());
    }

    public void close() {
        dbHelper.close();
        Log.d(LOG_TAG, "Database closed using the DbHelper.");
    }

    public UserList createUser(String name, String email, int password) {
        ContentValues values = new ContentValues();
        values.put(ShoppingMemoDbHelper.COLUMN_NAME, name);
        values.put(ShoppingMemoDbHelper.COLUMN_EMAIL, email);
        values.put(ShoppingMemoDbHelper.COLUMN_PASSWORD, password);
        long insertId = database.insert(ShoppingMemoDbHelper.TABLE_USER, null, values);
        Cursor cursor = database.query(ShoppingMemoDbHelper.TABLE_USER,
                columnsUser, ShoppingMemoDbHelper.COLUMN_ID_USER + "=" + insertId,
                null, null, null, null);
        cursor.moveToFirst();
        UserList userList = cursorToUserList(cursor);
        cursor.close();
        return userList;
    }

    public TaskList createTask(String itemText, boolean checked, long userID) {
        ContentValues values = new ContentValues();
        values.put(ShoppingMemoDbHelper.COLUMN_TASKS, itemText);
        values.put(ShoppingMemoDbHelper.COLUMN_CHECKED, checked); // Convert boolean to 1 or 0 for INTEGER storage
        values.put(ShoppingMemoDbHelper.COLUMN_ID_USER, userID);
        long insertId = database.insert(ShoppingMemoDbHelper.TABLE_TASKS, null, values);
        Cursor cursor = database.query(ShoppingMemoDbHelper.TABLE_TASKS,
                columnsTask, ShoppingMemoDbHelper.COLUMN_ID_TASKS + "=" + insertId,
                null, null, null, null);
        cursor.moveToFirst();
        TaskList taskList = cursorToTaskList(cursor);
        cursor.close();
        return taskList;
    }

    public void deleteUserList(UserList shoppingMemo) {
        long id = shoppingMemo.getId();
        database.delete(ShoppingMemoDbHelper.TABLE_USER,
                ShoppingMemoDbHelper.COLUMN_ID_USER + "=" + id,
                null);
        Log.d(LOG_TAG, "Entry deleted! ID: " + id + ", Content: " + shoppingMemo.toString());
    }

    private UserList cursorToUserList(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_ID_USER);
        int idName = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_NAME);
        int idEmail = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_EMAIL);
        int idPassword = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_PASSWORD);
        String name = cursor.getString(idName);
        String email = cursor.getString(idEmail);
        int password = cursor.getInt(idPassword);
        long id = cursor.getLong(idIndex);
        UserList userList = new UserList(name, email, password, id);
        return userList;
    }

    public TaskList cursorToTaskList(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_ID_TASKS);
        int idTask = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_TASKS);
        int idChecked = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_CHECKED);
        String task = cursor.getString(idTask);
        boolean checked = cursor.getInt(idChecked) == 0;
        long id = cursor.getLong(idIndex);
        TaskList taskList = new TaskList(task, checked, id);
        return taskList;
    }

    public List<UserList> getAllUsers() {
        List<UserList> UserList = new ArrayList<>();
        Cursor cursor = database.query(ShoppingMemoDbHelper.TABLE_USER,
                columnsUser, null, null, null, null, null);
        cursor.moveToFirst();
        UserList userList;
        while(!cursor.isAfterLast()) {
            userList = cursorToUserList(cursor);
            UserList.add(userList);
            Log.d(LOG_TAG, "ID: " + userList.getId() + ", Inhalt: " + userList.toString());
            cursor.moveToNext();
        }
        cursor.close();
        return UserList;
    }
    public ArrayList<TaskList> getAllTasks() {
        ArrayList<TaskList> TaskList = new ArrayList<>();
        Cursor cursor = database.query(ShoppingMemoDbHelper.TABLE_TASKS,
                columnsTask, null, null, null, null, null);
        cursor.moveToFirst();
        TaskList taskList;
        while(!cursor.isAfterLast()) {
            taskList = cursorToTaskList(cursor);
            TaskList.add(taskList);
            Log.d(LOG_TAG, "ID: " + taskList.getId() + ", Inhalt: " + taskList.toString());
            cursor.moveToNext();
        }
        cursor.close();
        return TaskList;
    }

    public boolean checkLoginCredentials(String email, String password) {
        String selection = ShoppingMemoDbHelper.COLUMN_EMAIL + " = ? AND " + ShoppingMemoDbHelper.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, String.valueOf(password)};

        Cursor cursor = database.query(
                ShoppingMemoDbHelper.TABLE_USER,
                columnsUser,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean loginSuccessful = cursor.moveToFirst();
        cursor.close();

        return loginSuccessful;
    }
    public long getCurrentUserID(String email, String password) {
        String selection = ShoppingMemoDbHelper.COLUMN_EMAIL + " = ? AND " + ShoppingMemoDbHelper.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = database.query(
                ShoppingMemoDbHelper.TABLE_USER,
                columnsUser,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        long userID = -1;  // Default value if no matching user is found

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_ID_USER);
            userID = cursor.getLong(idIndex);
        }

        cursor.close();

        return userID;
    }

    public ArrayList<TaskList> getTasksForUser(long userID) {
        ArrayList<TaskList> taskList = new ArrayList<>();
        String selection = ShoppingMemoDbHelper.COLUMN_ID_USER + " = ?";
        String[] selectionArgs = {String.valueOf(userID)};

        Cursor cursor = database.query(
                ShoppingMemoDbHelper.TABLE_TASKS,
                columnsTask,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TaskList task = cursorToTaskList(cursor);
            taskList.add(task);
            cursor.moveToNext();
        }

        cursor.close();
        return taskList;
    }

    public void deleteTask(String task) {
        database.delete(ShoppingMemoDbHelper.TABLE_TASKS,
                ShoppingMemoDbHelper.COLUMN_TASKS + " = ?",
                new String[]{task});
        Log.d(LOG_TAG, "Task deleted: " + task);
    }
}
