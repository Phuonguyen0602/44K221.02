package com.ecdue.lim.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class NotificationUtil {
    public static final String CHANNEL_ID = "com.ecdue.lim-lim";
    public static void createNotificationChannel(Context context, int importance, boolean showBadge,
                                           String name, String description)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setShowBadge(showBadge);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    public static void createNotification(Context context, int iconId, String title, String description,
                                          int priority, boolean autoCancel , PendingIntent pendingIntent)
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setSmallIcon(iconId);
        builder.setContentTitle(title);
        builder.setContentText(description);
        builder.setPriority(priority);
        builder.setAutoCancel(autoCancel);
        builder.setContentIntent(pendingIntent);
        // Pending intent here
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, builder.build());
    }
}
