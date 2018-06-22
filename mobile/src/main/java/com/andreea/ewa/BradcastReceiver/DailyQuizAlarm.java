package com.andreea.ewa.BradcastReceiver;

import 	android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.NotificationManager;
import com.andreea.ewa.MainActivity;
import com.andreea.ewa.R;

import android.app.PendingIntent;
import 	android.net.Uri;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat.Builder;

/**
 * Created by andreeagb on 6/7/2018.
 */

public class DailyQuizAlarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Activity that will be triggered when user clicks the notification.
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION);
        Builder builde = new Builder(context).setSmallIcon(R.mipmap.care)
                                                .setContentTitle("Daily quiz")
                                                .setContentText("Don't forget to update your daily health status.")
                                                .setSound(alarmSound)
                                                .setAutoCancel(true)
                                                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                                .setContentIntent(pendingIntent);
        notificationManager.notify(0, builde.build());
    }
}
