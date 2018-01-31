package com.js.sci.util;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.js.sci.R;
import com.js.sci.activity.IntroActivity;
import com.js.sci.constant.SCIConstants;
import com.js.sci.receiver.AlarmReceiver;

/**
 * Created by jskim on 2017. 10. 28..
 */

public class SCINotification implements SCIConstants {

    public static void notification(Context context, int id, String title, String text) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_noti);
        builder.setWhen(System.currentTimeMillis());
        builder.setTicker(title);
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setAutoCancel(true);
        builder.setContentIntent(PendingIntent.getActivity(context, 0, getIntent(context, id), PendingIntent.FLAG_UPDATE_CURRENT));
        nm.notify(id, builder.build());
    }

    public static void setAlarm(Context context, long time, int id, String text) {
        SCILog.debug("setAlarm");
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (isAlarmActivated(context, id, text) == false) {
            SCILog.debug("isAlarmActivated : false");
            am.set(AlarmManager.RTC_WAKEUP, time, getPendingIntent(context, id, text, 0));
        }
    }

    public static void resetAlarm(Context context, int id, String text) {
        SCILog.debug("resetAlarm : " + id);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = getPendingIntent(context, id, text, PendingIntent.FLAG_UPDATE_CURRENT);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    private static boolean isAlarmActivated(Context context,int id, String text) {
        PendingIntent pendingIntent = getPendingIntent(context, id, text, PendingIntent.FLAG_NO_CREATE);
        return pendingIntent != null;
    }

    private static PendingIntent getPendingIntent(Context context, int id, String text, int flag) {
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.putExtra(EXTRA_CULTCODE, id);
        alarmIntent.putExtra(EXTRA_TITLE, text);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, alarmIntent, flag);
        return pendingIntent;
    }

    private static Intent getIntent(Context context, int id) {
        Intent intent = new Intent(context, IntroActivity.class);
        intent.putExtra(EXTRA_CULTCODE, String.valueOf(id));
        return intent;
    }
}
