package com.karashily.apt_android_assi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.karashily.apt_android_assi.Reminder;


public class RemindersDbAdapter {

    //these are the column names
    public static final String COL_ID = "_id";
    public static final String COL_CONTENT = "content";
    public static final String COL_IMPORTANT = "important";
    //these are the corresponding indices
    public static final int INDEX_ID = 0;
    public static final int INDEX_CONTENT = INDEX_ID + 1;
    public static final int INDEX_IMPORTANT = INDEX_ID + 2;
    //used for logging
    private static final String TAG = "RemindersDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private static final String DATABASE_NAME = "dba_remdrs";
    private static final String TABLE_NAME = "tbl_remdrs";
    private static final int DATABASE_VERSION = 1;
    private final Context mCtx;
    //SQL statement used to create the database
    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + TABLE_NAME + " ( " +
                    COL_ID + " INTEGER PRIMARY KEY autoincrement, " +
                    COL_CONTENT + " TEXT, " +
                    COL_IMPORTANT + " INTEGER );";


    public RemindersDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }
    //open
    public void open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
    }
    //close
    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }


    //TODO implement the function createReminder() which take the name as the content of the dbAdapter and boolean important...note that the id will be created for you automatically
    public void createReminder(String name, int important) {
      open ();
      ContentValues contentValues = new ContentValues();
      contentValues.put(COL_CONTENT,name);
      contentValues.put(COL_IMPORTANT,important);
      long result = mDb.insert(TABLE_NAME,null ,contentValues);
      close();
    }
    //TODO overloaded to take a dbAdapter
    public long createReminder(Reminder reminder) {
        open ();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CONTENT,reminder.getContent());
        contentValues.put(COL_IMPORTANT,reminder.getImportant());
        long result = mDb.insert(TABLE_NAME,null ,contentValues);
        close();
        return result;
    }

    //TODO implement the function fetchReminderById() to get a certain dbAdapter given its id
    public Reminder fetchReminderById(int id) {
        open();
        Cursor  res = mDb.rawQuery("select * from "+TABLE_NAME+"where _id =?"+String.valueOf(id),null);
        res.moveToFirst();
        int returnedId = res.getInt(0);
        String comment = res.getString(1);
        int status = res.getInt(2);
        Reminder reminder = new Reminder(returnedId,comment,status);
        close();
        return reminder;
    }


    //TODO implement the function fetchAllReminders() which get all reminders
    public Cursor fetchAllReminders() {
        open();
        Cursor res = mDb.rawQuery("select * from "+TABLE_NAME,null);
        close();
        return res;

    }

    //TODO implement the function updateReminder() to update a certain dbAdapter
    public void updateReminder(Reminder reminder) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CONTENT,reminder.getContent());
        contentValues.put(COL_IMPORTANT,reminder.getImportant());
        mDb.update(TABLE_NAME, contentValues, "_id = ?",new String[] { String.valueOf(reminder.getId()) });
        close();
    }
    //TODO implement the function deleteReminderById() to delete a certain dbAdapter given its id
    public void deleteReminderById(int nId) {
        open();
        mDb.delete(TABLE_NAME, "_id = ?",new String[] {String.valueOf(nId)});
        close();
    }

    //TODO implement the function deleteAllReminders() to delete all reminders
    public void deleteAllReminders() {
       open();
        mDb.execSQL("delete from "+ TABLE_NAME);
        close();
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }


}
