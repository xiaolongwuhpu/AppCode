package com.longwu.appcode.wallpaper;

import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.core.SurfaceRequest;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import com.google.common.util.concurrent.ListenableFuture;

public class CameraXWallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new CameraEngine();
    }

    class CameraEngine extends Engine implements LifecycleOwner {
        private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
        private Camera camera;
        private Preview preview;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            cameraProviderFuture = ProcessCameraProvider.getInstance(CameraXWallpaperService.this);
            setTouchEventsEnabled(true);
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
            // 处理触摸事件
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            stopPreview();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (visible) {
                startPreview();
            } else {
                stopPreview();
            }
        }

        private void startPreview() {
            cameraProviderFuture.addListener(new Runnable() {
                @Override
                public void run() {
                    try {
                        ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                        bindPreview(cameraProvider);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, ContextCompat.getMainExecutor(CameraXWallpaperService.this));
        }

        private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
            SurfaceHolder surfaceHolder = getSurfaceHolder();
            preview = new Preview.Builder().build();
            preview.setSurfaceProvider(new Preview.SurfaceProvider() {
                @Override
                public void onSurfaceRequested(@NonNull SurfaceRequest request) {
                    SurfaceHolder surfaceHolder = getSurfaceHolder();
                    request.provideSurface(surfaceHolder.getSurface(), ContextCompat.getMainExecutor(CameraXWallpaperService.this), result -> {});
                }
            });

            try {
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                cameraProvider.unbindAll();
                camera = cameraProvider.bindToLifecycle(
                        this,
                        cameraSelector,
                        preview
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private void stopPreview() {
            if (cameraProviderFuture != null) {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    cameraProvider.unbindAll();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @NonNull
        @Override
        public Lifecycle getLifecycle() {
            return new LifecycleRegistry(this);
        }
    }
}