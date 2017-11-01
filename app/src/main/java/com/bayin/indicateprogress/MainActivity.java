package com.bayin.indicateprogress;

import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.bayin.indicateprogress.lib.IndicateProgress;

public class MainActivity extends AppCompatActivity {

    private IndicateProgress mIndicateProgress;
    private EditText mProgress;
    private Path.Direction path_1_Direction = Path.Direction.CCW;
    private Path.Direction path_2_Direction = Path.Direction.CCW;
    private Path.FillType path_type = Path.FillType.EVEN_ODD;
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
                        sendEmptyMessageDelayed(1, 10);
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
        RadioGroup radioGroup_1 = (RadioGroup) findViewById(R.id.radiogroup_1);
        RadioGroup radioGroup_2 = (RadioGroup) findViewById(R.id.radiogroup_2);
        RadioGroup radioGroup_3 = (RadioGroup) findViewById(R.id.radiogroup_3);
        radioGroup_1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.path_1_cw) {
                    path_1_Direction = Path.Direction.CW;
                } else {
                    path_1_Direction = Path.Direction.CCW;
                }
            }
        });

        radioGroup_2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.path_2_cw) {
                    path_2_Direction = Path.Direction.CW;
                } else {
                    path_2_Direction = Path.Direction.CCW;
                }
            }
        });

        radioGroup_3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.path_style_1:
                        path_type = Path.FillType.WINDING;
                        break;
                    case R.id.path_style_2:
                        path_type = Path.FillType.INVERSE_WINDING;
                        break;
                    case R.id.path_style_3:
                        path_type = Path.FillType.EVEN_ODD;
                        break;
                    case R.id.path_style_4:
                        path_type = Path.FillType.INVERSE_EVEN_ODD;
                        break;
                }
            }
        });

        findViewById(R.id.bt_style).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIndicateProgress.setStyle(IndicateProgress.Style.DETAIL);
            }
        });
        findViewById(R.id.bt_style_default).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIndicateProgress.setStyle(IndicateProgress.Style.SIMPLE);
            }
        });
    }

    public void restart(View view) {
        mIndicateProgress.setPathType(path_1_Direction, path_2_Direction, path_type);
        p = 0;
        mHandler.sendEmptyMessage(1);
    }
}
