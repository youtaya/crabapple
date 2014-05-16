package com.talk.demo;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.talk.demo.core.RecordManager;
import com.talk.demo.setting.RichPresent;

import java.util.ArrayList;
import java.util.Map;

public class RecordFragment extends Fragment {
    private static String TAG = "RecordFragment";
    
    private ArrayList<Map<String, String>> list;
    private RecordManager recordManager;
    private SimpleAdapter adapter;
    private RichPresent rp;
    private static RecordFragment instance;
    private OnItemClickListener listener;
    int[] status = new int[] {
            R.drawable.lock_status,
            R.drawable.unlock_status,
            R.drawable.send_knows,
            };
    
    static RecordFragment newInstance(RecordManager recordMgr) {
        if(instance == null) {
            instance = new RecordFragment(recordMgr);
            
        }
        return instance;

    }
    public RecordFragment(RecordManager recordMgr) {
        list = new ArrayList<Map<String, String>>();
        recordManager = recordMgr;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        
        list = recordManager.initDataListRecord(status);
        adapter = new SimpleAdapter(getActivity(), list, R.layout.record_status_listitem,  
                new String[]{"create_time", "status", "send_knows"}, 
                new int[]{R.id.create_time, R.id.status, R.id.send_knows});
        
        rp = RichPresent.getInstance(this.getActivity().getApplicationContext());
    }
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_record, container, false);
        ListView lv = (ListView)rootView.findViewById(R.id.record_list);
        lv.setAdapter(adapter);
        listener = new MyItemClickListener();
        lv.setOnItemClickListener(listener);
        adapter.notifyDataSetChanged();
        return rootView;
    }
    
    public class MyItemClickListener implements OnItemClickListener {
        private ImageButton buy;
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            buy = (ImageButton)view.findViewById(R.id.status);
            buy.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog();
                }
            });
            
        }
        
    }
    
    /*
     * ToDo: change to support DialogFragment
     */
    protected void dialog() {
        AlertDialog.Builder builder = new Builder(this.getActivity());
        builder.setMessage("确定适用蓝宝石");
        builder.setTitle("购买提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
            	rp.minusRich(2);
                dialog.dismiss();
                
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                
            }
        });
        builder.create().show();
    }
    
    public void update() {
    	list = recordManager.initDataListRecord(status);
        adapter.notifyDataSetChanged();
    }
    
    @Override
    public void onResume () {
        super.onResume();
        Log.d(TAG, "on Resume");
        list = recordManager.initDataListRecord(status);
        adapter.notifyDataSetChanged();
    }

    
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "on Pause");
    }
    
    @Override
    public void onDestroy() {  
        super.onDestroy();  

    }  
}