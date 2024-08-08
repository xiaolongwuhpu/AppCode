package com.longwu.appcode.camera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Size
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import com.longwu.appcode.R
import java.util.concurrent.ExecutionException


class CameraSurfaceActivity : AppCompatActivity() {
    private lateinit var surfaceView: SurfaceView
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_surface)
        surfaceView = findViewById<SurfaceView>(R.id.surfaceView)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1001);
        }
    }


    private fun startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            try {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                bindPreview(cameraProvider)
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val surfaceHolder: SurfaceHolder = surfaceView.getHolder()

        val preview = Preview.Builder().build()

        preview.setSurfaceProvider { request: SurfaceRequest ->
            val resolution: Size = request.resolution
            val surface: Surface = surfaceHolder.surface
            request.provideSurface(
                surface, ContextCompat.getMainExecutor(this)
            ) { result: SurfaceRequest.Result? -> }
        }

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(this, cameraSelector, preview)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        if (requestCode == 1001 && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}