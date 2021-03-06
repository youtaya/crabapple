package com.talk.demo.intimate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.JsonWriter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.HeatMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.talk.demo.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StepRelateFragment extends Fragment {

	private static final String TAG = "StepRelateFragment";
	private Button saveButton;
	private Context mContext;
	private List<LatLng> tempData;
	
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;

	MapView mMapView = null; // 定义mapview对象
	BaiduMap mBaiduMap = null;
	private HeatMap heatmap;
	
	boolean isFirstLoc = true;// 是否首次定位
	private SharedPreferences settings;
	private double lat, lng;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		SDKInitializer.initialize(getActivity().getApplicationContext());

		settings = getActivity().getSharedPreferences("GeoPoint_Info", 0);
		lat = (double) settings.getLong("Lat", 0);
		lng = (double) settings.getLong("Lng", 0);
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_relate_step,
				container, false);
		mMapView = (MapView) rootView.findViewById(R.id.mapview);
		mBaiduMap = mMapView.getMap();
		// 普通地图
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);

		// 修改为自定义marker
		/*
		mCurrentMode = LocationMode.NORMAL;
		mCurrentMarker = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_geo);
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, mCurrentMarker));
		*/
		
		LatLng ll = new LatLng(lat, lng);
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
		mBaiduMap.setMapStatus(u);
		
		addHeatMap();
		
		// 定位初始化
		mLocClient = new LocationClient(getActivity());
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();


		try {
		    tempData = getLocations(new FileInputStream(getJsonFile()));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		saveButton = (Button) rootView.findViewById(R.id.bt_save_publish);
		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					FileOutputStream out = new FileOutputStream(getJsonFile());
					writeJsonStream(out, tempData);
					/*
					out.flush();
					out.close();
					*/
				} catch (IOException e) {
					e.printStackTrace();
				}
				saveButton.setPressed(true);
			}

		});
		return rootView;
	}

	public void writeJsonStream(OutputStream out, List<LatLng> messages)
			throws IOException {
		JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
		writeMessagesArray(writer, messages);
		writer.close();
		
	}

	public void writeMessagesArray(JsonWriter writer, List<LatLng> mesgs)
			throws IOException {
		
		writer.beginArray();
		if(null != mesgs) {
			for (LatLng message : mesgs) {
				writeMessage(writer, message);
			}
		} else {
			Log.e(TAG, "file don't contain geo data");
		}
		// record this step place
		writeThisStep(writer);

		writer.endArray();
	}

	public void writeMessage(JsonWriter writer, LatLng message)
			throws IOException {
		writer.beginObject();
		writer.name("lng").value(message.longitude);
		writer.name("lat").value(message.latitude);
		writer.endObject();
	}

	public void writeThisStep(JsonWriter writer) throws IOException {
		writer.beginObject();
		writer.name("lng").value(lng);
		writer.name("lat").value(lat);
		writer.endObject();
	}

	private List<LatLng> getLocations(InputStream input) 
			throws IOException{
		List<LatLng> list = new ArrayList<LatLng>();
		Scanner scanner = new Scanner(input);
		if(!scanner.hasNext()) {
		    return null;
		}
		String json = scanner.useDelimiter("\\A").next();
		JSONArray array;
		try {
			array = new JSONArray(json);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				double lat = object.getDouble("lat");
				double lng = object.getDouble("lng");
				list.add(new LatLng(lat, lng));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

    public File getJsonFile() {
        File tempDir = new File(Environment.getExternalStorageDirectory()
                + "/Demo");
        if (!tempDir.exists()) {
            tempDir = new File("/sdcard/Demo");
            tempDir.mkdir();
        }

    	File tempFile = new File(tempDir, "testJson.json");
        
        return tempFile;
    }
    
	private void addHeatMap() {
		final Handler h = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				mBaiduMap.addHeatMap(heatmap);
			}
		};
		
		new Thread() {
			@Override
			public void run() {
				super.run();
				List<LatLng> data;
				try {
					data = getLocations(new FileInputStream(getJsonFile()));
					heatmap = new HeatMap.Builder().data(data).build();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				h.sendEmptyMessage(0);
			}
		}.start();
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

		/* 退出时保存这次的定位信息 */
		settings.edit().putLong("Lat", (long) lat).commit();
		settings.edit().putLong("Lng", (long) lng).commit();

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
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}
}