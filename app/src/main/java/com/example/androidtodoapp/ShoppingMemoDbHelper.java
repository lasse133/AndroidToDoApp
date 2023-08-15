package com.example.androidtodoapp;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
public class ShoppingMemoDbHelper extends SQLiteOpenHelper{

    private static final String LOG_TAG = ShoppingMemoDbHelper.class.getSimpleName();

    public static final String DB_NAME = "ToDoList.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_USER = "user";

    public static final String TABLE_TASKS = "tasks";

    public static final String COLUMN_ID_TASKS = "idTask";

    public static final String COLUMN_TASKS = "task";

    public static final String COLUMN_CHECKED = "checked";
    public static final String COLUMN_ID_USER = "idUser";

    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    public static final String SQL_CREATE_USER =
            "CREATE TABLE " + TABLE_USER +
                    "(" + COLUMN_ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_EMAIL + " TEXT NOT NULL, " +
                    COLUMN_PASSWORD + " INTEGER NOT NULL);";

    public static final String SQL_CREATE_TASKS =
            "CREATE TABLE " + TABLE_TASKS +
                    "(" + COLUMN_ID_TASKS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TASKS + " TEXT NOT NULL, " +
                    COLUMN_CHECKED + " BOOLEAN NOT NULL DEFAULT 0, " + // Correct position for "checked" column
                    COLUMN_ID_USER + " INTEGER NOT NULL, " +
                    "FOREIGN KEY(" + COLUMN_ID_USER + ") REFERENCES " + TABLE_USER + "(" + COLUMN_ID_USER + "));";

    public ShoppingMemoDbHelper(Context context) {
//super(context, "PLATZHALTER_DATENBANKNAME", null, 1);
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }
    // Die onCreate-Methode wird nur aufgerufen, falls die Datenbank noch nicht existiert
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE_USER + " angelegt.");
            db.execSQL(SQL_CREATE_USER);

            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE_TASKS + " angelegt.");
            db.execSQL(SQL_CREATE_TASKS);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
            onCreate(db); // Recreate the table with the latest schema.
        }
    }

}