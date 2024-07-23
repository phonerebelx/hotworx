package com.hotworx.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;


import com.hotworx.R;
import com.hotworx.activities.MainActivity;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.hotworx.global.Constants.WORKOUT_COMPLETED_NOTIFICATION_ID;

import androidx.core.app.NotificationCompat;

public class NewAlarmReceiver extends BroadcastReceiver {
    NotificationManager notificationManager;
//    Notification.Builder builder;

    public void createNotification(Context context) {
        int icon = R.drawable.after_burn;
        String channelId = "com.hotworx";

//        Intent notificationIntent = new Intent(context, MainActivity.class);
//        PendingIntent contentIntent = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
//            contentIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), notificationIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
//        } else {
//            contentIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        }

        NotificationChannel notificationChannel = new NotificationChannel(channelId, "My Notifications", NotificationManager.IMPORTANCE_HIGH);
        // Configure the notification channel.
        notificationChannel.setDescription("Channel description");
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
        notificationChannel.enableVibration(true);
        notificationManager.createNotificationChannel(notificationChannel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);

        builder.setSmallIcon(icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.after_burn))
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setOnlyAlertOnce(true)
                .setOngoing(false)
                .setChannelId(channelId)
                .setContentTitle("HOTWORX")
                .setContentText("RECORD YOUR CALORIES NOW!");

        notificationManager.notify(WORKOUT_COMPLETED_NOTIFICATION_ID, builder.build());
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        createNotification(context);
    }
}
