package com.ecdue.lim.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtil {
    public static String milliSecToString(long milli, String format){
        try {
            return new SimpleDateFormat(format, Locale.US).format(new Date(milli));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
