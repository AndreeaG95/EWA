package com.andreea.ewa.BradcastReceiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.andreea.ewa.healthPage.MedicineActivity;
import com.andreea.ewa.R;

/**
 * Created by andreeagb on 6/7/2018.
 */

public class MedicineAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MedicineActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Activity that will be triggered when user clicks the notification.
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builde = new NotificationCompat.Builder(context).setSmallIcon(R.mipmap.care)
                .setContentTitle("Take your pills")
                .setContentText("Don't forget to take your pills.")
                .setSound(alarmSound)
                .setSmallIcon(R.drawable.ic_pills)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setContentIntent(pendingIntent);
        notificationManager.notify(0, builde.build());
    }
}
