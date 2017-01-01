package com.legendmohe.myui.recycle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.legendmohe.myui.R;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecycleViewActivity extends AppCompatActivity {
    private static final String TAG = "RecycleViewActivity";

    @Bind(R.id.recycleView)
    RecyclerView mRecycleView;
    @Bind(R.id.button)
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view);
        ButterKnife.bind(this);

        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(new CircularRecyclerViewAdapter(Calendar.getInstance()));
        mRecycleView.scrollToPosition(CircularRecyclerViewAdapter.POSITION_MIDDLE);

        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    centerCurrentItem(true);
                }
            }
        });
        mRecycleView.post(new Runnable() {
            @Override
            public void run() {
                centerCurrentItem(false);
            }
        });
    }

    private void centerCurrentItem(boolean smooth) {
        int totalHeight = mRecycleView.getHeight();
        int height = mRecycleView.getLayoutManager().getChildAt(0).getHeight();

        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecycleView.getLayoutManager();
        int totalVisibleItems = layoutManager.findLastVisibleItemPosition() - layoutManager.findFirstVisibleItemPosition() + 1;
        int centeredItemPosition = totalVisibleItems / 2;

        int top = mRecycleView.getLayoutManager().getChildAt(centeredItemPosition).getTop();
        int targetTop = top - (totalHeight - height) / 2;

        if (smooth) {
            mRecycleView.smoothScrollBy(0, targetTop);
        }else {
            mRecycleView.scrollBy(0, targetTop);
        }
    }

    @OnClick(R.id.button)
    public void onTodayClick() {
        mRecycleView.smoothScrollToPosition(CircularRecyclerViewAdapter.POSITION_MIDDLE);
    }
}
