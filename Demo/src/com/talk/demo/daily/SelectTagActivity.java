package com.talk.demo.daily;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

import com.talk.demo.R;

import java.util.ArrayList;
import java.util.List;

public class SelectTagActivity extends Activity {
	private static final String TAG = "SelectTagActivity";
    private List<String> GroupData;//定义组数据  
    private List<List<String>> ChildrenData;//定义组中的子数据
    private void LoadListDate() {  
        GroupData = new ArrayList<String>();  
        GroupData.add("想你的夜");  
        GroupData.add("月亮代表我的心");  
        GroupData.add("下一站天后");  
  
        ChildrenData = new ArrayList<List<String>>();  
        List<String> Child1 = new ArrayList<String>();  
        Child1.add("2012年6月21日");  
        Child1.add("2012年6月22日");
        Child1.add("2012年6月23日");
        ChildrenData.add(Child1);  
        List<String> Child2 = new ArrayList<String>();  
        Child2.add("喜马拉雅");  
        Child2.add("泰山");  
        Child2.add("嵩山");  
        ChildrenData.add(Child2);  
        List<String> Child3 = new ArrayList<String>();  
        Child3.add("Sahara");  
        Child3.add("Egypt");  
        ChildrenData.add(Child3);  
    }  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag);
        LoadListDate();  
        
        ExpandableListView myExpandableListView = (ExpandableListView)findViewById(R.id.expandableListView);  
        myExpandableListView.setAdapter(new ExpandableAdapter());
        myExpandableListView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				Log.d(TAG, "group postion : "+groupPosition);
				String tag = parent.getExpandableListAdapter().getGroup(groupPosition).toString();
				Log.d(TAG, "parent : "+tag);
				Intent resultIntent = new Intent();
		        resultIntent.putExtra("tag_name", tag);
		        setResult(RESULT_OK, resultIntent);
		    	finish();
				return false;
			}
        	
        });
	
	}
	
    private class ExpandableAdapter extends BaseExpandableListAdapter {  
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return ChildrenData.get(groupPosition).get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return 0;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
            TextView myText = null;  
            if (convertView != null) {  
                myText = (TextView)convertView;  
                myText.setText(ChildrenData.get(groupPosition).get(childPosition));  
            } else {  
                myText = createView(ChildrenData.get(groupPosition).get(childPosition));  
            }
            return myText;  
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return ChildrenData.get(groupPosition).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return GroupData.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return GroupData.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return 0;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
            TextView myText = null;  
            if (convertView != null) {  
                myText = (TextView)convertView;  
                myText.setText(GroupData.get(groupPosition));  
            } else {  
                myText = createView(GroupData.get(groupPosition));  
            }  
            return myText;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}
		
        private TextView createView(String content) {  
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(    
                    ViewGroup.LayoutParams.FILL_PARENT, 80);    
            TextView myText = new TextView(SelectTagActivity.this);    
            myText.setLayoutParams(layoutParams);    
            myText.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);    
            myText.setPadding(80, 0, 0, 0);    
            myText.setText(content);  
            return myText;  
        }
    }

}
