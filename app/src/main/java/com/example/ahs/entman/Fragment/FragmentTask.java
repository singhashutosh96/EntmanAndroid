package com.example.ahs.entman.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ahs.entman.R;

/**
 * Created by ashu on 05-01-2018.
 */

public class FragmentTask extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task, container, false);

        TextView tv = (TextView) v.findViewById(R.id.tvFragTask);
        tv.setText(getArguments().getString("msg"));

        return v;
    }

    public static FragmentTask newInstance(String text) {

        FragmentTask f = new FragmentTask();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}
