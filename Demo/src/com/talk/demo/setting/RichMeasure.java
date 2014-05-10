package com.talk.demo.setting;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class RichMeasure extends Application {
    private int rich = 0;
    
    public int getRich() {
    	 SharedPreferences sPreferences = getSharedPreferences("rich", Context.MODE_PRIVATE);
    	 rich = sPreferences.getInt("measure", 0);
        return rich;
    }
    public void setRich(int param) {
    	this.rich = param;
        SharedPreferences sp = getSharedPreferences("rich", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt("measure", param);
        editor.commit();
        
    }
    public void addRich(int v) {
        this.rich += v;
        setRich(this.rich);
    }
    public void minusRich(int v) {
        this.rich -= v;
        setRich(this.rich);
    }
}
