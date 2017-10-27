package com.bayin.indicateprogress;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.bayin.indicateprogress.lib.IndicateProgress;

public class MainActivity extends AppCompatActivity {

    private IndicateProgress mIndicateProgress;
    private EditText mProgress;
    private int p = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //开始
                    p = p + 1;
                    mIndicateProgress.setProgress(p);
                    if (p < 100)
                        sendEmptyMessageDelayed(1, 100);
                    break;
                case 0:
                    //停止
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIndicateProgress = (IndicateProgress) findViewById(R.id.indicateProgress);
        mProgress = (EditText) findViewById(R.id.et_progress);
        findViewById(R.id.bt_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = mProgress.getText().toString();
                if (!TextUtils.isEmpty(string)) {
                    mIndicateProgress.setProgress(Integer.parseInt(string));
                }
            }
        });

        mHandler.sendEmptyMessage(1);
    }
}
