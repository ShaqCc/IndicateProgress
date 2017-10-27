package com.bayin.indicateprogress;

import android.content.Context;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/9/21.
 ****************************************/

public class DensityUtil {
    /**
     * 把 xxxdip转成px
     *
     * @param context
     * @param value
     * @return
     */
    public static int dip2px(Context context, String value) {
        if (value.contains("dip")) {
            String dip = value.replace("dip", "");
            float dp = Float.parseFloat(dip);
            return dip2px(context, dp);
        }
        return -1;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
