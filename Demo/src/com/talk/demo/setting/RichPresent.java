package com.talk.demo.setting;

import android.content.Context;

public class RichPresent {
	
	private static RichPresent mInstance = null;
	
	private RichCalc rc;
	private Context ctx;
	
    public static RichPresent getInstance(Context context){
        if(mInstance == null)
        {
            mInstance = new RichPresent(context);
        }
        return mInstance;
    }
    
	private RichPresent(Context context) {
		ctx = context;
		rc = new RichCalc(ctx);
	}
	
	public void setRich(int para) {
		rc.addRich(para);
	}
	
	public int getRich() {
		return rc.getRich();
	}
	
	public void addRich(int para) {
		rc.addRich(para);
	}
	
	public void minusRich(int para) {
		rc.minusRich(para);
	}
}
