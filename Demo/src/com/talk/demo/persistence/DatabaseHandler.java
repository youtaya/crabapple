
package com.talk.demo.persistence;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static DatabaseHandler singleton;

	public static DatabaseHandler getInstance(final Context context) {
		if (singleton == null) {
			singleton = new DatabaseHandler(context);
		}
		return singleton;
	}

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "providerRecord";

	private final Context context;

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// Good idea to have the context that doesn't die with the window
		this.context = context.getApplicationContext();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TimeRecord.CREATE_TABLE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public synchronized TimeRecord getTimeRecord(final long id) {
		final SQLiteDatabase db = this.getReadableDatabase();
		final Cursor cursor = db.query(TimeRecord.TABLE_NAME, TimeRecord.FIELDS,
				TimeRecord.COL_ID + " IS ?", new String[] { String.valueOf(id) },
				null, null, null, null);
		if (cursor == null || cursor.isAfterLast()) {
			return null;
		}

		TimeRecord item = null;
		if (cursor.moveToFirst()) {
			item = new TimeRecord(cursor);
		}
		cursor.close();
		return item;
	}

	public synchronized boolean putTimeRecord(final TimeRecord tr) {
		boolean success = false;
		int result = 0;
		final SQLiteDatabase db = this.getWritableDatabase();

		if (tr.id > -1) {
			result += db.update(TimeRecord.TABLE_NAME, tr.getContent(),
					TimeRecord.COL_ID + " IS ?",
					new String[] { String.valueOf(tr.id) });
		}

		if (result > 0) {
			success = true;
		} else {
			// Update failed or wasn't possible, insert instead
			final long id = db.insert(TimeRecord.TABLE_NAME, null,
					tr.getContent());

			if (id > -1) {
				tr.id = id;
				success = true;
			}
		}

		if (success) {
			notifyProviderOnTimeRecordChange();
		}

		return success;
	}

	public synchronized int removeTimeRecord(final TimeRecord tr) {
		final SQLiteDatabase db = this.getWritableDatabase();
		final int result = db.delete(TimeRecord.TABLE_NAME,
				TimeRecord.COL_ID + " IS ?",
				new String[] { Long.toString(tr.id) });

		if (result > 0) {
			notifyProviderOnTimeRecordChange();
		}
		return result;
	}

	private void notifyProviderOnTimeRecordChange() {
		context.getContentResolver().notifyChange(
				RecordProvider.URI_RECORDS, null, false);
	}
}
