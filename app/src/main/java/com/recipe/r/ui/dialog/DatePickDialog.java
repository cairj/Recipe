package com.recipe.r.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.recipe.r.R;
import com.recipe.r.utils.DateUtil;
import com.recipe.r.utils.Logger;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * 2017
 * 06
 * 2017/6/30
 * wangxiaoer
 * 功能描述：
 **/
public class DatePickDialog extends Dialog {
    private Context context;

    private TextView head_time_TV;
    private DatePicker dpPicker;// 日期选择器
//    private TimePicker tp_time; // 时间选择器
    private Button cancel_BT;
    private Button ok_BT;

    private DateListener listener;


    private Date date;// 显示的时间;
    private Date dateBack;// 返回到主页面的时间;

    private Date maxTime;// 最大开始日期为选中的截止日期
    private Date minTime;// 最小截止日期为选中的开始日期

    private String monthLabel;
    private String timeLabel;

    public DatePickDialog(Context context) {
        this(context, R.style.MyDataPickerDialog);
        this.context = context;
    }

    public DatePickDialog(Context context, int themeResId) {
        super(context, themeResId);

        setContentView(R.layout.dialog_date_pick);

        initViews();

        initListener();
    }

    public void setMaxTime(Date maxTime) {
        this.maxTime = maxTime;
    }

    public void setMinTime(Date minTime) {
        this.minTime = minTime;
    }

    /**
     * 设置回调
     *
     * @param listener
     */
    public void setListener(DateListener listener) {
        this.listener = listener;
    }

    /**
     * 加载视图
     */
    private void initViews() {
        head_time_TV = (TextView) findViewById(R.id.head_time_TV);
        dpPicker = (DatePicker) findViewById(R.id.dpPicker);
//        tp_time = (TimePicker) findViewById(R.id.tp_time);
        cancel_BT = (Button) findViewById(R.id.cancel_BT);
        ok_BT = (Button) findViewById(R.id.ok_BT);

        //是否使用24小时制
//        tp_time.setIs24HourView(true);
        TimeListener times = new TimeListener();
//        tp_time.setOnTimeChangedListener(times);

        date = new Date();
        dateBack = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        monthLabel = DateUtil.formatDateTime(calendar.getTimeInMillis(), "yyyy年MM月dd日"); // 设置默认值
        timeLabel = DateUtil.formatDateTime(calendar.getTimeInMillis(), "HH:mm"); // 设置默认值
        head_time_TV.setText(DateUtil.formatDateTime(date, "yyyy年MM月dd日 EEEE"));

        dpPicker.setMinDate(date.getTime() - 3 * 30 * 24 * 60 * 60 * 1000L);
//        dpPicker.setMaxDate(date.getTime());
        initDatePicker(calendar);
    }

    /**
     * 进入dialog datePicker上显示的时间
     */
    public void setShowTime(Date date) {
        this.date = date;
    }

    /**
     * 设置datePicker
     */
    private void initDatePicker(final Calendar calendar) {
        dpPicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 返回的month范围是: 0-11
                calendar.set(year, monthOfYear, dayOfMonth);
                head_time_TV.setText(DateUtil.formatDateTime(calendar.getTimeInMillis(), "yyyy年MM月dd日 EEEE"));
                monthLabel = DateUtil.formatDateTime(calendar.getTimeInMillis(), "yyyy年MM月dd日"); // 改变后的年月日
            }
        });
    }

    /**
     * 加载监听
     */
    private void initListener() {
        cancel_BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ok_BT.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Logger.d("", monthLabel + " " + timeLabel);

                try {
                    dateBack = DateUtil.getDate(monthLabel + " " + timeLabel, "yyyy年MM月dd日 HH:mm");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

//                if (null != minTime && dateBack.before(minTime)) {
//                    ToastUtil.showShort(context, "截止日期不能小于开始日期");
//                    return;
//                }
//                if (null != maxTime && dateBack.after(maxTime)) {
//                    ToastUtil.showShort(context, "开始日期不能大于截止日期");
//                    return;
//                }

                listener.callback(dateBack);
                dismiss();
            }
        });
    }

    /**
     * 回调接口
     */
    public interface DateListener {
        void callback(Date dateBack);
    }

    class TimeListener implements TimePicker.OnTimeChangedListener {

        /**
         * view 当前选中TimePicker控件
         * hourOfDay 当前控件选中TimePicker 的小时
         * minute 当前选中控件TimePicker  的分钟
         */
        @Override
        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
            timeLabel = hourOfDay + ":" + minute;
        }
    }
}