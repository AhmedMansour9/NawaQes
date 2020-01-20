package com.nawaqes;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.nawaqes.Model.CountNotifications_Response;
import com.nawaqes.ViewModel.CountNotifications_ViewModel;

import me.leolin.shortcutbadger.ShortcutBadger;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

//import com.raaleat.R;

public class MyNotification {

    public static final int ID_SMALL_NOTIFICATION = 235;
    private Context mCtx;
    public MyNotification(Context mCtx) {
        this.mCtx = mCtx;
    }
    CountNotifications_ViewModel notifications;
    Notification notification;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void showSmallNotification(String title, String message, Intent intent) {


        PendingIntent resultPendingIntent =
                PendingIntent.getService(
                        mCtx,
                        ID_SMALL_NOTIFICATION,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );


        Notification.Builder mBuilder = new Notification.Builder(mCtx);
        mBuilder.setStyle(new Notification.InboxStyle()
                .addLine(message));
        notification = mBuilder.setTicker(title)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setOngoing(true)
                .build();
        NotificationManager notificationManager=(NotificationManager)mCtx.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0,mBuilder.build());

//        try {
//
//            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
//                    + "://" + mCtx.getPackageName() + "/raw/songemergency");
//
//            AudioManager manager = (AudioManager)mCtx.getSystemService(Context.AUDIO_SERVICE);
//            manager.setStreamVolume(
//                    AudioManager.STREAM_MUSIC,
//                    manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
//                    0);
//
////            Ringtone r = RingtoneManager.getRingtone(mCtx, alarmSound);
//            MediaPlayer player = MediaPlayer.create(mCtx, alarmSound);
//            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            player.setLooping(true);
//            player.start();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    private void getNewNotification() {
        notifications= ViewModelProviders.of((FragmentActivity) mCtx).get(CountNotifications_ViewModel.class);
        notifications.getData(SharedPrefManager.getInstance(mCtx).getDeviceToken(),mCtx)
                .observe((LifecycleOwner) mCtx, new Observer<CountNotifications_Response>() {
                    @Override
                    public void onChanged(@Nullable CountNotifications_Response tripsData) {
                        if(tripsData!=null) {
                            if(tripsData.getData()!=0){
                                ShortcutBadger.applyNotification(getApplicationContext(), notification, tripsData.getData());

                            }
                        }

                    }
                });

    }
    public void showSmallNotificati(String title, String message, Intent intent) {
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mCtx,
                        ID_SMALL_NOTIFICATION,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            makeNotificationChannel("CHANNEL_1", "Example channel", NotificationManager.IMPORTANCE_DEFAULT);
            Notification.Builder mBuilder = new Notification.Builder(mCtx,"CHANNEL_1");
            notification = mBuilder.setTicker(title).setWhen(0)
                    .setAutoCancel(true)
                    .setContentIntent(resultPendingIntent)
                    .setContentTitle(title)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.mipmap.ic_launcher))
                    .setContentText(message)
                    .setStyle(new Notification.BigTextStyle().bigText(message))
                    .build();
            try {
                Uri notificatio = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(mCtx, notificatio);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
            notification.flags |= Notification.FLAG_AUTO_CANCEL;

            NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(ID_SMALL_NOTIFICATION, notification);

        }else {
            Notification.Builder mBuilder = new Notification.Builder(mCtx);
            notification = mBuilder.setTicker(title).setWhen(0)
                    .setAutoCancel(true)
                    .setContentIntent(resultPendingIntent)
                    .setContentTitle(title)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.mipmap.ic_launcher))
                    .setContentText(message)
                    .setStyle(new Notification.BigTextStyle().bigText(message))
                    .build();

            try {
                Uri notificatio = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(mCtx, notificatio);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
            notification.flags |= Notification.FLAG_AUTO_CANCEL;

            NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(ID_SMALL_NOTIFICATION, notification);

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void makeNotificationChannel(String id, String name, int importance)
    {
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.setShowBadge(true); // set false to disable badges, Oreo exclusive

        NotificationManager notificationManager =
                (NotificationManager)mCtx.getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
    }
}

