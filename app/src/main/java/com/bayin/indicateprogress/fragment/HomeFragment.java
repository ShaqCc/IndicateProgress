package com.bayin.indicateprogress.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bayin.indicateprogress.R;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/11/2.
 ****************************************/

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, null, false);
        return rootView;
    }
}
