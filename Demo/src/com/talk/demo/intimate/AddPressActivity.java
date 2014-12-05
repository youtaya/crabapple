package com.talk.demo.intimate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.talk.demo.R;

public class AddPressActivity extends FragmentActivity {
	private static String TAG = "AddPressActivity";
	private TextView me_write, me_step;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_press);
        
        me_step = (TextView) findViewById(R.id.step_some);
        me_step.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				Intent intent = new Intent(AddPressActivity.this, EditIntimateActivity.class); 
				Bundle bundle = new Bundle();
				bundle.putInt("STEP", 1);
				intent.putExtras(bundle);
	            setResult(RESULT_OK, intent);
	    		finish();
	    		*/
				switchContent(1);
			}
        	
        });
        
        me_write = (TextView) findViewById(R.id.say_some);
        me_write.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switchContent(2);
			}
        	
        });
        
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            WriteRelateFragment firstFragment = new WriteRelateFragment();
            
            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            //firstFragment.setArguments(getIntent().getExtras());
            
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }
    }
    
    
    public void switchContent(int id) {
     // Create fragment and give it an argument specifying the article it should show
    	Fragment newFragment; 
    	if(id == 1) {
    		newFragment = new StepRelateFragment();
    	} else {
    		newFragment = new WriteRelateFragment();
    	}
        
        /*
        Bundle args = new Bundle();
        args.putInt(ArticleFragment.ARG_POSITION, position);
        newFragment.setArguments(args);
        */

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

}
