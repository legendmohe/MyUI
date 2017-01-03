package com.legendmohe.myui.navigator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.legendmohe.myui.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NavigatorMainActivity extends AppCompatActivity {

    @Bind(R.id.push_button)
    Button mPushButton;
    @Bind(R.id.pop_button)
    Button mPopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.push_button)
    public void onPushButtonClick() {
        startActivity(new Intent(this, NavigatorMainActivity.class));
    }

    @OnClick(R.id.pop_button)
    public void onPopClick() {
        NavUtil.popTo(NavigatorDetailActivity.class);
    }

    @OnClick(R.id.detail_button)
    public void onDetailClick() {
        startActivity(new Intent(this, NavigatorDetailActivity.class));
    }

    @OnClick(R.id.login_button)
    public void onLoginClick() {
        startActivity(new Intent(this, NavigatorLoginActivity.class));
    }
}
