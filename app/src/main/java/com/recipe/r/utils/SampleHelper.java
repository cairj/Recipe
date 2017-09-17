package com.recipe.r.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 2017
 * 06
 * 2017/6/21
 * wangxiaoer
 * 功能描述：
 **/
public class SampleHelper {

    public static final String DATE_PICKER_FRAGMENT_DIALOG = "DatePickerFragmentDialog";
    public static final String TIME_PICKER_FRAGMENT_DIALOG = "TimePickerFragmentDialog";


    public static String getDateOnly(long time) {
        return new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(time);
    }

    public static String getDateAndTime(long time) {
        SimpleDateFormat sample = new SimpleDateFormat("dd MMM yyyy, hh:mma", Locale.getDefault());
        return sample.format(new Date(time));
    }

    public static String getTimeOnly(long time) {
        SimpleDateFormat sample = new SimpleDateFormat("hh:mma", Locale.getDefault());
        return sample.format(time);
    }
}
