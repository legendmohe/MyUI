package com.legendmohe.myui.binder;

import com.legendmohe.myui.R;
import com.legendmohe.viewbinder.annotation.BindPackage;
import com.legendmohe.viewbinder.annotation.BindWidget;
import com.legendmohe.viewbinder.annotation.BindWidgetClass;

/**
 * Created by legendmohe on 16/7/8.
 */
@BindPackage({
        "android.text.method.PasswordTransformationMethod"
})
public class TestViewModel {

    private static final String EXP_CHANGE_PWD =
            "if ($V) {\n" +
                    "$T.setTransformationMethod(null);\n" +
                    "} else {\n" +
                    "$T.setTransformationMethod(new PasswordTransformationMethod());\n" +
                    "}";

    @BindWidget(
            value = {R.id.textview},
            clazz = {BindWidgetClass.TEXTVIEW}
    )
    private String mTitle;

    @BindWidget(
            value = {R.id.textview},
            clazz = {BindWidgetClass.TEXTVIEW}
    )
    private boolean mToggle = false;

    @BindWidget(
            value = {R.id.pwd_edittext},
            clazz = {BindWidgetClass.EDITTEXT},
            expression = {EXP_CHANGE_PWD}
    )
    private boolean mShowPwd = false;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setToggle(boolean toggle) {
        mToggle = toggle;
    }

    public boolean isToggle() {
        return mToggle;
    }

    public boolean isShowPwd() {
        return mShowPwd;
    }

    public void setShowPwd(boolean showPwd) {
        this.mShowPwd = showPwd;
    }
}

