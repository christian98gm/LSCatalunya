package edu.salleurl.lscatalunya.repositories.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "User.db";
    private static final String USERS_TABLE_NAME = "users";
    private static final String USERS_COLUMN_ID = "id";
    private static final String USERS_COLUMN_USER = "user";
    private static final String USERS_COLUMN_PASS = "password";
    private static final String USERS_COLUMN_NAME = "name";
    private static final String USERS_COLUMN_SURNAME = "surname";
    private static final String USERS_COLUMN_EMAIL = "email";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, version);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + USERS_TABLE_NAME +
                        "(" + USERS_COLUMN_ID + " integer primary key, " + USERS_COLUMN_USER + " text, " + USERS_COLUMN_PASS + " text, " + USERS_COLUMN_NAME + " text, " + USERS_COLUMN_SURNAME + " text, " + USERS_COLUMN_EMAIL + " text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertUser(String user, String pass, String name, String surname, String email) {

        if (!userExists(user, email)) {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(USERS_COLUMN_USER, user);
            contentValues.put(USERS_COLUMN_PASS, pass);
            contentValues.put(USERS_COLUMN_NAME, name);
            contentValues.put(USERS_COLUMN_SURNAME, surname);
            contentValues.put(USERS_COLUMN_EMAIL, email);
            sqLiteDatabase.insert(USERS_TABLE_NAME, null, contentValues);
            return true;
        }
        return false;
    }

    public boolean userExists(String user, String email){

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String[] args = {user, email};
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM " + USERS_TABLE_NAME + " WHERE " + USERS_COLUMN_USER + " =? " + " OR " + USERS_COLUMN_EMAIL + " =?", args);
        if (c.moveToFirst()) {
            return true;
        }
        return false;
    }


    public boolean userRegistered(String user, String pass) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String[] args = {user, pass};
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM " + USERS_TABLE_NAME + " WHERE " + USERS_COLUMN_USER + " =? " + " AND " + USERS_COLUMN_PASS + " =?", args);
        if (c.moveToFirst()) {
            return true;
        }
        return false;
    }
}
