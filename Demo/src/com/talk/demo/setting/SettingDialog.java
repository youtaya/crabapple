package com.talk.demo.setting;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.talk.demo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingDialog extends DialogFragment {
	private ListView lv;
	private SimpleAdapter adapter;
	// The data to show
	List<Map<String, String>> planetsList = new ArrayList<Map<String,String>>();
	 
	 
	private void initList() {
	    // We populate the planets
	    planetsList.add(createPlanet("planet", "Mercury"));
	    planetsList.add(createPlanet("planet", "Venus"));
	    planetsList.add(createPlanet("planet", "Mars"));
	    planetsList.add(createPlanet("planet", "Jupiter"));
	    planetsList.add(createPlanet("planet", "Saturn"));
	    planetsList.add(createPlanet("planet", "Uranus"));
	    planetsList.add(createPlanet("planet", "Neptune"));
	  
	}
	
	private HashMap<String, String> createPlanet(String key, String name) {
	    HashMap<String, String> planet = new HashMap<String, String>();
	    planet.put(key, name);
	     
	    return planet;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        lv = (ListView)v.findViewById(R.id.setting_menu);
        initList();
        adapter = new SimpleAdapter(this.getActivity(), planetsList, android.R.layout.simple_list_item_1, new String[] {"planet"}, new int[] {android.R.id.text1});
        lv.setAdapter(adapter);
        return v;
    }
}