package com.longwu.appcode.wallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import com.longwu.appcode.R;

public class ImageSensorWallpaperService extends WallpaperService {

    private String TAG = "ImageWallpaperService";
    private static Bitmap bitmap;
    private static Bitmap bitmapForground;

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
        private float xOffset2 = 0.5f;
        private float yOffset2 = 0.5f;

        GyroscopeWallpaperEngine() {
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            Bitmap originalBitmapForground = BitmapFactory.decodeResource(getResources(), R.mipmap.huoying2);

            if (originalBitmapForground != null) {
                int width = originalBitmapForground.getWidth() / 2;
                int height = originalBitmapForground.getHeight() / 2;
                bitmapForground = Bitmap.createScaledBitmap(originalBitmapForground, width, height, true);
            } else {
                Log.w(TAG, "Foreground bitmap is null");
            }
            if (bitmapForground == null) {
                Log.w(TAG, "Foreground bitmap is null");
            }
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

            xOffset2 -= event.values[0] * 0.005f;
            yOffset2 += event.values[1] * 0.005f;

//            Log.w(TAG, "onSensorChanged x: " + event.values[0] + " y: " + event.values[1] + " z: " + event.values[2]);
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

                        float xForeground = (canvas.getWidth() - bitmapForground.getWidth()) * xOffset2;
                        canvas.drawBitmap(bitmapForground, (float) xForeground, 50, null);
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