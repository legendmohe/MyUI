package com.legendmohe.myui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.legendmohe.myui.drawable.RubberView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RubberViewActivity extends AppCompatActivity {

    @Bind(R.id.rubber_view)
    RubberView mRubberView;
    @Bind(R.id.start_button)
    Button mStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rubber_view);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.start_button)
    public void onStartButtonClick() {
        mRubberView.startAnimation();
    }
}
