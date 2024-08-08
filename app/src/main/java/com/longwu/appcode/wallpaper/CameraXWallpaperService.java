package com.longwu.appcode.wallpaper;

import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.core.ResolutionInfo;
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
        private LifecycleRegistry lifecycleRegistry;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            lifecycleRegistry = new LifecycleRegistry(this);
            lifecycleRegistry.setCurrentState(Lifecycle.State.CREATED);
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
            lifecycleRegistry.setCurrentState(Lifecycle.State.DESTROYED);
            stopPreview();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (visible) {
                lifecycleRegistry.setCurrentState(Lifecycle.State.STARTED);
                startPreview();
            } else {
                lifecycleRegistry.setCurrentState(Lifecycle.State.CREATED);
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
            Range<Integer> targetFrameRate = new Range<>(1, 15);
            Size minResolution = CameraUtils.getBestResolutionForBackCamera(getApplicationContext()); // 获取最小分辨率
            Preview.Builder builder = new Preview.Builder()
                    .setTargetFrameRate(targetFrameRate); // 设置较低的帧率
            if (minResolution != null) {
                builder.setTargetResolution(minResolution);
            }
            preview = builder.build();
            if (preview == null) {
                throw new RuntimeException("Use case not created");
            }
            ResolutionInfo info = preview.getResolutionInfo();
            Log.d("CameraXWallpaperService", "minResolution: " + minResolution);
            preview.setSurfaceProvider(new Preview.SurfaceProvider() {
                @Override
                public void onSurfaceRequested(@NonNull SurfaceRequest request) {
                    SurfaceHolder surfaceHolder = getSurfaceHolder();
                    request.provideSurface(surfaceHolder.getSurface(), ContextCompat.getMainExecutor(CameraXWallpaperService.this), result -> {
                    });
                }
            });

            try {
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                cameraProvider.unbindAll();
                camera = cameraProvider.bindToLifecycle(
                        CameraEngine.this,
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
            return lifecycleRegistry;
        }
    }
}