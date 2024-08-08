package com.longwu.appcode.wallpaper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Context;

public class ImageWallpaperService extends WallpaperService {

    private String TAG = "ImageWallpaperService";
    private static Bitmap bitmap;

    public static void setBitmap(Bitmap uri) {
        bitmap = uri;
    }

    @Override
    public Engine onCreateEngine() {
        return new GyroscopeWallpaperEngine();
    }

    private class GyroscopeWallpaperEngine extends Engine implements SensorEventListener {
        private final SensorManager sensorManager;
        private final Sensor gyroscopeSensor;
        private final Handler handler = new Handler();
        private float xOffset = 0.5f;
        private float yOffset = 0.5f;

        GyroscopeWallpaperEngine() {
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
//            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wallpaper_image);
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_GAME);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            Log.w(TAG, "onDestroy");
            sensorManager.unregisterListener(this);
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            // 根据陀螺仪数据调整图片位置
            xOffset -= event.values[0] * 0.001f;
            yOffset += event.values[1] * 0.001f;

            Log.w(TAG, "onSensorChanged x: " + event.values[0] + " y: " + event.values[1] + " z: " + event.values[2]);
            handler.post(drawRunner);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        private final Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };

        private void draw() {
            final SurfaceHolder holder = getSurfaceHolder();
            if (holder != null && holder.getSurface().isValid()) {  // Check if the surface is valid
                Canvas canvas = null;
                try {
                    canvas = holder.lockCanvas();
                    if (canvas != null) {
                        canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
                        float x = (canvas.getWidth() - bitmap.getWidth()) * xOffset;
                        float y = (canvas.getHeight() - bitmap.getHeight()) * yOffset;
                        canvas.drawBitmap(bitmap, x, y, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.w(TAG, "Exception: " + e.getMessage());
                } finally {
                    if (canvas != null) {
                        holder.unlockCanvasAndPost(canvas);
                    }
                }
            } else {
                Log.w(TAG, "Surface is not valid, skipping draw.");
            }
        }
    }
}