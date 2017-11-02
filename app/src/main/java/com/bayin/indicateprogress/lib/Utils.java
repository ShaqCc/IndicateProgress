package com.bayin.indicateprogress.lib;

import android.util.Log;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/11/3.
 */

public class Utils {
    private static final String TAG = "---utils---";

    /**
     * 求半圆周长
     *
     * @param radius
     * @return
     */
    public static float getCircleLength(int radius, boolean isHalf) {
        BigDecimal decimalRadius = new BigDecimal(radius);
        BigDecimal pi = new BigDecimal(Math.PI);
        BigDecimal halfLegth = decimalRadius.multiply(pi);
        Log.d(TAG, "周长第一步:" + halfLegth);
        BigDecimal result = halfLegth.setScale(1, BigDecimal.ROUND_HALF_DOWN);
        Log.d(TAG, "周长第二步:" + result.floatValue());
        if (isHalf) {
            return result.floatValue();
        } else return result.floatValue() * 2;

    }

    public static float getLengthByPercent(int radius, float percent) {
        float circleLength = getCircleLength(radius, true);
        BigDecimal total = new BigDecimal(circleLength);
        BigDecimal per = new BigDecimal(percent);
        BigDecimal multiply = total.multiply(per);
        Log.d(TAG, "长度第一步:" + multiply);
        BigDecimal result = multiply.setScale(1, BigDecimal.ROUND_HALF_UP);
        Log.d(TAG, "长度第二步:" + result.floatValue());
        return result.floatValue();
    }
}
