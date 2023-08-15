package com.example.androidtodoapp;

import android.database.Cursor;

public class UserList {

    private String name;
    private String email;
    private int password;
    private long id;
    public UserList(String name, String email, int password, long id) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getPassword() {
        return password;
    }
    public void setPassword(int password) {
        this.password = password;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    @Override
    public String toString() {
        String output = name + email + password;
        return output;
    }
    private UserList cursorToShoppingMemo(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_ID_USER);
        int idName = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_NAME);
        int idEmail = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_EMAIL);
        int idPassword = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_PASSWORD);
        String name = cursor.getString(idName);
        String email = cursor.getString(idEmail);
        int password = cursor.getInt(idPassword);
        long id = cursor.getLong(idIndex);
        UserList shoppingMemo = new UserList(name, email, password, id);
        return shoppingMemo;
    }
}
