package com.ecdue.lim.utils;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.ecdue.lim.R;
import com.ecdue.lim.features.authentication.welcome.WelcomeActivity;

public class ReminderBroadcast extends BroadcastReceiver {
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "message";
    public static final String NOTIFICATION_ID = "notification_id";
    @Override
    public void onReceive(Context context, Intent intent) {
        //TODO: issue notification here
        if (context != null && intent != null){
            String title = intent.getExtras().getString(TITLE) == null ? "Null" : intent.getExtras().getString(TITLE);
            String description = intent.getExtras().getString(DESCRIPTION) == null ? "Null" : intent.getExtras().getString(DESCRIPTION);
            int notificationId = intent.getExtras().getInt(NOTIFICATION_ID);
            Intent main = new Intent(context, WelcomeActivity.class);
            main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, main, 0);
            NotificationUtil.createNotification(context,
                    notificationId,
                    R.drawable.notification_small_icon,
                    title,
                    description,
                    NotificationCompat.PRIORITY_DEFAULT,
                    true,
                    pendingIntent
                    );
        }
    }
}
