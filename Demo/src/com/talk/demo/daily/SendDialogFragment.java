package com.talk.demo.daily;

import android.R;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class SendDialogFragment extends DialogFragment implements OnClickListener {
    private static String TAG = "SendDialogFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.send_dialog, container, false);
        Button meBtn = (Button)v.findViewById(R.id.share_dialog_send_to_me); 
        meBtn.setOnClickListener(this);          
        Button otherBtn = (Button)v.findViewById(R.id.share_dialog_send_to_other); 
        otherBtn.setOnClickListener(this);          
        Button tagBtn = (Button)v.findViewById(R.id.share_dialog_send_to_tag); 
        tagBtn.setOnClickListener(this); 
        return v;
    }
    
    @Override
    public void onClick(View v) {  
        switch(v.getId()){ 
        case R.id.share_dialog_send_to_me:
            Log.d(TAG, "send to me");
            dismiss();
            break; 
        case R.id.share_dialog_send_to_other:
            Log.d(TAG, "send to other");
            dismiss();
            break; 
        case R.id.share_dialog_send_to_tag:
            Log.d(TAG, "send to tag");
            dismiss();
            break; 
        default: 
            break; 
        } 
    } 
}