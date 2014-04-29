package com.talk.demo.prewrite;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.talk.demo.DailyFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PreWrite {

    private List<String> preData;
    private Context context;
    private DailyFragment dFragment;
    private String when;
    private String where;
    private String weather;
    public LocationClient mLocationClient;
    private MyLocationListener mMyLocationListener;
    
    public PreWrite(Context ctx, DailyFragment fragment) {
        context = ctx;
        dFragment = fragment;
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
        //where = "ZhangJiang";
        return where;
    }
    
    public void startPosition() {
        mLocationClient = new LocationClient(context);
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
    }
    
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation){
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\ndirection : ");
                sb.append(location.getDirection());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
            }
            
            where = sb.toString();
            
            dFragment.initListView();
        }

        @Override
        public void onReceivePoi(BDLocation arg0) {
        }
        
    }
    
    public String getWeather() {
        return weather;
    }
}
