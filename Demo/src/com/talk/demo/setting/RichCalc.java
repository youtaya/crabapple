package com.talk.demo.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class RichCalc implements RichOpt {
	private int rich = 0;
	private Context context;
	public RichCalc(Context ctx) {
		context = ctx;
	}
	@Override
	public void setRich(int param) {
    	this.rich = param;
        SharedPreferences sp = context.getSharedPreferences("rich", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt("measure", param);
        editor.commit();
		
	}

	@Override
	public int getRich() {
	   	 SharedPreferences sPreferences = context.getSharedPreferences("rich", Context.MODE_PRIVATE);
    	 rich = sPreferences.getInt("measure", 0);
        return rich;
	}

	@Override
	public void addRich(int v) {
        this.rich += v;
        setRich(this.rich);
	}

	@Override
	public void minusRich(int v) {
        this.rich -= v;
        setRich(this.rich);
	}

}
