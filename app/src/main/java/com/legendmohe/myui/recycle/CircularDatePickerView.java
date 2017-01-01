package com.legendmohe.myui.recycle;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legendmohe.myui.R;

import java.util.Calendar;

/**
 * Created by legendmohe on 16/7/15.
 */
public class CircularDatePickerView extends LinearLayout {

    private LinearLayout mWeekdayContainer;
    private LinearLayout mDayOfMonthContainer;

    private Calendar mCurrentDate;
    private OnItemClickedListener mListener;
    private int mHighLightDay;

    public CircularDatePickerView(Context context) {
        super(context);
        init();
    }

    public CircularDatePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircularDatePickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    ///////////////////////////////////function///////////////////////////////////

    private void init() {
        inflate(getContext(), R.layout.circular_date_picker_view, this);
        mWeekdayContainer = (LinearLayout) findViewById(R.id.weekday_container);
        mDayOfMonthContainer = (LinearLayout) findViewById(R.id.day_of_month_container);
    }

    public void renderDate(Calendar calendar) {
        mCurrentDate = (Calendar) calendar.clone();

        Calendar todayCalendar = Calendar.getInstance();
        int maxDayOfMonth = mCurrentDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstWeekDayOfMonth = getFirstWeekdayfMonth(mCurrentDate);// 1 for sunday
        int maxRow = (int) Math.ceil((firstWeekDayOfMonth + maxDayOfMonth - 1) / 7.0);
        int day = -firstWeekDayOfMonth + 1 + 1;

        for (int i = 0; i < mDayOfMonthContainer.getChildCount(); i++) {
            ViewGroup rowContainer = (ViewGroup) mDayOfMonthContainer.getChildAt(i);
            if (i > maxRow - 1) {
                rowContainer.setVisibility(View.GONE);
            } else {
                for (int j = 0; j < rowContainer.getChildCount(); j++) {
                    TextView dayTextView = (TextView) rowContainer.getChildAt(j);

                    if (day < 1 || day > maxDayOfMonth) {
                        dayTextView.setEnabled(false);
                        dayTextView.setSelected(false);
                        dayTextView.setText("");
                        dayTextView.setOnClickListener(null);
                    } else {
                        final Calendar targetCalendar = (Calendar) mCurrentDate.clone();
                        targetCalendar.set(Calendar.DAY_OF_MONTH, day);
                        dayTextView.setEnabled(targetCalendar.compareTo(todayCalendar) <= 0);
                        dayTextView.setText(String.valueOf(day));
                        dayTextView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mListener != null) {
                                    mListener.onItemClicked(targetCalendar);
                                }
                            }
                        });
                        dayTextView.setSelected(dayTextView.isEnabled() && mHighLightDay == day);
                    }
                    day++;
                }
            }
        }
    }

    private int getFirstWeekdayfMonth(Calendar currentDate) {
        Calendar calendar = (Calendar) currentDate.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public void setHighLightDay(int day) {
        mHighLightDay = day;

        int firstWeekDayOfMonth = getFirstWeekdayfMonth(mCurrentDate);// 1 for sunday
        int targetDay = firstWeekDayOfMonth + mHighLightDay - 1;

        ViewGroup rowContainer = (ViewGroup) mDayOfMonthContainer.getChildAt((int) (Math.ceil(targetDay/7.0) - 1));
        if (rowContainer != null) {
            TextView dayTextView = (TextView) rowContainer.getChildAt((targetDay - 1) % 7);
            if (dayTextView != null) {
                dayTextView.setSelected(dayTextView.isEnabled());
            }
        }
    }

    ///////////////////////////////////callback///////////////////////////////////


    public void setOnItemClickedListener(OnItemClickedListener listener) {
        mListener = listener;
    }

    public interface OnItemClickedListener {
        void onItemClicked(Calendar calendar);
    }
}
