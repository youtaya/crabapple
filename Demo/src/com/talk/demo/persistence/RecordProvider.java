package com.talk.demo.persistence;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class RecordProvider extends ContentProvider {

	// All URIs share these parts
	public static final String AUTHORITY = "com.talk.record.provider";
	public static final String SCHEME = "content://";

	// URIs
	// Used for all persons
	public static final String RECORDS = SCHEME + AUTHORITY + "/person";
	public static final Uri URI_RECORDS = Uri.parse(RECORDS);
	// Used for a single person, just add the id to the end
	public static final String RECORD_BASE = RECORDS + "/";

	public RecordProvider() {
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// Implement this to handle requests to delete one or more rows.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public String getType(Uri uri) {
		// TODO: Implement this to handle requests for the MIME type of the data
		// at the given URI.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO: Implement this to handle requests to insert a new row.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public boolean onCreate() {
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor result = null;
		if (URI_RECORDS.equals(uri)) {
			result = DatabaseHandler
					.getInstance(getContext())
					.getReadableDatabase()
					.query(TimeRecord.TABLE_NAME, TimeRecord.FIELDS, null, null, null,
							null, null, null);
			result.setNotificationUri(getContext().getContentResolver(), URI_RECORDS);
		} else if (uri.toString().startsWith(RECORD_BASE)) {
			final long id = Long.parseLong(uri.getLastPathSegment());
			result = DatabaseHandler
					.getInstance(getContext())
					.getReadableDatabase()
					.query(TimeRecord.TABLE_NAME, TimeRecord.FIELDS,
							TimeRecord.COL_ID + " IS ?",
							new String[] { String.valueOf(id) }, null, null,
							null, null);
			result.setNotificationUri(getContext().getContentResolver(), URI_RECORDS);
		} else {
			throw new UnsupportedOperationException("Not yet implemented");
		}

		return result;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO: Implement this to handle requests to update one or more rows.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}