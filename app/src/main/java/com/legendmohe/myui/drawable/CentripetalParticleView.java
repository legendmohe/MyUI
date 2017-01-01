package com.legendmohe.myui.drawable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by legendmohe on 16/6/13.
 */
public class CentripetalParticleView extends RelativeLayout {

    private static double sLOG2 = Math.log(2);
    private static Random sRandom = new Random();

    private Paint mPaint;
    private TimeInterpolator mInterpolator = new LinearInterpolator();
    private AnimatorSet mAnimatorSet;
    private Particle[] mParticles;

    private int mNumber;
    private int mHeight;
    private int mWidth;
    private int mMaxRadius = Integer.MAX_VALUE;
    private int mMinRadius = 0;
    private boolean mStopped;
    private int mDuration;
    private int mMaxRadiusVelocity;
    private int mMinRadiusVelocity;
    private float mMaxAngleVelocity;
    private float mMinAngleVelocity;
    private int mMaxStroke;
    private int mMinStroke;

    public CentripetalParticleView(Context context) {
        super(context);
        init(null);
    }

    public CentripetalParticleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CentripetalParticleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mStopped = true;

        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        mNumber = 200;
        mMaxRadius = 700;
        mMinRadius = 400;
        mDuration = 1000;
        mMaxRadiusVelocity = 6;
        mMinRadiusVelocity = 2;
        mMaxAngleVelocity = 0.01f;
        mMinAngleVelocity = 0.001f;
        mMaxStroke = 6;
        mMinStroke = 2;
    }

    private void setupAnimation() {
        int delay = (int) (mDuration * 1.0 / mNumber);
        mParticles = new Particle[mNumber];
        List<Animator> valueAnimators = new ArrayList<>();
        for (int i = 0; i < mNumber; i++) {
            final Particle particle = new Particle();

            ValueAnimator valueAnimator = new ValueAnimator();
            valueAnimator.setIntValues(0, 1000);
            valueAnimator.setDuration(mDuration);
            valueAnimator.setInterpolator(mInterpolator);
            valueAnimator.setRepeatMode(ValueAnimator.RESTART);
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (particle.radius <= 0) {
                        if (mStopped) {
                            animation.setRepeatCount(0);
                        } else {
                            resetParticle(particle);
                        }
                    }
                    invalidate();
                }
            });
            final int finalI = i;
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mParticles[finalI] = particle;
                    resetParticle(particle);
                }
            });
            valueAnimator.setStartDelay(delay * i);
            valueAnimators.add(valueAnimator);
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
        }
    }

    public void start() {
        if (mStopped) {
            mStopped = false;
            if (mAnimatorSet != null && mAnimatorSet.getChildAnimations().size() != 0) {
                for (Animator animator : mAnimatorSet.getChildAnimations()) {
                    ValueAnimator valueAnimator = (ValueAnimator) animator;
                    valueAnimator.cancel();
                }
            }
            setupAnimation();
            mAnimatorSet.start();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
    }

    private void resetParticle(Particle particle) {
        int maxRange = (int) Math.sqrt(mWidth / 2.0 * mWidth / 2.0 + mHeight / 2.0 * mHeight / 2.0);
        int range = Math.min(mMaxRadius, maxRange);

        int distance = sRandom.nextInt(range - mMinRadius) + mMinRadius;
        particle.radius = distance;

        particle.stroke = sRandom.nextInt(mMaxStroke - mMinStroke) + mMinStroke;

        particle.baseRadius = particle.radius;
        particle.radial_velocity = sRandom.nextInt(mMaxRadiusVelocity - mMinRadiusVelocity) + mMinRadiusVelocity;

        particle.angle = sRandom.nextFloat() * Math.PI * 2;
        particle.angle_velocity = sRandom.nextFloat()*(mMaxAngleVelocity - mMinAngleVelocity) + mMinAngleVelocity;

        particle.baseX = mWidth / 2;
        particle.baseY = mHeight / 2;
    }

    public void setParticleNumber(int number) {
        if (number == mNumber)
            return;

        Particle[] newParticles = new Particle[number];
        System.arraycopy(mParticles, 0, newParticles, 0, Math.min(number, mNumber));
        mParticles = newParticles;

        if (number > mNumber) {
            for (int i = mNumber; i < number; i++) {
                final Particle particle = new Particle();
                addParticle(i, particle);
            }
        } else {
            for (int i = number; i < mNumber; i++) {
                mAnimatorSet.getChildAnimations().get(i).cancel();
            }
        }

        mNumber = number;
    }

    private void addParticle(int indexInParticles, final Particle particle) {
        int delay = (int) (mDuration * 1.0 / mNumber);

        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(0, 1000);
        valueAnimator.setDuration(mDuration);
        valueAnimator.setInterpolator(mInterpolator);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (particle.radius <= 0) {
                    if (mStopped) {
                        animation.setRepeatCount(0);
                    } else {
                        resetParticle(particle);
                    }
                }
                invalidate();
            }
        });
        final int finalI = indexInParticles;
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mParticles[finalI] = particle;
                resetParticle(particle);
            }
        });
        valueAnimator.setStartDelay(delay * indexInParticles);
        mAnimatorSet.play(valueAnimator);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mParticles != null && mParticles.length != 0) {
            for (int i = 0; i < mNumber; i++) {
                Particle particle = mParticles[i];
                if (particle == null) continue;

                canvas.save();

                particle.radius -= particle.radial_velocity;
                particle.radius = Math.max(particle.radius, 0);
                particle.radial_velocity *= 1.01;
                particle.angle += particle.angle_velocity;
//                mPaint.setAlpha((int) (255 * (1 - particle.radius * 1.0 / particle.baseRadius)));
                mPaint.setAlpha((int) (255 * Math.log(1 - particle.radius * 1.0 / particle.baseRadius + 1)/sLOG2));
                canvas.drawCircle(particle.getCenterX(), particle.getCenterY(), particle.stroke, mPaint);

                canvas.restore();
            }
        }

        super.dispatchDraw(canvas);
    }

    public static class Particle {
        public int stroke;
        public double angle;
        public int radius;
        public int baseRadius;
        public double radial_velocity;
        public double angle_velocity;
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
