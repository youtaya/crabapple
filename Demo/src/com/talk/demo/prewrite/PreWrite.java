package com.talk.demo.prewrite;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PreWrite {

	private static String TAG = "PreWrite";
    private List<String> preData;
    private Context context;
    private String when;
    private String where;
    private String weather;
    public LocationClient mLocationClient;
    private MyLocationListener mMyLocationListener;
    
    public PreWrite(Context ctx) {
        context = ctx;
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
    	if (null == where) {
	        SharedPreferences sp = context.getSharedPreferences("current_where", Context.MODE_PRIVATE);
	        String cacheWhere = sp.getString("where", "");
	        where = cacheWhere;
    	}
        return where;
    }
    
    public void startPosition() {
        mLocationClient = new LocationClient(context);
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度，默认值gcj02
        option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        if (mLocationClient != null && mLocationClient.isStarted())
        	mLocationClient.requestLocation();
    	else 
    		Log.d(TAG, "locClient is null or not started");
    }
    
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            Log.d(TAG, "onReceiveLocation");
            
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
            
            where = location.getAddrStr();
            Log.d(TAG, "where : "+where);
            //save for cache
            SharedPreferences sp = context.getSharedPreferences("current_where", Context.MODE_PRIVATE);
            Editor editor = sp.edit();
            editor.putString("where", where);
            editor.commit();
            //stop position
            mLocationClient.stop();
        }

        @Override
        public void onReceivePoi(BDLocation arg0) {
        }
        
    }
    
    public String getWeather() {
        return weather;
    }
}
