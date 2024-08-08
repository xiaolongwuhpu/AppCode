package com.longwu.appcode.wallpaper;

import android.graphics.Canvas;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class VideoWallpaperService extends WallpaperService {
    private static Uri videoUri;

    public static void setVideoUri(Uri uri) {
        videoUri = uri;
    }

    @Override
    public Engine onCreateEngine() {
        return new VideoEngine();
    }


    private class VideoEngine extends Engine implements TextureView.SurfaceTextureListener {
        private MediaPlayer mediaPlayer;
        private TextureView textureView;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            Log.e("VideoEngine", "onCreate");
            textureView = new TextureView(getApplicationContext());
            textureView.setSurfaceTextureListener(this);
            textureView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                    WindowManager.LayoutParams.MATCH_PARENT,
//                    WindowManager.LayoutParams.MATCH_PARENT,
//                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
//                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                    PixelFormat.TRANSLUCENT);
//            windowManager.addView(textureView, params);



            // Create a FrameLayout and add the TextureView to it
            FrameLayout frameLayout = new FrameLayout(getApplicationContext());
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            frameLayout.addView(textureView);
// Add the FrameLayout to the SurfaceHolder's Surface
            Surface surface = surfaceHolder.getSurface();
            if (surface != null && surface.isValid()) {
                Canvas canvas = surfaceHolder.lockCanvas();
                if (canvas != null) {
                    frameLayout.draw(canvas);
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.e("VideoEngine", "onSurfaceTextureAvailable");
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(getApplicationContext(), videoUri);
                mediaPlayer.setSurface(new Surface(surface));
                mediaPlayer.setLooping(true);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            Log.e("VideoEngine", "onSurfaceTextureSizeChanged " + width + " " + height);
            // Adjust the TextureView to fill the screen
            textureView.setScaleX((float) width / mediaPlayer.getVideoWidth());
            textureView.setScaleY((float) height / mediaPlayer.getVideoHeight());
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            Log.e("VideoEngine", "onSurfaceTextureDestroyed ");
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            Log.e("VideoEngine", "onVisibilityChanged visible "+visible + " ; mediaplayer:" + mediaPlayer  );
            if (mediaPlayer != null) {
                if (visible) {
                    mediaPlayer.start();
                } else {
                    mediaPlayer.pause();
                }
            }
        }
    }
}