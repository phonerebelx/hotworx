package com.hotworx.helpers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.RemoteMessage;
import com.hotworx.R;
import com.hotworx.activities.SplashActivity;

import java.util.Map;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    public static String KEY_MESSAGE_TYPE_DEFAULT = "KEY_MESSAGE_TYPE_DEFAULT";
    BasePreferenceHelper prefHelper = new BasePreferenceHelper(this);


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Check if message contains a notification payload.
        if (remoteMessage.getData().size() > 0) {
            prefHelper.setNotificationString("navigate");

            Map<String, String> map = remoteMessage.getData();
            String title = map.get("title");
            String body = map.get("body");
            sendNotification(title, body, KEY_MESSAGE_TYPE_DEFAULT, 0, new Intent(this, SplashActivity.class));
        }



//        if (remoteMessage.getData() != null) {
//            Map<String, String> map = remoteMessage.getData();
//            String notification_type = "";
//            String title = "";
//            String body = "";
//            String booking_date = "";
//            String custom_message = "";
//            String objid = "";
//            String calendar_title="";
//            int duration=0;
//
//            notification_type = map.get("notification_type");
//            title = map.get("title");
//            body = map.get("body");
//            booking_date = map.get("booking_date");
//            custom_message = map.get("custom_message");
//            objid = map.get("objid");
//            duration = Integer.parseInt(map.get("duration"));
//            calendar_title = map.get("calendar_title");
//            sendNotification(notification_type, title, body, booking_date, custom_message, objid,calendar_title,duration);
//
//        }
    }


    @Override
    public void onNewToken(String token) {
        Log.d("Refreshed TAG", "Refreshed token: " + token);
    }



    public void sendNotification(String title, String messageBody, String channelId, int notificationId, Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra("navigateTo", "NotificationFragment");

        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT );
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        NotificationChannel channel = new NotificationChannel(channelId,
                this.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);

        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());
    }

    private void sendNotification(String notification_type, String title, String body, String booking_date, String custom_message, String objid,String calendar_title,int duration) {
        try {
            Intent intent = new Intent(this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("notification_type", notification_type);
            intent.putExtra("custom_message", custom_message);
            intent.putExtra("booking_date", booking_date);
            intent.putExtra("title", title);
            intent.putExtra("objid", objid);
            intent.putExtra("calendar_title",calendar_title);
            intent.putExtra("duration",duration);
            intent.putExtra("flag","");

            PendingIntent pendingIntent = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_ONE_SHOT);
            } else {
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            }
            String channelId = "ID: Hotworx Channel";
            String channelName = "Name: Hotworx Channel";
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder;
            notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(title)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                            .setContentText(body)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);


            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());




        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
















