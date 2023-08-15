package com.example.androidtodoapp;

import android.database.Cursor;

public class TaskList {

    private String text;

    boolean checked;
    private long id;

    public TaskList(String text, boolean checked, long id) {
        this.text = text;
        this.checked = checked;
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String name) {
        this.text = text;
    }

    public boolean getChecked() {
        return checked;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String toString() {
        String output = text + checked;
        return output;
    }

    private TaskList cursorToShoppingMemo(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_ID_TASKS);
        int idText = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_TASKS);
        int idChecked = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_CHECKED);
        boolean checked = cursor.getInt(idChecked) == 0;
        String text = cursor.getString(idText);
        long id = cursor.getLong(idIndex);
        TaskList shoppingMemo = new TaskList(text,checked, id);
        return shoppingMemo;
    }
}
