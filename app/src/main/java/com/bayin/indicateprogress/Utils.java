package com.bayin.indicateprogress;

import android.util.Log;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/10/24.
 */

public class Utils {

    public static float div(int x, int y) {
        if (y <= 0) return 0;
        try {
            DecimalFormat df = new DecimalFormat("0.00");
            float number = (float) x / y;
            String format = df.format(number);
            float v = Float.parseFloat(format);
            Log.w("除结果", v + "");
            return v;
        } catch (Exception e) {
            return 0;
        }
    }
}
