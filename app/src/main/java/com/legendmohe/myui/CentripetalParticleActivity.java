package com.legendmohe.myui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.legendmohe.myui.drawable.CentripetalParticleView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CentripetalParticleActivity extends AppCompatActivity {

    @Bind(R.id.centripetalParticleView)
    CentripetalParticleView mCentripetalParticleView;
    @Bind(R.id.fan_imageview)
    ImageView mFanImageview;
    private ValueAnimator mFanValueAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centripetal_particle);
        ButterKnife.bind(this);
    }

    private void startFan() {
        mFanValueAnimator = new ValueAnimator();
        mFanValueAnimator.setDuration(1000);
        mFanValueAnimator.setFloatValues(mFanImageview.getRotation(), mFanImageview.getRotation() + 359);
        mFanValueAnimator.setInterpolator(new AccelerateInterpolator(1.5f));
        mFanValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mFanImageview.setRotation((Float) animation.getAnimatedValue());
            }
        });
        mFanValueAnimator.addListener(new AnimatorListenerAdapter() {
            public boolean canceled = false;

            @Override
            public void onAnimationEnd(Animator animation) {
                if (canceled) {
                    canceled = false;
                    return;
                }
                mFanValueAnimator = new ValueAnimator();
                mFanValueAnimator.setDuration(500);
                mFanValueAnimator.setFloatValues(mFanImageview.getRotation(), mFanImageview.getRotation() + 359);
                mFanValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
                mFanValueAnimator.setRepeatMode(ValueAnimator.RESTART);
                mFanValueAnimator.setInterpolator(new LinearInterpolator());
                mFanValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mFanImageview.setRotation((Float) animation.getAnimatedValue());
                    }
                });
                mFanValueAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        stopFanValueAnimator().start();
                    }
                });
                mFanValueAnimator.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                canceled = true;
                stopFanValueAnimator().start();
            }
        });
        mFanValueAnimator.start();
    }

    private ValueAnimator stopFanValueAnimator() {
        ValueAnimator stopAnimator = new ValueAnimator();
        stopAnimator.setDuration(1000);
        stopAnimator.setFloatValues(mFanImageview.getRotation(), mFanImageview.getRotation() + 359);
        stopAnimator.setInterpolator(new DecelerateInterpolator());
        stopAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mFanImageview.setRotation((Float) animation.getAnimatedValue());
            }
        });
        return stopAnimator;
    }

    @OnClick(R.id.stop_button)
    public void onStopButtonClicked() {
        mCentripetalParticleView.stop();

//        mFanValueAnimator.cancel();
    }

    @OnClick(R.id.start_button)
    public void onStartButtonClicked() {
        mCentripetalParticleView.start();
//        startFan();
    }
}
