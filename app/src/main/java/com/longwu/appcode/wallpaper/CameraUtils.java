package com.longwu.appcode.wallpaper;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.WindowManager;

import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CameraUtils {

    private static final String TAG = "CameraUtils";

    // 获取设备上所有相机的最小分辨率
    public static Size getBestResolutionForBackCamera(Context context) {
        CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        float screenAspectRatio = getScreenAspectRatio(context);
        try {
            for (String cameraId : cameraManager.getCameraIdList()) {
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_BACK) {
                    StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    if (map != null) {
                        Size[] sizes = map.getOutputSizes(ImageFormat.JPEG);
                        if (sizes != null) {
                            Log.d(TAG, "Camera " + cameraId + " JPEG sizes: " + Arrays.toString(sizes));
                            //sizes 从小到大排序
                            Arrays.sort(sizes, new Comparator<Size>() {
                                @Override
                                public int compare(Size size1, Size size2) {
                                    return Long.signum(size1.getWidth() - size2.getWidth());
                                }
                            });
                            Log.d(TAG, "Camera sort " + cameraId + " JPEG sizes: " + Arrays.toString(sizes));

                            Size bestSize = null;
                            // 选择最小的分辨率
                            for (Size size : sizes) {
                                int minSize = Math.min(size.getWidth(), size.getHeight());
                                int maxSize = Math.max(size.getWidth(), size.getHeight());
                                float ratio = (float) minSize / maxSize;
                                Log.d(TAG, "ratio " + ratio  + " width " + minSize + " height: " + maxSize);
                                if (Math.abs(ratio - screenAspectRatio) < 0.07) {
                                    bestSize =  size;
                                    return bestSize;
                                }
                            }

                            Log.d(TAG, "Camera " + cameraId + " bestSize: " + bestSize);
                            return bestSize;
                        }
                    }
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return null; // 如果没有找到
    }

    private static float getScreenAspectRatio(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getRealMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        float rate = (float) width / height;
        Log.d(TAG, "width " + width + " height: " + height + " rate: " + rate);
        return rate;
    }
}
