package com.legendmohe.myui.drawable;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by legendmohe on 16/6/14.
 */
public class RubberView extends View {

    private ValueAnimator mYValueAnimator;
    private ValueAnimator mXValueAnimator;
    private AnimatorSet mAnimatorSet;
    private int mCenterX, mCenterY;
    private Paint mPaint;
    private PointF mControlPoint;
    private PointF mStartPoint;
    private PointF mEndPoint;

    public RubberView(Context context) {
        super(context);
        init();
    }

    public RubberView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RubberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mControlPoint = new PointF(0,0);
        mStartPoint = new PointF(0,0);
        mEndPoint = new PointF(0,0);

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(8);
        mPaint.setStyle(Paint.Style.STROKE);

//        Path path = new Path();
//        path.moveTo(0, 0);
//        path.cubicTo(0.04f, 0.97f, 0.91f, 0.07f, 1f, 1f);

        mYValueAnimator = new ValueAnimator();
        mYValueAnimator.setDuration(1000);
        mYValueAnimator.setInterpolator(new DampingInterpolator(5, 0.3f));
        mYValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mControlPoint.y = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mXValueAnimator = new ValueAnimator();
        mXValueAnimator.setDuration(1000);
        mXValueAnimator.setInterpolator(new DampingInterpolator(5, 0.3f));
        mXValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mControlPoint.x = (float) animation.getAnimatedValue();
//                invalidate();
            }
        });

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(mYValueAnimator, mXValueAnimator);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mCenterX = w/2;
        mCenterY = h/2;
        mControlPoint.set(mCenterX, mCenterY);
        mStartPoint.set(0, mCenterY);
        mEndPoint.set(w, mCenterY);
        mYValueAnimator.setFloatValues(h, mCenterY);
        mXValueAnimator.setFloatValues(mCenterX, mCenterX);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        path.moveTo(mStartPoint.x,mStartPoint.y);
        path.quadTo(mControlPoint.x,mControlPoint.y,mEndPoint.x,mEndPoint.y);
        canvas.drawPath(path, mPaint);
    }

    public void startAnimation() {
//        mControlPoint.set(mCenterX, mCenterY * 2);
        mAnimatorSet.start();
    }

    public void stopAnimation() {
        mAnimatorSet.cancel();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mControlPoint.y = y;
                mControlPoint.x = x;
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                mYValueAnimator.setFloatValues(y, mCenterY);
                mXValueAnimator.setFloatValues(x, mCenterX);
                mAnimatorSet.start();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                mControlPoint.y = y;
                mControlPoint.x = x;
                postInvalidate();
                break;
        }
        return true;
    }
}
