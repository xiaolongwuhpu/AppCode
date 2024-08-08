package com.longwu.appcode.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.opengl.GLSurfaceView
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Surface
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
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
import javax.microedition.khronos.opengles.GL10

class CameraGLSurfaceActivity : AppCompatActivity() {
    private lateinit var glSurfaceView: GLSurfaceView
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_gl)

        glSurfaceView = findViewById(R.id.surfaceView)
        glSurfaceView.setEGLContextClientVersion(2)
        glSurfaceView.setRenderer(object : GLSurfaceView.Renderer {
            override fun onSurfaceCreated(gl: GL10?, config: javax.microedition.khronos.egl.EGLConfig?) {}

            override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {}

            override fun onDrawFrame(gl: GL10) {}
        })

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1001)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                bindPreview(cameraProvider)
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview = Preview.Builder().build()

        preview.setSurfaceProvider { request: SurfaceRequest ->
            val resolution = request.resolution
            val surfaceTexture = glSurfaceView.surfaceControl
            val surface = Surface(surfaceTexture)
            request.provideSurface(surface, ContextCompat.getMainExecutor(this)) { result: SurfaceRequest.Result? -> }
        }

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(this, cameraSelector, preview)

        adjustAspectRatio(preview)
    }

    @SuppressLint("RestrictedApi")
    private fun adjustAspectRatio(preview: Preview) {
        glSurfaceView.post {
            val resolution = preview.attachedSurfaceResolution
            Log.d("CameraGLSurfaceActivity", "resolution: $resolution width: ${resolution?.width} height: ${resolution?.height}")
            val width:Int = resolution?.width ?: 1080
            val height:Int = resolution?.height ?: 1920

            val viewWidth = glSurfaceView.width
            val viewHeight = glSurfaceView.height

            val aspectRatio = width.toFloat() / height
            val newWidth: Int
            val newHeight: Int
            if (viewWidth > viewHeight * aspectRatio) {
                newWidth = (viewHeight * aspectRatio).toInt()
                newHeight = viewHeight
            } else {
                newWidth = viewWidth
                newHeight = (viewWidth / aspectRatio).toInt()
            }

            val params = FrameLayout.LayoutParams(newWidth, newHeight)
            params.gravity = Gravity.CENTER
            glSurfaceView.layoutParams = params
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 1001 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}