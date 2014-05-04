package com.talk.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.talk.demo.audio.AudioRecorderActivity;
import com.talk.demo.daily.DailyEditActivity;
import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.TimeRecord;
import com.talk.demo.prewrite.PreWrite;
import com.talk.demo.util.NetworkUtilities;
import com.talk.demo.util.TalkUtil;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DailyFragment extends Fragment implements OnItemClickListener {
    OnItemChangedListener mCallback;

    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnItemChangedListener {
        public void onItemChanged();
    }
    
    private static String TAG = "DailyFragment";
    //private ListView lv;
    private PullToRefreshListView pullToRefreshView;
    private EditText et;
    private ImageView iv,ivSpring, ivPhoto, ivGallery, ivTape;
    private DBManager mgr;
    private LinkedList<String> daily_record;
    private DailyListAdapter adapter;
    private LinearLayout take_snap;
    private boolean snap_on = false;
    private String selectedImagePath;
    private PreWrite pw;
    private LinkedList<String> mListItems;
    
    public DailyFragment(DBManager db, PreWrite prewrite) {
        daily_record = new LinkedList<String>();
        mgr = db;
        pw = prewrite;
        
    }

    private void hideKeyboardAndClearET() {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
      	      Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        et.setText("");
        iv.setImageResource(R.drawable.btn_check_off_normal);
    }
    
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
        	startActivityForResult(takePictureIntent, TalkUtil.REQUEST_IMAGE_CAPTURE);
        }
    }
    
    private void dispatchTakeGalleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), TalkUtil.REQUEST_SELECT_PICTURE);
    }
    
    private void dispatchTakeTapeIntent() {
        Intent intent = new Intent(getActivity(), AudioRecorderActivity.class);
        startActivityForResult(intent, TalkUtil.REQUEST_AUDIO_CAPTURE);
    }
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_daily, container, false);
        
        
        take_snap = (LinearLayout)rootView.findViewById(R.id.take_snap);
        // Set a listener to be invoked when the list should be refreshed.
        pullToRefreshView = (PullToRefreshListView)rootView.findViewById(R.id.daily_list);
        //lv = (ListView)rootView.findViewById(R.id.daily_list);
        et = (EditText)rootView.findViewById(R.id.fast_record);
        ivSpring = (ImageView)rootView.findViewById(R.id.tool_spring);
        ivPhoto = (ImageView)rootView.findViewById(R.id.take_photo);
        ivPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dispatchTakePictureIntent();
				take_snap.setVisibility(View.GONE);
			}
        	
        });
        
        ivGallery = (ImageView)rootView.findViewById(R.id.take_gallery);
        ivGallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dispatchTakeGalleryIntent();
				take_snap.setVisibility(View.GONE);
			}
        	
        });
        ivTape = (ImageView)rootView.findViewById(R.id.take_tape);
        ivTape.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dispatchTakeTapeIntent();
                take_snap.setVisibility(View.GONE);
            }
            
        });
        ivSpring.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!snap_on) {
					take_snap.setVisibility(View.VISIBLE);
					snap_on = true;
				} else {
					take_snap.setVisibility(View.GONE);
					snap_on = false;
				}
			}
        	
        });
        iv = (ImageView)rootView.findViewById(R.id.ok_fast_record);
        TextWatcher watcher = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				iv.setImageResource(R.drawable.btn_check_on_normal);
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if( s.length() > 0) {
					iv.setImageResource(R.drawable.btn_check_on_normal);
				} else {
					iv.setImageResource(R.drawable.btn_check_off_normal);
				}
			}
        };
        et.addTextChangedListener(watcher);
        
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et.getText().toString();
				// Do nothing if content is empty
                if(content.length() > 0) {
                    TimeRecord tr = new TimeRecord(content);  
                    tr.setContentType(TalkUtil.MEDIA_TYPE_TEXT);
                    mgr.add(tr);
                    
                    mCallback.onItemChanged();
                    // update record list view
                    RecordFragment rFragment = RecordFragment.newInstance(mgr);;
                    rFragment.update();
                    // update time list view
                    initDataList();
                    adapter.notifyDataSetChanged();
                    
                    hideKeyboardAndClearET();
                }
            }
        });
        
        pullToRefreshView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                // Do work to refresh the list here.
                new GetDataTask().execute();
            }
        });
        
        initListView();
        
        return rootView;
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnItemChangedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnItemChangedListener");
        }
    }

    public LinkedList<String> initDataList() {  
        Log.d(TAG, "init data list");

        if(!daily_record.isEmpty()) {
            daily_record.clear();
        }
        
        // Get where, when and weather
        daily_record = pw.getPreWriteData();
        return daily_record;
    }
    
    public void initListView() {
    	
        if(pullToRefreshView == null)
            return;
        mListItems = initDataList();
        adapter = new DailyListAdapter(getActivity(), mListItems);
        pullToRefreshView.setAdapter(adapter);
        pullToRefreshView.setOnItemClickListener(this);

    }
    
    private class GetDataTask extends AsyncTask<Void, Void, List<String>> {
        List<String> getDataList = new ArrayList<String>();
        @Override
		protected List<String> doInBackground(Void... params) {
            // Simulates a background job.
            try {
                getDataList = NetworkUtilities.syncNews();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            return getDataList;
            
		}
		
        @Override
        protected void onPostExecute(List<String> result) {
            for(String temp: result) {
                Log.d(TAG, "temp is "+temp);
                mListItems.addFirst(temp);
            }
            adapter.notifyDataSetChanged();
            // Call onRefreshComplete when the list has been refreshed.
            pullToRefreshView.onRefreshComplete();
            super.onPostExecute(result);
        }		
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        
        String valueContent = parent.getItemAtPosition(position).toString();
        
        Intent mIntent = new Intent(getActivity(), DailyEditActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("precontent", valueContent);
        mIntent.putExtras(mBundle);
        startActivity(mIntent);
        
    } 
    
    @Override
    public void onPause() {
    	super.onPause();
    }
    
    @Override
    public void onResume () {
        super.onResume();
        Log.d(TAG, "on Resume");
        initDataList();
        adapter.notifyDataSetChanged();

    }
    
    private void createDirAndSaveFile(Bitmap imageToSave, String fileName) {
        File direct = new File(Environment.getExternalStorageDirectory() + "/Demo");
        
        if(!direct.exists()) {
            File fileDirectory = new File("/sdcard/Demo/");
            fileDirectory.mkdirs();
        }
        
        File file = new File(new File("/sdcard/Demo/"), fileName);
        
        if(file.exists())
            file.delete();
        
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    private String getTimeAsFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss"); 
        Date date = new Date();
        return dateFormat.format(date);
    }
    
    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
            // just some safety built in 
            if( uri == null ) {
                // TODO perform some logging or show user feedback
                return null;
            }
            // try to retrieve the image from the media store first
            // this will only work for images selected from gallery
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
            if( cursor != null ){
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }
            // this is our fallback here
            return uri.getPath();
    }
    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "got the return :"+requestCode+" :"+resultCode);
        switch(requestCode) {
            case TalkUtil.REQUEST_IMAGE_CAPTURE:
                if (resultCode == getActivity().RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    String fileName = getTimeAsFileName();
                    createDirAndSaveFile(imageBitmap, fileName);
                    
                    TimeRecord tr = new TimeRecord("/sdcard/Demo/"+fileName);
                    tr.setContentType(TalkUtil.MEDIA_TYPE_PHOTO);;
                    mgr.add(tr);
                }
                break;
            case TalkUtil.REQUEST_SELECT_PICTURE:
                if (resultCode == getActivity().RESULT_OK) {
                    Uri selectedImageUri = data.getData();
                    selectedImagePath = getPath(selectedImageUri);
                    TimeRecord tr = new TimeRecord(selectedImagePath);
                    tr.setContentType(TalkUtil.MEDIA_TYPE_PHOTO);;
                    mgr.add(tr);
                }
                break;
            case TalkUtil.REQUEST_AUDIO_CAPTURE:
            	if (resultCode == getActivity().RESULT_OK) {
            		Bundle extras = data.getExtras();
            		String audioFileName = (String)extras.get("audio_file_name");
                    TimeRecord tr = new TimeRecord(audioFileName);
                    tr.setContentType(TalkUtil.MEDIA_TYPE_AUDIO);;
                    mgr.add(tr);
            	}
            	break;
            default:
                Log.d(TAG, "unknown type!!");
                break;
        }
    }

}
