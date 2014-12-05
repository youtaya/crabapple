package com.talk.demo.intimate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.talk.demo.R;

public class StepRelateFragment extends Fragment {
    MapView mapView = null;  // 定义mapview对象 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    	
    	SDKInitializer.initialize(getActivity().getApplicationContext());  
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_relate_step, container, false);
    }
}