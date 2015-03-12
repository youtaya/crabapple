package com.talk.demo.intimate;

import com.talk.demo.R;
import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.FriendRecord;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class WriteRelateFragment extends Fragment {
    private GridView gridView;
    private MentGridViewAdapter mentAdapter;
    private TextView pressWall;
    private Button saveButton;
    
    private Context mContext;
    private int mFriendId;
    private DBManager mgr;
    private FriendRecord mFriendRecord;
    String[] contents = {
            "I Miss You",
            "I Love You",
            "A U OK",
            "All Right",
            "Think It",
            "More, More"
    };
    public WriteRelateFragment(Context ctx, int friend_id) {
        mContext = ctx;
        mFriendId = friend_id;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        
        mgr = new DBManager(mContext);
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_relate_write, container, false);
       
        pressWall = (TextView) rootView.findViewById(R.id.press_wall);
        mFriendRecord = mgr.queryFriendTheParam(mFriendId);
        pressWall.setText(mFriendRecord.getFriend().getDescription());
        
        gridView = (GridView) rootView.findViewById(R.id.ment_content);
        mentAdapter = new MentGridViewAdapter(getActivity(), contents);
        gridView.setAdapter(mentAdapter);
        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String oring = pressWall.getText().toString();
                TextView ment_item = (TextView) view.findViewById(R.id.ment_item_content);
                String afterStr = ment_item.getText().toString()+";"+oring;
                pressWall.setText(afterStr);
            }
            
        });
        
        saveButton = (Button) rootView.findViewById(R.id.bt_save_publish);
        saveButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                
                String des = pressWall.getText().toString();
                mgr.updateDescription(mFriendId, des);
                saveButton.setPressed(true);
            }
            
        });
        return rootView;
    }
}