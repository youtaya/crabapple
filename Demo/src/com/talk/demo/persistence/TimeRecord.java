package com.talk.demo.persistence;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;

public class TimeRecord {
	   // SQL convention says Table name should be "singular", so not Persons
    public static final String TABLE_NAME = "Record";
    // Naming the id column with an underscore is good to be consistent
    // with other Android things. This is ALWAYS needed
    public static final String COL_ID = "_id";
    // These fields can be anything you want.
    public static final String COL_TITLE = "title";
    public static final String COL_CONTENT = "content";
    public static final String COL_CREATE_TIME = "create_time";

    // For database projection so order is consistent
    public static final String[] FIELDS = { COL_ID, COL_TITLE, COL_CONTENT,
    	COL_CREATE_TIME };

    /*
     * The SQL code that creates a Table for storing Persons in.
     * Note that the last row does NOT end in a comma like the others.
     * This is a common source of error.
     */
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
            + COL_ID + " INTEGER PRIMARY KEY,"
            + COL_TITLE + " TEXT NULL '',"
            + COL_CONTENT + " TEXT NOT NULL DEFAULT '',"
            + COL_CREATE_TIME + " TEXT NOT NULL DEFAULT ''"
            + ")";

    // Fields corresponding to database columns
    public long id = -1;
    public String title = "";
    public String content = "";
    public String create_time = "";

    
    
    public TimeRecord() {
    }
    
    public TimeRecord(String v1) {
        content = v1;
        create_time = handledTime();
    }
    
    public String handledTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
        Date date = new Date();
        return dateFormat.format(date);
    }
    
    /**
     * Convert information from the database into a TimeRecord object.
     */
    public TimeRecord(final Cursor cursor) {
        // Indices expected to match order in FIELDS!
        this.id = cursor.getLong(0);
        this.title = cursor.getString(1);
        this.content = cursor.getString(2);
        this.create_time = cursor.getString(3);
    }

    /**
     * Return the fields in a ContentValues object, suitable for insertion
     * into the database.
     */
    public ContentValues getContent() {
        final ContentValues values = new ContentValues();
        // Note that ID is NOT included here
        values.put(COL_TITLE, title);
        values.put(COL_CONTENT, content);
        values.put(create_time, create_time);

        return values;
    }

}
