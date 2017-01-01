package com.legendmohe.myui.binder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.legendmohe.myui.R;
import com.legendmohe.viewbinder.ViewModelBinder;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BinderActivity extends AppCompatActivity {

    TextView mTextView;
    TextView mTextView2;
    EditText mEditText;

    private TestViewModel mViewModel;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder);

        mTextView = (TextView) findViewById(R.id.textview);
        mTextView2 = (TextView) findViewById(R.id.textview2);
        mEditText = (EditText) findViewById(R.id.pwd_edittext);

        mButton = (android.widget.Button) findViewById(R.id.button);
        if (mButton != null) {
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    mViewModel.setToggle(!mViewModel.getToggle());
//                    mViewModel.setTitle(mViewModel.getTitle() + "!");
                    mViewModel.setShowPwd(!mViewModel.isShowPwd());
                }
            });
        }
        mViewModel = ViewModelBinder.create(this, TestViewModel.class, true);
    }
}
