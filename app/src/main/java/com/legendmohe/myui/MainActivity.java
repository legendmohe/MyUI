package com.legendmohe.myui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cocosw.bottomsheet.BottomSheet;
import com.legendmohe.myui.binder.BinderActivity;
import com.legendmohe.myui.drawable.RippleDrawable;
import com.legendmohe.myui.dummy.DummyContent;
import com.legendmohe.myui.navigator.NavigatorMainActivity;
import com.legendmohe.myui.recycle.RecycleViewActivity;
import com.legendmohe.viewbinder.ViewModelBinder;
import com.legendmohe.viewbinder.annotation.BindWidget;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    //    @Bind(R.id.bottom_sheet)
//    FrameLayout mBottomSheet;
//    @Bind(R.id.cl)
//    CoordinatorLayout mCl;

    @Bind(R.id.custom_drawable)
    Button mCustomDrawableButton;
    @Bind(R.id.container_layout)
    ViewGroup mContainerLayout;
    @Bind(R.id.center_imageview)
    ImageView mCenterImageview;

    private RelativeLayout mTargetView;
    private TextView mBgView;
    private CoordinatorLayout mCoordinatorLayout;
    private BottomSheetBehavior<FrameLayout> mBehavior;
    private RippleDrawable mRippleDrawable;
    private boolean mAnimationStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

//        mBehavior = BottomSheetBehavior.from(mBottomSheet);
//        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//
//            }
//        });

        mCenterImageview.post(new Runnable() {
            @Override
            public void run() {
                int radius = mCenterImageview.getWidth() / 2;
                mRippleDrawable = new RippleDrawable(radius + 160, radius, 6, 2000, false);
                mCenterImageview.setBackground(mRippleDrawable);
            }
        });
    }

    @OnClick(R.id.bottom_sheet_button)
    public void onBottomSheetButtonClicked() {
        new BottomSheet.Builder(this, R.style.BottomSheet_StyleDialog).sheet(R.menu.bottomsheet).listener(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                }
            }
        }).show();
    }

    @OnClick(R.id.bottom_sheet_button2)
    public void onBottomSheetButton2Clicked() {
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @OnClick(R.id.bottom_sheet_button3)
    public void onBottomSheetButton3Clicked() {
//        CustomBottomSheetFragment fragment = CustomBottomSheetFragment.newInstance(R.layout.sheet_content);
//        fragment.show(getSupportFragmentManager());

        Intent intent = new Intent(this, StickyTabActivity.class);
        intent.putExtra("string", "sdsd");
        intent.putExtra("int", 101);
        intent.putExtra("boolean", true);
        intent.putExtra("object", DummyContent.ITEMS.get(0));
        startActivity(intent);
    }

    @OnClick(R.id.animation_button)
    public void onAnimationButtonClick() {
        Intent intent = new Intent(this, AnimationActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.custom_drawable)
    public void onCustomDrawableButtonClick() {
        if (!mAnimationStarted) {
            mRippleDrawable.startAnimation();
        } else {
            mRippleDrawable.stopAnimation();
        }
        mAnimationStarted = !mAnimationStarted;
    }

    @OnClick(R.id.rotate_circle_view)
    public void onRotateButtonClick() {
        startActivity(new Intent(this, RotateZCircleActivity.class));
    }

    @OnClick(R.id.centripetal_view)
    public void onCentripetalButtonClick() {
        startActivity(new Intent(this, CentripetalParticleActivity.class));
    }

    @OnClick(R.id.centripetal2_view)
    public void onCentripetal2ButtonClick() {
        startActivity(new Intent(this, CentripetalParticle2Activity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @OnClick(R.id.rubberView_view)
    public void onRubberClick() {
        startActivity(new Intent(this, RubberViewActivity.class));
    }

    @OnClick(R.id.topsheet_button)
    public void onTopSheetButtonClick() {
        startActivity(new Intent(this, TopSheetActivity.class));
    }

    @OnClick(R.id.binder_button)
    public void onBinderButtonClicked( ) {
        startActivity(new Intent(this, BinderActivity.class));
    }

    @OnClick(R.id.recycle_button)
    public void onRecycleButtonClicked( ) {
        startActivity(new Intent(this, RecycleViewActivity.class));
    }

    @OnClick(R.id.navigator_button)
    public void onNavigatorButtonClicked() {
        startActivity(new Intent(this, NavigatorMainActivity.class));
    }
}
