package com.talk.demo;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.talk.demo.persistence.RecordProvider;
import com.talk.demo.persistence.TimeRecord;

public class RecordFragment extends ListFragment {
    private static String TAG = "RecordFragment";
 
    public RecordFragment() {
    }
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initDataList();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_record_list, null);
	}
	
    public void initDataList() {  
		setListAdapter(new SimpleCursorAdapter(getActivity(),
				R.layout.fragment_record_list, null, new String[] {
						TimeRecord.COL_TITLE, TimeRecord.COL_CONTENT,
						TimeRecord.COL_CREATE_TIME }, new int[] { R.id.title,
						R.id.content, R.id.create_time }, 0));
        // Load the content
        getLoaderManager().initLoader(0, null, new LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(getActivity(),
                        RecordProvider.URI_RECORDS, TimeRecord.FIELDS, null, null,
                        null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
                ((SimpleCursorAdapter) getListAdapter()).swapCursor(c);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> arg0) {
                ((SimpleCursorAdapter) getListAdapter()).swapCursor(null);
            }
        });
 
    } 
    
    @Override
    public void onResume () {
        super.onResume();
        Log.d(TAG, "on Resume");

    }
    
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "on Pause");
    }
    
    @Override
    public void onDestroy() {  
        super.onDestroy();  
    }  
}