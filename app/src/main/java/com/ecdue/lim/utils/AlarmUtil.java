package com.ecdue.lim.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.ecdue.lim.data.Product;

import static android.content.Context.ALARM_SERVICE;

public class AlarmUtil {
    public static void registerNotificationAlarm(Context context) {
        Intent intent = new Intent(context, ReminderBroadcast.class);
        intent.putExtra(ReminderBroadcast.TITLE, "Expiration date reminder");
        intent.putExtra(ReminderBroadcast.DESCRIPTION, "Hello from the other side");
        intent.putExtra(ReminderBroadcast.NOTIFICATION_ID, 0);
        intent.setType("test1234");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        long timeClicked = System.currentTimeMillis();
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                timeClicked + 1000*5,
                pendingIntent);
    }
    public static void registerNotificationAlarm(Context context, Product product, int notificationId, long notificationTime) {
        Intent intent = new Intent(context, ReminderBroadcast.class);
        intent.putExtra(ReminderBroadcast.TITLE, "Expiration date reminder");
        intent.putExtra(ReminderBroadcast.DESCRIPTION,
                "" + product.getName() + " will expire in " +
                        DateTimeUtil.milliSecToDay(product.getExpire() - DateTimeUtil.getCurrentDayTime()) + " day(s)");
        intent.putExtra(ReminderBroadcast.NOTIFICATION_ID, notificationId);
        intent.setType("" + product.getAddDate());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                notificationTime,
                pendingIntent);
    }
    public static void deleteNotificationAlarm(Context context, String alarmId){
        Intent intent1 = new Intent(context, ReminderBroadcast.class);
        intent1.setType(alarmId);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 0, intent1, 0);
        AlarmManager alarmManager1 = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager1.cancel(pendingIntent1);
    }
}
