package com.legendmohe.myui.navigator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.legendmohe.myui.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class NavigatorDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator_detail);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.push_button)
    public void onPushButtonClick() {
        startActivity(new Intent(this, NavigatorDetailActivity.class));
    }

    @OnClick(R.id.pop_button)
    public void onPopClick() {
        NavUtil.getInstance().popTo(NavigatorMainActivity.class);
    }

    @OnClick(R.id.main_button)
    public void onMainClick() {
        startActivity(new Intent(this, NavigatorMainActivity.class));
    }
}
