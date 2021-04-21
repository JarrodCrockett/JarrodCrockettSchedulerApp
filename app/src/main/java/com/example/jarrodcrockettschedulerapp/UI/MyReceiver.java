package com.example.jarrodcrockettschedulerapp.UI;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.jarrodcrockettschedulerapp.R;

public class MyReceiver extends BroadcastReceiver {
    static int notificationId;
    String channel_id = "test";

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, intent.getStringExtra("key"),Toast.LENGTH_LONG).show();

        createNotificationChannel(context, channel_id);

            Notification n = new NotificationCompat.Builder(context, channel_id)
                    .setSmallIcon(R.drawable.ic_baseline_notifications_none_24)
                    .setContentText(intent.getStringExtra("key"))
                    .setContentTitle("Jarrod Crockett Scheduler App Notification ").build();

            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notificationId++,n);

    }

        private void createNotificationChannel(Context context, String CHANNEL_ID) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "notification channel";
                String description = "notification channel description";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);

                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        }
}