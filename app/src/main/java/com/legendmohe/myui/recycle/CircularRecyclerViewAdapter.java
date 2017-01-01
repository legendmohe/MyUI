package com.legendmohe.myui.recycle;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.legendmohe.myui.R;

import java.util.Calendar;

public class CircularRecyclerViewAdapter extends RecyclerView.Adapter<CircularRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "CircularAdapter";

    public static final int ITEM_RANGE = 100;
    public static final int POSITION_MIDDLE = ITEM_RANGE / 2 + 1;

    private Calendar mAnchorDate;

    public CircularRecyclerViewAdapter(Calendar anchor) {
        mAnchorDate = anchor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_recycle_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Calendar curDate = (Calendar) mAnchorDate.clone();
        curDate.add(Calendar.MONTH, position - POSITION_MIDDLE);

        holder.mItem = curDate;
        holder.mIdView.setText(getTitle(curDate));
        holder.mCircularDatePickerView.renderDate(holder.mItem);
        holder.mCircularDatePickerView.setHighLightDay(curDate.get(Calendar.DAY_OF_MONTH));
        holder.mCircularDatePickerView.setOnItemClickedListener(new CircularDatePickerView.OnItemClickedListener() {
            @Override
            public void onItemClicked(Calendar calendar) {
                Log.d(TAG, "onItemClicked:" + calendar);
            }
        });
    }

    @NonNull
    private String getTitle(Calendar curDate) {
        return String.format("%d.%02d", curDate.get(Calendar.YEAR), curDate.get(Calendar.MONTH) + 1);
    }

    @Override
    public int getItemCount() {
        return ITEM_RANGE;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final CircularDatePickerView mCircularDatePickerView;
        public Calendar mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.title_textview);
            mCircularDatePickerView = (CircularDatePickerView) view.findViewById(R.id.dateView);
        }
    }
}
