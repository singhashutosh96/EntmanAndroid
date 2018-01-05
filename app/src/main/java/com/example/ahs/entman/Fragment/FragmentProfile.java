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

public class FragmentProfile extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView tv = (TextView) v.findViewById(R.id.tvFragProfile);
        tv.setText(getArguments().getString("msg"));

        return v;
    }

    public static FragmentProfile newInstance(String text) {

        FragmentProfile f = new FragmentProfile();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}
