package com.talk.demo.setting;

import android.app.Application;

public class RichMeasure extends Application {
    private int rich = 0;
    
    public int getRich() {
        return rich;
    }
    public void setRich(int param) {
        this.rich = param;
    }
    public void addRich(int v) {
        this.rich += v;
    }
    public void minusRich(int v) {
        this.rich -= v;
    }
}
