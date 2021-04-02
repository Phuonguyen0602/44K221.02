package com.ecdue.lim.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
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
    public static long getCurrentDayTime(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy" , Locale.US);
        try {
            Date date = dateFormat.parse(String.format(Locale.US, "%d/%d/%d", day, month+1, year));
            return date != null ? date.getTime() : 0;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static long dayToMilliSec(int numberOfDays){
        return numberOfDays * 24 * 60 * 60 * 1000;
    }
    public static long dayToMilliSec(long numberOfDays){
        return numberOfDays * 24 * 60 * 60 * 1000;
    }
}
