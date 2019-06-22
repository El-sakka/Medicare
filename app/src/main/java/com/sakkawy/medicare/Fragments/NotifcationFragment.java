package com.sakkawy.medicare.Fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sakkawy.medicare.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotifcationFragment extends Fragment {


    public NotifcationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifcation, container, false);
    }

}
