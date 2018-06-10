package com.ihypnus.ihypnuscare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.calendarlistview.DatePickerController;
import com.ihypnus.ihypnuscare.calendarlistview.DayPickerView;
import com.ihypnus.ihypnuscare.calendarlistview.SimpleMonthAdapter;
import com.ihypnus.ihypnuscare.utils.DateTimeUtils;
import com.ihypnus.ihypnuscare.utils.ToastUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Description: 日期选择Activity
 */
public class SelectDateActivity extends BaseActivity {
    public static final int RESPONSE_SELECT_OK = 202;
    private DayPickerView mDpv_date_picker;
    private Calendar mCalendar;
    private Calendar mCalendar2;
    private SimpleDateFormat mFormat;
    private int currentYear; //获取当前日期（只获取年）
    private int moudle;   //模块  0,其他界面选择
    String endtime;
    private int mCalendarType;//日历的类型(选择范围,默认为0);

    @Override
    protected int setView() {
        return R.layout.activity_select_date;
    }

    @Override
    protected void findViews() {
        mDpv_date_picker = (DayPickerView) findViewById(R.id.dpv_date_picker);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        // 1, 设置标题
        /*String title = getIntent().getStringExtra("title");
        if (title != null)
            setTitle(title);
        else*/
        setTitle("选择日期");

        // 2, 初始化日期格式化工具
        mFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

        // 3, 获取今年
        currentYear = (Integer.parseInt(DateTimeUtils.getFightDateTime()));

        mCalendar = Calendar.getInstance();
        mCalendar2 = Calendar.getInstance();

        // 4, 如果用户传入时间，则选中传入的时间；否则默认选中今天
        mCalendar = Calendar.getInstance();
        Intent intent = getIntent();
        String time = getIntent().getStringExtra("time");
        String starttime = intent.getStringExtra("starttime");
        endtime = intent.getStringExtra("endtime");
        moudle = intent.getIntExtra("moudle", 0);
        mCalendarType = intent.getIntExtra("type", 0);


        int mode = getIntent().getIntExtra("mode", 0);

        mDpv_date_picker.setMode(mode);

        if (!TextUtils.isEmpty(time)) {
            if (!TextUtils.isEmpty(starttime)) {          //设置了起始时间
                try {
                    Date date1 = mFormat.parse(starttime);
                    Date date2 = mFormat.parse(endtime);
                    mCalendar.setTime(date1);
                    mCalendar2.setTime(date2);
                    return;
                } catch (ParseException e) {
                    ToastUtils.showToastDefault(this, "传入的日期格式错误!");
                }
            }
            try {
                Date date = mFormat.parse(time);
                mCalendar.setTime(date);
            } catch (ParseException e) {
                ToastUtils.showToastDefault(this, "传入的日期格式错误!");
            }
        }
    }

    @Override
    protected void initEvent() {
        mDpv_date_picker.setController(new DatePickerController() {
            @Override
            public int getMinYear() {
                if (mCalendarType == 1) {
                    return currentYear;
                } else if (mCalendarType == 2) {
                    return 2016;
                } else {
                    return currentYear - 1;
                }
            }

            @Override
            public int getMaxYear() {
                if (mCalendarType == 1) {
                    return currentYear + 1;
                } else {
                    return currentYear + 1;
                }
            }

            @Override
            public void onDayOfMonthSelected(int year, int month, int day) {

                Intent intent = getIntent();
                intent.putExtra("year", year);
                intent.putExtra("month", month + 1);
                intent.putExtra("day", day);
                setResult(RESPONSE_SELECT_OK, intent);
                finish();

            }

            @Override
            public void onDateRangeSelected(SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays) {
                String start = DateTimeUtils.getTimeFromDate(selectedDays.getFirst().getDate(), "yyyy-MM-dd");
                String end = DateTimeUtils.getTimeFromDate(selectedDays.getLast().getDate(), "yyyy-MM-dd");
                if (selectedDays.getFirst().getDate().compareTo(selectedDays.getLast().getDate()) > 0) {
                    start = DateTimeUtils.getTimeFromDate(selectedDays.getLast().getDate(), "yyyy-MM-dd");
                    end = DateTimeUtils.getTimeFromDate(selectedDays.getFirst().getDate(), "yyyy-MM-dd");
                }

            }
        }, mCalendarType);
    }

    @Override
    protected void loadData() {
        SimpleMonthAdapter.CalendarDay day = new SimpleMonthAdapter.CalendarDay(mCalendar);
        SimpleMonthAdapter.CalendarDay day2 = new SimpleMonthAdapter.CalendarDay(mCalendar2);
        if (moudle == 0) {
            mDpv_date_picker.getSelectedDays().setFirst(day);
        } else {
            if (!TextUtils.isEmpty(endtime)) {
                mDpv_date_picker.getSelectedDays().setFirst(day);
                mDpv_date_picker.getSelectedDays().setLast(day2);
            }
        }
//        datePicker.smoothScrollToPosition(datePicker.getAdapter().getItemCount() - 1);
//        if (mCalendarType == 0 || mCalendarType == 2 ) {//默认的选择范围
//        }
        int childPosition = mDpv_date_picker.getChildPosition(day);
        if (childPosition == -1) {
            if (mCalendarType == 0 || mCalendarType == 2) {
                mDpv_date_picker.scrollToPosition(mDpv_date_picker.getAdapter().getItemCount() - 2);
            }
        } else {
            mDpv_date_picker.scrollToPosition(childPosition);
        }
    }

    @Override
    public void finish() {
        super.finish();
    }
}
