package com.example.waveview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import java.util.Random;

public class WaveView extends View {
    private Paint mPaint;
    private Path mPath;
    //平移偏移量
    private int offset;

    /**
     * 一个水波纹长度
     */
    private int mWL = 1000;
    /**
     * 水波纹个数
     */
    private int mWaveCount;
    private int mCenterY;

    //屏幕高度
    private int mScreenHeight;
    //屏幕宽度
    private int mScreenWidth;
    private Paint linePaint;


    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        initAnimator();
    }

    private void initPaint() {
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#59c3e2"));
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mScreenWidth = w;
        mScreenHeight = h;
        mCenterY = mScreenHeight / 2;
        mWaveCount = (int) Math.round(mScreenWidth / mWL + 1.5);
    }

    private void initAnimator() {
        ValueAnimator animator = ValueAnimator.ofInt(0, mWL);
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /** 清除上次画的路径 */
        mPath.reset();
        /** 让波浪升高 */
        mCenterY-=1;
        if(mCenterY<0){
            mCenterY=mScreenHeight/2;
        }
        mPath.moveTo(-mWL + offset, mCenterY);
        //100就是波浪高度
        for (int i = 0; i < mWaveCount; i++) {
            mPath.quadTo(-mWL * 3 / 4 + (i*mWL)+offset, mCenterY + 100, -mWL / 2 +(i*mWL)+offset, mCenterY);
            mPath.quadTo(-mScreenWidth / 4 + (i*mWL)+offset, mCenterY - 100, 0 + (i*mWL)+offset, mCenterY);
        }
        mPath.lineTo(mScreenWidth, mScreenHeight);
        mPath.lineTo(0,mScreenHeight);
        mPath.close();
        canvas.drawPath(mPath, mPaint);

}
}
