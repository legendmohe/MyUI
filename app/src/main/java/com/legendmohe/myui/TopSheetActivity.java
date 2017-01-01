package com.legendmohe.myui;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class TopSheetActivity extends AppCompatActivity {
    private static final String TAG = "TopSheetActivity";

    private TopSheetBehavior<View> mBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_sheet);
        mBehavior = TopSheetBehavior.from(findViewById(R.id.scroll));
        mBehavior.setTopSheetCallback(new TopSheetBehavior.TopSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, @TopSheetBehavior.State int newState) {
                Log.d(TAG, "onStateChanged() called with: " + "bottomSheet = [" + bottomSheet + "], newState = [" + newState + "]");
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.d(TAG, "onSlide() called with: " + "bottomSheet = [" + bottomSheet + "], slideOffset = [" + slideOffset + "]");
            }
        });
    }

    public void intro(View view) {
        if(mBehavior.getState() == TopSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(TopSheetBehavior.STATE_COLLAPSED);
        }else {
            mBehavior.setState(TopSheetBehavior.STATE_EXPANDED);
        }
    }

    public void intro2(View view) {
        BottomSheetBehavior behavior = BottomSheetBehavior.from(findViewById(R.id.scroll2));
        if(behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }else {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }
}
