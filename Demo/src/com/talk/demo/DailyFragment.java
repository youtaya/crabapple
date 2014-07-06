package com.talk.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.talk.demo.core.RecordManager;
import com.talk.demo.daily.DailyEditActivity;
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
    private static String TAG = "DailyFragment";
    //private ListView lv;
    private PullToRefreshListView pullToRefreshView;
    private EditText et;
    private ImageView btn_maximize,btn_more, ivPhoto, ivGallery, ivTape;
    private ImageView btn_new;
    private RecordManager recordManager;
    private LinkedList<String> daily_record;
    private DailyListAdapter adapter;
    private LinearLayout take_snap;
    private boolean snap_on = false;
    private String selectedImagePath;
    private PreWrite pw;
    private LinkedList<String> mListItems;
    
    public DailyFragment(RecordManager recordMgr, PreWrite prewrite) {
        daily_record = new LinkedList<String>();
        recordManager = recordMgr;
        pw = prewrite;
        
    }

    private void hideKeyboardAndClearET() {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
      	      Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        et.setText("");
        btn_maximize.setImageResource(R.drawable.btn_maximize_normal);
    }
    
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
        	startActivityForResult(takePictureIntent, TalkUtil.REQUEST_IMAGE_CAPTURE);
        }
    }
    
    private void dispatchTakeGalleryIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), TalkUtil.REQUEST_SELECT_PICTURE);
    }
    
    private void dispatchTakeTapeIntent() {
        Intent intent = new Intent(getActivity(), AudioRecorderActivity.class);
        startActivityForResult(intent, TalkUtil.REQUEST_AUDIO_CAPTURE);
    }
    
    private void diamondDialog() {
        AlertDialog.Builder builder = new Builder(getActivity());
        builder.setTitle("新进宝石一枚");
        builder.setMessage("宝石可以用于解锁锁定时光:)");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_daily, container, false);
        
        
        take_snap = (LinearLayout)rootView.findViewById(R.id.take_snap);
        // Set a listener to be invoked when the list should be refreshed.
        pullToRefreshView = (PullToRefreshListView)rootView.findViewById(R.id.daily_list);
        //lv = (ListView)rootView.findViewById(R.id.daily_list);
        et = (EditText)rootView.findViewById(R.id.fast_record);
        btn_more = (ImageView)rootView.findViewById(R.id.btn_more);
        ivPhoto = (ImageView)rootView.findViewById(R.id.take_photo);
        ivPhoto.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
	  			int action = event.getAction();
			    switch (action) {
			    case MotionEvent.ACTION_MOVE:
			    case MotionEvent.ACTION_DOWN:
			    	ivPhoto.setPressed(true);
			    	break;
			    case MotionEvent.ACTION_UP:
			    	dispatchTakePictureIntent();
			    	ivPhoto.setPressed(false);
			    	take_snap.setVisibility(View.GONE);
			    	btn_more.setImageResource(R.drawable.quickmore_button_selector);
			    case MotionEvent.ACTION_CANCEL:
			    	ivPhoto.setPressed(false);
			    default:
			    	break;
			    }
				
				return true;
			}
        	
        });
        
        ivGallery = (ImageView)rootView.findViewById(R.id.take_gallery);
        ivGallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dispatchTakeGalleryIntent();
				take_snap.setVisibility(View.GONE);
				btn_more.setImageResource(R.drawable.quickmore_button_selector);
			}
        	
        });
        ivTape = (ImageView)rootView.findViewById(R.id.take_tape);
        ivTape.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dispatchTakeTapeIntent();
                take_snap.setVisibility(View.GONE);
                btn_more.setImageResource(R.drawable.quickmore_button_selector);
            }
            
        });
        btn_more.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
    			int action = event.getAction();
			    switch (action) {
			    case MotionEvent.ACTION_MOVE:
			    case MotionEvent.ACTION_DOWN:
			    	btn_more.setPressed(true);
			    	break;
			    case MotionEvent.ACTION_UP:
					if(!snap_on) {
						take_snap.setVisibility(View.VISIBLE);
						btn_more.setImageResource(R.drawable.quickstowed_button_selector);
						Animation hyperspaceJumpAnimation = AnimationUtils.
								loadAnimation(getActivity(), R.anim.in_from_bottom);
						take_snap.startAnimation(hyperspaceJumpAnimation);
						snap_on = true;
				        btn_more.setFocusableInTouchMode(true);
				        btn_more.requestFocus();
					} else {
						btn_more.setImageResource(R.drawable.quickmore_button_selector);
						Animation hyperspaceJumpAnimation = AnimationUtils.
								loadAnimation(getActivity(), R.anim.out_to_bottom);
						take_snap.startAnimation(hyperspaceJumpAnimation);
						take_snap.setVisibility(View.GONE);
						snap_on = false;
						btn_more.setFocusableInTouchMode(false);
						btn_more.setFocusable(false);
					}
			    	btn_more.setPressed(false);
			    	break;
			    case MotionEvent.ACTION_CANCEL:
			    	btn_more.setPressed(false);
			    default:
			    	break;
			    }
				
				return true;
			}
        	
        });
        
        btn_more.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus) {
					if(snap_on) {
						btn_more.setImageResource(R.drawable.quickmore_button_selector);
						Animation hyperspaceJumpAnimation = AnimationUtils.
								loadAnimation(getActivity(), R.anim.out_to_bottom);
						take_snap.startAnimation(hyperspaceJumpAnimation);
						take_snap.setVisibility(View.GONE);
						snap_on = false;
						btn_more.setFocusableInTouchMode(false);
					}
			    	btn_more.setPressed(false);
				}
				
			}
        	
        });
        
        btn_more.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if(snap_on) {
						btn_more.setImageResource(R.drawable.quickmore_button_selector);
						Animation hyperspaceJumpAnimation = AnimationUtils.
								loadAnimation(getActivity(), R.anim.out_to_bottom);
						take_snap.startAnimation(hyperspaceJumpAnimation);
						take_snap.setVisibility(View.GONE);
						snap_on = false;
						btn_more.setFocusableInTouchMode(false);
						btn_more.setFocusable(false);
					}
			    	btn_more.setPressed(false);
					return true;
				}
				return false;
			}
		});
        
        btn_maximize = (ImageView)rootView.findViewById(R.id.btn_maximize);
        btn_maximize.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
    			int action = event.getAction();
			    switch (action) {
			    case MotionEvent.ACTION_MOVE:
			    case MotionEvent.ACTION_DOWN:
			    	btn_maximize.setPressed(true);
			    	break;
			    case MotionEvent.ACTION_UP:
	            	Intent intent = new Intent(getActivity(),DailyEditActivity.class);
	                Bundle mBundle = new Bundle();
	                mBundle.putString("precontent", et.getText().toString());
	                intent.putExtras(mBundle);
	            	getActivity().startActivity(intent);  
	            	getActivity().overridePendingTransition(R.anim.in_from_bottom,0);
	            	btn_maximize.setPressed(false);
			    case MotionEvent.ACTION_CANCEL:
			    	btn_maximize.setPressed(false);
			    default:
			    	break;
			    }
            	return true;
            }
        });
        
        btn_new = (ImageView)rootView.findViewById(R.id.btn_new);
        btn_new.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
			    switch (action) {
			    case MotionEvent.ACTION_MOVE:
			    case MotionEvent.ACTION_DOWN:
			    	btn_new.setPressed(true);
			    	break;
			    case MotionEvent.ACTION_UP:
			    	if(!btn_new.isFocusable()) {
			    		Log.d(TAG, "test....");
				    	Intent intent = new Intent(getActivity(),DailyEditActivity.class);  
		            	getActivity().startActivity(intent);  
		            	getActivity().overridePendingTransition(R.anim.in_from_bottom, R.anim.keep_unchanage);
			    	} else {
			    		Log.d(TAG, "test....222");
			    		String content = et.getText().toString();
						// Do nothing if content is empty
		                if(content.length() > 0) {
		                    TimeRecord tr = new TimeRecord(content);  
		                    tr.setContentType(TalkUtil.MEDIA_TYPE_TEXT);
		                    recordManager.addRecord(tr);
		                    
		                    // update time list view
		                    initDataList();
		                    adapter.notifyDataSetChanged();
		                    
		                    diamondDialog();
		                }
		                hideKeyboardAndClearET();
			    	}
			    	btn_new.setPressed(false);
	            	break;
			    case MotionEvent.ACTION_CANCEL:
			    	btn_new.setPressed(false);
			    default:
			    	break;
			    }
			    
				return true;
			}
        	
        });
   
        TextWatcher watcher = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				//btn_maximize.setImageResource(R.drawable.btn_maximize_active);
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if( s.length() > 0) {
					btn_maximize.setVisibility(View.VISIBLE);
					btn_new.setImageResource(R.drawable.done_button_selector);
					btn_new.setFocusable(true);
				} else {
					btn_maximize.setVisibility(View.GONE);
					btn_new.setImageResource(R.drawable.quicknew_button_selector);
					btn_new.setFocusable(false);
				}
			}
        };
        et.addTextChangedListener(watcher);
        
        
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
    public void onResume() {
        super.onResume();
        Log.d(TAG, "on Resume");
        initDataList();
        adapter.notifyDataSetChanged();

        //Todo:get today latest
        
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
                    //prvent content null
                    TimeRecord tr = new TimeRecord("photo");
                    tr.setPhoto("/sdcard/Demo/"+fileName);
                    tr.setContentType(TalkUtil.MEDIA_TYPE_PHOTO);;
                    recordManager.addRecord(tr);
                }
                break;
            case TalkUtil.REQUEST_SELECT_PICTURE:
                if (resultCode == getActivity().RESULT_OK) {
                    Uri selectedImageUri = data.getData();
                    selectedImagePath = getPath(selectedImageUri);
                    TimeRecord tr = new TimeRecord("photo");
                    tr.setPhoto(selectedImagePath);
                    tr.setContentType(TalkUtil.MEDIA_TYPE_PHOTO);;
                    recordManager.addRecord(tr);
                }
                break;
            case TalkUtil.REQUEST_AUDIO_CAPTURE:
            	if (resultCode == getActivity().RESULT_OK) {
            		Bundle extras = data.getExtras();
            		String audioFileName = (String)extras.get("audio_file_name");
                    TimeRecord tr = new TimeRecord(audioFileName);
                    tr.setContentType(TalkUtil.MEDIA_TYPE_AUDIO);;
                    recordManager.addRecord(tr);
            	}
            	break;
            default:
                Log.d(TAG, "unknown type!!");
                break;
        }
    }

}
