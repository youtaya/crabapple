
package com.talk.demo.audio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.talk.demo.R;

public class AudioRecorderActivity extends Activity
{
    private static final String LOG_TAG = "AudioRecorderActivity";
    private String mFileName = null;

    private MediaRecorder mRecorder = null;
    private ImageButton mRecorderBtn = null;
    private MediaPlayer mPlayer = null;
    private ImageButton mPlayerBtn = null;
    
    private boolean mStartRecording = true;
    private boolean mStartPlaying = true;
    
    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(getTimeAsFileName());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        
    }
  
    private void createDir(String fileName) {
        File direct = new File(Environment.getExternalStorageDirectory() + "/Demo");
        
        if(!direct.exists()) {
            File fileDirectory = new File("/sdcard/Demo/");
            fileDirectory.mkdirs();
        }
    }
    
    private void saveFile(String fileName) {
    	
        File file = new File(new File("/sdcard/Demo/"), fileName);
        if(file.exists())
            file.delete();
        
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.flush();
            out.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private String getTimeAsFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss"); 
        Date date = new Date();
        
        createDir(dateFormat.format(date));
        String fileName = "/sdcard/Demo/"+ dateFormat.format(date);
        mFileName = fileName;
        return fileName;
    }
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_audio_recorder);
        
        mRecorderBtn = (ImageButton)findViewById(R.id.recorder_btn);
        mPlayerBtn = (ImageButton)findViewById(R.id.player_btn);
        
        mRecorderBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
                onRecord(mStartRecording);
                mStartRecording = !mStartRecording;
				
			}
        	
        });
        mPlayerBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
                onPlay(mStartPlaying);
                mStartPlaying = !mStartPlaying;
				
			}
        	
        });
        
        mRecorder = new MediaRecorder();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.confirm_actions, menu);
        return true;
    }  
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_confirm:
                //save record audio file
                saveFile(mFileName);
                //pass result to previous activity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("audio_file_name", mFileName);
                setResult(RESULT_OK, resultIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

}
