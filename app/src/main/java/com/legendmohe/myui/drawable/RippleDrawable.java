package com.legendmohe.myui.drawable;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.legendmohe.myui.AnimationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by legendmohe on 16/6/12.
 */
public class RippleDrawable extends Drawable {


    private AnimatorSet mAnimatorSet;
    private Paint mPaint;
    private int mDuration;
    private int mMaxRadius;
    private int mMinRadius;
    private int mNumber;
    private Circle[] mCircies;

    public RippleDrawable(int maxRadius, int minRadius, int number, int duration, boolean startNow) {
        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(3);
        mPaint.setAntiAlias(true);

        mMaxRadius = maxRadius;
        mMinRadius = minRadius;
        mNumber = number;
        mCircies = new Circle[mNumber];
        mDuration = duration;
        int interval = mDuration / mNumber;
        List<Animator> valueAnimators = new ArrayList<>();
        for (int i = 0; i < mNumber; i++) {
            final Circle circle = new Circle();
            mCircies[i] = circle;
            circle.radius = 0;

            ValueAnimator valueAnimator = new ValueAnimator();
            valueAnimator.setIntValues(mMinRadius, mMaxRadius);
            valueAnimator.setDuration(duration);
            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            valueAnimator.setRepeatMode(ValueAnimator.RESTART);
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    circle.radius = (int) animation.getAnimatedValue();
                    invalidateSelf();
                }
            });
            valueAnimators.add(valueAnimator);
            valueAnimator.setStartDelay(interval * i);
        }
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.addListener(new AnimatorListenerAdapter() {
            public boolean mAnimationStarted;

            @Override
            public void onAnimationStart(Animator animation) {
                mAnimationStarted = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimationStarted = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (mAnimationStarted) {
                    animateToDisappear();
                }
                mAnimationStarted = false;
            }
        });
        mAnimatorSet.playTogether(valueAnimators);
        mAnimatorSet.setStartDelay(500);
        if (startNow) {
            mAnimatorSet.start();
        }
    }

    private void animateToDisappear() {
        List<Animator> valueAnimators = new ArrayList<>();
        for (int i = 0; i < mNumber; i++) {
            final Circle circle = mCircies[i];
            if (circle.radius <= mMinRadius)
                continue;

            ValueAnimator valueAnimator = new ValueAnimator();
            valueAnimator.setIntValues(circle.radius, mMaxRadius);
            valueAnimator.setDuration((long) (mDuration * (1 - (circle.radius - mMinRadius) * 1.0 / (mMaxRadius - mMinRadius))));
            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    circle.radius = (int) animation.getAnimatedValue();
                    invalidateSelf();
                }
            });
            valueAnimators.add(valueAnimator);
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(valueAnimators);
        animatorSet.start();
    }

    @Override
    public void draw(Canvas canvas) {
        for (Circle circle :
                mCircies) {
            if (circle.radius >= mMinRadius) {
                mPaint.setAlpha((int) (255 * (1 - circle.radius * 1.0 / mMaxRadius)));
                Rect rect = getBounds();
                canvas.drawCircle(rect.centerX(), rect.centerY(), circle.radius, mPaint);
            }
        }
    }

    @Override
    public int getIntrinsicWidth() {
        return mMaxRadius;
    }

    @Override
    public int getIntrinsicHeight() {
        return mMaxRadius;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void startAnimation() {
        if (!mAnimatorSet.isRunning()) {
            mAnimatorSet.start();
        }
    }

    public void stopAnimation() {
        mAnimatorSet.cancel();
    }

    public static class Circle {
        int radius;
    }
}
