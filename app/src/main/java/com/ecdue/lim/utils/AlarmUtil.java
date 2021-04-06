package com.ecdue.lim.utils;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ecdue.lim.data.Product;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmUtil {
    public static void createNotificationAlarm(Context context) {
        Intent intent = new Intent(context, ReminderBroadcast.class);
        intent.putExtra(ReminderBroadcast.TITLE, "Expiration date reminder");
        intent.putExtra(ReminderBroadcast.DESCRIPTION, "Hello from the other side");
        intent.putExtra(ReminderBroadcast.NOTIFICATION_ID, 0);
        intent.setType("test" + System.currentTimeMillis());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        long timeClicked = System.currentTimeMillis();
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                timeClicked + 1000*5,
                pendingIntent);
    }
    public static void createNotificationAlarm(Context context, Product product, long notificationTime) {
        Log.d("AlarmManager", "Registering notification for " + product.getName());
        Intent intent = new Intent(context, ReminderBroadcast.class);
        intent.putExtra(ReminderBroadcast.TITLE, "EXP date reminder");
        intent.putExtra(ReminderBroadcast.DESCRIPTION, getDescription(product));
        intent.putExtra(ReminderBroadcast.NOTIFICATION_ID, product.getNotificationId());
        intent.setType("" + product.getAddDate());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, product.getNotificationId(), intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                notificationTime,
                pendingIntent);
    }
    public static void recreateNotificationAlarm(Context context, Product product, long notificationTime) {
        deleteNotificationAlarm(context, product);
        createNotificationAlarm(context, product, notificationTime);
    }
    public static String getDescription(Product product){
        int daysLeft = DateTimeUtil.milliSecToDay(product.getExpire() - DateTimeUtil.getCurrentDayTime());
        if (daysLeft < 0)
            return product.getName() + " is already expired";
        else if (daysLeft == 0)
            return product.getName() + " is expiring today";
        else if (daysLeft == 1)
            return product.getName() + " will expire tomorrow";
        else
            return product.getName() + " will expire in " + daysLeft + " days";
    }
    public static void deleteNotificationAlarm(Context context, Product product){
        Log.d("AlarmManager", "Removing notification for " + product.getName());
        Intent intent = new Intent(context, ReminderBroadcast.class);
        intent.setType(Long.toString(product.getAddDate()));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0 , intent, PendingIntent.FLAG_NO_CREATE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            notificationManager.cancel(product.getNotificationId());
        }
        else
            Log.d("AlarmManager", "Can't find the PendingIntent");
    }
}
