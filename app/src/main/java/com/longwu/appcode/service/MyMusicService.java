package com.longwu.appcode.service;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.media.MediaBrowserServiceCompat;

import com.longwu.appcode.R;
import com.longwu.appcode.util.TargetSSupport;

import java.util.List;

public class MyMusicService extends MediaBrowserServiceCompat {
    private static final String CHANNEL_ID = "my_channel";
    private static final String CHANNEL_ID2 = "my_channel2";
    public static final int NOTIFICATION_ID = 101;
    public static final int NOTIFICATION_ID2 = 102;
    private static final String channelName = "channel_name";
    private static final String channelName2 = "channel_name2";
    private static final String description = "description";
    MediaSessionCompat mediaSession;
    MediaSessionCompat mediaSession2;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("MyMusicService", "onCreate             开始了。。。。。。");
        mediaSession = new MediaSessionCompat(this, "media-session"+CHANNEL_ID);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS);
        createNotification(NOTIFICATION_ID,CHANNEL_ID,channelName,mediaSession);

//        mediaSession2 = new MediaSessionCompat(this, "media-session"+CHANNEL_ID);
//        mediaSession2.setFlags(MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS);
//        createNotification(NOTIFICATION_ID2,CHANNEL_ID2,channelName2,mediaSession2);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int aaa = intent.getIntExtra("action", 0);
        Log.e("MyMusicService", "aaa = " + aaa + "  onStartCommand");
        if (aaa == 1) {
            stop();
        }else if(aaa == 101){
            stop();
        }
        return super.onStartCommand(intent, flags, startId);

    }

    private void stop() {
        Log.e("MyMusicService", "stop");
        stopForeground(false);
        cancelNotification();
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
//        // 在这里设置媒体浏览器的根目录
//        // 例如，如果你想要提供所有音乐的列表，可以设置为 null
//        MediaBrowserCompat.MediaItem mediaItem = new MediaBrowserCompat.MediaItem(
//                new MediaDescriptionCompat.Builder()
//                        .setMediaId("root")
//                        .setTitle("All Music")
//                        .build(),
//                MediaBrowserCompat.MediaItem.FLAG_BROWSABLE);
//        List<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();
//        mediaItems.add(mediaItem);
//        result.sendResult(mediaItems);
        return new BrowserRoot("root", null);// You need to return a suitable BrowserRoot based on your actual situation
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        // Here, you need to load the music list, but we'll leave it blank for now
    }

    private void createNotification(int id, String channelId, String channelName, MediaSessionCompat mediaSession) {


        // 获取 sessionToken
        MediaSessionCompat.Token token = mediaSession.getSessionToken();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(channelId+ "   My notificationxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
                .setContentText(channelName+ "   Hello World!xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setSubText("subText")
                .setCustomHeadsUpContentView(new RemoteViews(getPackageName(), R.layout.notification_layout))
                .setCustomContentView(new RemoteViews(getPackageName(), R.layout.notification_layout))
                .setCustomBigContentView(new RemoteViews(getPackageName(), R.layout.notification_layout))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .addAction(androidx.mediarouter.R.drawable.mr_media_play_dark, "Previous", null)
                .addAction(androidx.mediarouter.R.drawable.ic_media_pause_dark, "Play", null)
                .addAction(R.drawable.shape_bg_desktop_widget2, "Next", getPendingIntent(this, 101))
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2)
                        .setMediaSession(token));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
        Log.e("MyMusicService", "createNotification  1111");
        startForeground(id, builder.build());
    }

    private PendingIntent getPendingIntent(MyMusicService myMusicService, int i) {
        Intent intent = new Intent(myMusicService, MyMusicService.class);
        intent.putExtra("action", i);
        PendingIntent pendingIntent = PendingIntent.getService(myMusicService, 0, intent, TargetSSupport.INSTANCE.pendingIntentFlagImmutable(PendingIntent.FLAG_UPDATE_CURRENT));
        return pendingIntent;
    }

    public  void cancelNotification() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("MyMusicService", "onDestroy");
       stop();
    }
}

