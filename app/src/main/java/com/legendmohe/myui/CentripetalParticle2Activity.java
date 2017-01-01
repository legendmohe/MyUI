package com.legendmohe.myui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.legendmohe.myui.drawable.CentripetalParticle2View;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CentripetalParticle2Activity extends AppCompatActivity {

    @Bind(R.id.centripetalParticleView)
    CentripetalParticle2View mCentripetalParticleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centripetal_particle2);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.stop_button)
    public void onStopButtonClicked() {
        mCentripetalParticleView.stop();
    }

    @OnClick(R.id.start_button)
    public void onStartButtonClicked() {
        mCentripetalParticleView.start();
    }
}
