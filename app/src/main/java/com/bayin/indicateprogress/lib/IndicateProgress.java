package com.bayin.indicateprogress.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.os.health.PackageHealthStats;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.bayin.indicateprogress.DensityUtil;
import com.bayin.indicateprogress.R;
import com.bayin.indicateprogress.ScreenUtils;
import com.bayin.indicateprogress.Utils;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/10/27.
 ****************************************/

public class IndicateProgress extends View {
    private final static String TAG = "IndicateProgress";
    private int mBackgroundColor;
    private int mTextSize;
    private int mTextColor;
    private int mProgress;
    private int mTotal = 100;
    private Style mStyle = Style.SIMPLE;
    private State mState = State.START;
    private Paint mTextPaint;
    private Paint mBackgroundPaint;
    private Path mPath;
    private int mViewWidth;
    private int mViewHeight;
    private int mScreenWidth;
    private int mScreenHeight;
    private int defaultHeight = 160;
    private int corner = 30;

    /**
     * 按钮的风格
     */
    enum Style {
        SIMPLE, DETAIL
    }

    /**
     * 按钮的三种状态
     */
    enum State {
        START, DOWNLOADING, CONTINUE;
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public void setBackground(int backgroundColor) {
        mBackgroundColor = backgroundColor;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int textSize) {
        mTextSize = textSize;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        mProgress = progress;
        invalidate();
    }

    public int getTotal() {
        return mTotal;
    }

    public void setTotal(int total) {
        mTotal = total;
    }

    public IndicateProgress(Context context) {
        this(context, null);
    }

    public IndicateProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicateProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray att = context.obtainStyledAttributes(attrs, R.styleable.IndicateProgress);
        mBackgroundColor = att.getColor(R.styleable.IndicateProgress_IndicateBackground, getResources().getColor(R.color.colorPrimaryDark));
        mTextSize = att.getInt(R.styleable.IndicateProgress_IndicateTextSize, 60);
        mTextColor = att.getColor(R.styleable.IndicateProgress_IndicateTextColor, getResources().getColor(R.color.colorPrimaryDark));
        att.recycle();
        //控件尺寸
        mScreenWidth = ScreenUtils.getScreenWidth(context);
        mScreenHeight = ScreenUtils.getScreenHeight(context);
        String layout_width = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_width");
        String layout_height = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");
        mViewWidth = DensityUtil.dip2px(context, layout_width) < 0 ? mScreenWidth : DensityUtil.dip2px(context, layout_width);
        mViewHeight = DensityUtil.dip2px(context, layout_height) < 0 ? defaultHeight : DensityUtil.dip2px(context, layout_height);
        //初始化
        initialize();
    }

    private void initialize() {
        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(mBackgroundColor);
        mBackgroundPaint.setAntiAlias(true);

        mPath = new Path();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画背景边框
        mBackgroundPaint.setStyle(Paint.Style.STROKE);
        mBackgroundPaint.setStrokeWidth(1);
        canvas.drawRoundRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), corner, corner, mBackgroundPaint);
        //获取文字path
        String string = "";
        switch (mState) {
            case START:
                string = getResources().getString(R.string.start_download);
                break;
            case CONTINUE:
                string = getResources().getString(R.string.stop_download);
                break;
            case DOWNLOADING:
                if (mStyle == Style.DETAIL) {
                    string = getResources().getString(R.string.continue_download);
                } else {
                    string = getProgress() + "/" + mTotal;
                }
                break;
        }
        Log.d(TAG, "文字：" + string);
        //矫正text位置
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float offset = (fontMetrics.descent - fontMetrics.ascent) / 4;
        mTextPaint.getTextPath(string, 0, string.length(), getMeasuredWidth() / 2, getMeasuredHeight() / 2 + offset, mPath);
        //添加实心矩形path
        float progress = Utils.div(mProgress, mTotal);
        float rectWidth = progress * mViewWidth;

        //裁剪画布
        Path path = new Path();
        path.addRoundRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), corner, corner, path_2_Direction);
        canvas.clipPath(path);

        Log.d(TAG, "矩形宽度：" + rectWidth);
        mPath.addRect(0, 0, rectWidth, getMeasuredHeight(), path_1_Direction);
//        mPath.addRoundRect(0, 0, rectWidth, mViewHeight, 16, 16, path_2_Direction);
        mPath.setFillType(path_type);
        mBackgroundPaint.setShader(new LinearGradient(0, getMeasuredHeight() / 2, rectWidth, getMeasuredHeight() / 2,
                Color.parseColor("#0288D1"), Color.parseColor("#7C4DFF"), Shader.TileMode.CLAMP));
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(mPath, mBackgroundPaint);

    }

    private Path.Direction path_1_Direction = Path.Direction.CW;
    private Path.Direction path_2_Direction = Path.Direction.CW;
    private Path.FillType path_type = Path.FillType.WINDING;

    public void setPathType(Path.Direction direction1, Path.Direction direction2, Path.FillType type) {
        this.path_1_Direction = direction1;
        this.path_2_Direction = direction2;
        this.path_type = type;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST);
        int height = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.AT_MOST);
        super.onMeasure(width, height);
    }
}
