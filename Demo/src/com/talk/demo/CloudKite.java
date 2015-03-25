package com.talk.demo;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

public class CloudKite implements Runnable {
	public static interface taskListener {
		void onProgressChanged(final int progress);
	}
	private String desc;
	private int progress;
	private int interval;
	private String done_time;
	
	private Vector<CloudKite.taskListener> listenerList;

	public CloudKite(String desc, int val, String done) {
		progress=0;
		listenerList=new Vector<CloudKite.taskListener>();
		this.desc=desc;
		interval = val;
		done_time = done;
	}
	
	@Override
	public void run() {
		Random random = new Random();
		try {
			
			progress = measureProgress();
			setProgress(progress);
			
			Thread.sleep(random.nextInt(9)*1000);
			
			while(progress < 100) {
			    Thread.sleep(1000);
			    progress = measureProgress();
			    setProgress(progress);
			}
			
		} catch (InterruptedException e) {
			setProgress(0);
		} catch (Exception generalEcc) {
			setProgress(0);
		} finally {
			Thread.interrupted();
		}

	}
	
	private int measureProgress() {
        int result = progress;
        
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        try {
            Date recordDate = formatter.parse(done_time);
            long diff = recordDate.getTime() - date.getTime();
            if (diff < 0 || diff == 0) {
                result = 100;
                return result;
            }
            result = (interval-(int)diff)/interval * 100;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
        
	}

	public void addListener(taskListener l) {
		listenerList.add(l);
	}
	public void removeListener(taskListener l){
		listenerList.remove(l);
	}
	public int getProgress() {
		return progress > 100 ? 100 : progress;
	}

	private void setProgress(int progress) {
		this.progress = progress > 100 ? 100 : progress;
		if (!listenerList.isEmpty()) {
			for (CloudKite.taskListener listener : listenerList)
				listener.onProgressChanged(this.progress);
		}
	}

	private void incrementProgress(int increment) {
		setProgress(this.progress + increment);
	}

	public String getDesc() {
		return desc;
	}

	
}
