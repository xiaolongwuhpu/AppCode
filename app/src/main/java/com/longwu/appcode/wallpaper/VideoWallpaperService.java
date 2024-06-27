package com.longwu.appcode.wallpaper;

import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

public class VideoWallpaperService extends WallpaperService {
    private static Uri videoUri;

    public static void setVideoUri(Uri uri) {
        videoUri = uri;
    }

    @Override
    public Engine onCreateEngine() {
        return new VideoEngine();
    }

    private class VideoEngine extends Engine implements SurfaceHolder.Callback {
        private MediaPlayer mediaPlayer;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            surfaceHolder.addCallback(this);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mediaPlayer = new MediaPlayer();
            try {
                if (videoUri != null) {
                    mediaPlayer.setDataSource(getApplicationContext(), videoUri);
                    mediaPlayer.setSurface(holder.getSurface());
                    mediaPlayer.setLooping(true);
                    mediaPlayer.prepare();
                } else {
                    Log.e("VideoEngine", "Video URI is null");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // 处理表面更改
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (mediaPlayer != null) {
                if (visible) {
                    mediaPlayer.start();
                } else {
                    mediaPlayer.pause();
                }
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
    }
}