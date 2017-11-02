package com.bayin.indicateprogress.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.bayin.indicateprogress.R;
import com.bayin.indicateprogress.lib.OvalProgressView;
import com.bayin.indicateprogress.lib.Utils;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/11/2.
 ****************************************/

public class HomeFragment extends Fragment {

    private OvalProgressView ovalView;
    private EditText etProgress;
    private EditText et2;
    private EditText et1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, null, false);
        ovalView = (OvalProgressView) rootView.findViewById(R.id.ovalView);
        etProgress = (EditText) rootView.findViewById(R.id.et_progress);
        rootView.findViewById(R.id.bt_setprogress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String progress = etProgress.getText().toString();
                try {
                    ovalView.setProgress(Float.parseFloat(progress));

                } catch (Exception e) {
                    ovalView.setProgress(90);
                }
            }
        });
        et1 = (EditText) rootView.findViewById(R.id.et_1);
        et2 = (EditText) rootView.findViewById(R.id.et_2);
        rootView.findViewById(R.id.bt_result).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numb1 = et1.getText().toString();
                String numb2 = et2.getText().toString();
                float circleLength = Utils.getLengthByPercent(Integer.parseInt(numb1), Float.parseFloat(numb2));
                Toast.makeText(getActivity(), "结果" + circleLength, Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

}
