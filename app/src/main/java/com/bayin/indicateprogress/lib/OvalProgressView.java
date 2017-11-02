package com.bayin.indicateprogress.lib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.RectF;
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
    private int mRadius = 340;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mLeft;
    private int mRight;
    private int mStrokeWidth = (int) (mRadius*0.3);
    private int mStartAngle = -180;
    private int zero = 0;
    private float progress = 0;
    private int mTopSpace = 150;
    private Path path;
    private RectF rectF;
    private Point mCenterPoint = new Point();
    private Path progressPath;
    private float[] mPoint = new float[2];
    private float[] mTan = new float[2];
    private float mHelpCircleRadius;

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

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

        path = new Path();
        progressPath = new Path();

        Log.d(TAG, "半径：" + mRadius);
        mLeft = mScreenWidth / 2 - mRadius + mStrokeWidth / 2;
        mRight = mScreenWidth / 2 + mRadius - mStrokeWidth / 2;
        Log.d(TAG, "View Left = " + mLeft + "  Right = " + mRight);
        rectF = new RectF(mLeft, mStrokeWidth / 2 + mTopSpace, mRight, mRadius * 2 - mStrokeWidth / 2 + mTopSpace);
        //图形中心
        mCenterPoint.x = mScreenWidth / 2;
        mCenterPoint.y = (int) (rectF.top + mRadius);
        //辅助圆的半径
        mHelpCircleRadius = (rectF.right - rectF.left + mStrokeWidth * 2) / 2;
    }

    //背景色 #F7F7F7

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.parseColor("#F7F7F7"));
        mPaint.setStrokeWidth(mStrokeWidth);
        drawProgress(canvas, 180);
        //绘制进度
        mPaint.setColor(Color.parseColor("#ff6500"));
//        drawProgress(canvas, progress);
        progressPath.rewind();
        progressPath.arcTo(rectF, mStartAngle, progress, false);
        canvas.drawPath(progressPath, mPaint);

        //辅助矩形
//        mPaint.setStrokeWidth(2);
//        mPaint.setColor(Color.RED);
//        canvas.drawRect(rectF,mPaint);
        //辅助外圆
        mPaint.setStrokeWidth(1);
        mPaint.setColor(Color.BLACK);
        path.rewind();
        path.addArc(rectF.left - mStrokeWidth, rectF.top - mStrokeWidth, rectF.right + mStrokeWidth, rectF.bottom + mStrokeWidth, mStartAngle, 180);
        canvas.drawPath(path, mPaint);


        PathMeasure pathMeasure = new PathMeasure(path, false);

        pathMeasure.getPosTan(getDistance(), mPoint, mTan);
        //draw Line
        if (progress <= 0.5) {
            canvas.drawLine(0, mPoint[1], mPoint[0], mPoint[1], mPaint);
        } else {
            canvas.drawLine(mPoint[0], mPoint[1], mScreenWidth, mPoint[1], mPaint);
        }
        //圆心
//        mPaint.setStrokeWidth(5);
//        canvas.drawPoint(mCenterPoint.x, getMeasuredHeight() - padding, mPaint);
        //bottom线
//        mPaint.setStrokeWidth(1);
//        canvas.drawLine(0, getMeasuredHeight() - padding, mScreenWidth, getMeasuredHeight() - padding, mPaint);

    }

    private void drawProgress(Canvas canvas, int sweepAngle) {
        canvas.drawArc(rectF, mStartAngle, sweepAngle, false, mPaint);
    }

    private float getDistance() {
        return Utils.getLengthByPercent((int) mHelpCircleRadius, progress);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST);
        //计算view高度
        int height = MeasureSpec.makeMeasureSpec(mTopSpace + mRadius + padding, MeasureSpec.EXACTLY);
        super.onMeasure(width, height);
//        int measuredWidth = getMeasuredWidth();
//        int measuredHeight = getMeasuredHeight();
//        int minMeasure = Math.min(measuredHeight, measuredWidth);
//        mRadius = Math.min(mRadius, minMeasure);
    }
}
