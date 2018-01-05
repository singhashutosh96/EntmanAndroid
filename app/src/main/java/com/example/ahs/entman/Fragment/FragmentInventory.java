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

public class FragmentInventory extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_inventory, container, false);

        TextView tv = (TextView) v.findViewById(R.id.tvFragInventory);
        tv.setText(getArguments().getString("msg"));

        return v;
    }

    public static FragmentInventory newInstance(String text) {

        FragmentInventory f = new FragmentInventory();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}
