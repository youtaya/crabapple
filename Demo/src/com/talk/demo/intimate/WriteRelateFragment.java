package com.talk.demo.intimate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class WriteRelateFragment extends Fragment {
    private GridView gridView;
    private MentGridViewAdapter mentAdapter;
    String[] contents = {
            "I Miss You",
            "I Love You",
            "A U OK",
            "All Right",
            "Think It",
            "More, More"
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.relate_write, container, false);
        
        gridView = (GridView) rootView.findViewById(R.id.ment_content);
        mentAdapter = new MentGridViewAdapter(getActivity(), contents);
        gridView.setAdapter(mentAdapter);
        return rootView;
    }
}