package com.talk.demo.intimate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.talk.demo.R;

public class StepRelateFragment extends Fragment {
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;
	
    MapView mMapView = null;  // 定义mapview对象
    BaiduMap mBaiduMap = null;
    
	boolean isFirstLoc = true;// 是否首次定位
	private SharedPreferences settings;
	private double lat, lng;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    	
    	SDKInitializer.initialize(getActivity().getApplicationContext()); 
    	
    	settings = getActivity().getSharedPreferences("GeoPoint_Info",0);
    	lat = (double)settings.getLong("Lat", 0);
    	lng = (double)settings.getLong("Lng", 0);
        // Inflate the layout for this fragment
    	View rootView = inflater.inflate(R.layout.fragment_relate_step, container, false);
    	mMapView = (MapView)rootView.findViewById(R.id.mapview);
    	mBaiduMap = mMapView.getMap();  
    	//普通地图  
    	mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
    	
    	// 开启定位图层  
    	mBaiduMap.setMyLocationEnabled(true);
    	
		// 修改为自定义marker
    	mCurrentMode = LocationMode.NORMAL;
		mCurrentMarker = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_geo);
		mBaiduMap
				.setMyLocationConfigeration(new MyLocationConfiguration(
						mCurrentMode, true, mCurrentMarker));
		
		MyLocationData defaultData = new MyLocationData.Builder()
			.latitude(lat)
			.longitude(lng).build();
		mBaiduMap.setMyLocationData(defaultData);
		
		// 定位初始化
		mLocClient = new LocationClient(getActivity());
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		
    	return rootView;
    }
    
	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				lat = location.getLatitude();
				lng = location.getLongitude();
				LatLng ll = new LatLng(lat, lng);
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
	
    @Override  
    public void onDestroy() {  
        super.onDestroy();  
        
        /*退出时保存这次的定位信息*/
        settings.edit().putLong("Lat", (long)lat).commit();
        settings.edit().putLong("Lng", (long)lng).commit();
        
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		
    }  
    
	@Override
	public void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}
	
    @Override  
    public void onPause() {  
        super.onPause();  
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
        mMapView.onPause();  
    }
}