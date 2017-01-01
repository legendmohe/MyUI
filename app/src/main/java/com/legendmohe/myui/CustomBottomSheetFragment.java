package com.legendmohe.myui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.widget.FrameLayout;

/**
 * Created by legendmohe on 16/4/28.
 */
public class CustomBottomSheetFragment extends BaseDialogFragment {
    private static final String TAG = "CustomBottomSheet";

    private static final String BUNDLE_KEY_RESID = "BUNDLE_KEY_RESID";

    public static CustomBottomSheetFragment newInstance(@LayoutRes int layoutId) {
        CustomBottomSheetFragment fragment = new CustomBottomSheetFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_KEY_RESID, layoutId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int layoutId = getArguments().getInt(BUNDLE_KEY_RESID);

        BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), getTheme());
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = (FrameLayout) d.findViewById(android.support.design.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        dialog.setContentView(layoutId);
        return dialog;
    }
}
