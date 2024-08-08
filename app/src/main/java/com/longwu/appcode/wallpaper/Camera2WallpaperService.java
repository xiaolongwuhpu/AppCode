package com.longwu.appcode.wallpaper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Arrays;

public class Camera2WallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new CameraEngine();
    }

    class CameraEngine extends Engine {
        private CameraDevice cameraDevice;
        private CameraCaptureSession captureSession;
        private CaptureRequest.Builder previewRequestBuilder;
        private Handler backgroundHandler;
        private HandlerThread backgroundThread;
        private Surface previewSurface;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            startBackgroundThread();

            surfaceHolder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    previewSurface = holder.getSurface();
                    openCamera();
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    closeCamera();
                }
            });
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            stopBackgroundThread();
            closeCamera();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (visible) {
                startBackgroundThread();
                if (previewSurface != null) {
                    openCamera();
                }
            } else {
                closeCamera();
                stopBackgroundThread();
            }
        }

        private void startBackgroundThread() {
            backgroundThread = new HandlerThread("CameraBackground");
            backgroundThread.start();
            backgroundHandler = new Handler(backgroundThread.getLooper());
        }

        private void stopBackgroundThread() {
            if (backgroundThread != null) {
                backgroundThread.quitSafely();
                try {
                    backgroundThread.join();
                    backgroundThread = null;
                    backgroundHandler = null;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @SuppressLint("MissingPermission")
        private void openCamera() {
            CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            try {
                String cameraId = manager.getCameraIdList()[0];
                manager.openCamera(cameraId, stateCallback, backgroundHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        private void closeCamera() {
            if (captureSession != null) {
                captureSession.close();
                captureSession = null;
            }
            if (cameraDevice != null) {
                cameraDevice.close();
                cameraDevice = null;
            }
        }

        private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
            @Override
            public void onOpened(CameraDevice camera) {
                cameraDevice = camera;
                startPreview();
            }

            @Override
            public void onDisconnected(CameraDevice camera) {
                if (camera != null) {
                    camera.close();
                }
                cameraDevice = null;
            }

            @Override
            public void onError(CameraDevice camera, int error) {
                if (camera != null) {
                    camera.close();
                }
                cameraDevice = null;
            }
        };

        private void startPreview() {
            try {
                previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                previewRequestBuilder.addTarget(previewSurface);

                cameraDevice.createCaptureSession(Arrays.asList(previewSurface), new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(CameraCaptureSession session) {
                        if (cameraDevice == null) {
                            return;
                        }
                        captureSession = session;
                        try {
                            previewRequestBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
                            captureSession.setRepeatingRequest(previewRequestBuilder.build(), captureCallback, backgroundHandler);
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onConfigureFailed(CameraCaptureSession session) {
                        Log.e("CameraEngine", "Configuration failed");
                    }
                }, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        private final CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {
            @Override
            public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                super.onCaptureCompleted(session, request, result);
            }
        };
    }
}