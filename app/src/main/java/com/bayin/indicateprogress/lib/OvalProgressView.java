package com.bayin.indicateprogress.lib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/11/2.
 ****************************************/

public class OvalProgressView extends View {
    private static final String TAG = "---OvalProgressView---";

    private Paint mPaint;
    private int padding = 16;
    private int mRadius = 400;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mLeft;
    private int mRight;
    private int mStrokeWidth = 200;
    private int mStartAngle = -180;
    private int zero = 0;

    public OvalProgressView(Context context) {
        this(context, null);
    }

    public OvalProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OvalProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mScreenWidth = wm.getDefaultDisplay().getWidth();
        mScreenHeight = wm.getDefaultDisplay().getHeight();

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    //背景色 #F7F7F7

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.parseColor("#F7F7F7"));
        mPaint.setStrokeWidth(100);
        canvas.drawArc(mLeft, mStrokeWidth / 2, mRight, mRadius * 2 - mStrokeWidth / 2,
                mStartAngle, 180, false, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST);
        int height = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.AT_MOST);
        super.onMeasure(width, height);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int minMeasure = Math.min(measuredHeight, measuredWidth);
        mRadius = Math.min(mRadius, minMeasure);
        Log.d(TAG, "测量半径：" + mRadius);
        mLeft = mScreenWidth / 2 - mRadius + mStrokeWidth / 2;
        mRight = mScreenWidth / 2 + mRadius - mStrokeWidth / 2;
        Log.d(TAG, "View Left = " + mLeft + "  Right = " + mRight);
    }
}
