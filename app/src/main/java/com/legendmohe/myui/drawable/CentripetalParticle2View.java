package com.legendmohe.myui.drawable;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by legendmohe on 16/6/13.
 */
public class CentripetalParticle2View extends RelativeLayout {

    private Paint mPaint;
    private TimeInterpolator mInterpolator = new LinearInterpolator();
    private AnimatorSet mAnimatorSet;
    private ParticleCircle[] mParticleCircles;

    private static Random sRandom = new Random();
    private int mNumber;
    private int mDotNumber;
    private int mHeight;
    private int mWidth;
    private int mMaxRadius;
    private int mMinRadius;
    private int mMaxStroke;
    private int mMinStroke;
    private boolean mStopped;
    private int mDuration;

    public CentripetalParticle2View(Context context) {
        super(context);
        init(null);
    }

    public CentripetalParticle2View(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CentripetalParticle2View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        mMaxRadius = 500;
        mMinRadius = 200;
        mMaxStroke = 8;
        mMinStroke = 4;
        mNumber = 6;
        mDotNumber = 36;
        mDuration = 3000;

        mStopped = true;
    }

    public void setupAnimation() {
        mParticleCircles = new ParticleCircle[mNumber];
        int interval = mDuration / mNumber;
        List<Animator> valueAnimators = new ArrayList<>();
        for (int i = 0; i < mNumber; i++) {
            final ParticleCircle particle = new ParticleCircle();
            particle.baseX = mWidth/2;
            particle.baseY = mHeight/2;
            mParticleCircles[i] = particle;

            ValueAnimator valueAnimator = new ValueAnimator();
            valueAnimator.setIntValues(mMaxRadius, mMinRadius);
            valueAnimator.setDuration(mDuration);
            valueAnimator.setInterpolator(mInterpolator);
            valueAnimator.setRepeatMode(ValueAnimator.RESTART);
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float persent = animation.getAnimatedFraction();
//                    particle.angle += 1;
                    particle.radius = (int) animation.getAnimatedValue();
                    particle.alpha = (int) (255*persent);
                    particle.stroke = (int) (mMinStroke + (mMaxStroke - mMinStroke)*persent);
                    invalidate();
                }
            });

            valueAnimators.add(valueAnimator);
            valueAnimator.setStartDelay(interval * i);
        }
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(valueAnimators);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void stop() {
        if (!mStopped) {
            mStopped = true;
            for (Animator animator : mAnimatorSet.getChildAnimations()) {
                ValueAnimator valueAnimator = (ValueAnimator) animator;
                valueAnimator.setRepeatCount(0);
            }
        }
    }

    public void start() {
        if (mStopped) {
            mStopped = false;
            setupAnimation();
            mAnimatorSet.start();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mParticleCircles != null && mParticleCircles.length != 0) {
            float angle = 360*1.0f/mDotNumber;
            for (ParticleCircle particle : mParticleCircles) {
                canvas.save();

                canvas.rotate((float) particle.angle, particle.baseX, particle.baseY);

                mPaint.setAlpha(particle.alpha);
                for (int i = 0; i < mDotNumber; i++) {
                    canvas.rotate(angle, particle.baseX, particle.baseY);
                    canvas.drawCircle(particle.baseX + particle.radius, particle.baseY, particle.stroke, mPaint);
                }

                canvas.restore();
            }
        }

        super.dispatchDraw(canvas);
    }

    public static class ParticleCircle {
        public int alpha;
        public int stroke;
        public double angle;
        public int radius;
        public int baseRadius;
        public int baseX;
        public int baseY;

        public int getCenterX() {
            return baseX + (int) (radius * Math.cos(angle));
        }

        public int getCenterY() {
            return baseY + (int) (radius * Math.sin(angle));
        }
    }
}
