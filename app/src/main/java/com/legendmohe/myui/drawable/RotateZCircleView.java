package com.legendmohe.myui.drawable;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by legendmohe on 16/6/13.
 */
public class RotateZCircleView extends RelativeLayout {

    private Paint mPaint;
//    private Paint mOtherPaint;
    private TimeInterpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private Camera mCamera;
    private AnimatorSet mAnimatorSet;
    private Circle[] mCircies;
    private int mStrokeWidth = 4;

    public RotateZCircleView(Context context) {
        super(context);
        init(null);
    }

    public RotateZCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RotateZCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setAntiAlias(true);

//        mOtherPaint = new Paint();
//        mOtherPaint.setColor(Color.RED);
//        mOtherPaint.setStyle(Paint.Style.STROKE);
//        mOtherPaint.setStrokeWidth(3);
//        mOtherPaint.setAntiAlias(true);

        mCamera = new Camera();

        int number = 5;
        int duration = 1000;
        int interval = duration / number;
        mCircies = new Circle[number];
        List<Animator> valueAnimators = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            final Circle circle = new Circle();
            mCircies[i] = circle;
            circle.rect = new RectF();

            ValueAnimator valueAnimator = new ValueAnimator();
            valueAnimator.setIntValues(0, 180);
            valueAnimator.setDuration(duration);
            valueAnimator.setInterpolator(mInterpolator);
            valueAnimator.setRepeatMode(ValueAnimator.RESTART);
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    circle.angle = (int) animation.getAnimatedValue();
                    invalidate();
                }
            });
            valueAnimators.add(valueAnimator);
            valueAnimator.setStartDelay(50*i);
        }
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(valueAnimators);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAnimatorSet.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAnimatorSet.cancel();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        for (Circle circle :
                mCircies) {
            circle.rect.set(mStrokeWidth, mStrokeWidth, w - 2*mStrokeWidth, h - 2*mStrokeWidth);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        final float centerX = getWidth() / 2;
        final float centerY = getHeight() / 2;
        final Camera camera = mCamera;

        for (Circle circle :
                mCircies) {
            canvas.save();

            Matrix matrix = canvas.getMatrix();

            camera.save();
            camera.translate(0.0f, 0.0f, 0.0f);
            camera.rotateX(circle.angle);
            camera.getMatrix(matrix);
            camera.restore();

            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);

            canvas.concat(matrix);
            canvas.drawArc(circle.rect, 180, 180, false, mPaint);

            canvas.restore();


        }

        super.dispatchDraw(canvas);

        for (Circle circle :
                mCircies) {
            canvas.save();

            Matrix matrix = canvas.getMatrix();

            camera.save();
            camera.translate(0.0f, 0.0f, 0.0f);
            camera.rotateX(circle.angle);
            camera.getMatrix(matrix);
            camera.restore();

            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);

            canvas.concat(matrix);
            canvas.drawArc(circle.rect, 0, 180, false, mPaint);

            canvas.restore();
        }
    }

    public static class Circle {
        int angle;
        RectF rect;
    }
}
