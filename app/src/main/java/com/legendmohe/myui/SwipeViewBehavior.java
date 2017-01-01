package com.legendmohe.myui;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by legendmohe on 16/4/22.
 */
public class SwipeViewBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {

    public static final int STATE_IDLE = ViewDragHelper.STATE_IDLE;
    public static final int STATE_DRAGGING = ViewDragHelper.STATE_DRAGGING;
    public static final int STATE_SETTLING = ViewDragHelper.STATE_SETTLING;

    /** @hide */
    @IntDef({SWIPE_DIRECTION_START_TO_END, SWIPE_DIRECTION_END_TO_START, SWIPE_DIRECTION_ANY})
    @Retention(RetentionPolicy.SOURCE)
    private @interface SwipeDirection {}

    public static final int SWIPE_DIRECTION_START_TO_END = 0;
    public static final int SWIPE_DIRECTION_END_TO_START = 1;
    public static final int SWIPE_DIRECTION_ANY = 2;

    private static final float DEFAULT_DRAG_CLAMP_THRESHOLD = 0.7f;
    private static final float DEFAULT_ALPHA_START_DISTANCE = 0f;
    private static final float DEFAULT_ALPHA_END_DISTANCE = DEFAULT_DRAG_CLAMP_THRESHOLD;

    private ViewDragHelper mViewDragHelper;
    private OnStateChangedListener mListener;
    private boolean mIgnoreEvents;

    private float mSensitivity = 0f;
    private boolean mSensitivitySet;

    private int mSwipeDirection = SWIPE_DIRECTION_ANY;
    private float mDragClampThreshold = DEFAULT_DRAG_CLAMP_THRESHOLD;
    private float mAlphaStartSwipeDistance = DEFAULT_ALPHA_START_DISTANCE;
    private float mAlphaEndSwipeDistance = DEFAULT_ALPHA_END_DISTANCE;

    private View mBgView;

    private ClampState mClampState = ClampState.NORMAL;

    public enum ClampState {
        LEFT_CLAMPED,
        RIGHT_CLAMPED,
        NORMAL
    }

    /**
     * Callback interface used to notify the application that the view has been dismissed.
     */
    public interface OnStateChangedListener {

        /**
         * Called when the drag state has changed.
         *
         * @param state the new state. One of
         * {@link #STATE_IDLE}, {@link #STATE_DRAGGING} or {@link #STATE_SETTLING}.
         */
        public void onDragStateChanged(int state);

        public void onClampStateChanged(View view, ClampState clampState);
    }

    public SwipeViewBehavior(View bgView) {
        mBgView = bgView;
    }

