package com.bayin.indicateprogress.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.bayin.indicateprogress.R;

import java.security.cert.PolicyNode;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/11/2.
 ****************************************/

public class OvalProgressView extends View {
    private static final String TAG = "---OvalProgressView---";

    //单位都是dip，init里面进行转换
    private int padding = 16;
    private int mRadius = 100;
    private int mTopSpace = 30;
    private int mStrokeWidth = 40;
    private int mIndicateLineLength = 180;//文字下面的横线长度
    private int mStartAngle = -180;

    private RectF mHelpRectF;
    private RectF mInnerRectF;
    private RectF rectF;
    private Paint mProgressPaint;
    private Paint mTextPaint;
    private Paint mHelpPaint;
    private Paint mPaint;
    private Path mLinePath;
    private Path mHelpCirCelPath;//辅助外圆的path
    private Path progressPath;
    private float mSweepAngle = 0;
    private float progress = 0;
    private float mHelpCircleRadius;
    private int mScreenWidth;
    private int mLeft;
    private int mRight;
    private float[] mPoint = new float[2];
    private float[] mTan = new float[2];
    private String mText = "进度0%";
    private Point mTextLocPoint = new Point();
    private Point mCenterPoint = new Point();

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        mSweepAngle = progress * 180;
        float percent = progress * 100;
        mText = "进度" + percent + "%";
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
        TypedArray attributes = context.obtainStyledAttributes(R.styleable.OvalProgressView);
        int anInt = attributes.getInt(R.styleable.OvalProgressView_ovalproress_radius, mRadius);
        mRadius = dip2Px(anInt);
        mTopSpace = dip2Px(mTopSpace);
        padding = dip2Px(padding);
        mStrokeWidth = (int) (mRadius * 0.4);
        mIndicateLineLength = (int) (mRadius * 0.6);
        attributes.recycle();

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mProgressPaint = new Paint();
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStrokeWidth(mStrokeWidth);

        mHelpPaint = new Paint();
        mHelpPaint.setStyle(Paint.Style.FILL);
        mHelpPaint.setColor(Color.parseColor("#ffffff"));

        mTextPaint = new Paint();
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(sp2px(12));
        mTextPaint.setColor(Color.parseColor("#888888"));

        mHelpCirCelPath = new Path();
        progressPath = new Path();
        mLinePath = new Path();

        Log.d(TAG, "半径：" + mRadius);
        mLeft = mScreenWidth / 2 - mRadius + mStrokeWidth / 2;
        mRight = mScreenWidth / 2 + mRadius - mStrokeWidth / 2;
        Log.d(TAG, "View Left = " + mLeft + "  Right = " + mRight);
        rectF = new RectF(mLeft, mStrokeWidth / 2 + mTopSpace, mRight, mRadius * 2 - mStrokeWidth / 2 + mTopSpace);
        //图形中心
        mCenterPoint.x = mScreenWidth / 2;
        mCenterPoint.y = (int) (rectF.top + mRadius);
        //辅助圆的半径
        mHelpCircleRadius = (rectF.right - rectF.left + mStrokeWidth) / 2 + dip2Px(8);//比进度圆环大8dip
        //内部圆的rect
        mInnerRectF = new RectF(this.rectF.left - mStrokeWidth / 2, this.rectF.top + mStrokeWidth / 2,
                this.rectF.right + mStrokeWidth / 2, this.rectF.bottom - mStrokeWidth / 2);
        //辅助圆的外围rect
        mHelpRectF = new RectF(this.rectF.centerX() - mHelpCircleRadius, this.rectF.centerY() - mHelpCircleRadius,
                this.rectF.centerX() + mHelpCircleRadius, this.rectF.centerY() + mHelpCircleRadius);

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
        progressPath.rewind();
        progressPath.arcTo(rectF, mStartAngle, mSweepAngle, false);
        //设置渐变
        mProgressPaint.setShader(new LinearGradient(0, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight(),
                Color.parseColor("#fdcca8"), Color.parseColor("#ff9330"), Shader.TileMode.CLAMP));
        canvas.drawPath(progressPath, mProgressPaint);

        //辅助外圆
        mPaint.setStrokeWidth(1);
        mPaint.setColor(Color.TRANSPARENT);
        mHelpCirCelPath.rewind();
        mHelpCirCelPath.addArc(mHelpRectF, mStartAngle, 180);
        canvas.drawPath(mHelpCirCelPath, mPaint);
        //获取关键点（指示线与辅助圆相交的一点）
        PathMeasure pathMeasure = new PathMeasure(mHelpCirCelPath, false);
        pathMeasure.getPosTan(getDistance(), mPoint, mTan);
        //指示线的path
        mLinePath.rewind();

        //绘制指示线
        mPaint.setStrokeWidth(4);
        mPaint.setColor(Color.parseColor("#ff6500"));
        if (progress <= 0.5) {
            mLinePath.moveTo(mPoint[0] - mIndicateLineLength, mPoint[1]);
            mTextLocPoint.x = (int) (mPoint[0] - mIndicateLineLength / 2);
        } else {
            mLinePath.moveTo(mPoint[0] + mIndicateLineLength, mPoint[1]);
            mTextLocPoint.x = (int) (mPoint[0] + mIndicateLineLength / 2);
        }
        //文字坐标的Y值
        if (progress > 0) {
            mTextLocPoint.y = (int) mPoint[1] - dip2Px(5);//文字y轴位置，线上5dip
            mLinePath.lineTo(mPoint[0], mPoint[1]);
            mLinePath.lineTo(rectF.centerX(), rectF.centerY());
            canvas.drawPath(mLinePath, mPaint);
        }
        //画文字信息
        canvas.drawText(mText, mTextLocPoint.x, mTextLocPoint.y, mTextPaint);
        //遮挡线条
        canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2 - mStrokeWidth / 2, mHelpPaint);

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
    }

    private int dip2Px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px(float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
