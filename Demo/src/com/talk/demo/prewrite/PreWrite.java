package com.talk.demo.prewrite;

import com.talk.demo.util.TalkUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PreWrite {

    private List<String> preData;
    private String when;
    private String where;
    private String weather;
    
    public PreWrite() {
        preData = new ArrayList<String>();
    }
    public List<String> getPreWriteData() {
        
        preData.add(getWhen());
        preData.add(getWhere());
        //preData.add(getWeather());
        return preData;
    }
    
    public String getWhen() {
        SimpleDateFormat pDateFormat = new SimpleDateFormat("yyyy/MM/dd"); 
        when = pDateFormat.format(new Date());
        return when;
    }
    public String getWhere() {
        where = "ZhangJiang";
        return where;
    }
    public String getWeather() {
        return weather;
    }
}
