package com.mqt.solarprogressview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;


public class SolarView extends View {

    private int mColor = Color.RED;
    private int mMax = 9;
    private int mPregress = 0;

    private float mLolarScale;
    private int mLightRadius;
    private ObjectAnimator mScaleAnim;
    private int mLightRadiusAnim;
    private ObjectAnimator mColorAnim;

    public SolarView(Context context) {
        super(context);
        init(null, 0);
    }

    public SolarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SolarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.SolarView, defStyle, 0);

        mColor = a.getColor(R.styleable.SolarView_color, Color.RED);
        mMax = a.getInt(R.styleable.SolarView_max, 9);
        mPregress = a.getInt(R.styleable.SolarView_pregress, 0);
        mLolarScale = a.getFloat(R.styleable.SolarView_solarScale, 0.25F);
        mLightRadius = a.getDimensionPixelSize(R.styleable.SolarView_lightRadius, 10);
        mLightRadiusAnim = mLightRadius;
        a.recycle();
        initPaint();
        initAnim();
    }

    private void initAnim() {
        mScaleAnim = ObjectAnimator
                .ofInt(this, "mLightRadiusAnim", mLightRadius / 2, mLightRadius, mLightRadius * 2, mLightRadius);
        mScaleAnim.setDuration(1200);//设置动画时间
        mScaleAnim.setInterpolator(new DecelerateInterpolator());//设置动画插入器，减速
        mScaleAnim.setRepeatCount(0);//设置动画重复次数，这里-1代表无限
        mScaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                mLightRadiusAnim = value;
                postInvalidate();
            }
        });

        mColorAnim = ObjectAnimator
                .ofArgb(this, "mColor", mColor, Color.rgb(250, 128, 10), mColor);
        mColorAnim.setDuration(1200);//设置动画时间
        mColorAnim.setInterpolator(new DecelerateInterpolator());//设置动画插入器，减速
        mColorAnim.setRepeatCount(0);//设置动画重复次数，这里-1代表无限
        mColorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                mColor = value;
                postInvalidate();
            }
        });
    }

    private int radumColor() {
        return Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
    }

    Paint mPaint;

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //实心圆，直径为控件的最小宽高的一半
        int min = Math.min(getWidth(), getHeight());
        float r = min * mLolarScale;
        mPaint.setColor(mColor);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, r, mPaint);
        for (int i = 0; i < mMax && i < mPregress; i++) {
            canvas.save();
            canvas.rotate((360f / mMax) * i, getWidth() >> 1, getHeight() >> 1);
            if (i == mPregress - 1) {
                canvas.drawCircle(getWidth() >> 1, getHeight() >> 3, mLightRadiusAnim, mPaint);
            } else {
                canvas.drawCircle(getWidth() >> 1, getHeight() >> 3, mLightRadius, mPaint);
            }
            canvas.restore();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 0);
        int minh = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int h = resolveSizeAndState(minh, heightMeasureSpec, 0);
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(@ColorInt int color) {
        this.mColor = color;
        postInvalidate();
    }

    public int getMax() {
        return mMax;
    }

    public void setMax(@IntRange(from = 0) int max) {
        this.mMax = max;
        postInvalidate();
    }

    public int getPregress() {
        return mPregress;
    }

    public void setPregress(@IntRange(from = 0) int pregress) {
        if (pregress > this.mPregress) {
            mColorAnim.cancel();
            mColorAnim.start();
            mScaleAnim.cancel();
            mScaleAnim.start();//启动动画
        }
        this.mPregress = pregress;

    }

    public float getLolarScale() {
        return mLolarScale;
    }

    public void setLolarScale(@FloatRange(from = 0, to = 1) float lolarScale) {
        this.mLolarScale = lolarScale;
        postInvalidate();
    }

    public int getLightRadius() {
        return mLightRadius;
    }

    public void setLightRadius(@IntRange(from = 0) int lightRadius) {
        this.mLightRadius = lightRadius;
        postInvalidate();
    }
}