    public SwipeViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO - load it from xml
//        mDragClampWidth = attrs.getAttributeFloatValue();
    }

    /**
     * Set the listener to be used when a dismiss event occurs.
     *
     * @param listener the listener to use.
     */
    public void setListener(OnStateChangedListener listener) {
        mListener = listener;
    }

    /**
     * Sets the swipe direction for this behavior.
     *
     * @param direction one of the {@link #SWIPE_DIRECTION_START_TO_END},
     *                  {@link #SWIPE_DIRECTION_END_TO_START} or {@link #SWIPE_DIRECTION_ANY}
     */
    public void setSwipeDirection(@SwipeDirection int direction) {
        mSwipeDirection = direction;
    }

    /**
     * Set the threshold for telling if a view has been dragged enough to be dismissed.
     *
     * @param distance a ratio of a view's width, values are clamped to 0 >= x <= 1f;
     */
    public void setDragDismissDistance(float distance) {
        mDragClampThreshold = clamp(0f, distance, 1f);
    }

    /**
     * The minimum swipe distance before the view's alpha is modified.
     *
     * @param fraction the distance as a fraction of the view's width.
     */
    public void setStartAlphaSwipeDistance(float fraction) {
        mAlphaStartSwipeDistance = clamp(0f, fraction, 1f);
    }

    /**
     * The maximum swipe distance for the view's alpha is modified.
     *
     * @param fraction the distance as a fraction of the view's width.
     */
    public void setEndAlphaSwipeDistance(float fraction) {
        mAlphaEndSwipeDistance = clamp(0f, fraction, 1f);
    }

    /**
     * Set the sensitivity used for detecting the start of a swipe. This only takes effect if
     * no touch handling has occured yet.
     *
     * @param sensitivity Multiplier for how sensitive we should be about detecting
     *                    the start of a drag. Larger values are more sensitive. 1.0f is normal.
     */
    public void setSensitivity(float sensitivity) {
        mSensitivity = sensitivity;
        mSensitivitySet = true;
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, V child, MotionEvent event) {
        switch (MotionEventCompat.getActionMasked(event)) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // Reset the ignore flag
                if (mIgnoreEvents) {
                    mIgnoreEvents = false;
                    return false;
                }
                break;
            default:
                mIgnoreEvents = !parent.isPointInChildBounds(child,
                        (int) event.getX(), (int) event.getY());
                break;
        }

        if (mIgnoreEvents) {
            return false;
        }

        ensureViewDragHelper(parent);
        return mViewDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, V child, MotionEvent event) {
        if (mViewDragHelper != null) {
            mViewDragHelper.processTouchEvent(event);
            return true;
        }
        return false;
    }

    /**
     * Called when the user's input indicates that they want to swipe the given view.
     *
     * @param view View the user is attempting to swipe
     * @return true if the view can be dismissed via swiping, false otherwise
     */
    public boolean canSwipeDismissView(@NonNull View view) {
        return true;
    }

    private final ViewDragHelper.Callback mDragCallback = new ViewDragHelper.Callback() {
        private int mOriginalCapturedViewLeft;

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            if (getDragState() == STATE_SETTLING)
                return false;
            return canSwipeDismissView(child);
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            mOriginalCapturedViewLeft = capturedChild.getLeft();

            // The view has been captured, and thus a drag is about to start so stop any parents
            // intercepting
            final ViewParent parent = capturedChild.getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }

        @Override
        public void onViewDragStateChanged(int state) {
            if (mListener != null) {
                mListener.onDragStateChanged(state);
            }
        }

        @Override
        public void onViewReleased(View child, float xvel, float yvel) {
            final ViewParent parent = child.getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(false);
            }

            int targetLeft = mOriginalCapturedViewLeft;
            int clampWidth = mBgView.getWidth();
            boolean needClamp = false;
            ClampState targetState = null;

            if (mClampState == ClampState.NORMAL) {
                if (shouldClamp(child, xvel)){
                    needClamp = true;
                    if (child.getLeft() < mOriginalCapturedViewLeft) { // swipe left
                        targetLeft = mOriginalCapturedViewLeft - clampWidth;
                        targetState = ClampState.RIGHT_CLAMPED;
                    } else if (child.getLeft() > mOriginalCapturedViewLeft) { // swipe right
                        targetLeft = clampWidth;
                        targetState = ClampState.LEFT_CLAMPED;
                    }
                } else {
                    targetLeft = mOriginalCapturedViewLeft;
                }
            } else {
                if (mClampState == ClampState.LEFT_CLAMPED) {
                    if (child.getLeft() > mOriginalCapturedViewLeft) {
                        targetLeft = mOriginalCapturedViewLeft;
                    } else {
                        targetLeft = mOriginalCapturedViewLeft - clampWidth;
                        targetState = ClampState.NORMAL;
                    }
                } else if (mClampState == ClampState.RIGHT_CLAMPED) {
                    if (child.getLeft() < mOriginalCapturedViewLeft) {
                        targetLeft = mOriginalCapturedViewLeft;
                    } else {
                        targetLeft = mOriginalCapturedViewLeft + clampWidth;
                        targetState = ClampState.NORMAL;
                    }
                }
            }

            if (mViewDragHelper.settleCapturedViewAt(targetLeft, child.getTop())) {
                ViewCompat.postOnAnimation(child, new SettleRunnable(child, targetState));
            } else if (needClamp) {
                if (mListener != null) {
                    mListener.onClampStateChanged(child, targetState);
                }
                mClampState = targetState;
            }
        }

        private boolean shouldClamp(View child, float xvel) {
            if (xvel != 0f) {
                final boolean isRtl = ViewCompat.getLayoutDirection(child)
                        == ViewCompat.LAYOUT_DIRECTION_RTL;

                if (mSwipeDirection == SWIPE_DIRECTION_ANY) {
                    // We don't care about the direction so return true
                    return true;
                } else if (mSwipeDirection == SWIPE_DIRECTION_START_TO_END) {
                    // We only allow start-to-end swiping, so the fling needs to be in the
                    // correct direction
                    return isRtl ? xvel < 0f : xvel > 0f;
                } else if (mSwipeDirection == SWIPE_DIRECTION_END_TO_START) {
                    // We only allow end-to-start swiping, so the fling needs to be in the
                    // correct direction
                    return isRtl ? xvel > 0f : xvel < 0f;
                }
            } else {
                final int clampWidth = mBgView.getWidth();
                final int distance = child.getLeft() - mOriginalCapturedViewLeft;
                final int thresholdDistance = Math.round(clampWidth * mDragClampThreshold);
                return Math.abs(distance) >= thresholdDistance;
            }

            return false;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return child.getWidth();
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            final boolean isRtl = ViewCompat.getLayoutDirection(child)
                    == ViewCompat.LAYOUT_DIRECTION_RTL;
            int min, max;

            if (mSwipeDirection == SWIPE_DIRECTION_START_TO_END) {
                if (isRtl) {
                    min = mOriginalCapturedViewLeft - child.getWidth();
                    max = mOriginalCapturedViewLeft;
                } else {
                    min = mOriginalCapturedViewLeft;
                    max = mOriginalCapturedViewLeft + child.getWidth();
                }
            } else if (mSwipeDirection == SWIPE_DIRECTION_END_TO_START) {
                if (isRtl) {
                    min = mOriginalCapturedViewLeft;
                    max = mOriginalCapturedViewLeft + child.getWidth();
                } else {
                    min = mOriginalCapturedViewLeft - child.getWidth();
                    max = mOriginalCapturedViewLeft;
                }
            } else {
                min = mOriginalCapturedViewLeft - child.getWidth();
                max = mOriginalCapturedViewLeft + child.getWidth();
            }

            return clamp(min, left, max);
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return child.getTop();
        }

        @Override
        public void onViewPositionChanged(View child, int left, int top, int dx, int dy) {
        }
    };

    private void ensureViewDragHelper(ViewGroup parent) {
        if (mViewDragHelper == null) {
            mViewDragHelper = mSensitivitySet
                    ? ViewDragHelper.create(parent, mSensitivity, mDragCallback)
                    : ViewDragHelper.create(parent, mDragCallback);
        }
    }

    private class SettleRunnable implements Runnable {
        private final View mView;
        private final ClampState mTargetState;

        SettleRunnable(View view, ClampState targetState) {
            mView = view;
            mTargetState = targetState;
        }

        @Override
        public void run() {
            if (mViewDragHelper != null && mViewDragHelper.continueSettling(true)) {
                ViewCompat.postOnAnimation(mView, this);
            } else {
                if (mTargetState != null) {
                    mClampState = mTargetState;
                    if (mListener != null) {
                        mListener.onClampStateChanged(mView, mClampState);
                    }
                }
            }
        }
    }

    private static float clamp(float min, float value, float max) {
        return Math.min(Math.max(min, value), max);
    }

    private static int clamp(int min, int value, int max) {
        return Math.min(Math.max(min, value), max);
    }

    /**
     * Retrieve the current drag state of this behavior. This will return one of
     * {@link #STATE_IDLE}, {@link #STATE_DRAGGING} or {@link #STATE_SETTLING}.
     *
     * @return The current drag state
     */
    public int getDragState() {
        return mViewDragHelper != null ? mViewDragHelper.getViewDragState() : STATE_IDLE;
    }

    /**
     * The fraction that {@code value} is between {@code startValue} and {@code endValue}.
     */
    static float fraction(float startValue, float endValue, float value) {
        return (value - startValue) / (endValue - startValue);
    }
}